package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Employee;
import com.etlpat.service.EmployeeService;
import com.etlpat.mapper.EmployeeMapper;
import com.etlpat.utils.PageQueryUtil;
import com.mysql.cj.util.StringUtils;
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


    // 根据name分页查询
    @Override
    public Page<Employee> getPageByName(Integer page, Integer pageSize, String name) {
        // 当关键字不为null或""，进行模糊查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(!StringUtils.isNullOrEmpty(name), Employee::getName, name)
                .orderByDesc(Employee::getUpdateTime);

        return PageQueryUtil.getPage(page, pageSize, wrapper, employeeMapper);
    }
}




