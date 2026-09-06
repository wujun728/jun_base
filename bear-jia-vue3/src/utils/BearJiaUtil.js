import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
import axios from 'axios';
import { saveAs } from 'file-saver';
import { getToken } from '@/utils/auth';
import { message, Modal } from 'ant-design-vue';
import { getDicts as getDicts } from '@/api/system/dict/data';
import { treeselect } from '@/api/system/dept';
import { usePagination } from 'vue-request';
import { blobValidate, handleTree, parseTime, tansParams } from '@/utils/bearjia.js';
import request from '@/utils/request';

const baseURL = import.meta.env.VITE_APP_BASE_API || import.meta.env.VITE_API_DOMAIN || '';

const BearJiaUtil = {
  parseDateTime: function (dateTime, pattern) {
    return parseTime(dateTime, pattern);
  },
  getDictLabelByKey: function (key, dict) {
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
  getPostNamesByPostIds: function (postIds, dict) {
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
  getRoleNamesByRoleIds: function (roleIds, dict) {
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
  getDeptNameByDeptId: function (deptId, treeData) {
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
    } else {
      console.log('调用方法getDeptNameByDeptId时，传入的参数不正确！');
    }
  },
  getMenuNameByMenuId: function (menuId, treeData) {
    if (treeData) {
      for (let index = 0; index < treeData.length; index++) {
        let menu = treeData[index];
        if (menu.menuId === menuId) {
          return menu.menuName;
        }
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
  getIcon: function (iconName) {
    return BearJiaIcon({
      icon: iconName,
    });
  },
  confirmDeleteSelectedData: function (okFun, _cancelFun) {
    return Modal.confirm({
      content: '确认删除选中记录？',
      icon: BearJiaUtil.getIcon('ExclamationCircleOutlined'),
      okText: '确认',
      cancelText: '取消',
      onOk() {
        okFun();
      },
    });
  },
  confirmCleanAllData: function (okFun, _cancelFun) {
    return Modal.confirm({
      content: '确认清空全部记录？',
      icon: BearJiaUtil.getIcon('ExclamationCircleOutlined'),
      okText: '确认',
      cancelText: '取消',
      onOk() {
        okFun();
      },
    });
  },
  confirmOperate: function (operateName, okFun, _cancelFun) {
    return Modal.confirm({
      content: '确认进行【' + operateName + '】操作吗？',
      icon: BearJiaUtil.getIcon('ExclamationCircleOutlined'),
      okText: '确认',
      cancelText: '取消',
      onOk() {
        okFun();
      },
    });
  },
  // 将form中所有字段的值清空
  resetFormFieldsToEmpty: function (objData, arrData) {
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
  },
  getDeptTreeData: function () {
    return treeselect();
  },
  getDictsByType: function (type) {
    return new Promise((resolve, _reject) => {
      var dicts = [];
      getDicts(type).then((res) => {
        for (let d of res.data) {
          if (d) {
            dicts.push({
              value: d.dictValue,
              label: d.dictLabel,
            });
          }
        }
        resolve(dicts);
      });
    });
  },
  // 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
  getTableDataByQueryFunc: function (queryFunc, tableObj) {
    tableObj.loading = true;
    queryFunc.then((res) => {
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
  getTableTreeByQueryFunc: function (queryFunc, tableObj, treeId) {
    tableObj.loading = true;
    queryFunc.then((res) => {
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
  tableHandChangePage: function (tablePage, page, filters, sorter) {
    tablePage.run({
      results: page.pageSize,
      page: page?.current,
      sortField: sorter.field,
      sortOrder: sorter.order,
      ...filters,
    });
  },
  // 获取翻页工具栏对象
  createTablePagination: function (tableObj, queryForm) {
    return {
      total: tableObj.total,
      current: queryForm.data.pageNum,
      pageSize: queryForm.data.pageSize,
      showSizeChanger: true,
      showTotal: function (total, range) {
        return `第${range[0]}-${range[1]}条，共${total}条`;
      },
    };
  },
  getTablePagination: function (table) {
    return {
      total: table.total,
      current: table.current.value,
      pageSize: table.pageSize.value,
      showSizeChanger: true,
      showTotal: function (total, range) {
        return `第${range[0]}-${range[1]}条，共${total}条`;
      },
    };
  },
  usePagination: function (queryListFun, tableData) {
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
  usePaginationByHandleTree: function (queryListFun, tableData, treeId) {
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
  getPostDict: function (postOptions) {
    var dict = [];
    for (let index = 0; index < postOptions.length; index++) {
      let element = postOptions[index];
      if (element.status != 1) {
        dict.push({
          value: element.postId,
          label: element.postName,
        });
      }
    }
    return dict;
  },
  getRoleDict: function (roleOptions) {
    var dict = [];
    for (let index = 0; index < roleOptions.length; index++) {
      let element = roleOptions[index];
      if (element.status != 1) {
        dict.push({
          value: element.roleId,
          label: element.roleName,
        });
      }
    }
    return dict;
  },
  messageInfo: function (msg) {
    if (msg) {
      message.info(msg);
    } else {
      message.info('请求操作成功。');
    }
  },
  messageSuccess: function (msg) {
    if (msg) {
      message.success(msg);
    } else {
      message.success('请求操作成功。');
    }
  },
  messageWarn: function (msg) {
    if (msg) {
      message.warn(msg);
    } else {
      message.warn('请确认操作是否正确！');
    }
  },
  messageError: function (msg) {
    if (msg) {
      message.error(msg);
    } else {
      message.error('请求操作失败！');
    }
  },
  showTableTotal: function (total, range) {
    return `${range[0]}-${range[1]} of ${total} items`;
  },
  zip(url, name) {
    const requestUrl = baseURL + url;
    axios({
      method: 'get',
      url: requestUrl,
      responseType: 'blob',
      headers: {
        Authorization: 'Bearer ' + getToken(),
      },
    }).then(async (res) => {
      const isLogin = await blobValidate(res.data);
      if (isLogin) {
        const blob = new Blob([res.data], {
          type: 'application/zip',
        });
        saveAs(blob, name);
      } else {
        BearJiaUtil.messageError('请求操作失败！');
      }
    });
  },
  // 对应后端处理方式
  download(url, params, fileName) {
    BearJiaUtil.messageInfo('开始下载数据，请稍候...');
    request
      .post(url, params, {
        transformRequest: [
          (params) => {
            return tansParams(params);
          },
        ],
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        responseType: 'blob',
      })
      .then(async (response) => {
        const isLogin = await blobValidate(response);
        if (isLogin) {
          const blob = new Blob([response], {
            type: 'application/octet-stream',
          });
          saveAs(blob, fileName);
        } else {
          BearJiaUtil.messageError('下载文件出现错误，请联系管理员！');
        }
      })
      .catch((r) => {
        console.error(r);
        BearJiaUtil.messageError('下载文件出现错误，请联系管理员！');
      });
  },
  // 构造树型结构数据
  handleTree: function (data, id, parentId, children) {
    return handleTree(data, id, parentId, children);
  },

  /**
   * 清理空值，对象
   * @param children
   * @returns {*[]}
   */
  filterEmpty: function (children = []) {
    return children.filter((c) => c.tag || (c.text && c.text.trim() !== ''));
  },

  /**
   * 获取字符串长度，英文字符 长度1，中文字符长度2
   * @param {*} str
   */
  getStrFullLength: function (str = '') {
    return str.split('').reduce((pre, cur) => {
      const charCode = cur.charCodeAt(0);
      if (charCode >= 0 && charCode <= 128) {
        return pre + 1;
      }
      return pre + 2;
    }, 0);
  },

  /**
   * 截取字符串，根据 maxLength 截取后返回
   * @param {*} str
   * @param {*} maxLength
   */
  cutStrByFullLength: function (str = '', maxLength) {
    let showLength = 0;
    return str.split('').reduce((pre, cur) => {
      const charCode = cur.charCodeAt(0);
      if (charCode >= 0 && charCode <= 128) {
        showLength += 1;
      } else {
        showLength += 2;
      }
      if (showLength <= maxLength) {
        return pre + cur;
      }
      return pre;
    }, '');
  },
};

export default BearJiaUtil;
