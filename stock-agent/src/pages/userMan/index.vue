<template>
  <el-container>
    <el-main>
      <ContainterTable></ContainterTable>
      <!-- <home-footer></home-footer> -->
    </el-main>
  </el-container>
</template>
<script>
import HomeHeader from '@/components/HeaderOrder'
import HomeFooter from '@/components/Footer'
import ContainterTable from './components/table'

export default {
  components: {
    HomeHeader,
    HomeFooter,
    ContainterTable
  },
  props: {},
  data () {
    return {}
  },
  watch: {},
  computed: {},
  methods: {},
  created () {
    this.$store.state.activeIndex = 'userMan'
  },
  mounted () {

  }
}
</script>
<style>

</style>
