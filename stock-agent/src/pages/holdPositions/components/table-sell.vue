<template>
  <div>
    <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
      <el-form-item label="类型">
        <el-select filterable v-model="form.positionType" placeholder="持仓单">
          <el-option label="全部" value=""></el-option>
          <el-option label="正式持仓" value="0"></el-option>
          <el-option label="模拟持仓" value="1"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="持仓状态">
          <el-select filterable v-model="form.state" placeholder="持仓单">
          <el-option label="全部" value=""></el-option>
          <el-option label="持仓" value="0"></el-option>
          <el-option label="平仓" value="1"></el-option>
          </el-select>
      </el-form-item> -->
      <el-form-item label="下级代理">
        <el-select filterable v-model="form.agentId" placeholder="下级代理">
          <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
          <!-- <el-option label="区域二" value="beijing"></el-option> -->
        </el-select>
      </el-form-item>
      <el-form-item label="用户Id">
        <el-input v-model="form.userId" placeholder="用户Id"></el-input>
      </el-form-item>
      <el-form-item label="持仓订单号">
        <el-input v-model="form.positionSn" placeholder="持仓订单号"></el-input>
      </el-form-item>
      <el-form-item label="选择范围">
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
      </el-form-item>
    </el-form>
    <div class="table">
      <el-table
        v-loading="loading"
        show-summary
        :summary-method="getSummaries"
        :data="list.list"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="杠杆倍数">
                <span>{{ props.row.orderLever}}</span>
              </el-form-item>
              <el-form-item label="手续费">
                <span>{{ props.row.orderFee}}</span>
              </el-form-item>
              <el-form-item label="印花税">
                <span>{{ props.row.orderSpread}}</span>
              </el-form-item>
              <el-form-item label="留仓费">
                <span>{{ props.row.orderStayFee}}</span>
              </el-form-item>
              <el-form-item label="留仓天数">
                <span>{{ props.row.orderStayDays}}</span>
              </el-form-item>
              <el-form-item label="数量/股">
                <span>{{ props.row.orderNum}}</span>
              </el-form-item>
              <el-form-item label="持仓id">
                <span>{{ props.row.id}}</span>
              </el-form-item>
              <el-form-item v-if="props.row.isLock == 1" label="锁定原因">
                <span>{{ props.row.lockMsg}}</span>
              </el-form-item>
              <el-form-item label="买入时间">
                <span>{{props.row.buyOrderTime | timeFormat}}</span>
              </el-form-item>
              <el-form-item label="卖出时间">
                <span>{{props.row.sellOrderTime | timeFormat}}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="positionType"
          width="80px"
          label="账号类型">
          <template slot-scope="scope">
            <el-tag :type="scope.row.positionType == 1?'info':'success'">{{scope.row.positionType == 1?'模拟':'实盘'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="positionSn"
          width="160px"
          label="持仓订单号">
          <template slot-scope="scope">
            <!-- <span style="font-size:12px;color:#959595;" >{{scope.row.positionType == 1?'模拟':'实盘'}}/{{scope.row.id}}</span> -->
            <a class="hide-td" href="javasvript:;" :title="scope.row.positionSn">{{scope.row.positionSn}}</a>
          </template>
        </el-table-column>
        <el-table-column
          prop="stockName"
          width="160px"
          label="股票名称">
          <template slot-scope="scope">
            <p>
              {{scope.row.stockName}}
              <span>
              ({{scope.row.stockCode}})
            </span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="nickName"
          label="用户名/id">
          <template slot-scope="scope">
            <span>{{scope.row.nickName}}/{{scope.row.userId}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="profitAndLose"
          label="浮动盈亏">
          <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.profitAndLose<0?'green':'red'">{{scope.row.profitAndLose}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="allProfitAndLose"
          label="总盈亏">
          <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.allProfitAndLose<0?'green':'red'">{{scope.row.allProfitAndLose}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          label="买入价">
        </el-table-column>
        <el-table-column
          prop="sellOrderPrice"
          label="卖价">
        </el-table-column>
        <!-- <el-table-column
          width="110px"
          prop="orderNum"
          label="数量">
        </el-table-column> -->
        <el-table-column
          prop="orderDirection"
          label="方向">
          <template slot-scope="scope">
            <p>
              {{scope.row.orderDirection}}
              <i v-if="scope.row.orderDirection == '买涨'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  == '买跌'" class="green iconfont icon-down"></i>
            </p>
          </template>
        </el-table-column>
        <!-- <el-table-column
         prop="orderLever"
         label="杠杆倍数">
       </el-table-column> -->
        <el-table-column
          prop="orderTotalPrice"
          label="总市值">
        </el-table-column>

        <!-- <el-table-column
         prop="orderFee"
         label="手续费">
       </el-table-column>
        <el-table-column
         prop="orderSpread"
         label="印花税">
       </el-table-column>
        <el-table-column
         prop="orderStayFee"
         label="留仓费">
       </el-table-column>
        <el-table-column
         prop="orderStayDays"
         label="留仓天数">
       </el-table-column> -->
        <!-- <el-table-column
          prop="buyOrderTime"
          label="买入时间"
          width="180">
          <template slot-scope="scope">
            <span>
              {{scope.row.buyOrderTime | timeFormat}}
            </span>
          </template>
        </el-table-column>
         <el-table-column
          prop="buyOrderTime"
          label="卖出时间"
          width="180">
          <template slot-scope="scope">
            <span>
              {{scope.row.sellOrderTime | timeFormat}}
            </span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
          prop="positionType"
          label="类型">
          <template slot-scope="scope">
            <span>
              {{scope.row.positionType == 1?'模拟':'实盘'}}
            </span>
          </template>
        </el-table-column> -->
        <el-table-column
          fixed="right"
          prop="isLock"
          width="100px"
          label="查看详情">
          <template slot-scope="scope">
            <el-button type="text" title="查看详情" size="small" @click="toDetail(scope.row)"><i
              class="iconfont icon-chakan"></i></el-button>
          </template>
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
      <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>

    </div>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import DetailDialog from './detail-dialog'

export default {
  components: {
    DetailDialog
  },
  props: {
    type: {
      type: Number,
      default: 1
    }
  },
  data () {
    return {
      loading: false,
      form: {
        positionType: '',
        state: '1',
        userId: '',
        agentId: '',
        positionSn: '',
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
      detail: null // 详情数据
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
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
      console.log(`每页 ${val} 条`)
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      console.log(`当前页: ${val}`)
      this.getList()
    },
    onSubmit () {
      // 查询表格
      this.getList()
    },
    async getAgentList () {
      // 获取下级代理数据
      let data = await api.getSecondAgent()
      if (data.status === 0) {
        if(data.data.list.length > 0){
          this.agentList = data.data.list
        }
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      // 获取表格数据
      let opts = {
        positionType: this.form.positionType, // 正式 0 模拟 1
        state: this.form.state, // 持仓 0 平仓 1
        userId: this.form.userId,
        agentId: this.form.agentId,
        positionSn: this.form.positionSn,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.getUserPositionList(opts)
      this.loading = false
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      // this.tableData = data
    },
    toDetail (row) {
      // 查看详情
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
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
        if (column.property === 'buyOrderPrice' || column.property === 'orderTotalPrice' || column.property === 'orderNum' || column.property === 'profitAndLose' || column.property === 'allProfitAndLose' || column.property === 'orderFee' || column.property === 'orderSpread' || column.property === 'orderStayFee') {
          // 需要计算列 ==> 数量 浮动盈亏 总盈亏 总市值 手续费 印花税 留仓费
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
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
    }
  }
}
</script>
