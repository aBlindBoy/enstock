<template>
  <div>
    <el-form :inline="true" :model="info" class="demo-form-inline" size="small">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>期货设置</span>
          <span class="sub-tit">
                    填写规则请按照括号中的示例填写，":"统一为英文中的字符,百分比均采用小数来表示
                </span>
        </div>
        <div class="text item">
          <div class="title">期货买入比例</div>
          <p>
                    <span class="pro">
                        期货买入比例即期货买入的仓位,期货最多能够买的金额
                    </span>
          </p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="最大买入比例（例:0.8）">
                <el-input class="form-input" v-model="info.buyMaxPercent"></el-input>
                <el-tooltip class="item" effect="dark" content="最大买入比例，不得超过可用资金的百分比" placement="top-right">
                  <a href="javascript:;" title="最大买入比例，不得超过可用资金的百分比"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="title">
            期货强平设置
            <span class="sub-tit">
                        百分比均采用小数来表示,例:0.8表示80%
                    </span>
          </div>
          <p>
                    <span class="pro red">
                        强制平仓线计算规则：可用资金 + （冻结保证金 * 强制平仓比例）
                    </span>
          </p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="强平比例（例:0.8）">
                <el-input class="form-input" v-model="info.forceSellPercent"></el-input>
                <el-tooltip class="item" effect="dark" content="最大买入比例，不得超过可用资金的百分比" placement="top-right">
                  <a href="javascript:;" title="最大买入比例，不得超过可用资金的百分比"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="强制平仓提醒比例（例:0.5）">
                <el-input class="form-input" v-model="info.forceStopRemindRatio"></el-input>
                <el-tooltip class="item" effect="dark" content="最大买入比例，不得超过可用资金的百分比" placement="top-right">
                  <a href="javascript:;" title="最大买入比例，不得超过可用资金的百分比到达后提醒"><i class="iconfont icon-xiangqing"></i></a>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="title"> 设置多少分钟内同一只期货不得下单多少次(同一用户)</div>
          <el-row>
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buySameTimes"
                        placeholder="设置时间间隔"></el-input>
              分钟内同一只期货不得下单
            </el-form-item>
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buySameNums"
                        placeholder="下单次数"></el-input>
              次
            </el-form-item>
          </el-row>
          <div class="title"> 设置多少分钟内交易手数不得超过多少手(同一用户)</div>
          <el-row>
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buyNumTimes"
                        placeholder="设置时间间隔"></el-input>
              分钟内交易手数不得超过
            </el-form-item>
            <el-form-item label="">
              <el-input style="width:100px" type="number" class="form-input" v-model="info.buyNumLots"
                        placeholder="下单次数"></el-input>
              手
            </el-form-item>
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
      let data = await api.getFuturesRiskSetting()
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
        buyMaxPercent: this.info.buyMaxPercent, // 最大买入比例
        forceSellPercent: this.info.forceSellPercent, // 强平百分比 占用保证金的百分比
        buySameTimes: this.info.buySameTimes, // 最大购买数量
        buySameNums: this.info.buySameNums, // 最大购买数量
        buyNumTimes: this.info.buyNumTimes, // 最大购买数量
        buyNumLots: this.info.buyNumLots, // 最大购买数量
        forceStopRemindRatio: this.info.forceStopRemindRatio //强平提醒比例
      }
      let data = await api.futuresRiskSetting(opts)
      if (data.status === 0) {
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
