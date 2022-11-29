<template>
  <div class="wrapper">
      <div class="funds-info">
          <div class="money-info">
            <div class="money-info_left">
                <div class="money-info_total">
                    <img class="ino-ico" v-show="$state.theme !='red' " src="../../assets/ico/zongzichan.png">
                    <img class="ino-ico" v-show="$state.theme =='red' " src="../../assets/ico/zongzichan-red.png">
                    <span class="ino-title">Total assets of Taiwan stocks</span>
                    <span class="ino-money">
                    <span style="font-size: .3rem"></span>
                    {{(Number($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)).toFixed(2)}}</span>
                </div>
                <div class="money-info_progress">
                    <div class="money-info_progress__inner"
                    style="width: `${progress}`%"
                    ></div>
                </div>
            </div>
            <div class="money-info_right">
                <div class="money-info_botton redBtn" @click="toRecharge">Recharge</div>
                <div class="money-info_botton blueBtn" @click="toCash">Withdrawal</div>
            </div>
          </div>
          <div class="money-detail">
              <div class="money-detail_title">
                  <span style="font-size: .24rem; font-weight:bold;color:#fff"
                  :style="{color:$state.theme =='red'&&'#000'}"
                  >US stock account
                  <span
                  >({{(Number($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)).toFixed(2)}})</span>
                  </span>
                  <span style="font-size: .2rem"
                  :style="{color:$state.theme =='red'&&'#000'}"
                  >mutual fund transfer
                  <i 
                  style="font-size: .2rem"
                  class="iconfont icon-you"></i>
                  </span>
              </div>
              <div class="money-detail_acc">
                  <div class=" acc">
                      <img src="../../assets/ico/hszzj.png"
                      style="width:.25rem;height:.3rem"
                      >
                      <div class="acc-item">
                          <div class="acc-item_title">Total U.S. stock capital</div>
                          <div class="acc-item_num">{{(Number($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)).toFixed(2)}}</div>
                      </div>
                  </div>
                  <div class=" acc">
                      <img src="../../assets/ico/hsdjbzj.png"
                      style="width:.29rem;height:.25rem"
                      >
                      <div class="acc-item">
                          <div class="acc-item_title">US stocks freeze margin</div>
                          <div class="acc-item_num">{{$store.state.hide?'****':$store.state.userInfo.allFreezAmt}}</div>
                      </div>
                  </div>
                  <div class=" acc">
                      <img src="../../assets/ico/hskyzj.png"
                      style="width:.28rem;height:.28rem"
                      >
                      <div class="acc-item">
                          <div class="acc-item_title">U.S. stocks available funds</div>
                          <div class="acc-item_num">{{$store.state.hide?'****':$store.state.userInfo.enableAmt}}</div>
                      </div>
                  </div>
                  <div class=" acc">
                      <img src="../../assets/ico/zyk.png"
                      style="width:.28rem;height:.28rem"
                      >
                      <div class="acc-item">
                          <div class="acc-item_title">Total profit and loss of U.S. stock holdings</div>
                          <div class="acc-item_num">{{$store.state.hide?'****':$store.state.userInfo.tradingAmount}}</div>
                      </div>
                  </div>
              </div>
          </div>
      </div>
      <div class="funds-list">
          <div class="funds-list-title">
            my allocation
          </div>
          <div class="funds-list-item"
            v-for="(i, k) in list.list"
            :key="k"
          >
            <div class="funds-list-item__title">
                <span class="funds-list-item__title-left">
                    <!-- <span class="zhishu">
                        
                    </span> -->
                    Total trading capital:
                    <span class="green">{{i.totalTradingAmount}}</span>
                </span>
                <span class="funds-list-item__title-right">
                </span>
            </div>
            <div class="funds-list-item__account clearfix">
                <div class="col-xs-4 account">
                    <!-- Allocating funds：17.63 股票可用金額：17.63 配資管理費：Over -->
                    <div class="order-title">Allocating funds</div>
                    <div class="order-money">{{i.fundsAmount || 0}}</div>
                    <div class="order-title">Amount available in stock</div>
                    <div class="order-money">{{i.totalTradingAmount || 0}}</div>
                    <div class="order-title">Funding management fee</div>
                    <div class="order-money">{{i.manageFee || 0}}</div>
                </div>
                <div class="col-xs-4 account">
                    <!-- Margin：100 股票市值：0 股票盈虧：0  -->
                    <div class="order-title">Margin</div>
                    <div class="order-money">{{i.margin || 0}}</div>
                    <div class="order-title">stock market value</div>
                    <div class="order-money">{{i.orderTotalPrice || 0}}</div>
                    <div class="order-title">stock profit and loss</div>
                    <div class="order-money">{{i.allProfitAndLose || 0}}</div>
                </div>
                <div class="col-xs-4 account">
                    <div class="order-title">state</div>
                    <div class="order-money">
                        <span v-if="i.status==0" >Pending review</span>
                        <span v-if="i.status==1" >In operation</span>
                        <span v-if="i.status==2" >Did not pass</span>
                        <span v-if="i.status==3" >Be expired</span>
                        <span v-if="i.status==4" >Over</span>
                    </div>
                    <div class="peizi">
                        Allotment
                    </div>
                </div>
            </div>
          </div>
          <div class="funds-list-loadmore">
            Click to load more content
          </div>
      </div>
  </div>
</template>

<script>
import * as api from '../../axios/api'

export default {
    data() {
        return {
            list:{
                list:[]
            }
        }
    },
    computed:{
        progress() {
            let per = this.$store.state.userInfo.userAmt/(this.$store.state.userInfo.userAmt+this.$store.state.userInfo.userIndexAmt) * 100 > 100?100:this.$store.state.userInfo.userAmt/(this.$store.state.userInfo.userAmt+this.$store.state.userInfo.userIndexAmt) * 100 > 100?this.$store.state.userInfo.userAmt/(this.$store.state.userInfo.userAmt+this.$store.state.userInfo.userIndexAmt) * 100 > 100?100:this.$store.state.userInfo.userAmt/(this.$store.state.userInfo.userAmt+this.$store.state.userInfo.userIndexAmt) * 100:100
            return per
        }
    },
    created() {
        this.getlist()
    },
    methods:{
        toRecharge () {
      // Recharge
        this.$router.push('/recharge')
        },
        toCash () {
        // Withdrawal
        this.$router.push('/cash')
        },
        async getlist () {
            // 獲取持仓列錶
            let opt = {
            userId: 0, 
            pageNum: this.pageNum,
            pageSize: this.pageSize
            }
            let data = await api.getUserApplyList(opt)
            if (data.status === 0) {
                this.list = data.data
            } 
            // else {
            //     this.$message.error(data.msg)
            // }
        },
    }
}
</script>

<style lang="less" scoped>
  body {
    background-color: #16171d;
  }
  .wrapper {
      padding-bottom: 0;
  }
  .funds-info {
      display: block;
      width: 6.9rem;
      margin:.3rem;
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
                width:  1.4rem;
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
            .acc{
                padding: 0;
                display: flex;
                width: 50%;
                .acc-item{
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
    margin:.3rem;
    padding: .12rem .2rem .12rem .2rem;
    background-color: #1F2636;
    border-radius: .1rem;
    &-title {
        text-align: center ;
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
            }
            .order-money {
                color: #fff;
                font-size: .24rem;
                margin-bottom: .13rem;
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
      .funds-info{
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