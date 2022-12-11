<template>
  <div class="indexdialog dialog-wrapper">
    <el-dialog
      title="futures purchase"
      :center='true'
      :visible.sync="DialogVisible"
      width="500px">
      <div class="buy-table">
        <el-tabs v-model="activeName" class="black-style">
          <el-tab-pane label="trade" name="first">
            <!-- 买 -->
            <div class="buy-box">
              <el-form ref="ruleForm" :hide-required-asterisk='true' size="mini" :model="form" :rules="rule"
                       label-width="60px">
                <div class="buy-item">
                  <span class="buy-name">{{futuresInfo.futuresName}}</span><span class="buy-code">{{futuresInfo.futuresCode}}</span>
                </div>
                <el-row class="buy-item">
                  <el-col :span="12">
                    latest price:<span
                    :class="((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) < 0 ?'green price':((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) > 0 ?'red price':'price'">{{detail.buyPrice}} {{futuresInfo.coinCode}}</span>
                  </el-col>
                  <el-col :span="12">
                    <!-- 涨跌幅： -->
                    <span style="margin-right:20px;"
                          :class="((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) < 0 ?'green price':((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) > 0 ?'red price':'price'">{{Number(detail.buyPrice - detail.lastClose).toFixed(2)}} {{futuresInfo.coinCode}}</span>
                    <span
                      :class="((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) < 0 ?'green price':((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2) > 0 ?'red price':'price'">{{((detail.buyPrice - detail.lastClose)/detail.lastClose).toFixed(2)}}%</span>
                  </el-col>
                </el-row>
                <!-- <el-form-item label="买入价">
                    <el-input v-model="form.stock" placeholder="买入价格">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </el-form-item> -->
                <!-- <el-form-item label="手数" prop="buyNum">
                  <el-input placeholder="手数" v-model="form.buyNum" class="input-with-select">
                    <el-select v-model="form.buyNum" title="点击选择手数" slot="prepend" placeholder="请选择">
             
                      <el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>
                    </el-select>
                    <el-button slot="append">手</el-button>
                  </el-input>
                </el-form-item> -->
                <!-- <p class="prompt clearfix">
                  <span class="pull-left">最小购买{{Number(futuresInfo.minNum)}}手</span>
                  <span class="pull-right">最大可购买{{Number(futuresInfo.maxNum)}}手</span>
                </p> -->
                <el-form-item label="Direction" prop="buyType">
                  <el-radio-group v-model="form.buyType">
                    <el-radio label="bullish" value="0"></el-radio>
                    <el-radio label="bearish" value="1"></el-radio>
                  </el-radio-group>
                </el-form-item>

                <p class="prompt clearfix">
                  <span class="">{{form.buyNum*futuresInfo.futuresStandard}} {{futuresInfo.futuresUnit}}</span>
                  <span class="pull-right">Maximum purchase amount{{(settingInfo.buyMaxPercent * $store.state.userInfo.enableFuturesAmt).toFixed(2)}}USD</span>
                </p>
                <el-row class="buy-item">
                  <el-col :span="24">
                    need to pay:<span class="price">{{total?total:0}} {{futuresInfo.coinCode}}</span>
                    ≈ <span class="price">{{USDTotal?USDTotal:0}} USD</span>
                  </el-col>
                </el-row>
                <p class="prompt clearfix">
                  <span class="pull-right">Available funds: {{$store.state.userInfo.enableFuturesAmt}}USD</span>
                </p>
                <el-row class="buy-item text-center" style="font-size:12px;">
                  <!-- <el-checkbox class="check-box" v-model="agree" name="type"></el-checkbox>我同意<a href="javascript:;" @click="tradeDialogVisible = true">《{{siteInfo.tradeAgreeTitle}}》</a> -->
                </el-row>
              </el-form>
              <el-button :loading="loadingBtn" class="buy-button" type="primary" @click="onSubmit('ruleForm')">
                下单
              </el-button>
            </div>
          </el-tab-pane>
        </el-tabs>
        <el-dialog
          :title="siteInfo.tradeAgreeTitle"
          class="agree-dialog"
          :center='true'
          :visible.sync="tradeDialogVisible"
          append-to-body
          width="80%">
          <div class="dialog-iframe">
            <div class="content" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
                <p v-for="item in tradeAgreeText.split('。')" :key="item">{{item}}。</p>
            </div>
            <!-- <iframe class="iframe-box" :src="$store.state.siteInfo.tradeAgree" frameborder="0"></iframe> -->
            <div slot="footer" class="text-center dialog-footer clearfix">
              <el-button type="primary" @click="agreeTrade">I have read and agree {{siteInfo.tradeAgreeTitle}}</el-button>
            </div>
          </div>
        </el-dialog>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as api from '../../../axios/api'
  import { Toast } from 'mint-ui'

  export default {
    components: {},
    props: {
      futuresInfo: {
        type: Object,
        default () {
          return {
            futuresName: '',
            futuresCode: '',
            currentPoint: '',
            floatPoint: '',
            floatRate: '',
			siteInfo:{}
          }
        }
      },
      handleOptions3: {
        type: Function,
        default: function () {}
      }
    },
    data () {
      return {
        loadingBtn: false, // 下单
        DialogVisible: false,
        tradeDialogVisible: false, //
        loading: false,
        tradeAgreeText: '',
        detail: '', // 当前股票的详情
        activeName: 'first',
        // buyNumList: [
        //   { label: '1手', value: 1 }
        // ],
        form: {
          buyNum: '',
          buyType: '',
          lever: ''
        },
        rule: {
          buyNum: [
            { required: true, message: 'Please enter or select the lot size to buy', trigger: 'blur' }
          ],
          buyType: [
            { required: true, message: 'Please select the buying and selling direction', trigger: 'blur' }
          ]
        },
        settingInfo: {}, // 设置信息
        agree: true, // 协议
        buyNumber: 0, // 下单次数
        exchangeNumber: '' // 获取汇率
      }
    },
    watch: {
      futuresInfo (newVal) {
        if (newVal) {
          // this.buyNumList = []
          // for (let i = 0; i < 10; i++) {
          //   if (i === 0 || i % 2 === 1) {
          //     let val = newVal.minNum * i + newVal.minNum
          //     let item = { label: val + '手', value: val }
          //     this.buyNumList.push(item)
          //   }
          // }
         // this.getDetail()
          this.queryExchange() // 获取当前基币汇率
        }
      }
    },
    computed: {
      total () {
        if (this.form.buyNum) {
          return (this.futuresInfo.depositAmt * this.form.buyNum).toFixed(2)
        } else {
          return 0
        }
        //   每手保证金 * 手数  = 价钱
      },
      USDTotal () {
        // 价钱 * 汇率 转为 人民币
        return (this.total * this.exchangeNumber).toFixed(2)
      },
      price () {
        if (this.form.buyNum) {
          return (this.futuresInfo.depositAmt * this.form.buyNum).toFixed(2)
        } else {
          return 0
        }
        // 市值价 = 现价 * 股（1手 = 100股）
      }
    },
    created () {
    //  this.timer = setInterval(this.getDetail, 10000)
    },
    beforeDestroy () {
      // clearInterval(this.timer)
    },
    mounted () {
      if (this.$store.state.haslogin) {
        this.getUserInfo()
      }
      this.getSettingInfo()
      this.getInfoSite()
    },
    methods: {
      async queryExchange () {
        // 基币汇率
        let data = await api.queryExchange({ coinCode: this.futuresInfo.coinCode })
        if (data.status === 0) {
          // 成功
          this.exchangeNumber = data.data
        } else {
          Toast(data.msg)
        }
      },
      async getInfoSite () {
        // 获取网站信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
          this.tradeAgreeText = this.siteInfo.tradeAgreeText
        } else {
          this.$message.error(result.msg)
        }
      },
      async getDetail () {
        let opts = {
          futuresGid: this.futuresInfo.futuresGid
        }
        this.loading = true
        let data = await api.querySingleMarket(opts)
        this.loading = false
        if (data.status === 0) {
          this.detail = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      async getSettingInfo () {
        // 网站设置信息 期货
        let data = await api.getFuturesSetting()
        if (data.status === 0) {
          // 成功
          this.settingInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          this.$store.state.userInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      onSubmit (formName) {
        // 先判断是否登录
        if (!this.$store.state.haslogin) {
          this.$store.state.loginIsShow = true
          this.$message.error('please log in first')
        } else {
          this.$refs[formName].validate(async (valid) => {
            if (valid) {
              if (!this.agree) {
                this.$message.error('Read and agree to the registration agreement to place an order')
                return
              }
              // 买入
              let opts = {
                FuturesId: this.futuresInfo.id,
                buyNum: this.form.buyNum ? this.form.buyNum : 0,
                buyType: this.form.buyType === 'bullish' ? 0 : 1
              }
              this.loadingBtn = true
              let data = await api.buyFutures(opts)
              if (data.status === 0) {
                this.buyNumber++
                this.handleOptions3(this.buyNumber)
                this.$message.success(data.data)
                this.DialogVisible = false
                this.getUserInfo() // 刷新
              } else {
                this.$message.error(data.msg)
              }
              this.loadingBtn = false
            }
          })
        }
      },
      agreeTrade () {
        // 同意注册协议协议
        this.agree = true
        this.tradeDialogVisible = false
      }
    }
  }
</script>
<style lang="less" scoped>
  .nowprice-box {
    height: 40px;
    line-height: 38px;
    color: #8b97b0;

    .price {
      font-size: 16px;
    }
  }

  .transaction-table .buy-table {
    /deep/ th.is-leaf {
      display: none;
    }
  }

  .buy-box {
    .buy-item {
      margin-bottom: 16px;

      .price {
        font-size: 16px;
      }
    }

    .prompt {
      font-size: 12px;
      color: #606266;
      margin-bottom: 10px;
    }

    .buy-name {
      font-size: 16px;
      color: #efbb53;
    }

    .buy-code {
      color: #4c5770;
      font-size: 12px;
      margin-left: 10px;
    }

    .buy-button {
      width: 100%;
    }

    .check-box {
      margin-right: 10px;
    }

    /deep/ .el-form-item__label {
      color: #8b97b0;
      text-align: left;
    }

    // /deep/ .el-input__inner{
    //     background: #202838;
    //     border-color: #202838;
    // }
    // /deep/ .el-checkbox__inner{
    //     color: #efbb53;
    //     background: #202838;
    // }
    // /deep/ .el-radio__inner{
    //    background: #202838;
    // }
    // 涨
    /deep/ .el-radio__input.is-checked + .el-radio__label {
      color: #c11815;
    }

    /deep/ .el-radio__input.is-checked .el-radio__inner {
      border-color: #c11815;
      background: #c11815;
    }

    // /deep/ .el-input-group__append{
    //     background: #202838;
    //     border-color: #202838;
    // }
    // /deep/ .el-input-group__prepend{
    //     background: #202838;
    //     border-color: #202838;
    // }
    /deep/ .el-radio {
      color: #8b97b0;
    }
  }

  .dialog-iframe {
    .iframe-box {
      width: 100%;
      height: 460px;
    }

    .dialog-footer {
      //  height: 300px;
      margin-top: 20px;
    }
  }

  // .dialog-wrapper /deep/ .el-dialog{
  //     background: #171e2a;
  //     .el-dialog__body{
  //         padding-top: 0px;
  //     }
  //     .el-dialog__title{
  //         color: #c3d4f7;
  //     }
  // }
</style>
