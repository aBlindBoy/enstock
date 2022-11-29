<template>
  <div class="wrapper">
    <el-dialog
      title="操作详情 "
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :span="12">
                {{info?info.userName:'-'}}
                <span class="small">
                               ({{info?info.userId:'-'}})
                            </span>
              </el-col>
              <el-col :span="12" class="text-right">
                            <span>
                               {{info?info.deType:'-'}}
                            </span>
                ￥{{info?info.deAmt:'-'}}
              </el-col>

            </el-row>
          </div>
          <div class="text box-content">
            <!-- <el-row>
               <el-col :span="8">
                   用户名：
                   <span>
                       {{info?info.nickName:'-'}}
                   </span>
               </el-col>
               <el-col :span="8">
                   手机号：
                   <span>
                       {{info?info.nickName:'-'}}
                   </span>
               </el-col>
               <el-col :span="8">
                   用户名：
                   <span>
                       {{info?info.nickName:'-'}}
                   </span>
               </el-col> -->
            <!-- </el-row> -->
            <el-row>
              {{info?info.deSummary:''}}
            </el-row>
            <el-row>
              <!-- <el-col class="text-right" :span="12">
                  订单号：
                  <span>
                      {{info?info.buyOrderId:'-'}}
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
