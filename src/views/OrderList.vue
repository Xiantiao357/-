<script setup>
/**
 * 订单列表页面组件
 */
import { ref, onMounted, computed, reactive } from 'vue'
import { 
  ElTable, 
  ElTableColumn, 
  ElPagination, 
  ElButton, 
  ElMessage, 
  ElTag,
  ElSelect,
  ElOption,
  ElDatePicker,
  ElInput,
  ElCard
} from 'element-plus'
import { orderApi } from '@/api'
import { useRouter } from 'vue-router'

const router = useRouter()

// 数据
const orders = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const hasError = ref(false)
const errorMessage = ref('')

// 筛选条件
const filters = reactive({
  status: 'ALL',
  startTime: '',
  endTime: ''
})

// 订单状态映射
const statusMap = {
  'PENDING': '待支付',
  'SHIPPED': '已发货',
  'COMPLETED': '已完成',
  'CANCELLED': '已取消'
}

// 订单状态颜色映射
const statusColor = {
  'PENDING': 'warning',
  'SHIPPED': 'info',
  'COMPLETED': 'success',
  'CANCELLED': 'danger'
}

// 计算用户名
const username = computed(() => localStorage.getItem('username') || '用户')

/**
 * 获取订单列表
 */
const fetchOrders = async (resetPage = false) => {
  if (resetPage) {
    pageNum.value = 1
  }
  
  loading.value = true
  hasError.value = false
  errorMessage.value = ''
  
  try {
    const userId = Number(localStorage.getItem('userId'))
    
    if (!userId) {
      ElMessage.error('用户未登录，请重新登录')
      router.push('/login')
      return
    }
    
    // 构建查询参数
    const params = {
      userId: userId,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    
    // 添加筛选条件
    if (filters.status && filters.status !== 'ALL') {
      params.status = filters.status
    }
    if (filters.startTime) {
      params.startTime = filters.startTime
    }
    if (filters.endTime) {
      params.endTime = filters.endTime
    }
    
    const response = await orderApi.getOrderList(params)
    
    if (response.code === 200) {
      orders.value = response.data.records || []
      total.value = response.data.total || 0
      
      if (orders.value.length === 0) {
        ElMessage.info('暂无订单数据')
      }
    } else {
      errorMessage.value = response.message || '获取订单失败'
      hasError.value = true
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    errorMessage.value = '获取订单列表失败，请稍后重试'
    hasError.value = true
  } finally {
    loading.value = false
  }
}

/**
 * 分页切换
 */
const handlePageChange = (val) => {
  pageNum.value = val
  fetchOrders()
}

/**
 * 筛选条件变更
 */
const handleFilterChange = () => {
  fetchOrders(true)
}

/**
 * 重置筛选条件
 */
const resetFilters = () => {
  filters.status = 'ALL'
  filters.startTime = ''
  filters.endTime = ''
  fetchOrders(true)
}

/**
 * 退出登录
 */
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('userId')
  ElMessage.success('退出成功')
  router.push('/login')
}

/**
 * 格式化日期
 */
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchOrders()
})
</script>

