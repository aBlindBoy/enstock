<template>
  <div class="wrapper">
    <!-- 主页面 -->
    <div class="wrapper">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-row>
            <el-col v-if="form" :span="12">
                        <span>
                            <i class="iconfont icon-add"></i> 添加指数
                        </span>
            </el-col>
          </el-row>
        </div>
        <div class="text box-content">
          <el-form :inline="true" :rules="rule" ref="ruleForm" :model="form" class="demo-form-inline" size="small">
            <div class="title">指数基本信息<span>指数ID填写规范,例: 上证指数 为 "sh + 股票代码",深圳指数 为 "sz + 股票代码"</span></div>
            <el-row>
              <el-col :span="6">
                <el-form-item prop="indexName" label="指数名称">
                  <el-input class="form-input" v-model="form.indexName" placeholder="指数名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="indexCode" label="指数代码">
                  <el-input class="form-input" v-model="form.indexCode" placeholder="指数代码"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="indexGid" label="指数ID">
                  <el-input class="form-input" v-model="form.indexGid" placeholder="指数ID"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">指数交易信息<span>该指数交易规则的设置信息,请根据您的设置仿照示例填写,价格单位为元,数量单位为手</span></div>
            <el-row>
              <el-col :span="6">
                <el-form-item prop="depositAmt" label="每手保证金(例:10000)">
                  <el-input class="form-input" v-model="form.depositAmt" placeholder="每手保证金"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="transFee" label="双边手续费(例:200)">
                  <el-input class="form-input" v-model="form.transFee" placeholder="双边手续费"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="eachPoint" label="每点浮动价(例:300)">
                  <el-input class="form-input" v-model="form.eachPoint" placeholder="每点浮动价"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="maxNum" label="最大买入手数(例:200)">
                  <el-input class="form-input" v-model="form.maxNum" placeholder="最大买入手数"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="minNum" label="最小买入手数(例:1)">
                  <el-input class="form-input" v-model="form.minNum" placeholder="最小买入手数"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="transState" label="是否可交易">
                  <el-select v-model="form.transState" placeholder="交易状态">
                    <el-option label="可交易" value="1"></el-option>
                    <el-option label="不可交易" value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">指数显示信息</div>
            <el-row>
              <el-col :span="6">
                <el-form-item prop="homeShow" label="是否首页显示">
                  <el-select v-model="form.homeShow" placeholder="首页显示状态">
                    <el-option label="显示" value="1"></el-option>
                    <el-option label="不显示" value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
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
            <el-button @click="goback">返 回列表</el-button>
            <el-button type="primary" @click="submit('ruleForm')">确 认添加</el-button>
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
      form: {
        depositAmt: '', // 每手保证金
        eachPoint: '', // 每点的浮动价格
        homeShow: '0', // 是否首页显示
        indexCode: '', //
        indexGid: '',
        indexName: '',
        listShow: '0',
        maxNum: '',
        minNum: '',
        tdesc: null,
        transFee: '',
        transState: '0'
      },
      rule: {
        indexName: [
          { required: true, message: '请输入股票名称', trigger: 'blur' }
        ],
        indexCode: [
          { required: true, message: '请输入股票代码', trigger: 'blur' },
          { min: 6, max: 6, message: '请输入正确的股票代码', trigger: 'blur' }
        ],
        indexGid: [
          { required: true, message: '请输入股票ID', trigger: 'blur' }
        ],
        depositAmt: [
          { required: true, message: '请输入每手保证金', trigger: 'blur' }
        ],
        eachPoint: [
          { required: true, message: '请输入每点的浮动价格', trigger: 'blur' }
        ],
        maxNum: [
          { required: true, message: '请输入最大买入手数', trigger: 'blur' }
        ],
        minNum: [
          { required: true, message: '请输入最小买入手数', trigger: 'blur' }
        ],
        transFee: [
          { required: true, message: '请输入双边手续费', trigger: 'blur' }
        ],
        homeShow: [
          { required: true, message: '请选择股票类型', trigger: 'change' }
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
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  },
  methods: {
    async changePwd () {
      this.$store.state.loginIsShow = true
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
          let checkCode = await api.getSingleIndex({ indexCode: this.form.indexGid })
          if (checkCode.data.name == null || checkCode.data.name === '') {
            this.$message.error('您输入的指数不正确,请添加正确的指数')
            return
          } else {
            let opts = {
              currentPoint: this.form.currentPoint,
              depositAmt: this.form.depositAmt,
              eachPoint: this.form.eachPoint,
              floatPoint: this.form.floatPoint,
              floatRate: this.form.floatRate,
              homeShow: this.form.homeShow,
              indexCode: this.form.indexCode,
              indexGid: this.form.indexGid,
              indexName: this.form.indexName,
              listShow: this.form.listShow,
              maxNum: this.form.maxNum,
              minNum: this.form.minNum,
              transFee: this.form.transFee,
              transState: this.form.transState,
              tdesc: ''
            }
            let data = await api.addIndex(opts)
            if (data.status === 0) {
              this.$message.success('添加成功')
              this.clearForm()
            } else {
              this.$message.error(data.msg)
            }
          }
        } else {
          return false
        }
      })
    },
    goback () {
      this.$router.push('/product/index')
    },
    clearForm () {
      this.form = {
        currentPoint: '', // 当前点数
        depositAmt: '', // 每手保证金
        homeShow: '0', // 是否首页显示
        indexCode: '', //
        indexGid: '',
        indexName: '',
        listShow: '0',
        maxNum: '',
        minNum: '',
        tdesc: null,
        transFee: '',
        transState: '0'
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
</style>
