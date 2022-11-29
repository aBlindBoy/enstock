<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入金状态">
          <el-select v-model="form.state" placeholder="入金状态">
            <el-option label="审核中" value="0"></el-option>
            <el-option label="入金成功" value="1"></el-option>
            <el-option label="入金失败" value="2"></el-option>
            <el-option label="入金取消" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="form.payChannel" placeholder="支付方式">
            <el-option label="支付宝" value="0"></el-option>
            <!-- <el-option label="微信" value="1"></el-option> -->
            <el-option label="对公转账" value="1"></el-option>
            <el-option label="其他" value=""></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <!-- <el-form-item label="选择范围">
            <el-date-picker
            v-model="form.time"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item> -->
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
          <el-table-column
            prop="orderSn"
            width="180"
            label="订单号">
          </el-table-column>
          <el-table-column
            prop="userId"
            width="80"
            label="用户id">
          </el-table-column>
          <el-table-column
            width="80"
            prop="nickName"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="payChannel"
            label="充值方式">
            <template slot-scope="scope">
            <span>
              {{scope.row.payChannel==0?'支付宝':scope.row.payChannel==1?'对公转账':'现金转账'}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="payAmt"
            label="充值金额">
          </el-table-column>
          <el-table-column
            prop="orderStatus"
            width="100px"
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
            label="入金时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.payTime | timeFormat}}
            </span>
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
      </div>
    </el-card>
  </div>

</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {},
  data () {
    return {
      form: {
        realName: '',
        payChannel: '',
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
      loading: false // 表格加载
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
        realName: this.form.realName,
        payChannel: this.form.payChannel,
        state: this.form.state,
        agentId: this.form.agentId,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.getUserComeinList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
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
          // 需要计算列 ==> 出金金额 浮动盈亏 总盈亏 总市值 手续费 印花税 留仓费
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
