<template>
  <div class="newFooter">
      <div class="footer">
         <div class="cot">
             <div class="left">
                 <div><span class="iconfont icon-dianhua"></span>Contact Method: {{siteInfo.sitePhone}}</div>
                 <!-- <div><span class="iconfont icon-qq"></span>QQ號:{{siteInfo.siteQq}}</div> -->
                 <div><span class="iconfont icon-ziyuan"></span>Mail: {{siteInfo.siteHost}}</div>
             </div>
             <div class="mid">
                 <div class="text">Risk Warning: Investment involves risks, the price of securities may rise or fall, and may even become worthless in extreme cases.
                     Before making any investment decision, investors should understand the potential risks of securities investment and confirm that they have the corresponding risk tolerance.
                     And evaluate your risk appetite, relevant experience and knowledge before deciding whether to trade. Any investment involves risks and losses that may exceed the principal invested.
                     Past performance of individual stocks, industries, industries, markets and any investment product does not guarantee future performance returns.</div>
                 <!-- <div class="copyright">© 2020 嘉信投資國際有限公司 All rights reserved  嘉信投資（國際）投資管理公司</div> -->
             </div>
             <div class="right">
                 <div class="app">Official download</div>
                 <div class="pic">
                     <div class="pic_l">
                         <div class="adrpic"><img :src="siteInfo.siteAndroidImg" alt=""></div>
                         <div class="adrtext">Official Download (Android)</div>
                         <a class="iostext" :href="siteInfo.siteAndroidUrl">Link to download (Android)</a>
                     </div>
                     <div class="pic_r">
                         <div class="iospic"><img :src="siteInfo.siteIosImg" alt=""></div>
                         <div class="iostext">Official Download (Apple)</div>
                         <a class="iostext" :href="siteInfo.siteIosUrl">Link to download (Apple)</a>
                     </div>
                 </div>
                 <!-- <div class="pc"><span class="iconfont icon-pc"></span>官方PC下載</div> -->
             </div>
         </div>
         <div class="fot">
             <div class="fotcot" style="width:100%;text-align:center">
                 <div class="btm" style="width:100%;line-height:39px">
                     <p>© 2020. VICTORY GROUP is one brand of SNB Finance Holdings Limited.</p>
                    
                 </div>
             </div>
         </div>
      </div>
  </div>
</template>

<script>
import * as api from '../axios/api'

export default {
  name: 'newFooter',
  components: {
  },
  props: {
  },
  data () {
    return {
        siteInfo: {}, // 站點信息
    }
  },
  computed: {
  },
  watch: {
  },
  created () {
      this.getInfoSite()
  },
  mounted () {
  },
  methods: {
      async getInfoSite () {
        // 獲取站點信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
          console.log(this.siteInfo)
        } else {
          this.$message.error(result.msg)
        }
      },
  }
}
</script>

<style scoped lang="less">
</style>
