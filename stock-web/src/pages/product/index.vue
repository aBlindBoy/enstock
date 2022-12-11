<template>
  <el-container class="product">
    <el-header>
      <home-header></home-header>
    </el-header>
        <backdrop1>
          <slot>
            <div class="bancot">
              <div class="description">
                <div class="tit">
                  <span class="en">{{$t('product.title')}}</span></div>
                <div class="text">{{$t('product.productContent')}}</div>
              </div>
            </div>
          </slot>
        </backdrop1>
        <div class="main ">
          <div class="cot ">
            <div class="notice">
              <layout>
              <div slot="left">
                <div class="s_left" >
                  <!-- <div class="not_l" style="width: 35px;overflow: hidden;"><span class="iconfont icon-kaihuhedui"></span></div> -->
                  <div class="not_r">
                    <ul>
                      <li>{{$t('product.openContent1')}}</li>
                      <li>{{$t('product.openContent2')}}</li>
                      <li>{{$t('product.openContent3')}}</li>
                      <li>{{$t('product.openContent4')}}</li>
                      <li>{{$t('product.openContent5')}}</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div slot="right">
                <div class="s_right">
                  <!-- <div class="not_l" style="width: 35px;overflow: hidden;"><span class="iconfont icon-kaihuhedui"></span></div> -->
                    <div class="not_r">
                    <ul>
                      <li>{{$t('product.principalContent1')}}</li>
                      <li>{{$t('product.principalContent2')}}</li>
                      <li>{{$t('product.principalContent3')}}</li>
                      <li>{{$t('product.principalContent4')}}</li>
                      <li>{{$t('product.principalContent5')}}</li>
                      <li>{{$t('product.principalContent6')}}</li>                           
                    </ul>
                    </div>
                  </div>
                </div>
              </layout>
            </div>
            <!-- 股市 -->
            <div class="stock">
              <!-- <div class="tit"><span class="iconfont icon-_gushiloushi"></span> U.S. Stock market</div> -->
              <div class="info">
                <div class="left">
                  <div class="item">
                    <div class="i_tit">{{$t('product.targetName')}}</div>
                    <div class="msg">{{$t('product.targetContent')}}</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.marketTradingHours')}}</div>
                    <div class="msg">
                      <span>{{$t('product.sectionOne')}}{{sitesetInfo.transAmBegin}}-{{sitesetInfo.transAmEnd}}</span>
                      <span>{{$t('product.sectionOne')}} {{sitesetInfo.transPmBegin}}-{{sitesetInfo.transPmEnd}}</span>
                      <p>{{$t('product.marketTradingHoursContent')}}</p>
                    </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.minimumTransactionUnit')}}	</div>
                    <div class="msg">{{(sitesetInfo.buyMinAmt/10000).toFixed(2)}} {{$t('common.shares')}}</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.maximumTransactionUnit')}}</div>
                    <div class="msg">{{(sitesetInfo.buyMaxNum/1000).toFixed(2)}} {{$t('common.shares')}}</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.meansOfTransaction')}}</div>
                    <div class="msg">{{$t('product.meansOfTransactionContent')}}</div>
                  </div>
                </div>
                <div class="mid">
                  <div class="item">
                    <div class="i_tit">{{$t('product.transactionAmount')}}</div>
                    <div class="msg">Using financing and securities lending, buying up and down by buying and selling</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.minimumPurchaseAmount')}}</div>
                    <div class="msg">{{sitesetInfo.buyMinAmt}} USD</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.handlingFee')}}	</div>
                    <div class="msg">{{$t('product.handlingContent')}} * {{sitesetInfo.buyFee}} </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.stampDuty')}}		</div>
                    <div class="msg">{{$t('product.stampDutyContent')}} * {{sitesetInfo.dutyFee}} </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.storagFee')}}		</div>
                    <div class="msg">{{$t('product.storagFee1')}} * {{sitesetInfo.stayFee}}{{$t('product.storagFee2')}}</div>
                  </div>
                </div>
                <div class="right">
                  <div class="item">
                    <div class="i_tit">{{$t('product.maximumDaysToStay')}}	</div>
                    <div class="msg">{{sitesetInfo.stayMaxDays}} {{$t('product.day')}}</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.withdrawalRules')}}</div>
                    <div class="msg">{{$t('product.withdrawalRulesContent')}} {{sitesetInfo.withMinAmt}} USD</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.withdrawalTime')}}	</div>
                    <div class="msg">{{$t('product.withdrawalTimeContent')}} {{sitesetInfo.withTimeBegin}} :00 - {{sitesetInfo.withTimeEnd}} : 00 </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">{{$t('product.jetLag')}}		</div>
                    <div class="msg">{{$t('product.jetLagContent')}}	</div>
                  </div>
                  <!-- <div class="item">
                    <div class="i_tit">數據來源：		</div>
                    <div class="msg">臺灣證券交易所及深訓證券交易所</div>
                  </div> -->
                </div>
              </div>
            </div>
          </div>
        </div>
    <newFooter />
  </el-container>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  import newFooter from '../../components/newFooter'
  // import TableBox from './components/table'
  import * as api from '../../axios/api'
  import backdrop1 from '../../components/backdrop1'
  import layout from '../../components/layout'

  export default {
    components: {
      HomeHeader,
      newFooter,
      backdrop1,
      layout
    },
    props: {},
    data () {
      return {
        siteInfo: {}, // 站点信息
        sitesetInfo: {
          transAmBegin: '',
          transAmEnd: '',
          transPmBegin: '',
          transPmEnd: '',
          buyminnum: '',
          buymaxnum: '',
          buyminamt: '',
          buyfee: '',
          dutyfee: '',
          stayfee: '',
          staymaxday: '',
          begintime: '',
          endtime: '',
          withminamt: ''
        }, // 设置信息
        timer: null
      }
    },
    watch: {},
    computed: {},
    created () {
      // this.timer = setInterval(this.refreshList, 5000)
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getSettingSite()
      this.getInfoSite()
      this.$store.state.activeIndex = 'product'
    },
    methods: {
      async getInfoSite () {
        // 获取站点详情
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
        } else {
          this.$message.error(result.msg)
        }
      },
      async getSettingSite () {
        // 获取站点设置信息
        let result = await api.getSetting()
        if (result.status === 0) {
          this.sitesetInfo = result.data
        } else {
          this.$message.error(result.msg)
        }
      }
    }
  }
</script>
<style lang="less" scoped>
  .introduce {
    padding: 20px;
    min-height: 400px;

    .title {
      font-size: 20px;
      line-height: 38px;
    }

    .introduce-body {
      line-height: 28px;
      padding-top: 30px;
      text-indent: 2em;
      font-size: 14px;
    }
  }

  .table-box {
    .table-title {
      padding: 0;
      color: #cbd3ff;
      width: 894px;
    }
  }

  .panel-box {
    margin: 0 auto;
    margin-top: 35px;
    width: 900px;
    box-shadow: 0.8px 0.6px 15px rgba(0, 0, 0, 0.1);

    .panel-title {
      padding: 6px 0;
      // background: #24252c;
    }

    .panel-body {
      padding: 15px 0;
      // background: #1f2026;
    }
  }
</style>
