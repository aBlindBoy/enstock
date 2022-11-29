<template>
  <div>
    <el-alert
      v-if="list.list.length>0 && list.list[0].nowPrice === 0"
      style="margin-bottom:10px"
      title="In the call auction, 'Latest Points', 'Floating Profit and Loss', and 'Total Profit and Loss' cannot be viewed temporarily"
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
          prop="futuresName"
          width="120px"
          label="futures">
          <template slot-scope="scope">
            <div class="name">
              {{scope.row.futuresName}}
              <i v-if="scope.row.orderDirection === 'Bullish'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  === 'Bearish'" class="green iconfont icon-down"></i>
              <p class="exchange-p">
                {{scope.row.futuresCode}}
              </p>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column
          prop="orderDirection"
          label="Direction">
          <template slot-scope="scope">
            <p>
              {{scope.row.orderDirection}}

            </p>
          </template>
        </el-table-column> -->
        <el-table-column
          prop="nowPrice"
          label="latest price">
          <template slot-scope="scope">
            <div v-if="scope.row.nowPrice"
                 :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
              <p
                :class="scope.row.nowPrice - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.nowPrice - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                {{scope.row.nowPrice === 0?'-':Number(scope.row.nowPrice).toFixed(2)}} <span class="jibi-code">{{scope.row.coinCode}}</span>
                <!-- <i v-if="scope.row.nowPrice - scope.row.buyOrderPrice < 0" class="iconfont icon-down"></i> -->
                <!-- <i v-if="scope.row.nowPrice - scope.row.buyOrderPrice > 0" class="iconfont icon-up"></i> -->
              </p>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          label="buy point">
          <template slot-scope="scope">
            {{scope.row.buyOrderPrice}} <span class="jibi-code">{{scope.row.coinCode}}</span>
          </template>
        </el-table-column>

        <el-table-column
          prop="profitAndLose"
          label="Floating profit and loss">
          <template slot-scope="scope">
            <div class="bounceIn tab-number">
              <p :class="changeTextClass[scope.$index] === true?'heartBeat':''">
                <span v-if="scope.row.nowPrice === 0">-</span>
                <span v-else :class="scope.row.profitAndLose<0?'green bounceIn':'red bounceIn'">{{scope.row.profitAndLose}} <i
                  class="jibi-code">{{scope.row.coinCode}}</i></span>
              </p>
              <p class="exchange-p">≈{{(scope.row.profitAndLose*scope.row.coinRate).toFixed(2)}} USD</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="allProfitAndLose"
          label="Total profit and loss">
          <template slot-scope="scope">
            <div class="bounceIn tab-number">
              <p :class="changeTextClass[scope.$index] === true?'heartBeat':''">
                <span v-if="scope.row.nowPrice === 0">-</span>
                <span v-else :class="scope.row.allProfitAndLose<0?'green bounceIn':'red bounceIn'">{{scope.row.allProfitAndLose}} <i
                  class="jibi-code">{{scope.row.coinCode}}</i></span>
              </p>
              <p class="exchange-p">≈{{(scope.row.allProfitAndLose*scope.row.coinRate).toFixed(2)}} USD</p>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column
         prop="orderTotalPrice"
         label="保证金">
       </el-table-column> -->
        <el-table-column
          width="70px"
          prop="orderNum"
          label="quantity">
        </el-table-column>
        <el-table-column
          width="80px"
          label="close the position">
          <template slot-scope="scope">
            <el-button class="btn-sell" plain title="I want to close my position" size="mini" @click="toSell(scope.row)"><i
              class="iconfont icon-chakan"></i>close the position
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
        refresh: false, // 刷新中
        changeTextClass: {}, // 表格中的数据变化
        hasChangeSell: 0 // 平仓状态改变
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
        // 获取表格数据
        let opts = {
          state: 0,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getFuturesOrderList(opts)
        this.refresh = false
        if (data.status === 0) {
          if (data.data.list.length === this.list.list.length) {
            data.data.list.forEach((element, i) => {
              this.changeTextClass[i] = ''
              if (data.data.list[i].nowPrice !== this.list.list[i].nowPrice) {
                this.changeTextClass[i] = true // 设置对应的动画过滤
                this.list.list[i].nowPrice = data.data.list[i].nowPrice
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
        // 查询表格
        this.getList()
      },
      async getList () {
        // 取表格数据
        let opts = {
          state: 0,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true
        let data = await api.getFuturesOrderList(opts)
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
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          this.$store.state.userInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      toSell (val) {
        this.$confirm('Are you sure you want to close the position?', 'Tips', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(async () => {
          let opt = {
            positionSn: val.positionSn
          }
          let data = await api.sellFutures(opt)
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
