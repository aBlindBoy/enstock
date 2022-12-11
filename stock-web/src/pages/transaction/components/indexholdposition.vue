<template>
  <div>
    <el-alert
      v-if="list.list.length>0 && list.list[0].now_price === 0"
      style="margin-bottom:10px"
      title="In the call auction, 'Latest Points', 'Floating Profit and Loss', and 'Total Profit and Loss' cannot be viewed temporarily"
      type="warning"
      close-text="知道了">
    </el-alert>
    <div class="table transaction-table">
      <el-table
        stripe
        v-loading="loading"
        :data="list.list"
        height="250"
        style="width: 100%">
        <el-table-column
          prop="indexName"
          width="160px"
          label="index">
          <template slot-scope="scope">
            <p class="name">
              {{scope.row.indexName}}
              <span>
              ({{scope.row.indexCode}})
            </span>
              <i v-if="scope.row.orderDirection === 'bullish'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  === 'bearish'" class="green iconfont icon-down"></i>
            </p>
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
          prop="now_price"
          label="最新">
          <template slot-scope="scope">
            <div v-if="scope.row.now_price"
                 :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
              <p
                :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                {{scope.row.now_price === 0?'-':Number(scope.row.now_price).toFixed(2)}}
                <!-- <i v-if="scope.row.now_price - scope.row.buyOrderPrice < 0" class="iconfont icon-down"></i> -->
                <!-- <i v-if="scope.row.now_price - scope.row.buyOrderPrice > 0" class="iconfont icon-up"></i> -->
              </p>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          label="Buy point">
        </el-table-column>
        <el-table-column
          prop="orderNum"
          label="Quantity/手">
          <template slot-scope="scope">
            <div>
              {{(scope.row.orderNum).toFixed(0)}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="profitAndLose"
          label="Floating profit and loss">
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
          label="Total profit and loss">
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
         label="Margin">
       </el-table-column> -->
        <el-table-column
          label="Close the position">
          <template slot-scope="scope">
            <el-button class="btn-sell" plain title="I want to close my position" size="mini" @click="toSell(scope.row)"><i
              class="iconfont icon-chakan"></i>Close the position
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
        let data = await api.getIndexOrderList(opts)
        this.refresh = false
        if (data.status === 0) {
          if (data.data.list.length === this.list.list.length) {
            data.data.list.forEach((element, i) => {
              this.changeTextClass[i] = ''
              if (data.data.list[i].now_price !== this.list.list[i].now_price) {
                this.changeTextClass[i] = true // 设置对应的动画过滤
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
        // 查询表格
        this.getList()
      },
      async getList () {
        // 获取表格数据
        let opts = {
          state: 0,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true
        let data = await api.getIndexOrderList(opts)
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
          let data = await api.sellIndex(opt)
          if (data.status === 0) {
            this.hasChangeSell++
            console.log(this.hasChangeSell, 'haschangesell')
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
