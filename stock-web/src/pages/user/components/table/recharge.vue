<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot recharge">
      <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main style=" min-height: calc(100vh - 150px );">
        <div class="user-center-title" style="text-align:left;">
            <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
            Recharge records
        </div>
        <div class="wrapper">
          <el-table v-if="true"
                    :data="list.list"
                    style="width: 100%">
            <el-table-column
              width="100px"
              prop="payChannel"
              >
              <template slot-scope="scope">
                <!-- <span class="paytype" style="background-color: #138EB4;" v-if="scope.row.payChannel === '0'">支付寶</span> -->
                <span class="paytype" style="background-color: ;" v-if="scope.row.payChannel === 1">對公轉帳</span>
                <!-- <span class="paytype" style="background-color: #04823E;" v-else-if="scope.row.payChannel === 2">微信</span> -->
                <span class="paytype" style="background-color: ;" v-else>{{scope.row.payChannel}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="payAmt"
              
              width="400px">
              <template slot-scope="scope">
                <span class="number">Recharge: <span class="rmb">{{scope.row.payAmt}}</span></span>
              </template>
            </el-table-column>
            <el-table-column
              width="168px"
              prop="orderSn"
              >
            </el-table-column>
            <el-table-column
              prop="addTime"
              width="178px"
              >
              <template slot-scope="scope">
                <b v-if="scope.row.addTime">{{scope.row.addTime | timeFormat}}</b>
                <b v-else></b>
              </template>
            </el-table-column>
            <el-table-column
              prop="orderStatus"
              >
              <template slot-scope="scope">
                        <span :class="scope.row.orderStatus === 1?'green':scope.row.orderStatus === 2?'red':'red'">
                            <!-- 1 => 成功 2 失敗 3取消 4 等待 -->
                            <i v-if="scope.row.orderStatus === 1" class="iconfont icon-tongguo4 animated bounceIn"></i>
                            <i v-if="scope.row.orderStatus==0" class="iconfont icon-dengdai animated bounceInDown"></i>
                            <i v-if="scope.row.orderStatus === 2"
                               class="iconfont icon-failure animated bounceInDown"></i>
                            <i v-if="scope.row.orderStatus === 3"
                               class="iconfont icon-iconfontweitongguo animated bounceInDown"></i>
                            {{scope.row.orderStatus === 1?'Recharge succeeded':scope.row.orderStatus === 2?'Recharge failed':
                            scope.row.orderStatus === 3?'Cancel Recharge':'Under review'}}
                        </span>
              </template>
            </el-table-column>
          </el-table>
          <el-card class="box-card">
            <!-- <div class="user-center-title">
              Recharge記錄
            </div> -->



            <!-- <div v-if="false" v-for="item in list.list" :key="item.key" class="box-card-item">
              <div>
                <el-tag :type="item.payChannel === '0'?'':item.payChannel === 1?'warning':'success'">
                  <span v-if="item.payChannel === '0'">對公轉帳</span>
                  <span v-else-if="item.payChannel === 1">對公轉帳</span>
                  <span v-else>{{item.payChannel}}</span>
                </el-tag>
                <span class="money">{{item.payAmt}}</span>
                <div class="pull-right">
                            <span :class="item.orderStatus === 1?'green':item.orderStatus === 2?'red':'red'">
                                <i v-if="item.orderStatus === 1" class="iconfont icon-tongguo4 animated bounceIn"></i>
                                <i v-if="item.orderStatus==0" class="iconfont icon-dengdai animated bounceInDown"></i>
                                <i v-if="item.orderStatus === 2"
                                   class="iconfont icon-failure animated bounceInDown"></i>
                                <i v-if="item.orderStatus === 3"
                                   class="iconfont icon-iconfontweitongguo animated bounceInDown"></i>
                                {{item.orderStatus === 1?'Recharge成功':item.orderStatus === 2?'Recharge失敗':item.orderStatus === 3?'取消Recharge':'Under review'}}
                            </span>
                </div>
              </div>
              <div>
                <p>訂單號：{{item.orderSn}}</p>
                <p class="mini-font">
                  Recharge時間：{{item.addTime | timeFormat}}
                </p>
              </div>

            </div> -->
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
          </el-card>

        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
    </div>
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
      this.$store.state.userMenu = '2-5'
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
        // 获取 提现记录
        let opts = {
          pageNum: this.pageNum,
          pageSize: 10
        }
        let data = await api.rechargeList(opts)
        if (data.status === 0) {
          // data.data.list.forEach(element => {
          //     this.list.push(element)
          // });
          this.list = data.data
          console.log(data)
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

  .number {
    font-size: 18px;
    color: #333;
  }

  .box-card-item {
    // box-shadow: 0 2px 12px 0 #ddd;
    padding: 10px 20px;
    border-radius: 4px;
    margin-bottom: 20px;
    line-height: 30px;
    border-bottom: 1px solid #eee;

    .money {
      font-size: 20px;
    }

    p {
      color: #666;
    }
  }

  .box-card {
    padding-bottom: 20px;
  }
</style>
