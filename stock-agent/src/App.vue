<template>
  <div id="app" class="app-wrapper">
    <el-container>
      <el-aside :width="isCollapse ? '60px' : '200px'" v-show="!isLogin">
        <div class="logo-wrapper" :style="{borderRight: isCollapse ? '0' : ''}">
          <img :style="{marginLeft: isCollapse ? '16px' : '6px'}" width="50px" :src="$store.state.siteInfo.siteLogo" alt="logo">
          <span v-if="!isCollapse">{{$store.state.siteInfo.siteName}}</span>
        </div>
        <NavMenu :navselected='1' :isCollapse="isCollapse" />
      </el-aside>
      <el-main>
        <el-header class="header-home" v-show="!isLogin">
          <div class="switch-wrapper">
            <img @click="handleChangeCollapse(false)" v-if="isCollapse" :src="expandIcon" alt="">
            <img @click="handleChangeCollapse(true)" v-if="!isCollapse" :src="unExpandIcon" alt="">
          </div>
          <div class="user fr main-col">
            <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link" style="cursor: pointer;">
              <i class="iconfont icon-user-5" style="margin-right: 5px;font-size: 18px"></i>{{$store.state.userInfo.agentName}}<i
              class="el-icon-arrow-down el-icon--setting"></i>
            </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="a">重置密码</el-dropdown-item>
                <el-dropdown-item command="b">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <router-view/>
        <login></login>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'
import Login from './components/Login'
import NavMenu from '@/components/NavMenu'
import expandIcon from '@/assets/expand-icon.png'
import unExpandIcon from '@/assets/un-expand-icon.png'
export default {
  name: 'App',
  data () {
    return {
      expandIcon,
      unExpandIcon,
      myTitle: '标题',
      keyWords: 'keyWords',
      description: 'description',
      isCollapse: false,
      activeIndex: this.$store.state.activeIndex, // 当前选中
      navShow: {
        order: false,
        artical: false
      },
      outMoneyOrder: 0, // 出金待审核金额
      timer: null,
      isLogin: false
    }
  },
  components: {
    Login,
    NavMenu
  },

  // metaInfo () {
  //   return {
  //     title: this.myTitle,
  //     meta: [
  //       { vmid: 'keywords', name: 'keywords', content: this.keyWords },
  //       { vmid: 'description', name: 'description', content: this.description }
  //     ]
  //   }
  // },
  created () {
    this.timer = setInterval(this.refreshOutMoneyOrderNum, 1000 * 60)
    console.log()
    if (this.$route.fullPath === '/login') {
      this.isLogin = true
    } else {
      this.isLogin = false
    }
  },
  watch: {
    $route(val) {
      if (val.fullPath === '/login') {
        this.isLogin = true
      } else {
        this.isLogin = false
      }
    }
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {
    if (!this.$store.state.userInfo.agentName) {
      this.getAgentInfo()
    }
    if (!this.$store.state.siteInfo.siteName) {
      this.getSiteInfo()
    }
    this.getOutMoneyOrderNum()
  },
  methods: {
    handleChangeCollapse(status) {
      this.isCollapse = status
    },
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
  }
}
</script>

<style scoped>
  .app-wrapper{
    font-family: "Microsoft Yahei", 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #000;
    min-width: 900px;
  }
  
  .el-main{
    padding: 0;
    min-height: 500px;
  }
        
  .header-home{
    box-sizing: border-box;
    padding-right: 40px;
    position: relative;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
  }

  
  .logo-wrapper{
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid rgba(0,0,0,0.1);
    box-sizing: border-box;
    background-color: #1f2636;
    color: rgb(191, 203, 217);

  }

  .logo-wrapper img{
    width: 30px;
    margin: 0 6px;
  }

  .switch-wrapper{
    position: absolute;
    width: 60px;
    height: 60px;
    left: 20px;
    top: 0;
    display: flex;
    align-items: center;
  }
  .switch-wrapper img{
    width: 30px;
    cursor: pointer;
  }

  .main-col{
    height: 100%;
    display: flex;
    align-items: center;
  }
          
      

</style>
