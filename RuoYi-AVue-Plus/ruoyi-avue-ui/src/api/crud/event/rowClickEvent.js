/**
 * 通用的行事件定义，通过后端的参数来确定那个事件被触发。这里的方法参数都是固定的，请最好不要更改，而只是逻辑自行处理。
 *
 * @author liukx
 * @date  -
 */

import crudUtil from "@/utils/server-crud";
import _remote from "@/api/crud/remoteApi";

/**
 * 通用的测试按钮A
 * @param self    全局指向
 * @param item    按钮对象
 * @param row     选择行对象
 * @param index   下标对象
 */
export function testA(self, item, row, index) {
  self.$message.success('testA --> ' + JSON.stringify(item) + ' 值 ' + JSON.stringify(row));
}

/**
 * 通用的测试按钮方法B
 * @param self    全局指向
 * @param item    按钮对象
 * @param row     选择行对象
 * @param index   下标对象
 */
export function testB(self, item, row, index) {
  self.$message.success('testB --> ' + JSON.stringify(item) + ' 值' + JSON.stringify(row));
}


/**
 * 点击跳转到特定页面
 * @param self
 * @param item
 * @param row
 * @param index
 */
export function hrefClick(self, item, row, index) {
  debugger;
  let json = listToJson(item['attrExt']);
  if (json) {
    window.open(json.url);
  } else {
    self.message.error("没有配置跳转链接，无法跳转！")
  }
}

/**
 * 确认框, 询问之后,触发远程调用,传递的参数是当前行数据
 * @param self
 * @param item
 * @param row
 * @param index
 */
export function confirmClickRemoteApi(self, item, row, index) {
  let param = listToJson(item['attrExt']);
  let title = param.title || '您确认执行该操作吗?';
  let url = param.url;
  let method = param.method || 'post';

  if (!url) {
    self.$message.error("该操作会触发远程调用,请回填url参数!");
    return;
  } else {
    url = crudUtil.completionDomain(self, url);
  }

  // 触发远程调用操作
  self.$confirm(title, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    _remote.api(url, method, row, (data) => {
      if (crudUtil.processDMLResponse(self, data)) {
        // 刷新表格
        self.$refs.crud.refreshChange();
      }
    });
  })
}


/**
 * 打开一个弹层，输入json参数给后端
 * @param self
 * @param item
 * @param row
 * @param index
 */
export function openWindowJsonRemote(self, item, row, index) {
  // 显示弹窗窗口
  let param = listToJson(item['attrExt']);
  let submitUrl = param.submitUrl;
  let requestUrl = crudUtil.completionDomain(self, param.url || '/avue/crud');
  // 先清空对象
  self.dialogConfig.objectData = {};

  let group = param.group;
  let acceptToken = self.config.acceptToken;
  let requestBody = {
    "group": group,
    "acceptToken": acceptToken
  }
  _remote.post(requestUrl, requestBody, (res) => {
    // 定义后端的请求路径
    // self.dialogConfig.config = res.config;
    self.dialogConfig.config = {};
    self.dialogConfig.config["submitUrl"] = submitUrl;
    self.dialogConfig.formOption = res.option;
    self.dialogConfig.dialogOption = {
      title: res.option.title || '弹窗操作'
    };
    self.dialogConfig.objectData = row;
    self.dialogConfig.showDialogProps = true;
  });
}

/**
 * 将List转化为json对象
 * @param valueList
 */
function listToJson(valueList) {
  if (valueList && Array.isArray(valueList)) {
    let result = {};
    for (let i = 0; i < valueList.length; i++) {
      let name = valueList[i].name;
      result[name] = valueList[i].value;
    }
    return result;
  }
  return valueList;

}
