<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot auth">
      <el-container class="main-wrapper">
        <el-aside width="200px">
          <menu-box></menu-box>
        </el-aside>
        <el-main style=" min-height: calc(100vh - 150px );">
          <div class="header-chi" style="margin: 15px 10px 10px 10px;">
            <div class="user-center-title">
              <img src="../../../../assets/image/sanjao.png" alt />
              <span>{{$t('auth.title')}}</span>
            </div>
          </div>
          <div class="wrapper">
            <div class="box page-part">
              <div
                v-if="this.$store.state.userInfo.isActive === 1 ||  this.$store.state.userInfo.isActive === 2"
                class="box-contain clearfix"
              >
                <div class="yanzeng-cont">
                  <div class="yanzeng-left">
                    <img src="../../../../assets/image/thingsOk.png" alt />
                  </div>
                  <div class="yanzeng-right">
                    <div>
                      <span>{{$t('auth.actualName')}}:{{$store.state.userInfo.realName}}</span>
                      <span class="tongguo" v-if="$store.state.userInfo.isActive === 2">
                        <span class="iconfont icon-duihao"  ></span>
                        {{$t('auth.examinationPassed')}}
                        </span>
                      <span class="renzheng" v-else>
                        <span class="iconfont icon-quan-cuo"></span>
                        {{$t('auth.notCertified')}}
                      </span>
                    </div>
                    <div>
                      <span>{{$t('auth.idNumber')}}:{{$store.state.userInfo.idCard}}</span>
                    </div>
                    <div class="zfz">
                        <span>{{$t('auth.frontIdCard')}}:</span>

                      <div class="img-auth">
                        <!-- <img src="../../../../assets/image/banner-down.png" alt /> -->

                        <img :src="$store.state.userInfo.img1Key" alt />
                      </div>
                      <span class="fan">{{$t('auth.reverseIdCard')}}:</span>
                      <div >
                        <span class="img-auth">
                          <!-- <img src="../../../../assets/image/banner-down.png" alt /> -->
                          <img :src="$store.state.userInfo.img2Key" alt />
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- <div class="text-center thingsOk">
                  <i
                    v-if="this.$store.state.userInfo.isActive === 1"
                    style="color:red;font-size: 1.5rem;"
                    class="iconfont icon-renzhengzhong"
                  ></i>
                  <img v-else src="../../../../assets/image/thingsOk.png" alt />
                </div>-->
                <!-- <div class="text-center">
                  <i style="color:red;font-size: 1.5rem;" class="iconfont icon-tongguo1"></i>

                </div>-->
                <!-- <div class="auth-box authed-box">
                  <el-row>
                      <el-col>
                      ???????????????{{$store.state.userInfo.realName}}
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col>
                      ???????????????{{$store.state.userInfo.idCard}}
                      </el-col>
                  </el-row>
                </div>-->
                <!-- <el-card class="box-card">
                  <div slot="header" class="clearfix">
                    <span>????????????</span>
                  </div>
                  <div class="text item">
                    <div class="auth-box">
                      <i
                        v-if="$store.state.userInfo.isActive === 2"
                        style="font-size: 2rem;"
                        class="maincolor-font iconfont icon-tongguo1"
                      ></i>
                      <i
                        v-else
                        style="font-size: 2rem;"
                        class="maincolor-font iconfont icon-shenhezhong"
                      ></i>
                      <p>????????????????????????</p>
                    </div>
                    <el-row>
                      <el-col>
                        <span class="name">???????????????</span>
                        <span class="info">{{$store.state.userInfo.realName}}</span>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col>
                        <span class="name">???????????????</span>
                        <span class="info">{{$store.state.userInfo.idCard}}</span>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col>
                        <span class="name">??????????????????</span>
                        <span class="img-auth">
                          <img :src="$store.state.userInfo.img1Key" alt />
                        </span>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col>
                        <span class="name">??????????????????</span>
                        <span class="img-auth">
                          <img :src="$store.state.userInfo.img2Key" alt />
                        </span>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col>
                          <span class="name">??????????????????</span>
                          <span class="img-auth">
                              <img :src="$store.state.userInfo.img3Key" alt="">
                          </span>
                      </el-col>
                    </el-row>
                  </div>
                </el-card>-->
              </div>
              <div
                v-if="this.$store.state.userInfo.isActive === 0 ||  this.$store.state.userInfo.isActive === 3"
                class="box-contain clearfix"
              >
                <!-- <div class="text-center prompt-box">
                <i v-if="this.$store.state.userInfo.isActive === 1" style="color:red;font-size: 1.5rem;"
                   class="iconfont icon-shenhezhong"></i>
                <i v-else style="color:red;font-size: 1.5rem;" class="iconfont icon-icon-test"></i>
                <p>* ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>
                <p>????????????????????????????????????????????????????????????????????????????????????</p>
                </div>-->
                <div class="auth-box chongzhi-cont">
                  <el-form
                    :hide-required-asterisk="true"
                    :model="form"
                    label-width="100px"
                    ref="ruleForm"
                    :rules="rule"
                    class="demo-form-inline"
                  >
                    <el-form-item :label="$t('auth.actualName')" prop="name">
                      <el-input
                        type="text"
                        class="chongzhi-input"
                        v-model="form.name"
                        :placeholder="$t('auth.realNamePlaceholder')"
                      ></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('auth.idNumber')" prop="idCard">
                      <el-input
                        type="text"
                        class="chongzhi-input"
                        v-model="form.idCard"
                        :placeholder="$t('auth.idCardPlaceholder')"
                      ></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('auth.frontIdCard')" prop="idCard">
                      <el-row>
                        <el-col :span="10">
                          <el-upload
                            :with-credentials="true"
                            class="avatar-uploader"
                            :action="admin+'/user/upload.do'"
                            list-type="picture-card"
                            name="upload_file"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload"
                          >
                            <img v-if="form.img1key" :src="form.img1key" class="avatar" />
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                          </el-upload>
                        </el-col>
                        <el-col :span="14">
                          <img src="../../../../assets/image/img1.jpg" alt />
                        </el-col>
                      </el-row>
                    </el-form-item>
                    <el-form-item :label="$t('auth.idCardBack')" prop="idCard">
                      <el-row>
                        <el-col :span="10">
                          <el-upload
                            :with-credentials="true"
                            class="avatar-uploader"
                            list-type="picture-card"
                            name="upload_file"
                            :action="admin+'/user/upload.do'"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess2"
                            :before-upload="beforeAvatarUpload2"
                          >
                            <img v-if="form.img2key" :src="form.img2key" class="avatar" />
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                          </el-upload>
                        </el-col>
                        <el-col :span="14">
                          <img src="../../../../assets/image/img2.jpg" alt />
                        </el-col>
                      </el-row>
                    </el-form-item>
                    <!-- <el-form-item label="???????????????" prop="idCard">
                      <el-row>
                          <el-col :span="10">
                              <el-upload
                                :with-credentials='true'
                                  class="avatar-uploader"
                                  list-type="picture-card"
                                  name="upload_file"
                                  :action="admin+'/user/upload.do'"
                                  :show-file-list="false"
                                  :on-success="handleAvatarSuccess3"
                                  :before-upload="beforeAvatarUpload3">
                                  <img v-if="form.img3key" :src="form.img3key" class="avatar">
                                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                              </el-upload>
                          </el-col>
                          <el-col :span="14">
                              <img src="../../../../assets/image/img3.jpg" alt="">
                          </el-col>
                      </el-row>
                    </el-form-item>-->
                  </el-form>
                  <div slot="footer" class="dialog-footer">
                    <el-button type="primary" :loading="islogin" @click="submit('ruleForm')">Certification</el-button>
                    <!-- <el-button  type="primary" :loading="islogin" @click="submit('ruleForm')">??????</el-button> -->
                  </div>
                </div>
              </div>
              <div class="chongzhi-bizhi">
                <div class="chongzhi-bizhi-cont">
                  <div class="youyi">{{$t('auth.friendshipTips')}}:</div>
                  <div class="chongzhi-item">
                    <span class="circle">1</span>
                    <span>{{$t('auth.friendshipTipsContent1')}}</span>
                  </div>
                  <div class="chongzhi-item">
                    <span class="circle">2</span>
                    <span>{{$t('auth.friendshipTipsContent2')}}</span>
                  </div>
                  <div class="chongzhi-item">
                    <span class="circle">3</span>
                    <span>{{$t('auth.friendshipTipsContent3')}}</span>
                  </div>
                </div>
                <div class="right">

                </div>
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
import HomeHeader from "../../../../components/HeaderOrder";
import MenuBox from "../menu";
import * as api from "../../../../axios/api";
import url from '@/axios/api.url'

