// #ifdef APP-VUE || H5
/**
* v-copyText 复制文本内容
* Copyright (c) 2022 ruoyi
* v-copyText="要复制的文本内容"
* v-copyText:callback="复制成功后的回调函数"
* 点击被标注的元素即可复制文本内容
*/

import type { Directive, DirectiveBinding } from "vue";
interface ElType extends HTMLElement {
  $copyValue: string;
  $copyCallback: Function;
  $destroyCopy:Function;
}
const vCopyText:Directive = {
  beforeMount(el:ElType , binding:DirectiveBinding) {
    if (binding.arg === "callback") {
      el.$copyCallback = binding.value;
    } else {
      el.$copyValue = binding.value;
      const handler = () => {
        copyTextToClipboard(el.$copyValue);
        if (el.$copyCallback) {
          el.$copyCallback(el.$copyValue);
        }
      };
      el.addEventListener("click", handler);
      el.$destroyCopy = () => el.removeEventListener("click", handler);
    }
  }
}
export default vCopyText;

function copyTextToClipboard(input:string, { target = document.body } = {}) {
  const element = document.createElement('textarea');
  const previouslyFocusedElement = document.activeElement  as HTMLElement;

  element.value = input;

  // Prevent keyboard from showing on mobile
  element.setAttribute('readonly', '');

  element.style.contain = 'strict';
  element.style.position = 'absolute';
  element.style.left = '-9999px';
  element.style.fontSize = '12pt'; // Prevent zooming on iOS

  const selection = document.getSelection();
  if(!selection)return
  const originalRange = selection.rangeCount > 0 && selection.getRangeAt(0);

  target.append(element);
  element.select();

  // Explicit selection workaround for iOS
  element.selectionStart = 0;
  element.selectionEnd = input.length;

  let isSuccess = false;
  try {
    isSuccess = document.execCommand('copy');
  } catch { }

  element.remove();

  if (originalRange) {
    selection.removeAllRanges();
    selection.addRange(originalRange);
  }

  // Get the focus back on the previously focused element, if any
  if (previouslyFocusedElement) {
    previouslyFocusedElement.focus();
  }

  return isSuccess;
}
// #endif