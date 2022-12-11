<template>
  <el-container>
    <el-header>
      <HomeHeader></HomeHeader>
    </el-header>
    <el-container class="main-wrapper">
      <el-main class="transform-main " style="overflow: hidden;">
        <div
          style="margin-bottom: 2px;display: flex;background-color: #000;justify-content: space-between;"
        >
          <div
            class="tab-bg"
            style="display: flex;justify-content: space-between; height: 40px;"
          >
            <div style="display: flex;justify-content: space-between;">
              <div class style="cursor: pointer; display: flex;">
                <div
                  class="market transaction-red-bg hangqing"
                  @click="
                    optTablebox(
                      {
                        name: '',
                        type: 'first'
                      },
                      0,
                      '1'
                    )
                  "
                >
                  <span
                    class="iconfont icon-hangqing transaction-hangqing"
                  ></span>
                  <span style="font-size: 14px;">{{$t('tradingFloor.quotes')}}</span>
                </div>
                <div
                  class="optional optional-item-hover"
                  v-if="$store.state.haslogin"
                  :class="currIndex == 'second' ? 'currIndex' : ''"
                  @click="optTablebox('', '', '1', 'zi')"
                >
                <span
                    class="iconfont icon-hangqing transaction-hangqing"
                  ></span>
                  >{{$t('tradingFloor.optional')}}
                </div>
              </div>
            </div>
          </div>
        </div>

        <el-row :gutter="3">
        
        
          <el-col :span="5" v-if="cutIndex == 1">
            <!--  ==> 监听融资下单 -->
            <table-box
              ref="tableBox"
              @toTransaction="toTransaction"
              :hasGetNewOrder="hasGetNewOrder2"
              :handleOptions2="handleOptionsindex2"
              :handleOptions3="handleOptions3"
              :handleOptions4="handleOptions4"
            ></table-box>
          </el-col>
          <el-col class="alterWidthCenter" :span="14" v-if="cutIndex == 1" style="height: calc(100vh - 103px);">
            <ChartNew
              :detail="detail"
              :code="code"
              :ucode="ucode"
            ></ChartNew>

            <!-- 持仓单子 -->

            <div class="tab-box jiaoyi-000" style="height: calc(100vh);margin-top:5px">
              <div class="trade">
                <el-tabs
                v-model="activeName"
                class="black-style"
               
              >
              <!--  v-if="$store.state.haslogin" -->
                <el-tab-pane label="In/Out" name="zero">
                  <BuyBox1
                    @selectDetailsItem="selectDetailsItem"
                    :cutIndex="cutIndex"
                    :detailsCont="detailsCont"
                    :hasGetNewOrder="hasGetNewOrder"
                    :handleOptions2="handleOptions2"
                    :settingInfo="settingInfo"
                    :code="code"
                  ></BuyBox1>
                </el-tab-pane>
                <el-tab-pane label="Financing position" name="first">
                  <!-- 我的持仓 -->
                  <hold-position
                    :haslogin="haslogin"
                    :hasGetNewOrder="hasGetNewOrder"
                    :handleOptions="handleOptions"
                  ></hold-position>
                </el-tab-pane>

                <el-tab-pane label="Financing to close the position" name="second">
                  <sell-box
                    :hasChangeSell="hasChangeSell"
                    :handleOptions="handleOptions"
                  ></sell-box>
                </el-tab-pane>
               <!-- 我的持仓 指數 -->
                <!-- <el-tab-pane
                  label="Index positions"
                  v-if="$store.state.productSetting.indexDisplay"
                  name="three"
                >
                
                  <index-hold-position
                    :haslogin="haslogin"
                    :hasGetNewOrder="hasGetNewOrder2"
                    :handleOptions="handleOptionsindex"
                  ></index-hold-position>
                </el-tab-pane>
                <el-tab-pane
                  label="Index close"
                  v-if="$store.state.productSetting.indexDisplay"
                  name="fours"
                >
                  <index-sell-box
                    :hasChangeSell="hasChangeSell2"
                    :handleOptions="handleOptionsindex"
                  ></index-sell-box>
                </el-tab-pane> -->
                  <!-- 我的持仓 期貨 -->
                <!-- <el-tab-pane
                  label="futures position"
                  v-if="$store.state.productSetting.futuresDisplay"
                  name="five"
                >
                
                  <futures-hold-position
                    :haslogin="haslogin"
                    :hasGetNewOrder="hasChangeSell3"
                    :handleOptions="handleOptionsFutures"
                  ></futures-hold-position>
                </el-tab-pane> -->
                <!-- <el-tab-pane
                  label="futures close"
                  v-if="$store.state.productSetting.futuresDisplay"
                  name="six"
                >
                  <futures-sell-box
                    :hasChangeSell="hasChangeSell3"
                    :handleOptions="handleOptionsFutures"
                  ></futures-sell-box>
                </el-tab-pane> -->
