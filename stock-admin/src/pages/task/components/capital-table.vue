<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="用户id">
          <el-input v-model="form.userId" placeholder="用户id"></el-input>
        </el-form-item>
        <el-form-item label="用户名字">
          <el-input v-model="form.realName" placeholder="用户名字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="agentId"
            label="代理id">
          </el-table-column>
          <el-table-column
            width="200px"
            prop="realName"
            label="真实姓名/id">
            <template slot-scope="scope">
              <p>
                {{scope.row.realName}}
                <span class="small">({{scope.row.userId}})</span>
              </p>
            </template>
          </el-table-column>

          <el-table-column
            prop="amtFrom"
            label="转出账户">
            <template slot-scope="scope">
              <el-tag :type="scope.row.amtFrom == '融资'?'warning':scope.row.amtFrom == '指数'?'success':''">
                {{scope.row.amtFrom}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="transAmt"
            label="转出金额">
          </el-table-column>
          <el-table-column
            prop="amtTo"
            label="转入账户">
            <template slot-scope="scope">
              <el-tag :type="scope.row.amtTo == '融资'?'warning':scope.row.amtTo == '指数'?'success':''">
                {{scope.row.amtTo}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            width="180px"
            prop="addTime"
            label="注册时间">
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
        userId: '',
        realName: '',
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
        userId: this.form.userId,
        realName: this.form.realName,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.exchangeList(opts)
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
