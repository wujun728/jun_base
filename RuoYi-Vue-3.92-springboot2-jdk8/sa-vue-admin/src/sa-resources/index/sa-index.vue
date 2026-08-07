<!-- 
	sa-admin的index文件 
	其中：sa-admin总盒子，sa-admin-frame为模板框架，sa-admin-view为视图盒子
 -->
<template>
	<div class="sa-admin">
		<!-- 模板框架 -->
		<div class="app" :class="['theme-0', 'theme-' + themeV, (is_fold ? 'fold' : ''), (themeToggling ? 'themeToggling' : ''), (is_show_tabbar ? '' : 'hide-tabbar')]">
			<!-- 左 -->
			<div class="nav-left">
				<!-- 左上 -->
				<div class="nav-left-top" :title="title" @click="showTab(homeTab)">
					<img :src="logo_url" class="admin-logo" v-if="logo_url != null && logo_url != ''">
					<img src="./admin-logo.png" class="admin-logo" v-else>
					<!-- <span v-else style="margin-right: 25px;" title="无logo时的占位">&emsp;</span> -->
					<span class="nav-title" style="font-size: 1.05em;">{{title}}</span>
				</div>
				<!-- 左下 -->
				<div class="nav-left-bottom">
					<div class="menu-box-1">
						<div class="menu-box-2">
							<!-- 菜单 -->
							<el-menu class="el-menu-style-1" :unique-opened="unique_opened" :default-active="default_active"
								:default-openeds="default_openeds" :collapse="is_fold" @select="selectMenu">
								<div v-for="(menu, index) in menuList" :key="index">
									<!-- 1 子菜单 -->
									<el-menu-item v-if="!menu.childList && menu.is_show" :index="menu.id + '' ">
										<i :class="menu.icon" :title="menu.name"></i>
										<span class="menu-name">{{menu.name}}</span>
									</el-menu-item>
									<!-- 1 父菜单 -->
									<el-submenu v-if="menu.childList && menu.is_show" :index="menu.id + '' ">
										<template slot="title">
											<i :class="menu.icon" :title="menu.name"></i>
											<span class="menu-name">{{menu.name}}</span>
										</template>
										<!-- 遍历其子项 -->
										<div v-for="(menu2, index) in menu.childList" :key="index">
											<!-- 2 如果是子菜单 -->
											<el-menu-item v-if="!menu2.childList && menu2.is_show" :index="menu2.id + '' ">
												{{menu2.name}}
											</el-menu-item>
											<!-- 2 如果是父菜单 -->
											<el-submenu v-if="menu2.childList && menu2.is_show" :index="menu2.id + '' ">
												<template slot="title">
													{{menu2.name}}
												</template>
												<div v-for="(menu3, index3) in menu2.childList" :key="index3">
													<el-menu-item v-if="menu3.is_show" :index="menu3.id + '' ">
														{{menu3.name}}
													</el-menu-item>
												</div>
											</el-submenu>
										</div>
									</el-submenu>
								</div>
							</el-menu>
						</div>
					</div>
				</div>
				<!-- tab左拖拽：关闭 -->
				<div class="shade-fox" v-if="is_drag" @dragover="$event.preventDefault();" @drop="rightTab = dragTab; right_close();">
					<span style="font-size: 16px;">关闭</span>
				</div>
			</div>
			<!-- 添加新tab弹出的dom -->
			<div class="at-form-fox" style="width: 0px; height: 0px; overflow: hidden; ">
				<div class="at-form-dom" style="width: 300px; padding: 20px 0 10px 0; background-color: #FFF;">
					<el-form label-width="80px" size="mini">
						<!-- <h5 style="padding: 0 0 10px 26px;">创建新页面</h5> -->
						<el-form-item label="标题：">
							<el-input style="width: 200px;" v-model="atTitle" placeholder="页面标题"></el-input>
						</el-form-item>
						<el-form-item label="地址：" style="margin-top: -10px;">
							<el-input style="width: 200px;" v-model="atUrl" placeholder="https://www.baidu.com/" @keyup.native.enter="atOk()"></el-input>
						</el-form-item>
						<el-form-item label="操作：" style="margin-top: -10px;">
							<el-button type="primary" icon="el-icon-plus" size="mini" @click="atOk()">确定</el-button>
						</el-form-item>
					</el-form>
				</div>
			</div>
		
			<!-- 右边 -->
			<div class="nav-right" :class="(is_fold_right ? 'is_fold_right' : '')">
				<!-- 右边，第一行 -->
				<div class="nav-right-1">
					<div class="tools-left">
						<span title="折叠菜单" class="tool-fox" v-if="is_fold == false" @click="fold_start()">
							<i class="el-icon-s-fold"></i>
						</span>
						<span title="展开菜单" class="tool-fox" v-if="is_fold == true" @click="fold_end()">
							<i class="el-icon-s-unfold"></i>
						</span>
						<span title="刷新" class="tool-fox" @click="rightTab = nativeTab; right_f5();">
							<i class="el-icon-refresh-right" style="font-weight: bold;"></i>
						</span>
						<span title="当前时间" class="tool-fox">
							<span style="font-size: 0.90em;">{{now_time}}</span>
						</span>
					</div>
					<div class="tools-right">
						<span title="点击登录" class="tool-fox" @click="openLogin()" v-if="user == null">
							<span style="font-size: 0.8em; font-weight: bold; position: relative; top: -2px;">未登录</span>
						</span>
						<span title="我的信息" class="tool-fox user-info" style="padding: 0;" v-if="user != null">
							<el-dropdown @command="handleCommand" trigger="click" size="medium">
								<span class="el-dropdown-link user-name" style="height: 100%; padding: 0 1em; display: inline-block;">
									<img :src="user.avatar" class="user-avatar">
									{{user.username}}
									<i class="el-icon-arrow-down el-icon--right"></i>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item v-for="drop in dropList" :command="drop.name" :key="drop.name">{{drop.name}}</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</span>
						<span title="切换效果" class="tool-fox" style="padding: 0;">
							<el-dropdown @command="toggleSwitch" trigger="click" size="medium">
								<span class="el-dropdown-link" style="height: 100%; padding: 0 1em; display: inline-block;">
									<i class="el-icon-sort" style="font-weight: bold; transform: rotate(90deg)"></i>
									<span style="font-size: 0.9em;">切换</span>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item :command="s.value" v-for="s in switchList" :key="s.name">
										<span :style=" switchV == s.value ? 'color: #44f' : '' ">{{s.name}} </span>
									</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</span>
						<span title="主题" class="tool-fox" style="padding: 0;">
							<el-dropdown @command="toggleTheme" trigger="click" size="medium">
								<span class="el-dropdown-link" style="height: 100%; padding: 0 1em; display: inline-block;">
									<i class="el-icon-price-tag" style="font-weight: bold;"></i>
									<span style="font-size: 0.9em;">主题</span>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item :command="t.value" v-for="t in themeList" :key="t.name">
										<span :style=" themeV == t.value ? 'color: #44f' : '' ">{{t.name}} </span>
									</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</span>
						<span title="便签" class="tool-fox" @click="openNote()">
							<i class="el-icon-edit" style="font-weight: bold; font-size: 0.9em;"></i>
							<span style="font-size: 0.9em;">便签</span>
						</span>
						<span title="全屏" class="tool-fox" style="margin-right: 0px;" v-if="is_full_screen == false" @click="is_full_screen = true">
							<i class="el-icon-rank" style="font-weight: bold; position: relative; top: 1px; transform: rotate(45deg)"></i>
						</span>
						<span title="退出全屏" class="tool-fox" style="margin-right: 0px;" v-if="is_full_screen == true" @click="is_full_screen = false">
							<i class="el-icon-bottom-left" style="font-weight: bold; position: relative; top: 1px; "></i>
						</span>
					</div>
					<!--  tab上拖拽：全屏打开 -->
					<div class="shade-fox" v-if="is_drag && !dragTab.url" @dragover="$event.preventDefault();" @drop="rightTab = dragTab; right_full();">
						<span style="font-size: 16px;">全屏打开</span>
					</div>
					<div class="shade-fox" v-if="is_drag && dragTab.url" @dragover="$event.preventDefault();" @drop="rightTab = dragTab; right_window_open();">
						<span style="font-size: 16px;">新窗口打开</span>
					</div>
				</div>
		
				<!-- 右边，第二行 -->
				<div class="nav-right-2">
					<div class="towards-left" @click="scrollToLeft" title="向左滑">
						<i class="el-icon-arrow-left"></i>
					</div>
					<div class="towards-middle" @dblclick="atOpen($event)">
		
						<div class="tab-title-box" :style="{left: scrollX + 'px'}" @dblclick.stop="">
							<div v-for="tab in tabList" :key="tab.id" :id=" 'tab-' + tab.id " class="tab-title" :class=" (tab == nativeTab ? 'tab-native' : '') "
								@click="showTab(tab)" @contextmenu.prevent="right_showMenu(tab, $event)" 
								draggable="true" @dragstart="is_drag = true; dragTab = tab"
								@dragend="is_drag = false;">
								<div class="tab-title-2">
									<!-- <i class="el-icon-caret-right"></i> -->
									<span :style=" tab.is_have_en ? 'font-weight: 400;' : '' ">{{tab.name}}</span>
									<i class="el-icon-close" v-if="!tab.hide_close" @click.stop="closeTab(tab)"></i>
								</div>
							</div>
						</div>
					</div>
					<div class="towards-right" @click="scrollToRight" title="向右滑">
						<i class="el-icon-arrow-right"></i>
					</div>
				</div>
				<!-- 面包屑导航栏 -->
				<div class="nav-right-bre" @dblclick="atOpen($event)" @contextmenu.prevent="right_showMenu(nativeTab, $event);"
					draggable="true" @dragstart="is_drag = true; dragTab = nativeTab" @dragend="is_drag = false;">
					<el-breadcrumb separator-class="el-icon-arrow-right" @dblclick.stop="">
						<el-breadcrumb-item>
							<i class="el-icon-menu" @click="showHome()"></i>
						</el-breadcrumb-item>
						<!-- <el-breadcrumb-item v-if="bre_is_show_title">{{title}}</el-breadcrumb-item> -->
						<el-breadcrumb-item v-for="(menu) in breMenuList" :key="menu.id">{{menu.name}}</el-breadcrumb-item>
					</el-breadcrumb>
				</div>
				<!-- 右边，第三行 -->
				<!-- swiper滑动 -->
				<div class="nav-right-3">
					<!-- swiper -->
					<swiper class="sa-swiper" :options="swiperOption" ref="mySwiper" >
						<swiper-slide class="swiper-no-swiping" v-for="tab in tabList" :key="tab.id">
							<div class="view-fox">
								<component v-bind:is="tab.view" :params="tab.params" :ref="'view-'+tab.id" v-if="tab.is_rend" :key="tab.id"></component>
							</div>
						</swiper-slide>
					</swiper>
					
					<!-- 下托拽：悬浮打开 -->
					<div class="shade-fox" v-if="is_drag" @dragover="$event.preventDefault();" @drop="rightTab = dragTab; right_xf();">
						<span style="font-size: 24px;">拖拽至此：悬浮打开</span>
					</div>
				</div>
			</div>
			<!-- 鼠标右键弹出的盒子 -->
			<!-- 【向下展开动画，坐标平移动画】二者只可得其一 -->
			<div class="right-box" :style="rightStyle" v-show="rightShow" tabindex="-1" @blur="right_closeMenu2()">
				<div class="right-box-2">
					<div @click="right_closeMenu(); right_f5()"><i class="el-icon-caret-right"></i>刷新</div>
					<div @click="right_closeMenu(); right_copy()"><i class="el-icon-caret-right"></i>复制</div>
					<div @click="right_closeMenu(); right_close()"><i class="el-icon-caret-right"></i>关闭</div>
					<div @click="right_closeMenu(); right_close_other()"><i class="el-icon-caret-right"></i>关闭其它</div>
					<div @click="right_closeMenu(); right_close_all()"><i class="el-icon-caret-right"></i>关闭所有</div>
					<div @click="right_closeMenu(); right_xf()"><i class="el-icon-caret-right"></i>悬浮打开</div>
					<div @click="right_closeMenu(); right_full()" v-if="rightTab && !rightTab.url"><i class="el-icon-caret-right"></i>全屏打开</div>
					<div @click="right_closeMenu(); right_window_open()" v-if="rightTab && rightTab.url"><i class="el-icon-caret-right"></i>新窗口打开</div>
					<div @click="right_closeMenu2();"><i class="el-icon-caret-right"></i>取消</div>
				</div>
			</div>
		</div>
		
		<!-- dialog -->
		<el-dialog
			v-if="dialogTab"
			:title="dialogTab.title" 
			:visible.sync="dialogTab.isShow" 
			:width="dialogTab.way == 1 ? '100%' : '80%' " 
			:before-close="dialogTab.beforeClose"
			:close-on-click-modal="false"
			:top="dialogTab.way == 1 ? '0px' : '10vh' "
			:fullscreen="dialogTab.way == 1" 
			custom-class="full-dialog"
			>
			<div :style="dialogTab.way == 1 ? '' : 'height: 70vh' ">
				<component v-bind:is="dialogTab.view" :params="dialogTab.params" v-if="dialogTab.isShow2"></component>
			</div>
		</el-dialog>
		
		<!-- 登录视图 -->
		<sa-login ref="sa-login"></sa-login>
		
		<!-- 403视图 -->
		<sa403 ref="sa403"></sa403>
		
		<!-- 404视图 -->
		<sa404 ref="sa404"></sa404>
		
		<!-- 500视图 -->
		<sa500 ref="sa500"></sa500>
		
		
	</div>
</template>
<script src="./sa-index.js"></script>
<style src="./sa-index.css"></style>
<style src="./sa-theme.css"></style>
