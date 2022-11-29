<!-- <template>
  <div class="footer bg-f5">
    <div class="footer-contain">
      <div class="footer-link container clearfix">
        <div class="footer-list">
          <ul>
            <li v-for="item of listData" :key="item.key">
              <div class="title">{{item.title}}</div>
            </li>

          </ul>
        </div>
      </div>
      <div class="footer-copyright">
        <el-row>
          <el-col :span="24">
            <p class="first">© <span class="year">2019</span> {{siteInfo.siteName}} All rights reserved </p>
            <p class="first">{{siteInfo.siteVersionInfo}}</p>
            <P class="color">Risk warning:</P>
            <p class="color">{{siteInfo.riskNotice}}</p>
          </el-col>
          <el-col :span="8"></el-col>
          <el-col :span="8">
            <!-- <p v-if="siteInfo.sitePhone">
             聯系方式 <span class="info" style="color: #6a6c70;">{{siteInfo.sitePhone}}</span>
            </p> -->
             <!-- <p v-if="siteInfo.siteQq">
              Line號 <span class="info" style="color: #6a6c70;">{{siteInfo.siteQq}}</span>
            </p>  -->
            <p v-if="siteInfo.sitePhone">
             Line號 <span class="info" style="color: #6a6c70;">{{siteInfo.sitePhone}}</span>
            </p>
            <p v-if="siteInfo.siteQq">
              郵件 <span class="info" style="color: #6a6c70;">{{siteInfo.siteEmailTo}}</span>
            </p>
          </el-col>
        </el-row>
      </div>
      <el-row class="company-info">
        <span class="foot-item"></span>
        <span class="foot-item"></span>
        <span class="foot-item"></span>
        <span class="foot-item"></span>
        <span class="foot-item"></span>
      </el-row>
    </div>
  </div>
</template>

<script>
  export default {
    components: {},
    props: {
      siteInfo: {
        type: Object,
        default () {
          return {
            siteName: '',
            sitePhone: '',
            siteQq: '',
            siteEmailTo: '',
            siteVersionInfo: ''
          }
        }
      }
    },
    data () {
      return {
        listData: []
      }
    },
    watch: {},
    computed: {},
    created () {
    },
    mounted () {
    },
    methods: {}
  }
</script>
<style lang="less" scoped>
  .footer {
    color: #6a6c70;

    .footer-contain {
      width: 1000px;
      margin: 0 auto;
      padding: 20px 0px;
      line-height: 30px;
      border-top: 1px solid #2d2f33;
    }

    .footer-copyright {
      padding-bottom: 20px;

      .info {
        color: #fefefe;
        font-size: 20px;
        margin-left: 10px;
      }

      .first {
        font-size: 12px;
      }

    }

    .company-info {
      padding-top: 20px;
      border-top: 1px solid #2d2f33;
    }

    .foot-item {
      display: inline-block;
      width: 96px;
      height: 48px;
      margin-right: 28px;
      background: url('../assets/image/footer-icons.png') no-repeat 0 0;

      &:nth-child(1) {
        background-position: 0 -110px;
      }

      &:nth-child(2) {
        background-position: 0 -197px;
      }

      &:nth-child(3) {
        width: 107px;
        background-position: -252px -150px;
      }

      &:nth-child(4) {
        width: 107px;
        background-position: -252px -194px;
      }

      &:nth-child(5) {
        width: 42px;
        background-position: -161px -110px;
      }
    }
  }
</style> -->
