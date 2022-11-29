<template>
  <div>
    <el-form :inline="true" :model="info" class="demo-form-inline" size="small">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>时间设置</span>
          <span class="sub-tit">
                    填写规则请按照括号中的示例填写，":"统一为英文中的字符，提现时间为24小时制，请填写整数
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="上午开始交易时间（例：9:30）">
                <el-input class="form-input" v-model="info.transAmBegin" placeholder="交易时间"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="上午结束交易时间（例：10:30）">
                <el-input class="form-input" v-model="info.transAmEnd" placeholder="交易时间"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="下午开始交易时间（例：13:30）">
                <el-input class="form-input" v-model="info.transPmBegin" placeholder="交易时间"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="下午结束交易时间（例：15:00）">
                <el-input class="form-input" v-model="info.transPmEnd" placeholder="交易时间"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="提现开始时间（例：9）">
                <el-input class="form-input" v-model="info.withTimeBegin" placeholder="提现开始时间"></el-input>
                <el-tooltip class="item" effect="dark" content="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"
                            placement="top-left">
                  <a href="javascript:;" title="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"><i
                    class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="提现结束时间（例：18）">
                <el-input class="form-input" v-model="info.withTimeEnd" placeholder="提现结束时间"></el-input>
                <el-tooltip class="item" effect="dark" content="用户可以发起提现的结束时间，时间为24小时制的整点数。例如18即为18:00"
                            placement="top-right">
                  <a href="javascript:;" title="用户可以发起提现的结束时间，时间为24小时制的整点数。例如18即为18:00"><i
                    class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>费用设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="买入手续费（例:0.001）">
                <el-input class="form-input" v-model="info.buyFee" placeholder="买入手续费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="卖出手续费（例:0.001）">
                <el-input class="form-input" v-model="info.sellFee" placeholder="卖出手续费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="留仓费（例:0.001）">
                <el-input class="form-input" v-model="info.stayFee" placeholder="留仓费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="印花税（例:0.001）">
                <el-input class="form-input" v-model="info.dutyFee" placeholder="印花税"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>购买设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="最小购买金额（例:1000）">
                <el-input class="form-input" v-model="info.buyMinAmt" placeholder="最小购买金额"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最大买入比例（例:0.8）">
                <el-input class="form-input" v-model="info.buyMaxAmtPercent"
                          placeholder="最大买入比例，不得超过可用资金的80%"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最小购买股数（例:5000）">
                <el-input class="form-input" v-model="info.buyMinNum" placeholder="最小购买股数"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最大买入股数（例:1000000）">
                <el-input class="form-input" v-model="info.buyMaxNum" placeholder="最大买入股数"></el-input>
                <el-tooltip class="item" effect="dark" content="单笔订单最大买入股数" placement="top-right">
                  <a href="javascript:;" title="单笔订单最大买入股数"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="杠杆倍数（例:100/50/30）">
                <el-input class="form-input" v-model="info.siteLever" placeholder="杠杆倍数"></el-input>
                <el-tooltip class="item" effect="dark" content="配资的杠杆倍数,多个用/分割" placement="top-right">
                  <a href="javascript:;" title="配资的杠杆倍数,多个用/分割"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="买入多长时间内不能平仓/分钟（例:30）">
                买入
                <el-input class="form-input" type="number" style="width:100px;" v-model="info.cantSellTimes"
                          placeholder="买入多长时间内不能平仓/分钟"></el-input>
                <!-- <el-tooltip class="item" effect="dark" content="买入多长时间内不能平仓,如果您设置的时间为30,则表示买入之后30分钟内不能平仓" placement="top-right">
                    <a href="javascript:;" title="买入多长时间内不能平仓,如果您设置的时间为30,则表示买入之后30分钟内不能平仓"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip> -->
                分钟内不能平仓
              </el-form-item>
            </el-col>
            <!--<el-col :span="6">
               <el-form-item  label="快速平仓时间设定（例:2）">
                   <el-input class="form-input" v-model="info.creaseMaxPercent" placeholder="快速平仓时间设置"></el-input>
                   <el-tooltip class="item" effect="dark" content="" placement="top-right">
                       <a href="javascript:;" title="买入之后"><i class="iconfont icon-xiangqing"></i></a>
                   </el-tooltip>
               </el-form-item>
           </el-col>
           <el-col :span="6">
               <el-form-item  label="快速平仓附加手续费（例:0.006）">
                   <el-input class="form-input" v-model="info.creaseMaxPercent" placeholder="快速平仓附加手续费"></el-input>
                   <el-tooltip class="item" effect="dark" content="" placement="top-right">
                       <a href="javascript:;" title="买入"><i class="iconfont icon-xiangqing"></i></a>
                   </el-tooltip>
               </el-form-item>
           </el-col> -->

          </el-row>
          <div class="title"> 设置多少分钟内同一只股票不得下单多少次(同一用户)</div>
          <el-row>
            <!-- <el-col :span="6"> -->
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buySameTimes"
                        placeholder="设置时间间隔"></el-input>
              <!-- <el-tooltip class="item" effect="dark" content="设置时间间隔,例30: 表示设置30分钟内" placement="top-right">
                  <a href="javascript:;" title="设置时间间隔,例30: 表示设置30分钟内"><i class="iconfont icon-xiangqing"></i></a>
              </el-tooltip> -->
              分钟内同一只股票不得下单
            </el-form-item>
            <!-- </el-col>
            <el-col :span="6"> -->
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buySameNums"
                        placeholder="下单次数"></el-input>
              <!-- <el-tooltip class="item" effect="dark" content="例10: 表示多少分钟内同一只股票不得下单10次" placement="top-right">
                  <a href="javascript:;" title="例10: 表示多少分钟内同一只股票不得下单10次"><i class="iconfont icon-xiangqing"></i></a>
              </el-tooltip> -->
              次
            </el-form-item>
            <!-- </el-col> -->
          </el-row>
          <div class="title"> 设置多少分钟内交易手数不得超过多少手(同一用户)</div>
          <el-row>
            <!-- <el-col :span="6"> -->
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buyNumTimes"
                        placeholder="设置时间间隔"></el-input>
              <!-- <el-tooltip class="item" effect="dark" content="设置时间间隔,例30: 表示设置30分钟内" placement="top-right">
                  <a href="javascript:;" title="设置时间间隔,例30: 表示设置30分钟内"><i class="iconfont icon-xiangqing"></i></a>
              </el-tooltip> -->
              分钟内交易手数不得超过
            </el-form-item>
            <!-- </el-col>
            <el-col :span="6"> -->
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buyNumLots"
                        placeholder="下单次数"></el-input>
              <!-- <el-tooltip class="item" effect="dark" content="例100: 表示多少分钟内交易手数不得超过100手" placement="top-right">
                  <a href="javascript:;" title="例100: 表示多少分钟内交易手数不得超过100手"><i class="iconfont icon-xiangqing"></i></a>
              </el-tooltip> -->
              手
            </el-form-item>

            <!-- </el-col> -->
          </el-row>
          <div class="title"> 同一股票连续 x 天 内涨幅超过 y 不能买入(同一用户)</div>
          <el-row>
            <el-form-item label="">
              同一股票连续
              <el-input style="width:100px" type="number" class="form-input" v-model="info.stockDays"
                        placeholder="设置时间间隔"></el-input>
              天 内涨幅超过
            </el-form-item>
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.stockRate"
                        placeholder="下单次数"></el-input>
              不能买入(同一用户)
            </el-form-item>
          </el-row>
          <div class="title"> 超过多少个点不能买入（例:7)</div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="A股">
                <el-input class="form-input" v-model="info.creaseMaxPercent" placeholder="超过多少个点不能买入"></el-input>
                <el-tooltip class="item" effect="dark" content="如您设置的点数为7,则该只股票今天涨了7个点就不能购买该只股票" placement="top-right">
                  <a href="javascript:;" title="如您设置的点数为7,则该只股票今天涨了7个点就不能购买该只股票"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="科创板">
                <el-input class="form-input" v-model="info.kcCreaseMaxPercent" placeholder="超过多少个点不能买入"></el-input>
                <el-tooltip class="item" effect="dark" content="如您设置的点数为7,则该只股票今天涨了7个点就不能购买该只股票" placement="top-right">
                  <a href="javascript:;" title="如您设置的点数为7,则该只股票今天涨了7个点就不能购买该只股票"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>强制平仓设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写,比例均采用小数来表示
                </span>

        </div>
        <div class="text item">
          <p>
                    <span class="pro">
                        强制平仓线计算规则：可用资金 + （冻结保证金 * 强制平仓比例）
                    </span>
          </p>
          <el-row>
            <el-col :span="6">
              <el-form-item label="强制平仓比例（例:0.7）">
                <el-input v-model="info.forceStopPercent" placeholder="强制平仓比例，（总资金的70%）"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="连续涨停强制平仓（例:0.2）">
                <el-input class="form-input" v-model="info.hightAndLow" placeholder="几个涨停强制平仓"></el-input>
                <el-tooltip class="item" effect="dark" content="设置用户订单连续几个涨停进行强制平仓" placement="top-right">
                  <a href="javascript:;" title="设置用户订单连续几个涨停进行强制平仓"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最大留仓天数（例:15）">
                <el-input class="form-input" v-model="info.stayMaxDays" placeholder="最大留仓天数"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="强制平仓手续费（例:0.001）">
                <el-input class="form-input" v-model="info.forceStopFee" placeholder="强制平仓手续费"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="强制平仓提醒比例（例:0.5）">
                <el-input v-model="info.forceStopRemindRatio" placeholder="强平提醒比例，（总资金的50%）"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>充值提现设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写,比例均采用小数来表示
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="最小充值金额（例:1000）">
                <el-input v-model="info.chargeMinAmt" placeholder="最小充值金额"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最小提现金额（例:1000）">
                <el-input v-model="info.withMinAmt" placeholder="最小提现金额"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="提现单笔手续费（例:5）">
                <el-input v-model="info.withFeeSingle" placeholder="提现单笔手续费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="提现手续费百分比（例:0.005）">
                <el-input v-model="info.withFeePercent" placeholder="提现手续费百分比"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <el-row class="text-center submit-btn">
        <el-button type="primary" @click="SettingInfo">保存设置信息</el-button>
      </el-row>
    </el-form>

    <!-- <DetailDialog  ref="detailDialog"></DetailDialog> -->
  </div>

