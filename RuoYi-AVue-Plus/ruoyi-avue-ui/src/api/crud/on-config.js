/**
 * 事件字段定义配置
 *
 * @author liukx
 * @date  -
 */
export default {
  option: {
    column: {
      // 处理需要进行domain路径补全的字段
      domain: {
        // 组件 : [ 字段值 ]
        select: [
          'dicUrl'
        ],
        upload: [
          'action'
        ]
      },
      // 行事件定义字段
      event: {
        'onClick': 'click',
        'onBlur': 'blur',
        'onFocus': 'focus',
        'onChange': 'change'
      }
    }
  }
}
