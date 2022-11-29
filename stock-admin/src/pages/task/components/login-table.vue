<template>
  <div>
    <el-card class="box-card">
      <div class="table">
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            label="id">
          </el-table-column>
          <el-table-column
            prop="userName"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="userId"
            label="用户id">
          </el-table-column>
          <el-table-column
            prop="loginAddress"
            label="登陆地址">
          </el-table-column>
          <el-table-column
            prop="loginIp"
            label="登陆ip">
          </el-table-column>
          <el-table-column
            width="170px"
            prop="addTime"
            label="登录时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <!-- <el-table-column
            fixed="right"
            width="100px"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" title="查看详情" size="small" @click="toDetail(scope.row)"><i class="iconfont icon-chakan"></i></el-button>
            </template>
          </el-table-column> -->
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
      let data = await api.loginList(opts)
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
