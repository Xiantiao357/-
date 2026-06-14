/**
 * Axios 配置
 * 配置请求拦截器和响应拦截器
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
instance.interceptors.request.use(
  config => {
    // 添加 Token 到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 添加请求日志
    console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`, config.data || '')
    
    return config
  },
  error => {
    console.error('[API Error] 请求配置错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  response => {
    // 添加响应日志
    console.log(`[API Response] ${response.config.url}`, response.data)
    
    // 如果响应状态不是 200，显示错误信息
    if (response.data && response.data.code !== 200) {
      ElMessage.error(response.data.message || '请求失败')
      return Promise.reject(response.data)
    }
    
    return response.data
  },
  error => {
    console.error('[API Error] 响应错误:', error)
    
    // 处理不同类型的错误
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // Token 无效或过期，清除本地存储并跳转登录
          localStorage.removeItem('token')
          localStorage.removeItem('username')
          localStorage.removeItem('userId')
          ElMessage.error('登录已过期，请重新登录')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      // 请求配置出错
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default instance
