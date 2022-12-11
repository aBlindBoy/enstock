<template>
	<div id="app">
		<div id="google_translate_element"  :style="$store.state.haslogin?'top: 18px;right: 280px;':'top: 18px;right: 184px;'"
		 style="position:absolute;z-index:2000;"></div>
		<router-view />
	</div>
</template>

<script>
	export default {
		name: "App",
		data() {
			return {
			};
		},
		components: {
		},

		created() {
			var styleName = localStorage.getItem('styleName')
			this.$store.commit('setSystemColor', styleName)
			var locale = localStorage.getItem('locale')
			this.$i18n.locale = locale
		},
		mounted() {
			
		},
		methods: {
		
		}
	};
</script>

<style>
	/* 
@import url("./style/shallowtheme.css"); */
	@import url("./style/deeptheme.css");
	@import url("./style/shallowtheme.css");
	@import "//at.alicdn.com/t/font_2023533_ngjjonxzq9m.css";
	@import url("./style/deep.css");
	@import url("./style/shallow.css");

	#app {
		font-family: "Microsoft Yahei", "Avenir", Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		color: #000;
	}
</style>
