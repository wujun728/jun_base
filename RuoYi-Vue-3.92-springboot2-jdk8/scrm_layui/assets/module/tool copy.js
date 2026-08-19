layui.define(function (exports) {
	let MOD_NAME = 'tool',dropdown=layui.dropdown;
	let tool = {
		loading: false,
		expressClose:function(index){return '<div class="express-close" data-index="'+index+'" title="关闭">\
			<div class="express-container">\
				<div class="express-bar">\
					<svg viewBox="0 0 202.9 45.5" >\
					  <clipPath id="menu" clipPathUnits="objectBoundingBox" transform="scale(0.0049285362247413 0.021978021978022)">\
						<path  d="M6.7,45.5c5.7,0.1,14.1-0.4,23.3-4c5.7-2.3,9.9-5,18.1-10.5c10.7-7.1,11.8-9.2,20.6-14.3c5-2.9,9.2-5.2,15.2-7c7.1-2.1,13.3-2.3,17.6-2.1c4.2-0.2,10.5,0.1,17.6,2.1c6.1,1.8,10.2,4.1,15.2,7c8.8,5,9.9,7.1,20.6,14.3c8.3,5.5,12.4,8.2,18.1,10.5c9.2,3.6,17.6,4.2,23.3,4H6.7z"/>\
					  </clipPath>\
					</svg>\
				</div>\
				<div class="express-btn">\
					<svg class="express-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M463.787 512l-253.44-253.44a34.133 34.133 0 0 1 48.213-48.213L512 463.787l253.44-253.44a34.133 34.133 0 1 1 48.213 48.213L560.213 512l253.44 253.44a34.133 34.133 0 1 1-48.213 48.213L512 560.213l-253.44 253.44a34.133 34.133 0 0 1-48.213-48.213z" fill="#ffffff"></path></svg>\
				</div>\
			</div>\
		</div>'
		},
		side: function (url, width) {
			let that = this;
			if (that.loading == true) {
				return false;
			}
			that.loading = true;
			sideWidth = '80%';
			if(window.innerWidth>1366 && window.innerWidth<=1600){
				sideWidth = '86%';
			}
			if(window.innerWidth>1000 && window.innerWidth<=1440){
				sideWidth = '93%';
			}
			if(window.innerWidth<=1000){
				sideWidth = '95%';
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 2,
				title: false,
				offset: 'r',
				anim: 'slideLeft',
				closeBtn: 0,
				content: url,
				area: [sideWidth, '100%'],
				skin:'layui-layer-gougu-admin',
				end: function(){
					$('body').removeClass('right-open');
					$(parent.$('.express-close')).removeClass('parent-colse');
					if (layui.pageTable && layui.pageTable.resize) {
						layui.pageTable.resize();
					}
				},
				success: function (obj, index) {
					$('body').addClass('right-open');
					obj.append(that.expressClose(index));
					that.loading = false;
					obj.on('click','.express-close', function () {					
						layer.close(index);
					})
				}
			})
		},
		box: function (url,title="内容", width=720,height=405) {
			let that = this;
			if (that.loading == true) {
				return false;
			}
			that.loading = true;
			layer.open({
				type: 2,
				title: title,
				content: url,
				area: [width+'px', height+'px'],
				maxmin: true,
				end: function(){
					if (layui.pageTable && layui.pageTable.resize) {
						layui.pageTable.resize();
					}
				},
				success: function (obj, index) {
					let btn = '<div data-index="'+index+'" class="express-close" style="display:none;" title="关闭">关闭</div>';
					obj.append(btn);
					that.loading = false;
					obj.on('click','.express-close', function () {					
						layer.close(index);
					})
				}
			})
		},
		//右侧ajax请求的方式打开页面参考勾股DEV
		open: function (url, width) {
			let that = this;
			if (that.loading == true) {
				return false;
			}
			that.loading = true;
			let countWidth = window.innerWidth-(window.innerWidth*0.5)+456;
			if(window.innerWidth<=1000){
				countWidth = 750;
			}
			if (width && width > 0) {
				sideWidth = width + 'px';
			}
			else{
				sideWidth = countWidth + 'px';
			}
			$.ajax({
				url: url,
				type: "GET",
				timeout: 10000,
				success: function (res) {
					if (res['code'] && res['code'] > 0) {
						layer.msg(res.msg);
						return false;
					}
					let index = timestamp = new Date().getTime();
					let express = '<section id="expressLayer" class="express-box" style="width:' + sideWidth + '"><article id="articleLayer">' + res + '</article>'+that.expressClose(index)+'</section><div id="expressMask" class="express-mask"></div>';

					$('body').append(express).addClass('right-open');
					$('#expressMask').fadeIn(200);
					$('#expressLayer').animate({ 'right': 0 }, 200, 'linear', function () {
						if (typeof (openInit) == "function") {
							openInit();
						}						
					});					
					that.loading = false;					
					//关闭
					$('body').on('click','.express-close', function () {
						$('#expressMask').fadeOut(100);
						$('body').removeClass('right-open');
						let op_width = $('#expressLayer').outerWidth();
						$('#expressLayer').animate({ left: '+=' + op_width + 'px' }, 200, 'linear', function () {
							$('#expressLayer').remove();
							$('#expressMask').remove();
							if (layui.pageTable) {
								layui.pageTable.resize();
							}
						})
					})
					$(window).resize(function () {
						let resizeWidth = window.innerWidth-(window.innerWidth*0.5)+456;
						if(window.innerWidth<=1000){
							resizeWidth = 750;
						}
						$('#expressLayer').width(resizeWidth);
					})
				},
				error: function (xhr, textstatus, thrown) {
					console.log('错误');
				},
				complete: function () {
					that.loading = false;
				}
			});
		},
		load: function (url) {
			let that = this;
			if (that.loading == true) {
				return false;
			}
			that.loading = true;
			$.ajax({
				url: url,
				type: "GET",
				timeout: 10000,
				success: function (res) {
					if (res['code'] && res['code'] > 0) {
						layer.msg(res.msg);
						return false;
					}
					$('#articleLayer').html(res);
					openInit();
				}
				, error: function (xhr, textstatus, thrown) {
					console.log('错误');
				},
				complete: function () {
					that.loading = false;
				}
			});
		},
		page: function (url) {
			let that = this;
			if (that.loading == true) {
				return false;
			}
			that.loading = true;
			$.ajax({
				url: url,
				type: "GET",
				timeout: 10000,
				success: function (res) {
					if (res['code'] && res['code'] > 0) {
						layer.msg(res.msg);
						return false;
					}
					$('#pageBox').html(res);
					pageInit();
				},
				error: function (xhr, textstatus, thrown) {
					console.log('错误');
				},
				complete: function () {
					that.loading = false;
				}
			});
		},
		reload: function (delay) {
			//延迟刷新，一般是在编辑完页面数据后需要自动关闭页面用到
			if(delay && delay>0){
				setTimeout(function () {
					location.reload();
				}, delay);
			}else{
				location.reload();
			}
		},
		ask:function(tips,yes,cancel){
			layer.confirm(tips, {
				icon: 3,
				title: '提示',
				success:function(){
					$(parent.$('.express-close')).addClass('parent-colse');
				},
				end:function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				}
			}, function (index) {
				if (yes && typeof yes === 'function') {
					yes();
				}
				layer.close(index);
			}, function (index) {
				if (cancel && typeof cancel === 'function') {
					cancel();
				}
				layer.close(index);
			});
		},
		close: function (delay,table) {
			//延迟关闭，一般是在编辑完页面数据后需要自动关闭页面用到
			if(delay && delay>0){
				setTimeout(function () {
					$('.express-close').last().click();
				}, delay);
			}else{
				$('.express-close').last().click();
			}
			if (typeof(table) == "undefined" || table == '') {
				table = 'pageTable';
			}
			if (layui[table]) {
				layui[table].reload();
			}
			else{
				tool.reload(delay);
			}
		},
		ajax: function (options, callback, clickbtn) {
			let format = 'json';
			if (options.hasOwnProperty('data')) {
				format = options.data.hasOwnProperty('format') ? options.data.format : 'json';
			}
			callback = callback || options.success;
			callback && delete options.success;
			let optsetting = { timeout: 10000 };
			if (format == 'jsonp') {
				optsetting = { timeout: 10000, dataType: 'jsonp', jsonp: 'callback' }
			}
			let opts = $.extend({}, optsetting, {
				success: function (res) {
					if (callback && typeof callback === 'function') {
						callback(res);
					}
				},
				beforeSend:function(){
					if (clickbtn && typeof clickbtn === 'object') {
						clickbtn.attr('disabled',true).html('提交中 ...');
					}					
				},
				complete: function () {
					if (clickbtn && typeof clickbtn === 'object') {
						setTimeout(function(){
							clickbtn.removeAttr('disabled').html('立即提交');
						},1000);					
					}					
				}
			}, options);
			$.ajax(opts);
		},
		get: function (url, data, callback) {
			this.ajax({url: url,type: "GET",data: data}, callback);
		},
		post: function (url, data, callback, clickbtn) {
			this.ajax({url: url,type: "POST",data: data}, callback, clickbtn);
		},
		put: function (url, data, callback) {
			this.ajax({url: url,type: "PUT",data: data}, callback);
		},
		delete: function (url, data, callback) {
			this.ajax({url: url,type: "DELETE",data: data}, callback);
		},
		parentAdmin:function () {
			if(parent.layui.admin){
				return parent.layui.admin;
			}
			else{
				if(parent.parent.layui.admin){
					return parent.parent.layui.admin;
				}
				else{
					if(parent.parent.parent.layui.admin){
						return parent.parent.parent.layui.admin;
					}
					else{
						return false;
					}
				}
			}
		},
		sideClose(delay,table){
			if(parent.layui.tool){
				parent.layui.tool.close(delay,table);
			}
			else{
				console.log('父页面没引用tool模块');
			}		
		},
		tabAdd:function(url,title,id){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.sonAdd(url,title,id);
			}		
		},
		tabClose:function(){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.sonClose();
			}
		},
		tabDelete:function(id){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.tabDelete(id);
			}
		},
		tabChange:function(id){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.tabChange(id);
			}
		},
		tabRefresh:function(id){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.refresh(id);
			}			
		},
		officeView:function(id,mode){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.officeView(id,mode);
			}			
		},
		pdfView:function(href){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.pdfView(href);
			}			
		},
		photoView:function(href){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.photoView(href);
			}		
		},
		videoView:function(href){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.videoView(href);
			}		
		},
		audioView:function(href){
			let parentAdmin = this.parentAdmin();
			if(parentAdmin){
				parentAdmin.audioView(href);
			}		
		},
		articleView:function(fileid) {
			tool.side('/disk/index/view_article?id='+fileid);
		},
		articleEdit:function(fileid) {
			tool.side('/disk/index/add_article?id='+fileid);
		},
		downloadFile:function(url, fileName) {
			let link = document.createElement("a");
			link.href = url;
			link.download = fileName;
			link.click();
			layer.msg('文件下载中...', {time: 2000});
		},
		copyCtrl:function(content) {
			var save = function(e){
				e.clipboardData.setData('text/plain', content);
				e.preventDefault();
			}
			document.addEventListener('copy', save);
			document.execCommand('copy');
			document.removeEventListener('copy',save);
			if (content != '') {
				layer.msg('复制成功');
			}
		}
	};
	//时间选择快捷操作
	$('body').on('click', '.tool-time', function () {
		let that = $(this);
        let type = that.data('type');
        let range = that.data('range');
        let min = that.data('min');
        let max = that.data('max');
        let format = that.data('format');
		if (typeof(type) == "undefined" || type === '') {
			type = 'date';
		}
		if (typeof(range) == "undefined" || type === '') {
			range = false;
		}
		if (typeof(min) == "undefined" || min === '') {
			min = '1900-1-1';
		}
		if (typeof(max) == "undefined" || max === '') {
			max = '2099-1-1';
		}
		if (typeof(format) == "undefined" || format === '') {
			layui.laydate.render({ 
				elem: that,
				show: true,
				type: type,
				range: range,
				min: min,
				max: max,
				fullPanel: true
			});
		}else{
			layui.laydate.render({ 
				elem: that,
				show: true,
				type: type,
				range: range,
				min: min,
				max: max,
				format:format,
				fullPanel: true
			});
		}

		return false;
	});

	//附件操作	
	const ctrlBtn=function(ctrl){
			//操作按钮'0下载','1查看','2编辑','3重命名','4删除','5移动','6分享','7取消分享','8标星','9取消标星','10还原','11清除'
		let ctrl_types = ['下载','查看','编辑','重命名','删除','移动','分享','取消分享','标星','取消标星','还原','清除'];
		let	ctrls=[];
		for(let i=0;i<ctrl.length;i++){
			ctrls.push({"id":ctrl[i]+'',"title":ctrl_types[ctrl[i]]});
		}
		return ctrls;
	}
	
	$('body').on('click','.file-ctrl',function () {
		let that = $(this);
		let ctrl = $(this).data('ctrl');
		let fileid = $(this).data('fileid');
		let filename = $(this).data('filename');
		let type = $(this).data('type');
		let ext = $(this).data('ext');
		let href = $(this).data('href');
		let actionid=0;
		let file_menu = ctrlBtn([0]);
		//type:0下载+重命名+删除，1下载+查看+重命名+删除，2下载+查看+编辑+重命名+删除
		if(ctrl=='edit'){
			let types=[
				[0,1,3,4],//下载+查看+重命名+删除
				[0,1,3,4],//下载+查看+重命名+删除
				[0,1,2,3,4]//下载+查看+编辑+重命名+删除
			];
			file_menu = ctrlBtn(types[type]);
		}
		if(ctrl=='view'){
			if(type==0){
				file_menu = ctrlBtn([0]);
			}
			else{
				file_menu = ctrlBtn([0,1]);
			}
		}
		if(ctrl=='disk'){
			if(type!=''){
				actionid = $(this).data('actionid');
				file_menu = ctrlBtn((type+'').split(","));
			}
		}
		console.log(actionid);
		dropdown.render({
			elem: that,
			className:'file-menu',
			align: 'right',
			show: true, // 外部事件触发即显示
			data: file_menu,
			click: function(obj){
				let click_t = obj.id;
				switch (click_t) {
				  case '0':
					tool.downloadFile(href,filename);
					break;
				  case '1':
					if(ext=='image'){
						tool.photoView(href);
					}
					if(ext=='pdf'){
						tool.pdfView(href);
					}
					if(ext=='video'){
						tool.videoView(href);
					}
					if(ext=='audio'){
						tool.audioView(href);
					}
					if(ext=='office'){
						if(ctrl=='disk'){
							tool.officeView(actionid,'view');
						}
						else{
							tool.officeView(fileid,'view');
						}
					}
					if(ext=='article'){
						tool.articleView(fileid);
					}
					break;
				  case '2':
					if(ext=='office'){
						if(ctrl=='disk'){
							tool.officeView(actionid,'view');
						}
						else{
							tool.officeView(fileid,'view');
						}
					}
					if(ext=='article'){
						tool.articleEdit(fileid);
					}
					break;
				  case '3'://编辑
					$('#fileEdit'+fileid).click();
					break;
				  case '4'://删除
					$('#fileDel'+fileid).click();
					break;
				  case '5'://移动
					$('#fileMove'+fileid).click();
					break;
				  case '6'://分享
					$('#fileShare'+fileid).click();
					break;
				  case '7'://取消分享
					$('#fileShareno'+fileid).click();
					break;
				 case '8'://标星
					$('#fileStar'+fileid).click();
					break;
				  case '9'://取消标星
					$('#fileStarno'+fileid).click();
					break;
				  case '10'://还原
					$('#fileBack'+fileid).click();
					break;
				  case '11'://清除
					$('#fileClear'+fileid).click();
					break;
				  default:
					return false;
					break;
				}				
			}
		});
	});
	
	//搜索表单重置快捷操作
	$('body').on('click', '[lay-filter="reset"]', function () {
		let that = $(this);
        let prev = that.prev();
		if (typeof(prev) != "undefined") {
			setTimeout(function () {
				prev.click();
			}, 10)
		}
	});
	
	//新建按钮快捷操作
	$('body').on('click', '.tool-add', function () {
		let href = $(this).data('href');
		if (typeof(href) == "undefined" || href == '') {
			return false;
		}
		tool.side(href);	
		return false;
	});
	
	$('body').on('click', '.tab-a', function () {
        let id = $(this).data('id');
		let url = $(this).data('href');
		let title = $(this).data('title');
		if (url && url !== '') {
			if (typeof(id) == "undefined" || id == '') {
				id = Date.now();
			}
			tool.tabAdd(url,title,id);
		}
		return false;
	});
	$('body').on('click', '.side-a', function () {
		let url = $(this).data('href');
		if (url && url !== '') {
			tool.side(url);
		}
		return false;
	});
	$('body').on('click', '.open-a', function () {
		let url = $(this).data('href');
		if (url && url !== '') {
			tool.open(url);
		}
		return false;
	});
	$('body').on('click', '.link-a', function () {
		let url = $(this).data('href');
		if (url && url !== '') {
			window.location.href=url;
		}
		return false;
	});
	exports(MOD_NAME, tool);
});  