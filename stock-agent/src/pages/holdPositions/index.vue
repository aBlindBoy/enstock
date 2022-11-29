<template>
  <el-container>
    <el-main>
      <div class="content-wrapper">
        <Containter v-if="panel=== '1'" :type="1"></Containter>
        <Containter2 v-if="panel=== '2'" :type="0"></Containter2>
        <Containter3 v-if="panel=== '3'" :type="1"></Containter3>
        <Containter4 v-if="panel=== '4'" :type="0"></Containter4>
        <Containter5 v-if="panel=== '5'" :type="1"></Containter5>
        <Containter6 v-if="panel=== '6'" :type="0"></Containter6>
      </div>
    </el-main>


  </el-container>
</template>
<script>
import HomeHeader from '@/components/HeaderOrder'
import HomeFooter from '@/components/Footer'
import Containter from './components/table-hold.vue'
import Containter2 from './components/table-sell.vue'
import Containter3 from './components/indextable-hold.vue'
import Containter4 from './components/indextable-sell.vue'
import Containter5 from './components/futurestable-hold.vue'
import Containter6 from './components/futurestable-sell.vue'

export default {
  components: {
    HomeHeader,
    HomeFooter,
    Containter,
    Containter2,
    Containter3,
    Containter4,
    Containter5,
    Containter6
  },
  props: {},
  data () {
    return {
      activetype: 'first',
      panel: '1'
    }
  },
  watch: {
    $route(val) {
      if (val.query.type) {
        this.panel = val.query.type
      }
    }
  },
  computed: {},
  methods: {
    handleClick () {

    }
  },
  created () {
    this.$store.state.activeIndex = 'holdPositions'
  },
  mounted () {

  }
}
</script>
<style scoped>
 .content-wrapper{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    border: 1px solid #ebeef5;
    padding: 20px;
  }
</style>
