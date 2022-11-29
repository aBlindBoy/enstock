<template>
  <div class="wrapper">
    <el-dialog
      title="添加banner"
      :visible.sync="dialogVisible"
      width="50%">
      <div>
        <el-form :model="form" ref="ruleForm" size="mini" label-width="100px" :rules="rule">
          <el-form-item label="图片" prop="bannerUrl">
            <el-upload
              :with-credentials='true'
              class="upload-demo"
              :action="admin+'/admin/upload.do'"
              :before-remove="beforeRemove"
              :on-success="handleSuccess"
              :multiple='false'
              :limit="1"
              name="upload_file"
              :on-exceed="handleExceed"
              :file-list="fileList">
              <el-button size="small" type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-row>
            <img class="img" :src="form.bannerUrl" alt="">
            <!-- <el-input v-model="form.targetUrl" placeholder="url"></el-input> -->
          </el-row>
          <el-form-item label="排序" prop="isOrder">
            <el-input v-model="form.isOrder" placeholder="排序值越大越靠前"></el-input>
          </el-form-item>
          <el-form-item label="pc图" prop="isPc">
            <el-switch
              v-model="form.isPc"
              active-color="#13ce66"
              inactive-color="#ddd">
            </el-switch>
            <span v-show="form.isPc">在pc端显示</span>
          </el-form-item>
          <p class="sub-tit">pc端图建议尺寸: 1980*500</p>
          <el-form-item label="移动端图" prop="isM">
            <el-switch
              v-model="form.isM"
              active-color="#13ce66"
              inactive-color="#ddd">
            </el-switch>
            <span v-show="form.isM">在移动端显示</span>
          </el-form-item>
          <p class="sub-tit">移动端图建议尺寸: 1753*1080</p>
          <el-form-item label="标题" prop="banTitle">
            <el-input v-model="form.banTitle" placeholder="标题"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="banDesc">
            <el-input v-model="form.banDesc" placeholder="描述"></el-input>
          </el-form-item>
          <el-form-item label="链接" prop="targetUrl">
            <el-input v-model="form.targetUrl" placeholder="链接"></el-input>
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
import APIUrl from '@/axios/api.url' // 引入api.url.js

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
        bannerUrl: '',
        isOrder: '',
        isPc: false,
        isM: false,
        banDesc: '',
        banTitle: '',
        targetUrl: ''
      },
      rule: {
        bannerUrl: [
          { required: true, message: '请输入url', trigger: 'blur' }
        ],
        isOrder: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      },
      admin: '',
      fileList: []
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.admin = APIUrl.baseURL
    console.log(this.admin)
    if (this.admin === undefined) {
      this.admin = ''
    }
  },
  methods: {
    handleExceed (files, fileList) {
      this.$refs.uploadBtn.clearFiles()
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccess (response, file, fileList) {
      this.form.bannerUrl = response.data.url
    },
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            bannerUrl: this.form.bannerUrl,
            isOrder: this.form.isOrder,
            isPc: this.form.isPc === true ? '0' : '1',
            isM: this.form.isM === true ? '0' : '1',
            banDesc: this.form.banDesc,
            banTitle: this.form.banTitle,
            targetUrl: this.form.targetUrl
          }
          let data = await api.addBanner(opts)
          if (data.status === 0) {
            this.$message.success('添加成功')
            this.getDate()
            this.clearForm()
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
        bannerUrl: '',
        isOrder: '',
        isPc: false,
        isM: false,
        banDesc: '',
        banTitle: '',
        targetUrl: ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .img {
    max-width: 200px;
    max-height: 120px;
    margin-left: 100px;
  }

  .sub-tit {
    margin-left: 100px;
    margin-bottom: 10px;
  }
</style>
