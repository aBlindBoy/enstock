<template>
  <div class="wrapper">
    <el-dialog
      title="修改订单状态"
      :visible.sync="dialogVisible"
      width="50%">
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" label-width="80px" :rules="rule" class="demo-form-inline">
          <el-form-item label="订单ID" prop="chargeId">
            <el-input disabled v-model="info" placeholder="订单ID"></el-input>
          </el-form-item>
          <el-form-item label="提现状态" prop="state">
            <el-select v-model="form.state" placeholder="提现状态">
              <el-option label="提现成功" value="1"></el-option>
              <el-option label="提现失败" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-show="form.state == 2" label="描述信息" prop="authMsg">
            <el-input v-model="form.authMsg" placeholder="描述信息"></el-input>
          </el-form-item>
          <el-alert
            v-if="form.state == 2"
            style="margin-bottom:10px"
            title='状态修改为"提现成功",用户的资金会减少'
            type="warning"
            close-text="知道了">
          </el-alert>
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
      type: Number
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        chargeId: '',
        state: '',
        authMsg: ''
      },
      rule: {
        authMsg: [
          { required: false, message: '请输入提示信息', trigger: 'blur' }
        ],
        state: [
          { required: true, message: '请选择充值状态', trigger: 'change' }
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
            withId: this.info,
            state: this.form.state,
            authMsg: this.form.authMsg
          }
          let data = await api.withdrawUpdateState(opts)
          if (data.status === 0) {
            this.$message.success('修改成功')
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
