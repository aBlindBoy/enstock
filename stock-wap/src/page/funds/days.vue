<template>
    <div class="wrapper">
        <div class="days-box">
            <div class="days-box_tile">
                <span>Margin</span>
                <span
                style="font-size:.24rem;color:#fff8"
                :style="{color:$state.theme =='red'?'#222':''}"
                >(Margin between {{fundsSetting.marginMin}} and {{fundsSetting.marginMax}} )</span>
            </div>
            <div class="days-box_items">
                <div
                :class="['days-box_item', item.value == form.margin?'active':'']"
                v-for="(item, key) in bzjs"
                :key="key"
                @click="setBaozhen(item.value)"
                >{{item.label}}</div>
                <div
                :class="['days-box_item', zidinyi?'active':'']"
                @click="setBaozhen('')"
                >
                customize
                </div>
                <div
                class="days-box_item"
                @click="setBaozhen('')"
                >
                <input v-show="zidinyi" type="text" v-model="form.margin">
                </div>
            </div>
        </div>
        <div class="days-box">
            <div class="days-box_tile">
                <span>Allocating funds</span>
                <span
                style="font-size:.24rem;color:#fff8"
                :style="{color:$state.theme =='red'?'#222':''}"
                >(Allocating funds for {{fundsAmount}})</span>
            </div>
            <div class="days-box_items beishu">
                <div 
                v-for="(item, index) in fundsTypeList" :key="item.id"
                :class="['days-box_item', selLever==item.lever?'active':'']"
                :style="{marginRight:(index+1)%4==0&&'0'}"
                @click="currentSel(item)"
                >
                    {{item.lever}} multiple
                </div>
            </div>
        </div>

        <div class="days-box">
            <div class="days-box_tile">
                <span>Trading deadline</span>
                <span
                style="font-size:.24rem;color:#fff8"
                :style="{color:$state.theme =='red'?'#222':''}"
                >(The trading period is between {{selMinDay}} day and {{selMaxDay}} day)</span>
            </div>
            <div class="days-box_items beishu">
                <div
                :class="['days-box_item']"
                >
                <input type="text" v-model="selDaysUseVal"
                @blur="currentSelDays"
                >
                </div>
                <span class="unit">day</span>
            </div>
        </div>
        <div class="notify-1">
            <span style="color:#fff"
                :style="{color:$state.theme =='red'?'#222':''}"
            
            >Total trading capital:</span>
            {{totalTradingAmount}}  = {{Number(form.margin).toFixed(0)}}(Margin)+ {{fundsAmount}} (Allocating funds)
        </div>
        <div class="notify-1">
            <span style="color:#fff"
                :style="{color:$state.theme =='red'?'#222':''}"
            
            >Funds to be prepared:</span>
             {{Number(Number(form.margin) + (fundsAmount * selManageRate * selDaysUseVal / 100)).toFixed(1)}}   = {{Number(form.margin).toFixed(0)}} (Margin)+ {{Number(fundsAmount * selManageRate * selDaysUseVal / 100).toFixed(1)}}  (免收取管理費)
        </div>
        <div class="days-box">
            <div class="guize">
                <span class="tile">Warning line</span>
                <span class="mony">{{Number(Number(form.margin) + (form.margin * fundsSetting.daysWarning)).toFixed(0)}} </span>
                <span class="desc">( Warning line = Allocating funds + Margin X {{fundsSetting.daysWarning}} )</span>
            </div>
            <div class="guize">
                <span class="tile">Closing Line</span>
                <span class="mony">{{Number(Number(form.margin) + (form.margin * fundsSetting.daysUnwind)).toFixed(0)}} </span>
                <span class="desc">( Closing Line = Allocating funds + Margin * {{fundsSetting.daysUnwind}} )</span>
            </div>
            <div class="guize">
                <span class="tile">Management fee</span>
                <span class="mony">{{selManageRate}}% </span>
                <span class="desc">( {{fundsAmount}}* day rate {{selManageRate}}% * {{selDaysUseVal}}={{manageFee}} )</span>
            </div>
            <div class="guize">
                <span class="tile">Custom renewal</span>
                <span class="desc">By default, the expiration of the automatic drug period is enabled, and the dispensing management fee is paid first and then used.</span>
            </div>
            <div class="guize">
                <span class="tile">Operating Instructions</span>
                <span class="desc">The maximum position ratio of a single stock is {{selSingleHoldingRatio*100}}%</span>
            </div>
        </div>

        <div class="days-submit"
        @click="Onsubmit()"
        >
        apply immediately
        </div>

        <div class="days-notify2">
            <div class="warn">Precautions:</div>
            <div class="warn-detail">
                <div>1.Margin：The money you use to invest in stocks starts at a fairly low level. </div>
                <div>
                 2.Trading period; calculated on a daily basis, excluding various statutory holidays. 
                </div>
                <div>3.Pay the management fee on a daily basis (excluding Stamp duty, transfer fee and commission), without any other fees</div>
                <div>4.If you trade for 10 days, the 10-day management fee will be waived</div>
                <div>5.Applications before 14:50 on the trading day will take effect on the same day (account management fees will be charged from that day), and applications after 14:50 on the trading day will take effect on the next trading day.</div>
                <div>6.The stock market is risky, and investment needs to be cautious.</div>
            </div>
        </div>
    </div>
