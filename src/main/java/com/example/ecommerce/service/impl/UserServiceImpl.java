package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户登录
     * 使用BCrypt验证密码
     */
    @Override
    public User login(String username, String password) {
        logger.info("登录尝试: username={}", username);
        
        // 查询用户
        User user = userMapper.selectByUsername(username);
        
        if (user == null) {
            logger.warn("用户不存在: username={}", username);
            return null;
        }
        
        logger.debug("用户查询成功: userId={}, username={}", user.getId(), user.getUsername());
        
        // 验证密码
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        
        if (matches) {
            logger.info("密码验证成功: username={}", username);
            return user;
        }
        
        logger.warn("密码验证失败: username={}", username);
        return null;
    }

    /**
     * 用户注册
     * 密码使用BCrypt加密存储
     */
    @Override
    public User register(User user) {
        logger.info("注册请求: username={}", user.getUsername());
        
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warn("用户名已存在: username={}", user.getUsername());
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 插入数据库
        userMapper.insert(user);
        
        logger.info("注册成功: userId={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    /**
     * 根据用户名查找用户
     */
    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据用户ID查找用户
     */
    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
}
