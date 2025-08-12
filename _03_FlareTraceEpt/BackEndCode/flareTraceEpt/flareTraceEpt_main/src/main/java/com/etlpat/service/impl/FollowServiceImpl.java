package com.etlpat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.Result;
import com.etlpat.dto.UserDTO;
import com.etlpat.pojo.Follow;
import com.etlpat.service.FollowService;
import com.etlpat.mapper.FollowMapper;
import com.etlpat.service.UserService;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author lenovo
 * @description 针对表【tb_follow】的数据库操作Service实现
 * @createDate 2025-08-04 11:29:16
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 关注/取关指定用户
    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        Long userId = UserHolder.getUser().getId();
        String followKey = RedisConstants.FOLLOW_KEY + userId;// key:关注者 value:被关注者

        if (isFollow) {
            // todo 1.关注指定用户（新增sql），并存入Redis的set集合中
            Follow follow = new Follow(userId, followUserId);
            boolean flag = save(follow);
            if (flag) {
                stringRedisTemplate.opsForSet().add(followKey, followUserId.toString());
            }

        } else {
            // todo 2.取关指定用户（删除sql），并从Redis中删除
            int flag = followMapper.deleteByUserIdAndFollowUserId(userId, followUserId);
            if (flag != 0) {
                stringRedisTemplate.opsForSet().remove(followKey, followUserId.toString());
            }
        }
        return Result.ok();
    }


    // 判断是否关注了指定用户
    @Override
    public Result isFollow(Long followUserId) {
        Long userId = UserHolder.getUser().getId();
        String followKey = RedisConstants.FOLLOW_KEY + userId;
        // 判断该用户是否在Redis集合中
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(followKey, followUserId.toString());
        return Result.ok(isMember);
    }


    // 查询当前登录用户与参数用户的共同关注列表
    @Override
    public Result followCommons(Long id) {
        // 1.将Redis中的set关注集合求交集，获取共同关注集合
        Long userId = UserHolder.getUser().getId();
        String followKey1 = RedisConstants.FOLLOW_KEY + userId;// 登录用户的关注集合key
        String followKey2 = RedisConstants.FOLLOW_KEY + id;// 参数用户的关注集合key
        // todo 求key1、key2交集，获取共同关注的集合
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(followKey1, followKey2);// 获取set的交集
        if (intersect == null || intersect.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }

        // 2.将共同关注集合，转换为UserDTO列表后返回
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> userDTOS = userService.listByIds(ids)
                .stream().map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
        return Result.ok(userDTOS);
    }

}




