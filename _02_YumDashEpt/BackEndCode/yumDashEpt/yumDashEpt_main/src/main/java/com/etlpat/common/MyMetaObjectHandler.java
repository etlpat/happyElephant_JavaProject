package com.etlpat.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.etlpat.utils.ThreadLocalUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


// 公共字段自动填充
//
// (1)概念
//      MybatisPlus公共字段自动填充,也就是在插入或者更新的时候为指定字段赋予指定的值,
//      使用它的好处就是可以统一对这些字段进行处理,避免了重复代码。
//
//  (2)实现步骤
//      ①在实体类的属性上加入@TableField注解,指定自动填充的策略
//      ②按照框架要求编写元数据对象处理器,在此类中统一为公共字段赋值,此类需要实现MetaObjectHandler接口
//


@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 插入操作，自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        // 为表的公共字段统一赋值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", ThreadLocalUtil.get());// 从ThreadLocal中获取当前登录用户的id
        metaObject.setValue("updateUser", ThreadLocalUtil.get());
    }


    // 更新操作，自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        // 为表的公共字段统一赋值
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocalUtil.get());
    }
}
