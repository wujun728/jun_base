layui.define(['tool','oaPicker'], function (exports) {
	const layer = layui.layer;
	const form = layui.form;
	const tool = layui.tool;
	const laydate = layui.laydate;
	const oaPicker = layui.oaPicker;
	// 定义工作时间段
	const workPeriods = [
		{ start: 8, end: 12 }, // 早上工作时间段
		{ start: 14, end: 18 } // 下午工作时间段
	];	
	const obj = {
		//计算工时
		calculateWorkHours:function(startTimeStr, endTimeStr) {
			if(startTimeStr == '' || endTimeStr==''){
				return 0;
			}
			// 解析时间字符串为Date对象
			const startTime = new Date(startTimeStr);
			const endTime = new Date(endTimeStr);

			// 确保结束时间晚于或等于开始时间
			if (endTime < startTime) {
				layer.msg('结束时间必须晚于或等于开始时间');
				return 0;
			}
			let totalWorkHours = 0;

			// 遍历每一天
			let currentDay = new Date(startTime);
			let nextDay = new Date(currentDay);
			//nextDay.setDate(nextDay.getDate() + 1); // 设置为下一天的同一时间

			while (currentDay <= endTime) {
				// 计算当前天的工作时间
				for (const period of workPeriods) {
					// 获取当前天的工作时间段开始和结束时间
					let dayStart = new Date(currentDay.getFullYear(), currentDay.getMonth(), currentDay.getDate(), period.start, 0, 0);
					let dayEnd = new Date(currentDay.getFullYear(), currentDay.getMonth(), currentDay.getDate(), period.end, 0, 0);

					// 检查是否在当前天的工作时间段内
					if (currentDay <= dayEnd && (dayStart <= endTime || dayStart <= currentDay)) {
						// 计算重叠的时间段
						let overlapStart = Math.max(dayStart, Math.max(currentDay, startTime));
						let overlapEnd = Math.min(dayEnd, endTime);

						// 计算重叠时间段的时长（以毫秒为单位）
						let overlapDuration = overlapEnd - overlapStart;

						// 将时长转换为小时，并累加到总工时
						totalWorkHours += overlapDuration / (1000 * 60 * 60);
					}
				}

				// 移到下一天
				currentDay = nextDay;
				nextDay.setDate(nextDay.getDate() + 1);
			}
			// 保留两位小数
			return totalWorkHours.toFixed(2);
		},
		count_total:function(detail){
			let amount=0,work_total=0;
			if(detail.fee==''){
				detail.fee=0;
			}
			if(detail.hours==0){
				amount = detail.fee;
			}
			else{
				work_total = detail.hours*detail.work_price;
				amount = (detail.fee*100+work_total*100)/100;
			}
			let total = {amount:amount.toFixed(2),work_total:work_total.toFixed(2)}
			return total;
		},
		//保存
		save: function (postData) {
			let callback = function (e) {
				if (e.code == 0) {
					layer.closeAll();
					layer.msg(e.msg);
					if(layui.workTable){
						layui.workTable.reload();
					}
					else{
						setTimeout(function(){
							location.reload();
						},1000);	
					}					
				} else {
					layer.msg(e.msg);
				}
			}
			tool.post("/task/work/add", postData, callback);
		},
		//查看
		view: function (detail) {
			let demand = '',problems = '',task = '';
			if(detail.demand_id>0){
				demand = `<tr><td class="layui-td-gray">关联请求</td><td colspan="3">${detail.demand}</td></tr>`;
			}
			if(detail.problems_id>0){
				problems = `<tr><td class="layui-td-gray">关联问题</td><td colspan="3">${detail.problems}</td></tr>`;
			}
			if(detail.task_id>0){
				task = `<tr><td class="layui-td-gray">关联任务</td><td colspan="3">${detail.task}</td></tr>`;
			}
			let content = `<div class="p-2">
					<table class="layui-table" style="margin:0">
						${demand}
						${problems}
						${task}
						<tr>
							<td class="layui-td-gray-2">开始时间</td>
							<td>${detail.start_time}</td>
							<td class="layui-td-gray-2">结束时间</td>
							<td>${detail.end_time}</td>
						</tr>
						<tr>
							<td class="layui-td-gray">所有者</td>
							<td>${detail.director_name}</td>
							<td class="layui-td-gray">工时费用(元)</td>
							<td>${detail.work_price}</td>
						</tr>
						<tr>
							<td class="layui-td-gray">所用工时(小时)</td>
							<td>${detail.hours}</td>
							<td class="layui-td-gray">技术费用(元)</td>
							<td>${detail.work_total}</td>
						</tr>
						<tr>
							<td class="layui-td-gray">其他费用(元)</td>
							<td>${detail.fee}</td>
							<td class="layui-td-gray">总费用(元)</td>
							<td>${detail.amount}</td>
						</tr>
						<tr>
							<td class="layui-td-gray">工作内容</td>
							<td colspan="3">${detail.title}</td>
						</tr>
						<tr>
							<td class="layui-td-gray">补充描述</td>
							<td colspan="3">${detail.remark}</td>
						</tr>
					</table>
				</div>`;
			layer.open({
				type: 1,
				title: '工作记录详情',
				area: ['692px', '432px'],
				content: content,
				success: function () {

				},
				btn: ['关闭'],
				btnAlign: 'c',
				yes: function (idx) {
					layer.close(idx);
				}
			})
		},
		add: function (schedule) {
			let that = this, detail = {}, title = '添加工作记录',timestamp = new Date().getTime();;
			let html_time = '';
			if (schedule['id'] > 0) {
				title = '编辑工作记录';
				detail['id'] = schedule['id'];
				detail['title'] = schedule['title'];
				detail['start_time'] = schedule['start_time'];
				detail['end_time'] = schedule['end_time'];
				detail['director_name'] = schedule['director_name'];
				detail['director_id'] = schedule['director_id'];
				detail['hours'] = schedule['hours'];
				detail['work_price'] = schedule['work_price'];
				detail['fee'] = schedule['fee'];
				detail['work_total'] = schedule['work_total'];
				detail['amount'] = schedule['amount'];
				detail['remark'] = schedule['remark'];
			} else {
				detail['task_id'] = schedule['task_id'];
				detail['problems_id'] = schedule['problems_id'];
				detail['demand_id'] = schedule['demand_id'];
				detail['title'] = '';
				detail['start_time'] = '';
				detail['end_time'] = '';
				detail['director_name'] = '';
				detail['director_id'] = 0;
				detail['hours'] = 0;
				detail['work_price'] = 0;
				detail['fee'] = 0;
				detail['work_total'] = 0;
				detail['amount'] = 0;
				detail['remark'] = '';
			}			
			let content = `<form class="layui-form p-2">
					<table class="layui-table" style="margin:0" id="table${timestamp}">
						<tr>
							<td class="layui-td-gray-2">开始时间 <span style="color: red">*</span></td>
							<td><input id="start_time" name="start_time" class="layui-input" value="${detail.start_time}" readonly lay-verify="required" lay-reqText="请选择"></td>
							<td class="layui-td-gray-2">结束时间 <span style="color: red">*</span></td>
							<td><input id="end_time" name="end_time" class="layui-input" value="${detail.end_time}" readonly lay-verify="required" lay-reqText="请选择"></td>
						</tr>
						<tr>
							<td class="layui-td-gray">所有者<font>*</font></td>
							<td>
								<input type="text" name="director_name" lay-verify="required" readonly placeholder="请选择" lay-reqText="请完善所有者" class="layui-input picker-diy" value="${detail.director_name}">
								<input type="hidden" name="director_id" value="${detail.director_id}">
							</td>
							<td class="layui-td-gray">工时费用(元)</td>
							<td><input name="work_price" class="layui-input layui-input-readonly" value="${detail.work_price}" readonly lay-verify="number"></td>
						</tr>
						<tr>
							<td class="layui-td-gray">所用工时(小时)</td>
							<td><input name="hours" class="layui-input layui-input-readonly" readonly value="${detail.hours}" lay-verify="number"></td>
							<td class="layui-td-gray">技术费用(元)</td>
							<td><input name="work_total" class="layui-input layui-input-readonly" value="${detail.work_total}" readonly lay-verify="number"></td>
						</tr>
						<tr>
							<td class="layui-td-gray">其他费用(元)</td>
							<td><input name="fee" class="layui-input" value="${detail.fee}" lay-verify="number"></td>
							<td class="layui-td-gray">总费用(元)</td>
							<td><input name="amount" class="layui-input layui-input-readonly" value="${detail.amount}" readonly lay-verify="number"></td>
						</tr>
						<tr>
							<td class="layui-td-gray">工作内容 <span style="color: red">*</span></td>
							<td colspan="3"><input name="title" class="layui-input" value="${detail.title}" lay-verify="required" lay-reqText="请完成工作内容"></td>
						</tr>
						<tr>
							<td class="layui-td-gray">补充描述</td>
							<td colspan="3">
								<textarea name="remark" form-input="remark" class="layui-textarea" style="min-height:92px;">${detail.remark}</textarea>
								<input name="tid" type="hidden" value="${detail.tid}">
							</td>
						</tr>
					</table>
				</form>`;					
			layer.open({
				type: 1,
				title: title,
				area: ['800px', '500px'],
				content: content,
				success: function () {	
					//选择员工弹窗        
					$('#table'+timestamp).on('click','.picker-diy',function () {
						let self = $(this);
						let ids=self.next().val()+'',names = self.val()+'';
						oaPicker.employeeInit({
							ids:ids,
							names:names,
							type:1,//1是单选，2是多选
							callback:function(data){
								self.val(data[0].name);
								self.next().val(data[0].id);
								detail.director_id = data[0].id;
								if(data[0].id==0){
									data[0].work_price=0.00;
								}
								detail.work_price = data[0].work_price;
								$('[name="work_price"]').val(data[0].work_price);
								let total = that.count_total(detail);
								$('[name="amount"]').val(total.amount);
								$('[name="work_total"]').val(total.work_total);
								detail.amount = total.amount;
								detail.work_total = total.work_total;
							}
						});
					});				
					//日期时间范围
					laydate.render({
						elem: '#start_time',
						max: 0,
						type: 'datetime',
						format: 'yyyy-MM-dd HH:mm',
						fullPanel: true,
						done: function (a, b, c) {
							detail.start_time = a;
							detail.hours = that.calculateWorkHours(detail.start_time,detail.end_time);
							$('[name="hours"]').val(detail.hours);
							let total = that.count_total(detail);
							$('[name="amount"]').val(total.amount);
							$('[name="work_total"]').val(total.work_total);
							detail.amount = total.amount;
							detail.work_total = total.work_total;
						}
					});

					//日期时间范围
					laydate.render({
						elem: '#end_time',
						max: 0,
						type: 'datetime',
						format: 'yyyy-MM-dd HH:mm',
						fullPanel: true,
						done: function (a, b, c) {
							detail.end_time = a;
							that.calculateWorkHours(detail.start_time,detail.end_time);
							detail.hours = that.calculateWorkHours(detail.start_time,detail.end_time);
							$('[name="hours"]').val(detail.hours);
							let total = that.count_total(detail);
							$('[name="amount"]').val(total.amount);
							$('[name="work_total"]').val(total.work_total);
							detail.amount = total.amount;
							detail.work_total = total.work_total;
						}
					});
					
					form.render();
					
					$('[name="title"]').on('input', function () {
						var _val = $(this).val();
						detail.title = _val;
					});
					$('[name="fee"]').on('input', function () {
						this.value = this.value.replace(/[^0-9]/g,'');
						var _val = $(this).val();
						detail.fee = _val;
						let total = that.count_total(detail);
						$('[name="amount"]').val(total.amount);
						$('[name="work_total"]').val(total.work_total);
						detail.amount = total.amount;
						detail.work_total = total.work_total;
					});
					$('[form-input="remark"]').on('input', function () {
						var _val = $(this).val();
						detail.remark = _val;
					});
				},
				btn: ['确定提交'],
				btnAlign: 'c',
				yes: function (idx) {
					if (detail.start_time == '') {
						layer.msg('请选择开始时间');
						return;
					}
					if (detail.end_time == '') {
						layer.msg('请选择结束时间');
						return;
					}
					if(detail.hours*100<=0){
						layer.msg('请选择工作时间有问题');
						return;
					}
					if(detail.director_id==0){
						layer.msg('请完善所有者');
						return;
					}
					if (detail.title == '') {
						layer.msg('请填写工作内容');
						return;
					}
					console.log(detail);
					that.save(detail);
				}
			})
		}
	};
	exports('oaWork', obj);
});  