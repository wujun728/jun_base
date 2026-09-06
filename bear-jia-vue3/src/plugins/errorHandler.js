// 全局错误处理插件
import { message, notification } from 'ant-design-vue';
import { h } from 'vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

// 错误类型枚举
const ErrorType = {
  NETWORK: 'network',
  API: 'api',
  RUNTIME: 'runtime',
  VALIDATION: 'validation',
};

// 错误处理配置
const errorConfig = {
  // 是否在控制台输出错误
  logError: true,
  // 是否向用户显示错误
  showError: true,
  // 是否收集错误信息（可用于后续接入错误监控系统）
  collectError: false,
};

/**
 * 处理网络错误
 * @param {Error} error 错误对象
 */
function handleNetworkError(error) {
  if (!navigator.onLine) {
    message.error('网络连接已断开，请检查您的网络');
    return;
  }

  const statusText = error.response?.statusText || '未知网络错误';
  const status = error.response?.status;

  if (status === 404) {
    notification.error({
      message: '请求的资源不存在',
      description: `${error.config?.url || ''}`,
      duration: 3,
    });
  } else if (status >= 500) {
    notification.error({
      message: '服务器错误',
      description: `服务器暂时无法响应请求，请稍后再试 (${status}: ${statusText})`,
      duration: 4,
    });
  } else {
    notification.error({
      message: '网络请求失败',
      description: statusText,
      duration: 3,
    });
  }
}

/**
 * 处理API错误（业务逻辑错误）
 * @param {Object} response API响应对象
 */
function handleApiError(response) {
  const { code, msg } = response;

  // 根据不同的错误码显示不同的提示
  if (code === 401) {
    notification.warning({
      message: '登录已过期',
      description: '您的登录状态已失效，请重新登录',
      duration: 3,
    });
    // 可以在这里添加重定向到登录页的逻辑
  } else if (code === 403) {
    notification.warning({
      message: '权限不足',
      description: '您没有权限执行此操作',
      duration: 3,
    });
  } else {
    message.error(msg || '操作失败');
  }
}

/**
 * 处理运行时错误
 * @param {Error} error 错误对象
 * @param {Object} vm Vue实例
 * @param {String} info 错误信息
 */
function handleRuntimeError(error, vm, info) {
  console.error('运行时错误:', error);
  console.error('错误组件:', vm);
  console.error('错误信息:', info);

  notification.error({
    message: '应用发生错误',
    description: '应用运行过程中发生错误，请刷新页面或联系管理员',
    duration: 4,
    icon: () => h(ExclamationCircleOutlined, { style: 'color: #ff4d4f' }),
  });

  // 如果配置了错误收集，可以在这里发送错误信息到服务器
  if (errorConfig.collectError) {
    // 实现错误收集逻辑
  }
}

/**
 * 处理表单验证错误
 * @param {Object} errors 验证错误对象
 */
function handleValidationError(errors) {
  const errorMsg = Object.values(errors)
    .map((err) => err.message || err)
    .filter(Boolean)
    .join('、');

  message.error(errorMsg || '表单验证失败，请检查输入');
}

/**
 * 全局错误处理函数
 * @param {Error|Object} error 错误对象
 * @param {String} type 错误类型
 * @param {Object} context 上下文信息
 */
function errorHandler(error, type = ErrorType.RUNTIME, context = {}) {
  // 如果配置了在控制台输出错误
  if (errorConfig.logError) {
    console.group('应用错误');
    console.error('错误类型:', type);
    console.error('错误详情:', error);
    if (Object.keys(context).length > 0) {
      console.error('上下文信息:', context);
    }
    console.groupEnd();
  }

  // 如果配置了向用户显示错误
  if (errorConfig.showError) {
    switch (type) {
      case ErrorType.NETWORK:
        handleNetworkError(error);
        break;
      case ErrorType.API:
        handleApiError(error);
        break;
      case ErrorType.VALIDATION:
        handleValidationError(error);
        break;
      case ErrorType.RUNTIME:
      default:
        handleRuntimeError(error, context.vm, context.info);
        break;
    }
  }
}

// 导出错误处理器和类型
export { errorHandler, ErrorType };

// Vue插件
export default {
  install(app) {
    // 设置全局错误处理
    app.config.errorHandler = (error, vm, info) => {
      errorHandler(error, ErrorType.RUNTIME, { vm, info });
    };

    // 添加到全局属性中，方便在组件中使用
    app.config.globalProperties.$errorHandler = errorHandler;
    app.config.globalProperties.$errorType = ErrorType;

    // 捕获未处理的Promise错误
    window.addEventListener('unhandledrejection', (event) => {
      const reason = event.reason;
      const isObjectLike =
        reason !== null && (typeof reason === 'object' || typeof reason === 'function');

      // 避免重复处理已经处理过的错误（仅对对象可用）
      if (isObjectLike && reason._handled) {
        event.preventDefault();
        return;
      }

      // 标记错误已被处理（字符串等原始类型无法挂属性）
      if (isObjectLike) {
        try {
          reason._handled = true;
        } catch {
          // ignore: reason 可能被冻结/不可扩展
        }
      }

      const errorType = reason?.response ? ErrorType.NETWORK : ErrorType.RUNTIME;
      // 阻止浏览器默认重复输出同一 Promise 异常
      event.preventDefault();
      errorHandler(reason, errorType, { unhandled: true });
    });
  },
};
