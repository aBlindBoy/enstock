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
                总资金：￥
                <span class="number">
                                {{info?Number(info.userAmt + info.userIndexAmt).toFixed(2):'-'}}
                            </span>
              </el-col>
            </el-row>
          </div>
          <div class="text box-content">
            <!-- <div>资金信息</div>
            <hr> -->
            <el-row class="account-box">
              <el-col :span="8">
                <p class="title">融资总资金</p>
                <p class="number">￥{{info?info.userAmt:'-'}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">指数总资金</p>
                <p class="number">￥{{info?info.userIndexAmt:'-'}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">期货总资金</p>
                <p class="number">￥{{info?info.userFutAmt:'-'}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">融资可用资金</p>
                <p class="number">￥{{info?info.enableAmt:'-'}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">指数可用资金</p>
                <p class="number">￥{{info?info.enableIndexAmt:'-'}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">期货可用资金</p>
                <p class="number">￥{{info?info.enableFutAmt:'-'}}</p>
              </el-col>
            </el-row>
            <div>基本信息</div>
            <hr>
            <el-row>
              <el-col :span="8">
                用户id：
                <span>
                                {{info?info.id:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                手机号码：
                <span>
                                {{info?info.phone:'-'}}
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
                所属代理：
                <span>
                                {{info?info.agentName:'-'}}
                            </span>
              </el-col>
            </el-row>
            <div>认证信息</div>
            <hr>
            <el-row>
              <el-col :span="8">
                真实姓名：
                <span>
                                {{info?info.realName:'-'}}
                            </span>
              </el-col>
              <el-col :span="16">
                身份证号码：
                <span>
                                {{info?info.idCard:'-'}}
                            </span>
              </el-col>
            </el-row>
            <el-row class="img-info">
              <el-col :span="8">
                身份证背面：
                <img :src="info.img1Key" alt="">
              </el-col>
              <el-col :span="8">
                身份证正面：
                <img :src="info.img2Key" alt="">
              </el-col>
              <el-col :span="8">
                手持身份证：
                <img :src="info.img3Key" alt="">
              </el-col>
            </el-row>
            <el-row class="auxiliary">
              <el-col :span="8">
                注册ip：
                <span>
                                {{info?info.regIp:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                注册地址：
                <span>
                                {{info?info.regAddress:'-'}}
                            </span>
              </el-col>
            </el-row>
            <el-row class="auxiliary">
              <el-col v-if="info" :span="24">
                注册时间：
                <span>
                                {{info.regTime | timeFormat}}
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
    agentList: {},
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
  .el-col img {
    max-height: 230px;
    max-width: 230px;
    line-height: 230px;
    vertical-align: middle;
  }

  hr {
    border-color: #ececec;
  }

  .account-box {
    text-align: center;
    background: #f1f3f8;
    border-radius: 8px;
    padding-top: 10px;

    .title {
      font-size: 12px;
      color: #acafb8;
      line-height: 20px;
    }

    .number {
      line-height: 20px;
      margin-bottom: 8px;
    }
  }
</style>
