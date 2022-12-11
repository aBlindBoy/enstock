<template>
  <div class="indexdialog dialog-wrapper">
    <el-dialog
      title="index buying"
      :center='true'
      :visible.sync="DialogVisible"
      width="500px">
      <div class="buy-table">
        <el-tabs v-model="activeName" class="black-style">
          <el-tab-pane label="trade" name="first">
            <!-- 買 -->
            <div class="buy-box">
              <el-form ref="ruleForm" :hide-required-asterisk='true' size="mini" :model="form" :rules="rule"
                       label-width="60px">
                <div class="buy-item">
                  <span class="buy-name">{{indexInfo.indexName}}</span><span
                  class="buy-code">{{indexInfo.indexCode}}</span>
                </div>
                <el-row class="buy-item">
                  <el-col :span="8">
                    latest price：<span
                    :class="indexInfo.floatRate < 0 ?'green price':indexInfo.floatRate > 0 ?'red price':'price'">{{indexInfo.currentPoint}}</span>
                  </el-col>
                  <el-col :span="12">
                    <!-- Quote change： -->
                    <span style="margin-right:20px;"
                          :class="indexInfo.floatRate < 0 ?'green price':indexInfo.floatRate > 0 ?'red price':'price'">{{Number(indexInfo.floatPoint).toFixed(2)}}</span>
                    <span :class="indexInfo.floatRate < 0 ?'green price':indexInfo.floatRate > 0 ?'red price':'price'">{{indexInfo.floatRate}}%</span>
                  </el-col>
                </el-row>
                <!-- <el-form-item label="Buying price">
                    <el-input v-model="form.stock" placeholder="Buying price格">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </el-form-item> -->
                <!-- <el-form-item label="手數" prop="buyNum">
                  <el-input placeholder="手數" v-model="form.buyNum" class="input-with-select">
                    <el-select v-model="form.buyNum" title="點擊選擇手數" slot="prepend" placeholder="請選擇">
                 
                      <el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>
                    </el-select>
                    <el-button slot="append">手</el-button>
                  </el-input>
                </el-form-item> -->
                <p class="prompt clearfix">
                  <span class="pull-left">Minimum purchase{{Number(indexInfo.minNum)}}</span>
                  <span class="pull-right">Maximum available for purchase{{Number(indexInfo.maxNum)}}</span>
                </p>
                <el-form-item label="Direction" prop="buyType">
                  <el-radio-group v-model="form.buyType">
                    <el-radio label="bullish" value="0"></el-radio>
                    <el-radio label="bearish" value="1"></el-radio>
                  </el-radio-group>
                </el-form-item>

                <p class="prompt clearfix">
                  <span
                    class="pull-right">Maximum purchase amount {{settingInfo.buyMaxPercent * $store.state.userInfo.enableIndexAmt}}USD</span>
                </p>
                <el-row class="buy-item">
                  <el-col :span="24">
                    need to pay:<span class="price">{{total?total:0}}</span>
                  </el-col>
                </el-row>
                <p class="prompt clearfix">
                  <span class="pull-right">Available funds:{{$store.state.userInfo.enableIndexAmt}}USD</span>
                </p>
                <el-row class="buy-item text-center" style="font-size:12px;">
                  <!-- <el-checkbox class="check-box" v-model="agree" name="type"></el-checkbox>我同意<a href="javascript:;" @click="tradeDialogVisible = true">《{{siteInfo.tradeAgreeTitle}}》</a> -->
                </el-row>
              </el-form>
              <el-button :loading="loadingBtn" class="buy-button" type="primary" @click="onSubmit('ruleForm')">place an order
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

  export default {
    components: {},
    props: {
      indexInfo: {
        type: Object,
        default () {
          return {
            indexName: '',
            indexCode: '',
            currentPoint: '',
            floatPoint: '',
            floatRate: ''
          }
        }
      },
      handleOptions2: {
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
        detail: '', // 当前股票的详情
        activeName: 'first',
        tradeAgreeText: '',
        buyNumList: [
          { label: '1手', value: 1 }
        ],
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
        buyNumber: 0 // 下单次数
      }
    },
    watch: {
      indexInfo (newVal) {
        if (newVal) {
          this.buyNumList = []
          for (let i = 0; i < 10; i++) {
            if (i === 0 || i % 2 === 1) {
              let val = newVal.minNum * i + newVal.minNum
              let item = { label: val + '手', value: val }
              this.buyNumList.push(item)
            }
          }
          this.getDetail()
        }
      }
    },
    computed: {
      total () {
        if (this.form.buyNum) {
          return (this.indexInfo.depositAmt * this.form.buyNum).toFixed(2)
        } else {
          return 0
        }
        // 需支付总价 = 现价 * 股/ 杠杆倍数
      },
      price () {
        if (this.form.buyNum) {
          return (this.indexInfo.depositAmt * this.form.buyNum).toFixed(2)
        } else {
          return 0
        }
        // 市值价 = 现价 * 股（1手 = 100股）
      }
    },
    created () {
      this.timer = setInterval(this.getDetail, 10000)
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
      async getDetail () {
        let opts = {
          indexCode: this.indexInfo.indexGid
        }
        this.loading = true
        let data = await api.getSingleIndex(opts)
        this.loading = false
        if (data.status === 0) {
          this.detail = data.data
        } else {
          this.$message.error(data.msg)
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
      async getSettingInfo () {
        // 网站设置信息 指数
        let data = await api.getIndexSetting()
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
              // 買入
              let opts = {
                indexId: this.indexInfo.id,
                buyNum: this.form.buyNum ? this.form.buyNum : 0,
                buyType: this.form.buyType === 'bullish' ? 0 : 1
              }
              this.loadingBtn = true
              let data = await api.indexBuy(opts)
              if (data.status === 0) {
                this.buyNumber++
                this.handleOptions2(this.buyNumber)
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

  .indexdialog .el-dialog {
    // background: #171e2a;
    .el-dialog__body {
      padding-top: 0px;
    }

    .el-dialog__title {
      // color: #c3d4f7;
    }
  }
</style>
