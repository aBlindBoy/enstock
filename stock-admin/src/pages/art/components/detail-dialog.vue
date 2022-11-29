<template>
  <div class="wrapper">
    <el-dialog
      title="公告详情 "
      :visible.sync="dialogVisible"
      width="70%"
    >
      <div>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col class="text-center" v-if="info" :span="24">
                {{info.artTitle?info.artTitle:'-'}}
              </el-col>
            </el-row>
          </div>
          <div v-if="info" class="text box-content">
            <p class="summary">
              {{info.artSummary}}
            </p>
            <div v-if="info.artImg" class="text-center">
              <img class="info-img" :src="info.artImg"/>
            </div>
            <p class="content">
              {{info.artCnt}}
            </p>
            <el-row>
              <el-col :span="8">
                公告类型：
                <span>
                                {{info.artType}}
                            </span>
              </el-col>
              <el-col :span="8">
                来源：
                <span>
                                {{info?info.author:'-'}}
                            </span>
              </el-col>
              <el-col :span="8">
                显示状态：
                <span>
                                {{info.isShow == 0?'显示中':'隐藏'}}
                            </span>
              </el-col>
            </el-row>
            <el-row class="auxiliary">
              <el-col :span="8">
                点击次数：
                <span>
                                {{info?info.hitTimes:'-'}}
                            </span>
              </el-col>
              <el-col :span="16">
                链接：
                <span>
                                {{info?info.spiderUrl:'-'}}
                            </span>
              </el-col>
            </el-row>
            <p class="auxiliary">
              创建时间：
              <span>
                  {{info?info.addTime:'-'}}
              </span>
            </p>
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
  .info-img {
    height: 150px;
  }

  .content {
    text-indent: 2em;
    margin-bottom: 10px;
    padding-bottom: 10px;
    border-bottom: 1px dotted #ccc;
  }

  .summary {
    color: #696969;
    text-indent: 2em;
  }
</style>
