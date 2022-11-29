<template>
  <div class="wrapper">
    <!-- 主页面 -->
    <el-tabs v-model="activetype" type="border-card">
      <el-tab-pane label="指数持仓单" name="first">
        <ConTable :type="1"/>
      </el-tab-pane>
      <el-tab-pane label="指数平仓单" name="second">
        <ConTable2 :type="0"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import ConTable from './components/indextable-hold'
import ConTable2 from './components/indextable-sell'
import * as api from '@/axios/api'

export default {
  components: {
    MenuNav,
    ConTable,
    ConTable2
  },
  props: {},
  data () {
    return {
      activetype: 'first',
      contentStyleObj: {
        height: ''
      }
    }
  },
  activated () {

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
  }

  .el-aside {
    color: #333;
  }

  .header-home {
    padding: 0 20px
  }
</style>
