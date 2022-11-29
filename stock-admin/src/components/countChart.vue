<template>
  <div>
    <!-- <el-carousel :interval="4000" type="card" height="200px">
        <el-carousel-item class="box-market" v-for="i in market" :key="i.key">

            </el-carousel-item>
    </el-carousel> -->
    <el-row :gutter="20">
      <el-col :span="18">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>资金情况</span>
              </div>
              <div class="text item">
                <div ref="myEchart2" style="height:200px;"></div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>当前持仓情况</span>
                <span class="pull-right">总入金({{info.charge_sum_amt}})</span>
              </div>
              <div class="text item">
                <div ref="myEchart3" style="height:200px;"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>实盘浮动盈亏情况</span>
              </div>
              <div class="text item other">
                <el-row>
                  <el-col :class="info.sp_profit_and_lose<0?' green':'red'">
                    实盘浮动盈亏:<span class="number">{{info.sp_profit_and_lose}}</span>
                  </el-col>
                  <el-col :class="info.sp_all_profit_and_lose<0?' green':'red'">
                    实盘总盈亏:<span class="number">{{info.sp_all_profit_and_lose}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>提现情况</span>
              </div>
              <div class="text item other">
                <el-row>
                  <el-col>
                    当前提现金额:<span class="number">{{info.sp_withdraw_sum_amt_success}}</span>
                  </el-col>
                  <el-col>
                    当前申请提现金额:<span class="number">{{info.sp_withdraw_sum_amt_apply}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>用户及代理信息统计</span>
              </div>
              <div class="text item other">
                <el-row>
                  <el-col>
                    代理数量:<span class="number">{{info.agent_num}}</span>
                  </el-col>
                  <el-col>
                    实盘用户:<span class="number">{{info.user_sp_num}}</span>
                  </el-col>
                  <el-col>
                    模拟用户:<span class="number">{{info.user_moni_num}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>股票信息</span>
              </div>
              <div class="text item other">
                <el-row>
                  <el-col>
                    股票数量:<span class="number">{{info.stock_num}}</span>
                  </el-col>
                  <el-col>
                    显示的股票:<span class="number">{{info.stock_show_num}}</span>
                  </el-col>
                  <el-col>
                    可交易的股票:<span class="number">{{info.stock_un_lock_num}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>充值提现统计</span>
              </div>
              <div class="text item other">
                <el-row>
                  <el-col>
                    今日充值:<span class="number">{{info.charge_today_sum_amt || 0}}</span>
                  </el-col>
                  <el-col>
                    总充值:<span class="number">{{info.charge_sum_amt}}</span>
                  </el-col>
                  <el-col>
                    今日提现:<span class="number">{{info.sp_withdraw_sum_today_amt_success || 0}}</span>
                  </el-col>
                  <el-col>
                    总提现:<span class="number">{{info.sp_withdraw_sum_amt_success}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
          
        </el-row>
      </el-col>
      <el-col :span="6">
        <div class="market-box" v-if="index<=5" v-for="(i,index) in market" :key="i.key">
          <p class="name">{{i.name}}</p>
          <p :class="i.increase<0?'price green':'price red'">{{i.nowPrice}}</p>
          <div class="status">
            <span :class="i.increase<0?'pifting green':'pifting red'">{{i.increase}}</span>
            <span :class="i.increase<0?'Percentage green':'Percentage red'">{{i.increaseRate}}%</span>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import echarts from 'echarts'
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

export default {
  components: {},
  props: {},
  data () {
    return {
      market: [
        { name: '上证指数', nowPrice: '3021.7512', increaseRate: '1.04', increase: '31.0662' },
        { name: '深证成指', nowPrice: '9550.54', increaseRate: '1.41', increase: '132.611' },
        { name: '创业板指', nowPrice: '1662.62', increaseRate: '0.75', increase: '12.437' },
        { name: '沪深300', nowPrice: '3745.00', increaseRate: '1.26', increase: '46.519' },
        { name: '中小板指', nowPrice: '6198.56', increaseRate: '1.41', increase: '85.965' },
        { name: '创业板50', nowPrice: '1343.36', increaseRate: '0.19', increase: '2.545' },
        { name: '深证综指', nowPrice: '1641.37', increaseRate: '1.43', increase: '23.110' },
        { name: '新 指 数', nowPrice: '7469.99', increaseRate: '1.47', increase: '108.242' }
      ],
      info: {}
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getData()
    this.getMarket()
  },
  methods: {
    async getData () {
      let data = await api.getCountInfo()
      if (data.status === 0) {
        this.info = data.data
        // this.initEchartMap(data.data);
        this.initEchartMap2(data.data)
        this.initEchartMap3(data.data)
      } else {
        this.$message.error(data.msg)
      }
    },
    async getMarket () {
      // 获取大盘指数
      let result = await api.getMarket()
      if (result.status === 0) {
        this.market = result.data.market
      } else {
        Toast(result.msg)
      }
    },
    initEchartMap (data) {
      let myChart = echarts.init(this.$refs.myEchart)
      window.onresize = myChart.resize
      let option = {
        color: ['#5962ff', '#ffc700', '#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#2b908f', '#f45b5b', '#91e8e1'],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: ['实盘用户', '模拟用户']
        },
        series: [
          {
            name: '股票详情',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              { value: data.user_sp_num, name: '实盘用户' },
              { value: data.user_moni_num, name: '模拟用户' }
            ]
          }
        ]
      }
      myChart.setOption(option)
    },
    initEchartMap2 (data) {
      let myChart = echarts.init(this.$refs.myEchart2)
      window.onresize = myChart.resize
      let option = {
        color: ['#f7622e', '#5962ff', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#2b908f', '#f45b5b', '#91e8e1'],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: ['当前实盘客户总资金', '当前实盘客户可用资金']
        },
        series: [
          {
            name: '当前持仓情况',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              { value: data.user_sp_sum_amt, name: '当前实盘客户总资金' },
              { value: data.user_sp_sum_enable, name: '当前实盘客户可用资金' }
            ]
          }
        ]
      }
      myChart.setOption(option)
    },
    initEchartMap3 (data) {
      let myChart = echarts.init(this.$refs.myEchart3)
      window.onresize = myChart.resize
      let option = {
        color: ['#7cb5ec', '#5962ff', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#2b908f', '#f45b5b', '#91e8e1'],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: ['当前持仓订单数量', '当前平仓订单数量']
        },
        series: [
          {
            name: '当前持仓情况',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              { value: data.sp_position_num, name: '当前持仓订单数量' },
              { value: data.sp_pc_position_num, name: '当前平仓订单数量' }
            ]
          }
        ]
      }
      myChart.setOption(option)
    }
  }
}
</script>
<style lang="css" scoped>
  .box-market {
    height: 130px;
    text-align: center;
    border-radius: 4px;
    background: -moz-linear-gradient(top, #d3dce6 1%, #99a9bf 100%); /* FF3.6-15 */
    background: -webkit-linear-gradient(top, #d3dce6 1%, #99a9bf 100%); /* Chrome10-25,Safari5.1-6 */
    background: linear-gradient(to bottom, #d3dce6 1%, #99a9bf 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
  }

  .box-market .name {
    font-size: 18px;
    padding: 15px 10px;
    font-weight: bold;
    color: #795548;
  }

  .box-card {
    margin-bottom: 15px;
  }

  .other .el-row div {
    margin-bottom: 10px;
  }

  .box-card .number {
    font-size: 16px;
    font-weight: 400;
    margin-left: 8px;
  }
</style>