<template>
  <div class="order-container">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="logo">电商管理系统</div>
      <div class="header-right">
        <span class="username">欢迎，{{ username }}</span>
        <ElButton type="text" @click="handleLogout">退出登录</ElButton>
      </div>
    </header>
    
    <!-- 主内容区域 -->
    <main class="main-content">
      <ElCard class="page-card">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1 class="page-title">订单管理</h1>
        </div>
        
        <!-- 筛选区域 -->
        <div class="filter-section">
          <div class="filter-row">
            <!-- 订单状态筛选 -->
            <div class="filter-item">
              <label>订单状态</label>
              <ElSelect 
                v-model="filters.status" 
                placeholder="全部"
                class="filter-select"
                @change="handleFilterChange"
              >
                <ElOption label="全部" value="ALL" />
                <ElOption label="待支付" value="PENDING" />
                <ElOption label="已发货" value="SHIPPED" />
                <ElOption label="已完成" value="COMPLETED" />
                <ElOption label="已取消" value="CANCELLED" />
              </ElSelect>
            </div>
            
            <!-- 时间范围筛选 -->
            <div class="filter-item">
              <label>开始时间</label>
              <ElDatePicker
                v-model="filters.startTime"
                type="datetime"
                placeholder="选择开始时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="handleFilterChange"
              />
            </div>
            
            <div class="filter-item">
              <label>结束时间</label>
              <ElDatePicker
                v-model="filters.endTime"
                type="datetime"
                placeholder="选择结束时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="handleFilterChange"
              />
            </div>
            
            <!-- 重置按钮 -->
            <div class="filter-item filter-actions">
              <ElButton type="default" @click="resetFilters">重置筛选</ElButton>
            </div>
          </div>
        </div>
        
        <!-- 订单列表 -->
        <div class="table-section">
          <!-- 加载中状态 -->
          <div v-if="loading" class="loading-state">
            <div class="loading-spinner"></div>
            <p>正在加载订单数据...</p>
          </div>
          
          <!-- 错误状态 -->
          <div v-else-if="hasError" class="error-state">
            <div class="error-icon">✗</div>
            <p>{{ errorMessage }}</p>
            <ElButton type="primary" @click="fetchOrders">重新加载</ElButton>
          </div>
          
          <!-- 空数据状态 -->
          <div v-else-if="orders.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">📦</div>
            <p>暂无订单数据</p>
            <p class="empty-hint">您还没有任何订单记录</p>
          </div>
          
          <!-- 订单表格 -->
          <ElTable 
            v-else
            :data="orders" 
            :loading="loading" 
            border 
            style="width: 100%"
            class="order-table"
          >
            <ElTableColumn prop="id" label="订单ID" width="80" align="center" />
            <ElTableColumn prop="orderNo" label="订单编号" min-width="180" />
            <ElTableColumn prop="totalAmount" label="订单金额" width="120" align="center">
              <template #default="scope">
                <span class="amount">¥{{ scope.row.totalAmount?.toFixed(2) }}</span>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="status" label="订单状态" width="120" align="center">
              <template #default="scope">
                <ElTag :type="statusColor[scope.row.status]" size="small">
                  {{ statusMap[scope.row.status] || scope.row.status }}
                </ElTag>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="createTime" label="下单时间" width="180" align="center">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </ElTableColumn>
          </ElTable>
        </div>
        
        <!-- 分页 -->
        <div v-if="total > 0" class="pagination-container">
          <ElPagination
            v-model:current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
            layout="total, prev, pager, next, jumper"
            background
          />
        </div>
      </ElCard>
    </main>
  </div>
</template>

<style scoped>
.order-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.header {
  background: white;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #667eea;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.username {
  color: #666;
  font-size: 14px;
}

.main-content {
  flex: 1;
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.page-card {
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.page-header {
  padding-bottom: 20px;
  border-bottom: 1px solid #e8e8e8;
  margin-bottom: 20px;
}

.page-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.filter-section {
  background: #fafafa;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-item label {
  font-size: 13px;
  color: #666;
}

.filter-select {
  width: 150px;
}

.filter-actions {
  flex-direction: row;
  margin-left: auto;
}

.table-section {
  min-height: 300px;
}

.loading-state,
.error-state,
.empty-state {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 60px 20px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon {
  font-size: 48px;
  color: #f56c6c;
  margin-bottom: 16px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-hint {
  color: #999;
  font-size: 14px;
  margin-top: 8px;
}

.order-table {
  font-size: 14px;
}

.amount {
  color: #f56c6c;
  font-weight: bold;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .header {
    padding: 12px 16px;
  }
  
  .logo {
    font-size: 16px;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .filter-row {
    gap: 12px;
  }
  
  .filter-item {
    width: calc(50% - 6px);
  }
  
  .filter-select {
    width: 100%;
  }
  
  .filter-actions {
    width: 100%;
    margin-left: 0;
    justify-content: flex-end;
  }
}
</style>
