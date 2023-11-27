<template>
  <div>
    <div class="card">
      <el-input style="width: 260px" v-model="data.name" placeholder="请输入课程名称以查询" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px">查询</el-button>
      <el-button type="info">重置</el-button>
    </div>

    <div class="card">

    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue";

const data = reactive({
  name: ''
})
</script>