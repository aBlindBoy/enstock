<template>
  <el-container class="user-center usercot days">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <el-aside width="178px">
        <menu-box></menu-box>
      </el-aside>
      <el-main>
        <div class="wrapper">
          <div class="box page-part transaction">
            <div class="box-contain clearfix">
         
              <el-card class="box-card" style="margin-top:0px;">
               
                <div class="text item">
                  <el-tabs>
                    <el-tab-pane label="Daily allocation" name="first">
                    </el-tab-pane>
                  </el-tabs>
                </div>

                <table style="width: 100%;border-spacing:160px 180px;" class="demo-form-inline auth-box daystable">
                  <tr>
                    <td class="tr-cols1">Warning line</td>
                    <td class="tr-cols2">{{Number(Number(form.margin) + (form.margin * fundsSetting.daysWarning)).toFixed(0)}} USD
                      <span class="td-font">（Warning line = Allocating funds + Margin X {{fundsSetting.daysWarning}}）</span>
                    </td>
                    <td class="tr-cols3 bgc" >Margin</td>
                    <td class="bgc">
                      <el-input type='number' @change="getIntNumber()" class="td-input" v-model="form.margin" placeholder="Please enter the amount"></el-input>
                      <span class="td-span">（Margin between {{fundsSetting.marginMin}} and {{fundsSetting.marginMax}}  ）</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Closing Line</td>
                    <td class="tr-cols2">{{Number(Number(form.margin) + (form.margin * fundsSetting.daysUnwind)).toFixed(0)}} USD
                      <span class="td-font">（Closing Line = Allocating funds + Margin X {{fundsSetting.daysUnwind}}）</span>
                    </td>
                    <td class="tr-cols3 bgc" >Allocating funds</td>
                    <td class="bgc">
                      <el-select clearable filterable v-model="selCycleType" placeholder="配置槓桿"  @change="currentSel" style="width:120px;" disabled>
                        <el-option v-for="item in fundsTypeList" :key="item.id" :value="item">{{item.lever}}</el-option>
                      </el-select>
                      <span class="td-span">（Allocating funds for{{fundsAmount}}USD）</span>multiple
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Management fee</td>
                    <td class="tr-cols2">{{selManageRate}}% 
                      <span class="td-font">（{{fundsAmount}} X day rate{{selManageRate}} % X {{selDaysUseVal}} = {{manageFee}} USD ）</span>
                    </td>
                    <td class="tr-cols3 bgc" >Trading deadline</td>
                    <td class="bgc">
                      <el-select clearable filterable v-model="selDaysUsePeriod" placeholder="Trading deadline"  @change="currentSelDays" style="width:120px;">
                        <el-option v-for="item in (fundsSetting.daysUsePeriod || '').split('|')" :key="item" :value="item">{{item}}days</el-option>
                      </el-select>
                      <span class="td-span">（The trading period is between {{selMinDay}} and {{selMaxDay}}  day ）</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">auto-renew</td>
                    <td class="tr-cols2">
                      <span class="td-font">By default, automatic renewal is turned on when it expires, and the allocation management fee is paid first and then used.</span>
                    </td>
                    <td class="tr-cols3 bgc" >total trading capital</td>
                    <td class="bgc">
                      <span class="td-span">{{totalTradingAmount}} USD = {{Number(form.margin).toFixed(0)}}USD（Margin）+ {{fundsAmount}} USD（Allocating funds）</span>
                    </td>
                  </tr>
                  <tr>
                    <td class="tr-cols1">Operating Instructions</td>
                    <td class="tr-cols2">
                      <span class="td-font">The maximum position ratio of a single stock is {{selSingleHoldingRatio*100}}%。</span>
                    </td>
                    <td class="tr-cols3 bgc">need to prepare funds</td>
                    <td class="bgc">
                      <span class="td-span">{{Number(Number(form.margin) + (fundsAmount * selManageRate * selDaysUseVal / 100)).toFixed(1)}}  USD = {{Number(form.margin).toFixed(0)}} USD（Margin）+ {{Number(fundsAmount * selManageRate * selDaysUseVal / 100).toFixed(1)}} USD (一次性收取管理費)</span>
                    </td>
                  </tr>

                </table>

                <div class="auth-box" style="padding-bottom:40px;">
                  <div slot="footer" class="dialog-footer">
                    <el-button type="primary" :loading="isloading" @click="Onsubmit()">apply immediately</el-button>
                  </div>
                </div>

                <div class="notice">
                    <div class="notice-hd">
                        Precautions：
                    </div>
                    <div class="notice-bd">
                        <ul>
                            <li>1.Margin：The money you use to invest in stocks, the starting point is fairly low。</li>
                            <li>2.Trading period: calculated by day, excluding various statutory holidays。</li>
                            <li>3.The management fee is paid on a daily basis (excluding transaction Stamp duty, transfer fee and commission), without any other fees.</li>
                            <li>4.Such as trading for 10 days, a one-time 10-day management fee will be charged</li>
                            <li>5.Applications made before 14:50 on the trading day will take effect on the same day (account management fees will be charged from that day), and applications made after 14:50 on the trading day will take effect on the next trading day.</li>
                            <li>6.The stock market is risky, and investment needs to be cautious.</li>
                        </ul>
                    </div>
                </div>

              </el-card>
              
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../components/HeaderOrder'
  import MenuBox from '@/pages/user/components/menu'
  import * as api from '../../../axios/api'

  export default {
    components: {
      HomeHeader,
      MenuBox
    },
    props: {},
    data () {
      return {
        isloading: false,
        form: {
          margin: ''
        },
        selLever: 0,
        selManageRate: 0,
        selCycleType: '',
        selDaysUsePeriod: '',
        selDaysUseVal: '',
        selMinDay: '',
        selMaxDay: '',
        selSingleHoldingRatio: '',
        fundsSetting: {},
        fundsTypeList: [{}]
      }
    },
    watch: {},
    computed: {
      fundsAmount () { //配资金额= 保证金*杠杆倍数
        if (this.form.margin) {
          return Number(this.form.margin * this.selLever).toFixed(0)
        } else {
          return 0
        }
      },
      totalTradingAmount () {//总操盘金额
        if (this.form.margin) {
          return Number(Number(this.form.margin) + (this.form.margin * this.selLever)).toFixed(0)
        } else {
          return 0
        }
      },
      manageFee () {//管理费
        if (this.selDaysUseVal) {
          return (this.fundsAmount * this.selManageRate * this.selDaysUseVal / 100).toFixed(1)
        } else {
          return 0
        }
      }
    },
    created () {
      this.getUserInfo()
      this.getFundsSetting()
      this.getFundsTypeList()
    },
    mounted () {
      this.$store.state.userMenu = '2-22'
    },
    methods: {
      async getUserInfo () {
        // 获取用户信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判断是否登录
          this.$store.state.userInfo = data.data
        } else {
        }
      },
      async getFundsSetting () {
        // 分仓配资设置信息查询
        let data = await api.getFundsSetting()
        if (data.status === 0) {
          this.fundsSetting = data.data
          this.form.margin = this.fundsSetting.marginMin
          this.selDaysUsePeriod = this.fundsSetting.daysUsePeriod.split('|')[0] + 'day'
          this.selDaysUseVal = this.fundsSetting.daysUsePeriod.split('|')[0]
          this.selMinDay = this.fundsSetting.daysUsePeriod.split('|')[0]
          this.selMaxDay = this.fundsSetting.daysUsePeriod.split('|')[this.fundsSetting.daysUsePeriod.split('|').length - 1]
        } else {
        }
      },
      async getFundsTypeList () {
        // 查询配资类型杠杆
        let data = await api.getFundsTypeList({cycleType:1})
        if (data.status === 0) {
          //取最后一条 10被倍
          this.fundsTypeList = data.data.list.slice(9)
          this.selLever = this.fundsTypeList[0].lever
          this.selManageRate = this.fundsTypeList[0].manageRate
          this.selCycleType = this.fundsTypeList[0].lever + 'multiple'
          this.selSingleHoldingRatio = this.fundsTypeList[0].singleHoldingRatio
        } else {
        }
      },
      getIntNumber () {
        if (this.form.margin >= this.fundsSetting.marginMax) {
          this.form.margin = this.fundsSetting.marginMax
        }
        this.form.margin = Math.floor(this.form.margin)
      },
      async Onsubmit () {
        // 融资转指数
        let opt = {
          userId: this.$store.state.userInfo.id,
          userName: this.$store.state.userInfo.realName,
          userPhone: this.$store.state.userInfo.phone,
          fundsType: 1, // 配资类型：1按天、2按周、3按月
          margin: this.form.margin, //保证金
          fundsAmount: this.fundsAmount, //配资金额
          lever: this.selLever, //杠杆
          totalTradingAmount: this.totalTradingAmount, //总操盘金额
          tradersCycle: this.selDaysUseVal, //操盘期限
          manageFee: this.manageFee //管理费
        }
        let data = await api.addFundsApply(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
        } else {
          this.$message.error(data.msg)
        }
      },
      currentSel(selVal) {
        this.selLever = selVal.lever
        this.selManageRate = selVal.manageRate
        this.selCycleType = selVal.lever + 'multiple'
        this.selSingleHoldingRatio = selVal.singleHoldingRatio
      },
      currentSelDays(selVal) {
        this.selDaysUsePeriod = selVal + 'day'
        this.selDaysUseVal = selVal
      }
      
    }
  }
