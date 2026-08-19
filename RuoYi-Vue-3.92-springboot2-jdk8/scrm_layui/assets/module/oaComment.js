layui.define(['tool','oaPicker'], function (exports) {
	const layer = layui.layer, tool = layui.tool;
	const opts={
		"box":'commentBox',//容器id
		"input": 'commentInput',
		"total": 'commentTotal',
		"topic_id":0,
		"module": '',
		"callback":function(e){
			layer.msg(e.msg);
			if(e.code==0){
				setTimeout(function(){
					obj.load();
				},1000)
			}			
		}
	};
	const obj = {
		load: function () {
			let me = this;
			let topic_id = me.sets.topic_id, module = me.sets.module;
			let page=1;
			let callback = function (res) {
				layer.closeAll();
				me.template(res,page);
			}
			tool.get("/api/comment/datalist.json", {topic_id: topic_id,module: module,page:page}, callback);
		},
		template:function(res,page){
			let me = this;
			if (res.code == 0 && res.data.length > 0) {
				let itemComment = '';
				$.each(res.data, function (index, item) {
					let to_names = '', ops = '' ,ptext='';
					if (item.to_names !='' && item.to_names !='-') {
						to_names = '<span class="blue">@' + item.to_names + '</span>';
					}
					if (item.admin_id == login_admin) {
						ops = `<a class="mr-4" data-event="edit" data-id="${item.id}">编辑</a><a class="mr-4" data-event="del" data-id="${item.id}">删除</a>`;
					}
					else{
						ops = `<a class="mr-4" data-event="replay" data-id="${item.id}" data-uid="${item.admin_id}" data-unames="${item.name}">回复</a>`;
					}
					if(item.pid>0){
						ptext=`<div style="padding-bottom:8px;"><fieldset style="border:1px solid #eeeeee; background-color:#f9f9f9;"><legend>回复『${item.padmin}』${item.ptimes}的评论</legend>${item.pcontent}</fieldset></div>`;
					}
					itemComment += `
						<div id="comment_${item.id}" class="comment-item py-3 border-t" data-content="${item.content}">
						<div class="comment-avatar" title="${item.name}">
							<img class="comment-image" src="${item.thumb}">
						</div>
						<div class="comment-body">
							<div class="comment-meta">
								<strong class="comment-name">${item.name}</strong><span class="ml-2 gray" title="${item.create_time}">${item.create_times}${item.update_times}</span>
							</div>
							<div class="comment-content py-2">${to_names} ${item.content}</div>
							${ptext}
							<div class="comment-actions">${ops}</div>
						</div>
					</div>
					`;
				});
				if(res.data.length>19){
					page++;
					itemComment+='<div class="py-3 log-more"><button class="layui-btn layui-btn-normal layui-btn-sm" type="button">查看更多</button></div>';
				}
				$('#'+me.sets.box).html(itemComment).data('page',page);
				$('#'+me.sets.total).html(res.totalRow.total);
			}
			else{
				if(page ==1){
					$('#'+me.sets.box).html('<div class="layui-data-none">暂无记录</div>');
				}
			}
		},
		add: function (id,content,pid,to_uids) {
			let me = this;
			if (content == '') {
				layer.msg('请完善评论内容');
				return false;
			}
			let postData = { id: id, topic_id: me.sets.topic_id, pid: pid, to_uids: to_uids, module: me.sets.module, content: content};
			tool.post("/api/comment/add", postData, me.sets.callback);
		},
		del: function (id) {
			let me = this;
			layer.confirm('确定删除该评论吗？', {
				icon: 3,
				title: '提示'
			}, function (index) {
				tool.delete("/api/comment/del", { id: id }, me.sets.callback);
				layer.close(index);
			});
		},
		//文本
		textarea: function (id,txt, pid, to_uid, to_uname) {
			let that = this;
			let display='',usersInput='',height='286px';
			if(id==0){
				usersInput='<div class="layui-input-wrap" style="margin-bottom:5px;"><div class="layui-input-prefix"><i class="layui-icon layui-icon-at"></i></div><input type="text" placeholder="要提醒的员工" value="'+to_uname+'" readonly class="layui-input picker-admin" data-type="2" /><input type="hidden" id="to_uids" value="'+to_uid+'" /></div>';
				height='320px';
			}
			$(parent.$('.express-close')).addClass('parent-colse');
			layer.open({
				type: 1,
				title: '请输入评论内容',
				area: ['600px', height],
				content: '<div style="padding:5px;">'+usersInput+'<textarea class="layui-textarea" id="editTextarea" style="width: 100%; height: 160px;">'+txt+'</textarea></div>',
				end: function(){
					$(parent.$('.express-close')).removeClass('parent-colse');
				},
				btnAlign: 'c',
				btn: ['提交保存'],
				yes: function () {
					let to_uids = $("#to_uids").val();
					let newval = $("#editTextarea").val();
					if (newval != '') {
						that.add(id,newval,pid,to_uids);
					} else {
						layer.msg('请输入评论内容');
					}
				}
			})
		},
		init: function (options) {
			this.sets = $.extend({}, opts, options);
			let me = this;
			let commentBox = $('#'+me.sets.box);
			me.load();
			$('#'+me.sets.input).on('click', function () {
				me.textarea(0,'',0,0,'');
			})
			//回复
			commentBox.on('click', '[data-event="replay"]', function () {
				let pid = $(this).data('id');
				let to_uid = $(this).data('uid');
				let to_unames = $(this).data('unames');
				me.textarea(0,'',pid,to_uid,to_unames);
			})
			//编辑
			commentBox.on('click', '[data-event="edit"]', function () {
				let id = $(this).data('id');
				let content = commentBox.find('#comment_' + id).data('content');
				me.textarea(id,content,0,0,'');
			})
			//删除
			commentBox.on('click', '[data-event="del"]', function () {
				let id = $(this).data('id');
				me.del(id);
			})
			//加载更多
			commentBox.on('click','.log-more',function(){
				let page = commentBox.data(page);
				let callback = function (res) {
					me.template(res,page);
				}
				tool.get("/api/comment/datalist", {topic_id: me.sets.topic_id,module: me.sets.module,page:page}, callback);
			});
		}
	};
	exports('oaComment', obj);
});  