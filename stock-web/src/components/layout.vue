<template>
  <div class="">
      <div class="content">
          <div class="left">
              <slot name="left"></slot>
          </div>
          <div class="right">
              <slot name="right"></slot>
          </div>
      </div>
  </div>
</template>

<script>
export default {
  name: '',
  components: {
  },
  props: {
  },
  data () {
    return {
    }
  },
  computed: {
  },
  watch: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style scoped lang="less">
.content{
    width: 100%;
    // height: ;
 
    display: flex;
    justify-content: space-between;
    .left{
        width: 47%;
    }
    .right{
        width: 47%;
    }
}
</style>
