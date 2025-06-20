package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.AddressBook;
import com.etlpat.service.AddressBookService;
import com.etlpat.mapper.AddressBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【address_book(地址管理)】的数据库操作Service实现
 * @createDate 2025-06-20 12:27:07
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public int setIsDefaultByUserId(Integer isDefault, Long userId) {
        return addressBookMapper.setIsDefaultByUserId(isDefault, userId);
    }

    @Override
    public AddressBook selectOneByUserIdAndIsDefault(Long userId, Integer isDefault) {
        return addressBookMapper.selectOneByUserIdAndIsDefault(userId, isDefault);
    }

    @Override
    public List<AddressBook> getList(AddressBook addressBook) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId())
                .orderByDesc(AddressBook::getUpdateTime);
        return addressBookMapper.selectList(wrapper);
    }
}




