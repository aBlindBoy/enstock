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
          <el-table :data="list.list" style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="demo-table-expand">
                  <div class="expand-row">
                    <p v-if="props.row.orderDirection === 'Bullish'">
                      Floating profit and loss：( {{ props.row.sellOrderPrice }} -
                      {{ props.row.buyOrderPrice }} ) *
                      {{ props.row.orderNum }} * {{ props.row.eachPoint }} =
                      {{
                        (
                          (props.row.sellOrderPrice - props.row.buyOrderPrice) *
                          props.row.orderNum *
                          props.row.eachPoint
                        ).toFixed(2)
                      }}
                      {{ props.row.coinCode }} ≈
                      {{ props.row.profitAndLose * props.row.coinRate }} USD
                    </p>
                    <p v-else>
                      Floating profit and loss：-( {{ props.row.sellOrderPrice }} -
                      {{ props.row.buyOrderPrice }} ) *
                      {{ props.row.orderNum }} * {{ props.row.eachPoint }} =
                      {{
                        (
                          -(
                            props.row.sellOrderPrice - props.row.buyOrderPrice
                          ) *
                          props.row.orderNum *
                          props.row.eachPoint
                        ).toFixed(2)
                      }}
                      {{ props.row.coinCode }} ≈
                      {{ props.row.profitAndLose * props.row.coinRate }} USD
                    </p>
                    <p>
                      Total profit and loss：({{ props.row.profitAndLose }} -
                      {{ props.row.orderFee }}) =
                      {{ props.row.profitAndLose - props.row.orderFee }}
                      {{ props.row.coinCode }} ≈
                      {{ props.row.allProfitAndLose * props.row.coinRate }}
                      USD
                    </p>
                  </div>
                  <el-form-item label="Margin">
                    <span>{{ props.row.allDepositAmt }} USD</span>
                  </el-form-item>
                  <el-form-item label="Per standard lot">
                    <span
                      >{{ props.row.futuresStandard
                      }}{{ props.row.futuresUnit }}</span
                    >
                  </el-form-item>
                  <el-form-item label="Handling fee">
                    <span
                      >{{ props.row.orderFee }} {{ props.row.sellRate }} ≈
                      {{
                        (props.row.orderFee * props.row.coinRate).toFixed(2)
                      }}
                      USD</span
                    >
                  </el-form-item>
                  <el-form-item label="Quantity/lot">
                    <span>{{ props.row.orderNum }}</span>
                  </el-form-item>
                  <el-form-item label="selling rate">
                    <span
                      >1{{ props.row.sellRate }} ≈
                      {{ props.row.coinRate }}</span
                    >
                  </el-form-item>
                  <el-form-item label="Buy time">
                    <span>{{ props.row.buyOrderTime | timeFormat }}</span>
                  </el-form-item>
                  <el-form-item label="Selling time">
                    <span>{{ props.row.sellOrderTime | timeFormat }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              prop="futuresName"
              width="126px"
              label="Futures name/Symbol"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.futuresName }}</span>
                <span class="code">{{ scope.row.futuresCode }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="buyOrderPrice" label="Buy point">
              <template slot-scope="scope">
                <span>{{ scope.row.buyOrderPrice }} </span>
                <span class="jibi-code">{{ scope.row.coinCode }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="sellOrderPrice" label="Sell point">
              <template slot-scope="scope">
                <span>{{ scope.row.sellOrderPrice }} </span>
                <span class="jibi-code">{{ scope.row.coinCode }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="profitAndLose" label="Floating profit and loss">
              <template slot-scope="scope">
                <p :class="scope.row.profitAndLose > 0 ? 'red' : 'green'">
                  {{ scope.row.profitAndLose }}
                  <span class="jibi-code">{{ scope.row.coinCode }}</span>
                </p>
                <p class="exchange-p">
                  ≈
                  {{
                    (scope.row.profitAndLose * scope.row.sellRate).toFixed(2)
                  }}
                  USD
                </p>
              </template>
            </el-table-column>
            <el-table-column prop="allProfitAndLose" label="Total profit and loss">
              <template slot-scope="scope">
                <p :class="scope.row.allProfitAndLose > 0 ? 'red' : 'green'">
                  {{ scope.row.allProfitAndLose }}
                  <span class="jibi-code">{{ scope.row.coinCode }}</span>
                </p>
                <p class="exchange-p">
                  ≈
                  {{
                    (scope.row.allProfitAndLose * scope.row.sellRate).toFixed(2)
                  }}
                  USD
                </p>
              </template>
            </el-table-column>
            <!-- <el-table-column
               prop="allDepositAmt"
               label="保证金 USD">
           </el-table-column> -->
            <el-table-column prop="orderDirection" width="80px" label="Direction">
              <template slot-scope="scope">
                <span
                  :class="scope.row.orderDirection === 'Bullish' ? 'red' : 'green'"
                >
                  {{ scope.row.orderDirection }}
                  <i
                    v-if="scope.row.orderDirection === 'Bullish'"
                    class="iconfont icon-up"
                  ></i>
                  <i
                    v-if="scope.row.orderDirection === 'Bearish'"
                    class="iconfont icon-down"
                  ></i>
                </span>
              </template>
            </el-table-column>
            <!-- <el-table-column
                prop="orderNum"
                label="数量">
            </el-table-column>
            <el-table-column
                prop="orderFee"
                label="手续费">
            </el-table-column>
            <el-table-column
                prop="futuresStandard"
                label="每标准手">
            </el-table-column> -->
            <!-- <el-table-column
                width="165px"
                prop="buyOrderTime"
                label="买入时间">
                <template slot-scope="scope">
                    <b v-if="scope.row.buyOrderTime">{{scope.row.buyOrderTime | timeFormat}}</b>
                    <b v-else></b>
                </template>
            </el-table-column>
            <el-table-column
                width="165px"
                prop="sellOrderTime"
                label="卖出时间">
                <template slot-scope="scope">
                    <b v-if="scope.row.sellOrderTime">{{scope.row.sellOrderTime | timeFormat}}</b>
                    <b v-else></b>
                </template>
            </el-table-column>
             -->
          </el-table>
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40, 50]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total"
          >
          </el-pagination>
        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import HomeHeader from "../../../../components/HeaderOrder";
import HomeFooter from "../../../../components/Footer";
import MenuBox from "../menu";
import * as api from "../../../../axios/api";

export default {
  components: {
    HomeHeader,
    HomeFooter,
    MenuBox
  },
  props: {},
  data() {
    return {
      futuresCode: "",
      indexSpell: "",
      pageNum: 1,
      pageSize: 15,
      list: {
        list: []
      }
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {
    this.getlist();
    this.$store.state.userMenu = "futuressell";
  },
  methods: {
    handleSizeChange(size) {
      this.pageSize = size;
      this.getlist();
    },
    handleCurrentChange(page) {
      this.pageNum = page;
      this.getlist();
    },
    async getlist() {
      // 获取资金明细
      let opt = {
        state: 1,
        futuresCode: this.futuresCode, // 代码
        indexSpell: this.indexSpell, // 简拼
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      let data = await api.getFuturesOrderList(opt);
      if (data.status === 0) {
        // data.data.list.forEach(element => {
        //     this.list.push(element)
        // });
        this.list = data.data;
        // this.total = data.data.total
      } else {
        this.$message.error(data.msg);
      }
    }
  }
};
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
