package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Employee;
import com.etlpat.service.EmployeeService;
import com.etlpat.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2025-06-16 17:02:52
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public Employee getByUsername(String username) {
        return employeeMapper.selectOneByUsername(username);
    }

}




