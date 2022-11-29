<template>
  <div class="wrapper">
    <el-card class="box-card">
      <el-form :inline="true" :rules="rule" :model="form" class="demo-form-inline" size="small" ref="form">
        <el-row>
          <el-form-item label="选择用户：" prop="userId">
            <el-input style="width:220px;" placeholder="输入用户id查询用户信息" v-model="form.userId" class="input-with-select">
              <el-button slot="append" icon="el-icon-search" :loading="userInfoLoading"
                         @click="getUserDetail"></el-button>
            </el-input>
            <!-- <el-input class="form-input" v-model="form.userId" placeholder="输入用户id查询用户信息"></el-input> -->
          </el-form-item>
          <!-- <el-button size="medium" type="primary" >查询</el-button> -->
        </el-row>
        <el-row class="info-box">
          <el-col :span="6">
            <span class="title">用户名：</span>
            <span class="value">
                    {{userInfo.realName}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">所属代理：</span>
            <span class="value">
                        {{userInfo.agentName}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">账号类型：</span>
            <span v-show="userInfo.accountType" class="value">
                    {{userInfo.accountType == 1?'模拟用户':'实盘用户'}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">手机号：</span>
            <span class="value">
                    {{userInfo.phone}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">总资金：</span>
            <span class="number">
                    {{userInfo.userAmt}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">可用资金：</span>
            <span class="number">
                    {{userInfo.enableAmt}}
                    </span>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="选择股票：" prop="code">
            <!-- <el-autocomplete
                v-model="form.code"
                :fetch-suggestions="querySearchAsync"
                placeholder="输入股票代码查询股票信息"
                @select="handleSelect"
            ></el-autocomplete> -->
            <el-input class="form-input" v-model="form.code" placeholder="输入股票代码查询股票信息">
              <el-button slot="append" icon="el-icon-search" :loading="stockInfoLoading" @click="getStock"></el-button>
            </el-input>
          </el-form-item>
          <!-- <el-button size="medium" type="primary" @click="getStock">选择股票</el-button> -->
        </el-row>
        <el-row class="info-box">
          <el-col :span="6">
            <span class="title">股票名字：</span>
            {{stockInfo.name}}
            <!-- <span style="font-size:12px;color:#959595;">{{stockInfo.code}}</span> -->
          </el-col>
          <el-col :span="6">
            <span class="title">代码：</span>
            <span class="value">
                        {{stockInfo.code}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">现价：</span>
            <span :class="stockInfo.hcrate<0?'green number':'number red'">
                    {{stockInfo.nowPrice}}
                    </span>
          </el-col>
          <el-col :span="6">
            <span class="title">涨跌：</span>
            <span v-show="stockInfo.hcrate" :class="stockInfo.hcrate<0?'green number':'number red'">
                    {{stockInfo.hcrate}}%
                    </span>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="买入时间：" prop="time">
              <el-date-picker
                v-model="form.time"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                placeholder="选择买入时间点">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买入价格：" prop="price">
              <el-input style="width:220px;" class="form-input" v-model="form.price"
                        placeholder="输入所选择时间点对应的价格"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="杠杆倍数：" prop="lever">
              <el-input style="width:220px;" class="form-input" v-model="form.lever" placeholder="输入买入杠杆倍数"></el-input>
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item label="买入方向：" prop="type">
              <el-radio v-model="form.type" label="0">买涨</el-radio>
              <el-radio v-model="form.type" label="1">买跌</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="买入数量：" prop="number">
              <el-input style="width:220px;" placeholder="请输入买入数量" v-model="form.number" class="input-with-select">
                <el-select v-model="form.number" slot="prepend" placeholder="请选择买入数量">
                  <!-- <el-option label="50手" value="50"></el-option>
                  <el-option label="100手" value="100"></el-option>
                  <el-option label="200手" value="200"></el-option> -->
                  <el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>

                </el-select>
                <el-button slot="append">手</el-button>
              </el-input>
              <!-- <el-input class="form-input" v-model="form.number" placeholder="输入买入数量"></el-input> -->
            </el-form-item>
            <p class="clearfix">
              <span class="pull-left">最小购买股数{{Number(riskInfo.buyMinNum)/100}}手</span>
              <span class="protem pull-right">最大可购买数量{{Number(riskInfo.buyMaxNum)/100}}手</span>
            </p>
          </el-col>

        </el-row>
        <el-row v-show="!cardShow" class="text-center" style="margin-top:20px;">
          <el-button size="medium" type="primary" :loading="loading" @click="submitOrder('form')">创建持仓单</el-button>
        </el-row>
      </el-form>
      <el-dialog
        title="详情"
        :visible.sync="cardShow"
        width="50%">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>您所创建的订单详情</span>
          </div>
          <div class="text item">
            <el-row class="info-box">
              <el-col :span="8">
                买入价格：
                <span>
                                {{form.price}}
                            </span>
              </el-col>
              <el-col class="text-center" :span="8">
                现价：
                <span class="number">
                            {{stockInfo.nowPrice}}
                        </span>
              </el-col>
              <el-col class="text-right" :span="8">
                买入方向：
                <span class="number">
                            {{form.type==0?'买涨':'买跌'}}
                        </span>
              </el-col>
              <el-col :span="8">
                总市值：
                <span>
                                {{orderInfo.orderTotalPrice}}
                            </span>
              </el-col>
              <el-col class="text-center" :span="8">
                浮动盈亏：
                <span :class="orderInfo.profitAndLose>0?'red number':'green number'">
                            {{(orderInfo.profitAndLose).toFixed(2)}}
                        </span>
              </el-col>
              <el-col :span="8" class="text-right">
                总盈亏：
                <span :class="orderInfo.allProfitAndLose>0?'red number':'green number'">
                            {{(orderInfo.allProfitAndLose).toFixed(2)}}
                        </span>
              </el-col>
              <el-col :span="8">
                手续费：
                <span>
                                {{orderInfo.orderFee}}
                            </span>
              </el-col>
              <el-col class="text-center" :span="8">
                印花税：
                <span>
                                {{orderInfo.orderSpread}}
                            </span>
              </el-col>
              <el-col :span="8" class="text-right">
                留仓费：
                <span>
                                {{orderInfo.orderStayFee}}
                            </span>
              </el-col>
            </el-row>

          </div>
        </el-card>
        <el-row class="text-center">
          <el-button size="medium" type="primary" @click="creatOrder">确认创建持仓单</el-button>
        </el-row>
      </el-dialog>
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
      loading: false,
      cardShow: false,
      form: {
        userId: '',
        code: '',
        time: '',
        price: '',
        number: '',
        type: '',
        lever: ''
      },
      rule: {
        userId: [
          { required: true, message: '请选择用户', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请选择股票', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请选择买入时间', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入买入价格', trigger: 'blur' }
        ],
        number: [
          { required: true, message: '请输入买入数量', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择买入方向', trigger: 'blur' }
        ],
        lever: [
          { required: true, message: '请输入杠杆倍数', trigger: 'blur' }
        ]
      },
      stockInfoLoading: false,
      userInfoLoading: false,
      userInfo: {
        realName: '',
        agentName: '',
        accountType: '',
        enableAmt: '',
        userAmt: ''
      },
      list: {},
      stockInfo: {
        name: '',
        code: '',
        nowPrice: '',
        hcrate: ''
      },
      riskInfo: {},
      orderInfo: {
        orderTotalPrice: '', // 总市值
        orderFee: '', // 手续费
        orderSpread: '', // 印花税
        orderStayFee: 0, // 留仓费
        profitAndLose: 0, // 留仓费
        allProfitAndLose: 0
      },
      buyNumList: []
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getSettingInfo()
  },
  methods: {
    async getUserDetail (id) {
      // 查询用户信息
      this.userInfoLoading = true
      let data = await api.getUserDetail({ userId: this.form.userId })
      if (data.status === 0) {
        if (data.data) {
          this.userInfo = data.data
        } else {
          this.$message.error('没有该用户!')
        }
      } else {
        this.$message.error(data.msg)
      }
      this.userInfoLoading = false
    },
    async getStock () {
      // 获取股票数据
      this.stockInfoLoading = true
      let opts = {
        code: this.form.code
      }
      let data = await api.getSingleStock(opts)
      if (data.status === 0) {
        this.stockInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.stockInfoLoading = false
    },
    async getSettingInfo () {
      // 获取风控设置
      let data = await api.getSettingInfo()
      if (data.status === 0) {
        this.riskInfo = data.data
        // 杠杆倍数
        this.form.lever = data.data.siteLever
        this.buyNumList = []
        for (let i = 0; i < 10; i++) {
          if (i === 0 || i % 2 === 1) {
            let val = data.data.buyMinNum * i + data.data.buyMinNum
            let item = { label: val / 100 + '手', value: val / 100 }
            this.buyNumList.push(item)
          }
        }
      } else {
        this.$message.error(data.msg)
      }
    },
    submitOrder (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 市值价 = 现价 * 股（1手 = 100股）
          // let day = Math.floor(new Date().getTime() - new Date(this.form.time).getTime()) / (24 * 60 * 60 * 1000)
          this.orderInfo.orderTotalPrice = (this.form.price * this.form.number * 100).toFixed(2)
          this.orderInfo.orderFee = (this.orderInfo.orderTotalPrice * this.riskInfo.buyFee).toFixed(2)
          this.orderInfo.orderSpread = (this.orderInfo.orderTotalPrice * this.riskInfo.dutyFee).toFixed(2)
          // this.orderInfo.orderStayFee = (this.orderInfo.orderTotalPrice * this.riskInfo.stayFee * day).toFixed(2)
          // 浮动盈亏 = 买涨 ==> (现价 - 买入价) * 股（1手 = 100股） 0 涨 1 跌
          this.orderInfo.profitAndLose = this.form.type === 0 ? (this.stockInfo.nowPrice - this.form.price) * this.form.number * 100 : -(this.stockInfo.nowPrice - this.form.price) * this.form.number * 100
          // 总盈亏 = 买涨 ==> (现价 - 买入价) * 股（1手 = 100股）
          this.orderInfo.allProfitAndLose = this.orderInfo.profitAndLose - this.orderInfo.orderFee - this.orderInfo.orderSpread - this.orderInfo.orderStayFee
          this.cardShow = true
        } else {
          return false
        }
      })
    },
    async creatOrder () {
      // 创建持仓单
      this.loading = true
      // let stockId = this.stockInfo.stockGid
      let opts = {
        userId: this.form.userId,
        stockCode: this.form.code,
        buyPrice: this.form.price,
        buyNum: this.form.number * 100,
        buyTime: this.form.time,
        buyType: this.form.type,
        lever: this.form.lever
      }
      let data = await api.CreateOrderlist(opts)
      if (data.status === 0) {
        this.$message.success('创建成功')
        this.cardShow = false
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    selectStock () {
      // 选择股票
    },
    querySearchAsync (queryString, cb) {
      var restaurants = this.restaurants
      var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants

      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 3000 * Math.random())
    },
    createStateFilter (queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    handleSelect (item) {
      console.log(item)
    }
  }
}
</script>
<style lang="less" scoped>
  .info-box {
    padding: 10px;
    border-bottom: 1px solid #ddd;
    margin-bottom: 15px;

    .el-col {
      margin-bottom: 15px;

      .title {
        color: #666;
      }

      .number {
        font-size: 16px;
      }
    }
  }

  .text-center .el-button {
    margin-top: 20px;
  }
</style>
