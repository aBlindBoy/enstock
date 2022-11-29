<template>
  <el-container>
    <el-header>
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <el-main>

        <div class="introduce">
          <h2 class="title text-center">COMPANY PROFILE</h2>
          <!-- <h2 class="title text-center">公告</h2> -->
          <div class="introduce-body">
            <ul>
              <li v-for="i in artList.list" :key="i.key">
                <div class="list-box">
                  <el-row :gutter="15">
                    <el-col :span="3">
                      <span class="date">{{(new Date(i.addTime)).getDate()}}</span>
                      <p class="date-p">
                        <span class="year">{{(new Date(i.addTime)).getFullYear()}}.</span>
                        <span class="month">{{(new Date(i.addTime)).getMonth()}}</span>
                      </p>
                    </el-col>
                    <el-col :span="15">
                      <p class="title">
                        <a @click="toDetail(i)" title="check the details" href="javascript:;">{{i.artTitle}}</a>
                      </p>
                      <p class="summary"><a @click="toDetail(i)" title="check the details" href="javascript:;">{{i.artSummary}}</a>
                      </p>
                      <p class="author">
                        <span class="pull-left">{{i.author}}</span>
                        <span class="pull-right"><i class="iconfont icon-yanjing"></i>{{i.hitTimes?i.hitTimes:0}}</span>
                      </p>
                    </el-col>
                    <el-col class="art-img text-center" :span="6">
                      <img :src="i.artImg" alt="">
                    </el-col>
                  </el-row>

                </div>
              </li>
            </ul>
            <div class="text-center">
              <a class="load-next" @click="loadNext" href="javascript:;">load more</a>
            </div>

          </div>
        </div>
        <home-footer :siteInfo="siteInfo"></home-footer>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  import HomeFooter from '../../components/Footer'
  // import DetailNotice from './components/components'
  import * as api from '../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter
      // DetailNotice
    },
    props: {},
    data () {
      return {
        loading: false,
        pageNum: 1,
        pageSize: 10,
        artList: {
          list: []
        }, // 公告列表
        siteInfo: {},
        total: '', // 公告总条数
        timer: null
      }
    },
    watch: {},
    computed: {},
    mounted () {
      this.getArtList()
      this.getInfoSite()
      this.$store.state.activeIndex = 'notice'
    },
    methods: {
      async getInfoSite () {
        // 获取站点信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
        } else {
          this.$message.error(result.msg)
        }
      },
      async getArtList () {
        
        // 获取公告列表
        let opts = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let result = await api.getArtList(opts)
        if (result.status === 0) {
          result.data.list.forEach(element => {
            this.artList.list.push(element)
          })
          this.total = result.data.total
        } else {
          this.$message.error(result.msg)
        }
      },
      async loadNext () {
        // 加载下一页
        if (this.loading || this.artList.list.length <= this.total) {
          this.$message.error('沒有更多了')
          return
        }
        this.loading = true
        this.pageNum = this.pageNum + 1
        await this.getArtList()
        this.loading = false
      },
      toDetail (row) {
        this.$router.push({
          path: '/noticedetail',
          query: {
            id: row.id
          }
        })
      }
    }
  }
</script>
<style lang="less" scoped>
  .introduce {
    padding: 20px;
    min-height: 400px;

    .title {
      font-size: 20px;
      line-height: 38px;
      width: 100%;
      overflow: hidden; /*超出部分隐藏*/
      white-space: nowrap; /*不换行*/
      text-overflow: ellipsis; /*超出部分文字以...显示*/
    }

    .introduce-body {
      line-height: 28px;
      padding-top: 30px;
      // text-indent: 2em;
      font-size: 14px;
      padding: 20px;
      box-shadow: 1px 1px 15px rgba(0, 0, 0, 0.4);
      width: 1000px;
      margin: 50px auto;
    }
  }

  .list-box {
    border-bottom: 1px solid #333;
    height: 146px;
    padding: 20px 25px;

    .title {
      font-size: 20px;
    }

    .summary {
      font-size: 16px;
      color: #b4b4b4;
      margin: 10px 0;
      text-indent: 2em;
      height: 52px;
      /*text-overflow: -o-ellipsis-lastline;*/
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .author {
      font-size: 12px;
      color: #777777;
    }

    .date {
      font-size: 60px;
      font-weight: 800;
      line-height: 66px;
      display: inline-block;
      margin-top: 6px;
    }

    .date-p {
      margin-top: 10px;

      .year {
        font-size: 22px;
        margin-top: 10px;
      }

      .month {
        font-size: 20px;
      }
    }

    .art-img {
      height: 146px;

      img {
        max-height: 100%;
      }
    }

    .load-next {
      padding: 5px 10px;
      display: inline-block;
    }
  }

  .icon-yanjing {
    margin-right: 10px;
    vertical-align: middle;
  }
</style>