<!-- 
                <el-tab-pane
                  label="Funding positions"
                  name="seven"
                  v-if="$store.state.productSetting.fundsDisplay"
                >
                  <funds-hold-position
                    :haslogin="haslogin"
                    :hasGetNewOrder="hasChangeSell4"
                    :handleOptions="handleOptionsFunds"
                  ></funds-hold-position>
                </el-tab-pane> -->
                <!-- <el-tab-pane
                  label="Allotment and liquidation"
                  name="eight"
                  v-if="$store.state.productSetting.fundsDisplay"
                >
                  <funds-sell-box
                    :hasChangeSell="hasChangeSell4"
                    :handleOptions="handleOptionsFunds"
                  ></funds-sell-box>
                </el-tab-pane> -->
              </el-tabs>
              </div>
          
          
              <div v-show="!$store.state.haslogin" class="empty text-center">
							  <div class="btn-wrap">
							    <el-button
							      class="btn-box"
							      type="primary"
							      style="background-color: rgba(0,0,0,0)!important;"
							      @click="toLogin"
							    >{{$t('common.login')}}</el-button>
							    <span>{{$t('tradingFloor.or')}}</span>
							    <el-button
							      class="btn-box"
							      type="primary"
							      style="background-color: rgba(0,0,0,0)!important;"
							      @click="toRegister"
							    >{{$t('common.register')}}</el-button>
							  </div>
							</div>
            </div>
          </el-col>
          <el-col class="alterWidthLeft" :span="5" v-if="cutIndex == 1">
            <Company
              :code="code"
            ></Company>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import HomeHeader from "@/components/HeaderOrder";
import ChartNew from "./components/chart2";
import TableBox from "./components/table";
import HoldPosition from "./components/holdposition";
// import IndexHoldPosition from "./components/indexholdposition";
// import FuturesHoldPosition from "./components/futuresholdposition";
// import FundsHoldPosition from "./components/fundsholdposition";
import SellBox from "./components/sell";
// import IndexSellBox from "./components/indexsell";
// import FuturesSellBox from "./components/futuressell";
// import FundsSellBox from "./components/fundssell";
import Company from "./components/company";
import BuyBox1 from "./components/buy1";

import * as api from "@/axios/api";

import {  format } from 'date-fns'

// import { setLocalstorage, getLocalstorage } from '@/utils/localstorage'

