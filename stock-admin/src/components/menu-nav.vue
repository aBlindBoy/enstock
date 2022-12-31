<template>
  <div 
    class="nav-left" 
    :unique-opened='true' 
    :style="contentStyleObj"
  >
    <el-scrollbar style="height: 100%">
      <el-menu 
        :default-openeds="index" 
        :default-active="itemMenu" 
        :collapse="isCollapse"
        background-color="#1f2636"
        text-color="rgb(191, 203, 217)"
        active-text-color="#409eff"
      >
        <!-- <div class="logo-img text-center">
          <img :src="siteInfo.siteLogo" alt="logo">
        </div> -->
        
        <!-- <el-submenu index="0">
          <template slot="title">
            <router-link to="/home" tag="li">
              <i class="iconfont icon-yemian"></i>
              首页
            </router-link>
          </template>
        </el-submenu> -->


        <el-menu-item index="0">
          <div class="menu-wra" @click="$router.push('/home')">
            <i class="iconfont icon-yemian"></i>
            <span slot="title">首页</span>
          </div>
        </el-menu-item>

        <el-submenu index="2">
          <template slot="title">
            <i class="iconfont icon-yonghuguanli"></i>
            <span slot="title">用户管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/userMan/list?type=0">
              <el-menu-item index="2-1">用户列表</el-menu-item>
            </router-link>
            <router-link to="/userMan/list?type=1">
              <el-menu-item index="2-2">模拟用户</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>


        
        <el-submenu index="3">
          <template slot="title">
            <i class="iconfont icon-dailiguanli"></i>
            <span slot="title">代理管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/agentMan/list">
              <el-menu-item index="3-1">代理列表</el-menu-item>
            </router-link>
          </el-menu-item-group>
          
          <!-- <router-link to="/agentStatistics" tag="li">
              <el-menu-item index="3-2">股票持仓统计</el-menu-item>
          </router-link>
          <router-link to="/inedxStatistics" tag="li">
              <el-menu-item index="3-3">指数持仓统计</el-menu-item>
          </router-link>
          <router-link to="/futuresStatistics" tag="li">
              <el-menu-item index="3-4">期货持仓统计</el-menu-item>
          </router-link> -->
        </el-submenu>

        <el-submenu index="12">
          <template slot="title">
            <i class="iconfont icon-dailiguanli"></i>
            <span slot="title">分仓配资</span>
          </template>
          <el-menu-item-group>
            <router-link to="/funds/setting" tag="li">
              <el-menu-item index="12-1">配资设置</el-menu-item>
            </router-link>
            <router-link to="/funds/lever" tag="li">
              <el-menu-item index="12-2">配置杠杆</el-menu-item>
            </router-link>
            <router-link to="/funds/dealer" tag="li">
              <el-menu-item index="12-3">券商机构管理</el-menu-item>
            </router-link>
            <router-link to="/funds/securities" tag="li">
              <el-menu-item index="12-4">证券信息管理</el-menu-item>
            </router-link>
            <router-link to="/funds/account" tag="li">
              <el-menu-item index="12-5">交易账户管理</el-menu-item>
            </router-link> 
            <!-- <router-link to="/funds/apply?type=0" tag="li">
              <el-menu-item index="12-6">待审核配资</el-menu-item>
            </router-link> 
            <router-link to="/funds/apply?type=1" tag="li">
              <el-menu-item index="12-7">操盘中配资</el-menu-item>
            </router-link> 
            <router-link to="/funds/apply?type=2" tag="li">
              <el-menu-item index="12-8">未通过配资</el-menu-item>
            </router-link> 
            <router-link to="/funds/apply?type=3" tag="li">
              <el-menu-item index="12-8">到期配资</el-menu-item>
            </router-link> 
            <router-link to="/funds/apply?type=4" tag="li">
              <el-menu-item index="12-9">已结束配资</el-menu-item>
            </router-link> 
            <router-link to="/funds/delay?type=0" tag="li">
              <el-menu-item index="12-10">续期申请</el-menu-item>
            </router-link> 
            <router-link to="/funds/delay?type=1" tag="li">
              <el-menu-item index="12-11">续期记录</el-menu-item>
            </router-link> 
            <router-link to="/funds/expand?type=0" tag="li">
              <el-menu-item index="12-12">扩大配资申请</el-menu-item>
            </router-link>
            <router-link to="/funds/expand?type=1" tag="li">
              <el-menu-item index="12-13">扩大配资记录</el-menu-item>
            </router-link> 
            <router-link to="/funds/margin?type=0" tag="li">
              <el-menu-item index="12-14">追加保证金申请</el-menu-item>
            </router-link> 
            <router-link to="/funds/margin?type=1" tag="li">
              <el-menu-item index="12-15">追加保证金记录</el-menu-item>
            </router-link> 
            <router-link to="/funds/end?type=0" tag="li">
              <el-menu-item index="12-16">终止操盘申请</el-menu-item>
            </router-link> 
            <router-link to="/funds/end?type=1" tag="li">
              <el-menu-item index="12-17">终止操盘记录</el-menu-item>
            </router-link> -->
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="iconfont icon-gupiao"></i>
            <span slot="title">产品管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/product/twstock" tag="li">
              <el-menu-item index="4-1">台湾股票</el-menu-item>
            </router-link>
            <router-link to="/product/stock" tag="li">
              <el-menu-item index="4-2">美国股票</el-menu-item>
            </router-link>
            <!-- <router-link to="/product/index" tag="li">
              <el-menu-item index="4-3">指数产品</el-menu-item>
            </router-link> -->
            <router-link to="/product/coin" tag="li">
              <el-menu-item index="4-5">汇率管理</el-menu-item>
            </router-link>
          </el-menu-item-group>
          <el-submenu index="4-4">
            <span slot="title">外盘期货</span>
           
            <router-link to="/product/futures" tag="li">
              <el-menu-item index="4-6">期货产品</el-menu-item>
            </router-link>
          </el-submenu>
        </el-submenu>

        <el-submenu index="5">
          <template slot="title">
            <i class="iconfont icon-zhaijichicang"></i>
            <span slot="title">持仓管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/hold/holdPositions" tag="li">
              <el-menu-item index="5-1">融资持仓</el-menu-item>
            </router-link>
            <router-link to="/hold/indexHoldPositions" tag="li">
              <el-menu-item index="5-2">指数持仓</el-menu-item>
            </router-link>
            <router-link to="/hold/futuresHoldPositions" tag="li">
              <el-menu-item index="5-3">期货持仓</el-menu-item>
            </router-link>
            <router-link to="/hold/addHoldPositions" tag="li">
              <el-menu-item index="5-4">创建融资持仓</el-menu-item>
            </router-link>
            <router-link to="/hold/subscribe" tag="li">
              <el-menu-item index="5-5">新股申購</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu index="5-4">
          <template slot="title">
            <i class="iconfont icon-qudaoguanli1"></i>
            <span slot="title">持仓统计</span>
          </template>
          <el-menu-item-group>
            <router-link to="/statistics/agentStatistics" tag="li">
              <el-menu-item index="agentStatistics">股票持仓统计</el-menu-item>
            </router-link>
            <router-link to="/statistics/inedxStatistics" tag="li">
              <el-menu-item index="inedxStatistics">指数持仓统计</el-menu-item>
            </router-link>
            <router-link to="/statistics/futuresStatistics" tag="li">
              <el-menu-item index="futuresStatistics">期货持仓统计</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu index="1">
          <template slot="title">
            <i class="iconfont icon-zijinguanli"></i>
            <span slot="title">资金管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/cap/capitalDetail" tag="li">
              <el-menu-item index="1-1">充值列表</el-menu-item>
            </router-link>
            <router-link to="/cap/withdraw" tag="li">
              <el-menu-item index="1-2">
                <el-badge :value="outMoneyOrder" class="item-mark">
                  提现列表
                </el-badge>
              </el-menu-item>
            </router-link>
            <router-link to="/cap/cash" tag="li">
              <el-menu-item index="1-3">资金记录</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="6">
          <template slot="title">
            <i class="iconfont icon-rizhiguanli1"></i>
            <span slot="title">日志管理</span>
          </template>
          <el-menu-item-group>
            <router-link to="/log/loginlist" tag="li">
              <el-menu-item index="6-1">登录日志</el-menu-item>
            </router-link>
            <router-link to="/log/msglist" tag="li">
              <el-menu-item index="6-2">短信日志</el-menu-item>
            </router-link>
            <router-link to="/log/task" tag="li">
              <el-menu-item index="6-3">定时任务</el-menu-item>
            </router-link>
            <router-link to="/log/exchange" tag="li">
              <el-menu-item index="6-4">资金互转记录</el-menu-item>
            </router-link>
            <router-link to="/log/message" tag="li">
              <el-menu-item index="6-5">站内消息</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="7">
          <template slot="title">
            <i class="iconfont icon-sysset"></i>
            <span slot="title">管理设置</span>
          </template>
          <el-menu-item-group>
            <router-link to="/manSet/manSetting" tag="li">
              <el-menu-item index="7-1">管理列表</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>
        <!-- <el-submenu index="8">
          <template slot="title"><i class="iconfont icon-gonggaoguanli"></i>公告管理</template>
          <router-link to="/art" tag="li">
              <el-menu-item index="8-1">公告列表</el-menu-item>
          </router-link>
        </el-submenu> -->
        <el-submenu index="9">
          <template slot="title">
            <i class="iconfont icon-fengkongguanli"></i>
            <span slot="title">风控设置</span>
          </template>
          <el-menu-item-group>
            <router-link to="/risk/product" tag="li">
              <el-menu-item index="9-0">产品配置</el-menu-item>
            </router-link>
            <router-link to="/risk/riskSetting" tag="li">
              <el-menu-item index="9-1">股票风控</el-menu-item>
            </router-link>
            <router-link to="/risk/indexRiskSetting" tag="li">
              <el-menu-item index="9-2">指数风控</el-menu-item>
            </router-link>
            <router-link to="/risk/futuresRiskSetting" tag="li">
              <el-menu-item index="9-3">期货风控</el-menu-item>
            </router-link>
            <router-link to="/risk/spreadSetting" tag="li">
              <el-menu-item index="9-4">点差设置</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>
        <!-- <el-submenu index="10">
        <template slot="title"><i class="iconfont icon-qudaoguanli"></i>支付渠道管理</template>
        <router-link to="/paychannel" tag="li">
            <el-menu-item index="10-1">支付渠道列表</el-menu-item>
        </router-link>
      </el-submenu> -->
        <el-submenu index="11">
          <template slot="title">
            <i class="iconfont icon-daohang-guanlishezhi"></i>
            <span slot="title">系统基本设置</span>
          </template>
          <el-menu-item-group>
            <router-link to="/set/baseSetting" tag="li">
              <el-menu-item index="11-1">系统基本设置</el-menu-item>
            </router-link>
            <router-link to="/set/art" tag="li">
              <el-menu-item index="11-2">公告设置</el-menu-item>
            </router-link>
            <!-- <router-link to="/set/banner" tag="li">
              <el-menu-item index="11-3">banner设置</el-menu-item>
            </router-link> -->
            <router-link to="/set/paychannel" tag="li">
              <el-menu-item index="11-4">支付渠道设置</el-menu-item>
            </router-link>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="14">
          <template slot="title">
              <span slot="title">
                <a title="融资融券 2.0 系统说明书" download="introduction.pdf" :href="adminUrl+'/admins/introduction.pdf'">系统说明</a>
              </span>
          </template>
          <!-- <template slot="title"> -->
            <!-- 融资融券 2.0 系统说明书 -->
            <!-- <a title="融资融券 2.0 系统说明书" download="introduction.pdf" :href="adminUrl+'/admins/introduction.pdf'">系统说明</a> -->
            <!-- <a title="系统说明" download="1.pdf" href="../assets/1.pdf">系统说明</a> -->
          <!-- </template> -->
        </el-submenu>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  name: 'MenuNav',
  components: {},
  props: {
    index: {
      type: Array
    },
    itemMenu: {
      type: String
    },
    isCollapse: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      contentStyleObj: {
        height: ''
      },
      siteInfo: '',
      adminUrl: '',
      outMoneyOrder: 0
    }
  },
  watch: {},
  computed: {},
  created () {
    window.addEventListener('resize', this.getHeight)
    this.getHeight()
    if (!this.$store.state.userInfo.adminName) {
      this.$store.state.userInfo.adminName = window.localStorage.getItem('adminName')
    }
    this.timer = setInterval(this.refreshOutMoneyOrderNum, 1000 * 60)
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
    clearInterval(this.timer)
  },
  mounted () {
    this.getSiteInfo()

    this.adminUrl = process.env.API_HOST
    if (this.adminUrl === undefined) {
      this.adminUrl = ''
    }
  },
  methods: {
    getHeight () {
      this.contentStyleObj.height = window.innerHeight - 62 + 'px'
    },
    async getSiteInfo () {
      // 获取站点信息
      let data = await api.getInfo()
      if (data.status === 0) {
        this.siteInfo = data.data
        this.$store.state.siteInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async refreshOutMoneyOrderNum () {
      // 刷新出金订单
      let opts = {
        state: 0,
        pageNum: 1,
        pageSize: 1
      }
      let data = await api.withdrawList(opts)
      if (data.status === 0) {
        // 出金待审核金额
        if (data.data.total !== 0 && data.data.total > this.outMoneyOrder) {
          // 有了新的记录
          this.$message({
            showClose: true,
            message: '您有新的订单记录待处理!',
            type: 'warning'
          })
        }
        this.outMoneyOrder = data.data.total
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .logo-img {
    padding: 10px 0;

    img {
      max-width: 80%;
      max-height: 50px;
    }
  }

  .nav-left {
    overflow: hidden;
    min-height: 500px;
    background-color: #1f2636;
    height: calc(100% - 60px);
    .iconfont {
      vertical-align: middle;
      margin-right: 8px;
      color: #fff;
    }
    /deep/.el-submenu__title{
      padding: 0 10px;
    }
  }

  .item-mark {
    /deep/ .el-badge__content.is-fixed {
      top: 19px;
      left: 35px;
      right: auto;
    }
  }
</style>
