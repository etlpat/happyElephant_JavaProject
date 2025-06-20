package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【address_book(地址管理)】的数据库操作Mapper
 * @createDate 2025-06-20 12:27:07
 * @Entity com.etlpat.pojo.AddressBook
 */
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    int setIsDefaultByUserId(@Param("isDefault") Integer isDefault, @Param("userId") Long userId);

    AddressBook selectOneByUserIdAndIsDefault(@Param("userId") Long userId, @Param("isDefault") Integer isDefault);
}




