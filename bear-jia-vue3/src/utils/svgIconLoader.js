/**
 * SVG 图标批量加载优化
 * 将所有 SVG 图标合并为一个 sprite，减少 HTTP 请求
 */

/**
 * 批量导入 SVG 图标
 * 使用 Vite 的 import.meta.glob 进行动态导入
 */
export function loadSvgIcons() {
  // 使用 eager: true 立即加载所有 SVG，避免动态导入
  const svgModules = import.meta.glob('@/assets/icons/*.svg', {
    eager: true,
    as: 'raw',
  });

  const svgMap = new Map();

  Object.entries(svgModules).forEach(([path, content]) => {
    // 提取文件名作为图标名称
    const name = path.match(/\/([^/]+)\.svg$/)?.[1];
    if (name && content) {
      svgMap.set(name, content);
    }
  });

  return svgMap;
}

/**
 * 创建 SVG Sprite
 * 将所有 SVG 图标合并到一个 sprite 中
 */
export function createSvgSprite() {
  const svgMap = loadSvgIcons();
  const symbols = [];

  svgMap.forEach((content, name) => {
    // 将 SVG 内容转换为 symbol
    const symbol = content
      .replace(/<svg/, `<symbol id="icon-${name}"`)
      .replace(/<\/svg>/, '</symbol>')
      .replace(/width="[^"]*"/, '')
      .replace(/height="[^"]*"/, '');

    symbols.push(symbol);
  });

  // 创建一个隐藏的 SVG sprite 容器
  const sprite = `
    <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
      ${symbols.join('\n')}
    </svg>
  `;

  return sprite;
}

/**
 * 将 SVG sprite 插入到 DOM 中
 */
export function injectSvgSprite() {
  if (typeof window === 'undefined') return;

  // 检查是否已经注入
  if (document.getElementById('svg-sprite-container')) {
    return;
  }

  const sprite = createSvgSprite();
  const container = document.createElement('div');
  container.id = 'svg-sprite-container';
  container.innerHTML = sprite;

  // 将 sprite 添加到 body 的开头
  if (document.body) {
    document.body.insertBefore(container, document.body.firstChild);
  } else {
    document.addEventListener('DOMContentLoaded', () => {
      document.body.insertBefore(container, document.body.firstChild);
    });
  }
}
