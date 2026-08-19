layui.define(['tool'], function (exports) {
	const layer = layui.layer;
	const form = layui.form;
	const tool = layui.tool;
	const dropdown = layui.dropdown;
	const opts={
		"flowBox":'flowBox',
		"flowBar":'flowBar',
		"callback": null
	};
	const obj = {
		loading:false,
		size:1.0,
		//节点模版
		nodeApend: function (types,nodeId) {
			let nodeHtml = '';
			if(types == 1){
				//审核人
				nodeHtml = `<div><div class="node-wrap">
								<div class="node-wrap-box">
									<div class="node-title" style="background:#ffb800;">
										<span class="user-edit">审核人</span>
										<i class="remove-node" data-id="${nodeId}">✖</i>
									</div>
									<div>
										<span>请设置审核人</span>
										<i class="select-icon"><?xml version="1.0" encoding="UTF-8"?><svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M19 12L31 24L19 36" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></i>
									</div>
								</div>
								<div class="node-add-btn-box">
									<div class="add-node-btn">
										<button class="add-node">+</button>
									</div>
								</div>
							</div><div>`;
			}
			else if(types == 2){
				//抄送人
				nodeHtml = `<div><div class="node-wrap">
								<div class="node-wrap-box">
									<div class="node-title" style="background:#1e9fff;">
										<span class="user-edit">抄送人</span>
										<i class="remove-node" data-id="${nodeId}">✖</i>
									</div>
									<div>
										<span>请设置抄送人</span>
										<i class="select-icon"><?xml version="1.0" encoding="UTF-8"?><svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M19 12L31 24L19 36" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></i>
									</div>
								</div>
								<div class="node-add-btn-box">
									<div class="add-node-btn">
										<button class="add-node">+</button>
									</div>
								</div>
							</div><div>`;
			}
			else if(types==3){
				//条件分支
				nodeHtml = `<div class="branch-wrap">
								<div class="branch-box-wrap">
									<div class="branch-box">
										<span class="add-branch" title="添加条件">添加条件分支</span>
										<div class="col-box">
											<div class="condition-node">
												<div class="condition-node-box">
													<div class="condition-judge">
														<div class="condition-title">   
															<input type="text" class="condition-input" placeholder="条件1">
															<span class="condition-priority">优先级1</span>
															<i class="remove-node" data-id="${nodeId}">✖</i>
														</div>
														<div>
															请设置条件
														</div>
													</div>
													<div class="node-add-btn-box">
														<div class="add-node-btn">
															<button class="add-node">+</button>
														</div>
													</div>
												</div>
											</div>                                           
											<div class="top-left-cover-line"></div>
											<div class="bottom-left-cover-line"></div>
										</div>
										<div class="col-box">
											<div class="condition-node">
												<div class="condition-node-box">
													<div class="condition-judge">
														<div class="condition-title">   
															<input type="text" class="condition-input" placeholder="其他情况">
															<span class="condition-priority">优先级2</span>
														</div>
														<div>
															如存在未满足其他分支条件的情况，则进入此分支
														</div>
													</div>
													<div class="node-add-btn-box">
														<div class="add-node-btn">
															<button class="add-node">+</button>
														</div>
													</div>                                                   
												</div>
											</div>
											<div class="top-right-cover-line"></div>
											<div class="bottom-right-cover-line"></div>
										</div>
									</div>
									<div class="node-add-btn-box">
										<div class="add-node-btn">
											<button class="add-node">+</button>
										</div>
									</div>    
								</div>
							</div>
						</div>`;
			}
			else{
				//条件内容
				nodeHtml = `<div class="col-box">
                    <div class="condition-node">
                        <div class="condition-node-box">
                            <div class="condition-judge">
                                <div class="condition-title">   
                                    <input type="text" class="condition-input" placeholder="条件1">
                                    <span class="condition-priority">优先级1</span>
                                    <i class="remove-node" data-id="${nodeId}">✖</i>
                                </div>
                                <div>
                                    请设置条件
                                </div>
                            </div>
                            <div class="node-add-btn-box">
                                <div class="add-node-btn">
                                    <button class="add-node">+</button>
                                </div>
                            </div>
                        </div>
                    </div>                                           
                </div>`;
			}
			return nodeHtml;
		},
		init: function (options) {
			this.settings = $.extend({}, opts, options);
			let me = this;
			let flowBox = $('#'+me.settings.flowBox);
			let flowBar = $('#'+me.settings.flowBar);
			//添加节点
			flowBox.on("click",".add-node",function(e){
				let that = $(this);
				dropdown.render({
					elem: that,
					show: true, // 外部事件触发即显示
					data: [{
					  title: '审 核 人',
					  id: '1',
					  templet: function(d){
						  return '<i style="vertical-align: sub; color:red"><?xml version="1.0" encoding="UTF-8"?><svg width="20" height="20" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M8 36L8.00461 28.0426C8.00551 27.4906 8.45313 27.0432 9.00519 27.0426C12.3391 27.0426 15.6731 27.0426 19.0071 27.0426C19.9286 27.0426 19.9237 26.2252 19.9237 24.2792C19.9237 22.3332 15.0221 20.6941 15.0221 13.8528C15.0221 7.01151 20.0999 5 24.32 5C28.5401 5 33.1366 7.01151 33.1366 13.8528C33.1366 20.6941 28.2607 21.7818 28.2607 24.2792C28.2607 26.7765 28.2607 27.0426 29.0413 27.0426C32.3609 27.0426 35.6806 27.0426 39.0003 27.0426C39.5525 27.0426 40.0003 27.4904 40.0003 28.0426V36H8Z" fill="none" stroke="#ffb800" stroke-width="4" stroke-linejoin="round"/><path d="M8 42H40" stroke="#ffb800" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg></i> ' + d.title;
					  }
					}, {
					  title: '抄 送 人',
					  id: '2',
					  templet: function(d){
						  return '<i style="vertical-align: sub; color:red"><?xml version="1.0" encoding="UTF-8"?><svg width="20" height="20" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M43 5L29.7 43L22.1 25.9L5 18.3L43 5Z" stroke="#1e9fff" stroke-width="4" stroke-linejoin="round"/><path d="M43.0001 5L22.1001 25.9" stroke="#1e9fff" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg></i> ' + d.title;
					  }
					}, {
					  title: '条件分支',
					  id: '3',
					  templet: function(d){
						  return '<i style="vertical-align: sub; color:red"><?xml version="1.0" encoding="UTF-8"?><svg width="20" height="20" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M24 33V15" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><rect x="10" y="9" width="28" height="6" fill="none" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><path d="M8 32L14 25H33.9743L40 32" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><rect x="4" y="33" width="8" height="8" fill="none" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><rect x="20" y="33" width="8" height="8" fill="none" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><rect x="36" y="33" width="8" height="8" fill="none" stroke="#16b777" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg></i> ' + d.title;
					  }
					}],
					click: function(data, othis){
						let timestamp = new Date().getTime();
						that.parent().parent().parent().parent().after(me.nodeApend(data.id,timestamp));
					}
				});
			});			
			
			//添加条件
			flowBox.on("click",".add-branch",function(e){
				let timestamp = new Date().getTime();
				$(this).parent().find(".col-box").first().after(me.nodeApend(4,timestamp));
				let branchWrap = $($(this).parents(".branch-wrap")[0]);
				let colBbox = branchWrap.find('.col-box');
				colBbox.each(function(index,item){
					$(item).find('.condition-priority').html('优先级'+(index+1));
					if(index<colBbox.length-1){
						$(item).find('.condition-input').attr('placeholder','条件'+(index+1));
					}
				});				
			})
		
			//删除节点、条件
			flowBox.on("click",".remove-node",function(e){
				if($(this).parents().hasClass("condition-judge")){
					let idx =$(this).closest('.col-box').index();
					let branchWrap = $($(this).parents(".branch-wrap")[0]);
					let colBbox = branchWrap.find('.col-box');
					if(colBbox.length==2 || idx==1){
						layer.confirm('确定需要删除整个条件分支？', {icon: 3}, function(){
							branchWrap.remove();
							layer.closeAll();
						});
					}
					else{
						$(this).closest(".col-box").remove();
						colBbox = branchWrap.find('.col-box');
						colBbox.each(function(index,item){
							$(item).find('.condition-priority').html('优先级'+(index+1));
							if(index<colBbox.length-1){
								$(item).find('.condition-input').attr('placeholder','条件'+(index+1));
							}
						});	
					}
				}else if($(this).parents().hasClass("node-wrap")){
					$(this).closest(".node-wrap").parent().remove();
				}
			})
			
			//放大、缩小
			flowBar.on("click",".zoom-ctrl",function(e){
				let type = $(this).data('type'),size=1;
				if(type==1){
					size = me.size - 0.25;
				}
				else{
					size = me.size + 0.25;
				}
				
				if(size<=0){
					layer.msg('已经是最小了');
					return false;
				}
				else if(size>3){
					layer.msg('已经是最大了');
					return false;
				}
				me.size = size;
				flowBox.css("transform","scale("+size+")");
				flowBar.find('.zoom-num').html((size*100)+'%');
			})
		}
	};
	exports('oaFlow', obj);
});  