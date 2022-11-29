<template>
  <div class="wrapper">
    <el-container style="border: 1px solid #eee">
      
      <el-container>
        <!-- 头部 -->
        <el-aside :width="isCollapse ? '60px' : '200px'">
          <div class="logo-wrapper" :style="{borderRight: isCollapse ? '0' : ''}">
            <img :style="{marginLeft: isCollapse ? '16px' : '6px'}" width="50px" :src="$store.state.siteInfo.siteLogo" alt="logo">
            <span v-if="!isCollapse">{{$store.state.siteInfo.siteName}}</span>
          </div>
          <!-- 菜单 -->
          <MenuNav :navselected='1' :isCollapse="isCollapse"/>
        </el-aside>
        <el-main class="wrapper-content" :style="contentStyleObj">
          <el-header class="header-home">
            <div class="switch-wrapper">
              <img @click="handleChangeCollapse(false)" v-if="isCollapse" :src="expandIcon" alt="">
              <img @click="handleChangeCollapse(true)" v-if="!isCollapse" :src="unExpandIcon" alt="">
            </div>
            <el-dropdown class="pull-right">
              <i class="el-icon-setting" style="margin-right: 15px"></i>
              <span>{{$store.state.userInfo.adminName}}</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="changePwd">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-header>
          <div class="main-content">
            <!-- 主页面 -->
            <countChart/>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import countChart from '@/components/countChart'
import * as api from '@/axios/api'
import expandIcon from '@/assets/expand-icon.png'
import unExpandIcon from '@/assets/un-expand-icon.png'

export default {
  components: {
    MenuNav,
    countChart
  },
  props: {},
  data () {
    return {
      expandIcon,
      unExpandIcon,
      activeCategory: 'home',
      contentStyleObj: {
        height: ''
      },
      isCollapse: false
    }
  },
  activated () {
    this.handleNav()
    this.$router.push('/capitalDetail')
  },
  created () {
    // 获取isCollapse
    let isCollapse = localStorage.getItem('el-menu-isCollapse')
    if (isCollapse === '1') {
      this.isCollapse = true
    }


    window.addEventListener('resize', this.getHeight)
    this.getHeight()
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  },
  methods: {
    handleChangeCollapse(status) {
      this.isCollapse = status
      localStorage.setItem('el-menu-isCollapse', status ? 1 : 0)
    },
    handleClick (vo) {
      this.$router.push(`/home`)
    },
    // 处理加载后nav选中
    handleNav () {
      let arr = this.$route.path.split('/')
      let path = arr[arr.length - 1]
      this.activeCategory = path
    },
    async changePwd () {
      this.$store.state.loginIsShow = true
    },
    async logout () {
      // 退出登录
      let data = await api.logout()
      if (data.status === 0) {
        this.$router.push('/login')
      } else {
        this.$message.error(data.msg)
      }
    },
    getHeight () {
      this.contentStyleObj.height = window.innerHeight - 64 + 'px'
    }
  }
}
</script>
<style lang="less" scoped>
  .el-header {
    // background-color: #B3C0D1;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
    overflow: hidden;
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
    img{
      width: 30px;
      margin: 0 6px;
    }
  }

  .header-home {
    padding: 0 30px 0 20px;
    position: relative;
    .switch-wrapper{
      position: absolute;
      width: 60px;
      height: 60px;
      left: 20px;
      top: 0;
      img{
        width: 30px;
        cursor: pointer;
      }
    }
  }

  .el-card {
    overflow: visible;
  }

  .wrapper-content{
    padding: 0;
    .main-content{
      padding: 20px;
    }
  }
</style>
