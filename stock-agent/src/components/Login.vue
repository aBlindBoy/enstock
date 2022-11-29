<template>
  <div class="wrapper">
    <el-dialog
      title="修改密码"
      :visible.sync="$store.state.loginIsShow"
      width="500px">
      <div>
        <el-form :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="旧密码" prop="oldPwd">
            <el-input type='password' v-model="form.oldPwd" placeholder="代理名"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPwd">
            <el-input type='password' v-model="form.newPwd" placeholder="密码"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
            <el-button @click="$store.state.loginIsShow = false">取 消</el-button>
            <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {},
  data () {
    return {
      dialogVisible: false,
      form: {
        oldPwd: '',
        newPwd: ''
      },
      rule: {
        oldPwd: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' }
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
            oldPwd: this.form.oldPwd,
            newPwd: this.form.newPwd
          }
          let data = await api.updatePwd(opts)
          if (data.status === 0) {
            this.$message.success('修改成功')
          } else {
            this.$message.error(data.msg)
          }
          this.$store.state.loginIsShow = false
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
