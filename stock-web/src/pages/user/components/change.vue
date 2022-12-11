<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot change">
      <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main style=" min-height: calc(100vh - 150px );">
        <div class="wrapper">
          <div class="box page-part transaction">
            <div class="box-contain clearfix">
              <div class="text-center">
                <i style="font-size: 1.5rem;" class="maincolor-font iconfont icon-jiaoyi"></i>
              </div>
              <el-card v-if="$store.state.productSetting.indexDisplay || $store.state.userInfo.accountType === 1"
                       class="box-card" style="margin-top:20px;">
                <!-- <div slot="header" class="clearfix">
                    <span>融資指數互轉</span>
                </div> -->
                <div class="text item">
                  <el-tabs v-model="activeName">
                    <el-tab-pane label="Financing to Index" name="first">
                      <div class="auth-box">
                        <el-alert
                          center
                          :closable="false"
                          style="width:400px;margin:10px auto;"
                          title="Current financing account available balance"
                          type="warning">
                          <span>{{$store.state.userInfo.enableAmt}}</span>
                        </el-alert>
                        <el-form label-width="100px" v-model="form" ref="ruleForm" class="demo-form-inline">
                          <el-form-item label="Transfer amount" prop="number">
                            <el-input type='number' @change="getIntNumber()" v-model="form.account1"
                                      placeholder="Please enter the transfer amount">
                              <el-button @click="selectAll1" slot="append">all</el-button>
                            </el-input>
                            <span class="red">The transfer amount only supports integers</span>
                          </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                          <el-button type="primary" :loading="isloading" @click="Onsubmit(1)">Confirm transfer to index account</el-button>
                        </div>
                      </div>
                    </el-tab-pane>
                    <el-tab-pane label="Index refinancing" name="second">
                      <div class="auth-box">
                        <el-alert
                          center
                          :closable="false"
                          style="width:400px;margin:10px auto;"
                          title="Current Index Account Available Balance"
                          type="warning">
                          <span>{{$store.state.userInfo.enableIndexAmt}}</span>
                        </el-alert>
                        <el-form label-width="100px" v-model="form" ref="ruleForm" class="demo-form-inline">
                          <el-form-item label="Transfer amount" prop="number">
                            <el-input type='number' v-model="form.account2" @change="getIntNumber2"
                                      placeholder="Please enter the transfer amount">
                              <el-button @click="selectAll2" slot="append">all</el-button>
                            </el-input>
                            <span class="red">The transfer amount only supports integers</span>
                          </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                          <el-button type="primary" :loading="isloading" @click="Onsubmit(2)">Confirm transfer to financing account</el-button>
                        </div>
                      </div>
                    </el-tab-pane>
                  </el-tabs>

                </div>
              </el-card>
              <el-card v-if="$store.state.productSetting.futuresDisplay || $store.state.userInfo.accountType === 1"
                       class="box-card" style="margin-top:20px;">
                <!-- <div slot="header" class="clearfix">
                    <span>指數轉融資</span>
                    <span class="red">轉帳金額僅支持整數</span>
                </div> -->
                <div class="text item">
                  <el-tabs v-model="activeName2">
                    <el-tab-pane label="Financing to futures" name="first">
                      <div class="auth-box">
                        <el-alert
                          center
                          :closable="false"
                          style="width:400px;margin:10px auto;"
                          title="Current Index Account Available Balance"
                          type="warning">
                          <span>{{$store.state.userInfo.enableAmt}}</span>
                        </el-alert>
                        <el-form label-width="100px" v-model="form" ref="ruleForm" class="demo-form-inline">
                          <el-form-item label="Transfer amount" prop="number">
                            <el-input type='number' v-model="form.account3" @change="getIntNumber3"
                                      placeholder="Please enter the transfer amount">
                              <el-button @click="selectAll3" slot="append">all</el-button>
                            </el-input>
                            <span class="red">The transfer amount only supports integers</span>
                          </el-form-item>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                          <el-button type="primary" :loading="isloading" @click="Onsubmit(3)">Confirm transfer to futures account</el-button>
                        </div>
                      </div>
                    </el-tab-pane>
                    <el-tab-pane label="Futures refinancing" name="second">
                      <div class="auth-box">
                        <el-alert
                          center
                          :closable="false"
                          style="width:400px;margin:10px auto;"
                          title="Current available balance of futures account"
                          type="warning">
                          <span>{{$store.state.userInfo.enableFuturesAmt}}</span>
                        </el-alert>
                        <el-form label-width="100px" v-model="form" ref="ruleForm" class="demo-form-inline">
                          <el-form-item label="Transfer amount" prop="number">
                            <el-input type='number' v-model="form.account4" @change="getIntNumber4"
                                      placeholder="Please enter the transfer amount">
                              <el-button @click="selectAll4" slot="append">all</el-button>
                            </el-input>
                            <span class="red">The transfer amount only supports integers</span>
                          </el-form-item>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                          <el-button type="primary" :loading="isloading" @click="Onsubmit(4)">Confirm transfer to financing account</el-button>
                        </div>
                      </div>
                    </el-tab-pane>
                  </el-tabs>

                </div>
              </el-card>

            </div>

          </div>

        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
    </div>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../components/HeaderOrder'
  import MenuBox from './menu'
  import * as api from '../../../axios/api'

  export default {
    components: {
      HomeHeader,
      MenuBox
    },
    props: {},
    data () {
      return {
        hasAuth: false,
        isloading: false,
        form: {
          account1: '',
          account2: '',
          account3: '',
          account4: '',
          password: ''
        },
        activeName: 'first',
        activeName2: 'first'
      }
    },
    watch: {},
    computed: {},
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.$store.state.userMenu = 'change'
    },
    methods: {
      async getUserInfo () {
        // 獲取用戶信息
        let data = await api.getUserInfo()
        if (data.status === 0) {
          // 判斷是否登錄
          this.$store.state.userInfo = data.data
        } else {

        }
      },
      getIntNumber () {
        if (this.form.account1 >= this.$store.state.userInfo.enableAmt) {
          this.form.account1 = this.$store.state.userInfo.enableAmt
        }
        this.form.account1 = Math.floor(this.form.account1)
      },
      getIntNumber2 () {
        if (this.form.account2 >= this.$store.state.userInfo.enableIndexAmt) {
          this.form.account2 = this.$store.state.userInfo.enableIndexAmt
        }
        this.form.account2 = Math.floor(this.form.account2)
      },
      getIntNumber3 () {
        if (this.form.account3 >= this.$store.state.userInfo.enableAmt) {
          this.form.account3 = this.$store.state.userInfo.enableAmt
        }
        this.form.account3 = Math.floor(this.form.account3)
      },
      getIntNumber4 () {
        if (this.form.account4 >= this.$store.state.userInfo.enableFuturesAmt) {
          this.form.account4 = this.$store.state.userInfo.enableFuturesAmt
        }
        this.form.account4 = Math.floor(this.form.account4)
      },
      selectAll1 () {
        // 選擇全部
        this.form.account1 = Math.floor(this.$store.state.userInfo.enableAmt)
      },
      selectAll2 () {
        // 選擇全部
        this.form.account2 = Math.floor(this.$store.state.userInfo.enableIndexAmt)
      },
      selectAll3 () {
        // 選擇全部
        this.form.account3 = Math.floor(this.$store.state.userInfo.enableAmt)
      },
      selectAll4 () {
        // 選擇全部
        this.form.account4 = Math.floor(this.$store.state.userInfo.enableFuturesAmt)
      },
      async getSettingInfo () {
        // 網站設置信息
        let data = await api.getSetting()
        if (data.status === 0) {
          // 成功
          this.settingInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      async Onsubmit (type) {
        // 融資轉指數
        let opt = {
          amt: type === 1 ? this.form.account1 : type === 2 ? this.form.account2 : type === 3 ? this.form.account3 : this.form.account4,
          type: type // 1 融資轉指數 2 指數轉融資
        }
        let data = await api.AmtChange(opt)
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.$router.push('/user')
        } else {
          this.$message.error(data.msg)
        }
      }
    }
  }
</script>
<style lang="less" scoped>
.black-bg .change .el-input-group{
  height: 41px;
}
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
      // width: 400px;
      margin: 20px auto;
    }

    .auth-box {
      .dialog-footer {
        // width: 400px;
        // margin: 0 auto;

        .el-button {
          margin-left: 100px;
          width: 638px;
          border-radius: 22px;
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
</style>
