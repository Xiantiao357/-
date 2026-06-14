<script setup>
/**
 * 登录页面组件
 */
import { ref, reactive } from 'vue'
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage, ElCard } from 'element-plus'
import { userApi } from '@/api'
import { useRouter } from 'vue-router'

const router = useRouter()

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '用户名只能包含字母、数字、下划线和连字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)/, message: '密码需包含字母和数字', trigger: 'blur' }
  ]
}

const formRef = ref(null)
const loading = ref(false)

/**
 * 处理登录
 */
const handleLogin = async () => {
  // 表单验证
  try {
    await formRef.value.validate()
  } catch (error) {
    ElMessage.warning('请填写正确的登录信息')
    return
  }

  loading.value = true
  
  try {
    const response = await userApi.login({
      username: form.username,
      password: form.password
    })
    
    if (response.code === 200) {
      // 保存用户信息到 localStorage
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('username', response.data.username)
      localStorage.setItem('userId', response.data.userId)
      
      ElMessage.success('登录成功')
      router.push('/orders')
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    // 错误信息已由 axios 拦截器处理
  } finally {
    loading.value = false
  }
}

/**
 * 重置表单
 */
const resetForm = () => {
  form.username = ''
  form.password = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}
</script>

<template>
  <div class="login-container">
    <ElCard class="login-card">
      <!-- 标题区域 -->
      <div class="login-header">
        <h2 class="login-title">电商管理系统</h2>
        <p class="login-subtitle">欢迎登录，开始您的购物之旅</p>
      </div>
      
      <!-- 登录表单 -->
      <ElForm
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <ElFormItem label="用户名" prop="username">
          <ElInput 
            v-model="form.username" 
            placeholder="请输入用户名（3-20位字母/数字/下划线）"
            clearable
            prefix-icon="user"
            class="login-input"
          />
        </ElFormItem>
        
        <ElFormItem label="密码" prop="password">
          <ElInput 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码（6-20位，需包含字母和数字）"
            show-password
            clearable
            prefix-icon="lock"
            class="login-input"
          />
        </ElFormItem>
        
        <ElFormItem class="form-actions">
          <ElButton 
            type="primary" 
            @click="handleLogin"
            :loading="loading"
            class="login-btn"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </ElButton>
          <ElButton type="default" @click="resetForm" class="reset-btn">
            重置
          </ElButton>
        </ElFormItem>
      </ElForm>
      
      <!-- 测试账号提示 -->
      <div class="tips-card">
        <div class="tips-header">
          <span class="tips-icon">💡</span>
          <span class="tips-title">测试账号</span>
        </div>
        <div class="tips-content">
          <div class="tip-item">
            <span class="tip-label">用户名：</span>
            <span class="tip-value">admin</span>
          </div>
          <div class="tip-item">
            <span class="tip-label">密码：</span>
            <span class="tip-value">admin123</span>
          </div>
        </div>
      </div>
    </ElCard>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 420px;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  padding: 40px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 0;
}

.login-subtitle {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-form {
  margin-bottom: 24px;
}

.login-input {
  width: 100%;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.login-btn {
  flex: 1;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
}

.reset-btn {
  width: 100px;
  height: 44px;
  border-radius: 8px;
}

.tips-card {
  background: #f8fafc;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e2e8f0;
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.tips-icon {
  font-size: 18px;
}

.tips-title {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
}

.tips-content {
  padding-left: 26px;
}

.tip-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-label {
  color: #64748b;
  margin-right: 8px;
}

.tip-value {
  color: #334155;
  font-weight: 500;
  font-family: monospace;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .login-card {
    padding: 24px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .reset-btn {
    width: 100%;
  }
}
</style>
