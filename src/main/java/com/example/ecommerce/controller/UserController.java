package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 处理用户登录、注册、信息查询等操作
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * @param request 登录请求（包含用户名和密码）
     * @return 登录结果（包含Token、用户ID、用户名）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("用户登录请求: username={}", request.getUsername());
        
        User user = userService.login(request.getUsername(), request.getPassword());
        
        if (user == null) {
            logger.warn("登录失败: 用户名或密码错误 - username={}", request.getUsername());
            return Result.error(400, "用户名或密码错误");
        }

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        logger.info("登录成功: userId={}, username={}", user.getId(), user.getUsername());

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());

        return Result.success(data);
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user) {
        logger.info("用户注册请求: username={}", user.getUsername());
        
        User registeredUser = userService.register(user);
        logger.info("注册成功: userId={}, username={}", registeredUser.getId(), registeredUser.getUsername());
        
        return Result.success("注册成功", registeredUser);
    }

    /**
     * 获取用户信息
     * @param userId 用户ID（从Token中提取）
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestAttribute Long userId) {
        logger.info("获取用户信息请求: userId={}", userId);
        
        User user = userService.findById(userId);
        if (user == null) {
            logger.warn("用户不存在: userId={}", userId);
            return Result.error(404, "用户不存在");
        }
        
        return Result.success(user);
    }

    /**
     * 登录请求类
     */
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
