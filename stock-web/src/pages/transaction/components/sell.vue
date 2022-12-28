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
          width="180px"
          prop="stockName"
          :label="$t('common.stockName')">
          <template slot-scope="scope">
            <p class="name">
              <span>{{scope.row.stockName}}/{{scope.row.stockCode}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="profitAndLose"
          :label="$t('common.ploatingProfitAndLoss')">
          <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.profitAndLose<0?'green':'red'">{{scope.row.profitAndLose}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="allProfitAndLose"
          :label="$t('common.totalProfitAndLoss')">
          <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.allProfitAndLose<0?'green':'red'">{{scope.row.allProfitAndLose}}</span>
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="buyOrderPrice"
          :label="$t('common.purchasePrice')">
        </el-table-column>
        <el-table-column
          prop="sellOrderPrice"
          :label="$t('common.sellPrice')">
        </el-table-column>
        <el-table-column
          width="110px"
          prop="orderNum"
          :label="$t('common.quantity')">
        </el-table-column>
        <el-table-column
          prop="orderDirection"
          :label="$t('common.direction')">
          <template slot-scope="scope">
            <p>
              {{scope.row.orderDirection}}
              <i v-if="scope.row.orderDirection === 'bullish'" class="red iconfont icon-up"></i>
              <i v-if="scope.row.orderDirection  === 'bearish'" class="green iconfont icon-down"></i>
            </p>
          </template>
        </el-table-column>
      </el-table>
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
          stockCode: '', // 代码
          stockSpell: this.marketType, 
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true
        let data = await api.getOrderList(opts)
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