export default {
  components: {
    HomeHeader,
    // ChartBox,
    ChartNew,
    TableBox,
    HoldPosition,
    Company,
    // IndexHoldPosition,
    // IndexSellBox,
    SellBox,
    // FuturesHoldPosition,
    // FundsHoldPosition,
    // FuturesSellBox,
    // FundsSellBox,
    BuyBox1
  },
  props: {},
  data() {
    return {
      time1: "time1",
      views1: "views1",
      pageNum: 1,
      cutIndex: 1,
      detailsCont: {},
      searchTran: "",
      newsList: [],
      optionalIndex: -1,
      currIndex: 0,
      direct: [
   
      ],
      // 行情登錄
      code: "",
      ucode: "",
      activeName: "zero",
      haslogin: false,
      detail: {},
      hasChangeSell: 0, // 是否平仓 (融资) 平仓之后数字一直加
      hasChangeSell2: 0, // 是否平仓 (指数) 平仓之后数字一直加
      hasChangeSell3: 0, // 是否平仓 (期货) 平仓之后数字一直加
      hasChangeSell4: 0, // 是否平仓 (配资) 平仓之后数字一直加
      hasGetNewOrder: 0, // 是否下单(融资)  下单数字++ 使用true/false 第二次为true的时候 页面监听不到
      hasGetNewOrder2: 0, // 是否下单(指数)
      hasGetNewOrder3: 0, // 是否下单(期货)
      hasGetNewOrder4: 0, // 是否下单(配资)
      settingInfo: {},
      isChartOld: false,
      windowWidth: document.documentElement.clientWidth, //实时屏幕宽度
      windowHeight: document.documentElement.clientHeight - 160, //实时屏幕高度
      windowHeight1: document.documentElement.clientHeight - 200 //实时屏幕高度
    };
  },
  watch: {
    change(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.getDetail(); // 分时数据
      }
    }
  },
  computed: {
    change() {
      return this.$route.query.code;
    }
  },
  beforeRouteEnter(to, from, next) {
    // 现在想不做判断，进入之前一律刷新一次
    if (!to.query.code) {
      let query = to.query;
      query.code = "6414";
      next({
        path: to.path,
        query: query
      });
    }
    next();
  },
  beforeRouteUpdate(to, from, next) {
    // 现在想不做判断，进入之前一律刷新一次
    if (!to.query.code) {
      let query = to.query;
      query.code = "6414";
      next({
        path: to.path,
        query: query
      });
    }
    next();
  },
  beforeCreate() {},
  created() {
    setInterval(() => {
      // this.form.pageSize = Math.ceil(this.windowHeight / 63)
      // this.form2.pageSize = Math.ceil(this.windowHeight / 63)
      this.windowHeight = document.documentElement.clientHeight - 160; //实时屏幕高度
      this.windowHeight1 = document.documentElement.clientHeight - 100; //实时屏幕高度
    }, 500);
    // 暂时排错
    // let data = {
    //   'indexDisplay': true
    // }
    // this.$store.state.productSetting = data

    this.code = this.$route.query.code;
    this.ucode = this.$route.query.code;

    if (this.code.indexOf("hf_") != -1) {
      this.isChartOld = true;
    } else {
      this.isChartOld = false;
    }

    if(this.$store.state.haslogin){
      
      this.getUserInfo();
    }
    this.$store.state.activeIndex = "transaction";
    this.getSettingInfo();
    if (!this.$store.state.productSetting) {
      this.getProductSetting();
    }
  },
  mounted() {
    window.activeName1 = "first";
   
    // this.getNewsList();
  },
  methods: {
    qidai() {
      this.$message("Stay tuned");
    },
   
    toUserCenter() {
      // 進入個人中心
      this.$router.push("/user");
    },
    async handleCommand(val) {
      if (val === "a") {
        // 修改密碼
      } else if (val === "c") {
        this.$store.state.loginIsShow = true;
      } else if (val === "d") {
        this.$router.push("/user");
      } else {
        // 退出登錄
        this.clearCookie();
        let data = await api.logout();
        if (data.status === 0) {
          // 退出登錄清除本地存儲 清除用戶數據
          window.localStorage.clear();
          this.$store.state.haslogin = false;
          this.$message.success("you are logged out");
          this.$store.state.userInfo = {};
          this.$router.push("/login");
        } else {
          this.$message.error(data.msg);
        }
      }
    },
    toLogin1() {
      this.$router.push("/login");
    },
    // 选择色系
    selectColor(command) {
      localStorage.setItem("styleName", command);
      this.$store.commit("setSystemColor", command);
      document.getElementsByTagName("body")[0].className = command;
      window.drawLine && window.drawLine();
    },
    toIndex() {
      this.$router.push({
        path: "/"
      });
    },
    async toTransaction(row) {
      var ziCode = this.$route.query;
      // 出仓数据
      var data = await api.findUserPositionByCode({
        stockCode: row.code || ziCode.code
      });
      console.log(row, "222222222222222");
      if (data.status == 0) {
        this.$store.commit("setUserPositionData", data.data.list[0]);
      }
    },
    async toStock() {
      //加载跟多新闻列表
      var page = ++this.pageNum;
      var query = {
        pageNum: page,
        pageSize: 15,
        type: this.newType
      };
      var list = await this.getNewsList(query);
      console.log(list);
      if (list.length <= 0) {
        this.$message({
          message: "no more data",
          type: "warning"
        });
        return;
      }
      this.newsList.push(...list);
    },
    optTablebox(item, index, type, zi) {
      // 行情tab选项
      this.optionalIndex = -1;
      if (zi) {
        this.cutIndex = 1;
        console.log(zi);
        this.currIndex = "second";
        setTimeout(() => {
          this.$refs.tableBox.activeName = "second";
        }, 500);
        window.activeName1 = "zi";
        return;
      }
      this.currIndex = index;
      this.cutIndex = type;
      window.activeName1 = item.type;
      this.$refs.tableBox.activeName = item.type;
      // if(item.type == 'start'){
      // 	this.$refs.tableBox.getlistStart()
      // }
    },
 
    // 时间转换
    switchData(list, time) {
      list.forEach(item => {
        var tempStr = item[time] + "";
        var timestamp = tempStr.slice(0, tempStr.length - 3);
        var newDate = new Date();
        newDate.setTime(timestamp * 1000);
        item[time] = newDate.toLocaleDateString();
      });
    },
   
    handleOptions(opts) {
      // 监听平仓状态 融资
      this.hasChangeSell = opts;
      if (this.hasChangeSell) {
        this.activeName = "second";
      }
    },
    handleOptionsindex(opts) {
      // 监听平仓状态 指数
      this.hasChangeSell2 = opts;
      if (this.hasChangeSell2) {
        this.activeName = "fours";
      }
    },
    handleOptionsFutures(opts) {
      // 监听平仓状态 期货
      this.hasChangeSell3 = opts;
      if (this.hasChangeSell3) {
        this.activeName = "six";
      }
    },
    handleOptionsFunds(opts) {
      // 监听平仓状态 配资
      this.hasChangeSell4 = opts;
      if (this.hasChangeSell4) {
        this.activeName = "eight";
      }
    },
    handleOptions2(opts) {
      // 监听指数下单状态
      this.hasGetNewOrder = opts;
      if (this.hasGetNewOrder) {
        this.activeName = "first";
      }
    },
    handleOptions3(opts) {
      // 监听期货下单状态
      this.hasGetNewOrder3 = opts;
      if (this.hasGetNewOrder3) {
        this.activeName = "five";
      }
    },
    handleOptions4(opts) {
      // 监听配资下单状态
      this.hasGetNewOrder4 = opts;
      if (this.hasGetNewOrder4) {
        this.activeName = "seven";
      }
    },
    handleOptionsindex2(opts) {
      // 监听下单状态
      this.hasGetNewOrder2 = opts;
      if (this.hasGetNewOrder2) {
        this.activeName = "three";
      }
    },
    async getDetail() {
      if (this.$route.query.code.indexOf("hf_") != -1) {
        this.isChartOld = true;
      } else {
        this.isChartOld = false;
      }
      let opts = {
        code: this.$route.query.code
      };
      this.loading = true;
      let res1 = await api.getUsStockData(opts.code)
      let stock = res1.data[0];
      let stockDetail ={}
      stockDetail.name = stock["200024"];//股票名稱
      stockDetail.date = stock["200007"];//最近交易日期
      // stockDetail.time = stock[""];//最近成交時刻
      stockDetail.price = stock["6"];//最新價格
      stockDetail.rate = stock["11"];//漲跌
      stockDetail.hcrate = stock["56"];//漲跌幅
      stockDetail.high = stock["12"];//最高價
      stockDetail.low = stock["13"];//最低價
      stockDetail.volumn = stock["800001"];//累積成交量
      stockDetail.amount = stock[""];//成交金額
      stockDetail.yes = stock["21"];//昨收
      stockDetail.open = stock["19"];//開盤價
      
      if (stockDetail.rate > 0) {
        stockDetail.color = "upColor";
      }
      if (stockDetail.rate < 0) {
        stockDetail.color = "lowColor";
      }
      this.ucode = opts.code;
      
      this.code = opts.code;
      this.loading = false;
      this.detail = stockDetail;
    },
    toRegister() {
      // 注册
      this.$router.push("/register");
    },
    toLogin() {
      // 登录
      // this.$store.state.loginIsShow = true;
      this.$router.push("/login");
    },
    async getList() {
      // 获取表格数据
      let opt = {
        pageNum: 1,
        pageSize: 1
      };
      let data = await api.getStock(opt);
      if (data.status === 0) {
        // data.data.list.forEach(element => {
        //     this.list.push(element)
        // });
        this.list = data.data.list[0];
        this.$router.push({
          path: "/transaction",
          query: {
            code: data.data.list[0].code
          }
        });
        // window.location.reload()
      } else {
        this.$message.error(data.msg);
      }
    },
    async getSettingInfo() {
      // 网站设置信息
      let data = await api.getSetting();
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data;
        // 杠杆倍数
        // this.form.lever = data.data.siteLever
      } else {
        this.$message.error(data.msg);
      }
    },
    async getProductSetting() {
      // 获取网站产品设置信息
      let result = await api.getProductSetting();
      if (result.status === 0) {
        this.$store.state.productSetting = result.data;
      } else {
        this.$message.error(result.msg);
      }
    },
    async getUserInfo() {
      // 获取用户信息
      let data = await api.getUserInfo();
      if (data.status === 0) {
        // 判断是否登录
        this.haslogin = true;
        this.$store.state.haslogin = true;
        this.$store.state.userInfo = data.data;
      } else {
        this.haslogin = false;
        this.$store.state.haslogin = false;
        // this.$message.error(data.msg)
      }
    },
    formatDate(date,fmt){
      return format(new Date(date),fmt)
    }
  }
};
</script>
<style lang="less" scoped>
.alterWidthLeft {
  width: 18.83333%;
}
.alterWidthCenter {
  width: 60.3336%;
}
.red-bg .el-dropdown {
  font-size: 14px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  width: 68px;
}

