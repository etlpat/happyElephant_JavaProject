package com.etlpat.controller;

import com.etlpat.pojo.AddressBook;
import com.etlpat.pojo.R;
import com.etlpat.service.AddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 前台客户端 -- 地址表
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;


    // 添加地址
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        addressBook.setCreateUser(userId);
        addressBook.setUpdateUser(userId);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }


    // 更新地址
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUpdateUser(userId);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    // 删除地址
    @DeleteMapping
    public R remove(Long ids) {
        addressBookService.removeById(ids);
        return R.success();
    }


    // 设置默认地址
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        // (1)把该用户的全部地址的is_default字段设为0
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBookService.setIsDefaultByUserId(0, userId);
        // (2)把指定地址的is_default字段设为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    // 根据id查询地址
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id) {
        AddressBook byId = addressBookService.getById(id);
        if (byId == null) {
            return R.error("地址不存在");
        }
        return R.success(byId);
    }


    // 查询默认地址
    @GetMapping("default")
    public R<AddressBook> getDefault(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        AddressBook isDefault = addressBookService.selectOneByUserIdAndIsDefault(userId, 1);
        if (isDefault == null) {
            return R.error("地址不存在");
        }
        return R.success(isDefault);
    }


    // 查询指定用户的全部地址
    @GetMapping("/list")
    public R<List<AddressBook>> getList(AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        List<AddressBook> list = addressBookService.getList(addressBook);
        return R.success(list);
    }

}
