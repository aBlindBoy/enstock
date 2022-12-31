<template>
  <el-container class="user-center userIndex">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot index">
      <el-container class="main-wrapper">
        <el-aside width="270px">
          <menu-box></menu-box>
        </el-aside>
        <el-main style="   min-height: calc(100vh - 150px );">
          <account-box></account-box>
          <router-view/>
        </el-main>
      </el-container>
    </div>
  </el-container>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  // import HomeFooter from '../../components/Footer'
  import AccountBox from './components/user'
  import MenuBox from './components/menu'
  import * as api from '../../axios/api'

  export default {
    components: {
      HomeHeader,
      // HomeFooter,
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
      this.timer = setInterval(this.getUserInfo, 10000)
      this.$store.state.activeIndex = 'user'
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getUserInfo()
      this.$store.state.userMenu = '2-1'
    },
    methods: {
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判断是否登录
          // debugger
          // if (this.$store.state.userInfo.userAmt !== data.data.userInfo) {
					// 	console.log(data.data)
					// 	this.$store.commit('setUserInfo',data.data)
          //   this.refresh = true
          // } else {
          //   this.refresh = false
          // }
          // this.$store.state.userInfo = data.data
          this.$store.commit('setUserInfo',data.data)
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
    background: #fff;

  }

  .con-box {
    padding-left: 20px;
  }
</style>
