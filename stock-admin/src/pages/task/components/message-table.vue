<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <!-- <el-form-item  label="任务类型">
           <el-select clearable v-model="form.taskType" placeholder="任务类型">
             <el-option v-for="i in tasktypeList" :key="i.key" :label="i.value" :value="i.value"></el-option>
           </el-select>
       </el-form-item>
       <el-form-item>
           <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
       </el-form-item> -->
      </el-form>
      <div class="table">
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            width="80px"
            label="id">
          </el-table-column>
          <el-table-column
            prop="typeName"
            width="120px"
            label="类型">
          </el-table-column>
          <el-table-column
            prop="userName"
            width="120px"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="status"
            width="120px"
            label="发送状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.status == 2" class="hide-td green" title="已读"><i v-if="scope.row.status == 2"
                                                       class="iconfont icon-zhengchang"></i>已读</a>
                <a v-else class="hide-td red" title="未读"><i v-if="scope.row.status == 1" class="iconfont icon-failure"></i>未读</a>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="content"
            label="内容">
          </el-table-column>
          <el-table-column
            width="160px"
            prop="addTime"
            label="发送时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
        </el-table>
        <div class="page-box">
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40,50]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
      </div>
    </el-card>
    <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import DetailDialog from './detail-dialog'

export default {
  components: {
    DetailDialog
  },
  props: {},
  data () {
    return {
      form: {
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      detail: {},
      loading: false
    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.getList()
  },
  methods: {
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getList()
    },
    onSubmit () {
      // 查询表格
      this.getList()
    },
    async getList () {
      // 获取表格数据
      let opts = {
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.messageList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    toDetail (row) {
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }
</style>
