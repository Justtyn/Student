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
        <el-button type="primary" style="margin-bottom: 10px" @click="handleAdd">新 增</el-button>
      </div>

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
              <el-button type="primary" size="small" plain="true" @click="handleEdit(scope.row)">编 辑</el-button>
              <el-button type="danger" size="small" plain="true" @click="del(scope.row.id)">删 除</el-button>
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

    <el-dialog width="30%" v-model="data.formVisible" title="课程信息">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称:">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程编号:">
          <el-input v-model="data.form.no" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程描述:">
          <el-input v-model="data.form.descr" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程课时:">
          <el-input v-model="data.form.times" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="任课老师:">
          <el-input v-model="data.form.teacher" autocomplete="off"/>
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
  formVisible: false,
  form: {},
  loading: false,
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

const handleCurrentChange = (pageNum) => {
  // 翻页之后重新加载数据即可
  load()
}

const reload = () => {
  data.name = ''
  data.no = ''
  data.teacher = ''
  load()
}

// 打开新增框方法
const handleAdd = () => {
  // 清空当前行数据
  data.form = {}
  data.formVisible = true
}

// 修改与新增数据方法
const save = () => {
  loading(true)
  request.request({
    url: data.form.id ? '/api/course/update' : '/api/course/add',
    method: data.form.id ? 'PUT' : 'POST',
    data: data.form
  }).then(res => {
    loading(false)
    if (res.code === '200') {
      load()  // 重新获取数据
      data.formVisible = false  // 关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 编辑数据方法
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 删除数据方法
const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复数据，确认要删除嘛？', '确认删除', {type: 'warning'}).then(res => {
    request.delete('/api/course/delete/' + id).then(res => {
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
</script>