// @ts-nocheck
interface CSSProperties {
	[key: string]: string | number
}
/** converting camel-cased strings to be lowercase and link it with Separato */
export function toLowercaseSeparator(key: string) {
  return key.replace(/([A-Z])/g, '-$1').toLowerCase();
}

export function getStyleStr(style: CSSProperties): string {
  return Object.keys(style).filter(key => style[key] !== undefined && style[key] !== null && style[key] !== '')
    .map((key: string) => `${toLowercaseSeparator(key)}: ${style[key]};`)
    .join(' ');
}

