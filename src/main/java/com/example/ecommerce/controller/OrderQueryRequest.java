package com.example.ecommerce.controller;

import java.time.LocalDateTime;

/**
 * 订单查询请求DTO
 */
public class OrderQueryRequest {
    
    /**
     * 用户ID（必填）
     */
    private Long userId;
    
    /**
     * 页码（默认1）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小（默认10）
     */
    private Integer pageSize = 10;
    
    /**
     * 订单状态筛选（可选）
     * 可选值：PENDING(待支付), SHIPPED(已发货), COMPLETED(已完成), CANCELLED(已取消)
     */
    private String status;
    
    /**
     * 开始时间（可选）
     */
    private String startTime;
    
    /**
     * 结束时间（可选）
     */
    private String endTime;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
