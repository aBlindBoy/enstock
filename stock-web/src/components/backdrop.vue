<template>
  <div class="backdrop" >
      <div class="bgdrop">
          <slot></slot>
      </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  
  name: 'backdrop',
  components: {
  },
  props: {
    isbgd:{
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
    }
  },
  computed: {
      ...mapState({
      color:(state)=>state.systemColor
    })
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
.backdrop{
  width: 100%;  
}
 .bgdrop{
    position: absolute;
    top: 0;
		left: 0;
    width: 100%;
    height: calc(100vh);
    // background: url(../../static/newimg/bgdrop0.png) no-repeat center ;
    // background-image: url(../../static/newimg/bgdrop1.jpg) no-repeat center , rgba(0,0,0,0.1);
    //
    background-image:  linear-gradient( rgba(0, 0, 0, 0.65), rgba(0, 0, 0, 0.65)), url("../../static/newimg/bgdrop2.png");
    // background:rgba(0,0,0,0.5); 

    background-size: 100% 100%;
  }

</style>
