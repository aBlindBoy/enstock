<template>
  <div class="wrapper">
    <el-dialog
      title="添加管理员"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :model="form" ref="ruleForm" size="mini" label-width="120px" :rules="rule">
          <el-form-item label="管理员名" prop="adminName">
            <el-input v-model="form.adminName" placeholder="管理员名"></el-input>
          </el-form-item>
          <el-form-item label="管理员手机号" prop="adminPhone">
            <el-input v-model="form.adminPhone" placeholder="管理员手机号"></el-input>
          </el-form-item>
          <el-form-item label="管理员密码" prop="adminPwd">
            <el-input v-model="form.adminPwd" placeholder="管理员密码"></el-input>
          </el-form-item>
          <el-form-item label="管理员状态" prop="isLock">
            <el-select v-model="form.isLock" placeholder="管理员状态">
              <el-option label="正常" value="0"></el-option>
              <el-option label="锁定" value="1"></el-option>
            </el-select>
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
        adminName: '',
        adminPhone: '',
        adminPwd: '',
        isLock: ''
      },
      rule: {
        adminName: [
          { required: true, message: '请输入管理员姓名', trigger: 'blur' }
        ],
        adminPhone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        adminPwd: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        isLock: [
          { required: true, message: '请选择管理员状态', trigger: 'change' }
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
            adminName: this.form.adminName,
            adminPhone: this.form.adminPhone,
            adminPwd: this.form.adminPwd,
            isLock: this.form.isLock
          }
          let data = await api.addManager(opts)
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
