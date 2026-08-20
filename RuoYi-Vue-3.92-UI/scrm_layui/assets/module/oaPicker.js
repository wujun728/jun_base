layui.define(['tool'], function (exports) {
	const layer = layui.layer, tool = layui.tool,form=layui.form, table=layui.table,tree = layui.tree;
	
	const dataPicker ={
		'department':{
			title:'选择部门',
			url:'/api/index/get_department',
			area: ['400px', '524px'],
			searchbar:'',
			page:false,
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'部门名称'}]
		},
		'position':{
			title:'选择岗位',
			url:'/api/index/get_position.json',
			area: ['400px', '524px'],
			searchbar:'',
			page:false,
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'岗位名称'}]
		},
		'services':{
			title:'选择服务类型',
			url:'/api/index/get_services',
			area: ['400px', '524px'],
			searchbar:'',
			page:false,
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'服务名称'},{field:'price',title:'服务单价'}]
		},
		'template':{
			title:'选择消息模板',
			url:'/api/index/get_template',
			area: ['600px', '568px'],
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'消息模板名称'}]
		},
		'workcate':{
			title:'选择工作类型',
			url:'/api/index/get_work_cate',
			area: ['400px', '524px'],
			searchbar:'',
			page:false,
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'工作类型名称'}]
		},
		'property':{
			title:'选择固定资产',
			url:'/adm/api/get_property',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'资产名称'}]
		},
		'car':{
			title:'选择车辆信息',
			url:'/adm/api/get_car',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'车辆名称'},{field:'name',width:100,title:'车牌号码',align:'center'}]
		},
		'room':{
			title:'选择会议室',
			url:'/adm/api/get_meeting_room',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'会议室名称'},{field:'num',width:100,title:'可容纳人数',align:'center'}]
		},
		'customer':{
			title:'选择客户',
			url:'/customer/api/get_customer',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'name',title:'客户名称'}]
		},
		'supplier':{
			title:'选择供应商',
			url:'/contract/api/get_supplier',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'供应商名称'}]
		},
		'contract':{
			title:'选择销售合同',
			url:'/contract/api/get_contract.json',
			area: ['800px', '568px'],
			cols:[{ field: 'code',width:160,title:'合同编号',align:'center'},{field:'name',title:'合同名称'},{ field:'customer',title:'关联客户',width: 240}]
		},
		'product':{
			title:'选择产品',
			url:'/contract/api/get_product.json',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'产品名称'},{field:'sale_price',title:'销售单价',width: 120,align:'center'}]
		},
		'purchase':{
			title:'选择采购合同',
			url:'/contract/api/get_purchase',
			area: ['800px', '568px'],
			cols:[{ field: 'code',width:160,title:'合同编号',align:'center'},{field:'name',title:'合同名称'},{ field:'supplier',title:'关联供应商',width: 240}]
		},
		'purchased':{
			title:'选择采购物品',
			url:'/contract/api/get_purchased',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'采购物品名称'},{field:'purchase_price',title:'采购单价',width: 120,align:'center'}]
		},
		'project':{
			title:'选择项目',
			url:'/project/api/get_project.json',
			cols:[{field:'id',width:90,title:'序号',align:'center'},{field:'title',title:'项目名称'}]
		},
		'task':{
			title:'选择任务',
			url:'/project/api/get_task',
			area: ['800px', '568px'],
			cols:[{field:'id',width:90,title:'序号',align:'center'},{ field:'title',title:'任务主题'},{field:'project',width:240,title:'关联项目'}]
		}
	}

	let select_ids=[];select_names=[];select_array=[];
	const obj = {
		employeeRender:function(){
			var me=this,letterTem='';
			for(var i=0;i<26;i++){
				letterTem+='<span class="layui-letter-span" data-code="'+String.fromCharCode(97+i)+'">'+String.fromCharCode(65+i)+'</span>';
			}
			var tpl='<div style="width:210px; height:388px; border-right:1px solid #eee; overflow-x: hidden; overflow-y: auto; float:left;">\
							<div id="employeeDepament" style="padding:6px 0;"></div>\
						</div>\
						<div style="width:588px; height:388px; user-select:none; overflow-x: hidden; overflow-y: auto; float:left;">\
							<div style="padding:12px 10px 0;"><div style="color:#999; text-align:center;">⇐ 点击左边部门筛选员工，或者点击下面字母筛选</div><div id="letterBar" style="color:#999; text-align:center;">'+letterTem+'</div></div>\
							<div id="employee" style="padding:6px 12px"></div>\
							<div style="padding:10px 15px; border-top:1px solid #eee;;"><strong>已选择</strong><span class="layui-tags-all">全选</span></div>\
							<div id="selectTags" style="padding:10px 15px;">'+me.employeeSelect(0)+'</div>\
						</div>';
			return tpl;
		},
		employeeSelect:function(t){
			var me=this,select_tags='';
			if(me.settings.type == 0){
				select_tags+='<span style="color:#1E9FFF">'+me.settings.names+'</span>';
			}
			else{
				select_ids=[];
				select_names=[];
				for(var a=0;a<select_array.length;a++){
					if(me.settings.fixedid==select_array[a].id && me.settings.fixedid!=0){
						select_tags+='<span class="layui-tags-span">'+select_array[a].name+'</span>';
					}
					else{
						select_tags+='<span class="layui-tags-span">'+select_array[a].name+'<i data-id="'+select_array[a].id+'" class="layui-icon layui-tags-close">ဆ</i></span>';
					}
					if(t==1){
						$('#employee').find('[data-id="'+select_array[a].id+'"]').addClass('on');
					}
					select_ids.push(parseInt(select_array[a].id));
					select_names.push(select_array[a].name);
				}
			}
			//console.log(select_array);
			return select_tags;
		},
		employeeInit: function (options) {
			const opts={
				"title":"选择员工",
				"department_url": "/api/index/get_department_tree.json",
				"employee_url": "/api/index/get_employee.json",
				"type":1,//1单人,2多人
				"fixedid":0,
				"ids":"",
				"names":"",
				"ajax_data":[],
				"callback": function(){}
			};
			this.settings = $.extend({}, opts, options);
			var me=this;
			select_ids=[];
			select_names=[];
			select_array=[];
			if(me.settings.ids!='' && me.settings.names!=''){
				select_ids=me.settings.ids.split(',');
				select_names=me.settings.names.split(',');
				select_ids.sort((a, b) => a - b);
				for(var m=0;m<select_ids.length;m++){
					select_array.push({id:select_ids[m],name:select_names[m]});
				}
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type:1,
				title:me.settings.title,
				area:['800px','500px'],
				resize:false,
				content:me.employeeRender(),
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				success:function(obj,idx){
						var dataList=[],letterBar=$('#letterBar'),employee = $('#employee'),selectTags = $('#selectTags');
						$.ajax({
							url:me.settings.department_url,
							type:'get',
							success:function(res){			
								//仅节点左侧图标控制收缩
								tree.render({
									elem: '#employeeDepament',
									data: res.trees,
									onlyIconControl: true,  //是否仅允许节点左侧图标控制展开收缩
									click: function(obj){
										var tagsItem='<div style="color:#999; text-align:center;">暂无员工</div>';
										$("#employeeDepament").find('.layui-tree-main').removeClass('on');
										$(obj.elem).find('.layui-tree-main').eq(0).addClass('on');
										letterBar.find('span').removeClass('on');
										$.ajax({
											url:me.settings.employee_url,
											type:'get',
											data:{did:obj.data.id},
											success:function(res){
												me.ajax_data = res.data;
												dataList=me.ajax_data;
												if(dataList.length>1 && me.settings.type == 2){
													$('.layui-tags-all').show();
												}
												else{
													$('.layui-tags-all').hide();
												}
												if(dataList.length>0){
													tagsItem='';
													for(var i=0; i<dataList.length; i++){
														if(select_ids.indexOf(dataList[i].id) == -1){
															tagsItem+='<span class="layui-tags-span" data-idx="'+i+'" data-id="'+dataList[i].id+'">'+dataList[i].name+'</span>';
														}
														else{
															tagsItem+='<span class="layui-tags-span on" data-idx="'+i+'" data-id="'+dataList[i].id+'">'+dataList[i].name+'</span>';
														}
													}
												}
												employee.html(tagsItem);
											}
										})
									}
								});	
								
								letterBar.on("click" ,'span',function(){
									var code=$(this).data('code');
									$(this).addClass('on').siblings().removeClass('on');
									$.ajax({
										url:me.settings.employee_url,
										type:'get',
										data:{id:1},
										success:function(res){	
											me.ajax_data = res.data;
											var letterData=[],tagsItem='<div style="color:#999; text-align:center;">暂无员工</div>';;
											if(me.ajax_data.length>0){
												var tagsItemCode='';
												for(var i=0; i<me.ajax_data.length; i++){
													if(me.ajax_data[i].username.slice(0,1)==code){
														if(select_ids.indexOf(me.ajax_data[i].id) == -1){
															tagsItemCode+='<span class="layui-tags-span" data-idx="'+i+'" data-id="'+me.ajax_data[i].id+'">'+me.ajax_data[i].name+'</span>';
														}
														else{
															tagsItemCode+='<span class="layui-tags-span on" data-idx="'+i+'" data-id="'+me.ajax_data[i].id+'">'+me.ajax_data[i].name+'</span>';
														}
														letterData.push(me.ajax_data[i]);
													}
												}
												dataList=letterData;
												if(dataList.length>2 && me.settings.type == 2){
													$('.layui-tags-all').show();
												}
												else{
													$('.layui-tags-all').hide();
												}
												if(tagsItemCode!=''){
													tagsItem = tagsItemCode;
												}
											}
											employee.html(tagsItem);
										}
									})
								});								
							}
						})
						
						if(me.settings.type == 2){
							$('.layui-tags-all').on('click',function(){
								for(var a=0; a<dataList.length;a++){
									if(select_ids.indexOf(dataList[a]['id']) == -1){
										select_array.push(dataList[a]);;
									}									
								}
								selectTags.html(me.employeeSelect(1));	
							});
						}					
						
						employee.on('click','.layui-tags-span',function(){
							let item_idx=$(this).data('idx');
							let select_item = me.ajax_data[item_idx];
							if(me.settings.type == 1){
								me.settings.callback([select_item]);
								layer.close(idx);
							}
							else{
								if(select_ids.indexOf(select_item['id']) == -1){
									select_array.push(select_item);
									selectTags.html(me.employeeSelect(1));	
								}
							}					
						});
						
						selectTags.on('click','.layui-tags-close',function(){
							let id=$(this).data('id');
							let new_slected=[];
							$('#employee').find('[data-id="'+id+'"]').removeClass('on');
							for(var i=0;i<select_array.length;i++){
								if(select_array[i].id!=id){
									new_slected.push(select_array[i]);
								}
							}
							select_array=new_slected;
							selectTags.html(me.employeeSelect(1));
						});
						if(me.settings.type == 1){
							$('#layui-layer'+idx).find('.layui-layer-btn0').hide();
						}						
					},
					btn: ['确定添加', '清空已选'],
					btnAlign:'c',
					btn1: function(idx){
						me.settings.callback(select_array);
						layer.close(idx);
					},
					btn2: function(idx){
						let canceldata= {department:"",did:0,id:0,mobile:0,name:"",nickname:"",position_id:0,sex:0,status:0,thumb:"",username:""};
						me.settings.callback([canceldata]);
						layer.close(idx);
					}
			})	
		},
		picker:function(types,type,callback,map){
			let pickerIndex = new Date().getTime();
			let pickerTable,options;
			const opts={
				"title":"选择",
				"url": "",
				"ids":"",
				"titles":"",
				"where":map,
				"area": ['600px', '568px'],
				"cols":[{field: 'id',width: 80,title:'序号',align:'center'},{field:'title',title:'名称'}],
				"searchbar":'<form class="layui-form pb-2"><div class="layui-input-inline" style="width:420px; margin-right:5px;"><input type="text" name="keywords" placeholder="请输入关键字" class="layui-input" autocomplete="off" /></div><button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="picker">提交搜索</button><button type="reset" class="layui-btn layui-btn-reset" lay-filter="picker-reset">清空</button></form>',
				"page":true,
				"type":type,//1单选择,2多选
				"btnno":true,
				"add": "",//新增url
				"callback": callback
			};
			if(Object.prototype.toString.call(types) === '[object Object]'){
				options = types;
			}
			else{
				options = dataPicker[types];
			}
			let settings = $.extend({},opts,options);
			//console.log(settings);
			let btn = ['确定选择'];
			if(settings.btnno==true){
				btn = ['确定选择','清空已选'];
			}
			if(settings.add!=''){
				btn = ['确定选择','清空已选','新增'];
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				title: settings.title,
				area: settings.area,
				type: 1,
				skin: 'gougu-picker',
				content: '<div class="picker-table" id="pickerBox'+pickerIndex+'">'+settings.searchbar+'<div id="pickerTable'+pickerIndex+'"></div></div>',
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				success: function () {
					let cols=JSON.parse(JSON.stringify(settings.cols));
					if(settings.type==1){
						cols.splice(0, 0, {type: 'radio', title: '选择'});
					}
					if(settings.type==2){
						cols.splice(0, 0, {type: 'checkbox', title: '选择'});
					}
					pickerTable = table.render({
						elem: '#pickerTable'+pickerIndex,
						url: settings.url,
						where:settings.where,
						page: settings.page, //开启分页
						limit: 10,
						height: '407',
						cols: [cols]
					});
					//搜索提交
					form.on('submit(picker)', function (data) {
						let maps = $.extend({}, settings.where, data.field);
						pickerTable.reload({where:maps,page:{curr: 1}});
						return false;
					});
					//重置搜索提交
					$('#pickerBox'+pickerIndex).on('click', '[lay-filter="picker-reset"]', function () {
						let prev = $(this).prev();
						if (typeof(prev) != "undefined" ) {
							setTimeout(function () {
								prev.click();
							}, 10)
						}
					});	
				},
				btn: btn,
				btnAlign: 'c',
				btn1: function (idx) {
					var checkStatus = table.checkStatus(pickerTable.config.id);
					var data = checkStatus.data;
					if (data.length > 0) {
						callback(data);
						layer.close(idx);
					}
					else {
						layer.msg('请先选择内容');
						return false;
					}
				},
				btn2: function (idx) {
					callback([{'id':0,'title':'','name':''}]);
					layer.close(idx);
				},
				btn3: function (idx) {
					tool.side(settings.add);
					layer.close(idx);
				}
			})
		}
	}
	
	//选择员工弹窗		
	$('body').on('click','.picker-admin',function () {
		let that = $(this);
		let type = that.data('type');
		if (typeof(type) == "undefined" || type == '') {
			type = 1;
		}
		let ids=that.next().val()+'',names = that.val()+'';
		obj.employeeInit({
			ids:ids,
			names:names,
			type:type,
			callback:function(data){
				let select_id=[],select_name=[],select_did=[];
				for(var a=0; a<data.length;a++){
					select_id.push(data[a].id);
					select_name.push(data[a].name);
					select_did.push(data[a].did);
				}
				console.log(select_name);
				that.val(select_name.join(','));
				that.next().val(select_id.join(','));
				that.next().next().val(select_did.join(','));
			}
		});
	});
	
	//选择下属员工弹窗		
	$('body').on('click','.picker-sub',function () {
		let that = $(this);
		let type = that.data('type');
		if (typeof(type) == "undefined" || type == '') {
			type = 1;
		}
		let ids=that.next().val()+'',names = that.val()+'';
		obj.employeeInit({
			title:"选择下属",
			department_url: "/api/index/get_department_tree_sub",
			employee_url: "/api/index/get_employee_sub",
			ids:ids,
			names:names,
			type:type,
			callback:function(data){
				let select_id=[],select_name=[],select_did=[];
				for(var a=0; a<data.length;a++){
					select_id.push(data[a].id);
					select_name.push(data[a].name);
					select_did.push(data[a].did);
				}
				console.log(select_name);
				that.val(select_name.join(','));
				that.next().val(select_id.join(','));
				that.next().next().val(select_did.join(','));
			}
		});
	});
	
	//选择OA数据弹层	
	$('body').on('click','.picker-oa',function () {
		let that = $(this),ids = [],titles=[],map = {};
		let types = that.data('types');
		let type = that.data('type');
		let where = that.data('where');
		if (typeof(types) == "undefined" || types == '') {
			layer.msg('请设置【picker】的类型');
			return false;
		}
		if (typeof(type) == "undefined" || type == '') {
			type = 1;
		}
		if (typeof(where) == "undefined" || where == '') {
			map = {};
		}
		else{
			map = JSON.parse(where);
		}
		let callback = function(data){
			for ( var i = 0; i <data.length; i++){
				ids.push(data[i].id);
				if(!data[i].title){
					titles.push(data[i].name);
				}else{
					titles.push(data[i].title);
				}				
			}
			that.val(titles.join(','));
			that.next().val(ids.join(','));
		}
		obj.picker(types,type,callback,map);
	});
	
	//输出接口
	exports('oaPicker', obj);
});   