<template>
  <div class="wrapper">
    <el-dialog
      title="修改订单状态"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" label-width="80px" :rules="rule" class="demo-form-inline">
          <el-form-item label="订单ID" prop="chargeId">
            <el-input disabled v-model="info" placeholder="订单ID"></el-input>
          </el-form-item>
          <el-form-item label="充值状态" prop="state">
            <el-select v-model="form.state" placeholder="充值状态">
              <el-option label="充值成功" value="1"></el-option>
              <el-option label="充值失败" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="充值凭证" >
            <el-image :src="imgUrl"></el-image>
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
      type: Number
    },
    imgUrl:{
      type: String
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        chargeId: '',
        state: ''
      },
      rule: {
        // chargeId: [
        //     { required: true, message: '请输入订单号码', trigger: 'blur' },
        // ],
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
            orderSn: this.info,
            chargeId: this.info,
            state: this.form.state
          }
          let data = await api.updateState(opts)
          if (data.status == 0) {
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
