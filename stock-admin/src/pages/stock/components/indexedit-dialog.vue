<template>
  <div class="wrapper">
    <el-dialog
      title="修改指数"
      :visible.sync="dialogVisible"
      width="50%">
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" size="mini" label-width="150px" :rules="rule">
          <el-form-item prop="depositAmt" label="每手保证金(例:1000)">
            <el-input class="form-input" v-model="form.depositAmt" placeholder="每手保证金"></el-input>
          </el-form-item>
          <el-form-item prop="transFee" label="双边手续费(例:200)">
            <el-input class="form-input" v-model="form.transFee" placeholder="双边手续费"></el-input>
          </el-form-item>
          <el-form-item prop="eachPoint" label="每点浮动价(例:300)">
            <el-input class="form-input" v-model="form.eachPoint" placeholder="每点浮动价"></el-input>
          </el-form-item>
          <el-form-item prop="maxNum" label="最大买入手数(例:200)">
            <el-input class="form-input" v-model="form.maxNum" placeholder="最大买入手数"></el-input>
          </el-form-item>
          <el-form-item prop="minNum" label="最小买入手数(例:1)">
            <el-input class="form-input" v-model="form.minNum" placeholder="最小买入手数"></el-input>
          </el-form-item>
          <el-form-item label="是否在首页显示" prop="homeShow">
            <el-select v-model="form.homeShow" placeholder="是否在首页显示">
              <el-option label="显示" :value="1"></el-option>
              <el-option label="不显示" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否在列表页显示" prop="listShow">
            <el-select v-model="form.listShow" placeholder="是否在列表页显示">
              <el-option label="显示" :value="1"></el-option>
              <el-option label="不显示" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否可交易" prop="transState">
            <el-select v-model="form.transState" placeholder="是否可交易">
              <el-option label="可交易" :value="1"></el-option>
              <el-option label="不可交易" :value="0"></el-option>
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
        depositAmt: '',
        transFee: '',
        eachPoint: '',
        maxNum: '',
        minNum: '',
        homeShow: '',
        listShow: '',
        transState: ''
      },
      rule: {
        depositAmt: [
          { required: true, message: '请输入每手保证金', trigger: 'blur' }
        ],
        transFee: [
          { required: true, message: '请输入双边手续费', trigger: 'blur' }
        ],
        eachPoint: [
          { required: true, message: '请输入每点浮动价', trigger: 'blur' }
        ],
        maxNum: [
          { required: true, message: '请输入最大买入手数', trigger: 'blur' }
        ],
        minNum: [
          { required: true, message: '请输入最小买入手数', trigger: 'blur' }
        ],
        homeShow: [
          { required: true, message: '是否在首页显示', trigger: 'change' }
        ],
        listShow: [
          { required: true, message: '是否在列表页显示', trigger: 'change' }
        ],
        transState: [
          { required: true, message: '是否可交易', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.depositAmt = this.info.depositAmt
        this.form.transFee = this.info.transFee
        this.form.eachPoint = this.info.eachPoint
        this.form.maxNum = this.info.maxNum
        this.form.minNum = this.info.minNum
        this.form.homeShow = this.info.homeShow
        this.form.listShow = this.info.listShow
        this.form.transState = this.info.transState
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
            // id:this.info.id,
            // id:this.info.id,
            // id:this.info.id,
            depositAmt: this.form.depositAmt,
            transFee: this.form.transFee,
            eachPoint: this.form.eachPoint,
            maxNum: this.form.maxNum,
            minNum: this.form.minNum,
            homeShow: this.form.homeShow,
            listShow: this.form.listShow,
            transState: this.form.transState,
            tdesc: ''
          }
          let data = await api.updateIndex(opts)
          if (data.status === 0) {
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
        depositAmt: '',
        transFee: '',
        eachPoint: '',
        maxNum: '',
        minNum: '',
        homeShow: '',
        listShow: '',
        transState: ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
