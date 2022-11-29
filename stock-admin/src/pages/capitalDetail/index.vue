<template>
  <div class="wrapper">
    <el-container style=" border: 1px solid #eee">
      <el-aside width="200px">
        <!-- 菜单 -->
        <MenuNav :index="['1']"/>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changePwd">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>{{$store.state.userInfo.adminName}}</span>
        </el-header>
        <el-main class="wrapper-content" :style="contentStyleObj">
          <!-- 主页面 -->
          <rechangeTable/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import rechangeTable from './components/rechage-table'
import * as api from '@/axios/api'

export default {
  components: {
    MenuNav,
    rechangeTable
  },
  props: {},
  // data(){
  //   const item = {
  //       date: '2016-05-02',
  //       name: '王小虎',
  //       address: '上海市普陀区金沙江路 1518 弄'
  //     };
  //     return {
  //       tableData: Array(10).fill(item)
  //     }
  // },
  data () {
    return {
      activeCategory: 'home',
      contentStyleObj: {
        height: ''
      }
    }
  },
  activated () {
    this.handleNav()
    this.$router.push('/capitalDetail')
  },
  created () {
    window.addEventListener('resize', this.getHeight)
    this.getHeight()
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  },
  methods: {
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
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }

  .header-home {
    padding: 0 20px
  }
</style>
