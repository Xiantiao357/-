package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.entity.Order;

import java.time.LocalDateTime;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 根据用户ID查询订单（分页）
     * @param userId 用户ID
     * @param page 分页参数
     * @return 订单分页结果
     */
    IPage<Order> findByUserId(Long userId, Page<Order> page);
    
    /**
     * 根据用户ID查询订单（分页+筛选）
     * @param userId 用户ID
     * @param page 分页参数
     * @param status 订单状态（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 订单分页结果
     */
    IPage<Order> findByUserIdWithFilters(Long userId, Page<Order> page, String status, String startTime, String endTime);
}
