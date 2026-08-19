layui.define(['tool','oaPicker','tinymce'],function(exports){
	let layer = layui.layer,tool=layui.tool,laydate = layui.laydate,dropdown = layui.dropdown,oaPicker = layui.oaPicker,tinymce = layui.tinymce;
	const opts={
		"box":'editBox',//编辑容器id
		"id":0,//编辑容器id
		"url": '',
		"dropdown":{},
		"callback":function(e){
			layer.msg(e.msg);
			if(e.code==0){
				setTimeout(function(){
					location.reload();
				},1000)
			}			
		}
	};
	
	const obj = {
		log: function (box,name,action_id) {
			let page=1;
			let callback = function (res) {
				if (res.code == 0 && res.data.length > 0) {
					let itemLog = '',logTime='';
					$.each(res.data, function (index, item) {
						let detail = "";
						if(logTime != item.create_time){
							if(logTime==''){
								itemLog+='<dl><dt><span class="date-second-point"></span>'+item.create_time+'</dt>'
							}
							else{
								itemLog+='</dl><dl><dt><span class="date-second-point"></span>'+item.create_time+'</dt>'
							}
							logTime = 	item.create_time;			
						}
						detail= `
							<span class="log-content gray">将<strong>${item.field_name}</strong>从 <span class="green">${item.old_content}</span> ${item.action}为 <span class="blue">${item.new_content}</span><span class="ml-4 gray" title="${item.create_time}">${item.times}</span></span>
						`;
						
						if(item.field=='new'){
							detail= `
								<span class="log-content gray">创建了该数据记录<span class="ml-4 gray" title="${item.create_time}">${item.times}</span></span>
							`;
						}
						
						itemLog+= `
							<dd><img src="${item.thumb}" class="log-thumb" /><span class="log-name">${item.admin_name}</span>${detail}</dd>
						`;
					});
					itemLog+='</dl>';
					if(res.data.length>19){
						itemLog+='<div class="py-3 log-more"><button class="layui-btn layui-btn-normal layui-btn-sm" type="button">查看更多</button></div>';
					}
					$("#"+box).html(itemLog);
					$('#'+box+'Total').html(res.action.total);
				}
				else{
					if(page ==1){
						$("#"+box).html('<div class="layui-data-none">暂无记录</div>');
					}
				}
			}
			tool.get("/api/index/load_log.json", {name:name,action_id:action_id,page:page}, callback);
			$("#"+box).on('click','.log-more',function(){
				page++;
				tool.get("/api/index/load_log.json", {name:name,action_id:action_id,page:page}, callback);
			});
		},
		//短文本
		text: function (that) {
			let me = this;
			let field = that.data('field');
			let text = that.data('text');
			if (typeof(text) == "undefined") {
				text = that.text();
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 1,
				title: '请输入内容',
				area: ['360px', '158px'],
				content: '<div style="padding:5px;"><input class="layui-input" id="oaEditText" value="' + text + '"/></div>',
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				btnAlign: 'c',
				btn: ['提交保存'],
				yes: function () {
					let val = $("#oaEditText").val();
					if (val != '') {
						let postData = {'id':me.sets.id,'scene':'oaedit'};
						postData[field] = val;
						tool.post(me.sets.url,postData,me.sets.callback);
					} else {
						layer.msg('请输入内容');
					}
				}
			})			
		},
		//长文本
		textarea: function (that) {
			let me = this;
			let field = that.data('field');
			let target = that.data('target');
			let textarea='';
			if (typeof(target) == "undefined") {
				textarea = that.text();
			}
			else{
				textarea = $('#'+target).text();
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 1,
				title: '请输入内容',
				area: ['600px', '320px'],
				content: '<div style="padding:5px;"><textarea class="layui-textarea" id="oaEditTextarea" style="width: 100%; height: 200px;">' + textarea + '</textarea></div>',
				btnAlign: 'c',
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				btn: ['提交保存'],
				yes: function () {
					let val = $("#oaEditTextarea").val();
					if (val != '') {
						let postData = {'id':me.sets.id,'scene':'oaedit'};
						postData[field] = val;
						tool.post(me.sets.url,postData,me.sets.callback);
					} else {
						layer.msg('请输入内容');
					}
				}
			})			
		},
		//数字
		num: function (that) {
			let me = this;
			let field = that.data('field');
			let text = that.data('text');
			let min = that.data('min');
			let max = that.data('max');
			if (typeof(min) == "undefined") {
				min = '';
			}
			if (typeof(max) == "undefined") {
				max = '';
			}
			if (typeof(text) == "undefined") {
				text = that.text();
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 1,
				title: '请输入数字',
				area: ['200px', '158px'],
				content: '<div style="padding:5px;"><input class="layui-input" oninput="this.value = this.value.replace(/[^0-9]/g,\'\')" id="oaEditNum" value="' + text + '"/></div>',
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				btnAlign: 'c',
				btn: ['提交保存'],
				yes: function () {
					let val = $("#oaEditNum").val();
					if (val != '') {
						if(min !='' && val<min){
							layer.msg('输入数字不能小于'+min);
							return false;
						}
						if(max !='' && val>max){
							layer.msg('输入数字不能大于'+max);
							return false;
						}
						let postData = {'id':me.sets.id,'scene':'oaedit'};
						postData[field] = val;
						tool.post(me.sets.url,postData,me.sets.callback);
					} else {
						layer.msg('请输入内容');
					}
				}
			})			
		},
		dropdown: function (that) {
			let me = this;
			let field = that.data('field');
			let text = that.data('text');
			let cancel = that.data('cancel');
			let arrayidx = that.data('array');
			if (typeof(text) == "undefined") {
				text = that.text();
			}
			if (typeof(cancel) == "undefined") {
				cancel = 0;
			}
			let data = me.sets.dropdown[arrayidx];
			let len = data.length;
			while (len--) {
				if (data[len].id == text) {
					data.splice(len, 1);
				}
			}
			if (data.length == 0) {
				layer.msg('无可关联的内容');
				return false;
			}
			if (cancel==1) {
				data.push({ id: 0, title: '<span style="color:#FF5722">取消关联</span>' });
			}
			dropdown.render({
				elem: that,
				show: true,
				data: data,
				click: function (data, othis) {
					let postData = {'id':me.sets.id,'scene':'oaedit'};
					postData[field] = data.id;
					tool.post(me.sets.url,postData,me.sets.callback);
				}
			});
		},
		oadate: function (that) {
			let me = this;
			let type = that.data('type');
			let range = that.data('range');
			let min = that.data('min');
			let max = that.data('max');
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
			let field = that.data('field');
			let text = that.data('text');
			if (typeof(text) == "undefined") {
				text = that.text();
			}
			layui.laydate.render({ 
				elem: that,
				show: true,
				type: type,
				range: range,
				min: min,
				max: max,
				fullPanel: true,
				done: function (val, date) {
					let postData = {'id':me.sets.id,'scene':'oaedit'};
					postData[field] = val;
					tool.post(me.sets.url,postData,me.sets.callback);
				}
			});
		},
		adminpicker: function (that){
			let me = this;
			let field = that.data('field');
			let ids = that.data('ids');
			let names = that.data('names');
			let type = that.data('type');
			if (typeof(type) == "undefined") {
				type = 1;
			}
			oaPicker.employeeInit({
				ids:ids.toString(),
				names:names.toString(),
				type:type,
				callback:function(seleted){
					let select_id=[],select_name=[];
					for(var a=0; a<seleted.length;a++){
						select_id.push(seleted[a].id);
						select_name.push(seleted[a].name);
					}
					let postData = {'id':me.sets.id,'scene':'oaedit'};
					postData[field] = select_id.join(',');
					tool.post(me.sets.url,postData,me.sets.callback);
				}
			});
		},
		picker:function(that){
			let me = this;
			let field = that.data('field');
			let types = that.data('picker');
			let type = that.data('type');
			if (typeof(type) == "undefined") {
				type = 1;
			}
			let map = {};
			let callback = function(data){
				let ids = [],titles=[];
                for ( var i = 0; i <data.length; i++){
                    ids.push(data[i].id);
                    titles.push(data[i].title);
                }
                let postData = {'id':me.sets.id,'scene':'oaedit'};
				postData[field] = ids.join(',');
				tool.post(me.sets.url,postData,me.sets.callback);
            }
            oaPicker.picker(types,type,callback,map);
		},
		editor:function(that){
			let me = this,index = Date.now();
			let field = that.data('field');
			let target = that.data('target');
			let content='';
			if (typeof(target) == "undefined") {
				content = that.html();
			}
			else{
				content = $('#'+target).html();
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 1,
				title: '请输入内容',
				zIndex:20,
				area: ['900px', '600px'],
				content: '<div style="padding:5px;"><textarea class="layui-textarea" id="oaEditEditor'+index+'" style="width: 100%;">' + content + '</textarea></div>',
				btnAlign: 'c',
				btn: ['提交保存'],
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				success:function(){					
					var edit = tinymce.render({
						selector: "#oaEditEditor"+index,
						images_upload_url: '/api/index/upload/sourse/tinymce',//图片上传接口
						height: 480
					});
				},
				yes: function () {
					let val = tinyMCE.editors['oaEditEditor'+index].getContent();
					if (val != '') {
						let postData = {'id':me.sets.id,'scene':'oaedit'};
						postData[field] = val;
						tool.post(me.sets.url,postData,me.sets.callback);
					} else {
						layer.msg('请输入内容');
					}
				}
			})
		},
		init: function (options) {
			this.sets = $.extend({}, opts, options);
			let me = this;
			let editBox = $('#'+me.sets.box);
			editBox.on('click','.click-edit',function(){
				let that = $(this);
				let types = that.data('types');
				switch (types) {
					case "num":
						me.num(that);
						break;
					case "oadate":
						me.oadate(that);
						break;
					case "textarea":
						me.textarea(that);
						break;
					case "dropdown":
						me.dropdown(that);
						break;
					case "adminpicker":
						me.adminpicker(that);
						break;
					case "picker":
						me.picker(that);
						break;
					case "editor":
						me.editor(that);
						break;
					default:
						me.text(that);
				}
			})
		}
	}
	//输出接口
	exports('oaEdit', obj);
});   