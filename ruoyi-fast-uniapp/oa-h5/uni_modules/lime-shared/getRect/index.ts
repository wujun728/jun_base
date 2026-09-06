// @ts-nocheck

// #ifdef APP-NVUE
const dom = uni.requireNativePlugin('dom')
// #endif

interface RectOptions {
	/**
	* 上下文
	*/
	context ?: any //ComponentInternalInstance
	/**
	* 所有节点 nvue不支持
	*/
	needAll ?: Boolean,
	nodes ?: UniNamespace.NodesRef
	type ?: keyof UniNamespace.NodesRef

}
/** 获取节点信息 */
export function getRect(selector : string, options : RectOptions = {}) {
	// #ifndef APP-NVUE
	const typeDefault = 'boundingClientRect'
	let { context, needAll, type = typeDefault } = options
	// #endif

	// #ifdef MP || VUE2
	if (context.proxy) context = context.proxy
	// #endif

	return new Promise<any>((resolve, reject) => {
		// #ifndef APP-NVUE
		const dom = uni.createSelectorQuery().in(context)[needAll ? 'selectAll' : 'select'](selector);
		const result = (rect) => {
			if (rect) {
				resolve(rect)
			} else {
				reject('no rect')
			}
		}
		if (type == typeDefault) {
			dom[type](result).exec()
		} else {
			dom[type]({
				node: true,
				size: true,
				rect: true
			}, result).exec()
		}
		// #endif
		// #ifdef APP-NVUE
		let { context } = options
		if (/#|\./.test(selector) && context.refs) {
			selector = selector.replace(/#|\./, '')
			if (context.refs[selector]) {
				selector = context.refs[selector]
				if(Array.isArray(selector)) {
					selector = selector[0]
				}
			}
		}
		dom.getComponentRect(selector, (res) => {
			if (res.size) {
				resolve(res.size)
			} else {
				reject('no rect')
			}
		})
		// #endif
	});
};