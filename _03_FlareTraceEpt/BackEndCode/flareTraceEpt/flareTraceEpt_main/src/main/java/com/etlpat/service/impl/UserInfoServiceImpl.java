package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.UserInfo;
import com.etlpat.service.UserInfoService;
import com.etlpat.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;


/**
 * @author lenovo
 * @description 针对表【tb_user_info】的数据库操作Service实现
 * @createDate 2025-08-04 11:30:08
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

}




