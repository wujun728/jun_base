import request from '@/utils/http'

export interface DictData {
  dictCode: number
  dictLabel: string
  dictValue: string
  listClass?: string
  cssClass?: string
}

export function fetchDictData(dictType: string) {
  return request.get<DictData[]>({ url: `/system/dict/data/type/${dictType}` })
}

export function uploadFile(file: File) {
  const data = new FormData()
  data.append('file', file)
  return request.post<{ url: string; fileName: string; newFileName: string }>({
    url: '/common/upload',
    data
  })
}

export async function download(url: string, params: Record<string, unknown>, filename: string) {
  return downloadFile('POST', url, params, filename)
}

export async function downloadByGet(
  url: string,
  params: Record<string, unknown>,
  filename: string
) {
  return downloadFile('GET', url, params, filename)
}

async function downloadFile(
  method: 'GET' | 'POST',
  url: string,
  params: Record<string, unknown>,
  filename: string
) {
  const blob = await request.request<Blob>({
    url,
    method,
    params,
    responseType: 'blob'
  })
  const objectUrl = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = objectUrl
  link.download = filename
  link.click()
  URL.revokeObjectURL(objectUrl)
}
