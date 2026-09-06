# 项目通用方法

本章节将介绍项目中常用的通用方法和工具函数。

## 日期格式化

```js
import { parseTime } from '@/utils/hsj/timeFormat'

// 格式化日期
parseTime(new Date()) // 2024-03-28 10:00:00
parseTime(new Date(), 'YYYY-MM-DD') // 2024-03-28
```

## local 存储方法

```js
import useStorage from '@/utils/hsj/useStorage'

// 存储数据
useStorage.set('key', { value: 'test' })
// 获取数据
const data = useStorage.get('key') // {value: 'test'}
// 删除数据
useStorage.remove('key')
// 清除所有localStorage
useStorage.clear()
```

## session 存储方法

```js
import useSession from '@/utils/hsj/useSession'

// 存储数据
useSession.set('key', { value: 'test' })
// 获取数据
const data = useSession.get('key') // {value: 'test'}
// 删除数据
useSession.remove('key')
// 清除所有localStorage
useSession.clear()
```
