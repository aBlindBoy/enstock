<template>
  <div class="wrapper">
    <el-container style="border: 1px solid #eee">
      <!-- 头部 -->
      <el-aside width="200px">
        <!-- 菜单 -->
        <MenuNav :index="['11']"/>
      </el-aside>
      <el-container>
        <el-header class="header-home">
          <el-dropdown class="pull-right">
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <span>{{$store.state.userInfo.adminName}}</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changePwd">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>
        <el-main>
          <!-- 主页面 -->
          <ConTable/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import ConTable from './components/table-banner'
import * as api from '@/axios/api'

export default {
  components: {
    MenuNav,
    ConTable
  },
  props: {},
  data () {
    return {
      activeCategory: 'home'
    }
  },
  activated () {

  },
  methods: {}
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