</template>

<script>
import * as api from '@/axios/api'
// import DetailDialog from './detail-dialog'
export default {
  components: {
    // DetailDialog,
  },
  props: {},
  data () {
    return {
      info: {}
    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.getSettingInfo()
  },
  methods: {
    async getSettingInfo () {
      let data = await api.getSettingInfo()
      if (data.status === 0) {
        this.info = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async SettingInfo () {
      // 设置风控
      let opts = {
        id: this.info.id, // id
        buyFee: this.info.buyFee, // 买入手续费
        sellFee: this.info.sellFee, // 卖出手续费
        stayFee: this.info.stayFee, // 留仓费
        dutyFee: this.info.dutyFee, // 印花税
        stayMaxDays: this.info.stayMaxDays, // 最大留仓天数
        buyMinAmt: this.info.buyMinAmt, // 最小购买金额
        chargeMinAmt: this.info.chargeMinAmt, // 最小充值金额
        buyMinNum: this.info.buyMinNum, // 最小购买数量 单位 股
        forceStopFee: this.info.forceStopFee, // 强平手续费
        buyMaxAmtPercent: this.info.buyMaxAmtPercent, // 最大买入比例
        forceStopPercent: this.info.forceStopPercent, // 强平百分比 占用保证金的百分比
        hightAndLow: this.info.hightAndLow, // 涨跌停限制 如果2个涨停 填2
        withMinAmt: this.info.withMinAmt, // 最小提现金额
        creaseMaxPercent: this.info.creaseMaxPercent, // 买入控制 涨跌超过多少点不能买入 默认7个点
        kcCreaseMaxPercent: this.info.kcCreaseMaxPercent, // 买入控制 涨跌超过多少点不能买入 默认7个点科创板
        buyMaxNum: this.info.buyMaxNum, // 最大购买数量
        cantSellTimes: this.info.cantSellTimes, // 最大购买数量
        buySameTimes: this.info.buySameTimes, // 最大购买数量
        buySameNums: this.info.buySameNums, // 最大购买数量
        buyNumTimes: this.info.buyNumTimes, // 最大购买数量
        buyNumLots: this.info.buyNumLots, // 最大购买数量
        stockDays: this.info.stockDays, // 最大购买数量
        stockRate: this.info.stockRate, // 最大购买数量

        withTimeBegin: this.info.withTimeBegin, // 提现开始时间
        withTimeEnd: this.info.withTimeEnd, // 提现结束时间
        transAmBegin: this.info.transAmBegin, // 交易时间 上午开始
        transAmEnd: this.info.transAmEnd, // 交易时间 上午结束
        transPmBegin: this.info.transPmBegin, // 交易时间 下午开始
        transPmEnd: this.info.transPmEnd, // 交易时间 下午结束
        withFeeSingle: this.info.withFeeSingle, // 提现单笔手续费
        withFeePercent: this.info.withFeePercent, // 提现手续费百分比
        siteLever: this.info.siteLever, // 杠杆倍数
        forceStopRemindRatio: this.info.forceStopRemindRatio //强平提醒比例
      }
      let data = await api.riskSetting(opts)
      if (data.status === 0) {
        // this.info = data.data
        this.$message.success(data.msg)
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .el-card .pro {
    font-size: 14px;
    color: #F44336;
    margin-left: -5px;
    line-height: 30px;
  }

  .sub-tit {
    font-size: 12px;
    color: #666;
    margin-left: 10px;
  }

  .form-input {
    width: 80%;
  }

  .item {
    a {
      color: #333;
    }
  }

  .submit-btn {
    margin: 20px 0;

    .el-button {
      width: 200px;
    }
  }

  .item {
    .title {
      border-bottom: 1px solid #eceff6;
      color: #606266;
      margin: 10px 0;
      text-indent: 1px;
      line-height: 35px;

      span {
        color: #999;
        font-size: 12px;
        margin-left: 10px;
      }

      &:before {
        content: "";
        width: 4px;
        height: 4px;
        background: #606266;
        display: inline-block;
        border-radius: 4px;
        margin-right: 5px;
        vertical-align: middle;
      }
    }
  }
</style>
