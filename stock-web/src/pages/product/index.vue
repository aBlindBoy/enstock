<template>
  <el-container class="product">
    <el-header>
      <home-header></home-header>
    </el-header>
    <!-- <el-container class=""> -->
        <backdrop1>
          <slot>
            <div class="bancot">
              <div class="description">
                <div class="tit">
                  <!-- <span class="zh">產品說明</span> -->
                  <span class="en">PRODUCT DESCRIPTION</span></div>
                <div class="text">Our greatest asset is our people,{{siteInfo.siteName}}
                  
                  Hire only those experienced professionals who have demonstrated their top-notch customer service skills and tie-in skills in the financial services industry. Provide professional customer service.
                   Our sales and operations representatives will answer any inquiries from customers in professional Chinese.</div>
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
                  <!-- <div class="not_l" style="width: 35px;overflow: hidden;"><span class="iconfont icon-kaihuhedui"></span>Account opening instructions</div> -->
                  <div class="not_r">
                    <ul>
                      <li>Before opening a real account (registered member), you must carefully read and review the company's "Trading Rules" and "Risk Disclosure and Notification".</li>
                      <li>Account opening requirements for customers: Citizens over the age of 18 with full capacity for civil conduct. Customers must open an account in a real and legal identity and ensure the legality of the source of funds.</li>
                      <li>Customers should prepare their own financial accounts for mutual transfer (open online finance), and customers should keep their financial passwords and transaction passwords well, and change the passwords regularly, and must not disclose the passwords to anyone.</li>
                      <li>Register yourself and follow the "One Person One Account Rule".</li>
                      <li>After the official account is successfully recharged, you can start trading.</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div slot="right">
                <div class="s_right">
                  <div class="not_l" style="width: 35px;overflow: hidden;"><span class="iconfont icon-kaihuhedui"></span>操盤須知</div>
                    <div class="not_r">
                    <ul>
                      <li>The principal you invest is the minimum 10000 USD;</li>
                      <li>After applying for funds, buy and sell at any time;</li>
                      <li>Once the accounts are matched, the same day can be traded;</li>
                      <li>When the funds are close to the loss and forced to liquidate the Warning line, the Margin needs to be replenished as soon as possible;</li>
                      <li>When the trading capital is lower than the Closing Line, the system will terminate the operation;</li>
                      <li>A single stock position shall not exceed the total funds in the account 90.00% ;</li>                           
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
                    <div class="i_tit">Target name:</div>
                    <div class="msg">U.S. Stock market margin financing and securities lending sector</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Market trading hours:</div>
                    <div class="msg">
                      <span>Section 1:{{sitesetInfo.transAmBegin}}-{{sitesetInfo.transAmEnd}}</span>
                      <span>Section two:{{sitesetInfo.transPmBegin}}-{{sitesetInfo.transPmEnd}}</span>
                      <p>(The actual open trading time is subject to the trading rules)</p>
                    </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Minimum transaction unit:	</div>
                    <div class="msg">{{(sitesetInfo.buyMinAmt/10000).toFixed(2)}} share</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Maximum transaction unit:</div>
                    <div class="msg">{{(sitesetInfo.buyMaxNum/1000).toFixed(2)}} share</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">means of transaction:</div>
                    <div class="msg">You can buy long, sell short, buy up, buy squat, buy on the same day, close the position on the same day, and earn the swing amplitude spread of the day!</div>
                  </div>
                </div>
                <div class="mid">
                  <div class="item">
                    <div class="i_tit">Transaction Amount:	</div>
                    <div class="msg">Using financing and securities lending, buying up and down by buying and selling</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Minimum purchase amount:	</div>
                    <div class="msg">{{sitesetInfo.buyMinAmt}} USD</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Handling fee：		</div>
                    <div class="msg">Charged when buying and selling, based on the total transaction price * {{sitesetInfo.buyFee}} charge</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Stamp duty：		</div>
                    <div class="msg">Charged when buying, based on the total transaction price* {{sitesetInfo.dutyFee}} charge</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Storage fee：		</div>
                    <div class="msg">If the transaction is not closed before the market close, the transaction will be recorded as a reserve order after the market close, and the closing price of the stock * the number of shares remaining will be charged * {{sitesetInfo.stayFee}} Storage fee,(Holidays are also charged)</div>
                  </div>
                </div>
                <div class="right">
                  <div class="item">
                    <div class="i_tit">maximum Days to stay：		</div>
                    <div class="msg">{{sitesetInfo.stayMaxDays}} day</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Withdrawal rules:	</div>
                    <div class="msg">Currently there is a position that cannot be withdrawn, the minimum withdrawal amount is {{sitesetInfo.withMinAmt}} USD</div>
                  </div>
                  <div class="item">
                    <div class="i_tit">Withdrawal time:			</div>
                    <div class="msg">Withdrawal time is  {{sitesetInfo.withTimeBegin}} :00 - {{sitesetInfo.withTimeEnd}} : 00 </div>
                  </div>
                  <div class="item">
                    <div class="i_tit">jet lag：		</div>
                    <div class="msg">Summer: 0 hours; Winter: 0 hours</div>
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
