/**
 * API 接口统一管理
 */

import axios from '@/utils/axios'

/**
 * 用户相关 API
 */
export const userApi = {
  /**
   * 用户登录
   * @param {Object} data - { username, password }
   * @returns {Promise} 登录结果
   */
  login(data) {
    return axios.post('/user/login', data)
  },

  /**
   * 用户注册
   * @param {Object} data - { username, password, email }
   * @returns {Promise} 注册结果
   */
  register(data) {
    return axios.post('/user/register', data)
  },

  /**
   * 获取用户信息
   * @returns {Promise} 用户信息
   */
  getUserInfo() {
    return axios.get('/user/info')
  }
}

/**
 * 订单相关 API
 */
export const orderApi = {
  /**
   * 获取订单列表（分页）
   * @param {Object} data - { userId, pageNum, pageSize }
   * @returns {Promise} 订单列表
   */
  getOrderList(data) {
    return axios.post('/order/list', data)
  }
}

export default {
  user: userApi,
  order: orderApi
}
