<template>
  <div class="wrapper">
    <!-- 主页面 -->
    <el-tabs v-model="activetype" type="border-card">
      <el-tab-pane label="期货持仓单" name="first">
        <ConTable :type="1"/>
      </el-tab-pane>
      <el-tab-pane label="期货平仓单" name="second">
        <ConTable2 :type="0"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import ConTable from './components/futurestable-hold'
import ConTable2 from './components/futurestable-sell'

export default {
  components: {
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
