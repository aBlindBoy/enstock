<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="关键字查询">
          <el-input v-model="form.keyword" style="width:220px;" placeholder="请输入子账户名称/证券机构名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="primary" size="small" @click="editInfo({id:0})" plain>添加交易账户</el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            label="id">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.id}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="dealerInstitutionsName"
            label="证券机构名称">
          </el-table-column>
          <el-table-column
            prop="accountName"
            label="证券账户名">
          </el-table-column>
          <el-table-column
            prop="subaccountNumber"
            label="子账户名称">
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span v-if="scope.row.status==1" class="green">已用</span>
              <span v-if="scope.row.status==0" class="red">未用</span>
            </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="100"
            label="操作">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status==0" type="primary" title="修改" size="small" @click="editInfo(scope.row)">修改</el-button>
            </template>
          </el-table-column>

        </el-table>
        <div class="page-box">
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
      </div>
    </el-card>
    <editInfoDialog :info='detail' :getDate='getTradingAccountList' ref="editInfoDialog"></editInfoDialog> 
  </div>

</template>

<script>
import * as api from '@/axios/api'
import editInfoDialog from './account-edit'

export default {
  components: {
    editInfoDialog
  },
  props: {},
  data () {
    return {
      form: {
        keyword: '',
        pageNum: 1,
        pageSize: 100
      },
      list: {
        list: []
      },
      loading: false, // 表格加载
      detail: {} // 用户信息
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getTradingAccountList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getTradingAccountList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getTradingAccountList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getTradingAccountList()
    },
 
    async getTradingAccountList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        keyword: this.form.keyword
      }
      let data = await api.getTradingAccountList(opts)
      if (data.status === 0) {
        this.list = data.data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    
    editInfo (row) {
      // 修改
      this.detail = row
      this.$refs.editInfoDialog.dialogVisible = true
    }
  }
}
</script>
