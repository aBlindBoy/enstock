<template>
  <div>
    <!-- <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
      <el-form-item label="代理名称">
        <el-input v-model="form.user" placeholder="代理名称"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form> -->
    <div class="table">
      <el-table
        v-loading="loading"
        :data="list.list"
        style="width: 100%">
        <el-table-column
          prop="feeType"
          label="费用类型"
          :formatter="statusFormat"
          width="120">
        </el-table-column>
        <el-table-column
          prop="aymentType"
          :formatter="aymentTypeFormat"
          label="收支类型"
          width="120">
        </el-table-column>
        <el-table-column
          prop="totalMoney"
          label="总金额">
        </el-table-column>
        <el-table-column
          prop="profitMoney"
          label="利润金额">
          <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.profitMoney>0?'green':'red'">{{scope.row.profitMoney}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="addTime"
          :formatter="timeFormat"
          label="添加时间">
        </el-table-column>
        <el-table-column
          prop="remarks"
          label="备注"
          width="550">
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

  </div>

</template>

<script>
import * as api from '@/axios/api'
import { timeFormat } from '@/utils/utils'

export default {
  components: {
  },
  props: {},
  data () {
    return {
      form: {
        user: '',
        region: '',
        pageNum: 1,
        pageSize: 15
      },
      list: {
        list: []
      },
      loading: false // 表格加载
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getList()
    },
    async getList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      let data = await api.getAgentAgencyFeeList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    statusFormat: function(row, column) {
      let str = ""
      if(row.feeType === 1){
        str = "入仓手续费";
      }else if(row.feeType === 2){
        str = "平仓手续费";
      } else if(row.feeType === 3){
        str = "延递费";
      } else if(row.feeType === 4){
        str = "分红";
      }
      return str
    },
    aymentTypeFormat: function(row, column) {
      let str = ""
      if(row.aymentType === 1){
        str = "收入";
      }else{
        str = "支出";
      } 
      return str
    },
    timeFormat: function(row, column) {
      return timeFormat(row.addTime)
    }

  }
}
</script>
