<template>
    <div class="wrapper">
        <mt-radio
            title="radio list"
            v-model="locale"
            align="right"
            :options="options">
        </mt-radio>
    </div>
  
  </template>
  
  <script>
  
  export default {
    components: {
   
    },
    props: {},
    data () {
      return {
        options:[
                {
                    label: 'English',
                    value: 'en',
                },
                {
                    label: '中文繁體',
                    value: 'zh-TW'
                }
        ],
        locale:""
      }
    },
    watch:{
        locale(newValue,oldValue){
            this.$i18n.locale = newValue
            this.locale = newValue
            localStorage.setItem("locale",newValue) 
        }
    },
    created () {
        let locale = localStorage.getItem("locale") 
        if (locale) {
            this.locale = locale
        }
    },
    mounted () {
   
     
    },
  
    beforeDestroy () {
    
    },
    computed: {},

    methods: {
    }
  }
  </script>
  <style lang="less" scoped>
    
  </style>
  