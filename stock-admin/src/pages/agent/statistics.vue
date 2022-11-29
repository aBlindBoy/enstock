<template>
  <div class="wrapper">
    <el-container style="border: 1px solid #eee">
      <!-- 头部 -->
      <el-aside width="200px">
        <!-- 菜单 -->
        <MenuNav :index="['5-4']" itemMenu="agentStatistics"/>
      </el-aside>
      <el-container>
        <el-header class="header-home">
          股票持仓统计
          <el-dropdown class="pull-right">
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <span>{{$store.state.userInfo.adminName}}</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changePwd">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>
        <el-main class="wrapper-content" :style="contentStyleObj">
          <!-- 主页面 -->
          <ConTable/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import ConTable from './components/statistics-table'
import * as api from '@/axios/api'

export default {
  components: {
    MenuNav,
    ConTable
  },
  props: {},
  data () {
    return {
      activeCategory: 'home',
      contentStyleObj: {
        height: ''
      }
    }
  },
  mounted () {
    // this.getIncome()
  },
  created () {
    window.addEventListener('resize', this.getHeight)
    this.getHeight()
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  },
  methods: {
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
    color: #333;
    line-height: 60px;
    border-bottom: 1px solid #ddd;
  }

  .el-aside {
    color: #333;
  }

  .header-home {
    padding: 0 20px
  }
</style>
