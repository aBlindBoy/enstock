<template>
  <div class="table-box-cont">
    <div class="table">
      <el-table
        stripe
        v-loading="loading"
        :data="list.list"
        @row-click="toTransaction"
        style="width: 100%"
        empty-text="No Data"
      >
        <el-table-column prop="name" width="200px" align="left" label="Stock Name">
          <template slot-scope="scope">
            <span style="padding-right:6px">{{ scope.row.stockName }}</span>
         
          </template>
        </el-table-column>

        <el-table-column prop="stockCode" width="200px" align="left" label="Stock Code">
          <template slot-scope="scope">
            <span class="code">
              {{ scope.row.stockCode }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="nowPrice"
          width="130px"
          align="left"
          label="Current price"
        >
          <template slot-scope="scope">
            <div class="price" v-if="scope.row.nowPrice">
              <div
                style="font-size: 12px;"
                :class="
                  scope.row.hcrate < 0
                    ? 'green'
                    : scope.row.hcrate == 0
                    ? ''
                    : 'red'
                "
              >
                {{ Number(scope.row.nowPrice).toFixed(2) }}
                <i
                  v-if="scope.row.hcrate > 0"
                  class="iconfont icon-direction-top"
                ></i>
                <i
                  v-if="scope.row.hcrate < 0"
                  class="iconfont icon-down"
                ></i>
              </div>
            </div>
            <div v-else>--</div>
            <!-- <div v-if="scope.row.now_price" :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
              <p :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                {{scope.row.now_price === 0?'-':scope.row.now_price}}
              </p>
            </div> -->
          </template>
        </el-table-column>
        <el-table-column prop="hcrate" align="left" label="Quote change">
          <template slot-scope="scope">
            <div class="price"  v-if="scope.row.hcrate">
              <div
                :class="
                  scope.row.hcrate < 0
                    ? 'green'
                    : scope.row.hcrate == 0
                    ? ''
                    : 'red'
                "
              >
                {{ Number(scope.row.hcrate).toFixed(2) }}%
                <!-- <i v-if="scope.row.hcrate>0" class="iconfont icon-up"></i>
                <i v-if="scope.row.hcrate<0" class="iconfont icon-down"></i> -->
              </div>
            </div>
            <div v-else>--</div>
          </template>
        </el-table-column>
        <el-table-column prop="today_max" align="left" label="Highest">
          <template slot-scope="scope">
              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'" v-if="scope.row.today_max">{{scope.row.today_max}}</div>
              <div v-else>--</div>
          </template>
        </el-table-column>
        <el-table-column prop="today_min" align="left" label="Lowest">
          <template slot-scope="scope">
              <div :class="scope.row.hcrate<0?'green':scope.row.hcrate==0?'':'red'" v-if="scope.row.today_min">{{scope.row.today_min}}</div>
              <div v-else>--</div>
          </template>
        </el-table-column>
        <!-- <el-table-column
          class="tab-name"
          align='center'
          label="走勢圖"
          width="370px"
        >
          <template slot-scope="scope">
            <chart-box :code="scope.row.code"></chart-box>
          </template>
        </el-table-column> -->
      </el-table>
      <div class="page-box text-center">
        <!-- <a class="more-btn" href="javascript:;">
          加载更多
          <i class="iconfont icon-xiasanjiao"></i>
        </a> -->
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
    </div>
  </div>
</template>

<script>
// import * as api from '../../../axios/api'
import ChartBox from "./chart";

export default {
  components: {
    ChartBox
  },
  props: {
    list: {
      type: Object,
      default() {
        return {
          list: []
        };
      }
    },
    getData: {
      type: Function,
      default: function() {}
    },
    handleOptions: {
      type: Function,
      default: function() {}
    }
  },
  data() {
    return {
      loading: false,
      refresh: false // 刷新中
    };
  },
  watch: {},
  computed: {},
  mounted() {},
  methods: {
    handleSizeChange(size) {
      this.handleOptions({ pageSize: size });
      this.getData();
    },
    handleCurrentChange(page) {
      this.handleOptions({ pageNum: page });
      this.getData();
    },
    toTransaction(row, column, event) {
      // 去交易界面
      this.$router.push({
        path: "/transaction",
        query: {
          code: row.stockCode
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
.table {
  min-height: 500px;
  /deep/ .el-table{
    
  }
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

  /deep/ .el-table  .cell {
    padding-left: 20px;
  }

  /deep/ .el-table td.el-table__cell, .el-table th.el-table__cell.is-leaf {
  border-bottom: 0px solid #dfe6ec !important;
  }

  // .table-wrapper ::v-deep .el-table {
  //   /* 表格字体颜色 */
  //   color: white;
  //   /* 表格边框颜色 */
  //   border: 0.5px solid #fcfcfc00;
  //   height: 80%;
  // }

  // /* 删除表格下横线 */
  // .table-wrapper ::v-deep .el-table::before {
  //   left: 0;
  //   bottom: 0;
  //   width: 100%;
  //   height: 0px;
  // }
  // /* 删除单元格底部横线 */
  // .table-wrapper ::v-deep .el-table td {
  //   border-bottom: 0px solid #dfe6ec !important;
  // }
}
</style>
