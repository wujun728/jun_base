import { ref, type ComponentInternalInstance, type Ref } from "vue";
import { verifyApi, getShortKeyApi, articleCaptchaApi } from "@/api/public";
import message from "@/utils/message";
import type { Res } from "@/utils/typeHelper";

const disabled: Ref<boolean> = ref(false);
const text: Ref<string> = ref("获取验证码");

// 发送验证码
const sendCode = () => {
  if (disabled.value) return;
  disabled.value = true;
  let n = 60;
  text.value = "剩余 " + n + "s";

  const run = setInterval(() => {
    n = n - 1;
    if (n < 0) {
      clearInterval(run);
    }
    text.value = "剩余 " + n + "s";
    if (text.value < "剩余 " + 0 + "s") {
      disabled.value = false;
      text.value = "重新获取";
    }
  }, 1000);
};
// 发送验证码逻辑
export function useSendCode() {
  return { text, disabled, sendCode };
}

// 获取短信验证码与key
export function useCmsKeyVerify() {
  // 接受手机号码
  const getKeyVerify = (phone: string, flag: number = 0) => {
    getShortKeyApi().then(async (res: Res) => {
      const cmsKey = res.data.key;
      const data = {
        phone: phone,
        key: cmsKey,
        types: 0,
        from: 1
      };
      if (flag === 1) {
        // 知识社区验证码
        await articleCaptchaApi(phone)
          .then((res: Res) => {
            sendCode();
            message.success(res.message, "none");
          })
          .catch((error: Res) => {
            message.error(error.message);
          });
      } else {
        await verifyApi(data)
          .then((res: Res) => {
            sendCode();
            message.success(res.message, "none");
          })
          .catch((error: Res) => {
            message.error(error.message);
          });
      }
    });
  };

  return { getKeyVerify };
}

/**
 * 获取元素信息
 */
export function useBarHeight() {
  const height = ref(0);
  const getBarHeight = (ele: string, instance: ComponentInternalInstance, type = true) => {
    const query = uni.createSelectorQuery().in(instance);
    query.select(ele).fields({ size: true, rect: true }, () => { });
    query.exec((data) => {
      height.value = type ? data[0].height + "px" : data[0].height;
    });
  };
  return { height, getBarHeight };
}
