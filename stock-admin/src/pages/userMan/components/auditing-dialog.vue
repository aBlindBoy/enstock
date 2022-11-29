<template>
  <div class="wrapper">
    <el-dialog
      title="实名审核 "
      :visible.sync="dialogVisible"
      width="60%">
      <div>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col v-if="info" :span="12">
                            <span>
                                <a class="blue" title="待认证"><i v-if="info.isActive == 0"
                                                               class="iconfont icon-iconfontweitongguo"></i></a>
                                <a class="red" title="待审核"><i v-if="info.isActive == 1"
                                                              class="iconfont icon-dengdai"></i></a>
                                <a class="green" title="认证成功"><i v-if="info.isActive == 2"
                                                                 class="iconfont icon-tongguo4"></i></a>
                                <a class="yellow" title="驳回"><i v-if="info.isActive == 3"
                                                                class="iconfont icon-audi1Reject"></i></a>
                            </span>
              </el-col>
              <el-col v-show="info.isActive == 1 || info.isActive == 3" class="text-right" :span="12">
                <el-button size="mini" type="primary" @click="successInfo1(2)">通过</el-button>
                <el-button size="mini" type="dranger" @click="successInfo(3)">驳回</el-button>
              </el-col>
            </el-row>
          </div>
          <div class="text box-content">
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
    getDate: {
      type: Function,
      default: function () {

      }
    },
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
  methods: {
    async successInfo1 (val) {
      let opts = {
        userId: this.info.id,
        state: val
      }
      let data = await api.authByAdmin(opts)
      if (data.status === 0) {
        this.getDate()
        this.dialogVisible = false
      } else {
        this.$message.error(data.msg)
      }
    },
    successInfo (val) {
      this.$prompt('请输入驳回原因', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(async ({ value }) => {
        let opts = {
          userId: this.info.id,
          state: val,
          authMsg: value
        }
        let data = await api.authByAdmin(opts)
        if (data.status === 0) {
          this.getDate()
          this.dialogVisible = false
        } else {
          this.$message.error(data.msg)
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    }
  }
}
</script>
<style lang="less" scoped>
  .img-info {
    img {
      width: 200px;
    }
  }
</style>
