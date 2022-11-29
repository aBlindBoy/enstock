<template>
  <div class="wrapper">
    <el-dialog
      title="修改股票"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" size="mini" label-width="150px" :rules="rule">
          <el-form-item label="股票名称" prop="stockName">
            <el-input v-model="form.stockName" placeholder="股票名称"></el-input>
          </el-form-item>
          <el-form-item label="点差费率" prop="spreadRate">
            <el-input v-model="form.spreadRate" placeholder="点差费率"></el-input>
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
        return {}
      }
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        stockName: '',
        spreadRate: 0
      },
      rule: {
        stockName: [
          { required: true, message: '请输入股票名称', trigger: 'blur' }
        ],
        spreadRate: [
          { required: true, message: '请输入点差费率', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.id = this.info.id
        this.form.stockName = this.info.stockName
        this.form.spreadRate = this.info.spreadRate
      }
    }
  },
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
            stockName: this.form.stockName,
            spreadRate: this.form.spreadRate
          }
          let data = await api.updateStock(opts)
          if (data.status == 0) {
            this.$message.success('修改成功')
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
      this.form = {
        stockName: ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
