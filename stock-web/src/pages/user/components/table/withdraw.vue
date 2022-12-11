<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot withdraw">
      <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main style=" min-height: calc(100vh - 150px );">
        <div class="wrapper">
          <div class="user-center-title" style="text-align:left;">
            <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
           {{$t('withdrawalRecords.title')}}
          </div>
          <el-table
            :data="list.list"
            style="width: 100%">
            <el-table-column
              prop="withAmt"
              :label="$t('withdrawalRecords.withdrawalAmount')">
            </el-table-column>
            <el-table-column
              prop="withFee"
              :label="$t('withdrawalRecords.handlingFee')">
            </el-table-column>
            <el-table-column
              width="100"
              prop="withStatus"
              :label="$t('withdrawalRecords.state')">
              <template slot-scope="scope">
                        <span :class="scope.row.withStatus === 1?'green':scope.row.withStatus === 2?'red':'red'">
                            <i v-if="scope.row.withStatus === 1" class="iconfont icon-tongguo4 animated bounceIn"></i>
                            <i v-if="scope.row.withStatus==0" class="iconfont icon-dengdai animated bounceInDown"></i>
                            <i v-if="scope.row.withStatus === 2"
                               class="iconfont icon-failure animated bounceInDown"></i>
                            <i v-if="scope.row.withStatus === 3"
                               class="iconfont icon-iconfontweitongguo animated bounceInDown"></i>
                            {{withStatus[scope.row.withStatus]}}
                        </span>
              </template>
            </el-table-column>
            <el-table-column
              width="170px"
              prop="withMsg"
              :label="$t('withdrawalRecords.remark')">
            </el-table-column>
            <el-table-column
              prop="addTime"
              width="180px"
              :label="$t('withdrawalRecords.withdrawalFinancialAccount')">
              <template slot-scope="scope">
                {{scope.row.bankName}}-{{scope.row.bankAddress}}
              </template>
            </el-table-column>
            <el-table-column
              width="170px"
              prop="bankNo"
              :label="$t('withdrawalRecords.withdrawalCardNumber')">
            </el-table-column>
            <el-table-column
              prop="applyTime"
              width="170px"
              :label="$t('withdrawalRecords.time')">
              <template slot-scope="scope">
                <b v-if="scope.row.applyTime">{{scope.row.applyTime | timeFormat}}</b>
                <b v-else></b>
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              prop="isLock"
              width="100px"
              :label="$t('withdrawalRecords.cancel')">
              <template slot-scope="scope">
                <el-button v-if="scope.row.withStatus != 3" type="danger" plain size="small"
                           @click="toOption(scope.row)">
                           {{$t('withdrawalRecords.cancelWithdrawal')}}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40,50]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
    </div>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../../components/HeaderOrder'
  // import HomeFooter from '../../../../components/Footer'
  import MenuBox from '../menu'
  import * as api from '../../../../axios/api'

  export default {
    components: {
      HomeHeader,
      // HomeFooter,
      MenuBox
    },
    props: {},
    data () {
      return {
        pageNum: 1,
        pageSize: 15,
        list: {
          list: []
        },
        withStatus:[this.$t('withdrawalRecords.underReview'),this.$t('withdrawalRecords.withdrawalSuccess'),
        this.$t('withdrawalRecords.withdrawalFailed'),this.$t('withdrawalRecords.cancelWithdrawal')]
   
      }
    },
    watch: {},
    computed: {},
    created () {},
    mounted () {
      this.getlist()
      this.$store.state.userMenu = '2-6'
    },
    methods: {
      handleSizeChange (size) {
        this.pageSize = size
        this.getlist()
      },
      handleCurrentChange (page) {
        this.pageNum = page
        this.getlist()
      },
      async getlist () {
        // 获取 提现记录
        let opts = {
          pageNum: this.pageNum,
          pageSize: 10
        }
        let data = await api.withdrawList(opts)
        if (data.status === 0) {
          // data.data.list.forEach(element => {
          //     this.list.push(element)
          // });
          this.list = data.data
          // this.total = data.data.total
        } else {
          this.$message.error(data.msg)
        }
      },
      async toOption (val) {
        // 取消提现
        let opt = {
          withId: val.id
        }
        let data = await api.canceloutMoney(opt)
        if (data.status === 0) {
          // this.list = {}
          this.getlist()
          this.$message.success(data.msg)
        } else {
          this.$message.success(data.msg)
        }
      }
    }
  }
</script>
<style lang="less" scoped>
  .code {
    font-size: 12px;
    color: #999;
  }

  .main-wrapper {
    .wrapper {
      padding: 20px;

      .table-search {
        margin-bottom: 15px;
      }
    }
  }
</style>
