<template>
  <el-container>
    <el-main>
      <Containter v-if="panel === '1'" :type="1"></Containter>
      <Containter2 v-if="panel === '2'" :type="0"></Containter2>
      <Containter3 v-if="panel === '3'" :type="0"></Containter3>
    </el-main>
  </el-container>
</template>
<script>
import HomeHeader from '@/components/HeaderOrder'
import HomeFooter from '@/components/Footer'
import Containter from './components/content.vue'
import Containter2 from './components/index-content.vue'
import Containter3 from './components/futures-content.vue'

export default {
  components: {
    HomeHeader,
    HomeFooter,
    Containter,
    Containter2,
    Containter3
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
  methods: {},
  created () {
    this.$store.state.activeIndex = 'statistics'
    console.log(this.$route.query.type)

    

  },
  mounted () {

  }
}
</script>
<style>

</style>
