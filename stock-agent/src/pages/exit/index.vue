<template>
  <el-container>
    <el-main>
      <Containter></Containter>
      <!-- <home-footer></home-footer> -->
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
    this.$store.state.activeIndex = 'exit'
  },
  mounted () {

  }
}
</script>
<style>

</style>
