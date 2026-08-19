layui.define(['tool','oaPicker'], function (exports) {
	const layer = layui.layer;
	const form = layui.form;
	const tool = layui.tool;
	const opts={
		"checkBox":"checkBox",//审核容器id
		"check_copy": 1,//是否需要操送人
		"check_name": "",//审核类型标识
		"check_btn":1,//是否显示提交审核按钮
		"check_back":0,//是否支持反确认审核操作
		"checking_btn":''//待审核状态下添加的按钮
	};
	const obj = {
		loading:false,
		checkStatus: function (status) {
			statusArray = ['待提交审批','审批中','审批通过','审批不通过','已撤回'];
			return '<span class="check-status-color-'+status+'">'+statusArray[status]+'</span>';
		},
		statusTemplate: function (status) {
			let me = this;
			let tem =`
				<tr>
					<td class="layui-td-gray">审批状态</td>
					<td>${me.checkStatus(status)}</td>
				</tr>
			`;
			return tem;
		},
		uidsTemplate: function () {
			let tem =`
				<td class="layui-td-gray">审批人<font>*</font></td>
				<td>
					<input type="text" name="check_uames" value="" autocomplete="off" placeholder="请选择审批人" lay-verify="required" lay-reqText="请选择审批人" class="layui-input picker-admin" readonly><input type="text" name="check_uids" value="" readonly style="display:none;">
				</td>
			`;
			return tem;
		},
		flowTemplate: function (data) {
			let flowtype='<option value="" title="">--请选择--</option>';
			if(data && data.length>0){
				for(let i=0;i<data.length;i++){
					flowtype+='<option value="'+data[i].id+'" title="'+data[i].check_type+'">'+data[i].title+'</option>';
				}				
			}
			let tem =`
				<tr>
					<td class="layui-td-gray">审批流程<font>*</font></td>
					<td>
						<select name="flow_id" lay-verify="required" lay-filter="flowtype" lay-reqText="请选择审批流程">
						${flowtype}
						</select>
					</td>
				</tr>
			`;
			return tem;
		},
		copyTemplate: function () {
			let me = this;
			let tem =`
			<tr>
				<td class="layui-td-gray">抄送人</td>
				<td>
					<input type="text" name="check_copy_unames" value="" readonly autocomplete="off" placeholder="请选择审批通过后的抄送人" class="layui-input picker-admin" data-type="2"><input type="text" name="check_copy_uids" value="" readonly style="display:none;">
				</td>
			</tr>
			`;
			if(me.sets.check_copy == 1){
				return tem;
			}else{
				return '';
			}			
		},
		btnTemplate: function () {
			let me = this;
			let tem =`
			<div class="pt-3">
				<button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="checkform">提交审批</button>
				<button type="reset" class="layui-btn layui-btn-primary">重置</button>
				${me.sets.checking_btn}
			</div>
			`;			
			if(me.sets.check_btn == 1){
				return tem;
			}else{
				return '';
			}			
		},
		recordTemplate: function (record) {
			let me = this;
			let tem ='';
			if(record.length>0){
				tem+='<tr><td class="layui-td-gray">审批记录</td><td colspan="5"><ul class="layui-timeline flow-record pt-2">';
				for(let l=0;l<record.length;l++){
					tem+='<li class="layui-timeline-item delete-'+record[l].delete_time+'">\
								<i class="layui-icon layui-timeline-axis">&#xe63f;</i>\
								<p style="padding-left:24px">'+record[l].check_time_str+'<span class="black ml-2">'+record[l].name+'</span><span class="check-status-color-'+(record[l].check_status+1)+'">『'+record[l].status_str+'』</span>了此申请。审批意见：<span class="green">'+record[l].content+'。</span></p>\
							</li>';
				}		
				tem+='</ul></td></tr>';	
			}
			return tem;		
		},
		createTemplate: function (flow) {
			let me = this;
			let checkHtml = `
				<form class="layui-form">
					<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						${me.flowTemplate(flow)}
						<tr id="checkTR">${me.uidsTemplate()}</tr>
						${me.copyTemplate()}
					</table>
				</form>
			`;
			return checkHtml;
		},
		//审批待提交模版
		initTemplate: function (detail) {
			let me = this;
			let checkHtml = `
				<form class="layui-form">
					<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						${me.statusTemplate(detail.check_status)}
						${me.recordTemplate(detail.check_record)}
						${me.flowTemplate(detail.flow)}
						<tr id="checkTR">${me.uidsTemplate()}</tr>
						${me.copyTemplate()}
					</table>
					${me.btnTemplate()}
				</form>
			`;
			let viewHtml = `
				<form class="layui-form">
					<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						${me.statusTemplate(detail.check_status)}
						${me.recordTemplate(detail.check_record)}
					</table>
				</form>
			`;
			if(detail.is_creater==1){
				return checkHtml;
			}
			else{
				return viewHtml;
			}
		},
		//审批中模版
		checkTemplate: function (detail) {
			let me = this,flowHtml='',list = detail.nodes;
			for(var f=0;f<list.length;f++){
				//审批流程
				let checkUser = '',
				iconRight ='<span class="layui-icon layui-icon-right"></span>',
				iconStatus ='<i class="layui-icon layui-icon-time"></i>',
				strStatus ='<div class="check-item-status">待审批</div>',
				check_types= '',
				sortClass ='';
				if(f == list.length-1){
					iconRight ='';
				}
				if(detail.check_step_sort == list[f].sort){
					sortClass ='flow-this';
					iconStatus ='<i class="layui-icon layui-icon-time"></i>';
					strStatus ='<div class="check-item-status">当前审批</div>';
					if(list[f].check_uids_info.length>1){
						check_types= ' <span class="layui-badge layui-bg-blue">会签</span>';
						if(list[f].check_types==2){
							check_types= ' <span class="layui-badge layui-bg-orange">或签</span>';
						}
					}
				}
				if(list[f].check_role == 0 || list[f].check_role == 5){
					checkUser=list[f].check_uids_info[0].name;
				}
				if(list[f].check_role == 1 || list[f].check_role == 2 || list[f].check_role == 3  || list[f].check_role == 4){
					checkUser=list[f].flow_name;
				}	
				if(list[f].check_list.length>0){
					let one=0,two=0,three=0;
					for(var m=0;m<list[f].check_uids_info.length;m++){
						if(list[f].check_uids_info[m].check_status == 2){
							three++
						}
						if(list[f].check_uids_info[m].check_status == 1){
							two++
						}
						if(list[f].check_uids_info[m].check_status == 0){
							one++
						}
					}
					//会签
					if(list[f].check_types==1){
						if(three > 0){
							iconStatus ='<i class="layui-icon layui-icon-close" data-no=""></i>';
							strStatus ='<div class="check-item-status">拒绝</div>';
						}
						else{
							if(one > 0){
								iconStatus ='<i class="layui-icon layui-icon-time"></i>';
								strStatus ='<div class="check-item-status">待审批</div>';
							}
							else{
								iconStatus ='<i class="layui-icon layui-icon-ok-circle" data-ok=""></i>';
								strStatus ='<div class="check-item-status">通过</div>';	
							}
						}
					}
					//或签
					if(list[f].check_types==2){
						if(two > 0){
							iconStatus ='<i class="layui-icon layui-icon-ok-circle" data-ok=""></i>';
							strStatus ='<div class="check-item-status">通过</div>';
						}
						else{
							iconStatus ='<i class="layui-icon layui-icon-close" data-no=""></i>';
							strStatus ='<div class="check-item-status">拒绝</div>';
						}
					}
				}
				flowHtml+= '<div class="flow-flexbox check-item flow-flex-row '+sortClass+'" id="flow'+f+'">'+iconStatus+'<div class="check-item-name">'+check_types+checkUser+'</div>'+strStatus+iconRight+'</div>';
			}			
			
			let checkCopy='<tr><td class="layui-td-gray">抄送人</td><td colspan="3">'+detail.copy_unames+'</td></tr>';
			
			let checkNode = `<tr>
					<td class="layui-td-gray">审批节点 <font>*</font></td>
					<td colspan="3">
						<input type="radio" name="check_node" lay-filter="check_node" value="1" title="审批结束">
						<input type="radio" name="check_node" lay-filter="check_node" value="2" title="下一审批人">
						<div class="layui-inline">
							<input type="text" name="check_uname" value="" readonly autocomplete="off" placeholder="请选择下一审批人" lay-verify="required" lay-reqText="请选择下一审批人" class="layui-input picker-admin"><input type="hidden" name="check_uids" value="">
						</div>				
					</td>
				</tr>
				`;
				
			let checkContent = `<tr>
							<td class="layui-td-gray">审批意见 <font>*</font></td>
							<td colspan="3">
								<textarea name="content" placeholder="请输入审批意见" class="layui-textarea"></textarea>
							</td>
					</tr>`;
				
			let btnCheck='<span class="layui-btn layui-btn-normal btn-check" data-status="1"><i class="layui-icon layui-icon-ok"></i> 通过</span><span class="layui-btn layui-btn-danger btn-check" data-status="2"><i class="layui-icon layui-icon-close"></i> 拒绝</span>';
			
			if(detail.step.check_role==5){
				btnCheck='<span class="layui-btn layui-btn-normal btn-check" data-status="1"><i class="layui-icon layui-icon-ok"></i> 通过</span><span class="layui-btn layui-btn-danger btn-check" data-status="2"><i class="layui-icon layui-icon-close"></i> 退回</span>';
			}
			
			let btnBack='<span class="layui-btn layui-btn-primary btn-check" data-status="3"><i class="layui-icon layui-icon-reduce-circle"></i> 撤回</span>';
			let btnCheckBack='<span class="layui-btn layui-btn-danger btn-check" data-event="check" data-status="4">反确认审核</span>';
			
			let checkHtml = `
				<form class="layui-form">
				<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						<tr>
							<td class="layui-td-gray">审批状态</td>
							<td>${me.checkStatus(detail.check_status)}</td>
							<td class="layui-td-gray-2">当前审批人</td>
							<td>${detail.check_unames}</td>
						</tr>
						${me.sets.check_copy == 1 ? checkCopy : ""}
						<tr>
							<td class="layui-td-gray">审批流</td>
							<td colspan="3">
								<div class="flow-flexbox check-items flow-flex-row" id="flowList">
									<div class="flow-flexbox check-item flow-flex-row">
										<i class="layui-icon layui-icon-add-circle" data-ok=""></i>
										<div class="check-item-name">${detail.admin_name}</div>
										<div class="check-item-status">创建</div>
										<span class="layui-icon layui-icon-right"></span>
									</div>
									${flowHtml}
								</div>
							</td>
						</tr>
						${me.recordTemplate(detail.check_record)}
						${detail.is_checker==1 && detail.step.check_role==0?checkNode:''}
						${detail.is_checker==1?checkContent:''}
					</table>
					<div class="pt-3">
						<input type="hidden" name="check_role" value="${detail.step.check_role}">
						${detail.is_checker==1?btnCheck:''}
						${detail.is_creater==1 && (detail.check_status==1 || detail.check_status==3)?btnBack:''}
						${me.sets.check_back == 1 && detail.check_status==2?btnCheckBack:''}						
					</div>
				</form>
			`;
			return checkHtml;
		},
		//审批撤回模版
		backTemplate: function (detail) {
			let me = this;
			let btnHtml ='';
			if(me.sets.check_btn == 1){
				btnHtml ='<div class="pt-3"><button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="checkform">提交审批</button><button type="reset" class="layui-btn layui-btn-primary">重置</button></div>';
			}
			let checkHtml = `
				<form class="layui-form">
					<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						${me.statusTemplate(detail.check_status)}
						${me.recordTemplate(detail.check_record)}
						${me.flowTemplate(detail.flow)}
						<tr id="checkTR">${me.uidsTemplate()}</tr>
						${me.copyTemplate()}
					</table>
					${me.btnTemplate()}
				</form>
			`;
			let viewHtml = `
				<form class="layui-form">
					<h3 class="pb-2">审批操作</h3>
					<table class="layui-table layui-table-form">
						${me.statusTemplate(detail.check_status)}
						${me.recordTemplate(detail.check_record)}
					</table>
				</form>
			`;
			if(detail.is_creater==1){
				return checkHtml;
			}
			else{
				return viewHtml;
			}
		},
		submit:function(data,callback){
			tool.post("/api/check/submit_check", data, callback);
		},
		init: function (options) {
			this.sets = $.extend({}, opts, options);
			let me = this;
			let checkBox = $('#'+me.sets.checkBox);
			let action_id = checkBox.data('id');
			let check_status = checkBox.data('status');
			let check_flow_id = checkBox.data('checkflowid');
			if(action_id === undefined || check_status === undefined || check_flow_id === undefined){
				return false;
			}
			//获取审批信息
			$.ajax({
				url: "/api/check/get_flow_nodes.json",
				type:'get',
				data:{check_name:me.sets.check_name,action_id:action_id,flow_id:check_flow_id},
				success: function (e) {
					if (e.code == 0) {
						if(check_status==0){
							if(action_id==0){
								checkBox.append(me.createTemplate(e.data));
							}
							else{
								checkBox.append(me.initTemplate(e.data));
							}
						}
						else if(check_status==4){
							checkBox.append(me.backTemplate(e.data));
						}
						else{
							checkBox.append(me.checkTemplate(e.data));
						}
						form.render();
					}
				}
			})
			
			//选择审批流
			form.on('select(flowtype)', function(data){
				var check_type = data.elem[data.elem.selectedIndex].title;
				if(data.value==''){
					$('#checkTR').html(me.uidsTemplate());
					checkBox.find('[name="check_copy_unames"]').val('');
					checkBox.find('[name="check_copy_uids"]').val('');
					form.render();
					return false;
				}
				if(check_type == 1){
					$('#checkTR').html(me.uidsTemplate());
					form.render();
				}
				$.ajax({
					url: "/api/check/get_flow_users",
					type:'get',
					data:{id:data.value},
					success: function (e) {
						if (e.code == 0) {
							var flow_li='',flow_idx=0;
							var flow_data = e.data.flow_data;
							if(e.data.copy_uids && e.data.copy_uids !='' && me.sets.check_copy==1){
								checkBox.find('[name="check_copy_unames"]').val(e.data.copy_unames);
								checkBox.find('[name="check_copy_uids"]').val(e.data.copy_uids.split(','));
							}
							if(check_type == 2 || check_type == 3){
								for(var a=0;a<flow_data.length;a++){
									var check_types = '',check_role = '',user_li='';
									if(flow_data[a].check_types==1){
										check_types= ' <span class="layui-badge layui-bg-blue">会签</span>';
									}
									if(flow_data[a].check_types==2){
										check_types= ' <span class="layui-badge layui-bg-orange">或签</span>';
									}
									
									if(flow_data[a].check_role==1){
										check_role = '当前部门负责人';
									}
									if(flow_data[a].check_role==2){
										check_role = '上级部门负责人';
									}
									if(flow_data[a].check_role==3){
										check_role = '岗位职称:'+flow_data[a].check_position;
									}
									if(flow_data[a].check_role==4){
										check_role = '指定人员';
									}
									if(flow_data[a].check_role==5){
										check_role = flow_data[a].flow_name;
										check_types= ' <span class="layui-badge layui-bg-green">可回退</span>';
									}
									let check_uids_info=flow_data[a].check_uids_info;
									if(check_uids_info.length>0){
										flow_idx++;
										for(var b=0;b<check_uids_info.length;b++){
											user_li+= '<li style="padding:3px 0;line-height:22px"><img src="'+check_uids_info[b].thumb+'" style="width:22px; height:22px; border-radius:50%; vertical-align: bottom; margin-right:8px;" />'+check_uids_info[b].name+'</li>';
										}
										flow_li+='<li class="layui-timeline-item" style="padding-bottom:5px;">\
											<i class="layui-icon layui-timeline-axis">&#xe63f;</i>\
											<div class="layui-timeline-content">\
											  <p class="layui-timeline-title" style="margin-bottom:3px">第'+flow_idx+'级审批『'+check_role+'』'+check_types+'</p>\
											  <ul>'+user_li+'</ul>\
											</div>\
										</li>';
									}
								}							
								formHtml = '<td class="layui-td-gray">审批流</td>\
											<td>\
												<ul id="flowList" class="layui-timeline">'+flow_li+'</ul>\
											</td>';
								$('#checkTR').html(formHtml);
							}
						}
					}
				})
			});
			
			//提交审批
			form.on('submit(checkform)', function(data){
				data.field.action_id = action_id;
				data.field.check_name = me.sets.check_name;
				let callback = function (e) {
					layer.msg(e.msg);
					if (e.code == 0) {
						tool.sideClose(1000);				
					}
				}
				tool.post("/api/check/submit_check", data.field, callback);
				return false;
			});
			
			//审批操作按钮
			checkBox.on('click','.btn-check', function(data){
				let content=checkBox.find('[name="content"]').val();
				let check_status=$(this).data('status');
				let check_role = checkBox.find('input[name="check_role"]').val();
				
				let check_node=0,check_uids='';
				if(check_role == 0 && check_status==1){
					check_node = checkBox.find('input[name="check_node"]:checked').val();
					check_uids = checkBox.find('input[name="check_uids"]').val();
					if(!check_node){
						layer.msg('请选择下一审批节点');
						return false;
					}
					if(check_node == 2 && check_uids==''){
						layer.msg('请选择下一审批人');
						return false;
					}
				}
				if(check_status ==1 || check_status==2){
					if(content==''){
						layer.msg('请输入审批意见');
						return false;
					}
					let confirmTips='确定通过该审批？';
					if(check_status==2){
						confirmTips='确定拒绝该审批？';
					}				
					tool.ask(confirmTips, function(index){
						$.ajax({
							url: "/api/check/flow_check",
							type:'post',
							data:{
								action_id:action_id,
								check_name:me.sets.check_name,
								check_flow_id:check_flow_id,
								check_node:check_node,
								check_uids:check_uids,
								check:check_status,
								content:content
							},
							success: function (e) {
								layer.msg(e.msg);
								if (e.code == 0) {
									if(e.check_status==2 && typeof me.sets.check_ok ==='function'){
										me.sets.check_ok();
									}
									tool.sideClose(1000);
								}
							}
						})
						layer.close(index);
					});   
				}
				else if(check_status ==3){			
					layer.prompt({
						formType: 2,
						title: '请输入撤回理由',
						area: ['480px', '120px'] //自定义文本域宽高
					}, function(value, index, elem){
						if(value==''){
							layer.msg('请输入撤回理由');
							return false;
						}
						$.ajax({
							url: "/api/check/flow_check",
							type:'post',
							data:{
								action_id:action_id,
								check_name:me.sets.check_name,
								check_flow_id:check_flow_id,
								check:check_status,
								content:value
							},
							success: function (e) {
								layer.msg(e.msg);
								if (e.code == 0) {	
									tool.sideClose(1000);
								}
							}
						})
						layer.close(index);
					});
				}
				else if(check_status ==4){			
					layer.prompt({
						formType: 2,
						title: '请输入反确认理由',
						area: ['480px', '120px'] //自定义文本域宽高
					}, function(value, index, elem){
						if(value==''){
							layer.msg('请输入反确认理由');
							return false;
						}
						$.ajax({
							url: "/api/check/flow_check",
							type:'post',
							data:{
								action_id:action_id,
								check_name:me.sets.check_name,
								check_flow_id:check_flow_id,
								check:check_status,
								content:value
							},
							success: function (e) {
								layer.msg(e.msg);
								if (e.code == 0) {	
									tool.sideClose(1000);
								}
							}
						})
						layer.close(index);
					});
				}
				return false;
			});
			
		}
	};
	exports('oaCheck', obj);
});