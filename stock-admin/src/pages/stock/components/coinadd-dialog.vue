<template>
  <div class="wrapper">
    <el-dialog
      title="添加基币"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" size="mini" label-width="150px" :rules="rule">
          <el-form-item label="基币名称" prop="name">
            <el-input v-model="form.name" placeholder="基币名称"></el-input>
          </el-form-item>
          <el-form-item label="基币代码" prop="code">
            <el-input v-model="form.code" placeholder="基币代码"></el-input>
          </el-form-item>
          <el-form-item label="默认汇率" prop="defaultRate">
            <el-input v-model="form.defaultRate" placeholder="基币代码"></el-input>
          </el-form-item>
          <el-form-item label="启用动态行情" prop="dynamicRate">
            <el-select v-model="form.dynamicRate" placeholder="是否启用动态行情">
              <el-option label="启用动态汇率值" value="0"></el-option>
              <el-option label="使用默认汇率值" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否可用" prop="isUse">
            <el-select v-model="form.isUse" placeholder="是否可用">
              <el-option label="不可用" value="0"></el-option>
              <el-option label="可用" value="1"></el-option>
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
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        code: '',
        name: '',
        defaultRate: '',
        dynamicRate: '0',
        isUse: '0'
      },
      rule: {
        name: [
          { required: true, message: '请输入基币名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入基币代码', trigger: 'blur' }
        ],
        defaultRate: [
          { required: true, message: '请输入默认汇率', trigger: 'blur' }
        ],
        dynamicRate: [
          { required: true, message: '请选择是否启用动态行情', trigger: 'change' }
        ],
        isUse: [
          { required: true, message: '是否可用', trigger: 'change' }
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
            coinName: this.form.name,
            coinCode: this.form.code,
            coinGid: 'fx_s' + this.form.code.toLowerCase() + 'USD',
            defaultRate: this.form.defaultRate,
            dynamicRate: this.form.dynamicRate,
            isUse: this.form.isUse
          }
          let data = await api.addCoin(opts)
          if (data.status === 0) {
            this.$message.success('添加成功')
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
        code: '',
        name: '',
        defaultRate: '',
        dynamicRate: '0',
        isUse: '0'
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
