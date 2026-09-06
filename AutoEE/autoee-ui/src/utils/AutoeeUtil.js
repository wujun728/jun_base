import {
	createVNode,
	defineComponent
} from 'vue';
import * as $Icon from '@ant-design/icons-vue';
import {
	AutoeeIcon
} from '@/utils/AutoeeIcon.js';
import axios from 'axios';
import {
	saveAs
} from 'file-saver';
import {
	getToken
} from '@/utils/auth';
import {
	Modal
} from 'ant-design-vue';
import {
	getDicts as getDicts
} from '@/api/system/dict/data';
import {
	treeselect
} from '@/api/system/dept';
import {
	usePagination
} from 'vue-request';
import {
	useRequest
} from 'vue-request';
import {
	message
} from 'ant-design-vue';
import {
	handleTree,
	tansParams
} from '@/utils/ruoyi.js';
import errorCode from '@/utils/errorCode';
import {
	parseTime,
	blobValidate
} from '@/utils/ruoyi';
import request from '@/utils/request';
import {
	download as commDownload
} from '@/utils/request';

const baseURL =
	import.meta.env.VITE_API_DOMAIN;

const AutoeeUtil = {
	parseDateTime: function(dateTime, pattern) {
		return parseTime(dateTime, pattern);
	},
	getDictLabelByKey: function(key, dict) {
		// key为0时，判断返回false
		// if (key && dict) {
		if (dict) {
			for (let index = 0; index < dict.length; index++) {
				const element = dict[index];
				// 都转为字符后进行判断，防止数值类型和字符类型比较时，返回false
				if (element.value + '' === key + '') {
					return element.label;
				}
			}
		} else {
			return key;
		}
	},
	getPostNamesByPostIds: function(postIds, dict) {
		let postNames = '';
		for (let index = 0; index < dict.length; index++) {
			const element = dict[index];
			for (let i = 0; i < postIds.length; i++) {
				if (element.value === postIds[i]) {
					if (postNames === '') {
						postNames = element.label;
					} else {
						postNames = postNames + '、' + element.label;
					}
				}
			}
		}
		return postNames;
	},
	getRoleNamesByRoleIds: function(roleIds, dict) {
		let names = '';
		for (let index = 0; index < dict.length; index++) {
			const element = dict[index];
			for (let i = 0; i < roleIds.length; i++) {
				if (element.value === roleIds[i]) {
					if (names === '') {
						names = element.label;
					} else {
						names = names + '、' + element.label;
					}
				}
			}
		}
		return names;
	},
	getDeptNameByDeptId: function(deptId, treeData) {
		if (treeData) {
			let result = []; // 记录路径结果
			// 递归方法
			let recursiveFun = (curKey, path, data) => {
				if (data.length === 0) {
					return;
				}
				for (let dept of data) {
					path.push(dept.label);
					if (dept.id === curKey) {
						result = JSON.parse(JSON.stringify(path));
						return;
					}
					const children = Array.isArray(dept.children) ? dept.children : [];
					recursiveFun(curKey, path, children); // 遍历
					path.pop(); // 回溯
				}
			};
			// 通过递归方法获取路径
			recursiveFun(deptId, [], treeData);
			return result.join('/');

			// for (let index = 0; index < treeData.length; index++) {
			//   let dept = treeData[index];
			//   if (dept.id === deptId) {
			//     return dept.label;
			//   }
			//   if (dept.children) {
			//     for (let index = 0; index < dept.children.length; index++) {
			//       let dept2 = dept.children[index];
			//       if (dept2.id === deptId) {
			//         return dept.label + ' / ' + dept2.label;
			//       }
			//       if (dept2.children) {
			//         for (let index2 = 0; index2 < dept2.children.length; index2++) {
			//           let dept3 = dept2.children[index2];
			//           if (dept3.id === deptId) {
			//             return dept.label + ' / ' + dept2.label + ' / ' + dept3.label;
			//           }
			//           if (dept3.children) {
			//             for (let index3 = 0; index3 < dept3.children.length; index3++) {
			//               let dept4 = dept3.children[index3];
			//               if (dept4.id === deptId) {
			//                 return dept.label + ' / ' + dept2.label + ' / ' + dept3.label + ' / ' + dept4.label;
			//               }
			//               if (dept4.children) {
			//                 for (let index4 = 0; index4 < dept4.children.length; index4++) {
			//                   let dept5 = dept4.children[index4];
			//                   if (dept5.id === deptId) {
			//                     return dept.label + ' / ' + dept2.label + ' / ' + dept3.label + ' / ' + dept4.label + ' / ' + dept5.label;
			//                   }
			//                 }
			//               }
			//             }
			//           }
			//         }
			//       }
			//     }
			//   }
			// }
		} else {
			console.log('调用方法getDeptNameByDeptId时，传入的参数不正确！');
		}
	},
	getMenuNameByMenuId: function(menuId, treeData) {
		// console.log('menuId=' + JSON.stringify(menuId));
		// console.log('treeData=' + JSON.stringify(treeData));
		if (treeData) {
			for (let index = 0; index < treeData.length; index++) {
				let menu = treeData[index];
				// console.log('menu.menuId=' + JSON.stringify(menu.menuId));
				// console.log('menu.menuName=' + JSON.stringify(menu.menuName));
				if (menu.menuId === menuId) {
					return menu.menuName;
				}
				// if (menu.children) {
				//   console.log('menu.children=' + JSON.stringify(menu.children));
				//   return AutoeeUtil.getmenuNameBymenuId(menuId, menu.children);
				// }
				if (menu.children) {
					for (let index = 0; index < menu.children.length; index++) {
						let menu2 = menu.children[index];
						if (menu2.menuId === menuId) {
							return menu.menuName + ' / ' + menu2.menuName;
						}
						if (menu2.children) {
							for (let index2 = 0; index2 < menu2.children.length; index2++) {
								let menu3 = menu2.children[index2];
								if (menu3.menuId === menuId) {
									console.log('menu3.label=' + JSON.stringify(menu3.menuName));
								}
							}
						}
					}
				}
			}
		} else {
			console.log('调用方法getMenuNameByMenuId时，传入的参数不正确！');
		}
	},
	getIcon: function(iconName) {
		return AutoeeIcon({
			icon: iconName
		});
	},
	confirmDeleteSelectedData: function(okFun, cancelFun) {
		return Modal.confirm({
			content: '确认删除选中记录？',
			icon: AutoeeUtil.getIcon('ExclamationCircleOutlined'),
			// icon: AutoeeIcon({ icon: 'ExclamationCircleOutlined' }),
			// icon: createVNode(ExclamationCircleOutlined),
			onOk() {
				okFun();
			},
			// cancelText: 'Click to destroy all',
			// onCancel() {
			//   cancelFun();
			// },
		});
	},
	confirmCleanAllData: function(okFun, cancelFun) {
		return Modal.confirm({
			content: '确认清空全部记录？',
			icon: AutoeeUtil.getIcon('ExclamationCircleOutlined'),
			// icon: AutoeeIcon({ icon: 'ExclamationCircleOutlined' }),
			// icon: createVNode(ExclamationCircleOutlined),
			onOk() {
				okFun();
			},
			// cancelText: 'Click to destroy all',
			// onCancel() {
			//   cancelFun();
			// },
		});
	},
	confirmOperate: function(operateName, okFun, cancelFun) {
		return Modal.confirm({
			content: '确认进行【' + operateName + '】操作吗？',
			icon: AutoeeUtil.getIcon('ExclamationCircleOutlined'),
			// icon: AutoeeIcon({ icon: 'ExclamationCircleOutlined' }),
			// icon: createVNode(ExclamationCircleOutlined),
			onOk() {
				okFun();
			},
			// cancelText: 'Click to destroy all',
			// onCancel() {
			//   cancelFun();
			// },
		});
	},
	// 将form中所有字段的值清空
	resetFormFieldsToEmpty: function(objData, arrData) {
		for (let ele in objData) {
			if (objData[ele] instanceof Array) {
				objData[ele] = [];
			} else {
				if (ele === 'params') {
					objData[ele] = {};
				} else {
					objData[ele] = '';
				}
			}
		}
		for (let arrEle in arrData) {
			arrData[arrEle] = [];
		}
		// formData = {};
	},
	getDeptTreeData: function() {
		return treeselect();
	},
	getDictsByType: function(type) {
		return new Promise((resolve, reject) => {
			var dicts = [];
			getDicts(type).then((res) => {
				// console.log('getDictsByType:res.data=' + JSON.stringify(res.data));
				for (let d of res.data) {
					if (d) {
						dicts.push({
							value: d.dictValue,
							label: d.dictLabel
						});
					}
				}
				resolve(dicts);
			});
		});
	},
	// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
	getTableDataByQueryFunc: function(queryFunc, tableObj) {
		tableObj.loading = true;
		queryFunc.then((res) => {
			// console.log("getTableDataByQueryFunc: res=" + JSON.stringify(res))
			// 获取列表记录数
			tableObj.total = res.total;
			// 获取列表数据
			if (res.rows) {
				tableObj.dataSource = res.rows;
			} else if (res.data) {
				tableObj.dataSource = res.data;
			}
			tableObj.loading = false;
		});
	},
	// 转为树形结构：调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
	getTableTreeByQueryFunc: function(queryFunc, tableObj, treeId) {
		tableObj.loading = true;
		queryFunc.then((res) => {
			// console.log("getTableDataByQueryFunc: res=" + JSON.stringify(res))
			// 获取列表记录数
			tableObj.total = res.total;
			// 获取列表数据
			if (res.rows) {
				tableObj.dataSource = handleTree(res.rows, treeId);
			} else if (res.data) {
				tableObj.dataSource = handleTree(res.data, treeId);
			}
			tableObj.loading = false;
		});
	},
	tableHandChangePage: function(tablePage, page, filters, sorter) {
		tablePage.run({
			results: page.pageSize,
			page: page?.current,
			sortField: sorter.field,
			sortOrder: sorter.order,
			...filters,
		});
	},
	// 获取翻页工具栏对象
	createTablePagination: function(tableObj, queryForm) {
		return {
			total: tableObj.total,
			current: queryForm.data.pageNum,
			pageSize: queryForm.data.pageSize,
			showSizeChanger: true,
			showTotal: function(total, range) {
				return `第${range[0]}-${range[1]}条，共${total}条`;
			},
		};
	},
	getTablePagination: function(table) {
		return {
			total: table.total,
			current: table.current.value,
			pageSize: table.pageSize.value,
			showSizeChanger: true,
			showTotal: function(total, range) {
				return `第${range[0]}-${range[1]}条，共${total}条`;
			},
		};
	},
	usePagination: function(queryListFun, tableData) {
		return usePagination(queryListFun, {
			formatResult: (res) => {
				// 获取列表记录数
				tableData.total = res.total;
				// 获取列表数据
				if (res.rows) {
					tableData.dataSource = res.rows;
				} else if (res.data) {
					tableData.dataSource = res.data;
				}
			},
			pagination: {
				currentKey: 'page', // 这个必须为page，否则翻页不好使
				pageSizeKey: 'results', // 这个必须为results，否则切换每页显示多少条不好使
			},
		});
	},
	usePaginationByHandleTree: function(queryListFun, tableData, treeId) {
		return usePagination(queryListFun, {
			formatResult: (res) => {
				// 获取列表记录数
				tableData.total = res.total;
				// 获取列表数据
				if (res.rows) {
					tableData.dataSource = handleTree(res.rows, treeId);
				} else if (res.data) {
					tableData.dataSource = handleTree(res.data, treeId);
				}
			},
			pagination: {
				currentKey: 'page', // 这个必须为page，否则翻页不好使
				pageSizeKey: 'results', // 这个必须为results，否则切换每页显示多少条不好使
			},
		});
	},
	getPostDict: function(postOptions) {
		var dict = [];
		for (let index = 0; index < postOptions.length; index++) {
			let element = postOptions[index];
			if (element.status != 1) {
				dict.push({
					value: element.postId,
					label: element.postName
				});
			}
		}
		return dict;
	},
	getRoleDict: function(roleOptions) {
		var dict = [];
		for (let index = 0; index < roleOptions.length; index++) {
			let element = roleOptions[index];
			if (element.status != 1) {
				dict.push({
					value: element.roleId,
					label: element.roleName
				});
			}
		}
		return dict;
	},
	messageInfo: function(msg) {
		if (msg) {
			message.info(msg);
		} else {
			message.info('请求操作成功。');
		}
	},
	messageSuccess: function(msg) {
		if (msg) {
			message.success(msg);
		} else {
			message.success('请求操作成功。');
		}
	},
	messageWarn: function(msg) {
		if (msg) {
			message.warn(msg);
		} else {
			message.warn('请确认操作是否正确！');
		}
	},
	messageError: function(msg) {
		if (msg) {
			message.error(msg);
		} else {
			message.error('请求操作失败！');
		}
	},
	showTableTotal: function(total, range) {
		return `${range[0]}-${range[1]} of ${total} items`;
	},
	// oss(ossId) {
	//   var url = baseURL + '/system/oss/download/' + ossId
	//   downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
	//   axios({
	//     method: 'get',
	//     url: url,
	//     responseType: 'blob',
	//     headers: { 'Authorization': 'Bearer ' + getToken() }
	//   }).then(async (res) => {
	//     const isLogin = await blobValidate(res.data);
	//     if (isLogin) {
	//       const blob = new Blob([res.data], { type: 'application/octet-stream' })
	//       this.saveAs(blob, decodeURI(res.headers['download-filename']))
	//     } else {
	//       this.printErrMsg(res.data);
	//     }
	//     downloadLoadingInstance.close();
	//   }).catch((r) => {
	//     console.error(r)
	//     Message.error('下载文件出现错误，请联系管理员！')
	//     downloadLoadingInstance.close();
	//   })
	// },
	zip(url, name) {
		var url = baseURL + url;
		axios({
			method: 'get',
			url: url,
			responseType: 'blob',
			headers: {
				Authorization: 'Bearer ' + getToken()
			},
		}).then(async (res) => {
			const isLogin = await blobValidate(res.data);
			if (isLogin) {
				const blob = new Blob([res.data], {
					type: 'application/zip'
				});
				saveAs(blob, name);
			} else {
				printErrMsg(res.data);
			}
		});
	},
	// 对应若依后端处理方式
	download(url, params, fileName) {
		AutoeeUtil.messageInfo('开始下载数据，请稍候...');
		const baseURL =
			import.meta.env.VITE_API_DOMAIN;
		request.post(url, params, {
				transformRequest: [
					(params) => {
						return tansParams(params);
					},
				],
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				responseType: 'blob',
			})
			.then(async (response) => { // 同步请求
				// console.log('response=' + JSON.stringify(response));
				const isLogin = await blobValidate(response);
				if (isLogin) {
					const blob = new Blob([response]);
					saveAs(blob, fileName);
				} else {
					message.error('无效的会话，或者会话已过期，请重新登录。');
				}
			})
			.catch((error) => {
				console.log('err' + error);
				let {
					message
				} = error;
				if (message == 'Network Error') {
					message = '后端接口连接异常';
				} else if (message.includes('timeout')) {
					message = '系统接口请求超时';
				} else if (message.includes('Request failed with status code')) {
					message = '系统接口' + message.substr(message.length - 3) + '异常';
				}
				message.error('失败信息：' + message);
			});
	},
	// download(url, params) {
	// 	AutoeeUtil.messageInfo('开始下载数据，请稍候...');
	// 	const baseURL =
	// 		import.meta.env.VITE_API_DOMAIN;
	// 	request({
	// 			url: url,
	// 			method: 'post',
	// 			params: params,
	// 		})
	// 		.then(async(response) => {  // 同步请求
	// 			console.log('response=' + JSON.stringify(response));
	// 			var name = response.msg;
	// 			console.log('name=' + JSON.stringify(name));
	// 			var url = baseURL + '/common/download?fileName=' + encodeURI(name) + '&delete=true';
	// 			axios({
	// 				method: 'get',
	// 				url: url,
	// 				responseType: 'blob',
	// 				headers: {
	// 					Authorization: 'Bearer ' + getToken()
	// 				},
	// 			}).then(async (res) => {
	// 				const isLogin = await blobValidate(res.data);
	// 				if (isLogin) {
	// 					const blob = new Blob([res.data]);
	// 					saveAs(blob, decodeURI(res.headers['download-filename']));
	// 				} else {
	// 					Message.error('无效的会话，或者会话已过期，请重新登录。');
	// 				}
	// 			});
	// 		})
	// 		.catch(() => {});
	// },
	// saveAs(text, name, opts) {
	//   saveAs(text, name, opts);
	// },
};
export default AutoeeUtil;
