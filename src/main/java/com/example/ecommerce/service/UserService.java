package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户对象（登录成功）或 null（登录失败）
     */
    User login(String username, String password);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册后的用户对象
     */
    User register(User user);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
    
    /**
     * 根据用户ID查找用户
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(Long id);
}
