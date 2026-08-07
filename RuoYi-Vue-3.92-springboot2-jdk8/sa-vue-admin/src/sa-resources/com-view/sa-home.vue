<style scoped>
	.vue-box {
		background-color: #EEE;font-size: 14px;
		font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
	}
	.content-box {margin: 1em;padding: 1em;background-color: #FFF;color: #666;}
	.el-tag{border-radius: 1px;}
	/*800之下*/
	@media(max-width: 800px) {
		.kapian {
			width: 100% !important;
			margin-right: 0px !important;
		}
		.saoma {
			display: none;
		}
	}
	/* markdown格式 */
	.md-contetn{padding-left: 1.5em; line-height: 26px;}
</style>
<style>
	/* 多个ul时，切换那个啥 */
	.md-contetn h4{margin-left: -0.5em;}
	.md-contetn ul,.md-contetn ol{padding-left: 1em;}
	.md-contetn pre{padding: 5px; background-color: #EEE; font-family: "times new roman";}
</style>

<template>
	<div class="vue-box">
		<!-- 欢迎 -->
		<div class="content-box">
			<div style="padding: 1em; border-left: 5px #2D8CF0 solid; background-color: #EEE;">
				<big><big>Welcome to sa-admin (vue单页版) </big></big>
				<span style="display: inline-block; margin-left: 1em; color: #888;"> —— 一个多窗口后台模板，流畅、易上手、提高生产力</span>
				<el-link type="primary" href="http://sa-admin.dev33.cn/" target="_blank" style="margin-left: 14px;">点我访问iframe版</el-link>
				<span></span>
			</div>
		</div>
		<!-- 介绍 -->
		<div class="content-box">
			<div class="saoma" style="width: 20%; float: left; margin-right: 4%;">
				<el-divider content-position="left">
					<big><big>扫码加入QQ群交流</big></big>
				</el-divider>
				<div style="text-align: left;">
					<img src="http://dev33-yxzj.oss-cn-beijing.aliyuncs.com/dyc/img/2020/01/17/157924554064970545739.png" style="width: 60%; margin-left: 10%;">
				</div>
			</div>
			<div class="kapian" style="width: 35%; float: left; margin-right: 4%;">
				<el-divider content-position="left">
					<big><big>sa-admin (vue单页版) </big></big>
				</el-divider>
				<div>
					<p>
						本模板为 <el-link type="info" style="vertical-align: top;" href="http://sa-admin.dev33.cn/" target="_blank">sa-admin (iframe版)</el-link>
						的升级版，与iframe版相比，单页版主要有以下两个优点：.vue模块化、es6新语法，技术选型也更加贴近主流，
						如果你在使用时有任何问题，可以进行 
						<el-link type="info" style="vertical-align: top;" @click="sa_admin.showMenuById('1-11')">意见提交</el-link> 
						或点击下方的链接加入qq群（群里都是IT精英哦）
					</p>
					<br>
					<ul style="padding-left: 1em; line-height: 1.5em;">
						<li>
							<b>QQ群：</b>
							<el-link type="primary" href="https://jq.qq.com/?_wv=1027&k=5DHN5Ib" target="_blank">782974737 点击加入</el-link>
						</li>
						<li>
							<b>码云地址：</b>
							<el-link type="primary" href="https://gitee.com/click33/sa-vue-admin" target="_blank">https://gitee.com/click33/sa-vue-admin</el-link>
						</li>
						<li>
							<b>GitHub地址：</b>
							<el-link type="primary" href="https://github.com/click33/sa-vue-admin" target="_blank">https://github.com/click33/sa-vue-admin</el-link>
						</li>
						<!-- <li>
							<b>新增在线论坛：</b>
							<el-link type="primary" href="javascript:parent.sa_admin.showMenuById('1-11');">点击打开，在线快速提交意见反馈(新)</el-link>
						</li> -->
						<li>
							<b>新增需求墙：</b>
							<el-link type="primary" href="http://sa-app.dev33.cn/wall.html?name=sa-vue-admin" target="_blank">点击打开，在线快速提交需求(新)</el-link>
						</li>
						<li>
							<b>开源不易，求鼓励，给个star吧</b>
							<!-- <iframe src="https://ghbtns.com/github-btn.html?user=click33&repo=sa-admin&type=watch&count=true"
								style="width: 150px; height: 25px; display: inline-block; border: 0px; vertical-align: middle; margin-left: 10px;"></iframe> -->
						</li>
					</ul>
					<br>
				</div>

			</div>
			<div class="kapian" style="width: 35%; float: left;">
				<el-divider content-position="left">
					<big><big>技术选型</big></big>
				</el-divider>
				<ul style="padding-left: 1em; line-height: 1.5em;">
					<li><el-link type="primary" href="https://cn.vuejs.org/" target="_blank">JS引擎：Vue @2.6.11</el-link></li>
					<li><el-link type="primary" href="https://cli.vuejs.org/zh/" target="_blank">脚手架：@vue/cli：@4.0.5</el-link></li>
					<li><el-link type="primary" href="https://element.eleme.cn/#/zh-CN" target="_blank">UI框架：Element-UI @2.13.0</el-link></li>
					<li><el-link type="primary" href="http://layer.layui.com/" target="_blank">web弹层：layer @3.1.1</el-link></li>
					<li><el-link type="primary" href="https://www.swiper.com.cn/" target="_blank">切页动画：Swiper @4.5.0</el-link></li>
					<li><el-link type="primary" href="http://www.wangeditor.com/" target="_blank">富文本编辑器：wangEditor @3.1.1</el-link></li>
					<li><el-link @click="meile">没了：占空</el-link></li>
				</ul>
			</div>
			<div style="clear: both;"></div>
		</div>

		<!-- 功能介绍 -->
		<div class="content-box">
			<div class="kapian" style="width: 35%; float: left; margin-right: 4%;">
				<el-divider content-position="left">
					<big><big>功能介绍</big></big>
				</el-divider>
				<div class="md-contetn" v-html="md_js"></div>
				<li style="margin-left: 1.5em;">
					窗口模式测试：
					<a href="javascript:;" @click="sa_admin.is_show_tabbar = true;">多窗口模式</a> &nbsp;&nbsp;
					<a href="javascript:;" @click="sa_admin.is_show_tabbar = false;">单窗口模式</a>
				</li>
			</div>
			<div class="kapian" style="width: 55%; float: left;">
				<el-divider content-position="left">
					<big><big>使用步骤</big></big>
				</el-divider>
				<div class="md-contetn" v-html="md_sy"></div>
			</div>
			<div style="clear: both;"></div>
		</div>
		
		
		<!-- 更新日志 -->
		<div class="content-box">
			<el-divider content-position="left">
				<big><big>更新日志</big></big>
			</el-divider>
			<div class="kapian">
				<ul style="padding-left: 2em; padding-top: 1em; line-height: 1.5em;">
					
					<!-- ====================== 一个版本 ===================== -->
					<li>
						<b> <el-tag size="mini">版本 v1.0.3</el-tag></b>
						&nbsp;&nbsp;2020-03-05
					</li>
					<li>去除：去除tab双击刷新功能</li>
					<div><br></div>
					
					<!-- ====================== 一个版本 ===================== -->
					<li>
						<b> <el-tag size="mini">版本 v1.0.2</el-tag></b>
						&nbsp;&nbsp;2020-03-02
					</li>
					<li>新增：新增tab双击刷新功能</li>
					<div><br></div>
					
					<!-- ====================== 一个版本 ===================== -->
					<li>
						<b> <el-tag size="mini">版本 v1.0.1</el-tag></b>
						&nbsp;&nbsp;2020-2-29 
					</li>
					<li>去除vue-router</li>
					<li>增加sa_admin.getView函数，用于跨窗口通信</li>
					<div><br></div>
					
					<!-- ====================== 一个版本 ===================== -->
					<li>
						<b> <el-tag size="mini">版本 v1.0.0</el-tag></b>
						&nbsp;&nbsp;2020-2-29 
					</li>
					<li>第一个版本出炉</li>
					<div><br></div>
				</ul>
			</div>
		</div>
		
		
	</div>
</template>

<script>
	import marked from 'marked';
	export default {
		data() {
			// 功能介绍 
			let md_js = `
- 菜单：支持一、二、三级菜单，并开放一系列接口方便的使用js操作菜单
- 主题：目前保留八种主题：蓝色、绿色、白色、灰色、灰色-展开、pro钛合金、沉淀式黑蓝、简约式灰蓝（切换主题时，可自动保存你的喜好，下次打开时仍然有效）
- 折叠：折叠或收缩菜单，并且监听窗口大小变化，在拉伸窗口时自动折叠或收缩菜单，自动响应式
- tabbar栏：
	- 卡片堆积：多卡片自动堆积，与菜单双向关联，切换tab卡时自动显示左侧菜单
	- 拖动手势：tab卡支持拖拽手势，上拖全屏打开、下拽悬浮打开、左拽快速关闭
	- 右键菜单：在tab上右击，可以：刷新、复制、关闭、关闭其它、关闭所有、悬浮打开、全屏打开、取消
	- 双击菜单：双击tabbar空白处，可以显示添加新tab窗口
	- 切换动画：集成swiper，滑动、淡入、方块、3D流、3D翻转，五种高大上切换动画，任你选择！
	- 开放接口：开放一系列api，助你方便的使用js操作tabbar栏，具体请查看集成文档
- 锚链：tab切换自动更改hash锚链，同时监听锚链改变tab，可灵活的用鼠标前后键切换tab窗口 （如不需要此功能可在初始化时关闭）
- 窗口：可在初始化时，设置是否显示tabbar栏，来控制它是多窗口还是单窗口，具体见使用文档
- 更新：功能不断更新中... 你有好的想法也可以告诉我，加群一起交流吧
- 文档：使用说明，见文档
			`;
			// 使用步骤
			let md_sy = `
#### 1、获取源码
- 方式1：通过gitee或者github下载
- 方式2：通过Git Bash：
\`\`\`
	git clone https://github.com/click33/sa-vue-admin.git
\`\`\`

#### 2、运行
- 获取源码后，在项目根目录执行cmd命令：
\`\`\`
	安装依赖：npm i
	运行项目：npm run serve
\`\`\`
- 根据控制台输出的路径，访问浏览器，一般为：<a href="http://localhost:8080/" target="_blank">http://localhost:8080/</a>

#### 3、使用注意
- 在开发时，如无必要，请不要直接魔改模板的代码，以免在运行时出现意外bug
- 在 sa-resourecs 文件下，有个 sa-admin-init.js， 这是模板为你预留一个文件，用来对接你的业务逻辑
- 在这个文件里，你可以：
\`\`\`
	sa_admin.title = 'SA-后台模板';		// 设置模板标题
	sa_admin.logo_url = '图片地址';    	// 设置logo图标地址   默认值空, 代表使用：./admin-logo.png
	sa_admin.icon_url = '图片地址';   	 // 设置icon图标地址   默认值空, 代表使用：./favicon.ico
	sa_admin.initMenu(); // 初始化菜单, 不传参代表默认显示所有菜单 菜单在 ./sa-menu-list.js 里定义,
	// sa_admin.initMenu(['1', '1-1', '1-2', '4']); 	// 传入一个id数组, 显示指定菜单  
	// sa.checkAuth('a');		// 鉴权认证 （sa_admin内部封装了鉴权功能）
	sa_admin.init();	// 初始化模板, 必须调用
\`\`\`

- 以上示例在 sa-admin-init.js 文件里均有详细的注释说明，如果还要明白的地方，可以加入qq群询问



			`;
			return {
				md_js: marked(md_js),
				md_sy: marked(md_sy)
			}
		},
		methods: {
			// 没了
			meile: function() {
				this.$message({
					message: '没了还点',
					type: 'warning'
				});
			},
			// 显示文档页面
			openDoc: function() {
				parent.sp.selectMenu(11);
			}

		},
		mounted() {
			// document.querySelectorAll('.md-contetn').forEach(function(item) {
			// 	console.log(item.innerHTML);
				
			// 	console.log(ds);
			// 	let str = marked(ds);
			// 	item.innerHTML = str;
			// })
		}
	}
</script>

