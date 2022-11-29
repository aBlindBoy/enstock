<template>
  <div class="wrapper">
    <el-dialog
      title="任务详情 "
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div>
        <el-card v-if="info" class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :span="12">
                            <span>
                                {{info.taskType}}
                            </span>
              </el-col>
              <el-col class="text-right" :span="12">
                            <span :class="info.isSuccess == 0?'number green':'number red'">
                                {{info.isSuccess == 0?'成功':'失败'}}
                                <i v-if="info.isSuccess == 0" class="iconfont icon-zhengchang"></i>
                                <i v-if="info.isSuccess == 1" class="iconfont icon-failure"></i>
                            </span>
              </el-col>
            </el-row>
          </div>
          <div class="text box-content">
            <el-row>
              <el-col :span="24">
                任务id：
                <span>
                                {{info?info.id:''}}
                            </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                任务目标：
                <span>
                                {{info?info.taskTarget:'-'}}
                            </span>
              </el-col>
            </el-row>
            <el-row v-if="info.taskCnt">
              <el-col :span="24">
                任务描述：
                <span>
                                {{info?info.taskCnt:''}}
                            </span>
              </el-col>
            </el-row>
            <el-row v-if="info.errorMsg">
              <el-col :span="24">
                错误信息：
                <span>
                                {{info?info.errorMsg:''}}
                            </span>
              </el-col>
            </el-row>
            <el-row class="auxiliary">
              <el-col v-if="info" :span="24">
                执行时间：
                <span>
                                {{info.addTime | timeFormat}}
                            </span>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
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
  .number {
    .iconfont {
      font-size: 20px;
      vertical-align: middle;
    }
  }
</style>
