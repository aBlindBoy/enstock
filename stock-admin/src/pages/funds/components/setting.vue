<template>
  <div>
    <el-form :inline="true" :model="info" class="demo-form-inline" size="small">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>保证金设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="保证金最小值（例:100，单位元）">
                <el-input class="form-input" v-model="info.marginMin" placeholder="保证金最小值"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="保证金最大值（例:10000，单位元）">
                <el-input class="form-input" v-model="info.marginMax" placeholder="保证金最大值"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>免息设置</span>
          <span class="sub-tit">
                    填写规则请按照括号中的示例填写，":"统一为英文中的字符，提现时间为24小时制，请填写整数
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="6">
              <el-form-item label="免息杠杆（例：1，填倍数）">
                <el-input class="form-input" v-model="info.interestFreeLever" placeholder="免息杠杆"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="免息天数（例：5，单位天）">
                <el-input class="form-input" v-model="info.interestFreeDays" placeholder="免息天数"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="免息盈利（例：0.85，百分比）">
                <el-input class="form-input" v-model="info.interestFreeProfit" placeholder="免息盈利"></el-input>
              </el-form-item>
            </el-col>
            
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="免息预警线（例：0.75）">
                <el-input class="form-input" v-model="info.interestFreeWarning" placeholder="免息预警线"></el-input>
                <el-tooltip class="item" effect="dark" content="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"
                            placement="top-left">
                  <a href="javascript:;" title="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"><i
                    class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="免息平仓线（例：0.82）">
                <el-input class="form-input" v-model="info.interestFreeUnwind" placeholder="免息平仓线"></el-input>
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
          <span>按天设置</span>
          <span class="sub-tit">
                    填写规则请按照括号中的示例填写，":"统一为英文中的字符，提现时间为24小时制，请填写整数
                </span>
        </div>
        <div class="text item">
          <el-row>
            <el-col :span="24">
              <el-form-item label="使用期限（例：2|3）">
                <el-input class="form-input" v-model="info.daysUsePeriod" placeholder="免息天数" style="width:1000px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="按天预警线（例：0.75，百分比）">
                <el-input class="form-input" v-model="info.daysWarning" placeholder="按天预警线"></el-input>
                <el-tooltip class="item" effect="dark" content="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"
                            placement="top-left">
                  <a href="javascript:;" title="用户可以发起提现的开始时间，时间为24小时制的整点数。例如9即为09:00"><i
                    class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="按天平仓线（例：0.82，百分比）">
                <el-input class="form-input" v-model="info.daysUnwind" placeholder="按天平仓线"></el-input>
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
          <span>交易设置</span>
          <span class="sub-tit">
                    请按照括号中的示例填写,比例均采用小数来表示
                </span>

        </div>
        <div class="text item">
          <!-- <p>
                    <span class="pro">
                        强制平仓线计算规则：可用资金 + （冻结保证金 * 强制平仓比例）
                    </span>
          </p> -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="交易佣金费率（例:0.75，百分比）">
                <el-input class="form-input" v-model="info.tradingCommissionRate" placeholder="交易佣金费率，（总资金的75%）"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="每笔最低费用（例:5，单位元）">
                <el-input class="form-input" v-model="info.stampDutyRate" placeholder="每笔最低费用"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="盈利分成比例（例:0.85，百分比）">
                <el-input class="form-input" v-model="info.profitSharingRatio" placeholder="盈利分成比例"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="提前终止利息（例:0.25，百分比）">
                <el-input class="form-input" v-model="info.earlyTerminationInterest" placeholder="提前终止利息"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
        </div>
      </el-card>
      
      <el-row class="text-center submit-btn">
        <el-button type="primary" @click="saveFundsSetting">保存设置信息</el-button>
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
    this.getFundsSetting()
  },
  methods: {
    async getFundsSetting () {
      let data = await api.getFundsSetting()
      if (data.status === 0) {
        this.info = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async saveFundsSetting () {
      // 设置风控
      let opts = {
        id: this.info.id, // id
        marginMin: this.info.marginMin, // 保证金最小值
        marginMax: this.info.marginMax, // 保证金最大值
        interestFreeLever: this.info.interestFreeLever, // 免息杠杆
        interestFreeDays: this.info.interestFreeDays, // 免息天数
        interestFreeProfit: this.info.interestFreeProfit, // 免息盈利
        interestFreeWarning: this.info.interestFreeWarning, // 免息预警线
        interestFreeUnwind: this.info.interestFreeUnwind, // 免息平仓线
        daysWarning: this.info.daysWarning, // 按天预警线
        daysUnwind: this.info.daysUnwind, // 按天平仓线
        earlyTerminationInterest: this.info.earlyTerminationInterest, // 提前终止利息
        tradingCommissionRate: this.info.tradingCommissionRate, // 交易佣金费率
        stampDutyRate: this.info.stampDutyRate, // 每笔最低费用
        profitSharingRatio: this.info.profitSharingRatio, // 盈利分成比例
        daysUsePeriod: this.info.daysUsePeriod // 按天使用期限
        
      }
      let data = await api.saveFundsSetting(opts)
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
