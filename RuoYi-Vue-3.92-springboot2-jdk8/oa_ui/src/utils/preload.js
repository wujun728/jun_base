function All() {}
All.prototype = {
  timer: '',
  debounce(fn, delay = 500) {
    var _this = this;
    return function (arg) {
      // 获取函数的作用域和变量
      var that = this;
      var args = arg;
      clearTimeout(_this.timer); // 清除定时器
      _this.timer = setTimeout(function () {
        fn.call(that, args);
      }, delay);
    };
  },
  setCookie(val) {
    // cookie设置[{key:value}]、获取key、清除['key1','key2']
    for (var i = 0, len = val.length; i < len; i++) {
      for (var key in val[i]) {
        document.cookie = key + '=' + encodeURIComponent(val[i][key]) + '; path=/';
      }
    }
  },
  getCookie(name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split('; ');
    for (var i = 0, len = arrCookie.length; i < len; i++) {
      var arr = arrCookie[i].split('=');
      if (name == arr[0]) {
        return decodeURIComponent(arr[1]);
      }
    }
  },
  clearCookie(name) {
    var myDate = new Date();
    myDate.setTime(-1000); // 设置时间
    for (var i = 0, len = name.length; i < len; i++) {
      document.cookie = '' + name[i] + "=''; path=/; expires=" + myDate.toGMTString();
    }
  },
  arrToStr(arr) {
    if (arr) {
      return arr
        .map((item) => {
          return item.card?item.card.name:item.name;
        })
        .toString();
    }
  },
  toggleClass(arr, elem, key = 'id') {
    return arr.some((item) => {
      return item[key] == elem[key];
    });
  },
  toChecked(arr, elem, key = 'id') {
    var isIncludes = this.toggleClass(arr, elem, key);
    !isIncludes ? arr.push(elem) : this.removeEle(arr, elem, key);
  },
  removeEle(arr, elem, key = 'id') {
    var includesIndex;
    arr.map((item, index) => {
      if (item[key] == elem[key]) {
        includesIndex = index;
      }
    });
    arr.splice(includesIndex, 1);
  },
  setApproverStr(nodeConfig) {
    if (nodeConfig.settype == 1) {
      if (nodeConfig.nodeUserList.length == 1) {
       let name =  nodeConfig.nodeUserList[0].card?nodeConfig.nodeUserList[0].card.name:nodeConfig.nodeUserList[0].name
        return '指定成员审批: ' +name;
      } else if (nodeConfig.nodeUserList.length > 1) {
        if (nodeConfig.examineMode == 3) {
          return '指定成员依次审批: ' + this.arrToStr(nodeConfig.nodeUserList);
        } else if (nodeConfig.examineMode == 2) {
          return '指定成员会签: ' + this.arrToStr(nodeConfig.nodeUserList);
        } else if (nodeConfig.examineMode == 1) {
          return '指定成员或签: ' + this.arrToStr(nodeConfig.nodeUserList);
        }
      }
    } else if (nodeConfig.settype == 2) {
      const level = nodeConfig.directorLevel == 1 ? '直属上级' : '第' + nodeConfig.directorLevel + '级上级';
      return '指定上级审批: ' + level;
    } else if (nodeConfig.settype == 4) {
      var str = '申请人自选';
      str += nodeConfig.selectRange == 1 ? ' 不限范围' : ' 指定范围';
      if (nodeConfig.selectMode == 1) {
        str += ' 单选';
      } else {
        str += ' 多选';
        if (nodeConfig.examineMode == 1) {
          str += ' 或签';
        } else if (nodeConfig.examineMode == 2) {
          str += ' 会签';
        } else if (nodeConfig.examineMode == 3) {
          str += ' 依次审批';
        }
      }
      return str;
    } else if (nodeConfig.settype == 5) {
      return '申请人本人';
    } else if (nodeConfig.settype == 7) {
      const str = nodeConfig.directorLevel == 1 ? '直属上级' : '第' + nodeConfig.directorLevel + '级上级';
      return this.conditionOrder(nodeConfig.directorOrder) + ' 审批到 ' + str;
    }
  },
  dealStr(str, obj) {
    var arr = [];
    var list = str.split(',');
    for (var elem in obj) {
      list.map((item) => {
        if (item == elem) {
          arr.push(obj[elem].value);
        }
      });
    }
    return arr.join('或');
  },
  conditionStr(nodeConfig, index,type) {
  
    var { conditionList, isDefault } = nodeConfig.conditionNodes[index];
    var str = '';
    if (isDefault) {
      return '其他条件进入此流程';
    } else {
      if (conditionList.length == 0) {
        return index == nodeConfig.conditionNodes.length - 1 && nodeConfig.conditionNodes[0].conditionList.length != 0
          ? '其他条件进入此流程'
          : '请设置条件';
      } else  {
      
        if(type=='') {
          for (let i = 0; i < conditionList.length; i++) {
            const { type, option, value, title, options, category, min, max } = conditionList[i];
            if (type === 'inputNumber' || type === 'timeFrom' || type === 'moneyFrom') {
              const str1 = value == 4 ? min + '与' + max : option;
              str += ' 并且 ' + title + ' ' + this.conditionNumber(value) + ' ' + str1;
            } else if (type === 'radio') {
              str += ' 并且 ' + title + ' 属于 ' + this.conditionRadio(options, option);
            } else if (type === 'checkbox') {
              str +=
                ' 并且 ' + title + ' ' + this.conditionCheckbox(value) + this.conditionSelect(options, option, value);
            } else if (type === 'select') {
              str +=
                ' 并且 ' + title + ' ' + this.conditionCheckbox(value) + this.conditionSelect(options, option, value);
            } else if (type === 'departmentTree' && category == 1) {
              str += ' 并且 ' + title + ' ' + this.conditionDepartment(options);
            } else if (type === 'departmentTree' && category != 1) {
              str += ' 并且 ' + title + ' ' + this.departmentSelect(value) + this.conditionDepartment(options, value);
            }
          } 
        }else {
          str = '1234已设置条件'+'('+conditionList.length+')'
        }
        
      } 
      return str ? str.substr(4) : '请设置条件';
    }
  },
  conditionFieldsStr(nodeConfig, index) {
    const { conditionList } = nodeConfig.conditionNodes[index];
    let str = '';
    if (conditionList.length > 0) {
      conditionList.map((value) => {
        if (value.field) {
          str += value.field + ',';
        }
      });
    }
    return str ? str.substr(0, str.length - 1) : '';
  },
  conditionNumber(value) {
    let str = '';
    value = Number(value);
    if (value === 0) {
      str = '小于';
    } else if (value === 1) {
      str = '等于';
    } else if (value === 2) {
      str = '小于等于';
    } else if (value === 3) {
      str = '大于等于';
    } else if (value === 4) {
      str = '介于（两个数字之间）';
    }
    return str;
  },
  conditionRadio(options, val) {
    let str = '';
    if (options.length > 0) {
      options.forEach((value) => {
        if (value.value == val) {
          str += ' ' + value.label;
        }
      });
    }
    return str;
  },
  conditionDepartment(options, val = '1') {
  
    let str = '';
    if (options.depList&&options.depList.length > 0) {
      options.depList.forEach((value) => {
        str += val == 0 ? ' 且 ' + value.name : ' 或 ' + value.name;
      });
    }
    if (options.userList&&options.userList.length > 0) {
    
      options.userList.forEach((value) => {
        str += val == 0 ? ' 且 ' + value.name : ' 或 ' 
        + value.name;
      });
    }
    return str ? str.substr(2) : '';
  },
  departmentSelect(value) {
    let str = '';
    value = Number(value);
    if (value === 0) {
      str = '完全属于';
    } else if (value === 1) {
      str = '其一属于';
    }
    return str;
  },
  conditionSelect(options, option, val) {
    var str = '';
    if (option.length > 0) {
      options.forEach((item) => {
        option.forEach((value) => {
          if (value == item.value) {
            str += val == 0 ? ' 且 ' + item.label : ' 或 ' + item.label;
          }
        });
      });
    }
    return str ? str.substr(2) : '';
  },
  conditionCheckbox(value) {
    let str = '';
    value = Number(value);
    if (value === 0) {
      str = '完全等于';
    } else if (value === 1) {
      str = '包含任意';
    }
    return str;
  },
  conditionOrder(value) {
    let str = '';
    value = Number(value);
    if (value === 0) {
      str = '从上至下';
    } else if (value === 1) {
      str = '从下至上';
    }
    return str;
  },
  copyerStr(nodeConfig) {
    let str = '';
    if (nodeConfig.ccSelfSelectFlag == 1) {
      str += ' , 申请人自选';
    }
    if (nodeConfig.nodeUserList.length !== 0) {
      str += ' , 指定成员 ' + this.arrToStr(nodeConfig.nodeUserList);
    }
    if (nodeConfig.departmentHead && nodeConfig.departmentHead.length > 0) {
      let str1 = '';
      nodeConfig.departmentHead.forEach((value) => {
        str1 += value == 1 ? '直接上级, ' : '第' + value + '级' + '上级, ';
      });
      str1 = str1 ? str1.substr(0, str1.length - 2) : '';
      str += ' , 指定上级  ' + str1;
    }
    return str ? str.substr(3) : '';
  },
  onlyValue() {
    return 'xxxxxxx4xxxyxxxxx'.replace(/[xy]/g, function (c) {
      const r = (Math.random() * 16) | 0;
      const v = c == 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  },
  toggleStrClass(item, key) {
    let a = item.zdy1 ? item.zdy1.split(',') : [];
    return a.some((item) => {
      return item == key;
    });
  },
  getExamineStatus(id,data) {
    var str = '';
    if (id === 0) {
      str = '审核中';
    } else if (id === 1&&!data.recall) {
      str = '已通过';
    } else if (id === 2) {
      str = '已拒绝';
    } else if (id === -1) {
      str = '已撤销';
    } else if(id === 1&&data.recall){
      str = '撤销中'
    }
    return str;
  },
};

export default new All();
