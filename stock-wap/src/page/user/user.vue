  <template>
    <div class="page wrapper">
      <div class="account-info">
        <div class="account-info_avatar">
          <img src="../../assets/ico/wogerenziliao.png" alt="">
        </div>
        <div class="account-info_detail">
          <div class="account-phone">{{$t('mine.tradingAccount')}}:{{$store.state.userInfo.phone || 'not logged in'}}</div>
          <div class="account-name">{{$t('mine.userName')}}:{{$store.state.userInfo.nickName || 'Not real name'}}</div>
        </div>
        <div class="account-info_ctl" @click="hideNumber">
          {{$t('mine.assetStatus')}}
          <i v-show="$store.state.hide" class="iconfont icon-yanjing"></i>
          <i v-show="!$store.state.hide" class="iconfont icon-yanjing1"></i>
        </div>
      </div>
      <div class="account-container">
        <div class="account-preview">
          <div class="acc-pre-left">
            <span class="ti">{{$t('mine.totalAssets')}}</span>
            <span class="de">
              <div>
                <p 
                  class="account">{{$store.state.hide?'****':
                  formartAmount( Number($store.state.userInfo.userAmt).toFixed(2))
                }} USD</p>
                <p 
                  class="account" style="color: red;">{{$store.state.hide?'****':
                  formartAmount( Number($store.state.userInfo.twUserAmt).toFixed(2))
                }} TWD</p>
              </div>
            </span>
          </div>
          <div class="acc-pre-center">
              <p>
                美股資產: 
                  <span>{{$store.state.hide?'****':
                  formartAmount($store.state.userInfo.userAmt)
                  }}</span>
              </p>
              <p>
                台股資產: <span style="color: red;">{{$store.state.hide?'****':
                formartAmount($store.state.userInfo.twUserAmt)
                }}</span>
              </p>
          </div>
          <div class="acc-pre-right">
            <div class="redbtn btn" @click="toRecharge">{{$t('common.recharge')}}</div>
            <div class="bluebtn btn" @click="toCash">{{$t('common.withdraw')}}</div>

          </div>
        </div>
        <!-- 賬號詳情信息 -->
        <div >
          <!-- 美股資產 -->
          <div class="account-box" >
            <!-- 頭部 -->
            <div class="header" @click="usData.isShow = usData.isShow?false:true">
              <!-- <i v-if="item.isShow" class="iconfont jian"></i>
              <i v-else class="iconfont jia"></i> -->
              <div class="header-left" style="width: 100%;">
                <div>
                  <img v-if="usData.isShow&&$state.theme =='red'" class="iconfont"
                    src="../../assets/ico/jian-red.png"
                    >
                    <img v-else-if="usData.isShow&&$state.theme !='red'" class="iconfont"
                    src="../../assets/ico/jian.png"
                    >
                    <img v-else-if="!usData.isShow&&$state.theme !='red'" class="iconfont"
                    src="../../assets/ico/jia.png"
                    >
                    <img v-else class="iconfont"
                    src="../../assets/ico/jia-red.png"
                    >
                    <span :style="{color:$state.theme =='red'?'#000':'#fff'}">
                        {{usData.name}}
                      {{$t('mine.account')}}</span>
                        <span >({{$store.state.hide?'****':
                        formartAmount($store.state.userInfo.userAmt)
                        }})</span>
                        <div>
                         
                    </div>
                </div>
                <div style="color:azure;background-color: #E6003E;border-radius:0.2rem;width: 1rem;display: flex;    align-items: center; justify-content: center; height: .4rem;"
                    @click="toTransfer(1)">
                    <span>兑换</span></div>
                </div>
            </div>
            <!-- 資產詳情 -->
            <div v-show="usData.isShow" class="content">
              <ul class="clearfix">
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('mine.totalAssets')}}</div>
                  <p  class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.userAmt)
                    }}</p>
                </li>
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('common.availableFunds')}}</div>
                  <p  class="number yellow">
                    {{$store.state.hide?'****': formartAmount($store.state.userInfo.enableAmt)}}</p>
                </li>
                <li>
                  <i class="iconfont icon-dongjiezijin"></i>
                  <div class="name">{{$t('mine.freezeMargin')}} </div>
                  <p class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allFreezAmt)
                    }}</p>
                </li>
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('common.totalProfitAndLoss')}} </div>
                  <p 
                    :class="$store.state.userInfo.allProfitAndLose>0?'number red':$store.state.userInfo.allProfitAndLose<0?'number green':'number'">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allProfitAndLose)
                    }}</p>
                </li>
              </ul>
            </div>
          </div>
          <!-- 美股停損綫 -->
          <div v-show="usData.isShow" 
              class="pcx"
              style="padding: 0.12rem 0.4rem 0.15rem;">
            <!-- 強制Closing Line為 ： 可用資金 + 凍結Margin * 0.6 -->
            <div style="background:#1A1E29">
              {{$t('mine.your')}} {{usData.name}} {{$t('mine.lossLine')}}
            <!-- <span v-if="item.name == 'index'" style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">{{$store.state.hide?'****':
            formartAmount(Number(($store.state.userInfo.enableIndexAmt + $store.state.userInfo.allIndexFreezAmt)
            * indexSettingInfo.forceSellPercent).toFixed(2))
            }} </span> -->
            <!-- v-if="item.name == 'US stocks'" -->
            <span  style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">
            {{$store.state.hide?'****':
              formartAmount(Number(($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt) 
              * settingInfo.forceStopPercent).toFixed(2))}}
            </span>
            <!-- <span v-if="item.name == 'futures'" style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">{{$store.state.hide?'****':
            formartAmount(Number(($store.state.userInfo.enableFuturesAmt + $store.state.userInfo.allFuturesFreezAmt) 
            * futuresSettingInfo.forceSellPercent).toFixed(2))
            }} </span> -->
            <i @click="focePromptPopup = true" ref="button" class="iconfont icon-xinshou"></i>
            </div>
          </div>
          <!-- 台股資產 -->
          <div class="account-box" >
            <div class="header" @click="twData.isShow = twData.isShow?false:true">
              <!-- <i v-if="item.isShow" class="iconfont jian"></i>
              <i v-else class="iconfont jia"></i> -->
              <div class="header-left" style="width:100%">
                <div>
                  <img v-if="twData.isShow&&$state.theme =='red'" class="iconfont"
                  src="../../assets/ico/jian-red.png"
                  >
                  <img v-else-if="twData.isShow&&$state.theme !='red'" class="iconfont"
                  src="../../assets/ico/jian.png"
                  >
                  <img v-else-if="!twData.isShow&&$state.theme !='red'" class="iconfont"
                  src="../../assets/ico/jia.png"
                  >
                  <img v-else class="iconfont"
                  src="../../assets/ico/jia-red.png"
                  >
                  <span :style="{color:$state.theme =='red'?'#000':'#fff'}">
                      {{twData.name}}
                    {{$t('mine.account')}}</span>
                  <span >({{$store.state.hide?'****':
                  formartAmount($store.state.userInfo.twUserAmt)
                  }})</span>
                </div>
                 
                <div style="color:azure;background-color: #E6003E;border-radius:0.2rem;width: 1rem;display: flex;    align-items: center; justify-content: center; height: .4rem;"
                    @click="toTransfer(2)">
                    <span>兑换</span>
                </div>
              </div>
              
            </div>
            <div v-show="twData.isShow" class="content">
              <ul class="clearfix">
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('mine.totalAssets')}}</div>
                  <!-- <p v-if="item.name == 'index'" class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.userIndexAmt)
                    }}</p> -->
                  <p  class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.twUserAmt)
                    }}</p>
                  <!-- <p v-if="item.name == 'futures'" class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount(Number($store.state.userInfo.userFuturesAmt).toFixed(2) )
                    }}</p> -->
                </li>
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('common.availableFunds')}}</div>
                  <!-- <p v-if="item.name == 'index'" class="number yellow">
                    {{$store.state.hide?'****': formartAmount($store.state.userInfo.enableIndexAmt)}}</p> -->
                  <p  class="number yellow">
                    {{$store.state.hide?'****': formartAmount($store.state.userInfo.twEnableAmt)}}</p>
                  
                  <!-- <p v-if="item.name == 'futures'" class="number yellow">
                    {{$store.state.hide?'****': formartAmount($store.state.userInfo.enableFuturesAmt)}}</p> -->
                </li>
                <li>
                  <i class="iconfont icon-dongjiezijin"></i>
                  <div class="name">{{$t('mine.freezeMargin')}} </div>
                  <!-- <p v-if="item.name == 'index'" class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allIndexFreezAmt)
                    }}</p> -->
                  <p class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allFreezAmt)
                    }}</p>
                  <!-- <p v-if="item.name == 'futures'" class="number yellow">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allFuturesFreezAmt)
                    }}</p> -->
                </li>
                <li>
                  <i class="iconfont icon-yingkuixuanzhong"></i>
                  <div class="name">{{$t('common.totalProfitAndLoss')}} </div>
                  <!-- <p v-if="item.name == 'index'"
                    :class="$store.state.userInfo.allIndexProfitAndLose>0?'number red':
                    $store.state.userInfo.allIndexProfitAndLose<0?'number green':'number'">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allIndexProfitAndLose)
                    }}</p> -->
                    <!-- v-if="item.name == 'US stocks'" -->
                  <p 
                    :class="$store.state.userInfo.allProfitAndLose>0?'number red':$store.state.userInfo.allProfitAndLose<0?'number green':'number'">
                    {{$store.state.hide?'****':
                    formartAmount($store.state.userInfo.allProfitAndLose)
                    }}</p>
                  <!-- <p v-if="item.name == 'futures'"
                    :class="$store.state.userInfo.allFuturesProfitAndLose>0?'number red':$store.state.userInfo.allFuturesProfitAndLose<0?'number green':'number'">
                    {{$store.state.hide?'****':
                    formartAmount(Number($store.state.userInfo.allFuturesProfitAndLose).toFixed(2))
                    }}</p> -->
                </li>
                <!-- <li>
                  <i class="iconfont icon-dongjiezijin"></i>
                  <div class="name">New shares freeze Margin</div>
                  <p>{{shengoudj.djzj}}</p>
                </li> -->
              </ul>
            </div>
          </div>
          <!-- 台股停損綫 -->
          <div v-show="twData.isShow" 
              class="pcx"
              style="padding: 0.12rem 0.4rem 0.15rem;">
            <!-- 強制Closing Line為 ： 可用資金 + 凍結Margin * 0.6 -->
            <div style="background:#1A1E29">
              {{$t('mine.your')}} {{twData.name}} {{$t('mine.lossLine')}}
            <!-- <span v-if="item.name == 'index'" style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">{{$store.state.hide?'****':
            formartAmount(Number(($store.state.userInfo.enableIndexAmt + $store.state.userInfo.allIndexFreezAmt)
            * indexSettingInfo.forceSellPercent).toFixed(2))
            }} </span> -->
            <!-- v-if="item.name == 'US stocks'" -->
            <span  style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">
            {{$store.state.hide?'****':
              formartAmount(Number(($store.state.userInfo.twEnableAmt + $store.state.userInfo.allFreezAmt) 
              * settingInfo.forceStopPercent).toFixed(2))}}
            </span>
            <!-- <span v-if="item.name == 'futures'" style="font-weight:bold;font-size:0.26rem;margin:0 0.1rem;">{{$store.state.hide?'****':
            formartAmount(Number(($store.state.userInfo.enableFuturesAmt + $store.state.userInfo.allFuturesFreezAmt) 
            * futuresSettingInfo.forceSellPercent).toFixed(2))
            }} </span> -->
            <i @click="focePromptPopup = true" ref="button" class="iconfont icon-xinshou"></i>
            </div>
          </div>
        </div>

        
      </div>
      <div class="panel" >
        <div class="panel-head">
          <span class="font-w">{{$t('mine.distributionAllocation')}}</span>
        </div>
        <div class="panel-body">
          <div class="row">
            <div @click="goFunds(3)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              {{$t('mine.ipo')}}
            </div>
            <!-- <div @click="goFunds(1)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              Allotment
            </div>
            <div @click="goFunds(2)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              Daily allocation
            </div>
            <div @click="goFundsList(1)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              my allocation
            </div>
            <div @click="goFundsList(2)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              Funding positions
            </div>
            <div @click="goFundsList(3)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              Allotment and liquidation
            </div> -->
          </div>
        </div>
      </div>
      <div class="panel">
        <div class="panel-head">
          <span class="font-w">{{$t('mine.myHolding')}}</span>
        </div>
        <div class="panel-body">
          <div class="row">
            <div @click="goOrderList(1)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
            {{$t('mine.usStockHoldings')}}
            </div>
            <div @click="goOrderList(1)" class="col-xs-3">
              <i class="iconfont icon-rongzi2"></i>
              {{$t('mine.usStockClosing')}}
            </div>
            <!-- <div v-if="this.$store.state.settingForm.indexDisplay" @click="goOrderList(2)" class="col-xs-3">
              <i class="iconfont icon-zhishuyidong"></i>
              Index positions
            </div>
            <div v-if="this.$store.state.settingForm.indexDisplay" @click="goOrderList(2)" class="col-xs-3">
              <i class="iconfont icon-geguyingkui"></i>
              Index close
            </div>
            <div v-if="this.$store.state.settingForm.futuresDisplay" @click="goOrderList(4)" class="col-xs-3">
              <i class="iconfont icon-jiaoyitixing"></i>
              futures position
            </div>
            <div v-if="this.$store.state.settingForm.futuresDisplay" @click="goOrderList(4)" class="col-xs-3">
              <i class="iconfont icon-qihuo1"></i>
              futures close
            </div> -->
          </div>
        </div>
      </div>
      <div class="other">
        <ul class="after">
          <li @click="toAuthentication">
                    <span>
                      <!-- <icon name="shoufei" slot="icon"></icon> -->
                      <!-- <i style="font-size:0.34rem" class="iconfont icon-shenfenrenzheng"></i> -->
                      <img src="../../assets/ico/shimin.png"
                      style="width:.28rem;height:.24rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.realName')}}
                      <span class="renzhen done"
                      v-if="$store.state.userInfo.isActive == 2"
                      >
                        <i class="iconfont el-icon-circle-check" ></i>
                        {{$t('mine.examinationPassed')}}
                      </span>
                      <span class="renzhen ing"
                      v-if="$store.state.userInfo.isActive == 0 || $store.state.userInfo.isActive == 3" style="color:red;font-size: 0.2rem;"
                      >
                        <i class="iconfont el-icon-circle-close" ></i>
                        {{$t('mine.notReviewed')}}
                      </span>
                      <!-- <i v-if="$store.state.userInfo.isActive == 1" style="color:red;font-size: 0.7rem;"
                        class="iconfont icon-shenhezhong"></i>
                      <i v-if="$store.state.userInfo.isActive == 2" style="color:red;font-size: 0.7rem;"
                        class="iconfont icon-tongguo1"></i>
                      <i v-if="$store.state.userInfo.isActive == 0 || $store.state.userInfo.isActive == 3"
                        style="color:red;font-size: 0.75rem;" class="iconfont icon-icon-test"></i> -->
                      <icon name="right66" class="right" slot="icon"></icon>
                    </span>
          </li>
          <li @click="goCard">
                  <span>
                      <!-- <i style="font-size:0.28rem" class="iconfont icon-yinhangqia"></i> -->
                      <img src="../../assets/ico/yinhangka.png"
                        style="width:.28rem;height:.24rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.financialAccount')}}
                      <!-- <i v-if="!$store.state.bankInfo.bankNo" style="color:red;font-size: 0.3rem;margin-left: 0.1rem;"
                        class="iconfont icon-iconfontweitongguo"></i>
                      <i v-if="$store.state.bankInfo.bankNo" style="color:red;font-size: 0.3rem;margin-left: 0.1rem;"
                        class="iconfont icon-yanzhengma"></i> -->
                      <icon name="right66" class="right" slot="icon"></icon>
                  </span>
          </li>
          <li @click="tosetting">
              <span>
                  <!-- <i style="font-size:0.28rem" class="iconfont icon-yinhangqia"></i> -->
                  <img src="../../assets/ico/gaimima.png"
                        style="width:.24rem;height:.24rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.changePassword')}}
                  <!-- <i v-if="!$store.state.bankInfo.bankNo" style="color:red;font-size: 0.3rem;margin-left: 0.1rem;"
                      class="iconfont icon-iconfontweitongguo"></i>
                  <i v-if="$store.state.bankInfo.bankNo" style="color:red;font-size: 0.3rem;margin-left: 0.1rem;"
                      class="iconfont icon-yanzhengma"></i> -->
                  <icon name="right66" class="right" slot="icon"></icon>
              </span>
          </li>
        </ul>
        <ul class="after">
          <!-- <li  @click="goOrderList">
              <span>
                <i style="font-size:0.28rem" class="iconfont icon-chicang"></i>
                我的持仓
                <icon name="right66" class="right" slot="icon"></icon>
              </span>
          </li> -->

        </ul>
        <ul class="after">
          
          <li @click="goDetail">
                <span>
                    <!-- <i style="font-size:0.28rem" class="iconfont icon-zijinmingxi"></i> -->
                    <img src="../../assets/ico/yinhangka.png"
                        style="width:.27rem;height:.24rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.fundingDetails')}}
                    <icon name="right66" class="right" slot="icon"></icon>
                </span>
          </li>
          <li @click="toRechargeList">
                    <span>
                      <!-- <i style="font-size:0.28rem" class="iconfont icon-dingdanjilu1"></i> -->
                      <img src="../../assets/ico/yinhangka.png"
                        style="width:.27rem;height:.27rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.rechargeRecords')}}
                      <icon name="right66" class="right" slot="icon"></icon>
                    </span>
          </li>
          <li @click="toCashList">
                  <span>
                      <!-- <i style="font-size:0.28rem" class="iconfont icon-dingdanjilu1"></i> -->
                      <img src="../../assets/ico/yinhangka.png"
                        style="width:.24rem;height:.24rem;margin-right: 0.15rem;"
                      >
                      {{$t('mine.withdrawalRecords')}}
                      <icon name="right66" class="right" slot="icon"></icon>
                  </span>
          </li>
        </ul>
        <!-- <ul class="after">
            <li  @click="changeStyle">
                <span>
                  <i style="font-size:0.28rem" class="iconfont icon-shouye"></i>
                  主題換膚
                  <span class="right">
                      <i v-if="styleName == 'red'" style="color:#ff9800" class="iconfont icon-baitian"></i>
                      <i v-if="styleName == 'black'" style="color:#ff9800" class="iconfont icon-yewan1"></i>
                  </span>
                </span>
            </li>
        </ul> -->
        <mt-popup v-model="focePromptPopup" popup-transition="popup-fade" class="mint-popup-white">
          <div class="clearfix">
            <a @click="focePromptPopup = false" class="pull-right"><i class="iconfont icon-weitongguo"></i></a>
          </div>
          <p class="font-title"> {{$t('mine.whatIsLossLine')}}?</p>
          <!--  帳戶可用資金 +  -->
          <!-- <p v-if="$store.state.settingForm.stockDisplay" class="font-bold">(美股)強制Closing Line = (帳戶可用資金+凍結Margin) *
            {{settingInfo.forceStopPercent ? settingInfo.forceStopPercent:0}}</p> -->
          <!-- <p v-if="$store.state.settingForm.indexDisplay" class="font-bold">(Index) Forced Stop Loss Line = (Account Available Funds + Freeze Margin) *
            {{indexSettingInfo.forceSellPercent ? indexSettingInfo.forceSellPercent:0}}</p>
          <p v-if="$store.state.settingForm.futuresDisplay" class="font-bold">(Futures) Forced Stop Loss Line = (Account Available Funds + Freeze Margin) *
            {{futuresSettingInfo.forceSellPercent ? futuresSettingInfo.forceSellPercent:0}}</p> -->
          <p v-if="$store.state.settingForm.stockDisplay">
            {{$t('mine.whatIsLossLineContent1')}}<span class="green number">
            - {{formartAmount(Number(($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)
            * settingInfo.forceStopPercent).toFixed(2))}}</span>
            {{$t('mine.whatIsLossLineContent2')}}
          </p>
          <!-- <p v-if="$store.state.settingForm.indexDisplay">When the total profit and loss of your index account positions is<span class="green number">
            -{{formartAmount(Number(($store.state.userInfo.allIndexFreezAmt + $store.state.userInfo.enableIndexAmt) 
            * indexSettingInfo.forceSellPercent).toFixed(2))}}</span>When the system will force a stop loss, to ensure that your account will not expand losses
          </p>
          <p v-if="$store.state.settingForm.futuresDisplay">When the total profit and loss of your futures account positions is<span class="green number">
            -{{formartAmount(Number(($store.state.userInfo.allFuturesFreezAmt + $store.state.userInfo.enableFuturesAmt)
            * futuresSettingInfo.forceSellPercent).toFixed(2))}}</span>When the system will force a stop loss, to ensure that your account will not expand losses
          </p> -->
        </mt-popup>
        <div class="btnbox">
          <!-- {{$store.state.userInfo}} -->
          <span class="text-center btnok loginout" @click="toRegister"> {{$t('mine.logout')}}</span>
        </div>
      </div>
      <foot></foot>
    </div>
  </template>
  <script type="text/ecmascript-6">
  import { Toast } from 'mint-ui'
  // import '@/assets/style/bg.less'
  import foot from '../../components/foot/foot'
  // import { hideNumberTo } from '@/utils/utils'
  import * as api from '@/axios/api'
  // var numeral = require('numeral')
  import numeral from 'numeral'


  export default {
    components: {
      foot
    },
    data () {
      return {
        user: {
          img: ''
        },
        defaultUser: {
          img: require('../../assets/img/default-head.png')
        },
        changeHideStatus: false,
        userAmt: '',
        settingInfo: {}, // 設定信息
        indexSettingInfo: {}, // 設定信息 指數
        futuresSettingInfo: {}, // 設定信息 期貨
        focePromptPopup: false, // 強制平倉提示框
        buttonBottom: 0,
        showChangeBtn: false, // 是否顯示資金互轉按鈕
        styleName: 'black',
        usData:{
          isShow:false,
          name:this.$t('mine.usStock'),
        },
        twData:{
          isShow:false,
          name:"台股賬戶",
        },
      }
    },
    watch: {
      
      //   changeHideStatus(newval){
      //     //   this.userAmt = hideNumberTo(this.$store.state.userInfo.userAmt)
      //   }
    },
    computed: {
      account(){
        return [
          { name: this.$t('mine.usStock'), link: 'stock', isShow: true, isDisplay: false },
          { name: "台股賬戶", link: 'stock', isShow: true, isDisplay: false },
          // { name: 'index', link: 'index', isShow: false, isDisplay: false },
          // { name: 'futures', link: 'futures', isShow: false, isDisplay: false }
        ]
      }

    },
    created () {
      this.getUserInfo()
      this.styleName = window.localStorage.getItem('styleName') ? window.localStorage.getItem('styleName') : 'red'
    },
    mounted () {
      this.getSettingInfo()
      this.getIndexSettingInfo()
      this.getFuturesSetting()
      this.getCardDetail()
      // this.getprice()
      this.changeHideStatus = this.$store.state.hide
      if (this.$store.state.settingForm.indexDisplay || this.$store.state.settingForm.futuresDisplay) {
        this.showChangeBtn = true
      }
    },
    methods: {

      formartAmount(amount){
        return numeral(amount).format('0,0.00')
      },
      // async getprice() {
      //   let opt = {};
      //   let data = await api.getprice(opt);
      //   this.shengoudj = data.data;
      // },
      goFunds(type) {
        if (type  == 1) {
          this.$router.push('/funds')
        }
        if (type == 2) {
          this.$router.push('/days')
        }
        if (type == 3){
          this.$router.push('/ipo')
        }
      },
      changeStyle () {
        if (this.styleName === 'red') {
          this.styleName = 'black'
          this.$store.state.className = 'black'
          window.localStorage.setItem('styleName', 'black')
        } else {
          this.styleName = 'red'
          this.$store.state.className = 'red'
          window.localStorage.setItem('styleName', 'red')
        }
        window.location.reload()
      },
      async getProductSetting () {
        let data = await api.getProductSetting()
        if (data.status === 0) {
          this.$store.state.settingForm = data.data
          // if(this.$store.state.userInfo.accountType != 1){
          this.account[0].isDisplay = data.data.stockDisplay
          // this.account[1].isDisplay = data.data.indexDisplay
          // this.account[2].isDisplay = data.data.futuresDisplay
          // }else{
          //     this.account[0].isDisplay = true
          //     this.account[1].isDisplay = true
          //     this.account[2].isDisplay = true
          // }
        } else {
          this.$message.error(data.msg)
        }
      },
      hideNumber () {
        this.changeHideStatus = this.$store.state.hide
        let i = false
        let j = true
        this.$store.state.hide = this.$store.state.hide ? i : j
      },
      goOrderList: function (val) {
        //   this.$router.push('/orderlist')
        this.$router.push('/orderlist?index=' + val)
      },
      goFundsList: function (val) {
        this.$router.push('/fundslist?index=' + val)
      },
      goDetail: function () {
        this.$router.push('/detail')
      },
      goCard: function () {
        this.$router.push('/card')
      },
      toAggre: function () {
        this.$router.push('/aggre')
      },
      toAuthentication: function () {
        this.$router.push('/authentication')
      },
      toRecharge () {
        // Recharge
        this.$router.push('/recharge')
      },
      toCash () {
        // Withdrawal
        this.$router.push('/cash')
      },
      async toRegister () {
        // 登出登陸
        //this.clearCookie()
        let data = await api.logout()
        if (data.status === 0) {
          // Toast(data.msg)
          this.$router.push('/login')
        } else {
          Toast(data.msg)
        }
      },
      tosetting () {
        this.$router.push('/setting')
      },
      toCashList () {
        this.$router.push('/Cashlist')
      },
      toRechargeList () {
        this.$router.push('/rechargelist')
      },
      toTransfer (val) {
        this.$router.push({
          path: '/transfer',
          query: {
            type: val
          }
        })
      },
      async getCardDetail () {
        // 獲取金融帳戶信息
        let data = await api.getBankCard()
        if (data.status === 0) {
          this.$store.state.bankInfo = data.data
        } else {
          // Toast(data.msg)
        }
      },
      async getSettingInfo () {
        let data = await api.getSetting()
        if (data.status === 0) {
          // 成功
          this.settingInfo = data.data
        } else {
          Toast(data.msg)
        }
      },
      async getIndexSettingInfo () {
        // 網站設定信息 指數
        let data = await api.getIndexSetting()
        if (data.status === 0) {
          // 成功
          this.indexSettingInfo = data.data
        } else {
          Toast(data.msg)
        }
      },
      async getFuturesSetting () {
        // 網站設定信息 期貨
        let data = await api.getFuturesSetting()
        if (data.status === 0) {
          // 成功
          this.futuresSettingInfo = data.data
        } else {
          Toast(data.msg)
        }
      },
      async getUserInfo () {
        // 獲取用戶信息
        //   let showcookie = this.getCookie('USER_TOKEN');
        let data = await api.getUserInfo()
        if (data.status === 0) {
          this.getProductSetting()
          this.$store.state.userInfo = data.data
        } else {
          Toast(data.msg)
        }
        // this.$store.state.user = this.user
      }
    }
  }
  </script>

  <style lang="less" scoped>
    // @bgColor: #fff;
    @bgColor: #16171d;
    @fontColor: #fff;
    @borderColor: #676b6f;
    body {
      background: #000
    }

    .page .head {
      width: 100%;
      // height: 0;
      // padding-top: 44%;
      height: 2.8rem;
      background-image: url('../../assets/img/header.png');
      background-size: 100% 100%;

    }
    .wrapper {
      background-color: @bgColor;
    }
    .after {
      .iconfont {
        vertical-align: middle;
        margin-right: 0.15rem;
      }
    }

    .setting {
      margin-right: 0.2rem;
    }

    .status {
      font-size: 0.24rem;
      // margin-left: 0.2rem;
      .iconfont {
        margin-left: 0.2rem;
        font-size: 0.24rem;
      }
    }

    .user {
      .user-top {
        padding: 0 0.4rem;
        // width: 96%;
        // height: 1.96rem;
        margin: 0 auto;
        background: #2e3237;
        // border-radius: 0.11rem;
        box-shadow: 0.014rem 0.014rem 0.014rem rgba(103, 107, 111, 0.38);
        // margin-top: -0.945rem;
        position: relative;
        margin-bottom: .16rem;
        padding-bottom: 0.2rem;
        margin-top: 0.1rem;

        .user-header {
          width: 1.4rem;
          height: 1.4rem;
          border-radius: 100%;
          background: #000;
          position: absolute;
          left: 50%;
          top: -0.68rem;
          margin-left: -0.68rem;

          .green {
            color: green;
          }
        }

        .user-img {
          width: 1.3rem;
          height: 1.3rem;
          background-color: @bgColor;
          border-radius: 100%;
          margin: 0 auto;
          margin-top: 0.014rem;

          img {
            width: 100%;
            height: 100%;
          }
        }

        .user-box {
          // padding-top: 1.08rem;
          font-size: 0.33rem;
          color: @fontColor;
          font-weight: 700;
          height: 0.68rem;
          line-height: 0.695rem;

          .btn-red {
            // width: 1.418rem;
            width: 2.418rem;
            height: 0.68rem;
            font-size: 0.29rem;
            line-height: 0.68rem;
            padding: 0;
            border-radius: 0.028rem;
            background: #b60c0d;
          }
        }
      }
    }

    .other {
      margin: 0.12rem .28rem;

      ul {
        position: relative;
        background-color: #1F2636;
        border-radius: 4px;
        overflow: hidden;
          padding: 0 0.2rem ;
        li {
          position: relative;
          height: 0.92rem;
          line-height: 0.92rem;
          // padding: 0 0.4rem;
          font-size: 0.26rem;
          background-color: #1F2636;
          position: relative;
          border-bottom: 1px solid #3f444a;
        }
        li:last-child {
          border-bottom: none;
        }
      }

      .after {
        margin-bottom: 0.125rem;
      }

    }
    .renzhen{
      position: relative;
      padding: 0 .1rem;
      font-size: .18rem;
      height: .35rem;
      line-height: .35rem;
      display: inline-block;
      vertical-align: middle;
      padding-left: .3rem;
      .iconfont{
        font-size: .18rem;
        vertical-align: middle;
        position: absolute;
        left: 0.05rem ;
        top: .05rem;
      }
      &.ing {
        
      }
      &.done {
        background-color: #04823E;
      }
    }
    .other ul li svg {
      width: 16px;
      height: 15px;
      line-height: 25px;
      margin-top: .28rem;
      color: #ccc;
    }

    .other ul li svg:first {
      float: left;
      margin-right: 0.39rem;
    }

    .my-Assets {
      .img-box {
        width: 1.3rem;
        height: 1.3rem;
      }

      .assets-box {
        line-height: 0.5rem;
        margin-top: 0.2rem;

        .iconfont {
          margin-right: 0.2rem;
        }
      }

      font-size: 0.25rem;
      padding: 0.2rem 0 0.3rem;
    }

    .user-header {
      padding: 0rem 0.3rem 0;
      background: #2e3237;
      margin-bottom: 0.15rem;

      .box:nth-child(1) {
        margin-right: 6%;
      }

      .box {
        padding: 0.26rem 0.25rem 0.3rem;
        padding: 0.26rem 0.25rem 0.2rem;
        background-color: #c6c8d4;
        width: 47%;
        float: left;
        color: #2f2f2f;
        border-radius: 0.2rem 0.2rem 0.1rem 0.1rem;

        .name {
          font-size: 0.26rem;
          font-weight: bold;
          margin-bottom: 0.2rem;
        }

        .account {
          font-size: 0.3rem;
          font-weight: bold;
        }

        .name2 {
          font-size: 0.23rem;
          margin-top: 0.2rem;

          span {
            padding-left: 0.1rem;
          }
        }
      }
    }

    .loginout {
      color: #999;
      font-size: 0.3rem;
      background: none;
    }

    .btnbox .btnok {
      background: none;
      font-size: 0.28rem;
      height: 0.6rem;
      line-height: 0.6rem;
      color: #606060;
    }

    .btnbox {
      width: 100%;
      padding: 0 0.3rem;
    }

    .mint-popup-1 {
      color: #333;
      width: 200px;
      border-radius: 8px;
      padding: 10px;
      transform: translate(-50%, 0);

      h1 {
        font-size: 20px;
        color: #26a2ff;
      }

      p {
        margin-bottom: 10px;
      }

      top: 3.2rem;
    }

    .mint-popup-1::before {
      triangle: 10px top #fff;
      content: '';
      position: absolute;
      top: -20px;
      right: 50px;
    }

    .mint-popup-white {
      padding: 0.3rem 0.28rem;

      .font-title {
        font-size: 0.26rem;
        margin-bottom: 0.12rem;
      }

      .font-bold {
        font-weight: bold;
      }

      .number {
        margin: 0 0.1rem;
        font-weight: bold;
        font-size: 0.26rem;
      }

      p {
        line-height: 0.4rem;
      }
    }

    // 總資產
    .account-header {
      padding: 0.2rem 0.2rem 0;
      position: relative;
      background-color: #1F2636;

      .iconfont {
        position: absolute;
        right: 0.2rem;
        // top: 50%;
        top: 1.1rem;
        font-size: 0.8rem;
        color: #FF5722;
        margin-top: -0.4rem;

        .btn-red {
          margin-left: 0.2rem;
          padding: 0.08rem 0.2rem;
          background: #FF5722;
        }
      }

      .title {
        font-size: 0.28rem;
        margin-bottom: 0.26rem;
      }

      .sub-title {
        color: #7e8c8d;
        font-size: 0.2rem;

        i {
          font-style: normal;
        }
      }

      .account {
        color: #b60c0d;
        font-size: 0.4rem;
        font-weight: 600;
        font-family: lightnumber;
      }
    }

    .account-box {
      // margin-bottom: 0.12rem;
      padding: 0 0.2rem;
      // padding: 0 0.35rem;
      background-color: #1F2636;
      .header {
        font-size: 0.22rem;
        line-height: 0.7rem;
        height: .7rem;
        display: flex;
        justify-content: space-between;
        .header-left{
          display: flex;
          align-items: center;
          justify-content: space-between;
        }
        img.iconfont {
          color: #f5ca07;
          width: .3rem;
          height: .3rem;
          display: inline-block;
          margin-right: .2rem;
          &.jian {
            background-image: url(../../assets/ico/jian.png);
          }
        }

        .iconfont {
          font-size: 0.24rem;
          vertical-align: middle;
          margin: 0 0.05rem;
        }
      }

      .content {
        padding: 0 0.2rem 0.18rem;

        li {
          width: 50%;
          float: left;
          padding: 0.2rem 0.2rem 0.04rem 0.56rem;
          line-height: 0.36rem;
          position: relative;

          &:nth-child(2) {
            .iconfont {
              color: #2f97fc;
            }
          }

          &:nth-child(3) {
            .iconfont {
              color: #17b780;
            }
          }

          &:nth-child(3) {
            .iconfont {
              color: #ff7602;
            }
          }

          .iconfont {
            position: absolute;
            top: 0.38rem;
            left: 0;
            font-size: 0.42rem;
            color: #fd4256;

          }

          .name {
            color: #888;
            font-size: 0.22rem;
          }

          .number {
            font-size: 0.27rem;
          }

        }
      }
    }

    .mt-progress {
      height: 5px;
      line-height: 5px;
      margin-top: 0.3rem;
      border-radius: 0.2rem;
      // padding: 0 0.2rem;
      /deep/ .mt-progress-runway {
        border-radius: 0.2rem;
        background-color: #ff7602;
      }

      /deep/ .mt-progress-progress {
        border-radius: 0.2rem;
        background-color: #bb3d4c;
      }
    }

    .panel {
      margin: 0.2rem .28rem;
      padding: 0 0.2rem;
      background-color: #1F2636;

      .panel-head {
        height: 0.88rem;
        line-height: 0.88rem;
        padding-top: .1rem;
        text-align: center;
        font-weight: bold;
        .font-w {
          font-size: 0.28rem;
          // font-weight: 600;
          // color: #000;
        }
      }

      .panel-body {
        padding: 0 0.2rem;
        text-align: center;

        .iconfont {
          display: block;
          font-size: 20px;
          margin-bottom: 0.1rem;
          color: #138DB2;
        }

        .font {
          font-size: 0.3rem;
          color: #000;
          font-weight: 600;
          line-height: 0.5rem;
        }

        .col-xs-3 {
          padding: 0.2rem 0;
        }
      }
    }

    .account-info {
      padding: .28rem;
      display: flex;
      justify-content: space-between;
      &_avatar {
        width: 1.13rem;
        height: 1.13rem;
        line-height: 1.13rem;
        border-radius: 50%;
        background-color: #444656;
        display: flex;
        align-items: center;
        justify-content: center;
        img {
          width: .55rem;
          height: .58rem;
        }
      }
      &_detail {
        padding-left: .22rem;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        flex: 1;
        .account-phone{
          font-size: .3rem;
          color: #fff;
        }
        .account-name {
          color: #636A93;
        }
      }
      &_ctl {
        margin-top: .1rem;
      }
    }
    .account-container {
      // margin: .28rem;
      width: 6.9rem;
      margin: .28rem auto;
      border-radius: 5px;
      overflow: hidden;
      background-color: #1F2636;
      .pcx {
        margin: .2rem;
        // background-color: #1F2636;
        background:#1A1E29
      }
      .account-preview {
        display: flex;
        padding: .3rem .2rem .28rem;
        .acc-pre-left {
          width: 1.92rem;
          height: 1.62rem;
          background-image: url(../../assets/ico/round.png);
          background-size: cover;
          display: flex;
          flex-direction: column;
          justify-content: center;
          text-align: center;
          align-items: center;
          position: relative;
          img {
            width:.36rem;
            height: .36rem;
            margin-bottom: .1rem;
          }
          span.ti {
            font-size: .24rem;
            color: #ccc;
            margin-bottom: .1rem;
          }
          span.de {
            font-size: .24rem;
            color: #009C46;
            font-family: lightnumber;
            .account {
              font-family: lightnumber;
            }
          }
          &::after {
            display: block;
            content : "";
            width: .67rem;
            height: 2.21rem;
            position: absolute;
            top: -.16rem;
            right: -.44rem;
            background-image: url(../../assets/ico/round-r.png);
            background-size: cover;
          }
        }
        .acc-pre-center {
          margin-left: .45rem;
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          font-family: lightnumber;
          color: #ccc;
          span {
            color: #fff;
            font-family: lightnumber;
            font-weight: 300;
          }
        }
        .acc-pre-right {
          flex: 1;
          align-items: flex-end;
          display: flex;
          flex-direction: column;
          justify-content: space-around;

          .btn {
            color: #fff;
            padding: 0 10px;
            border-radius: .2rem;
          }
          .redbtn {
            background-color: #E6003E;
          }
          .bluebtn {
            background-color: #024DA1;
          }
        }
      }
    }

    .red-theme {
      .wrapper {
        background-color: #E9E9E9;
      }
      .account-info_avatar{
        background-color: #B5B5B5;
      }
      .account-phone{
        color: #000;
      }
      .account-name {
        color: #999999;
      }
      .account-info_ctl{
        color: #858585;
      }
      .account-container{
        background-color: #fff;
        span.ti{
          color: #000 !important;
        }
      }
    .acc-pre-center{
      display: flex;flex-direction: column;justify-content: space-between;
      color: #333 !important;
      span {
        color: #333 !important;
      }
    }
    .acc-pre-right {
      .bluebtn {
        background-color: #222 !important;
      }
    }
    .account-box{
      background-color: #fff;
    }
    .name {
      color : #12354E !important;
    }
    .number {
      color: #E6003E !important;
    }
    .pcx {
      background-color: #E7E7E8;
      div {
        background-color: transparent !important;
        color: #000;
      }
    }
    .panel {
      background-color: #fff;
      color: #000;
      .panel-head {
        border-bottom-color: #cbcbcb;
      }
      .iconfont {
        color: #E6003E;
      }
      .font-w {
        color: #000;
      }
    }
    .other {
      .after{
        background-color: #fff;
        li{
          background-color: #fff;
          color: #000;
        border-bottom-color: #cbcbcb;
          .renzhen {
            color: #fff;
          }
          svg{
            color: #222;
          }
        }
      }
    }
    }
  </style>
