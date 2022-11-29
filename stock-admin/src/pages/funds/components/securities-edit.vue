<template>
  <div class="wrapper">
    <el-dialog
      title="证券信息"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="120px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="证券机构" prop="dealerInstitutionsName">
            <el-select clearable filterable v-model="form.dealerInstitutionsName" placeholder="证券机构" value-key="id"  @change="currentSel">
              <el-option v-for="item in dealerList" :key="item.id" :label="item.dealerName" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="证券营业部" prop="salesDepartment">
            <el-input v-model="form.salesDepartment" placeholder="证券营业部"></el-input>
          </el-form-item>
          <el-form-item label="证券账户" prop="accountName">
            <el-input v-model="form.accountName" placeholder="证券账户"></el-input>
          </el-form-item>
          <el-form-item label="交易通账户" prop="transactAccount">
            <el-input v-model="form.transactAccount" placeholder="交易通账户"></el-input>
          </el-form-item>
          <el-form-item label="交易通密码" prop="transactPassword">
            <el-input v-model="form.transactPassword" placeholder="交易通密码"></el-input>
          </el-form-item>
          <el-form-item label="通讯密码" prop="communicationPassword">
            <el-input v-model="form.communicationPassword" placeholder="通讯密码"></el-input>
          </el-form-item>
          <el-form-item label="佣金比例" prop="commissionRatio">
            <el-input v-model="form.commissionRatio" placeholder="佣金比例,0.005代表千分之五"></el-input>
          </el-form-item>
          <el-form-item label="最低佣金" prop="minimumCommissions">
            <el-input v-model="form.minimumCommissions" placeholder="最低佣金，单位元"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select clearable filterable v-model="form.status" placeholder="状态">
              <el-option label="启用" :value="1"></el-option>
              <el-option label="停用" :value="0"></el-option>
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
        return {
          id: 0
        }
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
        tmepdealerInstitutionsName: '',
        dealerInstitutionsId: '',
        dealerInstitutionsName: '',
        salesDepartment: '',
        accountName: '',
        transactAccount: '',
        transactPassword: '',
        communicationPassword: '',
        commissionRatio: '',
        minimumCommissions: '',
        status: ''
      },
      dealerList: [{
        dealerName: '证券机构',
        id: 0
      }],
      rule: {
        dealerInstitutionsName: [
          { required: false, message: '请选择证券机构', trigger: 'change' }
        ],
        salesDepartment: [
          { required: true, message: '请输入证券营业部', trigger: 'blur' }
        ],
        accountName: [
          { required: true, message: '请输入证券账户', trigger: 'blur' }
        ],
        transactAccount: [
          { required: true, message: '请输入交易通账户', trigger: 'blur' }
        ],
        transactPassword: [
          { required: true, message: '请输入交易通密码', trigger: 'blur' }
        ],
        communicationPassword: [
          { required: true, message: '请输入通讯密码', trigger: 'blur' }
        ],
        commissionRatio: [
          { required: true, message: '请输入佣金比例', trigger: 'blur' }
        ],
        minimumCommissions: [
          { required: true, message: '请输入最低佣金', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.dealerInstitutionsId = this.info.dealerInstitutionsId
        this.form.dealerInstitutionsName = this.info.dealerInstitutionsName
        this.form.salesDepartment = this.info.salesDepartment
        this.form.accountName = this.info.accountName
        this.form.transactAccount = this.info.transactAccount
        this.form.transactPassword = this.info.transactPassword
        this.form.communicationPassword = this.info.communicationPassword
        this.form.commissionRatio = this.info.commissionRatio
        this.form.minimumCommissions = this.info.minimumCommissions
        this.form.status = this.info.status
        this.form.tmepdealerInstitutionsName = this.info.dealerInstitutionsName
        
      }
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getDealerList()
  },
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          
          let opts = {
            id: this.info.id,
            dealerInstitutionsId: this.form.dealerInstitutionsId,
            dealerInstitutionsName: this.form.tmepdealerInstitutionsName,
            salesDepartment: this.form.salesDepartment,
            accountName: this.form.accountName,
            transactAccount: this.form.transactAccount,
            transactPassword: this.form.transactPassword,
            communicationPassword: this.form.communicationPassword,
            commissionRatio: this.form.commissionRatio,
            minimumCommissions: this.form.minimumCommissions,
            status: this.form.status
          }
          let data = await api.saveSecuritiesInfo(opts)
          if (data.status === 0) {
            this.$message.success("操作成功")
            this.dialogVisible = false
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
        } else {
          return false
        }
      })
    },
    async getDealerList () {
      // 获取可用券商
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        keyword: this.form.keyword,
        status: 1
      }
      let data = await api.getFundsDealerInstitutionsList(opts)
      if (data.status === 0) {
        this.dealerList = data.data.data.list
      } else {
        this.$message.error(data.msg)
      }
    },
    currentSel(selVal) {
      this.form.dealerInstitutionsId = selVal.id
      this.form.tmepdealerInstitutionsName = selVal.dealerName
      // console.log("选择的name为：" + this.form.tmepdealerInstitutionsName, "选择的id为:" + this.form.dealerInstitutionsId+",selval="+selVal);
    }
  }
}
</script>
<style lang="less" scoped>
</style>
