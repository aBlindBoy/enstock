<template>
  <div class="wrapper">
    <el-dialog
      title="添加用户"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :model="form" ref="ruleForm" size="mini" label-width="100px" :rules="rule">
          <el-form-item label="所属代理" prop="agentId">
            <el-select filterable v-model="form.agentId" placeholder="下级代理">
              <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="账号类型" prop="phone">
            <el-select filterable v-model="form.accountType" placeholder="下级代理">
              <el-option label="正式" value="0"></el-option>
              <el-option label="模拟" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="手机号"></el-input>
          </el-form-item>
          <!-- <el-form-item label="姓名" prop="phone">
             <el-input v-model="form.phone" placeholder="代理名"></el-input>
         </el-form-item> -->
          <el-form-item label="密码" prop="pwd">
            <el-input v-model="form.pwd" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="金额" prop="amt">
            <el-input v-model="form.amt" placeholder="金额"></el-input>
          </el-form-item>
          <div>
            添加的金额默认为融资资金
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
        </span>
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
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        phone: '',
        accountType: '0',
        pwd: '',
        amt: '',
        agentId: ''
      },
      rule: {
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { min: 11, max: 11, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '最少需要6位数的密码', trigger: 'blur' }
        ],
        amt: [
          { required: true, message: '请输入金额', trigger: 'blur' }
        ],
        accountType: [
          { required: true, message: '请选择账号类型', trigger: 'change' }
        ],
        agentId: [
          { required: true, message: '请输入代理', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {},
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            accountType: this.form.accountType,
            phone: this.form.phone,
            pwd: this.form.pwd,
            amt: this.form.amt,
            agentId: this.form.agentId
          }
          let data = await api.addSimulatedAccount(opts)
          if (data.status === 0) {
            this.$message.success('添加成功')
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
          this.dialogVisible = false
        } else {
          return false
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
</style>
