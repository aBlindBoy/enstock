<template>
  <div id="app" :class="`${$state.theme === 'red' ? 'red-theme' : 'black-theme'}`">
    <div :class="`header-box`" v-if="hasHeader">
      <mt-header :title="title">
        <mt-button icon="back" slot="left" @click="$router.go(-1)"></mt-button>
        <template v-if="iconRight == 'search'">
          <img slot="right" class="search-right" src="./assets/ico/fangdajing.png" alt="">
        </template>
        <template v-else>
          <mt-button icon="more" slot="right"></mt-button>
        </template>
      </mt-header>
    </div>
    <div class="body-box">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  created () {
    let locale = localStorage.getItem("locale") 
    if (locale) {
      this.$i18n.locale =  locale
    }
    let title = this.$route.meta.title || 'Home'
    this.title = title

    if (this.$route.meta.hasHeader) {
      this.hasHeader = true
    } else{
      this.hasHeader = false
    }
    if (this.$route.meta.iconRight) {
      this.iconRight = this.$route.meta.iconRight
    } else{
      this.iconRight = 'default'
    }
  },
  watch: {
    $route(val) {
      let title = val.meta.title || 'Home'
      this.title = title

      if (val.meta.iconRight) {
        this.iconRight = val.meta.iconRight
      } else{
        this.iconRight = 'default'
      }
      if (val.meta.hasHeader) {
        this.hasHeader = true
      } else{
        this.hasHeader = false
      }
    }
  },
  data() {
    return {
      title: 'Home',
      hasHeader: false,
      iconRight:'default'
    }
  }
}
</script>

<style lang="less" scoped>
  #app{
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    .header-box{
      width: 100%;
      height: 1rem;
      /deep/.mint-header{
        height: 100%;
        background-color: rgba(20,45,55,0.3);
        .is-left{
          .mintui{
            font-size: 20px;
          }
        }
        .mint-header-title{
          font-size: 0.36rem;
          color:rgba(255,255,255,1);
        }
        
      }
      button{
        outline: none;
      }
    }
    .body-box{
      width: 100%;
      height: calc(100% - 1rem);
      box-sizing: border-box;
      overflow-y: auto;
    }
    &.red-theme{
      background: #E9E9E9;
      /deep/.mint-header{
        background: none;
        .mint-header-title{
          font-size: 0.36rem;
          color:#212121;
        }
        .mintui{
          color:#212121;
        }
      }
    }
    &.black-theme{
      background: #16171d;
    }
  }
  .search-right {
    width: .3rem;
    height: .3rem;
  }
.mint-search-list{
    position: relative !important;
  }
</style>
