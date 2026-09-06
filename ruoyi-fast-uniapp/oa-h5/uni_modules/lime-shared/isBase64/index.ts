// @ts-nocheck
/**
 * @param path base64
 */
export const isBase64 = (path: string) => /^data:image\/(\w+);base64/.test(path)