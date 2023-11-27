<template>
  <div>
    <!-- 垂直水平居中、浮动布局 -->
    <div class="login-container">
      <div style="width: 400px" class="login-box">
        <div style="font-weight: bold;font-size: 24px;text-align: center;margin-bottom: 30px">登 陆</div>
        <el-form :model="data.form" ref="formRef" :rules="rules">
          <!-- 账号表单 -->
          <el-form-item prop="username">
            <el-input prefix-icon="User" v-model="data.form.username" placeholder="请输入账号"></el-input>
          </el-form-item>
          <!-- 密码表单 -->
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">登 陆</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-top: 30px;text-align: right;font-size: 15px">
          还没有账号？请<a href="/register">注册</a>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
// 需要导入 reactive
import {reactive, ref} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  form: {}
})

const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
})


const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem('student-user', JSON.stringify(res.data))
          ElMessage.success('登陆成功')
          console.log(res.data)
          // 跳转到主页
          router.push('/home')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("@/assets/imgs/bg2.jpg");
  background-size: cover;
}

.login-box {
  border-radius: 15px;
  background-color: rgba(255, 255, 255, 0.83);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
}
</style>