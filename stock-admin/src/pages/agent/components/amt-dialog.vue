<template>
  <div class="wrapper">
    <el-dialog
      title="账户扣入款"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form label-width="100px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="用户id" prop="id">
            <el-input disabled v-model="info.id" placeholder="代理名"></el-input>
          </el-form-item>
          <el-form-item label="金额" prop="amt">
            <el-input v-model="form.amt" placeholder="金额"></el-input>
          </el-form-item>
          <el-form-item label="扣款" prop="amt">
            <el-select filterable v-model="form.direction" placeholder="扣款">
              <el-option label="扣款" :value="1"></el-option>
              <!-- <el-option label="入款" :value="0"></el-option> -->
            </el-select>
          </el-form-item>
          <p style="margin-left:100px;" class="red">
            该操作会直接修改代理用户账户金额,默认修改总资金
          </p>
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
        amt: '',
        direction: 1 // 添加 0 扣款1
      },
      rule: {
        id: [
          { required: false, message: '请输入用户', trigger: 'blur' }
        ],
        amt: [
          { required: true, message: '请输入金额', trigger: 'blur' }
        ],
        direction: [
          { required: true, message: '请选择扣入款', trigger: 'change' }
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
            agentId: this.info.id,
            amt: this.form.amt,
            direction: this.form.direction
          }
          let data = await api.updateAgentAmt(opts)
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
    clearForm () {
      this.form.amt = ''
      this.form.direction = ''
    }
  }
}
</script>
<style lang="less" scoped>
</style>
