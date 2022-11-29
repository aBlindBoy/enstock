<template>
  <div>
    <el-form :inline="true" :model="info" class="demo-form-inline" size="small">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>指数设置</span>
          <span class="sub-tit">
                    填写规则请按照括号中的示例填写，":"统一为英文中的字符,百分比均采用小数来表示
                </span>
        </div>
        <div class="text item">
          <div class="title">指数交易时间</div>
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
          <div class="title">指数买入比例</div>
          <p>
                    <span class="pro">
                        指数买入比例即指数买入的仓位,指数最多能够买的金额
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
            指数强平设置
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
          <div class="title">
            指数涨跌停限制
            <span class="sub-tit">
                        百分比均采用小数来表示,例:0.1表示10%
                    </span>
          </div>
          <p class="pro">
            在<span class="pro red">买涨</span>的情况下,当该指数涨幅达到<span class="pro red">涨停限制</span>的时候,用户不能买入;
            当该指数涨幅达到<span class="pro green">跌停限制</span>的时候,用户不能卖出.
          </p>
          <p class="pro">
            在<span class="pro green">买跌</span>的情况下,当该指数涨幅达到<span class="pro green">跌停限制</span>的时候,用户不能买入;
            当该指数涨幅达到<span class="pro red">涨停限制</span>的时候,用户不能卖出.
          </p>
          <!-- <p class="pro">
             当该指数涨幅达到涨停限制时,用户买入不能买涨,用户买跌的持仓单不能卖出;
          </p>
          <p class="pro">
             当该指数涨幅达到跌停限制时,用户买入不能买跌,用户买涨的持仓单不能卖出.
          </p> -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="涨停限制（例:0.1）">
                <el-input class="form-input" v-model="info.riseLimit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="跌停限制（例:0.1）">
                <el-input class="form-input" v-model="info.downLimit"></el-input>
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
      let data = await api.getIndexRiskSetting()
      if (data.status === 0) {
        this.info = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async SettingInfo () {
      // 设置风控
      let opts = {
        id: this.info.id,
        buyMaxPercent: this.info.buyMaxPercent, // 最大买入比例
        forceSellPercent: this.info.forceSellPercent, // 强平百分比 占用保证金的百分比
        transAmBegin: this.info.transAmBegin, // 交易时间 上午开始
        transAmEnd: this.info.transAmEnd, // 交易时间 上午结束
        transPmBegin: this.info.transPmBegin, // 交易时间 下午开始
        transPmEnd: this.info.transPmEnd, // 交易时间 下午结束
        downLimit: this.info.downLimit,
        riseLimit: this.info.riseLimit,
        forceStopRemindRatio: this.info.forceStopRemindRatio //强平提醒比例
      }
      let data = await api.indexRiskSetting(opts)
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
