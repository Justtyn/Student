<template>
  <div>
    <!--    查询栏-->
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 200px; margin-right: 10px" v-model="data.name" placeholder="请输入课程名称以查询"
                :prefix-icon="Search"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.no" placeholder="请输入课程编号以查询"
                :prefix-icon="Search"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.teacher" placeholder="请输入任课老师以查询"
                :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reload">重置</el-button>
    </div>

    <!--    表单栏-->
    <div class="card" style="margin-bottom: 10px">
      <div>
        <el-button type="primary" style="margin-bottom: 10px">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="课程ID" width="80"/>
          <el-table-column prop="name" label="课程名称"/>
          <el-table-column prop="no" label="课程编号"/>
          <el-table-column prop="descr" label="课程描述"/>
          <el-table-column prop="times" label="课时"/>
          <el-table-column prop="teacher" label="任课老师"/>
          <el-table-column>
            <template #default="scope">
              <el-button type="primary" size="small" plain="true">编辑</el-button>
              <el-button type="danger" size="small" plain="true">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!--    分页栏-->
    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     @current-change="handelCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request";

const data = reactive({
  no: '',
  teacher: '',
  // 搜索栏数据
  name: '',
  // 表单数据
  tableData: [],
  // 总页数
  total: 0,
  // 每页个数
  pageSize: 5,
  // 当前页码
  pageNum: 1,
})

const load = () => {
  request.get('/api/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      no: data.no,
      teacher: data.teacher,
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
    console.log(res)
  })
}
// 调用方法获取后台数据
load()

const handelCurrentChange = (pageNum) => {
  // 翻页之后重新加载数据即可
  load()
}

const reload = () => {
  data.name = ''
  data.no = ''
  data.teacher = ''
  load()
}
</script>