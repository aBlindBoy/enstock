<template>
  <div class="wrapper">
    <div class="header">
      <!-- <a href="/homes" class="nav-left pull-left">
          <img :src="siteInfo.siteLogo" alt="">
      </a>-->
	  <el-header>
		  <home-header></home-header>
	  </el-header>
      
    </div>
    <div class="loginbg-box">
      <!-- <img class="login-img" src="../../assets/image/login.jpeg" alt=""> -->
    </div>
    <div class="login-box">
      <div class="form-box top-box">
        <p class="animated fadeInDown">{{$t('login.title')}}</p>
      <p class="prompt animated fadeInUp">{{$t('login.subTitle')}}</p>
      </div>
      <div class="form-box">
        <h2 style="text-align:center">{{$t('common.login')}}</h2>
        <el-form
          :hide-required-asterisk="true"
          :model="form"
          ref="ruleForm"
          :rules="rule"
          class="demo-form-inline"
        >
          <el-form-item label prop="phone">
            <el-input
              maxlength="24"
              max="24"
              type="text"
              class="user-phone"
              v-model="form.phone"
              :placeholder="$t('login.phonePlaceholder')"
            >
              <i slot="prepend" class="iconfont icon-user1" style="font-size:13px"></i>
            </el-input>
          </el-form-item>
          <el-form-item label prop="pwd">
            <el-input type="password"  class="user-phone" v-model="form.pwd" 
             :placeholder="$t('login.passwordPlaceholder')">
              <i slot="prepend" class="iconfont icon-zu" style="font-size: 15px;"></i>
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
            class="box-btn"
            type="primary"
            :loading="islogin"
            @click="checkPhone('ruleForm')"
          >{{$t('login.loginImmediately')}}</el-button>
        </div>
        <div class="text-right clearfix">
          <div class="pull-left">
            <a @click="toForget">{{$t('login.forgetPassword')}}?</a>
          </div>
          <div class="pull-right">
            {{$t('login.noAccount')}} ?
            <a @click="toRegister">{{$t('login.signUp')}} </a>
          </div>
        </div>
      </div>
    </div>
    <div></div>
    <newFooter></newFooter>
    <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
  </div>
</template>

<script>
import * as api from "../../axios/api";
// import HomeFooter from "../../components/Footer";
import HomeHeader from "../../components/HeaderOrder";
import newFooter from '@/components/newFooter'
import cookies from 'vue-cookies'

export default {
  components: {
    // HomeFooter,
    HomeHeader,
    newFooter
  },
  props: {},
  data() {
    let validatePass = (rule, value, callback) => {

      if (value === "") {
        callback(new Error("Please enter your mobile number or email"));
      } 
      callback()
    };
    return {
      islogin: false,
      siteInfo: {},
      form: {
        phone: "",
        pwd: "",
      },
      rule: {
        phone: [
          {
            required: true,
            validator: validatePass,
            message: this.$t('login.phonePlaceholder'),
            trigger: "blur",
          },
        ],
        pwd: [{ required: true, message:  this.$t('login.passwordPlaceholder'), trigger: "blur" }],
      },
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {
    this.getInfoSite();
  },
  methods: {
    async getInfoSite() {
      // 获取网站信息
      let result = await api.getInfoSite();
      if (result.status === 0) {
        this.siteInfo = result.data;
        this.$store.state.siteInfo = this.siteInfo;
      } else {
        this.$message.error(result.msg);
      }
    },
    async checkPhone(val) {
      // 先验证是否已经注册
      let data = await api.checkPhone({ phoneNum: this.form.phone });
      if (data.status === 0) {
        // 如果用户已存在返回 0
        this.submit(val);
      } else {
        this.$message.success(this.$t('login.passwordPlaceholder'));
        this.$router.push("/register");
      }
    },
    submit(formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            phone: this.form.phone,
            userPwd: this.form.pwd,
          };
          this.islogin = true;
          let data = await api.login(opts);
          if (data.status === 0) {
            this.$store.state.userInfo.phone = this.phone;
            // window.localStorage.setItem('phone',this.form.phone)
            // setLocalstorage('phone',this.form.phone)
            this.$store.state.haslogin = true;
            this.$message.success(data.msg);
            cookies.set(data.data.key,data.data.token)
            // this.$router.push('/home')
            this.toTransform();
          } else {
            this.$message.error(data.msg);
          }
          this.islogin = false;
        } else {
          return false;
        }
      });
    },
    async toTransform() {
      // let opt = {
      //   pageNum: 1,
      //   pageSize: 1,
      //   stockPlate:'上市'
      // };
      // this.loading = true;
      // let data = await api.getTwStockPageList(opt);
      // let opt = {
			// 		pageNum: 1,
			// 		pageSize: 1
			// 	};
			// 	this.loading = true;
				// let data = await api.getStock(opt);
      // if (data.status === 0) {
        this.$router.push({
          path: "/transaction",
          query: {
            code: "A",
            marketType:"usa"
          },
        });
      // } else {
      //   this.$message.error(data.msg);
      // }
    },
    toRegister() {
      // 去注册
      this.$store.state.loginIsShow = false;
      this.$router.push("/register");
    },
    toForget() {
      this.$router.push("/forget");
    },
  },
};
</script>
<style lang="less" scoped>
.login-box .text-right[data-v-23b3be22]{
      margin: 25px 0 22px;
}
.login-box .form-box[data-v-23b3be22]{
  
    height: 400px;
    border-radius: 5px;
}
.black-bg {
  .user-phone {
    background-color: #2f2f2f;
    border-radius: 50px;
  }
  .el-button--primary{
    background-color: rgb(2, 77, 161) !important;
    border: none;
    border-radius: 50px;
  }
}
.red-bg {
  .user-phone {

    border: 1px solid rgb(150, 150, 150);
    border-radius: 50px;
  }
  .el-button--primary{
    border: none;
    border-radius: 50px;
  }
}
/deep/ .user-phone input {
  border: none;
}
/deep/ .login-box .el-input-group__prepend {
  left: 12px;
}
.header {
  height: 60px;
  // background: #fff;
  img {
    height: 58px;
    line-height: 58px;
  }
}

.el-form-item {
  margin-bottom: 40px;
}

.login-box {
  width: 80%;
  margin: 0 auto;
  height: 540px;
  position: relative;

  .form-box {
    width: 320px;
    height: 480px;
    text-align: center;
    position: absolute;
    right: 0;
    top: 32px;
    // background: #fff;
    color: #fff;
    background: rgba(3, 3, 3, 0.6);
    z-index: 2;
    padding: 0 40px;
    border-radius: 4px;

    h2 {
      height: 30px;
      line-height: 30px;
      text-align: left;
      font-size: 26px;
      padding-top: 30px;
    }

    .el-form {
      padding-top: 50px;
    }
  }

  .dialog-footer {
    margin-top: 20px;
  }

  .text-right {
    margin: 50px 0 25px;
  }

  /deep/ .el-input__inner {
    height: 40px;
    line-height: 40px;
    text-indent: 30px;
  }

  .login-box .iconfont {
    font-size: 28px;
  }

  /deep/ .el-form-item__error {
    padding-top: 12px;
  }
}

.header {
  // padding: 0 20px;
}

.login-box .top-box {
  position: absolute;
  top: 34%;
  width: 50%;
  height: 150px;
  left: 0;
  right: auto;
  background: none;

  .prompt {
    color: #b6b6b6;
    font-size: 18px;
  }

  p {
    color: #efbb53;
    font-size: 34px;
    margin-bottom: 40px;
  }
}
</style>
