<template>
    <div class="wrapper">

        <mt-navbar class="head" v-model="selected"  >
            <mt-tab-item id="1" @click="tabsClick">{{$t('ipo.openSubscription')}}</mt-tab-item>
            <mt-tab-item id="2" @click="tabsClick">{{$t('ipo.subscriptionList')}}</mt-tab-item>
        </mt-navbar>
        <div v-if="selected == 1">
            <!-- <div v-if="list.length<=0" 
            class="empty text-center">
            No order information yet!
            </div> -->
            <div v-if="list.length<=0 " 
            style="margin-top:20px" 
            class="empty text-center">
                    <mt-spinner type="fading-circle"></mt-spinner>
                    {{$t('common.loading')}}...
            </div>
            <div v-if="list.length>0">
            <ul
                class="order-info-box-wrap"
                :infinite-scroll-disabled="loading"
                infinite-scroll-distance="0">
                <li v-for="(item) in list" :key="item.key">
                <div class="order-info-box">
                    <div class="order-title">
                    <span class="main">{{$t('ipo.stockCodeName')}}</span>
                    <span class="secondary">({{item.stockCode}}/{{item.stockName}})</span>
                    <span class="direction pull-right big-font">
                        <!-- {{$t('ipo.issueMarket')}}: -->
                                <b class="space green">{{item.stockPlate}}</b>
                            </span>
                    </div>
                    
                    <div class="order-info">
                        <p class="clearfix">
                            <span class="col-xs-6 text-left">{{$t('ipo.dateOfIssuance')}}:<b class="space">{{item.ticketingDate}}</b></span>
                            <!-- <span class="col-xs-6 text-right green">承銷張數:<b class="space">{{item.underwritingSheet}}</b></span> -->
                            <span class="col-xs-6 text-right green">{{$t('ipo.marketPrice')}}:<b class="space">{{item.marketPrice}} USD</b></span>
                        </p>
                        <p class="clearfix">
                            <span class="col-xs-6  text-left">{{$t('ipo.dateOfDraw')}}:<b class="space">{{item.drawDate}}</b></span>
                            <span class="col-xs-6 text-right yellow">{{$t('ipo.underwritingPrice')}}:<b class="space">{{item.underwritingPrice}} USD</b></span>

                        </p>
                        <p class="clearfix">
                            <span class="col-xs-6  text-left">{{$t('ipo.subscriptionPeriod')}}:<b class="space">{{item.subscriptionTime}} </b></span>
                        </p>
                    </div>
                    <div class="order-foot clearfix">
                    <!-- <div style="text-align: left;color: #666;padding: 0;" class="col-xs-6">
                        <b v-if="item.addTime">{{new Date(item.addTime) | timeFormat}}</b>
                        <b v-else></b>
                    </div> -->
                        <div  v-if="isSubscribe(item.subscriptionTime)"  @click="clickCtock(item)" class="foot-btn">
                            <i class='font-icon'></i>
                            {{$t('ipo.subscription')}}
                        </div>
                            <div v-else class="foot-btn">
                                {{item.remark ==''?$t("ipo.subscription"):item.remark}}
                            </div>
                        </div> 
                </div>
                </li>
            </ul>
            <div v-show="loading" class="load-all text-center">
                <mt-spinner type="fading-circle"></mt-spinner>
                {{$t('common.loading')}}...
            </div>
            <div v-show="!loading" class="load-all text-center">
                {{$t('common.allLoaded')}}
            </div>
            </div>
        </div>
        <!-- 申購記錄 -->
        <div v-if="selected == 2">
                    <!-- <div v-if="list.length<=0" 
                    class="empty text-center">
                    No order information yet!
                    </div> -->
                    <div  
                        v-if="historyLoading "
                        style="margin-top:20px"
                        class="empty text-center">
                        <mt-spinner type="fading-circle"></mt-spinner>
                        {{$t('common.loading')}}...
                    </div>
                    <div v-if="historyLoading">
                    <ul
                        class="order-info-box-wrap"
                        :infinite-scroll-disabled="loading"
                        infinite-scroll-distance="0">
                        <li v-for="(item) in historyList" :key="item.key">
                        <div class="order-info-box">
                            <div class="order-title">
                            <span class="main">{{$t('ipo.stockCodeName')}}</span>
                            <span class="secondary">({{item.stockCode}}/{{item.stockName}})</span>
                            <span class="direction pull-right big-font">
                                        <b class="space green">{{statusType[item.status]}}</b>
                                    </span>
                            </div>
                            
                            <div class="order-info">
                                <p class="clearfix">
                                    <span class="col-xs-6 text-left">{{$t('ipo.dateOfIssuance')}}:<b class="space">{{item.ticketingDate}}</b></span>
                                    <span class="col-xs-6 text-right ">{{$t('ipo.numberOfWinningTickets')}}:<b class="space green">{{item.submitSheets}}</b></span>
                                </p>
                                <p class="clearfix">
                                    <span class="col-xs-6  text-left">{{$t('ipo.dateOfDraw')}}:<b class="space">{{item.drawDate}}</b></span>
                                    <span class="col-xs-6 text-right ">{{$t('ipo.marketPrice')}}:<b class="space red">{{item.marketPrice}} USD</b></span>
                                </p>
                                <p class="clearfix">
                                    <span class="col-xs-6  text-left">{{$t('ipo.subscriptionDate')}}:<b class="space">{{formatDate(item.submitTime)}}</b></span>
                                    <span class="col-xs-6 text-right ">{{$t('ipo.underwritingPrice')}}:<b class="space yellow">{{item.underwritingPrice}} USD</b></span>
                                </p>
                                <p class="clearfix" v-if="item.status != 1">
                                    <span class="col-xs-6  text-left">{{$t('ipo.dateOfWinningTheLottery')}}:<b class="space">{{formatDate(item.endTime)}}</b></span>
                                    <span class="col-xs-6 text-right ">{{$t('ipo.numberOfWinningTickets')}}:<b class="space yellow">{{item.tradeSheets}}</b></span>
                                </p>
                                <p class="clearfix" >
                                    <span class="col-xs-6  text-left">{{$t('ipo.theWinningAmount')}}:<b class="space yellow">{{formartAmount(item.tradeAmount)}} USD</b></span>
                                    <!-- <span class="col-xs-6 text-right ">中籤張數:<b class="space yellow">{{item.tradeSheets}}</b></span> -->
                                    <span v-if="item.status == 1" class="col-xs-6 text-right ">{{$t('ipo.subscriptionPrice')}}:<b class="space yellow">{{formartAmount(item.submitAmount)}} USD</b></span>
                                </p>
                            </div>
                            <div class="order-foot clearfix">
                            <!-- <div style="text-align: left;color: #666;padding: 0;" class="col-xs-6">
                                <b v-if="item.addTime">{{new Date(item.addTime) | timeFormat}}</b>
                                <b v-else></b>
                            </div> -->
                                    <div >
                                        <div v-if="item.status != 1" class="col-xs-12 text-right ">

                                           
                                            <div v-if="item.deductionStatus == 1" class="foot-btn">
                                                <div v-if="isNowDay(item)"  @click="clickPay(item)">
                                                    {{$t('ipo.toPay')}}
                                                </div>

                                                <div v-else>
                                                    {{$t('ipo.notDuePaymentTime')}}
                                                </div>
                                                <!-- <i class='font-icon'></i> -->
                                                
                                            </div>
                                            <div v-if="item.deductionStatus == 2"  class="foot-btn">
                                                <i class='font-icon'></i>
                                                {{$t('ipo.paid')}}
                                            </div>
                                        </div>
                                  
                                       
                                    </div>
                            </div>
                        </div>
                        </li>
                    </ul>
                    <!-- <div v-show="loading" class="load-all text-center">
                        <mt-spinner type="fading-circle"></mt-spinner>
                        {{$t('common.loading')}}...
                    </div> -->
                    <div v-show="!loading" class="load-all text-center">
                        {{$t('common.allLoaded')}}
                    </div>
                    </div>
        </div>

        <el-dialog show-close :visible.sync="dialogCommunity" width="80%">
                <div class="storeinformation_popup">
                    <el-form :model="form" ref="form" class="demo-form">
                        <div class="storeinformation_popup_top">
                            <el-form-item>
                                <el-input type="text" v-model="form.submitSheets" 
                                :placeholder="$t('ipo.pleaseQuantity')"
                                    show-word-limit oninput="value=value.replace(/[^\d]/g,'')">
                                </el-input>
                            </el-form-item>
                            <p>  {{$t('common.stockName')}}:{{form.stockName}}</p>
                            <p style="margin-top:20px"> {{$t('ipo.underwritingPrice')}}:{{form.underwritingPrice}}USD</p>
                            <p style="margin-top:20px"> {{$t('ipo.availableFunds')}}:{{formartAmount($store.state.userInfo.enableAmt)}}USD</p>
                            <p v-if="form.sheets !=0" style="margin-top:20px"> {{$t('ipo.cost')}}:{{formartAmount(form.underwritingPrice*1000*form.submitSheets)}}USD</p>

                        </div>
                        <el-form-item style="text-align:center;margin-top:30px">
                            <el-button type="primary" @click="submitData()"
                                style="background-color: #fff !important;color:#333 !important;border-color:#DCDFE6 !important;">
                                {{$t('common.confirm')}}
                            </el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-dialog>
    </div>
