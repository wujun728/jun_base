import { EDITOR_BLUR, EDITOR_FOCUS } from "../constant";
import { getSystemInfo } from "@/utils/helper";
import { throttle } from "../utils/helper";

export type KBHeightChangeCallBack = (result: UniNamespace.OnKeyboardHeightChangeResult) => void;

export const useOnKeyboardHeightChange = (cb: KBHeightChangeCallBack) => {

  onMounted(() => {
    uni.onKeyboardHeightChange(cb);
  });

  onUnmounted(() => {
    uni.offKeyboardHeightChange(cb);
  });
}

type EventHandler<T = any> = (result: T) => void;

export const useEvent = (event: string, callback: EventHandler) => {
  onMounted(() => {
    uni.$on(event, callback);
  })

  onUnmounted(() => {
    uni.$off(event, callback);
  });
}

export const useInputFocus = (callback?: EventHandler<boolean>) => {
  const isFocus = ref(false);

  const handleFocus = () => {
    isFocus.value = true;
    callback && callback(isFocus.value);
  }

  const handleBlur = () => {
    isFocus.value = false;
    callback && callback(isFocus.value);
  }

  return [
    isFocus,
    handleFocus,
    handleBlur
  ];
}



export const useEditorFocus = (callback?: EventHandler) => {
  const editorIsFoucs = ref(false);

  useEvent(EDITOR_FOCUS, () => {
    editorIsFoucs.value = true;
    callback && callback(editorIsFoucs.value);
  });

  useEvent(EDITOR_BLUR, () => {
    editorIsFoucs.value = false;
    callback && callback(editorIsFoucs.value);
  });

  return [editorIsFoucs];
}

/**
 * 
 * Android 平台在 meta 标签中添加 interactive-widget=resizes-content 之后可以使软键盘唤起后
 * 布局窗口发生变化，fixed 元素会跟着键盘浮动。
 * 
 * iOS 不支持此标签，只能通过监听视觉窗口大小的变化和滚动位置的变化来判断软键盘相对屏幕的位置
 */
export const useSoftKeyboardOffsetY = () => {

  // 软键盘高度
  const softKeyboardHeight = ref(0);

  // 视觉窗口滚动高度
  const visualViewportOffsetTop = ref(0);

  const softKeyboardOffsetY = computed(() => {
    return softKeyboardHeight.value - visualViewportOffsetTop.value;
  });

  const { platform, safeAreaInsets } = getSystemInfo();
  const isIOS = platform === "ios";

  // #ifdef APP
  const handleKeyboardHeightChange = (result: UniNamespace.OnKeyboardHeightChangeResult) => {
    softKeyboardHeight.value = result.height ? result.height - safeAreaInsets.top / 2 : result.height;
  }

  isIOS && onPageScroll(options => {
    visualViewportOffsetTop.value = options.scrollTop;
  });
  
  onMounted(() => {
    isIOS && uni.onKeyboardHeightChange(handleKeyboardHeightChange);
  });

  onUnmounted(() => {
    isIOS && uni.offKeyboardHeightChange(handleKeyboardHeightChange);
  });
  // #endif


  // #ifdef H5

  const handleResize = (e: UIEvent) => {
    softKeyboardHeight.value = window.innerHeight - window.visualViewport.height;
  }

  const asyncHandleFocus = (e: UIEvent) => {
    setTimeout(() => {
      handleResize(e);
    }, 100);
  }

  const asyncHandleBlur = (e: UIEvent) => {
    setTimeout(() => {
      softKeyboardHeight.value = 0;
    }, 100);
  }

  const handleVisualViewportScroll = throttle((e: Event) => {
    visualViewportOffsetTop.value = window.visualViewport.offsetTop;
  }, 50);

  onMounted(() => {

    if (isIOS) {
      // 监听视觉窗口 resize 存在延迟，用编辑器的 foucs 事件代替
      // window.visualViewport.addEventListener("resize", handleResize);
      uni.$on(EDITOR_FOCUS, asyncHandleFocus);
      uni.$on(EDITOR_BLUR, asyncHandleBlur);

      window.visualViewport.addEventListener("scroll", handleVisualViewportScroll);
    }
  });

  onUnmounted(() => {
    if (isIOS) {
      // window.visualViewport.removeEventListener("resize", handleResize);
      uni.$off(EDITOR_FOCUS, asyncHandleFocus);
      uni.$off(EDITOR_BLUR, asyncHandleBlur);

      window.visualViewport.removeEventListener("scroll", handleVisualViewportScroll);
    }
  })

  // #endif

  return [softKeyboardOffsetY];
}