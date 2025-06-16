package com.etlpat.controller;

import com.etlpat.pojo.Employee;
import com.etlpat.pojo.R;
import com.etlpat.service.EmployeeService;
import com.etlpat.utils.MD5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
