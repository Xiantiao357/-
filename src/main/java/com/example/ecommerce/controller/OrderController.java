package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单列表（分页+筛选）
     * 
     * @param userId 用户ID（从Token中提取）
     * @param params 查询参数（包含pageNum, pageSize, status, startTime, endTime）
     * @return 订单分页结果
     */
    @PostMapping("/list")
    public Result<IPage<Order>> getOrderList(
            @RequestAttribute Long userId,
            @RequestBody(required = false) Map<String, Object> params) {
        
        logger.info("订单列表查询请求: userId={}, params={}", userId, params);
        
        // 默认参数
        int pageNum = 1;
        int pageSize = 10;
        String status = null;
        String startTime = null;
        String endTime = null;
        
        // 解析参数
        if (params != null) {
            if (params.get("pageNum") != null) {
                pageNum = ((Number) params.get("pageNum")).intValue();
            }
            if (params.get("pageSize") != null) {
                pageSize = ((Number) params.get("pageSize")).intValue();
            }
            status = (String) params.get("status");
            startTime = (String) params.get("startTime");
            endTime = (String) params.get("endTime");
        }
        
        // 创建分页对象
        Page<Order> page = new Page<>(pageNum, pageSize);
        
        // 查询订单（支持筛选）
        IPage<Order> orderPage = orderService.findByUserIdWithFilters(userId, page, status, startTime, endTime);
        
        logger.info("订单列表查询成功: total={}, records={}", orderPage.getTotal(), orderPage.getRecords().size());
        
        return Result.success(orderPage);
    }
}
