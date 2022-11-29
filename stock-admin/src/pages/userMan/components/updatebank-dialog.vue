<template>
  <div class="wrapper">
    <el-dialog
      title="修改银行卡信息"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-row>
            <el-col v-if="info" :span="12">
                            <span>
                                修改银行卡信息
                            </span>
            </el-col>
          </el-row>
        </div>
        <div v-if="bankId" class="text box-content">
          <div>
            <el-form label-width="100px" :model="form" ref="ruleForm2" class="demo-form-inline">
              <!-- <el-form-item label="用户id" prop="id">
                  <el-input disabled v-model="info.id" placeholder="代理名"></el-input>
              </el-form-item> -->
              <el-form-item label="银行名称" prop="bankName">
                <el-input v-model="form.bankName" placeholder="银行名称"></el-input>
              </el-form-item>
              <el-form-item label="银行卡" prop="bankNo">
                <el-input v-model="form.bankNo" placeholder="银行卡"></el-input>
              </el-form-item>
              <el-form-item label="支行地址" prop="bankAddress">
                <el-input v-model="form.bankAddress" placeholder="支行地址"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div slot="footer text-right clearfix" class="dialog-footer">
            <el-button class="pull-right" type="primary" @click="submit2('ruleForm2')">确 定</el-button>
          </div>
        </div>
        <div v-else>
          还未添加银行卡信息
        </div>
      </el-card>

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
        bankNo: ''
      },
      bankId: '',
      rule: {
        name: [
          { required: false, message: '请输入用户名', trigger: 'blur' }
        ],
        pwd: [
          { required: false, message: '请输入密码', trigger: 'blur' }
        ],
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
        this.form.pwd = this.info.userPwd
        this.form.idCard = this.info.idCard
        this.getBankInfo()
      }
    }
  },
  computed: {},
  created () {},
  mounted () {},
  methods: {
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
          this.form.bankAddress = data.data.bankAddress
        } else {
          this.bankId = ''
        }
      } else {
        this.$message.error(data.msg)
      }
    },
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.info.id,
            realName: this.form.name,
            phone: this.form.phone,
            pwd: this.form.pwd
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
      this.form.amt = ''
      this.form.direction = ''
    }
  }
}
</script>
<style lang="less" scoped>

</style>
