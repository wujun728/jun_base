/**
 * 通用js方法封装处理
 * Copyright (c) 2019 aidex
 */

export function replaceAll (text,stringToFind,stringToReplace) {
	   if ( stringToFind == stringToReplace) return this;
	          var temp = text;
	          var index = temp.indexOf(stringToFind);
	          while (index != -1) {
	              temp = temp.replace(stringToFind, stringToReplace);
	              index = temp.indexOf(stringToFind);
	          }
	return temp;
}
