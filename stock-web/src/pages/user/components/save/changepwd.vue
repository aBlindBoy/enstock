<template>
	<el-container class="user-center">
		<el-header class="user-header">
			<home-header></home-header>
		</el-header>
		<div class="usercot">
			<el-container class="main-wrapper">
				<el-aside width="200px">
					<menu-box></menu-box>
				</el-aside>
				<el-main style=" min-height: calc(100vh - 150px );">
					<div data-v-5d396ccb class="user-center-title" style="text-align: left;margin: 15px 10px 10px 10px;">
						<span data-v-5d396ccb class="iconfont icon-you" style="color: rgb(193, 24, 21); font-size: 18px; margin-right: 10px;"></span>
						{{$t('changePassword.title')}}
					</div>
					<div class="wrapper yhk-cont">
						<div class="box page-part transaction">
							
							<div class="box-contain clearfix">
								<div class="img-cont">
									<div class="box-img"></div>
								</div>
								<div class="auth-box">
									<el-form :hide-required-asterisk='true' :model="form" label-width="100px" ref="ruleForm" :rules="rule" class="demo-form-inline">
										<el-form-item prop="oldPwd">
											<el-input type='password' class="chongzhi-input" v-model="form.oldPwd" :placeholder="$t('changePassword.oldPasswordPlaceholder')">
											</el-input>
										</el-form-item>
										<el-form-item prop="newPsd">
											<el-input type='password' class="chongzhi-input" v-model="form.newPsd" :placeholder="$t('changePassword.newPasswordPlaceholder')">
											</el-input>
										</el-form-item>
										<el-form-item prop="newPsdQr">
											<el-input type='password' class="chongzhi-input" v-model="form.newPsdQr" :placeholder="$t('changePassword.againNewPasswordPlaceholder')">
											</el-input>
										</el-form-item>
									</el-form>
									<div slot="footer" class="dialog-footer">
										<el-button type="primary" :loading="isloading" @click="submit('ruleForm')">{{$t('changePassword.revise')}}</el-button>
									</div>
								</div>

							</div>
						</div>

					</div>
					<!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
				</el-main>
			</el-container>
		</div>
	</el-container>

</template>

<script>
	import HomeHeader from '../../../../components/HeaderOrder'
	import MenuBox from '../menu'
	import * as api from '../../../../axios/api'

	export default {
		components: {
			HomeHeader,
			MenuBox
		},
		props: {},
		data() {
			// let validatePass = (rule, value, callback) => {
			//   if (value === '') {
			//     callback(new Error('请输入银行卡号'))
			//   } else {
			//     let myreg = /^([1-9]{1})(\d{14,18})$/ // 卡号校验
			//     if (!myreg.test(value)) {
			//       callback(new Error('请输入正确的银行卡号'))
			//     }
			//     callback()
			//   }
			// }
			return {
				hasAuth: false,
				isloading: false,
				form: {
					oldPwd: '',
					newPsd: '',
					newPsdQr:''
				},
				rule: {
					newPsd: [{
							required: true,
							message: this.$t('changePassword.passwordPlease'),
							trigger: 'blur'
						},
						{
							min: 6,
							message: this.$t('changePassword.passwordFormat'),
							trigger: 'blur'
						}
					]
				}
			}
		},
		watch: {},
		computed: {},
		created() {},
		mounted() {
			this.$store.state.userMenu = '2-11'
		},
		methods: {
			submit(formName) {
				if(this.form.newPsd !== this.form.newPsdQr){
					this.$message.error( this.$t('changePassword.passwordNotMatch'))
					return 
				}
				// 提交
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						// 修改密码
						let opts = {
							oldPwd: this.form.oldPwd,
							newPwd: this.form.newPsd
						}
						let data = await api.changePassword(opts)
						if (data.status === 0) {
							this.$message.success(data.msg)
						} else {
							this.$message.error(data.msg)
						}
					} else {
						return false
					}
				})
			}
		}
	}
