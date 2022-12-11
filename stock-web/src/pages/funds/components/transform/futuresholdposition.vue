<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main>
        <div class="wrapper">
          <el-table
            :data="list.list"
            show-summary
            :summary-method="getSummaries"
            style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="props">

                <el-form label-position="left" inline class="demo-table-expand">
                  <div class="expand-row" v-if="props.row.orderDirection === 'bullish'">
                    Floating profit and loss:( {{props.row.nowPrice}} - {{props.row.buyOrderPrice}} ) * {{props.row.orderNum}} *
                    {{props.row.eachPoint}} = {{((props.row.nowPrice -
                    props.row.buyOrderPrice)*props.row.orderNum*props.row.eachPoint).toFixed(2)}}{{props.row.coinCode}}
                    ≈ {{(props.row.profitAndLose * props.row.coinRate).toFixed(2)}} 
                    <!-- USD -->
                  </div>
                  <div class="expand-row" v-else>
                    Floating profit and loss:-( {{props.row.nowPrice}} - {{props.row.buyOrderPrice}} ) * {{props.row.orderNum}} *
                    {{props.row.eachPoint}} = {{(-(props.row.nowPrice -
                    props.row.buyOrderPrice)*props.row.orderNum*props.row.eachPoint).toFixed(2)}}{{props.row.coinCode}}
                    ≈ {{(props.row.profitAndLose * props.row.coinRate).toFixed(2)}} 
                    <!-- USD -->
                  </div>
                  <el-form-item label="Margin">
                    <!-- USD -->
                    <span>{{props.row.allDepositAmt}} </span>
                  </el-form-item>
                  <el-form-item label="per standard lot">
                    <span>{{props.row.futuresStandard}}{{props.row.futuresUnit}}</span>
                  </el-form-item>
                  <el-form-item label="Quantity/lot">
                    <span>{{ props.row.orderNum}}</span>
                  </el-form-item>
                  <el-form-item label="Handling fee">
                    <span>{{ props.row.orderFee}} {{props.row.coinCode}} ≈ {{ (props.row.orderFee * props.row.coinRate).toFixed(2)}} USD</span>
                  </el-form-item>
                  <el-form-item label="Current exchange rate"> 
                    <!-- USD -->
                    <span>1{{props.row.coinCode}} ≈ {{ props.row.coinRate}}</span>
                  </el-form-item>

                  <el-form-item label="Buy time">
                    <span>{{props.row.buyOrderTime | timeFormat}}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              width="126px"
              prop="futuresName"
              label="Futures name/Symbol">
              <template slot-scope="scope">
                <span>{{scope.row.futuresName}}</span>
                <span class="code">{{scope.row.futuresCode}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="nowPrice"
              label="Latest price">
              <template slot-scope="scope">
                <p :class="scope.row.nowPrice - scope.row.buyOrderPrice > 0 ? 'red':'green'">
                  {{scope.row.nowPrice}}
                  <span class="jibi-code">{{scope.row.coinCode}}</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              prop="buyOrderPrice"
              label="Buy point">
              <template slot-scope="scope">
                <p>
                  <span>{{scope.row.buyOrderPrice}}</span>
                  <span class="jibi-code">{{scope.row.coinCode}}</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              prop="profitAndLose"
              label="Floating profit and loss">
              <template slot-scope="scope">
                <p :class="scope.row.profitAndLose > 0 ? 'red':'green'">
                  {{scope.row.profitAndLose}}
                  <span class="jibi-code">{{scope.row.coinCode}}</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              prop="allProfitAndLose"
              label="Total profit and loss">
              <template slot-scope="scope">
                <p :class="scope.row.allProfitAndLose > 0 ? 'red':'green'">
                  {{scope.row.allProfitAndLose}}
                  <span class="jibi-code">{{scope.row.coinCode}}</span>
                </p>
              </template>
            </el-table-column>
            <!-- <el-table-column
                prop="allDepositAmt"
                label="保证金USD">
            </el-table-column> -->
            <el-table-column
              width="80px"
              prop="orderDirection"
              label="Direction">
              <template slot-scope="scope">
                        <span :class="scope.row.orderDirection === 'bullish' ? 'red':'green'">
                            {{scope.row.orderDirection}}
                            <i v-if="scope.row.orderDirection === 'bullish'" class="iconfont icon-up"></i>
                            <i v-if="scope.row.orderDirection === 'bearish'" class="iconfont icon-down"></i>
                        </span>
              </template>
            </el-table-column>
            <!-- <el-table-column
                prop="orderNum"
                label="数量/手">
            </el-table-column>

            <el-table-column
                prop="orderFee"
                label="手续费">
                <template slot-scope="scope">
                    {{scope.row.orderFee}}
                    <span class="jibi-code">{{scope.row.coinCode}}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="futuresStandard"
                label="每标准手">
                <template slot-scope="scope">
                    {{scope.row.futuresStandard}} {{scope.row.futuresUnit}}
                </template>
            </el-table-column> -->
            <!-- <el-table-column
                width="165px"
                prop="buyOrderTime"
                label="买入时间">
                <template slot-scope="scope">
                    <b v-if="scope.row.buyOrderTime">{{scope.row.buyOrderTime | timeFormat}}</b>
                    <b v-else></b>
                </template>
            </el-table-column> -->
            <el-table-column
              fixed="right"
              prop="isLock"
              width="80px"
              label="Close the position">
              <template slot-scope="scope">
                <!-- <el-button type="primary" plain size="small" @click="toDetail(scope.row)">查看详情</el-button> -->
                <el-button type="success" plain size="small" @click="toSell(scope.row)">Close the position</el-button>
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
  </el-container>

</template>

<script>
  import HomeHeader from '../../../../components/HeaderOrder'
  import HomeFooter from '../../../../components/Footer'
  import MenuBox from '../menu'
  import * as api from '../../../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter,
      MenuBox
    },
    props: {},
    data () {
      return {
        pageNum: 1,
        pageSize: 15,
        futuresCode: '', // 代码
        indexSpell: '', // 简拼
        list: {
          list: []
        }
      }
    },
    watch: {},
    computed: {},
    created () {
      this.timer = setInterval(this.refreshList, 5000)
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getlist()
      this.$store.state.userMenu = 'futureshold'
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
        // 获取持仓列表
        let opt = {
          state: 0,
          futuresCode: this.futuresCode, // 代码
          indexSpell: this.indexSpell, // 简拼
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getFuturesOrderList(opt)
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
      async refreshList () {
        if (this.refresh || this.loading) {
          return
        }
        this.refresh = true
        this.changeTextClass = {}
        // 获取表格数据
        let opts = {
          state: 0,
          futuresCode: this.futuresCode, // 代码
          indexSpell: this.indexSpell, // 简拼
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getFuturesOrderList(opts)
        this.refresh = false
        if (data.status === 0) {
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
          if (data.success === false) {

          } else {
            this.$message.error(data.msg)
          }
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
            this.getlist()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消平倉'
          })
        })
      },
      getSummaries (param) {
        // 底部计算
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          // 第一行 不统计
          if (index === 0) {
            sums[index] = '統計'
            return
          }
          if (column.property === 'orderNum' || column.property === 'profitAndLose' || column.property === 'allProfitAndLose' || column.property === 'orderFee' || column.property === 'orderSpread' || column.property === 'orderStayFee') {
            // 需要计算列 ==> 数量 浮动盈亏 总盈亏 保证金 手续费 印花税 留仓费
            const values = data.map(item => Number(item[column.property]))
            if (!values.every(value => isNaN(value))) {
              sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                  return prev + curr
                } else {
                  return prev
                }
              }, 0)
              sums[index] = sums[index].toFixed(2)
            } else {
              sums[index] = 'N/A'
            }
          }
        })
        return sums
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
