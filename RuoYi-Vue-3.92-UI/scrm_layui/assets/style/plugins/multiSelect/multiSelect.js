
$.fn.extend({
	multiSelect : function (obj) {
		var field=this.selector;
		$(field).hide();
		var label=obj.label;
		var data=obj.data;
		var selected=obj.selected;
		var placeholder=obj.placeholder;
		var onload = obj.onload;
		if(!placeholder){
			placeholder="请选择...";
		}
		var style=obj.style;
		var sel_txt='';
		if(selected && selected.length>0){
			for(var i=0;i<selected.length;i++){
				sel_txt+='<li class="mt_select_li">'+
							'<a class="mt_select_close" href="javascript:;"></a>'+
							'<label val="'+selected[i].value+'">'+selected[i].name+'</label>'+
						'</li>';
			}
		}
		
		var labelHtml="";
		if(label){
			labelHtml='<label class="mt_option_li_label">'+label+'</label>';
		}
		var multiHtml='<div class="mt_select_container">'+
			'<div class="mt_select_ful">'+
				'<ul class="mt_select_ul">'+sel_txt+
					'<li class="mt_select_active">'+
						'<input class="mt_select_input" maxlength="0" type="text" name="">'+
					'</li>'+
				'</ul>'+
			'</div>'+
			'<div class="mt_option_container">'+
				'<ul class="mt_option_ul">'+labelHtml+'</ul>'+
			'</div>'+
		'</div>';
		var selects=$(field);
		if(selects!=null && selects!=""){
			for(var n=0;n<selects.length;n++){
				var k=n;
				var idk=field.substring(1)+k;
				$("#multiSelect_"+idk).remove();
				$(selects[k]).attr("mt_val",k);
				$(selects[k]).before(multiHtml);
				var multiSelect=$(selects[k]).prev();
				$(multiSelect).attr("id","multiSelect_"+idk);
				$(multiSelect).children(".mt_select_ful").attr("id","mt_select_ful_"+idk);
				$("#mt_select_ful_"+idk).children(".mt_select_ul").attr("id","mt_select_ul_"+idk);
				$("#mt_select_ful_"+idk).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("id","mt_select_input_"+idk);
				$(multiSelect).children(".mt_option_container").attr("id","mt_option_container_"+idk);
				$("#mt_option_container_"+idk).children(".mt_option_ul").attr("id","mt_option_ul_"+idk);
				
				if(data && data.length>0){
					var optionHtml = '';
					$.each(data, function(index,item){
						var flag=0;
						 optionHtml = '<option value="'+item.value+'">'+item.name+'</option>';
						 if(selected){
						 	for(var j=0;j<selected.length;j++){
						 		if(selected[j].value==item.value){
						 			flag=1;
						 		}
						 	}
						 }
						 if(flag==0){
						 	var li='<li class="mt_option_li">'+
						 		'<label val="'+item.value+'" index="'+index+'" class="mt_option_val">'+item.name+'</label>'+
						 	'</li>';
						 	$(multiSelect).children("div").eq(1).children("ul").append(li);
						 }
					});
					$(selects[k]).html(optionHtml);
				}else{
					var options=$(selects[k]).children("option");
					data = [];
					if(options && options.length>0){
						for(var i=0;i<options.length;i++){
							var flag=0;
							var val=$(options[i]).val();
							var name=$(options[i]).html();
							if(!val){
								val=name;
							}
							if(selected){
								for(var j=0;j<selected.length;j++){
									if(selected[j].value==val){
										flag=1;
									}
								}
							}
							if(flag==0){
								var li='<li class="mt_option_li">'+
									'<label val="'+val+'" index="'+i+'" class="mt_option_val">'+name+'</label>'+
								'</li>';
								$(multiSelect).children("div").eq(1).children("ul").append(li);
							}
							var d = {
								name: name,
								value: val
							};
							data.push(d);
						}
					}
				}
				
				if(placeholder){
					$("#mt_select_ful_"+idk).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder",placeholder);
				}
				if(sel_txt){
					$("#mt_select_ful_"+idk).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").width(20);
					$("#mt_select_ful_"+idk).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder","");
				}
				var height;
				var lineHeight;
				if(style){
					var width=style.width;
					height=style.height;
					lineHeight = style.lineHeight;
					if(height && !lineHeight){
						lineHeight = height;
					}
					var border=style.border;
					if(border){
						$("#multiSelect_"+idk).css("border",border);
						$("#mt_option_container_"+idk).css("border",border);
						$("#mt_option_container_"+idk).css("border-top","none");
					}
					if(width){
						$("#multiSelect_"+idk).css("width",width);
					}
					if(height){
						var numHeight = parseInt(height.substring(0,height.length-2));
						$("#multiSelect_"+idk+" .mt_select_ul li").css("height",(numHeight-8)+"px");
						$("#mt_option_container_"+idk+" .mt_option_ul li").css("height",height);
						var mtop=(numHeight-13-8)/2;
						$("#multiSelect_"+idk+" .mt_select_ul li a").css("margin-top",mtop+"px");
					}
					if(lineHeight){
						var numLineHeight = parseInt(lineHeight.substring(0,lineHeight.length-2));
						$("#multiSelect_"+idk+" .mt_select_ul li").css("line-height",(numLineHeight-8)+"px");
						$("#mt_option_container_"+idk+" .mt_option_ul li").css("line-height",lineHeight);
					}
				}
				bindFunc(obj, field, idk,height,lineHeight);
			}
		}
		if(onload){
			onload(data);
		}
	},multiVal:function(){
		var select=this.selector;
		var valArr;
		if (select){
			valArr=new Array();
			var k=$(select).attr("mt_val");
			var idk=select.substring(1)+k;
			var labels=$("#mt_select_ul_"+idk+" li label");
			for (var i =0;i< labels.length; i++) {
				var val=$(labels[i]).attr("val");
				valArr.push(val);
			}
		}
		return valArr;
	},multiText:function(){
		var select=this.selector;
		var valArr;
		if (select){
			valArr=new Array();
			var k=$(select).attr("mt_val");
			var idk=select.substring(1)+k;
			var labels=$("#mt_select_ul_"+idk+" li label");
			for (var i =0;i< labels.length; i++) {
				var val=$(labels[i]).text();
				valArr.push(val);
			}
		}
		return valArr;
	},multiData:function(){
		var select=this.selector;
		var array;
		if (select){
			array=new Array();
			var k=$(select).attr("mt_val");
			var idk=select.substring(1)+k;
			var labels=$("#mt_select_ul_"+idk+" li label");
			for (var i =0;i< labels.length; i++) {
				var o = {
					name: $(labels[i]).text(),
					value: $(labels[i]).attr("val")
				};
				array.push(o);
			}
		}
		return array;
	}
});
function bindFunc(obj, selector, k,height,lineHeight){
	
	var onchange = obj.onchange;
	var onclick = obj.onclick;
	
	var no_match_label='<label class="no_match_label">没有找到匹配项</label>';
	$(".mt_select_container").on("click","#mt_select_ul_"+k+" li .mt_select_close",function(){
		$(this).parent().remove();
		var name=$(this).parent().children("label").eq(0).html();
		var val=$(this).parent().children("label").eq(0).attr("val");
		var li='<li class="mt_option_li">'+
					'<label val="'+val+'" class="mt_option_val">'+name+'</label>'+
				'</li>';
		$("#mt_option_ul_"+k).append(li); 
		$("#mt_option_container_"+k).hide();
		$("#mt_select_input_"+k).focus();
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").width(20);
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder","");
		$("#mt_option_ul_"+k +" .no_match_label").remove();
		$("#mt_option_ul_"+k +" .mt_option_li_label").show();

		if(height){
			var numHeight = parseInt(height.substring(0,height.length-2));
			$("#multiSelect_"+k+" .mt_select_ul li").css("height",(numHeight-8)+"px");
			$("#mt_option_container_"+k+" .mt_option_ul li").css("height",height);
			var mtop=(numHeight-13-8)/2;
			$("#multiSelect_"+k+" .mt_select_ul li a").css("margin-top",mtop+"px");
		}
		if(lineHeight){
			var numLineHeight = parseInt(lineHeight.substring(0,lineHeight.length-2));
			$("#multiSelect_"+k+" .mt_select_ul li").css("line-height",(numLineHeight-8)+"px");
			$("#mt_option_container_"+k+" .mt_option_ul li").css("line-height",lineHeight);
		}
		
		if(onchange){
			onchange($(selector).multiData());
		}
		
	});

	$(".mt_select_container").on("click","#mt_select_input_"+k,function(){
		var disStyle =$("#mt_option_container_"+k).css("display")
		if(disStyle == "block"){
			$("#mt_option_container_"+k).hide();
			return;
		}
		$(".mt_option_container").hide();
		$("#mt_option_container_"+k).show();
		$(this).focus();
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").width(20);
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder","");
		var option=$(".mt_option_container #mt_option_ul_"+k+" li");
		if(option && option.length==0 || !option){
			$("#mt_option_ul_"+k +" .no_match_label").remove();
			$("#mt_option_container_"+k).children("ul").append(no_match_label);
			$("#mt_option_ul_"+k +" .no_match_label").show();
			$("#mt_option_ul_"+k +" .mt_option_li_label").hide();
		}
	});
	$(".mt_select_container").on("click","#mt_select_ful_"+k+" .mt_select_ul .mt_select_active",function(){
		var disStyle =$("#mt_option_container_"+k).css("display")
		if(disStyle == "block"){
			$("#mt_option_container_"+k).hide();
			return;
		}else{
			$("#mt_option_container_"+k).show();
		}
	});

	$(".mt_select_container").on("click","#mt_select_ful_"+k,function(){
		var disStyle =$("#mt_option_container_"+k).css("display")
		if(disStyle == "block"){
			$("#mt_option_container_"+k).hide();
			return;
		}
		$(".mt_option_container").hide();
		$("#mt_option_container_"+k).show();
		$("#mt_select_input_"+k).focus();
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").width(20);
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder","");
		var option=$(".mt_option_container #mt_option_ul_"+k+" li");
		if(option && option.length==0 || !option){
			$("#mt_option_ul_"+k +" .no_match_label").remove();
			$("#mt_option_container_"+k).children("ul").append(no_match_label);
			$("#mt_option_ul_"+k +" .no_match_label").show();
			$("#mt_option_ul_"+k +" .mt_option_li_label").hide();
		}
	});

	$(".mt_select_container").on("click","#mt_option_ul_"+k+" .mt_option_li",function(){
		$(this).remove();
		var name=$(this).children("label").eq(0).html();
		var val=$(this).children("label").eq(0).attr("val");
		var li='<li class="mt_select_li">'+
				'<a class="mt_select_close" href="javascript:;"></a>'+
				'<label val="'+val+'">'+name+'</label>'+
			'</li>';
		$("#mt_select_ul_"+k+" .mt_select_active").before(li);
		$("#mt_option_container_"+k).hide();
		$("#mt_select_input_"+k).focus();
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").width(20);
		$("#mt_select_ful_"+k).children(".mt_select_ul").children(".mt_select_active").children(".mt_select_input").attr("placeholder","");
		
		if(height){
			var numHeight = parseInt(height.substring(0,height.length-2));
			$("#multiSelect_"+k+" .mt_select_ul li").css("height",(numHeight-8)+"px");
			$("#mt_option_container_"+k+" .mt_option_ul li").css("height",height);
			var mtop=(numHeight-13-8)/2;
			$("#multiSelect_"+k+" .mt_select_ul li a").css("margin-top",mtop+"px");
		}
		
		if(lineHeight){
			var numLineHeight = parseInt(lineHeight.substring(0,lineHeight.length-2));
			$("#multiSelect_"+k+" .mt_select_ul li").css("line-height",(numLineHeight-8)+"px");
			$("#mt_option_container_"+k+" .mt_option_ul li").css("line-height",lineHeight);
		}
		
		if(onclick){
			var data = {
				name: name,
				value: val
			};
			onclick(data);
		}
		
		if(onchange){
			onchange($(selector).multiData());
		}
		
	});
}
$(document).on("click",".no_match_label",function(){
	$(this).parent().parent().hide();
});


