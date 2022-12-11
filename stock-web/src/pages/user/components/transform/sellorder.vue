<template>
  <el-container class="user-center sellorder">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot">
      <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main style=" min-height: calc(100vh - 150px );">
        <div class="wrapper">
          <div class="user-center-title" style="text-align:left;">
            <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
            {{$t('financingClose.title')}}
          </div>
          <div class="table-search">
            <el-row type="flex" justify='end'>
              <el-col :span="8">
                <el-input size="small" v-model="stockCode" placeholder="Please enter code">
                  <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                </el-input>
              </el-col>
              <!-- <el-col :span="8">
                <el-input size="small" v-model="stockSpell" placeholder="請輸入簡拼">
                  <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                </el-input>
              </el-col> -->
            </el-row>
          </div>
          <el-table
            :data="list.list"
            style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="Handling fee">
                    <span>{{ scope.row.orderFee}}</span>
                  </el-form-item>
                  <el-form-item label="Stamp duty">
                    <span>{{ scope.row.orderSpread}}</span>
                  </el-form-item>
                  <el-form-item label="Storage fee">
                    <span>{{ scope.row.orderStayFee}}</span>
                  </el-form-item>
                  <el-form-item label="Days to stay">
                    <span>{{ scope.row.orderStayDays}}</span>
                  </el-form-item>
                  <el-form-item v-if="scope.row.isLock === 1" label="Lock reason">
                    <span>{{ scope.row.lockMsg}}</span>
                  </el-form-item>
                  <el-form-item label="Buy time">
                    <span>{{scope.row.buyOrderTime | timeFormat}}</span>
                  </el-form-item>
                  <el-form-item label="Selling time">
                    <span>{{scope.row.sellOrderTime | timeFormat}}</span>
                  </el-form-item>
                  <el-form-item label="Spread fee">
                    <span>{{ scope.row.spreadRatePrice}}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              prop="stockName"
              width="126px"
              :label="$t('common.stockName')">
              <template slot-scope="scope">
                <span>{{scope.row.stockName}}</span>
                <p>
                  <!-- <i v-if="scope.row.stockPlate === '科創'" class="iconfont kechuang-stock">科創</i> -->
                  <span class="code">{{scope.row.stockCode}}</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              prop="buyOrderPrice"
              :label="$t('common.purchasePrice')">
              <template slot-scope="scope">
                <span>{{scope.row.buyOrderPrice}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="sellOrderPrice"
              :label="$t('common.sellPrice')">
            </el-table-column>
            <el-table-column
              prop="profitAndLose"
              :label="$t('common.ploatingProfitAndLoss')">
              <template slot-scope="scope">
                <span :class="scope.row.profitAndLose > 0 ? 'red':'green'">{{scope.row.profitAndLose}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="allProfitAndLose"
              :label="$t('common.totalProfitAndLoss')">
              <template slot-scope="scope">
                <span :class="scope.row.allProfitAndLose > 0 ? 'red':'green'">{{scope.row.allProfitAndLose}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="orderDirection"
              :label="$t('common.direction')">
              <template slot-scope="scope">
                        <span :class="scope.row.orderDirection === 'bullish' ? 'red':'green'">
                            {{scope.row.orderDirection}}
                            <i v-if="scope.row.orderDirection === 'bullish'" class="iconfont icon-up"></i>
                            <i v-if="scope.row.orderDirection === 'bearish'" class="iconfont icon-down"></i>
                        </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="orderNum"
              :label="$t('common.quantity')">
            </el-table-column>
           <!--  <el-table-column
              prop="orderTotalPrice"
              label="The total market capitalization">
            </el-table-column>
            <el-table-column
                prop="orderFee"
                label="Handling fee">
            </el-table-column>
            <el-table-column
                prop="orderSpread"
                label="Stamp duty">
            </el-table-column>
            <el-table-column
                prop="orderStayFee"
                label="Storage fee">
            </el-table-column>
            <el-table-column
                prop="orderStayDays"
                label="Days to stay">
            </el-table-column>
            <el-table-column
                width="165px"
                prop="buyOrderTime"
                label="時間">
                <template slot-scope="scope">
                    <b v-if="scope.row.buyOrderTime">{{scope.row.buyOrderTime | timeFormat}}</b>
                    <b v-else></b>
                </template>
            </el-table-column> -->
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
  import HomeHeader from '@/components/HeaderOrder'
  import MenuBox from '../menu'
  import * as api from '@/axios/api'

  export default {
    components: {
      HomeHeader,
      MenuBox
    },
    props: {},
    data () {
      return {
        stockCode: '',
        stockSpell: '',
        pageNum: 1,
        pageSize: 15,
        list: {
          list: []
        }
      }
    },
    watch: {},
    computed: {},
    created () {},
    mounted () {
      this.getlist()
      this.$store.state.userMenu = '2-3'
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
        // 獲取資金明細
        let opt = {
          state: 1,
          stockCode: this.stockCode, // 代碼
          // stockSpell: this.stockSpell, // 簡拼
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getOrderList(opt)
        if (data.status === 0) {
          // data.data.list.forEach(element => {
          //     this.list.push(element)
          // });
          this.list = data.data
          // this.total = data.data.total
        } else {
          this.$message.error(data.msg)
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
