<template>
  <el-container>
    <el-main>
      <div class="containter">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>代理信息</span>
            <strong style="">【账号余额：{{detail.totalMoney}}】</strong>
            <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
          </div>
          <div class="text item">
            <el-row type="flex" justify="space-around">
              <el-col :span="8">
                代理名称：{{detail.agentName}}
              </el-col>
              <el-col :span="8">
                真实姓名：{{detail.agentRealName}}
              </el-col>
              <el-col :span="8">
                代理代码：{{detail.agentCode}}
              </el-col>
            </el-row>
            <el-row type="flex" justify="space-around">
              <el-col :span="8">
                锁定状态：
                {{detail.isLock == 0?'正常':'锁定'}}
              </el-col>
              <el-col :span="8">
                电话号码：{{detail.agentPhone}}
              </el-col>
              <el-col :span="8">
                创建时间：
                <span v-if="detail.addTime">{{detail.addTime | timeFormat}}</span>
                <span v-else></span>
              </el-col>
            </el-row>
            <el-row>
              链接（移动端）：
              <a :href="detail.murl" target="_blank">{{detail.murl}}</a>
              <el-button v-clipboard:copy="detail.murl"
                          v-clipboard:success="onCopy"
                          v-clipboard:error="onError"
                          type="text">复制
              </el-button>
            </el-row>
            <el-row>
              链接（pc端）：
              <a :href="detail.pcUrl" target="_blank">{{detail.pcUrl}}</a>
              <el-button v-clipboard:copy="detail.pcUrl"
                          v-clipboard:success="onCopy"
                          v-clipboard:error="onError"
                          type="text">复制
              </el-button>
            </el-row>
          </div>
        </el-card>
        <!-- <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>统计信息</span>
          </div>
          <div class="text item">
            <ChartBox></ChartBox>
          </div>
        </el-card> -->
      </div>
      <!-- <home-footer></home-footer> -->
    </el-main>
  </el-container>
</template>

<script>
import HomeHeader from '../components/HeaderOrder'
import HomeFooter from '../components/Footer'
import ChartBox from '../components/ChartBox'
import * as api from '@/axios/api'

export default {
  components: {
    HomeHeader,
    HomeFooter,
    ChartBox
  },
  props: {},
  data () {
    return {
      detail: {
        'agentName': '',
        'agentRealName': '',
        'agentPhone': ''
      }
    }
  },
  watch: {},
  // filters: {
  //   formatDate(time) {
  //       var date = new Date(time);
  //       return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
  //   }
  // },
  computed: {},
  methods: {
    onCopy: function (e) {
      this.$message({
        message: '复制成功！',
        type: 'success'
      })
      // alert('复制内容: ' + e.text)
    },
    onError: function (e) {
      console.log(e)
      this.$message({
        message: '复制失败！',
        type: 'warning'
      })
    },
    async getAgentInfo () {
      let data = await api.getAgentInfo()
      if (data.status === 0) {
        this.detail = data.data
        this.$store.state.userInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    }
  },
  created () {
    this.$store.state.activeIndex = 'home'
  },
  mounted () {
    this.getAgentInfo()
  }
}
</script>
<style lang="stylus" scoped>
  .containter
    padding 0px;

  .el-row
    height 35px
    line-height 35px

  .box-card
    margin-bottom 15px
</style>
