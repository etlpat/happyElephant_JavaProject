package com.etlpat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Employee;
import com.etlpat.pojo.R;
import com.etlpat.service.EmployeeService;
import com.etlpat.utils.MD5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


// 后台管理端 -- 员工表
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    // 员工登录
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        // ①根据登录用户名获取员工
        Employee byUsername = employeeService.getByUsername(employee.getUsername());
        if (byUsername == null) {
            return R.error("员工不存在");
        }

        // ②判断密码是否正确
        if (!MD5Util.match(employee.getPassword(), byUsername.getPassword())) {
            return R.error("密码错误");
        }

        // ③判断员工状态是否已禁用
        if (byUsername.getStatus() == 0) {
            return R.error("员工已被禁用");
        }

        // 登录成功！
        request.getSession().setAttribute("employee", byUsername.getId());// 将当前登录员工的id存入session中
        return R.success(byUsername);
    }


    // 退出登录
    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");// 删除session中当前登录员工的id
        return R.success();
    }


    // 添加员工
    @PostMapping
    public R save(@RequestBody Employee employee) {
        employee.setPassword(MD5Util.getMD5String("123456"));// 设置初始密码
        employeeService.save(employee);
        return R.success();
    }


    // 分页查询员工信息
    @GetMapping("/page")
    public R<Page<Employee>> getPageByName(Integer page, Integer pageSize, String name) {
        Page<Employee> pageByName = employeeService.getPageByName(page, pageSize, name);
        return R.success(pageByName);
    }


    // 更新员工信息
    @PutMapping
    public R updateById(@RequestBody Employee employee) {
        employeeService.updateById(employee);
        return R.success();
    }


    // 根据id获取员工
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee byId = employeeService.getById(id);
        if (byId == null) {
            return R.error("员工不存在");
        }

        return R.success(byId);
    }

}
