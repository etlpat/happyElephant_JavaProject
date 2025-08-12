package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @author lenovo
 * @description 针对表【tb_follow】的数据库操作Mapper
 * @createDate 2025-08-04 11:29:16
 * @Entity com.etlpat.pojo.Follow
 */
public interface FollowMapper extends BaseMapper<Follow> {
    int deleteByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);

    int countByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);

    List<Follow> selectUserIdByFollowUserId(@Param("followUserId") Long followUserId);
}