.black-bg .el-dropdown {
  color: #fff;
  font-size: 14px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  width: 68px;
}

/deep/ .tab-box .el-tabs__content {
  // height: 250px !important;
}

.header-btn-new {
  text-align: center;
  width: 45px;
  color: #fff;
  line-height: 30px;
}
.optional-item-hover {
  &:hover {
    background-color: #002346;
  }
}
.black-bg {
  .hei {
    background-color: #000;
  }
  .newsList-item {
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }

  .buy-xian {
    color: #3d3d41;
  }

  .el-main {
    margin: 0 !important;
    background: rgb(33, 39, 50);
  }

  .more {
    background: #a1a1a1;
    opacity: 0.8;
    border-radius: 3px;
    display: flex;
    justify-content: center;
    align-items: center;

    .more-btn {
      width: 96px;
      height: 12px;
      font-size: 12px;
      font-family: MicrosoftYaHeiLight;
      font-weight: 400;
      color: #ffffff;
    }

    height: 30px;
  }
}

.red-bg {
  .new-list-jiaoyi {
    margin-top: 4px;
    border: 1px solid #f44336;
  }

  .more {
    background: #000000;
    opacity: 0.8;
    border-radius: 3px;
    display: flex;
    justify-content: center;
    align-items: center;

    .more-btn {
      width: 96px;
      height: 12px;
      font-size: 12px;
      font-family: MicrosoftYaHeiLight;
      font-weight: 400;
      color: #ffffff;
    }

    height: 30px;
  }

  .shangxiajiantou {
    font-size: 12px;
    color: #ccc;
  }

  .redu,
  .date-icon {
    color: rgb(193, 24, 21);
  }

  .shangxiajiantou {
    font-size: 12px;
    color: #ccc;
  }

  .shous {
    border: 1px solid rgb(202, 202, 202);
    width: 80%;
    padding: 5px 5px 5px 20px;
  }

  .search-tran {
    position: absolute;
    right: 25px;
    top: 10px;
  }

  .listhi-cont {
    background-color: #fff;
  }

  .history-title {
    color: #000;
    line-height: 18px;
    font-weight: bolder !important;
  }

  .sanjiao {
    position: absolute;
    top: 4px;
    -webkit-transform: rotate(45deg);
    transform: rotate(-180deg);
    /* width: 0px; */
    right: 4px;
    font-size: 10px;
  }

  .red {
    color: #c11815;
  }

  .sanjiao.ccc {
    color: rgb(65, 65, 85);
  }

  .current-price {
    color: #c11815;
  }

  .quantity {
    display: inline-block;
    background-color: #c11815;
    text-align: center;
    // margin-left: 10px;
    margin-right: 10px;
    border-radius: 2px;
    line-height: 13px;
    color: #fff;
    padding: 3px 5px;
    height: 13px;
  }

  .bg-cc {
    background-color: rgb(65, 65, 85);
  }

  .listhi-cont::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 5px;
    /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }

  .listhi-cont::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 5px;
    box-shadow: inset 0 0 5px rgba(199, 14, 14, 0.2);
    background: rgb(193, 24, 21);
  }

  .listhi-cont::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    background: #ededed;
  }
}

