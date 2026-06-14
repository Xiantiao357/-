package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public IPage<Order> findByUserId(Long userId, Page<Order> page) {
        logger.info("查询订单列表: userId={}, pageNum={}, pageSize={}", userId, page.getCurrent(), page.getSize());
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreateTime);
        
        IPage<Order> orderPage = orderMapper.selectPage(page, wrapper);
        logger.info("查询结果: total={}, records={}", orderPage.getTotal(), orderPage.getRecords().size());
        
        return orderPage;
    }

    @Override
    public IPage<Order> findByUserIdWithFilters(Long userId, Page<Order> page, String status, String startTime, String endTime) {
        logger.info("查询订单列表(带筛选): userId={}, status={}, startTime={}, endTime={}, pageNum={}, pageSize={}", 
                userId, status, startTime, endTime, page.getCurrent(), page.getSize());
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        // 用户ID过滤（必填）
        wrapper.eq(Order::getUserId, userId);
        
        // 状态筛选（可选）
        if (status != null && !status.isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        
        // 时间范围筛选（可选）
        if (startTime != null && !startTime.isEmpty()) {
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(startTime, DATE_FORMATTER);
                wrapper.ge(Order::getCreateTime, startDateTime);
            } catch (Exception e) {
                logger.warn("解析开始时间失败: {}", startTime);
            }
        }
        
        if (endTime != null && !endTime.isEmpty()) {
            try {
                LocalDateTime endDateTime = LocalDateTime.parse(endTime, DATE_FORMATTER);
                wrapper.le(Order::getCreateTime, endDateTime);
            } catch (Exception e) {
                logger.warn("解析结束时间失败: {}", endTime);
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Order::getCreateTime);
        
        IPage<Order> orderPage = orderMapper.selectPage(page, wrapper);
        logger.info("查询结果: total={}, records={}", orderPage.getTotal(), orderPage.getRecords().size());
        
        return orderPage;
    }
}
