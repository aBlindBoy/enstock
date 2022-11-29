<template>
  <div>
    <div class="header-top">
      <div class="header-top-wrap clearfix">
        <div class="header-top-left">{{$store.state.siteInfo.siteName}}</div>
        <!-- <div class="header-top-right">热线电话：400-1758988<span class="circle"></span></div> -->
      </div>
    </div>

    <div class="header-nav">
      <div class="header-nav-wrap">
        <a href="/agents" class="nav-left">
          <!-- <img src="../assets/image/linghanglogo.jpg" alt=""> -->
          <img :src="$store.state.siteInfo.siteLogo" alt="">
        </a>
        <div class="user fr main-col">
          <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <i class="iconfont icon-user-5"></i>{{$store.state.userInfo.agentName}}<i
            class="el-icon-arrow-down el-icon--setting"></i>
          </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="a">重置密码</el-dropdown-item>
              <el-dropdown-item command="b">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

          <!-- <span><img src="../assets/imgs/order/login.png" alt=""></span>
          {{$store.state.mobile?$store.state.mobile:'用户名称'}} -->
        </div>
        <div class="nav-right">
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="home">代理中心</el-menu-item>
            <el-menu-item index="statistics">数据统计</el-menu-item>
            <el-menu-item index="userMan">用户管理</el-menu-item>
            <el-menu-item index="agent">代理管理</el-menu-item>
            <el-menu-item index="agentcyFee">利润明细</el-menu-item>
            <el-menu-item index="holdPositions">持仓管理</el-menu-item>
            <el-menu-item index="capitalDetail">资金明细</el-menu-item>
            <el-menu-item index="entry">入金记录</el-menu-item>
            <el-menu-item index="exit">
              <el-badge :value="outMoneyOrder" class="item-mark">
                出金记录
              </el-badge>
            </el-menu-item>
          </el-menu>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

export default {
  components: {},
  props: {},
  data () {
    return {
      activeIndex: this.$store.state.activeIndex, // 当前选中
      navShow: {
        order: false,
        artical: false
      },
      outMoneyOrder: 0, // 出金待审核金额
      timer: null
    }
  },
  created () {
    this.timer = setInterval(this.refreshOutMoneyOrderNum, 1000 * 60)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  methods: {
    async getSiteInfo () {
      // 获取站点信息
      let data = await api.getSiteInfo()
      if (data.status === 0) {
        this.$store.state.siteInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    getNav () {
      this.navShow.artical = !this.navShow.artical
    },
    getOrderType () {
      this.navShow.order = !this.navShow.order
    },
    handleSelect (key, keyPath) {
      if (key === 'order' && this.$store.state.activeIndex !== 'order') {
        this.$store.state.orderInfo = {} // 清空订单数据
        return
      }
      if (key === 'newsUpdate' && this.$store.state.activeIndex !== 'newsUpdate') { return }
      this.$router.push(key)
    },
    menuSelectOrderType (type) {
      this.$store.state.orderType = type
      this.$router.push('order')
    },
    dropMenuSelect (type) {
      this.$store.state.selectedType = type
      this.$router.push('newsUpdate')
    },
    async handleCommand (command) {
      // 修改密码
      console.log(command)
      if (command === 'a') {
        this.$store.state.loginIsShow = true
      } else {
        let data = await api.logout()
        if (data.status === 0) {
          // Toast(data.msg)
          this.$router.push('/login')
        } else {
          Toast(data.msg)
        }
      }
    },
    async getAgentInfo () {
      let data = await api.getAgentInfo()
      if (data.status === 0) {
        this.detail = data.data
        this.$store.state.userInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async getOutMoneyOrderNum () {
      // 获取出金订单未处理的条数
      let opts = {
        state: 0,
        pageNum: 1,
        pageSize: 15
      }
      let data = await api.getUserwithdrawList(opts)
      if (data.status === 0) {
        // 出金待审核金额
        this.outMoneyOrder = data.data.total
      } else {
        this.$message.error(data.msg)
      }
    },
    async refreshOutMoneyOrderNum () {
      // 刷新出金订单
      let opts = {
        state: 0,
        pageNum: 1,
        pageSize: 15
      }
      let data = await api.getUserwithdrawList(opts)
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
  },
  mounted () {
    if (!this.$store.state.userInfo.agentName) {
      this.getAgentInfo()
    }
    if (!this.$store.state.siteInfo.siteName) {
      this.getSiteInfo()
    }
    this.getOutMoneyOrderNum()
  }
}
</script>
<style lang="stylus" scoped>
  .icon-user-5
    margin-right: 6px
    font-size: 18px
    vertical-align: middle

  .header-top
    height: 34px
    line-height: 34px
    font-size: 12px

    .header-top-wrap
      padding 0 4%
      color: #a5a5a5
      margin: 0 auto

      .header-top-left
        float: left

      .header-top-right
        float: right

        .circle
          display: inline-block
          width: 6px
          height: 6px
          background: #ff8b2a
          border-radius: 50%
          vertical-align: text-top;
          margin-left: 6px;

  .header-nav
    // border-bottom 1px solid #ddd
    box-shadow: 0 2px 5px rgba(0, 0, 0, .05);

    .header-nav-wrap
      padding 0 4%
      margin 0 auto

      .nav-left
        float left

        img
          height 46px

      .user
        font-size 14px
        cursor pointer

        span
          margin-right 15px

      .nav-right
        float left
        line-height 80px
        width 76%

        /deep/ .el-badge__content.is-fixed
          top: 15px;
          right: 5px;

        .el-menu
          border-bottom none

          .el-menu-item
            font-size 16px
            margin-left 4%
            border-bottom-width 3px
            padding 3px

            .el-icon-caret-bottom
              color #000

          li:hover
            color $mainColor
            border-bottom-color $mainColor

          .is-active
            color $mainColor
            border-bottom-color $mainColor

          .last-list
            position relative
            padding-right 0px
            border-bottom-color #fff

            &:hover
              border-bottom-color transparent

              .el-icon-caret-bottom
                color $mainColor

            ul
              position absolute
              top 74px
              left 0px
              z-index 2
              color #000
              background #f7f7f7

              li
                height 44px
                line-height 44px
                padding 2px 8px

                &:nth-child(1)
                  border-bottom 1px solid #ff7400

                a
                  padding 9px 7px

                  &:hover
                    color $mainColor

</style>
