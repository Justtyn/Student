<template>
  <div>
    <!--    查询栏-->
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 200px; margin-right: 10px" v-model="data.name" placeholder="请输入课程名称以查询"
                :prefix-icon="Search"/>
      <el-input style="width: 200px; margin-right: 10px" v-model="data.no" placeholder="请输入课程编号以查询"
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
          <el-table-column prop="studentName" label="学生名称"/>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" plain="true" @click="addGrade(scope.row)"
                         v-if="data.user.role ==='ADMIN'">评分
              </el-button>
              <el-button type="danger" size="small" plain="true" @click="del(scope.row.id)">取消</el-button>
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

    <el-dialog width="35%" v-model="data.formVisible" title="成绩信息">
      <el-form :model="data.gradeForm" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称">
          <el-input v-model="data.gradeForm.name" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="分数">
          <el-input v-model="data.gradeForm.score" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="评语">
          <el-input type="textarea" v-model="data.gradeForm.comment" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.formVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {ref} from 'vue'
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {handleCurrentChange} from "element-plus/es/components/tree/src/model/util";

const loading = (loading) => {
  ref(loading)
}

const data = reactive({
  no: '',
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
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  gradeForm: {},
  formVisible: false
})

const load = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    name: data.name,
    no: data.no,
  }
  if (data.user.role === 'STUDENT') {
    params.studentId = data.user.id
  }
  data.loading = true
  request.get('/api/studentCourse/selectPage', {
    params: params
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
  load()
}

const del = (id) => {
  ElMessageBox.confirm('取消后无法恢复，确认要取消嘛？', '确认取消', {type: 'warning'}).then(res => {
    request.delete('/api/studentCourse/delete/' + id).then(res => {
      if (res.code === '200') {
        load()  // 重新获取数据
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
    ElMessage({
      type: 'info',
      message: '删除已取消'
    })
  })
}

// 打分
const addGrade = (row) => {
  data.formVisible = true
  data.gradeForm.name = row.name
  data.gradeForm.courseId = row.courseId
  data.gradeForm.studentId = row.studentId
}

const save = () => {
  request.post('/grade/add', data.gradeForm).then(res => {
    if (res.code === '200') {
      data.formVisible = false  // 关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>