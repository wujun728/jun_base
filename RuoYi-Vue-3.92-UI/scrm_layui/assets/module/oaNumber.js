layui.define([], function (exports) {
	let oaNumber = {
		// 加法函数
		accAdd:function(arg1, arg2) {
			var r1, r2, m;
			try {
				// 获取到小数点的位数
				r1 = arg1.toString().split(".")[1].length;
			} catch (e) {
				r1 = 0;
			}
			try {
				r2 = arg2.toString().split(".")[1].length;
			} catch (e) {
				r2 = 0;
			}
			// 取位数最大的 10^ n次方
			m = Math.pow(10, Math.max(r1, r2));
			return (arg1 * m + arg2 * m) / m;
		},
		//减法函数
		accSub:function(arg1, arg2) {
			var r1, r2, m, n;
			try {
				r1 = arg1.toString().split(".")[1].length;
			} catch (e) {
				r1 = 0;
			}
			try {
				r2 = arg2.toString().split(".")[1].length;
			} catch (e) {
				r2 = 0;
			}
			m = Math.pow(10, Math.max(r1, r2)); // 动态控制精度长度
			// 计算结果保留的位数
			n = (r1 >= r2) ? r1 : r2;
			return parseFloat(((arg1 * m - arg2 * m) / m).toFixed(n));
		},
		//乘法函数
		accMul:function(arg1, arg2) {
			var m = 0,
				s1 = arg1.toString(),
				s2 = arg2.toString();
			try {
				m += s1.split(".")[1].length;
			} catch (e) {
			}
			try {
				m += s2.split(".")[1].length;
			} catch (e) {
			}
			return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
		},
		//除法函数
		accDiv(arg1, arg2) {
			var t1 = 0,
				t2 = 0,
				r1, r2;
			try {
				t1 = arg1.toString().split(".")[1].length;
			} catch (e) {
			}
			try {
				t2 = arg2.toString().split(".")[1].length;
			} catch (e) {
			}
			with (Math) {
				r1 = Number(arg1.toString().replace(".", ""));
				r2 = Number(arg2.toString().replace(".", ""));
				return (r1 / r2) * pow(10, t2 - t1);
			}
		},
		//千分号格式化金额
		accFormat:function (num) {
			if (typeof num !== 'number') {
				return '-';
			}
			if(num=='-'||num==''){
				return '-';
			}
			var res=num.toString().replace(/\d+/, function(n){ // 先提取整数部分
			   return n.replace(/(\d)(?=(\d{3})+$)/g,function($1){
				  return $1+",";
				});
			})
			return res;
		},
		//金额转人民币大写
	    accCny:function(num){
			if (typeof num !== 'number') {
				return '-';
			}
			if (num === 0) {
				return '零元整';
			}
			const fraction = ['角', '分'];
			const digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
			const unit = [['元', '万', '亿'], ['', '拾', '佰', '仟']];
			let head = num < 0 ? '欠' : '';
			num = Math.abs(num);
			let s = '';
			for (let i = 0; i < fraction.length; i++) {
				s += (digit[Math.floor(num * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
			}
			s = s || '整';
			num = Math.floor(num);
			for (let i = 0; i < unit[0].length && num > 0; i++) {
				let p = '';
				for (let j = 0; j < unit[1].length && num > 0; j++) {
					p = digit[num % 10] + unit[1][j] + p;
					num = Math.floor(num / 10);
				}
				s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
			}
			return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
		}
	};
	exports('oaNumber', oaNumber);
});  