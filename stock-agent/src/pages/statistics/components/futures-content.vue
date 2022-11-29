<template>
  <div>
    <el-alert
      class="info-alert"
      title="查询指定时间内，代理所有客户（已平仓）的交易盈亏"
      type="warning"
      close-text="知道了"></el-alert>
    <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
      <el-form-item label="代理">
        <el-select clearable filterable v-model="form.agentId" placeholder="代理">
          <el-option :label="$store.state.userInfo.agentName?$store.state.userInfo.agentName:'本代理'"
                     value=""></el-option>
          <el-option v-for="i in agentList" :key="i.key" :label="i.agentName + ' /' + i.id" :value="i.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="持仓类型">
        <el-select v-model="form.positionType" placeholder="充值状态">
          <el-option label="正式持仓" value="0"></el-option>
          <el-option label="模拟持仓" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择范围">
        <el-date-picker
          v-model="form.time"
          :picker-options="pickerOptions"
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
    <div>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>期货统计信息</span>
            </div>
            <div class="text item">
              <el-row class="prompt">
                <el-col :span="6">&nbsp;</el-col>
                <el-col class="text-center all" :span="6">
                  总盈亏
                </el-col>
                <el-col class="text-center active" :span="6">
                  实际盈亏
                </el-col>
                <el-col class="text-center" :span="6">
                  手续费
                </el-col>
              </el-row>
              <el-row class="info-box">
                <el-col class="info" :span="6">
                  <p class="title">订单数量</p>
                  <span class="number">{{list.orderSize}}</span>
                </el-col>
                <el-col class="info" :span="6">
                  <p class="title">总盈亏</p>
                  <span class="number">{{list.orderAllAmt}}</span>
                </el-col>
                <el-col class="info" :span="6">
                  <p class="title">实际盈亏</p>
                  <span class="number">{{list.orderProfitAndLose}}</span>
                  <p class="pro">平仓单的实际盈亏</p>
                </el-col>
                <el-col class="info" :span="6">
                  <p class="title">手续费</p>
                  <span class="number">{{list.orderFeeAmt}}</span>
                  <p class="pro">期货买入手续费</p>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import ChartBox from '@/components/ChartBox'

export default {
  components: {
    ChartBox
  },
  props: {},
  data () {
    let nowDate = new Date()
    let beforeDate = new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000)
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近六个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 180)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      nowDate: nowDate,
      beforeDate: beforeDate,
      form: {
        agentId: '',
        positionType: '',
        time: '',
        pageNum: 1,
        pageSize: 15
      },
      list: {
        orderAllAmt: '-',
        orderFeeAmt: '-',
        orderProfitAndLose: '-',
        orderSize: '-'
      },
      loading: false, // 表格加载
      detail: {}, // 用户信息
      agentList: []
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    // this.getList()
    this.getAgentList()
  },
  methods: {
    async getAgentList () {
      // 获取下级代理数据
      let opts = {
        pageNum: 1,
        pageSize: 100
      }
      let data = await api.getSecondAgent(opts)
      if (data.status === 0) {
        if(data.data.list.length > 0){
          this.agentList = data.data.list
        }
      } else {
        this.$message.error(data.msg)
      }
    },
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
        agentId: this.form.agentId,
        positionType: this.form.positionType,
        beginTime: this.form.time ? this.form.time[0] : this.timeFormatTiString(this.nowDate),
        endTime: this.form.time ? this.form.time[1] : this.timeFormatTiString(this.beforeDate)
      }
      let data = await api.getFuturesIncome(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    timeFormatTiString (val) {
      let fmt = 'yyyy-MM-dd hh:mm:ss'
      if (!val) {
        return
      }
      let date = new Date(val)
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
      }
      let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
      }
      for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + ''
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str))
        }
      }
      return fmt
    },
    padLeftZero (str) {
      return ('00' + str).substr(str.length)
    }
  }
}
</script>
<style lang="less" scoped>
  .info-box {
    // height: 200px;
    .info {
      line-height: 30px;
      border-right: 1px solid #eee;
      text-align: center;
      height: 140px;

      &:nth-child(4) {
        border: none;
      }
    }

    .title {
      font-size: 16px;
      padding: 10px 0;
    }

    .number {
      font-size: 24px;
      color: red;
    }

    .pro {
      font-size: 12px;
      color: #ababab;
      text-indent: 6px;
      line-height: 22px;
      margin-top: 10px;
    }
  }

  .info-alert {
    margin-bottom: 10px;
  }

  .prompt {
    color: #ababab;
    margin-bottom: 20px;

    .all {
      position: relative;

      &:after {
        content: "=";
        position: absolute;
        right: -5px;
        font-size: 20px;
      }
    }

    .active {
      position: relative;

      &:after {
        content: "-";
        position: absolute;
        right: -5px;
        font-size: 20px;
      }
    }
  }
</style>
