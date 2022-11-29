<template>
  <div class="con-tab">
    <el-tabs v-model="activeName">
      <el-tab-pane label="Funding Details" name="first">
        <detail-table></detail-table>
      </el-tab-pane>
      <el-tab-pane label="Recharge records" name="second">
        <recharge-table></recharge-table>
      </el-tab-pane>
      <el-tab-pane label="Withdrawal records" name="three">
        <cash-table></cash-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import DetailTable from './detail'
  import CashTable from './cash'
  import RechargeTable from './recharge'
  // import * as api from '../../../../axios/api'

  export default {
    components: {
      DetailTable,
      RechargeTable,
      CashTable
    },
    props: {},
    data () {
      return {
        activeName: 'first'
      }
    },
    watch: {},
    computed: {},
    created () {},
    mounted () {},
    methods: {}
  }
</script>
<style lang="less" scoped>
  .con-tab {
    padding-left: 20px;
  }
</style>
