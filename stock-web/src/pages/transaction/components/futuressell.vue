<template>
  <div>
    <div class="table transaction-table">
      <el-table
        stripe
        v-loading="loading"
        :data="list.list"
        height="250"
        style="width: 100%">
        <el-table-column
          width="120px"
          prop="futuresName"
          label="futures name">
          <template slot-scope="scope">
            <p class="name">
              <span>{{scope.row.futuresName}}</span>
            </p>
            <p class="exchange-p">
              {{scope.row.futuresCode}}
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="profitAndLose"
          label="Floating profit and loss">
          <template slot-scope="scope">
            <p :class="scope.row.profitAndLose<0?'bounceIn green':'bounceIn red'">
              <span>{{scope.row.profitAndLose}}</span>
              <span class="jibi-code">{{scope.row.coinCode}}</span>
            </p>
            <p class="exchange-p">≈{{(scope.row.profitAndLose*scope.row.sellRate).toFixed(2)}} USD</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="allProfitAndLose"
          label="total profit and loss">
          <template slot-scope="scope">
            <p :class="scope.row.allProfitAndLose<0?'bounceIn green':'bounceIn red'">
              <span>{{scope.row.allProfitAndLose}}</span>
              <span class="jibi-code">{{scope.row.coinCode}}</span>
            </p>
            <p class="exchange-p">≈{{(scope.row.allProfitAndLose*scope.row.sellRate).toFixed(2)}} USD</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          label="buy point">
          <template slot-scope="scope">
            {{scope.row.buyOrderPrice}}
            <span class="jibi-code">{{scope.row.coinCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="sellOrderPrice"
          label="Sell points">
          <template slot-scope="scope">
            {{scope.row.sellOrderPrice}}
            <span class="jibi-code">{{scope.row.coinCode}}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="70px"
          prop="orderNum"
          label="quantity">
        </el-table-column>
        <el-table-column
          width="80px"
          prop="orderDirection"
          label="Direction">
          <template slot-scope="scope">
            <p>
              {{scope.row.orderDirection}}
              <i v-if="scope.row.orderDirection === 'Bullish'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  === 'Bearish'" class="green iconfont icon-down"></i>
            </p>
          </template>
        </el-table-column>
        <!-- <el-table-column
         width="120px"
         prop="orderTotalPrice"
         label="保证金">
       </el-table-column> -->
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
      hasChangeSell: {
        type: Number,
        default: function () {
          return 0
        }
      }
    },
    data () {
      return {
        loading: false,
        pageNum: 1,
        pageSize: 10,
        currentNum: 0,
        list: {
          list: []
        },
        detail: null // 详情数据
      }
    },
    watch: {
      haslogin (newval, oldVal) {
        if (newval !== oldVal) {
          this.getList()
        }
      },
      hasChangeSell (newval, oldVal) {
        if (newval) {
          this.list = []
          this.getList()
        }
      },
      deep: true
    },
    computed: {
      haslogin () {
        return this.$store.state.haslogin
      }
    },
    created () {},
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
      onSubmit () {
        // 查询表格
        this.getList()
      },
      async getList () {
        // 获取表格数据
        let opts = {
          state: 1,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true
        let data = await api.getFuturesOrderList(opts)
        this.loading = false
        if (data.status === 0) {
          this.list = data.data
        } else {
          this.$message.error(data.msg)
        }
      }
    }
  }
</script>
