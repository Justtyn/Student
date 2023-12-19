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
      <el-button type="primary" plain="true" style="margin-left: 10px" @click="load">查 询</el-button>
      <el-button type="info" plain="true" @click="reload">重 置</el-button>
    </div>

    <!--    表单栏-->
    <div class="card" style="margin-bottom: 10px">

      <div>
        <el-table :data="data.tableData" style="width: 100%" v-loading="loading(data.loading)">
          <el-table-column prop="id" label="课程ID" width="80"/>
          <el-table-column prop="name" label="课程名称"/>
          <el-table-column prop="no" label="课程编号"/>
          <el-table-column prop="descr" label="课程描述"/>
          <el-table-column prop="times" label="课时"/>
          <el-table-column prop="teacher" label="任课老师"/>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" plain="true" @click="selectCourse(scope.row)">选课</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!--    分页栏-->
    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

  </div>
</template>

<script setup>
import {ref} from 'vue'
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const loading = (loading) => {
  ref(loading)
}

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
  loading: false,
  student: JSON.parse(localStorage.getItem('student-user') || '{}')
})

const load = () => {
  data.loading = true
  request.get('/api/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      no: data.no,
      teacher: data.teacher,
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
    console.log(res)
  })
}
// 调用方法获取后台数据
load()

const reload = () => {
  data.name = ''
  data.no = ''
  data.teacher = ''
  load()
}

const selectCourse = (row) => {
  request.post('/api/studentCourse/add', {
    studentId: data.student.id,
    name: row.name,
    no: row.no,
    courseId: row.id
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>