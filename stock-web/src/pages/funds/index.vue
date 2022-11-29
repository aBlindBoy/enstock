<template>
  <el-container class="user-center usercot">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper ">
      <el-aside width="178px">
        <menu-box></menu-box>
      </el-aside>
      <el-main>
        <account-box></account-box>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  import HomeFooter from '../../components/Footer'
  import AccountBox from './components/account'
  // import MenuBox from './components/menu'
  import MenuBox from '@/pages/user/components/menu'
  import * as api from '../../axios/api'

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
    watch: {
      change (newVal, oldVal) {
        if (!newVal) {

        }
      }
    },
    computed: {
      change () {
        return this.$store.state.haslogin
        // this.$router.push('/home')
      }
    },
    created () {
      this.timer = setInterval(this.getUserInfo, 5000)
      // this.$store.state.activeIndex = 'user'
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getUserInfo()
      this.$store.state.userMenu = '2-21'
      // this.$store.state.userMenu = '2-2'
    },
    methods: {
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判断是否登录
          if (this.$store.state.userInfo.userAmt !== data.data.userInfo) {
            this.refresh = true
          } else {
            this.refresh = false
          }
          this.$store.state.userInfo = data.data
          this.$store.state.haslogin = true
        } else {
          this.haslogin = false
          this.$store.state.haslogin = false
          this.$router.push('/home')
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
    // background: #fff;
  }

  .con-box {
    padding-left: 20px;
  }
</style>
