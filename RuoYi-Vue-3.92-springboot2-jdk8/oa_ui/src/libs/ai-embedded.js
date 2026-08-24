import store from '@/store'
import { getAiConfigApi } from '@/api/public'
import defaultAIIcon from '@/assets/images/ai-assistant-icon.jpg'

const isDev = process.env.NODE_ENV === "development";

// 开发环境下修改 http://localhost:19527 为 AI 项目本地对应地址即可，线上环境无需修改
// const baseUrl = isDev ? "http://localhost:19527" : location.origin;
const baseUrl = isDev ? "http://dev.oa.crmeb.net" : location.origin;

// 异步加载 AI 相关脚本
const loadAiPlugin = () => {
  return new Promise((resolve, reject) => {
    if (window.ChatIframe) {
      resolve();
      return;
    }
    const scriptSrc = "/chat/entry/index.js";
    const script = document.createElement("script");
    script.src = new URL(scriptSrc, baseUrl).href;
    script.async = true;
    document.head.appendChild(script);
    script.onload = () => {
      resolve();
    }
    script.onerror = () => {
      reject();
    }
  });
}


export class AiEmbeddedManage {
  static children = [];
  static libLoadTask = loadAiPlugin();
  static __instance = null;

  _inited = false;
  _destoryed = false;

  // 会话窗口实例
  instance = null;

  constructor() {
    AiEmbeddedManage.children.push(this);
  }

  /**
 * 初始化 AI 插件
 * @param {string} token 
 * @param {boolean} defaultShow 是否默认显示
 * @param {string} appId 应用 ID
 * @param {boolean} screenState 屏幕状态
 */
  async init(token, {
    appId = null,
    defaultShow = true,
    scene
  } = {}) {
    if (this._inited) {
      return;
    }
    this._inited = true;
    try {
      await this.libLoadTask;
      const { data } = await getAiConfigApi();

      // iframe 中要加载的 URL 地址，相对于 baseUrl 的路径
      const iframePath = appId !== null ? `/chat/app/${appId}` : "/chat";

      const url = new URL(iframePath, baseUrl);
      
      const iconHref = data.ai_image || defaultAIIcon;
      this.instance = new ChatIframe({
        url: url.href,
        query: {
          scene,
          token
        },
        floatIcon: iconHref,
        zIndex: 1000,
        defaultShow
      });
    } catch (error) {
      console.error("AI 插件加载失败", error);
    }
  }

  show() {
    this.instance?.show();
  }

  hide() {
    this.instance?.hide();
  }

  /**
 * 销毁 AI 插件实例
 */
  destroy() {
    this._destoryed = true;
    if (this.instance) {
      this.instance?.destroy();
      this.instance = null;
    }
    AiEmbeddedManage.children = AiEmbeddedManage.children.filter(child => child !== this);
  }

  /**
   * 销毁所有 AI 插件实例
   */
  static destroyAll() {
    AiEmbeddedManage.children.forEach(child => {
      child.destroy();
    });
  }

  /**
 * 更新应用预览状态
 * @param {HTMLIFrameElement} iframeRef 
 * @param {object} data 
 */
  static updateAiAppPreviewState(iframeRef, data) {
    iframeRef.contentWindow.postMessage({
      source: "ai-chat-parent",
      action: "update-app-preview-state",
      data
    }, "*");
  }

  /**
   * 获取应用预览 iframe 地址
   * @param {string} appId 
   * @returns {string} 预览 iframe 地址
   */
  static getPreviewIframeUrl(appId) {
    const url = new URL(`/chat/app/${appId}`, baseUrl);
    url.searchParams.set("app-preview", "1");
    url.searchParams.set("not-save-chat", "1");
    url.searchParams.set("token", store.getters.token);
    return url.href;
  }

  /**
   * 获取 AI 插件实例
   * @param {boolean} force 是否强制创建新的实例
   * @returns {AiEmbeddedManage} AI 插件实例
   */
  static getAiEmbedded(force = false) {
    if (force) {
      if (AiEmbeddedManage.__instance) {
        AiEmbeddedManage.__instance.destroy();
      }
      AiEmbeddedManage.__instance = new AiEmbeddedManage();
    }
    if (!AiEmbeddedManage.__instance || AiEmbeddedManage.__instance._destoryed) {
      AiEmbeddedManage.__instance = new AiEmbeddedManage();
    }
    return AiEmbeddedManage.__instance;
  }
};