export default {
  components: {
    HomeHeader,
    MenuBox,
  },
  props: {},
  data() {
    // let validatePass = (rule, value, callback) => {
    //   if (value === "") {
    //     callback(new Error("????????????????????????"));
    //   } else {
    //     let myreg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; // ???????????????
    //     if (!myreg.test(value)) {
    //       callback(new Error("?????????????????????????????????"));
    //     }
    //     callback();
    //   }
    // };
    let validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t('auth.idCardPlaceholder')));
      }
      callback();
    };
    return {
      hasAuth: false,
      form: {
        name: "",
        idCard: "",
        img1key: "",
        img2key: "",
        img3key: "",
      },
      rule: {
        name: [{ required: true, message: this.$t('auth.realNamePlaceholder'), trigger: "blur" }],
        idCard: [
          {
            required: true,
            validator: validatePass,
            message: this.$t('auth.idCardPlaceholder'),
            trigger: "blur",
          },
        ],
        img1key: [
          { required: true, message: this.$t('auth.idCardUpload'), trigger: "blur" },
        ],
        img2key: [
          { required: true, message: this.$t('auth.idCardUpload'), trigger: "blur" },
        ],
        img3key: [
          { required: false, message:this.$t('auth.idCardUpload'), trigger: "blur" },
        ],
      },
      islogin: false,
      admin: "",
    };
  },
  watch: {},
  computed: {},
  created() {
    if (this.$store.state.userInfo.idCard) {
      this.form.idCard = this.$store.state.userInfo.idCard;
      this.form.name = this.$store.state.userInfo.realName;
      if (this.$store.state.userInfo.isActive === 2) {
        this.hasAuth = true;
      }
    } else {
      this.getUserInfo();
    }
  },
  mounted() {
    this.admin = process.env.API_HOST;
    if (this.admin === undefined) {
      this.admin = url.baseURL;
    }
    this.$store.state.userMenu = "2-9";
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.form.img1key = res.data.url;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error(this.$t('auth.imageFormat'));
      }
      // if (!isLt2M) {
      //     this.$message.error('???????????????????????????????????? 2MB!');
      // }
      return isJPG && isLt2M;
    },
    handleAvatarSuccess2(res, file) {
      this.form.img2key = res.data.url; // URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload2(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      return isJPG && isLt2M;
    },
    handleAvatarSuccess3(res, file) {
      this.form.img3key = res.data.url; // URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload3(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error(this.$t('auth.imageFormat'));
      }
      return isJPG && isLt2M;
    },
    async getUserInfo() {
      // ??????????????????
      let data = await api.getUserInfo();
      if (data.status === 0) {
        // ??????????????????
        this.$store.state.userInfo = data.data;
        if (this.$store.state.userInfo.isActive === 2) {
          this.hasAuth = true;
        }
      } else {
      }
    },
    submit(formName) {
      // ??????
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // ????????????
          let opts = {
            realName: this.form.name,
            idCard: this.form.idCard,
            img1key: this.form.img1key,
            img2key: this.form.img2key,
            img3key: this.form.img3key,
          };
          this.islogin = true;
          let data = await api.userAuth(opts);
          if (data.status === 0) {
            this.$message.success(this.$t('auth.authenticationSucceeded'));
            this.getUserInfo();
          } else {
            this.$message.success(data.msg);
          }
          this.islogin = false;
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style lang="">
</style>
<style lang="less" scoped>
.red-bg {
  .yanzeng-cont {
    display: flex;
      .tongguo,.renzheng{
      border-radius: 3px;
      padding: 2px 5px;
      display: inline-block;
      background-color:rgb(4, 130, 62);
      margin: 0 5px;
      color: #fff;
    }
    .renzheng{
      background-color: rgb(230, 0, 62);
    }
    .yanzeng-left {
      height: 60px;
      width: 60px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .yanzeng-right {
      margin-left: 10px;
    }
    .yanzeng-right > div {
      margin-bottom: 20px;
      // margin-left: 10px;
    }
    .zfz {
      display: flex;
      .fan {
        margin-left: 30px;
      }
    }
  }
  .youyi {
    color: #000;
  }
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

    .chongzhi-input {
      display: flex;
      align-items: center;
      width: 90%;
      background: none;
    }
    .chongzhi-input > span {
      display: inline-block;
      width: 100px;
    }
  }
}
.black-bg {
  .yanzeng-cont {
    display: flex;
      .tongguo,.renzheng{
      padding: 2px 5px;
      display: inline-block;
      background-color:rgb(4, 130, 62);
      border-radius: 2px;
      margin: 0 5px;
    }
    .renzheng{
      background-color: rgb(230, 0, 62);
    }
    .yanzeng-left {
      height: 60px;
      width: 60px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .yanzeng-right {
      margin-left: 10px;
    }
    .yanzeng-right > div {
      margin-bottom: 20px;
      // margin-left: 10px;
    }
    .zfz {
      display: flex;
      .fan {
        margin-left: 30px;
      }
    }
  }
  .youyi {
    color: #fff;
  }
  .chongzhi-bizhi {
    .right {
      width: 106px;
      height: 134px;

      background: url("../../../../assets/image/youqingtishi.png") no-repeat;
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

    .chongzhi-input {
      display: flex;
      align-items: center;
      width: 90%;
      background: none;
    }
    .chongzhi-input > span {
      display: inline-block;
      width: 100px;
    }
  }
}

.header-chi {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.thingsOk {
  margin: 30px;

  img {
    width: 150px;
  }
}

.box-card {
  position: relative;

  .el-row {
    line-height: 30px;
  }

  .auth-box {
    position: absolute;
    top: 0px;
    right: 20%;
  }

  .name {
    width: 96px;
    text-align: right;
    display: inline-block;
    color: #6e6e6e;
  }

  .info {
    font-size: 16px;
  }
}

.wrapper {
  .demo-form-inline {
    // width: 400px;
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
    margin-left: 120px;
  }

  .img-auth {
    img {
      max-height: 230px;
      max-width: 80%;
    }
  }
}

.el-upload {
  /deep/ .el-upload--picture-card {
    width: 230px;
  }

  img {
    max-height: 100%;
    max-width: 100%;
  }
}
</style>
