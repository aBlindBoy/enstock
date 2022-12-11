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
          <!-- <div class="table-search">
              <el-row type="flex" justify='end'>
                  <el-col :span="8">
                      <el-input size="small" v-model="indexCode" placeholder="请输入代码">
                          <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                      </el-input>
                  </el-col>
                  <el-col :span="8">
                      <el-input size="small" v-model="indexSpell" placeholder="请输入简拼">
                          <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                      </el-input>
                  </el-col>
              </el-row>
          </div> -->
          <el-table
            :data="list.list"
            style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="Floating price">
                    <span>{{ scope.row.eachPoint}}</span>
                  </el-form-item>
                  <el-form-item label="Bilateral handling fee">
                    <span>{{ scope.row.orderFee}}</span>
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
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              prop="indexName"
              width="126px"
              label="Index name/code">
              <template slot-scope="scope">
                <span>{{scope.row.indexName}}</span>
                <span class="code">{{scope.row.indexCode}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="buyOrderPrice"
              label="Buy point">
              <template slot-scope="scope">
                <span>{{scope.row.buyOrderPrice}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="sellOrderPrice"
              label="Sell points">
            </el-table-column>
            <el-table-column
              prop="profitAndLose"
              label="Floating profit and loss">
              <template slot-scope="scope">
                <span :class="scope.row.profitAndLose > 0 ? 'red':'green'">{{scope.row.profitAndLose}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="allProfitAndLose"
              label="Total profit and loss">
              <template slot-scope="scope">
                <span :class="scope.row.allProfitAndLose > 0 ? 'red':'green'">{{scope.row.allProfitAndLose}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="allDepositAmt"
              label="Margin">
            </el-table-column>
            <el-table-column
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
            <el-table-column
              prop="orderNum"
              label="Quantity">
            </el-table-column>
            <!-- <el-table-column
                prop="orderFee"
                label="Handling fee">
            </el-table-column>
            <el-table-column
                prop="eachPoint"
                label="Floating price">
            </el-table-column>
            <el-table-column
                width="165px"
                prop="buyOrderTime"
                label="Buy time">
                <template slot-scope="scope">
                    <b v-if="scope.row.buyOrderTime">{{scope.row.buyOrderTime | timeFormat}}</b>
                    <b v-else></b>
                </template>
            </el-table-column>
            <el-table-column
                width="165px"
                prop="sellOrderTime"
                label="Selling time">
                <template slot-scope="scope">
                    <b v-if="scope.row.sellOrderTime">{{scope.row.sellOrderTime | timeFormat}}</b>
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
  </el-container>

</template>

<script>
  import HomeHeader from '@/components/HeaderOrder'
  import HomeFooter from '@/components/Footer'
  import MenuBox from '../menu'
  import * as api from '@/axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter,
      MenuBox
    },
    props: {},
    data () {
      return {
        indexCode: '',
        indexSpell: '',
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
      this.$store.state.userMenu = 'indexsell'
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
        // 获取资金明细
        let opt = {
          state: 1,
          indexCode: this.indexCode, // 代码
          indexSpell: this.indexSpell, // 简拼
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getIndexOrderList(opt)
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
