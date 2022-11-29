<template>
  <div class="wrapper">
    <el-dialog
      title="修改用户信息"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-row>

        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <el-row>
                <el-col v-if="info" :span="12">
                  修改用户信息
                </el-col>
              </el-row>
            </div>
            <div class="text box-content">
              <div>
                <el-form label-width="100px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
                  <!-- <el-form-item label="用户id" prop="id">
                      <el-input disabled v-model="info.id" placeholder="代理名"></el-input>
                  </el-form-item> -->
                  <el-form-item label="所属代理" prop="agentId">
                    <el-select filterable v-model="form.agentId" placeholder="下级代理" @change="changeAgentId">
                      <el-option v-for="i in agentList" :key="i.id" :label="i.agentName" :value="i.id"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="手机" prop="phone">
                    <el-input v-model="form.phone" placeholder="手机"></el-input>
                  </el-form-item>
                  <el-form-item label="用户名" prop="name">
                    <el-input v-model="form.name" placeholder="用户名"></el-input>
                  </el-form-item>
                  <el-form-item label="密码" prop="name">
                    <el-input v-model="form.pwd" placeholder="密码"></el-input>
                  </el-form-item>
                  <el-form-item label="身份证号码" prop="name">
                    <el-input v-model="form.idCard" placeholder="身份证号码"></el-input>
                  </el-form-item>
                  <el-form-item label="登录状态" prop="loginStatus">
                    <el-select filterable v-model="form.loginStatus" placeholder="登录状态">
                      <el-option label="可登录" :value="0"></el-option>
                      <el-option label="不可登录" :value="1"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="交易状态" prop="lockStatus">
                    <el-select filterable v-model="form.lockStatus" placeholder="交易状态">
                      <el-option label="可交易" :value="0"></el-option>
                      <el-option label="不可交易" :value="1"></el-option>
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>
              <span slot="footer" class="dialog-footer text-right">
                        <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
                    </span>
            </div>

          </el-card>

        </el-col>
      </el-row>

    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {
    getDate: {
      type: Function,
      default: function () {

      }
    },
    agentList: {
      type: Array,
      default: function () {
      }
    },
    info: {
      type: Object,
      default: function () {
        return {
          id: ''
        }
      }
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        id: '',
        phone: '',
        name: '',
        pwd: '',
        bankName: '',
        bankAdress: '',
        bankNo: '',
        agentId: '', // 代理id
        loginStatus: '', // 0为可登陆 1为不可登录
        lockStatus: '' // 0为可交易 1为不可交易
      },
      bankId: '',
      rule: {
        name: [
          { required: false, message: '请输入用户名', trigger: 'blur' }
        ],
        // pwd: [
        //     { required: false, message: '请输入密码', trigger: 'blur' },
        //     { min: 6, message: '密码最少为6位数', trigger: 'blur' },
        // ],
        phone: [
          { required: false, message: '请输入手机', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    info (newval) {
      if (newval) {
        this.form.phone = this.info.phone
        this.form.name = this.info.realName
        // this.form.pwd = this.info.userPwd
        this.form.idCard = this.info.idCard
        this.form.agentId = this.info.agentId
        this.form.lockStatus = this.info.isLock
        this.form.loginStatus = this.info.isLogin
        this.getBankInfo()
      }
    }
  },
  computed: {},
  created () {},
  mounted () {},
  methods: {
    changeAgentId (val) {
      this.$forceUpdate()
    },
    async getBankInfo () {
      let opts = {
        userId: this.info.id
      }
      let data = await api.getBank(opts)
      if (data.status === 0) {
        if (data.data) {
          this.bankId = data.data.id
          this.form.bankName = data.data.bankName
          this.form.bankNo = data.data.bankNo
          this.form.bankAdress = data.data.bankAdress
        } else {

        }
      } else {
        this.$message.error(data.msg)
      }
    },
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          if (this.form.pwd && this.form.pwd.length < 6) {
            this.$message.error('密码最少为6位数!')
            return
          }
          let opts = {
            id: this.info.id,
            realName: this.form.name,
            phone: this.form.phone,
            idCard: this.form.idCard,
            agentId: this.form.agentId,
            isLogin: this.form.loginStatus,
            isLock: this.form.lockStatus
          }
          if (this.form.pwd) {
            opts.userPwd = this.form.pwd
          }
          let data = await api.updateUser(opts)
          if (data.status === 0) {
            this.$message.success(data.msg)
            this.clearForm()
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
          this.dialogVisible = false
        } else {
          return false
        }
      })
    },
    async submit2 () {
      // 提交
      //   this.$refs[formName].validate(async(valid) => {
      //     if (valid) {
      let opts = {
        id: this.bankId,
        bankName: this.form.bankName,
        bankAdress: this.form.bankAdress,
        bankNo: this.form.bankNo
      }
      let data = await api.updateBank(opts)
      if (data.status === 0) {
        this.$message.success(data.msg)
        this.clearForm()
        this.getDate()
      } else {
        this.$message.error(data.msg)
      }
      this.dialogVisible = false
      //     } else {
      //         return false;
      //     }
      // })
    },
    clearForm () {
      this.form = {
        id: '',
        phone: '',
        name: '',
        pwd: '',
        bankName: '',
        bankAdress: '',
        bankNo: '',
        loginStatus: '', // 0为可登陆 1为不可登录
        lockStatus: '' // 0为可交易 1为不可交易
      }
    }
  }
}
</script>
<style lang="less" scoped>

</style>