</script>
<style lang="less" scoped>
  .code {
    font-size: 12px;
    color: #999;
  }

  .auth-box {
    margin-top: 20px;

    .el-row {
      margin-bottom: 10px;
    }
  }

  .authed-box {
    .el-row {
      padding-top: 40px;
      width: 400px;
      margin: 0 auto;
    }
  }

  .wrapper {
    padding: 20px;

    .demo-form-inline {
      width: 400px;
      margin: 20px auto;
    }

    .auth-box {
      .dialog-footer {
        width: 400px;
        margin: 0 auto;

        .el-button {
          margin-left: 100px;
          width: 300px;
        }
      }
    }

    .prompt-box {
      padding-bottom: 20px;
      color: #f44336;

      p {
        line-height: 24px;
      }
    }

    .rule-box {
      margin-top: 30px;
      line-height: 26px;
      margin-left: 70px;
    }
  }

  .box-card {
    .red {
      font-size: 12px;
      margin-left: 10px;
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
    width: 60px;
  }
  .tr-cols2 {
    width: 280px;
  }
  .tr-cols3 {
    width: 80px;
    padding-left: 15px;
  }
  .td-input {
    width: 120px;
  }
  .daystable{
    border-spacing:0px 0px;
  }
  table td{ height:70px; border:#CCCCCC 0px solid;}


  .notice {
    padding: 10px 30px 40px;
    color: #bb9767;
  }
  .notice .notice-hd {
    line-height: 50px;
    font-size: 18px;
    border-bottom: 1px dashed #D9D9D9;
    padding: 0 16px;
  }

  .notice .notice-bd li {
    font-size: 12px;
    line-height: 30px;
}
  
</style>