</script>
<style lang="less" scoped>
	/deep/ .el-input__inner {
		border-radius: 50px;
	}


	.wrapper .demo-form-inline[data-v-62d7d9d4]{
		margin: 0;
	}
	.wrapper[data-v-62d7d9d4]{
		padding: 0;
	}
	.transaction{
		width: 100%;
	}
	.wrapper .demo-form-inline[data-v-62d7d9d4]{
		width: 90%;
	}
	.wrapper .auth-box .dialog-footer[data-v-62d7d9d4]{
		width: 90%;
		margin: 0;
	}
	.wrapper .auth-box .dialog-footer .el-button[data-v-62d7d9d4]{
		width: 89%;
		border-radius: 50px;
	}

	.red-bg {
			
		.yhk-cont {
		

			display: flex;
			justify-content: center;
			align-items: center;
			.img-cont{
				display: flex;
				align-items: center;
				justify-content: center;
				.box-img {
					
					width: 100px;
					height: 100px;
					background: url("../../../../assets/image/suo2.png") no-repeat center center;
				}
			}
			

			.yhk-list {
				padding: 30px;
				width: 100%;
				margin: 0 60px;
				border-bottom: 1px solid #ccc;
			}
		}

		.yanzeng-cont {

			display: flex;

			.tongguo,
			.renzheng {
				border-radius: 3px;
				padding: 2px 5px;
				display: inline-block;
				background-color: rgb(4, 130, 62);
				margin: 0 5px;
				color: #fff;
			}

			.renzheng {
				background-color: rgb(230, 0, 62);
			}

			.yanzeng-left {
				height: 60px;
				width: 60px;

				img {
					width: 100%;
					height: 100%;
				}
			}

			.yanzeng-right {
				margin-left: 10px;
			}

			.yanzeng-right>div {
				margin-bottom: 20px;
				// margin-left: 10px;
			}

			.zfz {
				display: flex;

				.fan {
					margin-left: 30px;
				}
			}
		}

		.youyi {
			color: #000;
		}

		.chongzhi-bizhi {
			.right {
				width: 106px;
				height: 134px;

				background: url("../../../../assets/image/youqingtishi1.png") no-repeat;
			}

			margin-top: 50px;

			background-color: #e5e5e5;
			display: flex;
			padding: 40px;
			justify-content: space-between;

			.chongzhi-bizhi-cont .chongzhi-item {
				margin: 15px 0;
				color: #000;

				.circle {
					display: inline-block;
					width: 15px;
					text-align: center;
					height: 15px;
					border: 1px solid rgb(121, 121, 121);
					border-radius: 50%;
					line-height: 15px;
				}
			}
		}

		.chongzhi-cont {
			position: relative;

			.left {
				position: absolute;
				left: 50%;
				transform: translateX(-50%);
			}

			.xianshi-cont {
				display: flex;
				justify-content: space-between;
			}

			.chongzhi-img {
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.chongzhi-type {
				display: flex;
				margin-top: 20px;
				margin-bottom: 20px;

				.chongzhi-type-item {
					margin-right: 20px;
				}
			}

			.chongzhi-type>span {
				display: inline-block;
				width: 85px;
			}

			.chongzhi-btn-cont {
				display: flex;
				justify-content: center;
				margin-top: 10px;

				.chongzhi-btn {
					width: 80%;
					border-radius: 50px;
					text-align: center;
					height: 40px;
					background-color: #c11815;
					color: #fff;
					line-height: 40px;
					font-size: 14px;
					font-family: Microsoft YaHei;
					font-weight: 400;
					margin-top: 20px;
				}
			}

			.chongzhi-input {
				display: flex;
				align-items: center;
				width: 90%;
				background: none;
			}

			.chongzhi-input>span {
				display: inline-block;
				width: 100px;
			}
		}
	}

	.black-bg {
		.yhk-cont {
			display: flex;
			justify-content: center;
			align-items: center;
			
			.img-cont{
				display: flex;
				align-items: center;
				justify-content: center;
				.box-img {
					
					width: 100px;
					height: 100px;
					background: url("../../../../assets/image/suo.png") no-repeat center center;
				}
			}
			
			
			.yhk-list {
				padding: 30px;
				width: 100%;
				margin: 0 60px;
				border-bottom: 1px solid #383838;
			}
		}

		.el-button--primary {
			border: none;
			background-color: rgb(2, 77, 161) !important;
		}

		.yanzeng-cont {
			display: flex;

			.tongguo,
			.renzheng {
				padding: 2px 5px;
				display: inline-block;
				background-color: rgb(4, 130, 62);
				border-radius: 2px;
				margin: 0 5px;
			}

			.renzheng {
				background-color: rgb(230, 0, 62);
			}

			.yanzeng-left {
				height: 60px;
				width: 60px;

				img {
					width: 100%;
					height: 100%;
				}
			}

			.yanzeng-right {
				margin-left: 10px;
			}

			.yanzeng-right>div {
				margin-bottom: 20px;
				// margin-left: 10px;
			}

			.zfz {
				display: flex;

				.fan {
					margin-left: 30px;
				}
			}
		}

		.youyi {
			color: #fff;
		}

		.chongzhi-bizhi {
			.right {
				width: 106px;
				height: 134px;

				background: url("../../../../assets/image/youqingtishi.png") no-repeat;
			}

			margin-top: 50px;

			background-color: #191e2b;
			display: flex;
			padding: 40px;
			justify-content: space-between;

			.chongzhi-bizhi-cont .chongzhi-item {
				margin: 15px 0;
				color: #7e8086;

				.circle {
					display: inline-block;
					width: 15px;
					text-align: center;
					height: 15px;
					border: 1px solid rgb(121, 121, 121);
					border-radius: 50%;
					line-height: 15px;
				}
			}
		}

		.chongzhi-cont {
			position: relative;

			.left {
				position: absolute;
				left: 50%;
				transform: translateX(-50%);
			}

			.xianshi-cont {
				display: flex;
				justify-content: space-between;
			}

			.chongzhi-img {
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.chongzhi-type {
				display: flex;
				margin-top: 20px;
				margin-bottom: 20px;

				.chongzhi-type-item {
					margin-right: 20px;
				}
			}

			.chongzhi-type>span {
				display: inline-block;
				width: 85px;
			}

			.chongzhi-btn-cont {
				display: flex;
				justify-content: center;
				margin-top: 10px;

				.chongzhi-btn {
					width: 80%;
					border-radius: 50px;
					text-align: center;
					height: 40px;
					background-color: #c11815;
					color: #fff;
					line-height: 40px;
					font-size: 14px;
					font-family: Microsoft YaHei;
					font-weight: 400;
					margin-top: 20px;
				}
			}

			.chongzhi-input {
				display: flex;
				align-items: center;
				width: 90%;
				background: none;
			}

			.chongzhi-input>span {
				display: inline-block;
				width: 100px;
			}
		}
	}




	.code {
		font-size: 12px;
		color: #999;
	}

	.auth-box {
		margin-top: 20px;

		.el-row {
			margin-bottom: 10px;
		}
	}

	.authed-box {
		.el-row {
			padding-top: 40px;
			width: 400px;
			margin: 0 auto;
		}
	}

	.wrapper {
		padding: 40px 100px 80px 0;

		.demo-form-inline {
			width: 400px;
			margin: 20px auto;
		}

		.auth-box {
			.dialog-footer {
				width: 400px;
				margin: 0 auto;

				.el-button {
					margin-left: 100px;
					width: 300px;
				}
			}
		}

		.prompt-box {
			padding-bottom: 20px;
			color: #f44336;

			p {
				line-height: 24px;
			}
		}

		.rule-box {
			margin-top: 30px;
			line-height: 26px;
			margin-left: 120px;
		}
	}
</style>