.black-bg {
  .shangxiajiantou {
    font-size: 12px;
    color: #ccc;
  }

  .date-icon,
  .redu {
    color: rgb(71, 167, 196);
  }

  .shous {
    border: 1px solid rgb(35, 75, 110);
    background: no-repeat;
    width: 80%;
    padding: 5px 5px 5px 20px;
    color: #fff;
  }

  .search-tran {
    position: absolute;
    right: 25px;
    top: 10px;
  }

  .listhi-cont {
    color: #ececec;
  }

  .history-title {
    line-height: 18px;

    color: #bcbcbc;
  }

  .sanjiao {
    position: absolute;
    top: 4px;
    -webkit-transform: rotate(45deg);
    transform: rotate(-180deg);
    /* width: 0px; */
    right: 4px;
    font-size: 10px;
  }

  .red {
    color: #c11815;
  }

  .sanjiao.ccc {
    color: rgb(65, 65, 85);
  }

  .current-price {
    color: #c11815;
  }

  .quantity {
    display: inline-block;
    background-color: #c11815;
    text-align: center;
    // margin-left: 10px;
    margin-right: 10px;
    border-radius: 2px;
    line-height: 13px;
    color: #fff;
    padding: 3px 5px;
    height: 13px;
  }

  .bg-cc {
    background-color: rgb(65, 65, 85);
  }

  .listhi-cont::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 5px;
    /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }

  .listhi-cont::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 5px;
    box-shadow: inset 0 0 5px rgba(199, 14, 14, 0.2);
    background: #138eb4;
  }

  .listhi-cont::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    background: #ededed;
  }

  .xianjin {
    color: rgb(151, 150, 150);
  }
}

.currIndex {
  background: rgb(2, 77, 161);
}

.transform-main {
  min-width: 1200px;
  padding: 10px;
}

.tab-box /deep/ .el-tabs .el-tab-pane {
  padding: 0;
  // min-height: 300px;
}

.el-table td {
  padding: 5px 0;
}

.tab-box {
  position: relative;

  .trade{
    // height: calc(100vh);
    // position: absolute;
    background-color: #000!important;
  }
  .empty {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    z-index: 10;
    height: calc(100vh);
  }

  .btn-wrap {
    padding-top: 5%;
    color: #fff;
    height: calc(100vh);
    position: absolute;
    width: 100%;

    top: 0;
    z-index: 10;
    background-color:  rgba(0, 52, 77,0);
    align-items: center;
    z-index: 100;
    span {
      padding: 0 20px;
    }
  }

  .btn-box {
    background: none;
    border-color: #c11815;
    color: #c11815;
    font-weight: 600;
    font-size: 16px;
  }
}

.account-state {
  color: #ddd;
  position: absolute;
  top: 0;
  right: 0;
  line-height: 36px;

  span {
    margin-right: 20px;
  }
}
</style>
