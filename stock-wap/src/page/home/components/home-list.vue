<template>
  <div class="wrapper">
    <ul class="table-list" v-if="false">
      <li class="title">
        <div>
          <ul class='clearfix'>
            <li class="li-title">Stock</li>
            <li class="li-base">Latest price</li>
            <li class="li-base">Quote change</li>
            <!-- <li class="li-base">
            </li> -->
          </ul>
        </div>

      </li>
    </ul>
    <ul>
      <li v-for="item in list" :key="item.key">
        <div class="box-gp" @click="toDetail(item.code)">
          <!-- <div class="name">
            {{item.title}}<span class="code">{{item.code}}</span>
            <span class="pull-right">{{item.status==1?'可買入':'不可買'}}</span>
          </div> -->
          <div class="con clearfix">
            <div class="col-xs-4">
              <p><span>{{item.title}}</span></p>
              <p class="title"><span>{{item.code}}</span></p>
            </div>
            <div class="col-xs-4">
              <p class="number red">{{item.nowPrice}}</p>
              <p class="title">Latest price</p>
            </div>
            <div class="col-xs-4">
              <p class="number red">{{item.gain}}</p>
              <p class="title">Quote change</p>
            </div>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data () {
    return {
      list: [
        { title: '浦發金融', code: '600000', nowPrice: '11.01', gain: '2.04%', high: '11.01', low: '10.07', status: 1 },
        { title: '白雲航空站', code: '600000', nowPrice: '11.01', gain: '2.04%', high: '11.01', low: '10.07', status: 1 },
        { title: '東風汽車', code: '600000', nowPrice: '11.01', gain: '2.04%', high: '11.01', low: '10.07', status: 1 },
        { title: '中國國貿', code: '600000', nowPrice: '11.01', gain: '2.04%', high: '11.01', low: '10.07', status: 1 }
      ]
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {},
  methods: {
    toDetail () {
      // 去列錶頁面
      this.$router.push('/listdetail')
    },
    getDetail () {

    }
  }
}
</script>
<style lang="less" scoped>

</style>
