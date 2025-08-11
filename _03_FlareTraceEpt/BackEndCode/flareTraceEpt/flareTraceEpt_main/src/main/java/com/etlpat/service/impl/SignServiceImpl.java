package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Sign;
import com.etlpat.service.SignService;
import com.etlpat.mapper.SignMapper;
import org.springframework.stereotype.Service;


/**
 * @author lenovo
 * @description 针对表【tb_sign】的数据库操作Service实现
 * @createDate 2025-08-04 11:30:00
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign>
        implements SignService {

}




