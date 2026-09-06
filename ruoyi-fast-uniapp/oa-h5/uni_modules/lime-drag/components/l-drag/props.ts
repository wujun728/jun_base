// @ts-nocheck
export default {
		list: {
			 type: Array,
			 default: []
		},
		column: {
			 type: Number,
			 default: 2
		},
		/**宽高比 填写这项, gridHeight 失效*/
		aspectRatio: Number,
		gridHeight: {
			type: [Number, String],
			default: '120rpx'
		},
		// removeStyle: String,
		// handleStyle: String,
		damping: {
			type: Number,
			default: 40
		},
		friction: {
			type: Number,
			default: 2
		},
		/**
		 * 由于 movable-area 无法动态设置高度，故增加额外的行数。用于增加动态项时，高度不够无法正确显示
		 */
		extraRow: {
			type: Number,
			default: 0
		},
		/**
		 * 由于 movable-area 无法动态设置高度，但vif 重染可以，另一种实现动态高度的方式, 这BUG uni官方好像修复了。
		 */
		// reset: Boolean,
		// sort: Boolean,
		// remove: Boolean,
		ghost: Boolean,
		handle: Boolean,
		touchHandle: Boolean,
		before: Boolean,
		after: Boolean,
		disabled: Boolean,
		longpress: Boolean,
	}