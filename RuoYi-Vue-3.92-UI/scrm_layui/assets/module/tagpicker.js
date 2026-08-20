layui.define(['layer'],function(exports){
  //提示：模块也可以依赖其它模块，如：layui.define('layer', callback);
  var layer = layui.layer;
  var opts={
		"title":'请选择',
		"len": 5,
		"url":null,
		"where":{},
		"width":720,
		"height":320,
		"target":'tag',
		"field_id":'id',
		"field_title":'title',
		"tag_ids": 'tag_ids',
		"tag_tags": 'tag_tags',
		"isDiy":0,
		"default":0,
		"callback": null
	};
	var tagpicker = function(options){
		this.settings = $.extend({}, opts, options);
		this.ajaxData = [];
		this.createStyle();
		var me=this;
		$('#'+me.settings.target).click(function(){
			me.init();
		});
	};    
	tagpicker.prototype = {		
		init: function () {
			var me 		= this;
			$.ajax({
			  url:me.settings.url,
			  type:'get',
			  data:me.settings.where,
			  success:function(e){
				if(e.code==0){
					if(e.data.length>0){
						me.ajaxData=e.data;
					}					
					layer.open({
						title:me.settings.title,
						area:[me.settings.width+'px',me.settings.height+'px'],
						content:me.render(),
						type:1,
						success:function(){
							me.click();
						},
						btn: ['确定'],
						yes: function(idx){
							me.set();
							layer.close(idx);
						}
					});
				}else{
				  layer.msg(e.msg);
				}
			  }
			})
		},
		render: function (){
			var me=this,_li='',_item='',_input='',_data=this.ajaxData,_ids=$('#'+this.settings.tag_ids).val(),_tags=$('#'+this.settings.tag_tags).val();
			if(_tags!='' && _tags.length>0){
				var tags_array=_tags.split(',');
				var ids_array=_ids.split(',');
				for(var i=0;i<tags_array.length;i++){
					_item+='<li data-id="'+ ids_array[i] +'" data-title="' + tags_array[i] + '">'+ tags_array[i] +'<a title="删除" class="del">×</a></li>';
				}
			}
			for(var i=0;i<_data.length;i++){
				if($.inArray(_data[i][this.settings.field_id].toString(),ids_array)>=0){
					_li+='<li class="a_cur" data-id="'+_data[i][this.settings.field_id]+'" data-title="'+_data[i][this.settings.field_title]+'">'+_data[i][this.settings.field_title]+'</li>';		
				}
				else{
					_li+='<li data-id="'+_data[i][this.settings.field_id]+'" data-title="'+_data[i][this.settings.field_title]+'">'+_data[i][this.settings.field_title]+'</li>';
				}
			}
			if(_item=='' && me.settings.isDiy==0){
				_item='<span style="color:#aaa; line-height:36px; display:inline-block">请选择下面的选项</span>';
			}
			if(me.settings.isDiy==1){
				_input='<input onfocus="this.style.color=\'#000000\'" onblur="this.style.color=\'#ccc\'" class="tag-txt" style="color: #333;" title="空格键或切换焦点提交" placeholder="可在此输入关键词,按空格增加" />';
			}
			var template = '<div class="selectbox">\
				<div id="keyword_list_'+this.settings.target+'" class="keywords">\
					<ul>'+_item+'</ul>'+_input+'\
				</div>\
				<div id="keyword_list_wrap_'+this.settings.target+'" class="tag-wrap">\
					<div class="tag-box">\
						<ul>'+_li+'</ul>\
					</div>\
				</div>\
			</div>';
			return template;
		},
		click:function(){
			var me=this,
				list=$('#keyword_list_'+me.settings.target),
			    list_wrap=$('#keyword_list_wrap_'+me.settings.target),
				tags 	= $("li", list),
				wraps 	= $('li', list_wrap);
			tags.each(function(i, o) {
				$(o).find('a').on('click', function() {
					var li = $(this).parents('li');
					me.remove(li);
				})
			});
			wraps.on('click', function() {
				me.select(this);
			});
			list.on('click', function() {
				$(".tag-txt", list).focus();
			});
			//如果开启自定义关键字
			if(me.settings.isDiy==1){
				$(".tag-txt", list).on('blur', function(e) {
					var text = $.trim($(this).val());
					if (me.add(text, 0)) {
						$(this).val("");
					}
				}).on('keypress', function(e) {
					var text = $.trim($(this).val());
					e = e ? e : window.event;
					var keyCode = e.which ? e.which : e.keyCode;
					if (keyCode == 32) {
						if (me.add(text, 0)) {
							$(this).val("");
						} else {
							$(this).val(text);
						}
					}
				}).on('keyup', function(e) {
					var text = $(this).val();
					e = e ? e : window.event;
					var keyCode = e.which ? e.which : e.keyCode;
					if (keyCode == 8) {
						if (text.length == 0) {
							var li = $('li', list).last();
							if (li) {
								li.find('a').trigger('click');
							}
						}
					}
					me.set();
				});	
			}
		},
		add: function(text, id) {
			var me 	= this,
				list=$('#keyword_list_'+me.settings.target),
				list_wrap=$('#keyword_list_wrap_'+me.settings.target),
				tags = $('li', list);
			text = $.trim(text);
			if (text == "") {
				return true;
			}
			if (text.length < 2) {
				layer.msg("关键词【"+text+"】至少要两个字以上");
				return false;
			}
			if (!(/^[\u0391-\uFFE5A-Za-z0-9]+$/.test(text))) {
				layer.msg("关键词【"+text+"】不能还有特殊字符或空格");
				return false;
			}
			if(tags.length==0){
					$('span', list).remove();
			}
			if(tags.length>=me.settings.len){
				layer.msg("最多个添加" + me.settings.len + "个选项");
				return false;
			}
			for (var i = 0; i < tags.length; i++) {
				if ($(tags[i]).data('title') == text) {
					layer.msg("关键词【"+text+"】关键词已添加");
					return false;
				}
			}
			var node = $('<li data-id="'+ id +'" data-title="' + text + '">'+ text +'<a title="删除" class="del">×</a></li>');
			node.find("a").click(function() {
				var li = $(this).parents('li');
				me.remove(li);
			});
			$('li[data-title="' + text + '"]',list_wrap).addClass('a_cur');
			node.appendTo($("ul", list));
			return true;
		},
		select: function (item) {
			var me 		= this,
				list=$('#keyword_list_'+me.settings.target),
				li 		= $(item),
				text  	= li.data('title'),
				id 	= li.data('id');
			if (li.hasClass('a_cur')) {
				li.removeClass("a_cur");
				me.del(text, id);
			} else {
				me.add(text,id);
			}
		},
        remove: function(li) {
			var me 		= this,
				list=$('#keyword_list_'+me.settings.target),
			    list_wrap=$('#keyword_list_wrap_'+me.settings.target),
				text  = $(li).data('title'),
				id 	= $(li).data('id');
			
			var wrap  = $('li[data-title="' + text + '"]', list_wrap);
			wrap && wrap.removeClass("a_cur");
			me.del(text, id);
		},
		del: function(text, id) {
			var me 		= this,
				list=$('#keyword_list_'+me.settings.target),
				tags 	= $('li', list);
			for(var i = 0; i < tags.length; i++) {
				if ($(tags[i]).data('id') == id) {
					$(tags[i]).remove();
					if(tags.length==1 && me.settings.isDiy==0){
						$('ul', list).append('<span style="color:#aaa; line-height:36px; display:inline-block">请选择下面的选项</span>');
					}
					return true;
				}
			}
			return false;
		},
		set: function() { 
			var me = this;
				list=$('#keyword_list_'+me.settings.target),
			    list_wrap=$('#keyword_list_wrap_'+me.settings.target),
				tags = $('li', list);
			var ids=[],text = [];
			tags.each(function(i, o) {
				ids.push($(o).data('id'));
				text.push($(o).data('title'));
			});
			$("#"+me.settings.tag_ids).val(ids.join(','));
			$("#"+me.settings.tag_tags).val(text.join(','));
		},		
		createStyle:function(){
			var cssText='.selectbox .keywords{padding:8px 0 10px 10px; color: #bfbfbf; background: #ffffff; min-height:35px; line-height: 32px; width: 700px;}\
			.selectbox .keywords ul{float:left;}\
			.selectbox .keywords ul li{background: #e1f2ff; color:#01AAED; border: 1px solid #69d6fe; display:inline-block; font-size: 12px;line-height: 20px;margin: 5px 3px;padding: 3px 5px 2px 6px; border-radius:2px;}\
			.selectbox .keywords ul li a {color: #333;text-decoration: none;}\
			.selectbox .keywords ul li a.del{cursor: pointer; font-size:16px; padding:0 1px 0 5px;text-decoration: none;}\
			.selectbox .keywords .tag-txt{background:#fff; float:left; border: 1px solid #fff;height: 36px;line-height: 36px;white-space: nowrap;width: 200px;}\
			.selectbox .tag-wrap{border-top: 1px solid #e7e8eb; background: #ffffff;}\
			.selectbox .tag-wrap .tag-box{padding: 15px;}\
			.selectbox .tag-wrap .tag-box ul li{background: transparent; border:1px solid #ddd; cursor:pointer; border-radius:3px; font-size:12px; background-color:#f9f9f9; display:inline-block; line-height: 27px; margin-bottom:6px; margin-right: 6px;text-align: center; padding:0 10px; position: relative;}\
			.selectbox .tag-wrap .tag-box ul li.a_cur{background: #1E9FFF;  border:1px solid #1E9FFF; color:#fff;}';
			
			var document = window.document;
			var styleTag = document.createElement("style");
			styleTag.setAttribute("type", "text/css");
			if (styleTag.styleSheet) {    //ie
				styleTag.styleSheet.cssText += cssText;
			}
			else{			
				styleTag.innerHTML = cssText;
			}        
			document.getElementsByTagName("head").item(0).appendChild(styleTag);
		}
	}

  //输出接口
  exports('tagpicker', tagpicker);
});   