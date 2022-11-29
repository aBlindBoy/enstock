<template>
  <div class="wrapper">
    <!-- 主页面 -->
    <div class="wrapper">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-row>
            <el-col v-if="form" :span="12">
                            <span>
                                <i class="iconfont icon-add"></i> 添加期货
                            </span>
            </el-col>
          </el-row>
        </div>
        <div class="text box-content">
          <el-form :inline="true" :rules="rule" ref="ruleForm" :model="form" class="demo-form-inline" size="small"
                   label-suffix=":">
            <div class="title">
              期货基本信息
              <!-- <span>期货ID填写规范,例: 上证期货 为 "sh + 期货代码",深圳期货 为 "sz + 期货代码"</span> -->
            </div>
            <el-row>
              <el-col :span="6">
                <el-form-item prop="futuresName" label="期货名称">
                  <el-input class="form-input" v-model="form.futuresName" placeholder="期货名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="futuresCode" label="期货代码">
                  <el-input class="form-input" v-model="form.futuresCode" placeholder="期货代码"></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="6">
                  <el-form-item  prop="futuresGid" label="期货 ID">
                      <el-input class="form-input" v-model="form.futuresGid" placeholder="期货ID"></el-input>
                  </el-form-item>
              </el-col> -->
            </el-row>
            <div class="title">期货交易信息<span>该期货交易规则的设置信息,请根据您的设置仿照示例填写,价格单位为元,数量单位为手</span></div>
            <el-row>
              <el-col :span="12">
                <el-form-item prop="transFee" label="每手手续费">
                  <el-input class="form-input" v-model="form.transFee" placeholder="每手手续费"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="transState" label="是否可交易">
                  <el-select v-model="form.transState" placeholder="交易状态">
                    <el-option label="可交易" value="1"></el-option>
                    <el-option label="不可交易" value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item prop="minNum" label="买入手数限制">
                  <el-row class="box-time">
                    <el-col :span="24">
                      <el-input class="form-input" v-model="form.minNum" placeholder="最小手数"></el-input>
                      手
                      <span class="separator">~</span>
                      <el-input class="form-input" v-model="form.maxNum" placeholder="最大手数"></el-input>
                      手
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item prop="transAmBegin" label="交易时间(24时制) 填写格式 (HH:MM) ">
                  <el-row class="box-time">
                    <el-col :span="12">
                      上午：
                      <el-input class="form-input" v-model="form.transAmBegin" placeholder="例：1:00"></el-input>
                      点
                      <span class="separator">~</span>
                      <el-input class="form-input" v-model="form.transAmEnd" placeholder="例：12:00"></el-input>
                      点
                    </el-col>
                    <el-col :span="12">
                      下午：
                      <el-input class="form-input" v-model="form.transPmBegin" placeholder="例：12:00"></el-input>
                      点
                      <span class="separator">~</span>
                      <el-input class="form-input" v-model="form.transPmEnd" placeholder="例：23:00"></el-input>
                      点
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item class="form-price" prop="depositAmt" label="每标准手规格">
                  1标准手 =
                  <el-input class="form-input" v-model="form.depositAmt" placeholder="每标准手规格"></el-input>
                  /

                  <el-input style="width:100px;" v-model="form.futuresUnit" placeholder="单位"></el-input>
                  <!-- <el-input class="form-input" v-model="form.depositAmt" placeholder="每标准手规格"></el-input> -->
                </el-form-item>
              </el-col>
              <el-col :span="12">
                例如美原油：1标准手 = 1000桶
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item prop="futuresStandard" label="每手保证金">
                  <el-row class="form-price">
                    <el-col :span="12">
                      <el-input class="form-input" v-model="form.futuresStandard" placeholder="价钱"></el-input>
                    </el-col>
                    <el-col :span="8">
                      <el-select class="form-input" style="width:100px;" v-model="form.coinCode" placeholder="基币单位">
                        <el-option v-for="i in coinList" :key="i.key" :label="i.coinName"
                                   :value="i.coinCode"></el-option>
                        <!-- <el-option label="人民币" value="USD"></el-option> -->
                      </el-select>
                      <!-- <el-input style="width:100px;" class="form-input" v-model="form.coinCode" placeholder="价钱单位"></el-input>/ -->
                    </el-col>
                    <!-- <el-col :span="8">
                        <el-input class="form-input" v-model="form.futuresUnit" placeholder="规格"></el-input>
                    </el-col> -->
                  </el-row>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                (例:300美元)
              </el-col>
            </el-row>
            <div class="title">期货显示信息</div>
            <el-row>
              <el-col :span="12">
                <el-form-item prop="homeShow" label="是否首页显示">
                  <el-select v-model="form.homeShow" placeholder="首页显示状态">
                    <el-option label="显示" value="1"></el-option>
                    <el-option label="不显示" value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="listShow" label="是否列表页显示">
                  <el-select v-model="form.listShow" placeholder="列表页显示状态">
                    <el-option label="显示" value="1"></el-option>
                    <el-option label="不显示" value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>

          <div slot="footer" class="dialog-footer text-center">
            <el-button @click="goback">返回列表</el-button>
            <el-button type="primary" @click="submit('ruleForm')">确认添加</el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import MenuNav from '@/components/menu-nav'
import * as api from '@/axios/api'

