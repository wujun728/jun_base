import { SysDictData } from '@/types/api/system/dict'
import response from '@/utils/http'

export function getDicts(dictType: string) {
  return response<SysDictData[]>({
    url: '/system/dict/data/type/' + dictType,
    method: 'get'
  })
}
