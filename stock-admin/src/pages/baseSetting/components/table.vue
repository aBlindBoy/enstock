<template>
  <div>
    <!-- <div>
        <div class="table-top-btn">
            <el-row class="text-right">
                <el-button type="text" size="small" @click="addAccount">
                    <i class="iconfont icon-add"></i>添加设置信息
                </el-button>
            </el-row>
        </div>

    </div> -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>logo设置</span>
      </div>
      <div class="text item">
        <el-form :inline="true" :model="logoform" class="demo-form-inline" size="small" ref="logoForm">
          <el-row>
            <el-col :span="8">
              <el-form-item label="名称">
                <el-input v-model="logoform.siteName" placeholder="名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="关键词">
                <el-input v-model="logoform.siteKeywords" placeholder="关键词"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="描述">
                <el-input v-model="logoform.siteDescription" placeholder="描述"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="大logo">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemovelogo"
                  :on-success="handleSuccesslogo"
                  multiple
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedlogo"
                  :file-list="fileListlogo">
                  <el-button size="small" type="primary">上传大logo</el-button>
                </el-upload>
              </el-form-item>
              <p>
                图片建议：背景色为白色或者透明色，格式为png或者jpg图片
              </p>
            </el-col>
            <el-col :span="6">
              <img class="img" :src="logoform.siteLogo" alt="logo">
            </el-col>
            <el-col :span="6">
              <el-form-item label="小logo">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemovelogo2"
                  :on-success="handleSuccesslogo2"
                  multiple
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedlogo2"
                  :file-list="fileListlogo2">
                  <el-button size="small" type="primary">上传min-logo</el-button>
                </el-upload>
                <!-- <el-input v-model="logoform.siteLogoSm" placeholder="小logo"></el-input> -->
              </el-form-item>
              <p>
                图片建议：背景色为透明色，格式为png图片
              </p>
            </el-col>
            <el-col :span="6">
              <img class="img" :src="logoform.siteLogoSm" alt="logo">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" class="text-right">
              <el-button type="primary" size="small" @click="SettingLogoInfo('logoForm')">保存设置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>下载链接设置</span>
      </div>
      <div class="text item">
        <el-form :inline="true" :model="downform" class="demo-form-inline" size="small" ref="downform">
          <el-row>
            <el-col :span="8">
              <el-form-item label="安卓二维码">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemove"
                  :on-success="handleSuccess"
                  multiple
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceed"
                  :file-list="fileList">
                  <el-button size="small" type="primary">上传安卓二维码</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <img class="img" style="height:80px;" :src="downform.siteAndroidImg" alt="">
            </el-col>
            <el-col :span="8">
              <el-form-item label="安卓下载链接">
                <el-input v-model="downform.siteAndroidUrl" placeholder="安卓下载链接"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="IOS二维码">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemove2"
                  :on-success="handleSuccess2"
                  multiple
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceed2"
                  :file-list="fileList2">
                  <el-button size="small" type="primary">上传IOS二维码</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <img class="img" style="height:80px;" :src="downform.siteIosImg" alt="">
            </el-col>
            <el-col :span="8">
              <el-form-item label="ios下载链接">
                <el-input v-model="downform.siteIosUrl" placeholder="ios下载链接"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" class="text-right">
              <el-button type="primary" size="small" @click="SettingDownInfo('downform')">保存设置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>系统颜色配置  （该颜色主题仅针对pc端和手机端用户配置）</span>
      </div>
      <div class="text item">
        <el-form :inline="true" :model="colorform" class="demo-form-inline" size="small" ref="colorform">
          <el-row>
            <el-col :span="8">
              <el-radio-group v-model="colorform.siteColor">
                <el-radio label="red">红色主题</el-radio>
                <el-radio label="black">黑色主题</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" class="text-right">
              <el-button type="primary" size="small" @click="SettingColorInfo('colorform')">保存设置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>公司信息</span>
      </div>
      <div class="text item">
        <el-form :model="companyform" label-width="80px" class="demo-form-inline" size="small" ref="companyform">
          <el-row>
            <el-col :span="24">
              <el-form-item label="公司简介">
                <el-input type="textarea" rows="3" v-model="companyform.siteIntro" placeholder="公司简介"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="公司信息">
                <el-input type="textarea" rows="5" v-model="companyform.companyInfo" placeholder="公司信息"></el-input>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="风险提示">
                <el-input type="textarea" rows="3" v-model="companyform.riskNotice" placeholder="风险提示"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="版权信息">
                <el-input type="textarea" v-model="companyform.siteVersionInfo" placeholder="版权信息"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="资质1">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemoveimg"
                  :on-success="handleSuccessimg"
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedimg"
                  :file-list="fileListimg">
                  <el-button size="small" type="primary">上传资质1</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资质2">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemoveimg2"
                  :on-success="handleSuccessimg2"
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedimg2"
                  :file-list="fileListimg2">
                  <el-button size="small" type="primary">上传资质2</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col class="text-center" :span="12">
              <img class="img" preview="0" preview-text="描述文字" style="max-height:200px;max-width:80%;"
                   :src="companyform.certImg1" alt="">
            </el-col>
            <el-col class="text-center" :span="12">
              <img class="img" preview="1" preview-text="描述文字" style="max-height:200px;;max-width:80%;"
                   :src="companyform.certImg2" alt="">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" class="text-right">
              <el-button type="primary" size="small" @click="SettingcompanyInfo('companyform')">保存设置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>联系方式及协议</span>
      </div>
      <div class="text item">
        <el-form :inline="true" :model="contactform" class="demo-form-inline" size="small" ref="contactform">
          <el-row>
            <el-col :span="8">
              <el-form-item label="Line号码">
                <el-input v-model="contactform.sitePhone" placeholder="Line号码"></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="8">
              <el-form-item label="客服电话">
                <el-input v-model="contactform.sitePhone" placeholder="客服电话"></el-input>
              </el-form-item>
            </el-col> -->
            <el-col :span="8">
              <el-form-item label="语言">
                <el-input v-model="contactform.siteLanguage" placeholder="语言"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="邮件地址">
                <el-input v-model="contactform.siteHost" placeholder="邮件发送人"></el-input>
                <p>例如：http://www.***.com</p>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="邮件接收人">
                <el-input v-model="contactform.siteEmailTo" placeholder="邮件接收人"></el-input>
                <p>出现550 User has no permission,登陆邮箱-设置-客户端授权密码</p>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="邮件发送人">
                <el-input v-model="contactform.siteEmailFrom" placeholder="邮件发送人"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="在线客服URL">
                <el-input v-model="contactform.onlineService" style="width:430px" placeholder="在线客服url"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>

            <el-col :span="24">
              <el-form-item label="注册协议地址">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemoveagress"
                  :on-success="handleSuccessagress"
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedagress"
                  :file-list="fileListagress">
                  <el-button size="small" type="primary">上传注册协议地址</el-button>
                </el-upload>
              </el-form-item>
              <p style="margin-bottom:10px;">
                {{contactform.regAgress}}
              </p>
            </el-col>
            <el-col :span="24">
              <el-form-item label="融资融券协议地址">
                <el-upload
                  :with-credentials='true'
                  class="upload-demo"
                  :action="admin+'/admin/upload.do'"
                  :before-remove="beforeRemoveagress2"
                  :on-success="handleSuccessagress2"
                  :limit="1"
                  name="upload_file"
                  :on-exceed="handleExceedagress2"
                  :file-list="fileListagress2">
                  <el-button size="small" type="primary">上传融资融券协议地址</el-button>
                </el-upload>
              </el-form-item>
              <p style="margin-bottom:10px;">
                {{contactform.tradeAgress}}
              </p>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="注册协议文本">
                <Editor v-model="contactform.regAgreeText" :isClear="false" @change="editorChange1"></Editor> 
                <!-- <el-input type="textarea" rows="5" style="width:800px" v-model="contactform.regAgreeText" placeholder="注册协议文本"></el-input> -->
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="融资融券标题">
                <el-input type="textarea" rows="1" style="width:800px" v-model="contactform.tradeAgreeTitle" placeholder="融资融券标题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="融资融券文本">
                 <Editor v-model="contactform.tradeAgreeText" :isClear="false" @change="editorChange2"></Editor> 
                <!-- <el-input type="textarea" rows="5" style="width:800px" v-model="contactform.tradeAgreeText" placeholder="融资融券协议文本"></el-input> -->
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" class="text-right">
              <el-button type="primary" size="small" @click="SettingContactInfo('contactform')">保存设置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <DetailDialog ref="addSettingDialog"></DetailDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import DetailDialog from './add-dialog'
