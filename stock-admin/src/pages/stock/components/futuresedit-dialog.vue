<template>
  <div class="wrapper">
    <el-dialog
      title="修改期货"
      :visible.sync="dialogVisible"
      width="50%">
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" size="mini" label-width="150px" :rules="rule">
          <el-form-item prop="transFee" label="双边手续费(例:200)">
            <el-input class="form-input" v-model="form.transFee" placeholder="双边手续费"></el-input>
          </el-form-item>
          <el-form-item prop="depositAmt" label="每手保证金">
            <el-row class="form-price">
              <el-col :span="12">
                <el-input class="form-input" v-model="form.depositAmt " placeholder="价钱"></el-input>
              </el-col>
              <el-col :span="12">
                <el-select class="form-input" style="width:100px;" v-model="form.coinCode" placeholder="基币单位">
                  <el-option v-for="i in coinList" :key="i.key" :label="i.coinName" :value="i.coinCode"></el-option>
                </el-select>
              </el-col>
            </el-row>
            <!-- <el-input class="form-input" v-model="form.depositAmt" placeholder="每手保证金"></el-input> -->
          </el-form-item>
          <el-form-item prop="eachPoint" label="每标准手">
            <el-row class="form-price">
              <!-- <el-form-item class="form-price" prop="depositAmt" label="每标准手" > -->
              1标准手 =
              <el-input class="form-input" v-model="form.futuresStandard" placeholder="每标准手规格"></el-input>
              /

              <el-input style="width:100px;" v-model="form.futuresUnit" placeholder="单位"></el-input>
              <!-- <el-input class="form-input" v-model="form.depositAmt" placeholder="每标准手规格"></el-input> -->
              <!-- </el-form-item> -->
            </el-row>
          </el-form-item>
          <el-form-item prop="eachPoint" label="每点浮动价(单位：USD)">
            <el-input class="form-input" v-model="form.eachPoint" placeholder="每点浮动价"></el-input>
          </el-form-item>
          <el-form-item prop="time" label="交易时间(24时制)">
            <el-row class="form-price">
              <el-col :span="24">
                上    午：
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transAmBegin"
                          placeholder="交易时间"></el-input>
                点
                <span class="separator">~</span>
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transAmEnd"
                          placeholder="交易时间"></el-input>
                点
              </el-col>
              <el-col :span="24">
                下午1：
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transPmBegin"
                          placeholder="交易时间"></el-input>
                点
                <span class="separator">~</span>
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transPmEnd"
                          placeholder="交易时间"></el-input>
                点
              </el-col>
              <el-col :span="24">
                下午2：
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transPmBegin2"
                          placeholder="交易时间"></el-input>
                点
                <span class="separator">~</span>
                <el-input class="form-input" style="width:100px;margin-right:2px" v-model="form.transPmEnd2"
                          placeholder="交易时间"></el-input>
                点
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="maxNum" label="最大买入手数(例:200)">
            <el-input class="form-input" v-model="form.maxNum" placeholder="最大买入手数"></el-input>
          </el-form-item>
          <el-form-item prop="minNum" label="最小买入手数(例:1)">
            <el-input class="form-input" v-model="form.minNum" placeholder="最小买入手数"></el-input>
          </el-form-item>
          <!-- <el-form-item label="是否在首页显示" prop="homeShow">
              <el-select  v-model="form.homeShow" placeholder="是否在首页显示">
                  <el-option label="显示" :value="1"></el-option>
                  <el-option label="不显示" :value="0"></el-option>
              </el-select>
          </el-form-item> -->
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
        depositAmt: '', // 每手保证金
        homeShow: '0', // 是否首页显示
        futuresName: '', //
        futuresCode: '',
        futuresGid: '',
        futuresUnit: '', // 每手单位
        futuresStandard: '', // 每单位报价
        coinCode: '', // 基币代码
        listShow: '0',
        maxNum: '',
        minNum: '',
        tdesc: null,
        transFee: '', // 每手手续费
        transState: '0',
        transAmBegin: '',
        transAmEnd: '',
        transPmBegin: '',
        transPmEnd: '',
        transPmBegin2: '',
        transPmEnd2: '',
        eachPoint: 0
      },
      coinList: [],
      rule: {
        depositAmt: [
          { required: true, message: '请输入每标准手规格', trigger: 'blur' }
        ],
        transFee: [
          { required: true, message: '请输入双边手续费', trigger: 'blur' }
        ],
        futuresStandard: [
          { required: true, message: '请输入报价', trigger: 'blur' }
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
        this.form.futuresName = this.info.futuresName
        this.form.futuresCode = this.info.futuresCode
        this.form.futuresGid = this.info.futuresGid
        this.form.futuresUnit = this.info.futuresUnit
        this.form.depositAmt = this.info.depositAmt
        this.form.transFee = this.info.transFee
        this.form.maxNum = this.info.maxNum
        this.form.minNum = this.info.minNum
        this.form.homeShow = this.info.homeShow
        this.form.listShow = this.info.listShow
        this.form.transState = this.info.transState
        this.form.futuresStandard = this.info.futuresStandard
        this.form.coinCode = this.info.coinCode
        this.form.transAmBegin = this.info.transAmBegin
        this.form.transAmEnd = this.info.transAmEnd
        this.form.transPmBegin = this.info.transPmBegin
        this.form.transPmEnd = this.info.transPmEnd
        this.form.transPmBegin2 = this.info.transPmBegin2
        this.form.transPmEnd2 = this.info.transPmEnd2
        this.form.eachPoint = this.info.eachPoint
      }
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getSelectCoin()
  },
  methods: {
    async getSelectCoin () {
      // 获取可用基币
      let data = await api.getSelectCoin()
      if (data.status === 0) {
        this.coinList = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.info.id,
            // id:this.info.id,
            // id:this.info.id,
            // id:this.info.id,
            // futuresName:this.form.futuresName,
            // futuresCode:this.form.futuresCode,
            // futuresGid:this.form.futuresGid,
            depositAmt: this.form.depositAmt,
            transFee: this.form.transFee,
            futuresUnit: this.form.futuresUnit,
            maxNum: this.form.maxNum,
            minNum: this.form.minNum,
            homeShow: this.form.homeShow,
            listShow: this.form.listShow,
            transState: this.form.transState,
            futuresStandard: this.form.futuresStandard,
            coinCode: this.form.coinCode,
            transAmBegin: this.form.transAmBegin,
            transAmEnd: this.form.transAmEnd,
            transPmBegin: this.form.transPmBegin,
            transPmEnd: this.form.transPmEnd,
            transPmBegin2: this.form.transPmBegin2,
            transPmEnd2: this.form.transPmEnd2,
            eachPoint: this.form.eachPoint
            // tdesc:''
          }
          let data = await api.updateFutures(opts)
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
  .form-price {
    .el-input {
      width: 108px;
      margin-right: 10px;
    }
  }

  .box-time {
    .el-input {
      width: 80%;
      margin-right: 10%;
    }
  }
</style>
