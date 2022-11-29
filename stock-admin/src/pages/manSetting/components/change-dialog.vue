<template>
  <div class="wrapper">
    <el-dialog
      title="修改密码"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="用户id" prop="id">
            <el-input disabled v-model="info.id" placeholder="代理名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="adminPwd">
            <el-input v-model="form.adminPwd" placeholder="密码"></el-input>
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
        adminPwd: ''
      },
      rule: {
        adminPwd: [
          { required: true, message: '请输入密码', trigger: 'blur' }
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
            id: this.info.id,
            adminPwd: this.form.adminPwd
          }
          let data = await api.changepwd(opts)
          if (data.status === 0) {
            this.$message.success(data.msg)
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