import APIUrl from '@/axios/api.url' // 引入api.url.js
import Editor from '@/components/UE'

export default {
  components: {
    DetailDialog,
    Editor
  },
  props: {},
  data () {
    return {
      colorform: {
        siteColor: 'red'
      },
      logoform: {
        siteName: '',
        siteKeywords: '',
        siteDescription: '',
        siteLogo: '',
        siteLogoSm: ''
      },
      downform: {
        siteAndroidImg: '',
        siteAndroidUrl: '',
        siteIosImg: '',
        siteIosUrl: ''
      },
      contactform: {
        siteQq: '',
        sitePhone: '',
        siteEmailFrom: '',
        siteEmailTo: '',
        siteLanguage: '',
        siteVersionInfo: '',
        regAgress: '',
        tradeAgress: '',
        siteHost: '',
        tradeAgreeTitle: '',
        tradeAgreeText: '',
        regAgreeText:'',
        onlineService: ''
      },
      companyform: {
        companyInfo: '',
        siteIntro: '',
        certImg1: '',
        certImg2: '',
        riskNotice: '',
        siteVersionInfo: ''
      },
      fileList: [],
      fileList2: [],
      fileListlogo: [],
      fileListlogo2: [],
      fileListimg: [],
      fileListimg2: [],
      fileListagress: [],
      fileListagress2: [],
      info: {},
      type: '', // 添加或者更新
      admin: '',
      rule: {},
      id: ''

    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.getSettingInfo()
    this.admin = APIUrl.baseURL
    if (this.admin === undefined) {
      this.admin = ''
    }
  },
  methods: {
    editorChange1(val) {
      this.contactform.regAgreeText = val
    },
    editorChange2(val) {
      this.contactform.tradeAgreeText = val
    },
    handleExceed (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccess (response, file, fileList) {
      this.downform.siteAndroidImg = response.data.url
    },
    handleExceed2 (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove2 (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccess2 (response, file, fileList) {
      this.downform.siteIosImg = response.data.url
    },
    // 上传资质
    handleExceedimg (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemoveimg (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccessimg (response, file, fileList) {
      this.companyform.certImg1 = response.data.url
    },
    handleExceedimg2 (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemoveimg2 (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccessimg2 (response, file, fileList) {
      this.companyform.certImg2 = response.data.url
    },
    // logo信息的设置
    handleExceedlogo (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemovelogo (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccesslogo (response, file, fileList) {
      this.logoform.siteLogo = response.data.url
    },
    handleExceedlogo2 (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemovelogo2 (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccesslogo2 (response, file, fileList) {
      this.logoform.siteLogoSm = response.data.url
    },
    // 协议上传
    handleExceedagress (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemoveagress (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccessagress (response, file, fileList) {
      this.contactform.regAgress = response.data.url
    },
    handleExceedagress2 (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemoveagress2 (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleSuccessagress2 (response, file, fileList) {
      this.contactform.tradeAgress = response.data.url
    },
    async getSettingInfo () {
      let data = await api.getInfo()
      if (data.status === 0) {
        // logo
        this.logoform.siteName = data.data.siteName
        this.logoform.siteKeywords = data.data.siteKeywords
        this.logoform.siteDescription = data.data.siteDescription
        this.logoform.siteLogo = data.data.siteLogo
        this.logoform.siteLogoSm = data.data.siteLogoSm
        // downform
        this.downform.siteAndroidImg = data.data.siteAndroidImg
        this.downform.siteAndroidUrl = data.data.siteAndroidUrl
        this.downform.siteIosImg = data.data.siteIosImg
        this.downform.siteIosUrl = data.data.siteIosUrl
        // contactform
        this.contactform.siteQq = data.data.siteQq
        this.contactform.sitePhone = data.data.sitePhone
        this.contactform.siteEmailFrom = data.data.siteEmailFrom
        this.contactform.siteEmailTo = data.data.siteEmailTo
        this.contactform.siteLanguage = data.data.siteLanguage
        this.contactform.regAgress = data.data.regAgree
        this.contactform.tradeAgress = data.data.tradeAgree
        this.companyform.siteVersionInfo = data.data.siteVersionInfo
        this.contactform.siteHost = data.data.siteHost
        this.contactform.tradeAgreeTitle = data.data.tradeAgreeTitle
        this.contactform.tradeAgreeText = data.data.tradeAgreeText
        this.contactform.regAgreeText = data.data.regAgreeText
        this.contactform.onlineService = data.data.onlineService
        // 公司信息
        this.companyform.companyInfo = data.data.companyInfo
        this.companyform.siteIntro = data.data.siteIntro
        this.companyform.certImg1 = data.data.certImg1
        this.companyform.certImg2 = data.data.certImg2
        this.companyform.riskNotice = data.data.riskNotice
        // 颜色设置
        this.colorform.siteColor = data.data.siteColor
        this.type = 'edit'
        this.id = data.data.id
      } else {
        this.type = 'add'
        this.$message.error('还没有设置信息，请添加')
      }
    },

    // 上传 网站logo设置
    async SettingLogoInfo (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.id,
            siteName: this.logoform.siteName,
            siteKeywords: this.logoform.siteKeywords,
            siteDescription: this.logoform.siteDescription,
            siteLogo: this.logoform.siteLogo,
            siteLogoSm: this.logoform.siteLogoSm
          }
          let data = ''
          if (this.type === 'add') {
            data = await api.addSettingInfo(opts)
          } else {
            data = await api.changeSettingInfo(opts)
          }
          if (data.status === 0) {
            this.getSettingInfo()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }
      })
    },
    async SettingColorInfo (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.id,
            siteColor: this.colorform.siteColor
          }
          let data = ''
          if (this.type === 'add') {
            data = await api.addSettingInfo(opts)
          } else {
            data = await api.changeSettingInfo(opts)
          }
          if (data.status === 0) {
            // this.getSettingInfo()
            location.reload()
            this.$message.success(data.msg)
          } else {
            location.reload()
            this.$message.error(data.msg)
          }
        }
      })
    },
    // 下载 设置
    async SettingDownInfo (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.id,
            siteAndroidImg: this.downform.siteAndroidImg,
            siteAndroidUrl: this.downform.siteAndroidUrl,
            siteIosImg: this.downform.siteIosImg,
            siteIosUrl: this.downform.siteIosUrl
          }
          let data = ''
          if (this.type === 'add') {
            data = await api.addSettingInfo(opts)
          } else {
            data = await api.changeSettingInfo(opts)
          }
          if (data.status === 0) {
            this.getSettingInfo()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }
      })
    },
    // 联系及版权信息 设置
    async SettingContactInfo (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.id,
            siteQq: this.contactform.siteQq,
            sitePhone: this.contactform.sitePhone,
            siteEmailFrom: this.contactform.siteEmailFrom,
            siteEmailTo: this.contactform.siteEmailTo,
            siteLanguage: this.contactform.siteLanguage,
            // siteVersionInfo:this.contactform.siteVersionInfo,
            regAgree: this.contactform.regAgress,
            tradeAgree: this.contactform.tradeAgress,
            siteHost: this.contactform.siteHost,
            tradeAgreeTitle: this.contactform.tradeAgreeTitle,
            tradeAgreeText: this.contactform.tradeAgreeText,
            regAgreeText: this.contactform.regAgreeText,
            onlineService: this.contactform.onlineService
          }
          let data = ''
          if (this.type === 'add') {
            data = await api.addSettingInfo(opts)
          } else {
            data = await api.changeSettingInfo(opts)
          }
          if (data.status === 0) {
            this.getSettingInfo()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }
      })
    },
    // 公司信息
    async SettingcompanyInfo (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.id,
            companyInfo: this.companyform.companyInfo,
            siteIntro: this.companyform.siteIntro,
            certImg1: this.companyform.certImg1,
            certImg2: this.companyform.certImg2,
            riskNotice: this.companyform.riskNotice,
            siteVersionInfo: this.companyform.siteVersionInfo
          }
          let data = ''
          if (this.type === 'add') {
            data = await api.addSettingInfo(opts)
          } else {
            data = await api.changeSettingInfo(opts)
          }
          if (data.status === 0) {
            this.getSettingInfo()
            this.$message.success(data.msg)
          } else {
            this.$message.error(data.msg)
          }
        }
      })
    },
    addAccount () {
      // 添加设置信息
      this.$refs.addSettingDialog.dialogVisible = true
    }
  }
}
</script>
<style lang="less" scoped>

  .box-card {
    margin-bottom: 10px;

    .img {
      max-width: 80%;
      background: #eee;
      max-height: 100px;
    }
  }
</style>
