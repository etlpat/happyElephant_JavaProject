package com.etlpat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// 滚动分页的结果【关注推送（Feed流）】
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollResult {
    private List<?> list;// 分页的博客列表
    private Long minTime;// 本次查询博客的最小时间戳
    private Integer offset;// 下次查询的偏移量
}
