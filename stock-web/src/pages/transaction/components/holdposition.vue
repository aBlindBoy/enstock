<template>
  <div>
    <el-alert
      v-if="list.list.length>0 && list.list[0].now_price === 0"
      style="margin-bottom:10px"
      title="In call auction, 'Current price', 'Floating profit and loss', and 'Total profit and loss' cannot be viewed temporarily"
      type="warning"
      close-text="understood">
    </el-alert>
    <div class="table transaction-table">
      <el-table
        stripe
        v-loading="loading"
        :data="list.list"
        height="250"
        style="width: 100%">
        <el-table-column
          :label="$t('common.stockName')"
          width="180px">
          <template slot-scope="scope">
            <p class="name">
              {{scope.row.stockName}}
              <span>
              ({{scope.row.stockCode}})
            </span>
              <i v-if="scope.row.orderDirection === 'bullish'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  === 'bearish'" class="green iconfont icon-down"></i>
            </p>
          </template>
        </el-table-column>
     
        <el-table-column
          prop="now_price"
          :label="$t('common.lastPrice')">
          <template slot-scope="scope">
            <div v-if="scope.row.now_price"
                 :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
              <p
                :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                {{scope.row.now_price === 0?'-':scope.row.now_price}}
              </p>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          :label="$t('common.purchasePrice')">
        </el-table-column>
        <el-table-column
          prop="orderNum"
          :label="$t('common.quantity')">
          <template slot-scope="scope">
            <div>
              {{scope.row.orderNum}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="profitAndLose"
          :label="$t('common.ploatingProfitAndLoss')">
          <template slot-scope="scope">
            <div class="bounceIn tab-number">
              <p :class="changeTextClass[scope.$index] === true?'heartBeat':''">
                <span v-if="scope.row.now_price === 0">-</span>
                <span v-else :class="scope.row.profitAndLose<0?'green bounceIn':'red bounceIn'">{{scope.row.profitAndLose}}</span>
              </p>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="allProfitAndLose"
          :label="$t('common.totalProfitAndLoss')">
          <template slot-scope="scope">
            <div class="bounceIn tab-number">
              <p :class="changeTextClass[scope.$index] === true?'heartBeat':''">
                <span v-if="scope.row.now_price === 0">-</span>
                <span v-else :class="scope.row.allProfitAndLose<0?'green bounceIn':'red bounceIn'">{{scope.row.allProfitAndLose}}</span>
              </p>
            </div>
            <!-- <el-tag
               :type="scope.row.allProfitAndLose < 0?'success':scope.row.allProfitAndLose === 0?'info':'danger'">

             </el-tag> -->
          </template>
        </el-table-column>
        <!-- <el-table-column
         prop="orderTotalPrice"
         label="The total market capitalization">
       </el-table-column> -->
        <el-table-column
          :label="$t('common.sell')">
          <template slot-scope="scope">
            <el-button class="btn-sell" plain title="I want to close my position" size="mini" @click="toSell(scope.row)"><i
              class="iconfont icon-chakan"></i>{{$t('common.sell')}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <div class="page-box">
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
      </div> -->

    </div>
  </div>

</template>

<script>
  import * as api from '../../../axios/api'

  export default {
    components: {},
    props: {
      handleOptions: {
        type: Function,
        default: function () {}
      },
      hasGetNewOrder: {
        type: Number,
        default: function () {}
      },
      marketType:{
        type: String,
      }
    },
    data () {
      return {
        loading: false,
        pageNum: 1,
        pageSize: 10,
        currentNum: 10,
        list: {
          list: []
        },
        timer: null,
        refresh: false, // ?????????
        changeTextClass: {}, // ????????????????????????
        hasChangeSell: 0 // ??????????????????
      }
    },
    watch: {
      haslogin (newval, oldVal) {
        if (newval !== oldVal) {
          this.getList()
          clearInterval(this.timer)
          this.timer = setInterval(this.refreshList, 5000)
        }
        if (!newval) {
          clearInterval(this.timer)
        }
      },
      hasGetNewOrder (newval, oldVal) {
        if (newval) {
          this.getList()
        }
      }
    },
    computed: {
      haslogin () {
        return this.$store.state.haslogin
      }
    },
    created () {
      if (this.$store.state.haslogin) {
        this.timer = setInterval(this.refreshList, 5000)
      }
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      if (this.$store.state.haslogin) {
        this.getList()
      }
    },
    methods: {
      handleSizeChange (val) {
        this.form.pageSize = val
        this.getList()
      },
      handleCurrentChange (val) {
        this.form.pageNum = val
        this.getList()
      },
      async refreshList () {
        if (this.refresh || this.loading) {
          return
        }
        this.refresh = true
        this.changeTextClass = {}
        // ??????????????????
        let opts = {
          state: 0,
          stockCode: '', // ??????
          stockSpell: this.marketType, 
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getOrderList(opts)
        this.refresh = false
        if (data.status === 0) {
          if (data.data.list.length === this.list.list.length) {
            data.data.list.forEach((element, i) => {
              this.changeTextClass[i] = ''
              if (data.data.list[i].now_price !== this.list.list[i].now_price) {
                this.changeTextClass[i] = true // ???????????????????????????
                this.list.list[i].now_price = data.data.list[i].now_price
                this.list.list[i].profitAndLose = data.data.list[i].profitAndLose
                this.list.list[i].allProfitAndLose = data.data.list[i].allProfitAndLose
              }
            })
          } else {
            this.list = data.data
          }
        } else {
          if (data.success === false) {

          } else {
            this.$message.error(data.msg)
          }
        }
      },
      onSubmit () {
        // ????????????
        this.getList()
      },
      async getList () {
        // ??????????????????
        let opts = {
          state: 0,
          stockCode: '', // ??????
          stockSpell: '', // ??????
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true
        let data = await api.getOrderList(opts)
        this.loading = false
        if (data.status === 0) {
          this.list = data.data
        } else {
          if (data.success === false) {

          } else {
            this.$message.error(data.msg)
          }
        }
      },
      async getUserInfo () {
        // ??????????????????
        let data = await api.getUserInfo()
        if (data.status === 0) {
          this.$store.state.userInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      toSell (val) {
        console.log(val)
        this.$confirm('Are you sure you want to close the position?', 'Tips', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(async () => {
          let opt = {
            positionSn: val.positionSn
          }
          console.log(opt)
          let data = await api.sellUsStock(opt)
          if (data.status === 0) {
            this.hasChangeSell++
            this.handleOptions(this.hasChangeSell)
            this.getList()
            this.getUserInfo()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: 'Closed position has been cancelled'
          })
        })
      }
    }
  }
</script>
<style lang="less" scoped>
  .transaction-table {
    .name {
      font-size: 12px;
    }
    .btn-sell {
      background: #4ba07b;
      border: 1px solid #4ba07b;
      color: #fff;
    }
  }
</style>
