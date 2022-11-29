<template>
  <div>
    <div class="table">
      <el-table
        stripe
        v-loading="loading"
        :data="list.list"
        @row-click='toTransaction'
        style="width: 100%">
        <el-table-column
          prop="name"
          width="200px"
          label="name">
          <template slot-scope="scope">
            <div class="tab-name">
              <p>{{scope.row.name}}</p>
              <span class="code">
                {{scope.row.code}}
            </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="nowPrice"
          width="130px"
          align='right'
          label="Current price">
          <template slot-scope="scope">
            <div class="price">
              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'">
                <span v-if="scope.row.nowPrice === 0">-</span>
                <span v-else>{{Number(scope.row.nowPrice).toFixed(2)}}</span>
                <i v-if="scope.row.hcrate>0" class="iconfont icon-up"></i>
                <i v-if="scope.row.hcrate<0" class="iconfont icon-down"></i>
              </div>
            </div>
            <!-- <div v-if="scope.row.now_price" :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
              <p :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                {{scope.row.now_price === 0?'-':scope.row.now_price}}
              </p>
            </div> -->
          </template>
        </el-table-column>
        <el-table-column
          prop="hcrate"
          align='right'
          label="Quote change">
          <template slot-scope="scope">
            <div class="price">

              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'">
                <span v-if="scope.row.nowPrice === 0">-</span>
                <span v-else>{{Number(scope.row.hcrate).toFixed(2)}}%</span>
                <!-- <i v-if="scope.row.hcrate>0" class="iconfont icon-up"></i>
                <i v-if="scope.row.hcrate<0" class="iconfont icon-down"></i> -->
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="today_max"
          align='right'
          label="Highest">
          <!-- <template slot-scope="scope">
              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'">{{scope.row.today_max}}</div>
          </template> -->
        </el-table-column>
        <el-table-column
          prop="today_min"
          align='right'
          label="Lowest">
          <!-- <template slot-scope="scope">
              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'">{{scope.row.today_min}}</div>
          </template> -->
        </el-table-column>
        <el-table-column
          class="tab-name"
          align='center'
          label="Trend"
          width="370px"
        >
          <template slot-scope="scope">
            <chart-box :code="scope.row.code"></chart-box>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-box text-center">
        <a @click="toTransaction" class="more-btn" href="javascript:;">
          load more
          <i class="iconfont icon-xiasanjiao"></i>
        </a>
        <!-- <el-pagination
        class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40,50]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination> -->
      </div>

    </div>
  </div>

</template>

<script>
  import * as api from '../../../axios/api'
  import ChartBox from './chart'

  export default {
    components: {
      ChartBox
    },
    props: {},
    data () {
      return {
        loading: false,
        form: {
          pageNum: 1,
          pageSize: 15
        },
        list: {
          list: []
        },
        timer: null,
        refresh: false, // 刷新中
        changeTextClass: {} // 表格中的数据变化
      }
    },
    watch: {},
    computed: {},
    created () {
      this.timer = setInterval(this.refreshList, 60000)
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getList()
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
        let data = await api.getStock()
        this.refresh = false
        if (data.status === 0) {
          data.data.list.forEach((element, i) => {
            this.changeTextClass[i] = ''
            if (data.data.list[i].nowPrice !== this.list.list[i].nowPrice) {
              this.changeTextClass[i] = true // 设置对应的动画过滤
              this.list.list[i].nowPrice = data.data.list[i].nowPrice // 现价
              this.list.list[i].hcrate = data.data.list[i].hcrate // 涨跌幅
              this.list.list[i].today_max = data.data.list[i].today_max // 最高
              this.list.list[i].today_min = data.data.list[i].today_min // 最低
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      },
      onSubmit () {
        // 查询表格
        this.getList()
      },
      async getList () {
        // 获取表格数据
        let opt = {
          pageNum: this.form.pageNum,
          pageSize: 15
        }
        this.loading = true
        let data = await api.getStock(opt)
        if (data.status === 0) {
          // data.data.list.forEach(element => {
          //     this.list.push(element)
          // });
          this.list = data.data
        } else {
          this.$message.error(data.msg)
        }
        this.loading = false
      },
      toTransaction (row, column, event) {
        // 去交易界面
        this.$router.push({
          path: '/transaction',
          query: {
            code: row.code
          }
        })
      }
    }
  }
</script>
<style lang="less" scoped>
  .table {
    .code {
      color: #6d718b;
      font-size: 12px;
    }

    .more-btn {
      text-align: center;
      color: #8f92a3;
    }

    /*/deep/ th.el-table_1_column_1 {*/
      /*padding-left: 50px;*/
    /*}*/

    /deep/ .el-table th>.cell {
      padding-left: 60px;
    }
  }
</style>
