<template>
  <div class="wrapper">
    <el-dialog
      title="添加下级代理"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">

          <el-form-item label="代理名" prop="agentName">
            <el-input v-model="form.agentName" placeholder="代理名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="agentPwd">
            <el-input v-model="form.agentPwd" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="代理手机" prop="agentPhone">
            <el-input v-model="form.agentPhone" placeholder="代理手机"></el-input>
          </el-form-item>
          <el-form-item label="真实姓名" prop="agentRealName">
            <el-input v-model="form.agentRealName" placeholder="真实姓名"></el-input>
          </el-form-item>
          <el-form-item label="手续费比例" prop="poundageScale">
            <el-input v-model="form.poundageScale" placeholder="手续费比例，1=100%例如：0.25"></el-input>
          </el-form-item>
          <el-form-item label="递延费比例" prop="deferredFeesScale">
            <el-input v-model="form.deferredFeesScale" placeholder="递延费比例，1=100%例如：0.25"></el-input>
          </el-form-item>
          <el-form-item label="分红比例" prop="receiveDividendsScale">
            <el-input v-model="form.receiveDividendsScale" placeholder="分红比例，1=100%例如：0.25"></el-input>
          </el-form-item>
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
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        agentName: '',
        agentPwd: '',
        agentPhone: '',
        agentRealName: '',
        loginInfo: []
      },
      rule: {
        agentName: [
          { required: true, message: '请输入代理名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        agentPwd: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        agentPhone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' }
        ],
        agentRealName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        poundageScale: [
          { required: true, message: '请输入手续费比例', trigger: 'blur' }
        ],
        deferredFeesScale: [
          { required: true, message: '请输入递延费比例', trigger: 'blur' }
        ],
        receiveDividendsScale: [
          { required: true, message: '请输入分红比例', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getAgentInfo()
  },
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            agentName: this.form.agentName,
            agentPwd: this.form.agentPwd,
            agentPhone: this.form.agentPhone,
            agentRealName: this.form.agentRealName,
            parentId: this.loginInfo.id,
            poundageScale: this.form.poundageScale,
            deferredFeesScale: this.form.deferredFeesScale,
            receiveDividendsScale: this.form.receiveDividendsScale
          }
          let data = await api.addAgent(opts)
          console.log(data)
          if (data.status === 0) {
            this.$message.success(data.msg)
            this.dialogVisible = false
            this.agentPhone = '';
            this.agentRealName = '';
            this.agentPwd = '';
            this.agentName = '';
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
        } else {
          return false
        }
      })
    },
    async getAgentInfo () {
      let data = await api.getAgentInfo()
      if (data.status === 0) {
        this.loginInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
