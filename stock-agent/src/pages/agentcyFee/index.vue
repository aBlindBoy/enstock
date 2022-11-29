<template>
  <el-container>
     <el-main>
       <div class="content-wrapper">
          <Containter></Containter>
          <!-- <home-footer></home-footer> -->
       </div>
      </el-main>
  </el-container>
</template>
<script>
import HomeHeader from '@/components/HeaderOrder'
import HomeFooter from '@/components/Footer'
import Containter from './components/table.vue'

export default {
  components: {
    HomeHeader,
    HomeFooter,
    Containter
  },
  props: {},
  data () {
    return {}
  },
  watch: {},
  computed: {},
  methods: {},
  created () {
    this.$store.state.activeIndex = 'agentcyFee'
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
