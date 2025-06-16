package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【employee(员工信息)】的数据库操作Mapper
 * @createDate 2025-06-16 17:02:52
 * @Entity com.etlpat.pojo.Employee
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    Employee selectOneByUsername(@Param("username") String username);
}




