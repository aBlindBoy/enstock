<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main>
        <account-box></account-box>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import HomeHeader from '../../../components/HeaderOrder'
  import HomeFooter from '../../../components/Footer'
  import AccountBox from './table/list'
  import MenuBox from './menu'
  import * as api from '../../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter,
      AccountBox,
      MenuBox
    },
    props: {},
    data () {
      return {
        timer: null,
        form: {
          stock: '',
          pageNum: 1,
          pageSize: 15
        },
        list: {
          list: []
        },
        loading: false,
        refresh: false, // 刷新中
        changeTextClass: {} // 表格中的数据变化
      }
    },
    watch: {},
    computed: {},
    created () {
      this.timer = setInterval(this.refreshList, 60000)
      this.$store.state.activeIndex = 'user'
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getUserInfo()
    },
    methods: {
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判断是否登录
          this.$store.state.userInfo = data.data
        } else {
          this.haslogin = false
          this.$store.state.haslogin = false
        }
      },
      handleOptions (opts) {
        this.form = { ...this.form, ...opts }
      }
    }
  }
</script>
<style lang="less" scoped>
  .table-box {
    padding-top: 65px;
    padding-bottom: 100px;
    width: 1200px;
    margin: 0 auto;
  }

  .table-search {
    padding: 20px 0;

    /deep/ .el-input-group__append {
      background-color: #353747;
      border-color: #353747;
    }

    /deep/ .el-input__inner {
      color: #fff;
      border-color: #353747;
      background-color: #24252c;
    }
  }

  .user-center {
    background: #fff;

    .user-header {
      background: rgba(0, 0, 0, 0.8);
    }
  }

  .con-box {
    padding-left: 20px;
  }
</style>