export default {
  components: {
    MenuNav
  },
  props: {},
  data () {
    return {
      activeCategory: 'home',
      contentStyleObj: {
        height: ''
      },
      coinList: [],
      form: {
        depositAmt: '', // 每手保证金
        homeShow: '0', // 是否首页显示
        futuresName: '', //
        futuresCode: '',
        futuresGid: '',
        futuresUnit: '', // 每手单位
        futuresStandard: '', // 每手保证金
        coinCode: '', // 基币代码
        listShow: '0',
        maxNum: '',
        minNum: '',
        tdesc: null,
        transFee: '', // 每手手续费
        transState: '0',
        transAmBegin: '',
        transAmEnd: '',
        transPmBegin: '',
        transPmEnd: ''
      },
      rule: {
        futuresName: [
          { required: true, message: '请输入期货名称', trigger: 'blur' }
        ],
        futuresCode: [
          { required: true, message: '请输入期货代码', trigger: 'blur' }
        ],
        futuresGid: [
          { required: true, message: '请输入期货ID', trigger: 'blur' }
        ],
        futuresUnit: [
          { required: true, message: '请输入每手单位', trigger: 'blur' }
        ],
        futuresStandard: [
          { required: true, message: '请输入每手保证金', trigger: 'blur' }
        ],
        coinCode: [
          { required: true, message: '请选择基币代码', trigger: 'change' }
        ],
        depositAmt: [
          { required: true, message: '请输入每标准手规格', trigger: 'blur' }
        ],
        eachPoint: [
          { required: true, message: '请输入每点的浮动价格', trigger: 'blur' }
        ],
        maxNum: [
          { required: true, message: '请输入最大买入手数', trigger: 'blur' }
        ],
        minNum: [
          { required: true, message: '请输入买入手数', trigger: 'blur' }
        ],
        transFee: [
          { required: true, message: '请输入每手手续费', trigger: 'blur' }
        ],
        transAmBegin: [
          { required: true, message: '请输入交易时间', trigger: 'blur' }
        ],
        transAmEnd: [
          { required: true, message: '请输入上午结束交易时间', trigger: 'blur' }
        ],
        transPmBegin: [
          { required: true, message: '请输入下午开始交易时间', trigger: 'blur' }
        ],
        transPmEnd: [
          { required: true, message: '请输入下午结束交易时间', trigger: 'blur' }
        ],
        homeShow: [
          { required: true, message: '请选择期货类型', trigger: 'change' }
        ],
        listShow: [
          { required: true, message: '请选择锁定状态', trigger: 'change' }
        ],
        transState: [
          { required: true, message: '请选择显示状态', trigger: 'change' }
        ]
      }
    }
  },
  activated () {

  },
  created () {
    window.addEventListener('resize', this.getHeight)
    this.getHeight()
    this.getSelectCoin()
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  },
  methods: {
    async changePwd () {
      this.$store.state.loginIsShow = true
    },
    async getSelectCoin () {
      // 获取可用基币
      let data = await api.getSelectCoin()
      if (data.status === 0) {
        this.coinList = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async logout () {
      // 退出登录
      let data = await api.logout()
      if (data.status === 0) {
        this.$router.push('/login')
      } else {
        this.$message.error(data.msg)
      }
    },
    getHeight () {
      this.contentStyleObj.height = window.innerHeight - 64 + 'px'
    },
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // let checkCode = await api.getSingleIndex({futuresCode:this.form.futuresGid})
          // if(checkCode.data.name == null || checkCode.data.name == ''){
          //     this.$message.error('您输入的期货不正确,请添加正确的期货');
          //     return
          // }else{
          let opts = {
            futuresCode: this.form.futuresCode,
            futuresGid: 'hf_' + this.form.futuresCode,
            futuresName: this.form.futuresName,
            depositAmt: this.form.depositAmt,
            listShow: this.form.listShow,
            homeShow: this.form.homeShow,
            transFee: this.form.transFee,
            futuresUnit: this.form.futuresUnit,
            futuresStandard: this.form.futuresStandard,
            coinCode: this.form.coinCode,
            maxNum: this.form.maxNum,
            minNum: this.form.minNum,
            transState: this.form.transState,
            transAmBegin: this.form.transAmBegin,
            transAmEnd: this.form.transAmEnd,
            transPmBegin: this.form.transPmBegin,
            transPmEnd: this.form.transPmEnd
          }
          let data = await api.addFutures(opts)
          if (data.status === 0) {
            this.$message.success('添加成功')
            this.clearForm()
          } else {
            this.$message.error(data.msg)
          }
        }
        // } else {
        //     return false;
        // }
      })
    },
    goback () {
      this.$router.push('/product/futures')
    },
    clearForm () {
      this.form = {
        depositAmt: '', // 每手保证金
        homeShow: '0', // 是否首页显示
        futuresName: '', //
        futuresCode: '',
        futuresGid: '',
        futuresUnit: '', // 每手单位
        futuresStandard: '', // 每手保证金
        coinCode: '', // 基币代码
        listShow: '0',
        maxNum: '',
        minNum: '',
        tdesc: null,
        transFee: '', // 每手手续费
        transState: '0',
        transAmBegin: '',
        transAmEnd: '',
        transPmBegin: '',
        transPmEnd: ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .el-header {
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }

  .header-home {
    padding: 0 20px
  }

  .dialog-footer {
    margin-top: 20px;
  }

  .box-content {
    .title {
      border-bottom: 1px solid #eceff6;
      color: #606266;
      margin: 10px 0;
      text-indent: 1px;

      span {
        color: #999;
        font-size: 12px;
        margin-left: 10px;
      }

      &:before {
        content: "";
        width: 4px;
        height: 4px;
        background: #606266;
        display: inline-block;
        border-radius: 4px;
        margin-right: 5px;
        vertical-align: middle;
      }
    }

  }

  .form-price {
    .el-input {
      width: 108px;
      margin-right: 10px;
    }

    .separator {
      margin: 0 10px;
    }
  }

  .box-time {
    .el-input {
      width: 22%;
      margin: 0 1%;
    }

    .separator {
      margin: 0 10px;
    }
  }
</style>
