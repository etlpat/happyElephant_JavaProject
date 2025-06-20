package com.etlpat.service;

import com.etlpat.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【address_book(地址管理)】的数据库操作Service
 * @createDate 2025-06-20 12:27:07
 */
public interface AddressBookService extends IService<AddressBook> {
    int setIsDefaultByUserId(Integer isDefault, Long userId);

    AddressBook selectOneByUserIdAndIsDefault(Long userId, Integer isDefault);

    List<AddressBook> getList(AddressBook addressBook);
}
