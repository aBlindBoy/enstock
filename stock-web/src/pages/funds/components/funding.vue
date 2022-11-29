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
        <div class="con-box">
          <div class="header-top">
            <div class="text item">
                  <el-tabs>
                    <el-tab-pane label="Funding details" name="first">
                    </el-tab-pane>
                  </el-tabs>
                </div>

                <table style="width: 100%;border-spacing:160px 180px;" class="demo-form-inline auth-box daystable">
                  <tr>
                    <td class="tr-cols1">Order Number</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.orderNumber}}</span>
                    </td>
                    <td class="tr-cols3">State</td>
                    <td>
                      <span v-if="accountinfo.status==0" class="red td-span">Pending review</span>
                      <span v-if="accountinfo.status==1" class="green td-span">In operation</span>
                      <span v-if="accountinfo.status==2" class="green td-span">Did not pass</span>
                      <span v-if="accountinfo.status==3" class="green td-span">Past due</span>
                      <span v-if="accountinfo.status==4" class="green td-span">Over</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Trader Starting time</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.beginTime | timeFormat}}</span>
                    </td>
                    <td class="tr-cols3">Trading end time</td>
                    <td>
                      <span class="td-span">{{accountinfo.endTime | timeFormat}}</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Total trading capital</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.totalTradingAmount}}USD</span>
                    </td>
                    <td class="tr-cols3">Warning line</td>
                    <td>
                      <span class="td-span">{{accountinfo.lineWarning}}USD</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Allocating funds</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.fundsAmount}}USD</span>
                    </td>
                    <td class="tr-cols3">Closing Line</td>
                    <td>
                      <span class="td-span">{{accountinfo.lineUnwind}}USD</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Margin</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.margin}}USD</span>
                    </td>
                    <td class="tr-cols3">sub account</td>
                    <td>
                      <span class="td-span">{{accountinfo.subaccountNumber}}</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Funding management fee</td>
                    <td class="tr-cols2">
                      <span class="td-font">{{accountinfo.manageFee}}USD</span>
                    </td>
                    <td class="tr-cols3"></td>
                    <td>
                      <span class="td-span"></span>
                    </td>
                  </tr>
                  <tr bgcolor="#fdf6ec" style="height:60px;text-align:right;">
                    <td style="padding-right:40px;"  colspan="4">
                      <div slot="footer" class="dialog-footer" v-if="accountinfo.status != 4 && accountinfo.status != 2">
                        <el-button type="primary" @click="look(accountinfo.subaccountNumber)">View live trades</el-button>
                        <span class="spanpadding" @click="expandDialogVisible = true">Expand allocation</span>
                        <span class="spanpadding" @click="marginDialogVisible = true">Append Margin</span>
                        <span class="spanpadding" @click="delayDialogVisible = true">Apply for an extension</span>
                        <span class="spanpadding" @click="endDialogVisible = true">Terminate the trader</span>
                        <span class="spanpadding" @click="contract()">View contract</span>
                      </div>
                    </td>
                    
                  </tr>
                </table>

                <div class="auth-box" style="padding-bottom:40px;">
                  
                </div>

          </div>

          <el-tabs v-model="activeName" @tab-click="handleClick" v-if="accountinfo.status != 4 && accountinfo.status != 2">
            <el-tab-pane label="Expand the allocation record" name="first">
              <div class="user-info">
                <el-table
                  :data="list.list"
                  style="width: 100%">
                  <el-table-column
                    prop="id"
                    label="Numbering">
                  </el-table-column>
                  <el-table-column
                    prop="appendMargin"
                    label="Application Margin">
                  </el-table-column>
                  <el-table-column
                    prop="appendServiceFee"
                    label="Application fee">
                  </el-table-column>
                  <el-table-column
                    prop="totalTradingAmount"
                    label="Trading funds (USD)">
                  </el-table-column>
                  <el-table-column
                    prop="addTime"
                    width="180px"
                    label="application time">
                    <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
                  </el-table-column>
                  <el-table-column
                    prop="appendCycle"
                    label="Period of use">
                    <template slot-scope="scope">
                      <span>{{scope.row.tradersCycle}}day</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="status"
                    label="application status">
                    <template slot-scope="scope">
                      <p class="bounceIn">
                        <span v-if="scope.row.status==0" class="red">Under review</span>
                        <span v-if="scope.row.status==1" class="green">Passed</span>
                        <span v-if="scope.row.status==2" class="red">Did not pass</span>
                      </p>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane label="Append Margin record" name="two">
              <div class="user-info">
                <el-table
                  :data="list.list"
                  style="width: 100%">
                  <el-table-column
                    prop="id"
                    label="Numbering">
                  </el-table-column>
                  <el-table-column
                    prop="appendMargin"
                    label="Append Margin">
                  </el-table-column>
                  <el-table-column
                    prop="addTime"
                    label="Submission date">
                    <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
                  </el-table-column>
                  <el-table-column
                    prop="status"
                    label="application status">
                    <template slot-scope="scope">
                      <p class="bounceIn">
                        <span v-if="scope.row.status==0" class="red">Under review</span>
                        <span v-if="scope.row.status==1" class="green">passed</span>
                        <span v-if="scope.row.status==2" class="red">Did not pass</span>
                      </p>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane label="Application for extension of record" name="three">
              <div class="user-info">
                <el-table
                  :data="list.list"
                  style="width: 100%">
                  <el-table-column
                    prop="id"
                    label="Numbering">
                  </el-table-column>
                  <el-table-column
                    prop="appendCycle"
                    label="Extension period">
                    <template slot-scope="scope">
                      <span>{{scope.row.appendCycle}}day</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="appendServiceFee"
                    label="Extension fee">
                  </el-table-column>
                  <el-table-column
                    prop="addTime"
                    label="Submission date">
                    <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
                  </el-table-column>
                  <el-table-column
                    prop="status"
                    label="Application status">
                    <template slot-scope="scope">
                      <p class="bounceIn">
                        <span v-if="scope.row.status==0" class="red">Under review</span>
                        <span v-if="scope.row.status==1" class="green">passed</span>
                        <span v-if="scope.row.status==2" class="red">Did not pass</span>
                      </p>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>

        </div>

        <!-- 擴大配資彈框 -->
        <el-dialog
          title="Expand allocation"
          class="agree-dialog"
          :center='true'
          :visible.sync="expandDialogVisible"
          width="35%">
          <div class="dialog-iframe">
            <div class="content finance-operate-page" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
                <div class="info">
                 1. Time period for expanding funding: 09:00:00 -14:45:00；<br>
                 2. The handling fee for expanding the allocation needs to be paid in one lump sum;
                </div>
                <div class="account">
                    <span class="label">Current account balance</span>
                    <span class="con">{{userInfo.enableAmt}} USD</span>
                </div>
                <div class="account">
                    <span class="label">Funding management fee</span>
                    <span class="con"><span id="fee">{{manageFee}}</span> USD</span>
                </div>
                <div class="account">
                    <span class="label">Expand the amount</span>
                    <span class="con">
                      <el-input type='number' class="td-input" v-model="appendMargin" placeholder="Please enter the amount"></el-input>USD
                    </span>
                </div>
            </div>
            <div slot="footer" class="text-center dialog-footer clearfix">
              <el-button type="error" @click="expandDialogVisible = false">Cancel expansion</el-button>
              <el-button type="primary" @click="expandApply()">Confirm expansion</el-button>
            </div>
          </div>
        </el-dialog>

        <!-- 追加Margin彈框 -->
        <el-dialog
          title="追加Margin"
          class="agree-dialog"
          :center='true'
          :visible.sync="marginDialogVisible"
          width="35%">
          <div class="dialog-iframe">
            <div class="content finance-operate-page" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
                <div class="info">
                  1. The system will only allow the operation to add Margin when the investor's position of the asset has a loss and is lower than the warning line of the asset allocation;<br>
                  2. There is a minimum and lower limit for the additional margin amount. When it is lower than the Warning line, the investor is prohibited from opening positions; when it is higher than the Warning line, the system automatically lifts the prohibition of opening positions, allowing normal positions to be opened; <br>
                  3. There is no upper limit on the amount of additional margin; <br>
                  4. Additional Margin does not have the effect of enlarging the funds. If you want to enlarge the funds, please apply for the expansion of the funds.
                </div>
                <div class="account">
                    <span class="label">Current account balance</span>
                    <span class="con">{{userInfo.enableAmt}} USD</span>
                </div>
                <div class="account">
                    <span class="label">Additional amount</span>
                    <span class="con">
                      <el-input type='number' class="td-input" v-model="appendMargin" placeholder="Please enter the amount"></el-input>USD
                    </span>
                </div>
            </div>
            <div slot="footer" class="text-center dialog-footer clearfix">
              <el-button type="error" @click="marginDialogVisible = false">Cancel append</el-button>
              <el-button type="primary" @click="marginApply()">Confirm to add</el-button>
            </div>
          </div>
        </el-dialog>

        <!-- 申請延期彈框 -->
        <el-dialog
          title="Apply for an extension"
          class="agree-dialog"
          :center='true'
          :visible.sync="delayDialogVisible"
          width="35%">
          <div class="dialog-iframe">
            <div class="content finance-operate-page" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
                <div class="info">
                  1. The handling fee generated by the renewal application needs to be paid in one lump sum;<br>
                  2. For renewal, you need to repay the interest owed before, and then recalculate according to the interest rate set by the current platform. The formula for charging the fee is the same as when applying for allocation.
                </div>
                <div class="account">
                    <span class="label">Current account balance</span>
                    <span class="con">{{userInfo.enableAmt}} USD</span>
                </div>
                <div class="account">
                    <span class="label">Funding management fee</span>
                    <span class="con"><span id="fee">{{manageFee}}</span> USD</span>
                </div>
                <div class="account">
                    <span class="label">Renewal time</span>
                    <span class="con">
                      <el-select clearable filterable v-model="selDaysUsePeriod" placeholder="Renewal time"  @change="currentSelDays" style="width:120px;">
                        <el-option v-for="item in (fundsSetting.daysUsePeriod || '').split('|')" :key="item" :value="item">{{item}}day</el-option>
                      </el-select>
                    </span>
                </div>
            </div>
            <div slot="footer" class="text-center dialog-footer clearfix">
              <el-button type="error" @click="delayDialogVisible = false">Cancel extension</el-button>
              <el-button type="primary" @click="delayApply()">Confirm extension</el-button>
            </div>
          </div>
        </el-dialog>

        <!-- 終止操盤彈框 -->
        <el-dialog
          title="Terminate the trader"
          class="agree-dialog"
          :center='true'
          :visible.sync="endDialogVisible"
          width="35%">
          <div class="dialog-iframe">
            <div class="content finance-operate-page" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
              <div class="info">
                Are you sure you want to apply for termination of trading? <br> <br>

                1. Trading on a daily/weekly basis, the deducted handling fee will not be returned；<br>
                2. Interest-free trading, without deduction of any fees； <br>
                3. Monthly trading will deduct 20% of the remaining interest not deducted as a penalty； <br>
                4. Please make sure that your trading account has been fully cleared, otherwise we will have the right to close your positions (the closing price is not guaranteed)。
              </div>
                
            </div>
            <div slot="footer" class="text-center dialog-footer clearfix">
              <el-button type="error" @click="endDialogVisible = false">Cancel termination</el-button>
              <el-button type="primary" @click="endApply()">Confirm termination</el-button>
            </div>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../components/HeaderOrder'
  import HomeFooter from '../../../components/Footer'
  import MenuBox from '@/pages/user/components/menu'
  import * as api from '../../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter,
      MenuBox
    },
    props: {
      list: {
        type: Object,
        default () {
          return {
            list: []
          }
        }
      },
      getData: {
        type: Function,
        default: function () {}
      },
      handleOptions: {
        type: Function,
        default: function () {}
      },
      refresh: {
        type: Boolean,
        default: function () {}
      }
    },
    data () {
      return {
        loading: false,
        activeName: 'first',
        settingInfo: {}, // 设置信息
        indexSettingInfo: {},
        futuresSettingInfo: {},
        userInfo: {},
        accountActiveNames: ['1'],
        pageNum: 1,
        pageSize: 8,
        accountinfo: {},
        delayDialogVisible: false,
        expandDialogVisible: false,
        marginDialogVisible: false,
        endDialogVisible: false,
        fundsSetting: {},
        selDaysUsePeriod: '',
        selDaysUseVal: '',
        manageFee: 0,
        rateInfo: {},
        appendMargin: ''
      }
    },
    watch: {},
    computed: {
      progressNnum () {
        if (this.$store.state.userInfo.userAmt !== 0) {
          return (this.$store.state.userInfo.enableAmt / this.$store.state.userInfo.userAmt) * 100
        } else {
          return 0
        }
      }
    },
    created () {
      this.getUserInfo()
      this.getSubaccountInfo()
      this.getFundsSetting()
      this.getlist(1)
    },
    mounted () {
      this.$store.state.userMenu = '2-23'
    },
    methods: {
      contract() {
        // let routeData = this.$router.push('/contract')
        let routeData = this.$router.resolve({ path: '/contract', query: {} });
        window.open(routeData.href, '_blank');
      },
      handleClick(tab, event) {
        // console.log(tab, event);
        if(tab.name == 'two'){
        	this.getlist(2);
        } else if(tab.name == 'three'){
        	this.getlist(3);
        } else {
        	this.getlist(1);
        }
      },
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判断是否登录
          this.$store.state.userInfo = data.data
          this.userInfo = data.data
        } else {
        }
      },
      async getSubaccountInfo () {
        let data = await api.getSubaccountInfo({id: this.$route.query.id})
        if (data.status === 0) {
          // 成功
          this.accountinfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      look (val) {
        this.$router.push('/transaction?sub='+ val)
      },
      handleSizeChange (size) {
        this.handleOptions({ pageSize: size })
        this.getData()
      },
      handleCurrentChange (page) {
        this.handleOptions({ pageNum: page })
        this.getData()
      },
      async delayApply() {
        if(this.selDaysUsePeriod == ''){
          this.$message.error('Please select an additional period')
          return
        }
        let opt = {
          id: 0,
          appendCycle: this.selDaysUseVal,
          applyId: this.$route.query.id,
          fundsType: 1,
          appendType: 3,
          appendServiceFee: this.manageFee
        }
        let data = await api.appendApply(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.delayDialogVisible = false
          this.selDaysUsePeriod = ''
          this.getlist(3)
        } else {
          this.$message.error(data.msg)
        }
      },
      async expandApply() {
        if(this.appendMargin == ''){
          this.$message.error('Please fill in the expansion amount')
          return
        }
        let opt = {
          id: 0,
          applyId: this.$route.query.id,
          fundsType: 1,
          appendType: 1,
          appendMargin: this.appendMargin
        }
        let data = await api.appendApply(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.expandDialogVisible = false
          this.appendMargin = ''
          this.getlist(1)
        } else {
          this.$message.error(data.msg)
        }
      },
      async marginApply() {
        if(this.appendMargin == ''){
          this.$message.error('Please fill in the additional amount')
          return
        }
        let opt = {
          id: 0,
          applyId: this.$route.query.id,
          fundsType: 1,
          appendType: 2,
          appendMargin: this.appendMargin
        }
        let data = await api.appendApply(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.marginDialogVisible = false
          this.appendMargin = ''
          this.getlist(2)
        } else {
          this.$message.error(data.msg)
        }
      },
      async endApply() {
        let opt = {
          id: 0,
          applyId: this.$route.query.id,
          fundsType: 1,
          appendType: 4,
          appendServiceFee: 0
        }
        let data = await api.appendApply(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.endDialogVisible = false
        } else {
          this.$message.error(data.msg)
        }
      },
      async getlist (appendType) {
        // 获取持仓列表
        let opt = {
          userId: this.$store.state.userInfo.id, 
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          appendType: appendType
        }
        let data = await api.getAppendList(opt)
        if (data.status === 0) {
          this.list = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      async getFundsSetting () {
        // 分仓配资设置信息查询
        let data = await api.getFundsSetting()
        if (data.status === 0) {
          this.fundsSetting = data.data
        } else {
        }
      },
      async getLeverRateInfo (cycleType, lever) {
        let data = await api.getLeverRateInfo({cycleType: cycleType, lever: lever})
        if (data.status === 0) {
          // 成功
          this.rateInfo = data.data
          // 管理费= 配资金额 * 杠杆费率 * 天数
          this.manageFee = Number(this.accountinfo.fundsAmount * this.rateInfo.manageRate / 100 * this.selDaysUseVal).toFixed(1)
        } else {
          this.$message.error(data.msg)
        }
      },
      currentSelDays(selVal) {
        this.selDaysUsePeriod = selVal + 'day'
        this.selDaysUseVal = selVal
        this.getLeverRateInfo(1, this.accountinfo.lever)
      }
    }
  }
</script>
<style lang="less" scoped>
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
        font-size: 16px;
      }

      .account {
        font-size: 46px;
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
          font-size: 16px;
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
        font-size: 18px;
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
        background-color: #FF9800;
      }

      .el-progress-bar__inner {
        background-color: #FF5722;
      }

      .el-progress-bar__innerText {
        color: #FF5722;
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
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    padding: 10px 0;
    margin: 0 !important;

    .box {
      position: relative;

      .iconfont {
        position: absolute;
        left: 10px;
        font-size: 30px;
        top: 17px;

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

  
  .td-font {
    font-size: 12px;
    margin-left: 0px;
    color: #606266;
  }

  .td-span {
    font-size: 12px;
    margin-left: 0px;
    color: #b13b51;
  }

  .tr-cols1 {
    width: 130px;
  }
  .tr-cols2 {
    width: 280px;
  }
  .tr-cols3 {
    width: 130px;
    padding-left: 15px;
  }
  .td-input {
    width: 120px;
  }
  .daystable{
    border-spacing:0px 0px;
  }
  table td{ height:50px; border:#CCCCCC 0px solid;}
  .spanpadding{
    padding:10px;cursor:pointer;
  }

  //弹框样式
  .finance-operate-page .info {
    color: #ff465a;
    font-size: 14px;
    line-height: 25px;
    margin-bottom: 10px;
}

  .finance-operate-page .fee, .finance-operate-page .operate, .finance-operate-page .account {
    border-bottom: 1px dotted #D9D9D9;
    background-color: #F9F9F9;
    padding: 10px;
    line-height: 30px;
}

  .finance-operate-page .account .label {
    float: left;
    width: 180px;
    text-align: left;
}
  .finance-operate-page .fee .con, .finance-operate-page .operate .con, .finance-operate-page .account .con {
    display: block;
    text-align: right;
}
  .con {
    display: block;
    text-align: right;
}

</style>