</template>

<script>
  import * as api from '../../axios/api'

export default {
    data() {
        return {
            bzjs:[
                { value:10000,label:'10000' },
                { value:50000,label:'50000' },
                { value:100000,label:'100000' },
                { value:200000,label:'200000' },
                { value:500000,label:'500000' },
                { value:1000000,label:'10000000' }, 
            ],
            baozhen:0,
            beishu:100,
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
    methods:{
        setBaozhen(val) {
            this.form.margin = val
        },
            async getUserInfo () {
        // 獲取用戶信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判斷是否登入
          this.$store.state.userInfo = data.data
        } else {
        }
      },
      async getFundsSetting () {
        // 分倉配資設定信息查詢
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
        // 查詢配資類型杠桿
        let data = await api.getFundsTypeList({cycleType:1})
        if (data.status === 0) {
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
        // 融資轉指數
        let opt = {
          userId: this.$store.state.userInfo.id,
          userName: this.$store.state.userInfo.realName,
          userPhone: this.$store.state.userInfo.phone,
          fundsType: 1, // 配資類型：1按天、2按周、3按月
          margin: this.form.margin, //Margin
          fundsAmount: this.fundsAmount, //配資金額
          lever: this.selLever, //杠桿
          totalTradingAmount: this.totalTradingAmount, //總操盤金額
          tradersCycle: this.selDaysUseVal, //操盤期限
          manageFee: this.manageFee //管理費
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
        let val = parseInt(selVal.target.value)
        if (isNaN(val)) {
            this.selDaysUsePeriod = this.selMinDay + 'day'
            this.selDaysUseVal = this.selMinDay
        } else {
            if (val< this.selMinDay) {
                this.selDaysUsePeriod = this.selMinDay + 'day'
                this.selDaysUseVal = this.selMinDay
            } else if(val> this.selMaxDay) {
                this.selDaysUsePeriod = this.selMaxDay + 'day'
                this.selDaysUseVal = this.selMaxDay
            } else {
                this.selDaysUsePeriod = val + 'day'
                this.selDaysUseVal = val
            }
        }
        // this.selDaysUsePeriod = selVal + 'day'
        // this.selDaysUseVal = selVal
      }
    },
    created() {
         this.getUserInfo()
        this.getFundsSetting()
        this.getFundsTypeList()
    },
    computed:{
        zidinyi() {
            return !this.bzjs.map(it => it.value).includes(this.form.margin)
        },
        fundsAmount () { //配資金額= Margin*杠桿倍數
        if (this.form.margin) {
          return Number(this.form.margin * this.selLever).toFixed(0)
        } else {
          return 0
        }
      },
      totalTradingAmount () {//總操盤金額
        if (this.form.margin) {
          return Number(Number(this.form.margin) + (this.form.margin * this.selLever)).toFixed(0)
        } else {
          return 0
        }
      },
      manageFee () {//管理費
        if (this.selDaysUseVal) {
          return (this.fundsAmount * this.selManageRate * this.selDaysUseVal / 100).toFixed(1)
        } else {
          return 0
        }
      }
    }
}
</script>

<style lang="less" scoped>
.wrapper {
    padding-bottom: 0;
    .days-box {
        display: block;
        width: 6.9rem;
        margin:.3rem;
        padding: .6rem .2rem .12rem .2rem;
        background-color: #1F2636;
        border-radius: .1rem;
        &_tile {
            padding: .12rem 0 .12rem .25rem;
            position: relative;
            font-size: .32rem;
            display: flex;
            justify-content: space-between;
            font-weight: bold;
            border-bottom: 1px solid #fff2;
            &:after {
                content: "";
                position: absolute;
                left: 0;
                top: 50%;
                width: .1rem;
                height: .1rem;
                margin-top: -.05rem;
                background: #138EB4;
                border-radius: 50%;
            }
        }
        &_items {
            display: flex;
            justify-content: space-between;
            padding-top: .25rem;
            flex-wrap: wrap;
            padding-bottom: .15rem;
            &.beishu {
                justify-content: flex-start;
                .days-box_item {
                    margin-right: .15rem;
                }
            }
            span.unit {
            line-height: .44rem;
            }
        }
        &_item {
            width: 1.5rem;
            height: .44rem;
            line-height: .44rem;
            border-radius: 3px;
            background-color: #2D2E3B;
            color: #fff;
            font-size: .24rem;
            margin-bottom: .2rem;
            text-align: center;
            padding: 0 .1rem;
            input {
                text-align: center;
                width: 100%;
            }
            &.active {
                background-color: #E00101;
            }
        }
        .guize {
            margin-bottom: .24rem;
            display: flex;
            align-items: center;
            .tile {
                width: 1.5rem ;
                height: .4rem;
                font-size: .22rem;
                line-height: .4rem;
                text-align: center;
                background-color: #138EB4;
                color: #fff;
                padding: 0.02rem 0rem;
                margin-right: .1rem;
                display: block;
            }
            .mony{
                color: #fff;
                font-size: .22rem;
                margin-right: 0.2rem;
            }
            .desc {
                color :#A9AAAE ;
                font-size: .22rem;
            }
        }
    }
    .notify-1 {
        padding: .2rem .3rem;
        font-size: .24rem;
        letter-spacing: 0;
        color: #999;
    }
    .days-submit {
        background-color: #024DA1;
        width: 6.9rem;
        height: .66rem;
        line-height: .66rem;
        margin: 0 auto;
        color: #fff;
        border-radius: .33rem;
        text-align: center;
    }
    .days-notify2 {
        margin-top: .8rem;
        padding: 0 .3rem .3rem;
        .warn {
            font-size: .36rem;
            font-weight: bold;
            margin-bottom: .2rem;

        }
        .warn-detail{
            color: #8D8F94;
            div {
                margin-bottom: .2rem;
            }
        }
    }
}
.red-theme {
    .days-box{
        background-color: #fff;
    }
    .wrapper .days-box_tile {
        color: #000;
        border-bottom-color: #E9E9E9;
    }
    .wrapper .days-box_tile:after {
        background:#000;
    }
    .wrapper .days-box_items span.unit{
        color: #222;
    }
    .wrapper .days-box_item{
        background-color: #D9D9D9;
        border: 1px solid #AEAEAE;
        color: #222;
    }
    .wrapper .days-box_item.active {
        background-color: #E00101;
        border: 1px solid #E00101;
        color: #fff;
    }
    .wrapper .days-box .guize .tile {
        background-color: #E00101;
    }
    .wrapper .days-box .guize .mony {
        color: #222;
    }
    .wrapper .days-box .guize .desc {
        color: #222;
    }
    .wrapper .days-submit {
        background-color: #E00101;
    }
    .wrapper .days-notify2 .warn {
        color: #000;
    }
}
</style>