<template>
  <div class="wrapper">
    <el-dialog
      title="添加充值订单"
      :visible.sync="dialogVisible"
      width="50%">
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" label-width="80px" :rules="rule" class="demo-form-inline">
          <el-form-item label="用户ID" prop="chargeId">
            <el-input v-model="form.userId" placeholder="用户ID"></el-input>
          </el-form-item>
          <el-form-item label="充值状态" prop="state">
            <el-select v-model="form.state" placeholder="充值状态">
              <el-option label="充值中" value="0"></el-option>
              <el-option label="充值成功" value="1"></el-option>
              <el-option label="充值失败" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="充值渠道" prop="state">
            <el-radio-group v-model="form.payChannel">
              <el-radio-button label="支付宝"></el-radio-button>
              <el-radio-button label="对公转账"></el-radio-button>
              <el-radio-button label="现金转账"></el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="充值金额" prop="amt">
            <el-input v-model="form.amt" placeholder="充值金额"></el-input>
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
        userId: '',
        amt: '',
        state: '',
        payChannel: ''
      },
      rule: {
        userId: [
          { required: true, message: '请输入用户id', trigger: 'blur' }
        ],
        amt: [
          { required: true, message: '请输入充值金额', trigger: 'blur' }
        ],
        state: [
          { required: true, message: '请选择充值状态', trigger: 'change' }
        ],
        payChannel: [
          { required: true, message: '请输入充值渠道', trigger: 'blur' }
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
            userId: this.form.userId,
            amt: this.form.amt,
            state: this.form.state,
            payChannel: this.form.payChannel === '支付宝' ? 0 : this.form.payChannel === '对公转账' ? 1 : this.form.payChannel
          }
          let data = await api.createOrder(opts)
          if (data.status === 0) {
            this.$message.success('创建成功')
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
