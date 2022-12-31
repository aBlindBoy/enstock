<template>
    <el-container class="user-center ">
      <el-header class="user-header">
        <home-header></home-header>
      </el-header>
  
      <div class="usercot">
        <el-container class="main-wrapper">
          <el-aside width="178px">
            <menu-box></menu-box>
          </el-aside>
          <el-main style=" min-height: calc(100vh - 150px );">
            <div class="user-center-title" style="text-align:left;margin-top: 30px;">
              <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
              资金兑换
            </div>
              <div class="transfer" style="display: flex;width: 100%;justify-content: center;margin-top:100px;" >
                <el-input v-model="fromAmount" style="width: 30%;" @input="inputFrom" >
                  <template slot="append" style="width: 100px;">
                    <el-select v-model="formCoinCode" style="width: 100px;" @change="formCoinCodeChange">
                        <el-option
                          v-for="item in fromOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                  </template>
                </el-input>
              </div>
              <div style="display: flex;width: 100%;justify-content: center;margin-top: 20px;">
                <i class="el-icon-refresh " :class="clickIconFlag?'clickIcon':''" style="font-size: 30px;" @click="changeFromCoin"></i>
              </div>
              <div style="display: flex;width: 100%;justify-content: center;margin-top: 20px;">
                <el-input v-model="toAmount"  style="width: 30%;" disabled>
                  <template slot="append" style="width: 100px;">
                    <el-select v-model="toCoinCode" style="width: 100px;" >
                        <el-option
                          v-for="item in toOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                  </template>
                </el-input>
              </div>
              <div style="display: flex;width: 100%;justify-content: center;margin-top: 20px;">
                <div>
                  <p style="line-height: 30px;">美股可用资金 <span style="font-size: 20px;">{{ $store.state.userInfo.enableAmt }}</span></p>
                  <p  style="line-height: 30px;">台股可用资金 <span style="font-size: 20px;">{{ $store.state.userInfo.twEnableAmt }}</span></p>
                  <el-button style="width:145px;margin-top: 50px;"  size="small" type="primary" round @click="submit()">确认兑换</el-button>
                </div>
              </div>

              <div class="chongzhi-bizhi">
                <div class="chongzhi-bizhi-cont">
                  <div class="youyi">友情提示:</div>
                  <div class="chongzhi-item">
                    <span class="circle">1</span>
                    <span>当前美元和新台币的汇率 <span>1:{{exchangeRate.defaultRate}}</span> </span>
                  </div>
                  <div class="chongzhi-item">
                    <span class="circle">2</span>
                    <span>资金兑换,免手续费,立即到账</span>
                  </div>
                  <div class="chongzhi-item">
                    <span class="circle">3</span>
                    <span>兑换成功转入到对应的股票账户</span>
                  </div>
                </div>
                <div class="right">

                </div>
              </div>
          </el-main>
        </el-container>
      </div>
    </el-container>
  </template>
  
  <script>
  import HomeHeader from "../../../../components/HeaderOrder";
  import MenuBox from "../menu";
  import * as api from "../../../../axios/api";
  
  export default {
    components: {
      HomeHeader,
      MenuBox,
    },
    props: {},
    data() {
      return {
        fromOptions: [{
          value: 'USD',
          label: 'USD'
        },{
          value: 'TWD',
          label: 'NT$'
        }],
        toOptions:[
          {
            value: 'USD',
            label: 'USD'
          }
        ],
        formCoinCode: 'TWD',
        toCoinCode:'USD',
        fromAmount:0,
        toAmount:0,
        exchangeRate:{},
        clickIconFlag:false,
      };
    },
    watch: {},
    computed: {},
    created() {
      this.getExchangeRate()
    },
    beforeDestroy() {},
    mounted() {
      this.$store.state.userMenu = "2-12";
    },
    methods: {
      async getUserInfo() {
        // 獲取用戶信息
        let data = await api.getUserInfo();
        if (data.status === 0) {
          // 判斷是否登入
          this.$store.state.userInfo = data.data;
        } else {
          this.haslogin = false;
          this.$store.state.haslogin = false;
        }
      },
      async getExchangeRate(){
        let params={
          coinCode:"TWD"
        }
        let data = await api.getExchangeRate(params);
        if (data.status === 0) {
          this.exchangeRate  = data.data;
        } 
      },
      inputFrom(){
        if (this.formCoinCode=="USD") {
          this.toAmount =(this.fromAmount * this.exchangeRate.defaultRate).toFixed(2)
        } else {
          this.toAmount =(this.fromAmount / this.exchangeRate.defaultRate).toFixed(2)
        }
      },
      changeFromCoin(){
          this.clickIconFlag = true
          setTimeout(()=>{
            this.clickIconFlag = false
          },1000)
          let formCoinCode = this.formCoinCode 
          let toCoinCode =   this.toCoinCode 
          this.formCoinCode = toCoinCode
          this.toCoinCode = formCoinCode
          this.inputFrom()
      },
      formCoinCodeChange(formCoinCode){
        this.formCoinCode  = formCoinCode;
        this.toCoinCode = formCoinCode=="TWD"?"USD":"TWD"
        this.inputFrom()
      },
      async submit(){
        let params ={
          fromCode:this.formCoinCode,
          fromAmount:this.fromAmount,
          toCode:this.toCoinCode
        }
        let data = await api.transfer(params);
        if (data.status === 0) {  
          this.getUserInfo()
          this.$message.success(data.data);
        } else {
          this.$message.error(data.data);

        }
      }
  
    },
  };
  </script>
  <style lang="less" >

  .el-input__inner{
     font-size: 26px;
    }
    .el-input--suffix .el-input__inner{
      font-size: 16px;
    }

  .clickIcon{
      animation:fadenum 1s infinite;
  }
  @keyframes fadenum{
    100%{transform:rotate(180deg);}
  }
  .black-bg {
      .el-input__inner{
      background: none;
      border: 1px solid #244C6E;
      color: #ccc;
    }
    .el-input.is-disabled .el-input__inner{
      background: none;
      border: 1px solid #244C6E;
      color: #ccc;
    }
    .el-input-group__append{
      background: none;
      border: 1px solid #244C6E;
      color: #ccc;
    }
    // .el-select-dropdown__item.hover{
    //   background: #244c6e80
    // }
    // .el-select-dropdown{
    //   background: none;
    //   border: 1px solid #244C6E;
    //   color: #ccc;
    // }

  .chongzhi-bizhi {
    .right {
      width: 106px;
      height: 134px;

      background: url("../../../../assets/image/youqingtishi1.png") no-repeat;
    }
    margin-top: 50px;

    background-color: #191e2b;
    display: flex;
    padding: 40px;
    justify-content: space-between;
    .chongzhi-bizhi-cont .chongzhi-item {
      margin: 15px 0;
      color: #7e8086;
      .circle {
        display: inline-block;
        width: 15px;
        text-align: center;
        height: 15px;
        border: 1px solid rgb(121, 121, 121);
        border-radius: 50%;
        line-height: 15px;
      }
    }
  }
  .chongzhi-cont {
    position: relative;
    .left {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }
    .xianshi-cont {
      display: flex;
      justify-content: space-between;
    }

    .chongzhi-img {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .chongzhi-type {
      display: flex;
      margin-top: 20px;
      margin-bottom: 20px;
      .chongzhi-type-item {
        margin-right: 20px;
      }
    }
    .chongzhi-type > span {
      display: inline-block;
      width: 85px;
    }
    .chongzhi-btn-cont {
      display: flex;
      justify-content: center;
      margin-top: 10px;
      .chongzhi-btn {
        width: 80%;
        border-radius: 50px;
        text-align: center;
        height: 40px;
        background-color: #c11815;
        color: #fff;
        line-height: 40px;
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        margin-top: 20px;
      }
    }
  }
    }
   

  .red-bg {
      .chongzhi-bizhi {
    .right {
      width: 106px;
      height: 134px;

      background: url("../../../../assets/image/youqingtishi1.png") no-repeat;
    }
    margin-top: 50px;

    background-color: #E5E5E5;
    display: flex;
    padding: 40px;
    justify-content: space-between;
    .chongzhi-bizhi-cont .chongzhi-item {
      margin: 15px 0;
      color: #000;
      .circle {
        display: inline-block;
        width: 15px;
        text-align: center;
        height: 15px;
        border: 1px solid rgb(121, 121, 121);
        border-radius: 50%;
        line-height: 15px;
      }
    }
  }
  .chongzhi-cont {
    position: relative;
    .left {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }
    .xianshi-cont {
      display: flex;
      justify-content: space-between;
    }

    .chongzhi-img {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .chongzhi-type {
      display: flex;
      margin-top: 20px;
      margin-bottom: 20px;
      .chongzhi-type-item {
        margin-right: 20px;
      }
    }
    .chongzhi-type > span {
      display: inline-block;
      width: 85px;
    }
    .chongzhi-btn-cont {
      display: flex;
      justify-content: center;
      margin-top: 10px;
      .chongzhi-btn {
        width: 80%;
        border-radius: 50px;
        text-align: center;
        height: 40px;
        background-color: #c11815;
        color: #fff;
        line-height: 40px;
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        margin-top: 20px;
      }
    }
  }
    
  }
 
  </style>
  