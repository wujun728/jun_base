layui.define(function(exports) {
	//tablePlus在原来的table模块的基础上实现了批量数据导出功能，实现defaultToolbar中筛选列记忆功能,实现了外部搜索功能
	var table = layui.table;	
	var form = layui.form;	
    var MOD_NAME='tablePlus';	
    var tablePlus=$.extend({},table);	
    tablePlus._render = tablePlus.render;
	tablePlus.excel = function(total,options){
		//console.log(options);
		let _page = parseInt(total/options.excel_limit);
		let page = total%options.excel_limit>0?(_page+1):_page;
		let pageHtml='<p style="padding:16px 10px 0; text-align:center; color:red">由于导出数据比较消耗服务器资源，建议使用搜索功能筛选好数据再导出</p><p style="padding:5px 10px 10px; text-align:center; color:red">如果点击导出后，没有立即导出文件，请耐心等待一下，30秒内请勿重复点击。</p><p style="padding:0 10px; text-align:center;">共查询到<strong> '+total+' </strong>条数据，每次最多导出<strong>'+options.excel_limit+'</strong>条，共<strong>'+page+'</strong>页，请点击下面的页码导出</p><div id="exportPage" class="layui-box layui-laypage" style="padding:10px 0; width:100%;text-align:center;">';
		for (i = 1; i <= page; i++) {
			pageHtml += '<a href="javascript:;" data-page="'+i+'">'+i+'</a>';
		}
		pageHtml+='</div>';							
		layer.open({
			type: 1,
			title: '导出数据',
			area:['580px','240px'],
			content: pageHtml,
			success:function(res){
				let tableWhere = JSON.parse(JSON.stringify(options.where));
				tableWhere.limit=options.excel_limit;										
				$('#exportPage').on('click','a',function(){
					tableWhere.page=$(this).data('page');
					let msg = layer.msg('正在导出数据...', {time:3000});
					$.ajax({
						url: options.url,
						data: tableWhere,
						success:function(res){
							table.exportFile(options.id, res.data,'csv');
							setTimeout(function(){
								layer.msg('导出完成');			
							},2000)	
						}
					});
				})
			}
		});
	}
	//重写渲染方法
    tablePlus.render=function(params){
		if(params.excel_limit === undefined){
			params.excel_limit = 10000;
		}
		if(params.cols_save === undefined){
			params.cols_save = false;
		}
		if(params.limit === undefined){
			params.limit = 20;
		}
		if(params.page === undefined){
			params.page = true;
		}
		if(params.cellMinWidth === undefined){
			params.cellMinWidth = 80;
		}
		if(params.help === undefined){
			params.help = '无帮助说明';
		}
		if(params.order_field === undefined){
			params.order_field = 'order_field';
		}		
		if(params.order_type === undefined){
			params.order_type = 'order_type';
		}
		
		let defaultToolbar = [
		  'filter',
		  {
			name: 'exports',
			onClick: function(obj) {
				// 当前示例配置项
				let options = obj.config;
				let total = obj.data.length;
				// 弹出面板
				obj.openPanel({
					list: [ // 列表
						'<li data-type="page">导出当前页</li>',
						'<li data-type="all">导出全部</li>'
					].join(''),
					done: function(panel, list) { 
						list.on('click', function() {
							if(total==0){
								layer.msg('暂无数据');
								return false;
							}
							let type = $(this).data('type')
							if(type === 'page') {
								// 调用内置导出方法
								let msg = layer.msg('正在导出数据...', {time:1000});
								table.exportFile(options.id, null, 'csv');
							}else if(type === 'all') {
								let tableWhere = JSON.parse(JSON.stringify(options.where));
								tableWhere.limit=options.excel_limit;
								if(total<=tableWhere.limit){
									tableWhere.page=1;
									let msg = layer.msg('正在导出数据...', {time:3000});
									$.ajax({
										url: options.url,
										data: tableWhere,
										success:function(res){
											table.exportFile(options.id, res.data,'csv');
											setTimeout(function(){
												layer.msg('导出完成');			
											},3000)	
										}
									});
								}
								else{
									tablePlus.excel(total,options)
								}
							}
						});
					}
				 });
			 }
		  },{
			  title:'数据说明',
			  icon: 'layui-icon-help',
			  layEvent: 'LAYTABLE_HELP',
			  onClick: function(obj) {
				layer.open({
					shadeClose: true,
					title:'帮助说明',
					type: 1,
					content: '<div style="padding:20px 15px; min-width:300px; line-height:1.8">'+obj.config.help+'</div>'
				})
			  }
		  }
		]
		
		if(params.defaultToolbar != false){
			params.defaultToolbar = defaultToolbar;
		}		
		if(params.cols_save == true){
			// 从本地存储获取用户保存的列显示设置
			let savedCols = localStorage.getItem('col-filter-'+params.url);
			let colsStatus = savedCols ? JSON.parse(savedCols) : {};
			let cols = params.cols;
			for (var i=0;i<cols[0].length;i++){
                if(cols[0][i].field!=undefined  && colsStatus[cols[0][i].field] != undefined){
                    cols[0][i].hide=colsStatus[cols[0][i].field];
                }
            }
			params.cols = cols;
			
			if(typeof params.done === "function"){
				let _done = params.done;
				params.done = function(data, curr, count){
					let obj = this;
					_done(data, curr, count);	
					obj.elem.next().on('mousedown', 'input[lay-filter="LAY_TABLE_TOOL_COLS"]+', function(){
						var input = $(this).prev()[0];
						layui.data ('col-filter-'+params.url,{
							key: input.name,
							value: input.checked
						})
					});
				}
			}
			else{
				params.done = function(data){
					let obj = this;
					obj.elem.next().on('mousedown', 'input[lay-filter="LAY_TABLE_TOOL_COLS"]+', function(){
						var input = $(this).prev()[0];
						layui.data ('col-filter-'+params.url,{
							key: input.name,
							value: input.checked
						})
					});
				}
			}			
		}
		var init = tablePlus._render(params);
		//监听搜索提交
		form.on('submit(table-search)', function(data) {
			init.reload({
				where: data.field,
				page: {curr: 1}
			});
			return false;
		});
		
		// 触发排序事件
		let filter = $(params.elem).attr('lay-filter');
		table.on('sort('+filter+')', function(obj){
			$('[name="'+params.order_field+'"]').val(obj.field);
			$('[name="'+params.order_type+'"]').val(obj.type);
            var searchObject = form.val('barsearchform');
			layui.pageTable.reload({
				where:searchObject,
				page: {curr: 1}
			});
		});
		
		//重置搜索提交
		$('body').on('click', '[lay-filter="table-reset"]', function () {
			let prev = $(this).prev();
			if (typeof(prev) != "undefined" ) {
				setTimeout(function () {
					prev.click();
				}, 10)
			}
		});		
		return init;
		//console.log(params);
    };
    exports(MOD_NAME, tablePlus);
});