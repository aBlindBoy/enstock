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
                                {{info?info.userAmt:'-'}}
                            </span>
              </el-col>
            </el-row>
          </div>
          <div class="text box-content">
            <el-row>
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

            </el-row>
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
                锁定状态：
                <span>
                                {{info?info.isLock == 1?'锁定':'未锁定':''}}
                            </span>
              </el-col>
              <el-col :span="8">
                用户密码：
                <span>
                                {{info?info.userPwd:'-'}}
                            </span>
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
</style>
