package com.etlpat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.dto.Result;
import com.etlpat.dto.UserDTO;
import com.etlpat.pojo.Blog;
import com.etlpat.service.BlogService;
import com.etlpat.utils.SystemConstants;
import com.etlpat.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;


    // 为博客点赞
    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        return blogService.likeBlog(id);
    }


    // 分页查询当前登录用户的博客列表
    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }


    // 分页查询博客信息
    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return blogService.queryHotBlog(current);
    }


    // 根据id查询博客
    @GetMapping("/{id}")
    public Result queryBlogById(@PathVariable("id") Long id) {
        return blogService.queryBlogById(id);
    }


    // 查询博客的点赞用户列表
    @GetMapping("/likes/{id}")
    public Result queryBlogLikes(@PathVariable("id") Long id) {
        return blogService.queryBlogLikes(id);
    }


    // 分页查询指定用户的博客列表
    @GetMapping("/of/user")
    public Result queryBlogByUserId(@RequestParam(value = "current", defaultValue = "1") Integer current
            , @RequestParam("id") Long id) {
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", id).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }


    // 添加博客【关注推送（Feed流）】
    // TODO 功能：向数据库添加博客信息，并推送到每个粉丝的sortedSet收件箱中
    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        return blogService.saveBlog(blog);
    }


    // 滚动分页查询【关注推送（Feed流）】
    // TODO 功能：从sortedSet收件箱中，滚动分页查询关注用户推送的博客
    @GetMapping("/of/follow")
    public Result queryBlogOfFollow(@RequestParam("lastId") Long max,// 本次查询的最大时间
                                    @RequestParam(value = "offset", defaultValue = "0") Integer offset) {// 偏移量（要跳过的元素个数）
        return blogService.queryBlogOfFollow(max, offset);
    }

}
