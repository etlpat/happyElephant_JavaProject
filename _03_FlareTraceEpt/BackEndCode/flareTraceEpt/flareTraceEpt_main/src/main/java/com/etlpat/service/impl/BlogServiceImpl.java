package com.etlpat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.Result;
import com.etlpat.dto.ScrollResult;
import com.etlpat.dto.UserDTO;
import com.etlpat.mapper.FollowMapper;
import com.etlpat.pojo.Blog;
import com.etlpat.pojo.Follow;
import com.etlpat.pojo.User;
import com.etlpat.service.BlogService;
import com.etlpat.mapper.BlogMapper;
import com.etlpat.service.UserService;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.SystemConstants;
import com.etlpat.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author lenovo
 * @description 针对表【tb_blog】的数据库操作Service实现
 * @createDate 2025-08-04 11:26:03
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 分页查询博客信息
    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 设置博客中的用户信息
        records.forEach(blog -> {
            queryAndSetBlogUser(blog);
            queryAndSetBlogIsLike(blog);
        });
        return Result.ok(records);
    }


    // 根据id查询博客
    @Override
    public Result queryBlogById(Long id) {
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("博客不存在！");
        }
        // 设置博客中的用户信息
        queryAndSetBlogUser(blog);
        // 查询并设置博客是否被当前用户点赞
        queryAndSetBlogIsLike(blog);
        return Result.ok(blog);
    }


    // 查询并设置博客中的用户信息
    private void queryAndSetBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }


    // 查询并设置博客是否被当前用户点赞
    private void queryAndSetBlogIsLike(Blog blog) {
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            return;
        }
        // 判断当前登录用户是否点过赞
        String userId = user.getId().toString();
        String likedKey = RedisConstants.BLOG_LIKED_KEY + blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(likedKey, userId);
        blog.setIsLike(score == null ? Boolean.FALSE : Boolean.TRUE);
    }


    // 为博客点赞
    @Override
    public Result likeBlog(Long id) {
        // 1.判断当前登录用户是否点过赞
        String userId = UserHolder.getUser().getId().toString();
        String likedKey = RedisConstants.BLOG_LIKED_KEY + id;
        // todo 判断某key是否在sortedSet中：查询key的分数score，若key不存在，则score为null；若score不为null，则key一定存在。
        Double score = stringRedisTemplate.opsForZSet().score(likedKey, userId);

        // 若分数为null => 用户不在sortedSet中 => 未点过赞
        if (score == null) {
            // 2.若未点赞，则可以点赞 -> 保存当前用户到Redis的sortedSet集合中；使数据库点赞数+1
            // 保存用户到Redis的sortedSet集合：zadd key value score
            stringRedisTemplate.opsForZSet().add(likedKey, userId, System.currentTimeMillis());// 将时间戳设为score
            blogMapper.incrementLiked(id);
        } else {
            // 3.若已点赞，则取消点赞 -> 把当前用户从Redis的sortedSet集合中移除；使数据库点赞数-1
            stringRedisTemplate.opsForZSet().remove(likedKey, userId);
            blogMapper.decrementLiked(id);
        }
        return Result.ok();
    }


    // 查询博客的点赞用户列表（最早的5条）
    @Override
    public Result queryBlogLikes(Long id) {
        // 1.从Redis中获取点赞最早的5个用户
        String likedKey = RedisConstants.BLOG_LIKED_KEY + id;
        // todo 从sortedSet获取指定排名范围内的元素：ZRANGE key min max  （范围:[min, max]，排名默认升序排序）
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(likedKey, 0, 4);
        if (top5 == null || top5.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }

        // 2.转化为UserDTO列表并返回
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        List<User> userList = userService.query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
        List<UserDTO> userDTOS = userList.stream().map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
        return Result.ok(userDTOS);
    }


    // 添加博客【关注推送（Feed流）】
    // TODO 功能：向数据库添加博客信息，并推送到每个粉丝的sortedSet收件箱中
    @Override
    public Result saveBlog(Blog blog) {
        // 1.将博客添加到数据库中
        Long userId = UserHolder.getUser().getId();
        blog.setUserId(userId);
        boolean flag = save(blog);
        if (!flag) {
            return Result.fail("添加博客失败！");
        }
        Long blogId = blog.getId();

        // 2.查询该博客作者的所有粉丝，并将博客推送给所有粉丝
        List<Follow> follows = followMapper.selectUserIdByFollowUserId(userId);
        for (Follow follow : follows) {
            Long fansId = follow.getUserId();// 粉丝id
            String feedKey = RedisConstants.FEED_KEY + fansId;// 粉丝的收件箱的key
            // todo 将当前的博客id推送到每个粉丝的sortedSet收件箱中
            stringRedisTemplate.opsForZSet().add(feedKey, blogId.toString(), System.currentTimeMillis());
        }
        return Result.ok(blogId);
    }


    // 滚动分页查询【关注推送（Feed流）】
    // TODO 功能：从sortedSet收件箱中，滚动分页查询关注用户推送的博客
    // 参数：max=本次查询的最大时间，offset=偏移量（要跳过的元素个数）
    @Override
    public Result queryBlogOfFollow(Long max, Integer offset) {
        // 1.滚动查询sortedSet收件箱
        Long userId = UserHolder.getUser().getId();
        String feedKey = RedisConstants.FEED_KEY + userId;// 收件箱key
        // TODO Redis指令：ZREVRANGEBYSCORE key max min LIMIT offset count
        //      功能：从分数范围[max,min]中，降序获取count个元素，偏移量为offset
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(feedKey, 0, max, offset, SystemConstants.DEFAULT_PAGE_SIZE);
        // 判空操作
        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.ok();
        }

        // 2.解析收件箱中数据（blogId、minTime（时间戳）、offset）
        List<Long> blogIds = new ArrayList<>(typedTuples.size());
        long minTime = 0;
        int offset1 = 1;
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            // ①读取博客id
            blogIds.add(Long.valueOf(tuple.getValue()));
            // ②获取时间戳、偏移量
            long time = tuple.getScore().longValue();// time原本在列表中降序排列
            if (time != minTime) {
                minTime = time;
                offset1 = 1;// 当最小时间戳变动，重置偏移量
            } else {
                offset1++;
            }
        }

        // 3.查询博客、封装数据并返回
        String blogIdStr = StrUtil.join(",", blogIds);
        List<Blog> blogs = query().in("id", blogIds).last("ORDER BY FIELD(id," + blogIdStr + ")").list();
        blogs.forEach(blog -> {
            queryAndSetBlogUser(blog);
            queryAndSetBlogIsLike(blog);
        });
        ScrollResult result = new ScrollResult(blogs, minTime, offset1);
        return Result.ok(result);
    }


}




