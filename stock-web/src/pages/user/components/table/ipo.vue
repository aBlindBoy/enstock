<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot message">
      <el-container class="main-wrapper">
        <el-aside width="200px">
          <menu-box></menu-box>
        </el-aside>
        <el-main style=" min-height: calc(100vh - 150px );">
          <div class="con-box account futuresholdposition">
            <el-tabs v-model="activeName" class="aaaa" @tab-click="tabsClick"	>
              <el-tab-pane :label="$t('ipo.openSubscription')" name="1">
                <div class="user-info">
                  <el-table :data="list" style="width: 100%">
        
                    <el-table-column prop="stockName" :label="$t('ipo.newStockName')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockName }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="stockCode" :label="$t('common.stockCode')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockCode }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="stockPlate" :label="$t('ipo.issueMarket')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockPlate }}</span>
                      </template>
                    </el-table-column>


                    <el-table-column prop="ticketingDate" :label="$t('ipo.dateOfIssuance')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.ticketingDate }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="subscriptionTime" :label="$t('ipo.subscriptionPeriod')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.subscriptionTime }}</span>
                      </template>
                    </el-table-column>
                    <!-- <el-table-column prop="underwritingSheet" label="承銷張數">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.underwritingSheet }}</span>
                      </template>
                    </el-table-column> -->

                    <el-table-column prop="underwritingPrice" :label="$t('ipo.underwritingPrice')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.underwritingPrice }}</span>
                      </template>
                    </el-table-column>

                    <el-table-column prop="marketPrice" :label="$t('ipo.marketPrice')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.marketPrice }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      fixed="right"
                      prop="isLock"
                      width="120px"
                      :label="$t('ipo.operate')"
                    >
                      <template slot-scope="scope">
                           <div  v-if="isSubscribe(scope.row.subscriptionTime)"  @click="clickCtock(scope.row)" class="foot-btn">
                            <i class='font-icon'></i>
                            <el-button
                                type="success"
                                plain
                                size="small"
                                @click="clickSubscribe(scope.row)"
                                >{{$t('ipo.subscription')}}</el-button
                              >
                           </div>
                            <div v-else class="foot-btn">
                                {{scope.row.remark ==''?'has not started':scope.row.remark}}
                            </div>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('ipo.subscriptionList')" name="2">
                <div class="user-info">
                  <el-table :data="historyList" style="width: 100%">
                   
                    <el-table-column prop="stockName" :label="$t('ipo.newStockName')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockName }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="stockCode" :label="$t('common.stockCode')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockCode }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="stockPlate" :label="$t('ipo.issueMarket')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.stockPlate }}</span>
                      </template>
                    </el-table-column>


                    <el-table-column prop="ticketingDate" :label="$t('ipo.dateOfIssuance')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.ticketingDate }}</span>
                      </template>
                    </el-table-column>
                 
                    <!-- <el-table-column prop="underwritingSheet" label="承銷張數">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.underwritingSheet }}</span>
                      </template>
                    </el-table-column> -->

                    <el-table-column prop="underwritingPrice" :label="$t('ipo.underwritingPrice')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.underwritingPrice }}</span>
                      </template>
                    </el-table-column>

                    <el-table-column prop="marketPrice" :label="$t('ipo.marketPrice')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.marketPrice }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="submitTime" :label="$t('ipo.subscriptionDate')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ formatDate(scope.row.submitTime) }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="submitAmount" :label="$t('ipo.fundsPurchased')">
                      <template slot-scope="scope">
                        <span style="color: #fff">{{ scope.row.submitAmount }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="endTime" :label="$t('ipo.dateOfWinningTheLottery')">
                      <template slot-scope="scope">
                        <span v-if="scope.row.endTime!=null" style="color: #fff">{{ formatDate(scope.row.endTime) }}</span>
                        <span>-</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="tradeSheets" :label="$t('ipo.numberOfWinningTickets')">
                      <template slot-scope="scope">
                        <span v-if="scope.row.tradeSheets!=null" style="color: #fff">{{ scope.row.tradeSheets }}</span>
                        <span v-else>-</span>
                      </template>
                    </el-table-column>
                    <!-- <el-table-column
                      prop="status"
                      label="狀態">
                      <template slot-scope="scope">
                        <el-tag> </el-tag>
                      </template>
                    </el-table-column> -->
                    <el-table-column
                      fixed="right"
                      prop="isLock"
                      width="120px"
                      :label="$t('ipo.operate')"
                    >
                      <template slot-scope="scope">
                        <div v-if="scope.row.status != 1" >
                          <div v-if="scope.row.deductionStatus == 1" @click="clickPay(scope.row)">
                              <el-button  type="primary" v-if="isNowDay(scope.row)">{{$t('ipo.toPay')}}</el-button>
                              <el-button type="info" v-else disabled>{{$t('ipo.notDuePaymentTime')}}</el-button>
                          </div>
                          <div v-if="scope.row.deductionStatus == 2">
                              <el-button type="info" disabled>{{$t('ipo.paid')}}</el-button>
                          </div>
                        </div>
                        <div v-else>
                          <el-button type="success" disabled>{{statusType[scope.row.status]}}</el-button>
                        </div>
                      </template>
                    </el-table-column>
                  </el-table>


                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <el-dialog
            show-close
            :visible.sync="dialogCommunity"
            width="30%"
          >
            <div class="storeinformation_popup">
              <el-form :model="form" ref="form" class="demo-form">
                <div class="storeinformation_popup_top">
                  <el-form-item>
                    <el-input
                      type="text"
                      v-model="form.submitSheets"
                      :placeholder="$t('ipo.pleaseQuantity')"
                      clearable="true"
                      show-word-limit
                      oninput="value=value.replace(/[^\d]/g,'')"
                    >
                    </el-input>
                  </el-form-item>
                </div>
                <p> {{$t('common.stockName')}}:{{form.stockName}}</p>
                <p style="margin-top:20px"> {{$t('ipo.subscriptionPrice')}}:{{form.underwritingPrice}}USD</p>
                <p style="margin-top:20px"> {{$t('ipo.availableFunds')}}:{{formartAmount($store.state.userInfo.enableAmt)}}USD</p>
                <p v-if="form.sheets !=0" style="margin-top:20px"> {{$t('ipo.cost')}}:{{formartAmount(form.underwritingPrice*1000*form.submitSheets)}}USD</p>

                <el-form-item style="text-align:center;">
                  <!-- <el-button @click="dialogCommunity = false">取 消</el-button> -->
                  <el-button
                    type="primary"
                    @click="submitData()"
                   style="background-color: #fff !important;color:#333 !important;border-color:#DCDFE6 !important;"
                    >{{$t('common.confirm')}}
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-dialog>
        </el-main>
      </el-container>
    </div>
  </el-container>
</template>

<script>
import HomeHeader from "../../../../components/HeaderOrder";
import MenuBox from "../menu";
import * as api from "../../../../axios/api";
// import {  format } from 'date-fns'
const dayjs = require('dayjs')
import numeral from 'numeral';

import isBetween from 'dayjs/plugin/isBetween'
  dayjs.extend(isBetween)
export default {
  components: {
    HomeHeader,
    MenuBox,
  },
  props: {},
  data() {
    return {
      pageNum: 1,
      pageSize: 11,
      list: [],
      historyList: [],
      activeName: "1",
      dialogCommunity:false,
      saveStock:'',
      form: {
          underwritingPrice:null,
      },
      // statusType:,//,"部分中签"
    };
  },
  computed: {
    statusType(){
      return ["",this.$t('ipo.appointmentSuccessful'),this.$t('ipo.wonTheLottery'),
      this.$t('ipo.didNotWinTheLottery')]
    }

  },
  created() {
    this.$store.state.userMenu = '2-14'
    this.getHiStockList()
    this.getStockSubscribeHistoryList()
    this.getUserInfo()
  },
  mounted() {
    // (this.$store.state.userMenu = "2-14"), this.getlist();
    // this.getShengou();
    
  },
  watch: {
    // activeName(val, oldval) {
    //   console.log(val);
    //   if (val == 'rengoulist') {
    //     this.shengouList()
    //   }
    // },
  },
  methods: {
    tabsClick(){
      if (this.activeName == "1") {
          this.getHiStockList()
      }   
      if (this.activeName == "2") {
        this.getStockSubscribeHistoryList()
      } 
    },
    clickSubscribe(row){
      this.dialogCommunity = true
        this.form = row
    },
    formartAmount(amount){
        return numeral(amount).format('0,0')
    },
    formatDate(date){
        return dayjs(date).format('YYYY/MM/DD')
    },
    // async getlist() {
    //   let opt = {
    //     userId: 0,
    //     pageNum: this.pageNum,
    //     pageSize: this.pageSize,
    //   };
    //   console.log(opt);
    //   let data = await api.getUserApplyList(opt);
    //   console.log(data);
    //   if (data.status === 0) {
    //     this.list = data.data;
    //   } else {
    //     this.$message.error(data.msg);
    //   }
    // },
    // async getShengou() {
    //   // 
    //   let opt = {
    //     e:format(new Date(),'yyyy/MM/dd'),
    //     f:format(new Date()+1000*60*24*7,'yyyy/MM/dd'),
    //   };
    //   let data = await api.xingusg(opt);
    //   this.shengou = data.data.list;
    //   console.log(this.shengou, "申購");
    // },
    async getHiStockList() {
        // 獲取新股列表
        this.loading = true
        let data = await api.getHiStockList()
        if (data.status === 0) {
            this.list =  data.data.map(item=>{
                item.subscriptionTime =  dayjs().year()+"/"+item.subscriptionTime.split('~')[0]+'~'+dayjs().year()+"/"+item.subscriptionTime.split('~')[1]
                return item
            })
            this.loading = false
        }
    },
     /**判斷是否可以申購 */
      isSubscribe(subscriptionTime){
            let date1 =  subscriptionTime.split('~')[0]
            let date2 =  subscriptionTime.split('~')[2]
            return dayjs(new Date()).isBetween(date1, dayjs(date2)) 
        },
      async getUserInfo () {
              // 獲取用戶信息
              //   let showcookie = this.getCookie('USER_TOKEN');
              let data = await api.getUserInfo()
              if (data.status === 0) {
                  this.$store.state.userInfo = data.data
              } else {
                  Toast(data.msg)
              }
              // this.$store.state.user = this.user
          },
          async getStockSubscribeHistoryList() {
                let data = await api.getStockSubscribeHistoryList()
                if (data.status === 0) {
                this.historyList =  data.data.list.map(item=>{
                                        item.submitTime =  dayjs( item.submitTime)
                                        return item;
                                    })
                }
            },
             // 申購
             async submitData() {
                // if(this.$store.state.userInfo.enableAmt < this.form.underwritingPrice*1000*this.form.submitSheets){
                //     this.$message({
                //         message:"申購失敗，可用資金不足",
                //         type: 'warning'
                //     });
                //     return
                // }
                let params={...this.form};
                params.submitAmount = this.form.underwritingPrice*1000*this.form.submitSheets
                let data = await api.saveStockSubscribe(params);
                this.dialogCommunity = false
                if (data.status == 200) {
                    this.$message({
                        message: data.msg,
                        type: 'success'
                    });
                } else {
                    this.$message({
                        message: data.msg,
                        type: 'warning'
                    });
                }
            },
               /**判斷是否可以申購 */
               isSubscribe(subscriptionTime){
                let date1 =  subscriptionTime.split('~')[0]
                let date2 =  subscriptionTime.split('~')[1]
                return dayjs(new Date()).isBetween(date1, dayjs(date2)) 
            },
            isNowDay(item){
                return dayjs(new Date()).isBetween( item.drawDate, dayjs().year()+"/"+item.ticketingDate) 
                // return dayjs().isSame(date, 'day')
            },



             clickPay(item ) {
              this.$confirm(this.$t('ipo.confirmPay'), 'Tips', {
                confirmButtonText: this.$t('common.comfirm'),
                cancelButtonText: this.$t('common.cancel'),
                type: 'warning'
              }).then( () => {
                  this.pay(item )
              });
            },

            async pay(item ){
              let opt = {
                      id: item.id
                  }
                let data = await api.payStockSubscribe(opt)
                  if (data.status === 0) {
                    this.$message({
                      type: 'success',
                      message: data.data
                    });
                    this.getStockSubscribeHistoryList()
                  } else {
                    if (data.status === 0) {
                      this.$message({
                        type: 'success',
                        message: data.data
                      });
                  }
            }
          },
    // async shengData() {
    //   var timestamps = (new Date()).getTime();
    //   let opt = {
    //     sgname:this.saveStock.names,
    //     sgdaima:this.saveStock.code,
    //     sgprice:this.saveStock.price,
    //     sgsumber:this.form.shehao,
    //     tmes:timestamps,
    //   };
    //   console.log(opt);
    //   let data = await api.xingusgs(opt);
    //   this.shengousaveStock = data.data.list;
    //   console.log(this.shengousaveStock, "申購提交");
    //   this.dialogCommunity = false
    //   if(data.status == 200){
    //     this.$message({
    //       message: data.msg,
    //       type: 'success'
    //     });
    //   }else{
    //     this.$message({
    //       message: data.msg,
    //       type: 'warning'
    //     });
    //   }
    // },
      // async shengouList(){
      //   let opt = {};
      //   let data = await api.xingusgsList(opt);
      //   this.sgList = data.data.list;
      //   // this.timestampToTime()
      //   console.log(this.sgList)
      // },
    },
};
</script>
<style lang="less" scoped>
/deep/ .el-input__inner{
  background-color: #fff !important;
  border: 1px solid #DCDFE6 !important;
  color: #333 !important;
}
/deep/ .cell {
  color: #fff !important;
}
/deep/  .el-tabs__item {
  color: #d8d8d8 !important;
}
/deep/ .el-tabs__item.is-active {
  color: #409eff !important;
}
/deep/ .black-bg .futuresholdposition .el-table th .cell {
  color: #fff !important;
}

.red {
  color: red !important;
}
.green {
  color: #17b780 !important;
}
.number {
  background-color: rgba(250, 250, 250, 0) !important;
}
.table {
  min-height: 500px;

  .code {
    color: #6d718b;
    font-size: 12px;
  }

  .more-btn {
    text-align: center;
    color: #8f92a3;
  }

  /deep/ th.el-table_1_column_1 {
    padding-left: 50px;
  }
}

.con-box {
  // color: #fff;
  padding: 0 20px;

  .box-account {
    padding: 20px 10px 0;

    .name {
      // font-size: 16px;
    }

    .account {
      // font-size: 46px;
      font-weight: 400;
    }

    .el-col {
      padding: 10px;
    }

    .box {
      padding: 10px;
      padding-left: 50px;
      border-bottom: 1px solid rgba(230, 230, 230, 0.6);

      .title {
        // font-size: 16px;
        color: #333;
        margin-bottom: 10px;
      }
    }

    .box1 {
      border-bottom: none;
    }

    .box-btn {
      padding: 12px;
    }

    .number {
      // font-size: 18px;
    }
  }
}

.user-info {
  padding: 20px 0;

  .el-row {
    margin-bottom: 15px;
    line-height: 30px;
    height: 30px;
  }

  .name {
    width: 96px;
    text-align: right;
    display: inline-block;
    color: #6e6e6e;
  }

  .info {
    font-size: 16px;
  }

  .btn-statue {
    margin-left: 100px;
    margin-bottom: 20px;
  }
}

.progress-box {
  position: relative;
  margin-bottom: 20px;

  .item {
    position: absolute;
    width: 46%;
    height: 30px;
    top: 38%;
    left: 27%;
    background: #fff;
  }

  .progress-title {
    font-size: 16px;
    padding: 0 12px;
    margin-top: 10px;
  }

  /deep/ .el-progress {
    .el-progress-bar__outer {
      background-color: #ff9800;
    }

    .el-progress-bar__inner {
      background-color: #ff5722;
    }

    .el-progress-bar__innerText {
      color: #ff5722;
      font-size: 0;
    }
  }
}

.el-tabs {
  margin-top: 30px;
}

.force-line {
  // margin-top: 30px;
  // background-color: #fdf6ec;
  color: #e6a23c;

  p {
    padding: 8px 16px;
  }

  .number {
    font-size: 18px;
  }
}

.box-btn {
  margin-top: 20px;

  .el-button {
    padding-left: 50px;
    padding-right: 50px;
  }
}

.Assets-box {
  // border: 1px solid #f1f1f1;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 10px 0;
  margin: 0 !important;

  .box {
    position: relative;

    .iconfont {
      // position: absolute;
      // margin-right: 5px;
      font-size: 30px;
      // top: 17px;

      &.color1 {
        color: #2f97fc;
      }

      &.color2 {
        color: #17b780;
      }

      &.color3 {
        color: #ff7602;
      }

      &.color3 {
        color: #fd4256;
      }

      &.color4 {
        color: #fda822;
      }
    }
  }
}

.account-all {
  margin-top: 20px;

  .title {
    font-size: 16px;
    line-height: 30px;

    span {
      font-size: 12px;
      color: #777;
    }
  }

  .number {
    font-size: 22px;
    margin-top: 4px;
    text-shadow: 1px 2px 2px rgba(24, 24, 24, 0.3);
  }
}
.code {
  font-size: 12px;
  color: #fff;
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
