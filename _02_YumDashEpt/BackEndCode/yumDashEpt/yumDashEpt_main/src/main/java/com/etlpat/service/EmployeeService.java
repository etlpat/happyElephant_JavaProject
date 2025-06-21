package com.etlpat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lenovo
 * @description 针对表【employee(员工信息)】的数据库操作Service
 * @createDate 2025-06-16 17:02:52
 */
public interface EmployeeService extends IService<Employee> {
    Employee getByUsername(String username);

    Page<Employee> getPageByName(Integer page, Integer pageSize, String name);
}
