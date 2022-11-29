<template>
  <div class="footer bg-f5">
    <div class="link-web">
      <a v-for="item of link" target="_blank" :key="item.key" :href="item.linkurl">{{item.linkname}}</a>
    </div>
    <div class="footer-contain">
      <div class="footer-link container clearfix">
        <div class="footer-logo">
          <!-- <img src="../assets/imgs/sula_foot.png" alt="logo"> -->
        </div>
        <div class="footer-list">
          <ul>
            <li v-for="item of listData" :key="item.key">
              <div class="title">{{item.title}}</div>
              <ul>
                <li v-for="i of item.children" :key="i.key"><a hrer="#"
                                                               @click="tomarkPage(i.route,i.type)">{{i.title}}</a></li>
              </ul>
            </li>
            <!-- <li>
              <div class="title">招募</div>
              <ul>
                <li><a hrer="#">认证模式</a></li>
                <li><a hrer="#">车队认证</a></li>
                <li><a hrer="#">加入我们</a></li>
              </ul>
            </li>
            <li>
              <div class="title">关于</div>
              <ul>
                <li><a hrer="#">关于我们</a></li>
              </ul>
            </li>
            <li>
              <div class="title">合作</div>
              <ul>
                <li><a hrer="#">资源共享</a></li>
                <li><a hrer="#">多项服务</a></li>
                <li><a @click="tomark" hrer="#cooperationProcess">合作流程</a></li>
              </ul>
            </li>
            <li>
              <div class="title">资讯</div>
              <ul>
                <li><a hrer="#">公司动态</a></li>
                <li><a hrer="#">行业动态</a></li>
              </ul>
            </li>
            <li>
              <div class="title">联系</div>
              <ul>
                <li><a hrer="#">联系我们</a></li>
              </ul>
            </li> -->
          </ul>
        </div>
      </div>
      <div class="footer-copyright">
        <p>Coperight @2018 www.sulahtc.com All Rights Reversed</p>
        <p>上海速拉网络科技有限公司版权所有</p>
        <p>沪IPC备 17032097号</p>
      </div>
    </div>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {},
  data () {
    return {
      link: [],
      listData: [
        {
          title: '首页',
          children: [
            {
              title: '产品首页',
              route: '/home'
            }
          ]
        }, {
          title: '招募',
          children: [
            {
              title: '认证模式',
              route: '/fleet'
            }, {
              title: '车队认证',
              route: '/fleet'
            }, {
              title: '加入我们',
              route: '/fleet'
            }
          ]
        }, {
          title: '关于',
          children: [
            {
              title: '关于我们',
              route: '/aboutus'
            }
          ]
        }, {
          title: '合作',
          children: [
            {
              title: '资源共享',
              route: '/cooperation'
            },
            {
              title: '多项服务',
              route: '/cooperation'
            }, {
              title: '合作流程',
              route: '/cooperation'
            }
          ]
        }, {
          title: '资讯',
          children: [
            {
              title: '公司动态',
              route: '/newsUpdate',
              type: 0
            }, {
              title: '行业动态',
              route: '/newsUpdate',
              type: 1
            }
          ]
        }, {
          title: '联系',
          children: [
            {
              title: '联系我们',
              route: '/contact'
            }
          ]
        }
      ]
    }
  },
  watch: {},
  computed: {},
  methods: {
    tomarkPage (link, type) {
      if (type) {
        this.$store.state.selectedType = type
      }
      this.$router.push(link)
    },
    async getList () {
      // this.link = await api.getListWithNameAndUrl()
    }
  },
  created () {},
  mounted () {
    // 底部连接
    // var url = 'http://www.slhtc.com/Link/getListWithNameAndUrl.action'
    // this.$http.get(url).then(res => {
    //   this.link = res.data.data
    // }).catch(err => {
    //   console.log(err)
    // })
    this.getList()
  }
}
</script>
<style lang="stylus" scoped>
  .footer
    .link-web
      margin-bottom 25px
      text-align center

      a
        color #ff4200
        font-size 18px
        padding 0 22px
        border-right 2px solid #ff4200

        &:last-child
          border none

    .footer-contain
      background rgb(17, 17, 17)

      .footer-link
        height 227px

        > div
          padding-top 60px

        .footer-logo
          margin-left 80px
          width 460px
          float left

          img
            float left

        .footer-list
          float left
          text-align left

          > ul > li
            width 110px
            line-height 30px
            float left

            .title
              color #ffffff
              font-size 16px

            li a
              font-size 16px
              color #aeaeae
              cursor pointer

      .footer-copyright
        height 83px
        padding-top 25px
        background-color rgb(31, 32, 36)

        p
          text-align center
          font-size 12px
          color #a6a6a6
          line-height 20px

</style>