</template>

<script>
    import * as api from '../../axios/api'
    import numeral from 'numeral';
    import { Toast, MessageBox } from 'mint-ui'
    const dayjs = require('dayjs')
    import isBetween from 'dayjs/plugin/isBetween'
    dayjs.extend(isBetween)

    export default {
        data() {
            return {
                selected:'1',
                list: [],
                historyList: [],
                dialogCommunity: false,
                form: {
                    underwritingPrice:null,
                },
                saveStock: null, //申購數據
                loading:false,
                historyLoading:false,
                // statusType:["","Appointment successful","Won the lottery","Did not win the lottery"],//,"部分中签"

            }
        },
        watch:{
            selected(newData,oldData){
                if (newData== "1") {
                    this.getHiStockList()
                }   
                if (newData == "2") {
                    this.getStockSubscribeHistoryList()
                } 
            }
        },
        computed: {
            userAmt(){
                console.log(this.$store.state.userInfo.userAmt);
                return  Number(this.$store.state.userInfo.userAmt).toFixed(2);
            },
            statusType(){
                return ["",this.$t('ipo.appointmentSuccessful'),
                this.$t('ipo.wonTheLottery'),
                this.$t('ipo.didNotWinTheLottery')]
            } 

        },
        created() {
            this.getHiStockList()
            this.getStockSubscribeHistoryList()
            this.getUserInfo()
        },
        methods: {
            tabsClick(){
                console.log(11);
                if (this.selected == "1") {
                    this.getHiStockList()
                }   
                if (this.selected == "2") {
                    this.getStockSubscribeHistoryList()
                } 
            },
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
            formartAmount(amount){
                return numeral(amount).format('0,0')
            },
            formatDate(date){
                return dayjs(date).format('YYYY/MM/DD')
            },
            /**判斷是否可以申購 */
            isSubscribe(subscriptionTime){
                let date1 =  subscriptionTime.split('~')[0]
                let date2 =  subscriptionTime.split('~')[1]
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
            clickCtock(item) {
                this.dialogCommunity = true
                this.form = item
            },
            async getStockSubscribeHistoryList() {
                let data = await api.getStockSubscribeHistoryList()
                this.historyLoading = true
                if (data.status === 0) {
                    this.historyList =  data.data.list.map(item=>{
                        item.submitTime =  dayjs( item.submitTime)
                        return item;
                    })
                  
                }
                this.historyLoading = false
            },

            // 申購
            async submitData() {
                // if(this.$store.state.userInfo.enableAmt < this.form.underwritingPrice*1000*this.form.submitSheets){
                //     // Toast("申購失敗，可用資金不足")
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

            clickPay(item){
                MessageBox.confirm(this.$t('ipo.confirmPay')).then(async action => {
                    let opt = {
                        id: item.id
                    }
                    let data = await api.payStockSubscribe(opt)
                    if (data.status === 0) {
                        Toast(data.data)
                        this.getStockSubscribeHistoryList()
                    } else {
                        Toast(data.data)
                    }
                })
            },
            isNowDay(item){
                return dayjs(new Date()).isBetween( item.drawDate, dayjs().year()+"/"+item.ticketingDate) 
                // return dayjs().isSame(date, 'day')
            }
        },

    }
</script>

<style lang="less" scoped>
    .wrapper .mint-navbar{
        background-color: #fff;
    }
    .funds-list-item {
        padding: 0.2rem 0.16rem 0.45rem !important;
    }

    body {
        background-color: #16171d;
    }

    .wrapper {
        padding-bottom: 0;
    }

    .funds-info {
        display: block;
        width: 6.9rem;
        margin: .3rem;
        padding: .6rem .2rem .12rem .2rem;
        background-color: #1F2636;
        border-radius: .1rem;

        .money-info {
            padding-bottom: .42rem;
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #fff2;

            &_left {
                flex: 1;
                position: relative;
                display: flex;
                flex-direction: column;
                justify-content: space-between;

                .money-info_total {
                    height: .4rem;
                    display: flex;
                    align-items: center;

                    .ino-ico {
                        width: .38rem;
                        height: .38rem;
                        margin-right: .15rem;
                    }

                    .ino-title {
                        font-size: .24rem;
                        margin-right: .15rem;
                    }

                    .ino-money {
                        font-size: .4rem;
                        color: #009C46;
                        letter-spacing: 0;
                        font-family: lightnumber;
                    }
                }

                .money-info_progress {
                    width: 100%;
                    height: .1rem;
                    border-radius: .05rem;
                    background-color: #4E5A73;

                    &__inner {
                        background-color: #E6003E;
                        height: .1rem;
                        border-radius: .05rem;
                    }
                }
            }

            &_right {
                margin-left: .3rem;
                width: 1.4rem;
                height: 1.14rem;
                display: flex;
                flex-direction: column;
                justify-content: space-between;

                .money-info_botton {
                    width: 1.4rem;
                    height: .42rem;
                    line-height: .42rem;
                    font-size: .24rem;
                    color: #fff;
                    text-align: center;

                    &.redBtn {
                        background-color: #E6003E;
                        border-radius: .21rem;
                    }

                    &.blueBtn {
                        background-color: #024DA1;
                        border-radius: .21rem;
                    }
                }
            }
        }

        .money-detail {
            position: relative;

            &_title {
                padding: .3rem 0;
                display: flex;
                justify-content: space-between;
            }

            &_acc {
                display: flex;
                flex-wrap: wrap;

                .acc {
                    padding: 0;
                    display: flex;
                    width: 50%;

                    .acc-item {
                        margin-left: .2rem;
                        margin-bottom: .3rem;
                    }

                    .acc-item_title {
                        font-size: .24rem;
                        color: #fff;
                        line-height: .3rem;
                        margin-bottom: .2rem;
                    }

                    .acc-item_num {
                        font-size: .3rem;
                        color: #E6003E;
                        font-family: lightnumber;
                    }
                }
            }
        }

    }

    .funds-list {
        display: block;
        width: 6.9rem;
        margin: .3rem;
        padding: .12rem .2rem .12rem .2rem;
        background-color: #1F2636;
        border-radius: .1rem;

        &-title {
            text-align: center;
            padding: .26rem 0;
            color: #fff;
            font-size: .25rem;
            font-weight: bold;
        }

        &-item {
            padding: .45rem .16rem;
            border-top: 1px solid #fff2;

            &__title {
                display: flex;
                justify-content: space-between;
                margin-bottom: .35rem;

                &-left {
                    font-size: .3rem;

                    .zhishu {
                        font-size: .2rem;
                        color: #fff;
                        padding: .05rem .15rem;
                        background-image: url(../../assets/ico/zhishu.png);
                        background-size: 100% 100%;
                    }
                }

                &-right {
                    font-size: .24rem;
                }
            }

            .account {
                padding: 0;

                .order-title {
                    color: #AAAAAA;
                    font-size: .24rem;
                    margin-bottom: .1rem;
                    margin-top: .4rem;
                    text-align:center;
                }

                .order-money {
                    color: #fff;
                    font-size: .24rem;
                    margin-bottom: .13rem;
                    text-align: center;
                }

                .peizi {
                    height: .34rem;
                    background-color: #138EB4;
                    border-radius: .17rem;
                    text-align: center;
                    width: 1.26rem;
                    line-height: .35rem;
                }
            }
        }

        &-loadmore {
            height: .67rem;
            line-height: .67rem;
            text-align: center;
            font-size: .27rem;
            color: #fff;
            background-color: #494951;
            border-radius: 3px;
            margin-bottom: .4rem;
        }
    }

    .red-theme {
            .wrapper .mint-navbar{
                background-color: #fff;
            }
            .mint-navbar .mint-tab-item{
                background-color: #fff;
            }
            .funds-info {
            background-color: #fff;

            .ino-title {
                color: #000;
            }

            .acc-item_title {
                color: #000 !important;
            }
        }

        .funds-list {
            background-color: #fff;

            &-title {
                color: #000;
            }

            &-item {
                border-top-color: #2222;

                &__title-left {
                    color: #222;
                }

                .account .order-title {
                    color: #666666;
                }

                .account .order-money {
                    color: #000;
                }

                .account .peizi {
                    background-color: #BC1816;
                    color: #fff;
                }
            }
        }

        .funds-list-loadmore {
            background-color: #BC1816;
        }
    }
</style>