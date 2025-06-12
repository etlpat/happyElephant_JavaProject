package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Purchase;
import com.etlpat.service.PurchaseService;
import com.etlpat.mapper.PurchaseMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【tb_purchase】的数据库操作Service实现
* @createDate 2025-06-12 02:37:55
*/
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase>
    implements PurchaseService{

}




