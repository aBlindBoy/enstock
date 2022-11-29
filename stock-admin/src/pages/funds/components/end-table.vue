<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="关键字查询">
          <el-input v-model="form.keyword" style="width:220px;" placeholder="请输入用户名/手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            width="60px"
            label="id">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.id}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="userPhone"
            label="手机号">
          </el-table-column>
          <el-table-column
            prop="userName"
            label="配资人">
          </el-table-column>
          <el-table-column
            prop="appendServiceFee"
            label="申请服务费">
          </el-table-column>
          <el-table-column
            prop="lever"
            label="配资倍数">
          </el-table-column>
          <el-table-column
            prop="totalTradingAmount"
            label="原操盘金额">
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
            label="保证金">
          </el-table-column>
          <el-table-column
            prop="fundsAmount"
            label="配资金额">
          </el-table-column>
          <el-table-column
            prop="addTime"
            label="申请时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            prop="endTime"
            label="终止时间">
            <template slot-scope="scope">{{scope.row.endTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            prop="lineWarning"
            label="预警线">
          </el-table-column>
          <el-table-column
            prop="lineUnwind"
            label="止损线">
          </el-table-column>
          <el-table-column
            prop="tradersCycle"
            label="使用时长">
            <template slot-scope="scope">
                <span style="font-size:12px;color:#959595;">{{scope.row.tradersCycle}}天</span>
            </template>
          </el-table-column>
          
          <el-table-column
            prop="status"
            label="状态">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span v-if="scope.row.status==0" class="red">审核中</span>
              <span v-if="scope.row.status==1" class="green">已通过</span>
              <span v-if="scope.row.status==2" class="red">未通过</span>
            </p>
            </template>
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
    <editInfoDialog :info='detail' :getDate='getAppendList' ref="editInfoDialog"></editInfoDialog> 
  </div>

</template>

<script>
import * as api from '@/axios/api'
import editInfoDialog from './delay-edit'

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
  watch: {
    $route(){
      this.getAppendList()
    },
  },
  computed: {},
  created () {},
  mounted () {
    this.getAppendList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getAppendList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getAppendList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getAppendList()
    },
 
    async getAppendList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        keyword: this.form.keyword,
        status: this.$route.query.type,
        appendType: 4
      }
      let data = await api.getAppendList(opts)
      if (data.status === 0) {
        this.list = data.data
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
