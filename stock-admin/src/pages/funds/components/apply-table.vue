<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="关键字查询">
          <el-input v-model="form.keyword" style="width:220px;" placeholder="请输入订单号/手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
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
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.id}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="orderNumber"
            width="152px"
            label="订单号">
          </el-table-column>
          <el-table-column
            prop="userName"
            width="80px"
            label="配资人">
          </el-table-column>
          <el-table-column
            prop="subaccountNumber"
            width="90px"
            label="子账号">
          </el-table-column>
          <el-table-column
            prop="userPhone"
            width="120px"
            label="手机号">
          </el-table-column>
          <el-table-column
            prop="fundsType"
            width="80px"
            label="配资类型">
            <template slot-scope="scope">
              <p>
                <span v-if="scope.row.fundsType==2">按周配资</span>
                <span v-if="scope.row.fundsType==3">按月配资</span>
                <span v-else>按天配资</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="margin"
            width="80px"
            label="保证金">
          </el-table-column>
          <el-table-column
            prop="fundsAmount"
            width="80px"
            label="配资金额">
          </el-table-column>
          <el-table-column
            prop="totalTradingAmount"
            width="100px"
            label="总操盘金额">
          </el-table-column>
          <el-table-column
            prop="tradersCycle"
            width="80px"
            label="操盘期限">
          </el-table-column>
          <el-table-column
            prop="status"
            width="80px"
            label="状态">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span v-if="scope.row.status==0" class="red">待审核</span>
              <span v-if="scope.row.status==1" class="green">操盘中</span>
              <span v-if="scope.row.status==2" class="green">未通过</span>
              <span v-if="scope.row.status==3" class="green">已到期</span>
              <span v-if="scope.row.status==4" class="green">已结束</span>
            </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="addTime"
            label="申请时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            prop="beginTime"
            label="开始时间">
            <template slot-scope="scope">{{scope.row.beginTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            prop="endTime"
            label="结束时间">
            <template slot-scope="scope">{{scope.row.endTime | timeFormat}}</template>
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
    <editInfoDialog :info='detail' :getDate='getApplyList' ref="editInfoDialog"></editInfoDialog> 
  </div>

</template>

<script>
import * as api from '@/axios/api'
import editInfoDialog from './apply-edit'

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
      detail: {}, // 用户信息
      applyType: 0
    }
  },
  watch: {
    $route(){
      this.applyType = this.$route.query.type
    },
    applyType() {
      this.getApplyList()
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getApplyList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getApplyList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getApplyList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getApplyList()
    },
 
    async getApplyList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        keyword: this.form.keyword,
        status: this.applyType
      }
      let data = await api.getApplyList(opts)
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
