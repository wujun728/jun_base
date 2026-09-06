/**
 * 将传递的百分比与十六进制颜色的R、G或B相加
 * @param {string} color 要更改的颜色
 * @param {number} amount 更改颜色的量
 * @returns {string} 颜色的处理部分
 */
function useAddLight(color: string, amount: number): string {
    const cc = parseInt(color, 16) + amount;
    const c = cc > 255 ? 255 : cc;
    return c.toString(16).length > 1 ? c.toString(16) : `0${c.toString(16)}`;
}

/**
 * 根据传递的百分比点亮6个字符的HEX颜色
 * @param {string} color 要更改的颜色
 * @param {number} amount 更改颜色的量
 * @returns {string} 处理后的颜色表示为HEX
 */
export function useLighten(color: string, amount: number): string {
    color = color.indexOf('#') >= 0 ? color.substring(1, color.length) : color;
    amount = Math.trunc((255 * amount) / 100);
    return `#${useAddLight(color.substring(0, 2), amount)}${useAddLight(
        color.substring(2, 4),
        amount
    )}${useAddLight(color.substring(4, 6), amount)}`;
}

/**
 * hex转rgba
 * @param bgColor
 * @param alpha
 */
export const useHexToRgba = (bgColor: string, alpha = 0.2): string => {
    let color = bgColor.slice(1); // 去掉'#'号
    let rgba = [
        parseInt("0x" + color.slice(0, 2)),
        parseInt("0x" + color.slice(2, 4)),
        parseInt("0x" + color.slice(4, 6)),
        alpha
    ];
    return `rgba(${rgba.toString()})`;
};