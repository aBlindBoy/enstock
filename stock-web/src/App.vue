<template>
	<div id="app">
		<div id="google_translate_element"  :style="$store.state.haslogin?'top: 18px;right: 280px;':'top: 18px;right: 184px;'"
		 style="position:absolute;z-index:2000;"></div>
		<router-view />
		<login></login>
	</div>
</template>

<script>
	import Login from "./components/Login";

	export default {
		name: "App",
		data() {
			return {
				myTitle: "标题",
				keyWords: "keyWords",
				description: "description",
			};
		},
		components: {
			Login,
		},

		// metaInfo () {
		//   return {
		//     title: this.myTitle,
		//     meta: [
		//       { vmid: 'keywords', name: 'keywords', content: this.keyWords },
		//       { vmid: 'description', name: 'description', content: this.description }
		//     ]
		//   }
		// },
		created() {
			console.log("--11111111111111111-",process.env);

			var styleName = localStorage.getItem('styleName')
			this.$store.commit('setSystemColor', styleName)





			// var that = this
			// 获取seo内容
			// this.$http.get('http://www.slhtc.com/seo/getSeo.action').then(res => {
			//   console.log(res)
			//   // 清楚选中数据
			//   that.myTitle = res.data.data[0].title
			//   that.keyWords = res.data.data[0].keyword
			//   that.description = res.data.data[0].keyword
			// }).catch(error => {
			//     console.log(error)
			// })
		},
		mounted() {
			// setInterval(()=>{
			
				
			// },500)
			// this.isEn()
			// var temp =  setInterval(()=>{
				
			// 	var code = this.getCookie('googtrans')
			// 	var dom = document.querySelector('.goog-te-menu-value > span')
			// 	if(code == '/auto/en'){
			// 		dom.innerText = '中'
			// 	}else if(code == '/auto/zh-CN'){
			// 		dom.innerText = 'En'
			// 	}else{
			// 		dom.innerText = 'En'
			// 	}
			// },500)
			
			
			
			// setTimeout(()=>{
			// 	var script = document.createElement('script')
			// 	script.src = 'https://translate.google.cn/translate_a/element.js?cb=googleTranslateElementInit'
			// 	document.body.appendChild(script)
			// 	console.log(script)
			// },500)
		},
		methods: {
			delCookie(name){
			    var exp = new Date();
			    exp.setTime(exp.getTime() - 1);
			    var cval=this.getCookie(name);
			    if(cval!=null)
			        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
			},
			setCookie(name, value) {
				var domain = document.domain;
				var Days = 30;
				var exp = new Date();
				exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
				document.cookie = name + "=" + escape(value) + ";expires=Session"  + ';path=/;domain='+''+domain;
			},
			getCookie(name) {
				var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
				if (arr = document.cookie.match(reg))
					return unescape(arr[2]);
				else
					return null;
			},
			isEn() {
					// var googFrameDom = document.querySelector('.goog-te-menu-frame')
					// googFrameDom.style.display = googFrameDom.style.display === 'block' ? 'none' :'block'
				// var code = this.getCookie('googtrans')
				// console.log(code)
				// this.delCookie('googtrans');
				// var newCode = code == '/auto/en' ? '/auto/zh-CN':'/auto/en'
				// location.reload();
				// this.setCookie('googtrans',newCode)
			}
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
