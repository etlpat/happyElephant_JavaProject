package com.etlpat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// PageBean：用于存储分页查询的结果
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private Integer pageNum;// 第几页
    private Integer pageSize;// 每页大小
    private Long total;// 总条数
    private List<T> items;// 本页数据列表
}
