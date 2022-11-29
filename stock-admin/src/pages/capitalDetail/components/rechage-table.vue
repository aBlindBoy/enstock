<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="充值状态">
          <el-select v-model="form.state" placeholder="充值状态">
            <el-option label="审核中" value="0"></el-option>
            <el-option label="入金成功" value="1"></el-option>
            <el-option label="入金失败" value="2"></el-option>
            <el-option label="入金取消" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户id">
          <el-input v-model="form.userId" placeholder="用户id"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="支付时间">
          <el-date-picker
            v-model="form.time"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
          <el-button type="success" @click="export2Excel">导出当前页数据</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addOrder">
              <i class="iconfont icon-add"></i>创建充值订单
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          show-summary
          :summary-method="getSummaries"
          style="width: 100%">
          <el-table-column
            width="60px"
            prop="id"
            label="id">
          </el-table-column>
          <el-table-column
            prop="orderSn"
            width="170px"
            label="订单号">
            <template slot-scope="scope">
              <a class="hide-td" href="javascript:;" :title="scope.row.orderSn">{{scope.row.orderSn}}</a>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="userId"
            width="80"
            label="用户id">
          </el-table-column> -->
          <el-table-column
            width="100"
            prop="nickName"
            label="用户名/id">
            <template slot-scope="scope">
              <p>
                {{scope.row.nickName}}
                <span class="small">
                {{scope.row.userId}}
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="120px"
            prop="payChannel"
            label="充值渠道">
            <template slot-scope="scope">
            <span>
              {{scope.row.payChannel==0?'支付宝':scope.row.payChannel==1?'对公转账':scope.row.payChannel}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="payAmt"
            label="充值金额">
          </el-table-column>
          <el-table-column
            width="90"
            prop="orderStatus"
            label="状态">
            <template slot-scope="scope">
              <p>
              <span
                :class="scope.row.orderStatus==1?'green':scope.row.orderStatus==2?'red':scope.row.orderStatus==0?'blue':'yellow'">
                <i v-if="scope.row.orderStatus==1" class="iconfont icon-zhengchang"></i>
                <i v-if="scope.row.orderStatus==2" class="iconfont icon-failure"></i>
                <i v-if="scope.row.orderStatus==3" class="iconfont icon-failure"></i>
                <i v-if="scope.row.orderStatus==0" class="iconfont icon-dengdai"></i>
                {{scope.row.orderStatus==1?'成功':scope.row.orderStatus==2?'失败':scope.row.orderStatus==0?'审核中':'取消'}}
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="deType"
            width="180"
            label="申请时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.addTime | timeFormat}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="deType"
            width="180"
            label="支付时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.payTime | timeFormat}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            prop="isLock"
            width="200px"
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" :disabled="scope.row.orderStatus != 0" :plain='scope.row.orderStatus != 0'
                         size="small" @click="editStatus(scope.row.orderSn,scope.row.imgUrl)">修改状态
              </el-button>
              <el-button type="danger" size="small" @click="toDelete(scope.row)">删除订单</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-box">
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 50, 100,150,200]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
        <ChangeDialog :info='detail' :getDate='getList' :imgUrl="imgUrl" ref="changeDialog"></ChangeDialog>
        <AddDialog :getDate='getList' ref="addDialog"></AddDialog>

      </div>
    </el-card>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import ChangeDialog from './change-dialog'
import AddDialog from './add-dialog'

export default {
  components: {
    ChangeDialog,
    AddDialog
  },
  props: {},
  data () {
    return {
      form: {
        realName: '',
        userId: '',
        status: '',
        agentId: '',
        time: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      agentList: [
        {
          'id': 2,
          'agentName': '下级1',
          'agentRealName': '下级1',
          'agentPhone': '18888888888',
          'agentCode': '8888'
        }
      ],
      loading: false, // 表格加载
      detail: null, // 选择的订单id
      imgUrl:null,// 选择的订单凭证
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getList()
    this.getAgentList()
  },
  methods: {
    export2Excel () {
      require.ensure([], () => {
        const { export_json_to_excel } = require('../../../assets/js/Export2Excel')
        const tHeader = ['订单号', '用户id', '代理id', '用户名', '充值金额', '充值渠道', '充值状态', '申请时间', '支付时间']
        // 上面设置Excel的表格第一行的标题
        const filterVal = ['orderSn', 'userId', 'agentId', 'nickName', 'payAmt', 'payChannel', 'orderStatus', 'addTime', 'addTime']
        // 上面的index、phone_Num、school_Name是tableData里对象的属性
        const list = this.list.list // 把data里的tableData存到list
        const data = this.formatJson(filterVal, list)
        let nowdate = new Date()
        let year = nowdate.getFullYear()
        let month = nowdate.getMonth() + 1
        let day = nowdate.getDay()
        export_json_to_excel(tHeader, data, '充值列表' + year + month + day)
      })
    },
    formatJson (filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]))
    },
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
    async getAgentList () {
      // 获取下级代理数据
      let opts = {
        pageNum: 1,
        pageSize: 100
      }
      let data = await api.getSecondAgent(opts)
      if (data.status === 0) {
        this.agentList = data.data.list
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      // 获取表格数据
      let opts = {
        realName: this.form.realName,
        userId: this.form.userId,
        state: this.form.state,
        agentId: this.form.agentId,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.rechargeList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    editStatus (val,imgUrl) {
      // 修改状态
      this.detail = val
      this.imgUrl = imgUrl
      this.$refs.changeDialog.dialogVisible = true
    },
    addOrder () {
      // 创建充值订单
      this.$refs.addDialog.dialogVisible = true
    },
    getSummaries (param) {
      // 底部计算
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        // 第一行 不统计
        if (index === 0) {
          sums[index] = '统计'
          return
        }
        if (column.property === 'payAmt') {
          // 需要计算列 ==>    userAmt enableAmt  allFreezAmt allProfitAndLose forceLine
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                let num = prev + curr
                return num
              } else {
                return prev
              }
            }, 0)
            sums[index] = sums[index].toFixed(2)
          } else {
            sums[index] = 'N/A'
          }
        }
      })
      return sums
    },
    // 删除提现
    toDelete (row) {
      this.$confirm('确认删除提现订单？此操作不可恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let data = await api.delcashList({ cId: row.id })
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.getList()
        } else {
          this.$message.error(data.msg)
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消'
        })
      })
    }
  }
}
</script>
