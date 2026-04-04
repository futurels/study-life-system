<template>
  <div class="login-page">
    <div class="login-panel">
      <div class="login-copy">
        <p class="eyebrow">Study Life System</p>
        <h1>记录计划、执行与成长</h1>
        <p>
          当前系统已完成登录鉴权基础能力，第二阶段继续接入学习计划模块，让首页和学习计划页都能真正联动后端数据。
        </p>
      </div>

      <el-card shadow="never" class="login-card">
        <template #header>
          <div class="card-header">
            <span>账号登录</span>
          </div>
        </template>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @keyup.enter="handleLogin">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>

          <el-form-item>
            <el-button class="submit-btn" type="primary" :loading="loading" @click="handleLogin">
              立即登录
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }
  loading.value = true
  try {
    await authStore.login(form)
    await authStore.fetchProfile()
    ElMessage.success('登录成功')
    router.push('/study-life/dashboard')
  } finally {
    loading.value = false
  }
}
</script>
