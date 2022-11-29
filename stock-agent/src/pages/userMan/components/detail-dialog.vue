<template>
  <div class="wrapper">
    <el-dialog
      title="用户详情 "
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col v-if="info" :span="12">
                            <span>
                                {{info.realName?info.realName:'未认证'}}
                            </span>
              </el-col>
              <el-col class="text-right" :span="12">
                账户总资金：
                <span class="number">
                                {{info?(info.userAmt + info.userIndexAmt + info.userFuturesAmt).toFixed(2):'-'}}
                            </span>
              </el-col>
            </el-row>
          </div>
          <div class="text box-content">
            <div class="class">
              融资资金
            </div>
            <el-row>
              <el-col :span="8">
                总资金：
                <span>
                                {{info?info.userAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                可用资金：
                <span>
                                {{info?info.enableAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                冻结保证金：
                <span>
                                {{info?info.allFreezAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                平仓线：
                <span class="yellow">
                                {{info?info.forceLine:'-'}}
                            </span>
              </el-col>
            </el-row>
            <div class="class">
              指数资金
            </div>
            <el-row>
              <el-col :span="8">
                总资金：
                <span>
                                {{info?info.userIndexAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                可用资金：
                <span>
                                {{info?info.enableIndexAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                冻结保证金：
                <span>
                                {{info?info.allIndexFreezAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                平仓线：
                <span class="yellow">
                                {{info?info.indexForceLine:'-'}}
                            </span>
              </el-col>
            </el-row>
            <div class="class">
              期货资金
            </div>
            <el-row>
              <el-col :span="8">
                总资金：
                <span>
                                {{info?info.userFuturesAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                可用资金：
                <span>
                                {{info?info.enableFuturesAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                冻结保证金：
                <span>
                                {{info?info.allFuturesFreezAmt:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                平仓线：
                <span class="yellow">
                                {{info?info.futuresForceLine:'-'}}
                            </span>
              </el-col>
            </el-row>
            <div class="class">
              基本信息
            </div>
            <el-row>
              <el-col :span="8">
                手机号码：
                <span>
                                {{info?info.phone:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                所属代理：
                <span>
                                {{info?info.agentName:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                用户id：
                <span>
                                {{info?info.id:'-'}}
                            </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                账号类型：
                <span>
                                {{info?info.accountType == 1?'模拟用户':'实盘用户':''}}
                            </span>
              </el-col>
              <el-col :span="8">
                交易状态：
                <span :class="info.isLock == 1?'red':''">
                                {{info?info.isLock == 1?'不可交易':'正常':''}}
                            </span>
              </el-col>
              <el-col :span="8">
                登录状态：
                <span :class="info.isLogin == 1?'red':''">
                                {{info?info.isLogin == 1?'不可登录':'正常':''}}
                            </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                银行名称：
                <span>
                                {{info?info.bankName:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                支行名称：
                <span>
                                {{info?info.bankAddress:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                银行卡号：
                <span>
                                {{info?info.bankNo:'-'}}
                            </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                身份证：
                <span>
                                {{info?info.idCard:'未认证'}}
                            </span>
              </el-col>
              <!-- <el-col :span="8" >
                  留仓费：
                  <span>
                      {{info?info.orderStayFee:'-'}}
                  </span>
              </el-col> -->
            </el-row>
          </div>
        </el-card>
      </div>
      <!-- <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
      </span> -->
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {
    info: {
      type: Object,
      default: function () {
        return {
          accountType: 0,
          agentId: 1,
          agentName: '总代理',
          enableAmt: 11266.34,
          id: 11,
          idCard: '132567788432',
          isActive: 2,
          isLock: 0,
          phone: '18163912132',
          profitAndLose: 0,
          realName: '认证',
          regAddress: '局域网',
          riskRate: 1,
          userAmt: 11266.34
        }
      }
    }
  },
  data () {
    return {
      dialogVisible: false
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
  },
  methods: {}
}
</script>
<style lang="less" scoped>
  .box-content {
    .class {
      color: #acafb8;
      border-bottom: 1px solid #f1f3f8;
    }

    .yellow {

    }
  }
</style>
