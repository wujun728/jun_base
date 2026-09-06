import {
	getLocalTime
} from "./util.js"
const isDev = process.env.NODE_ENV !== 'production'
export default function(args) {
	if (isDev) {
		const {
			mode,
			yearRange
		} = args
		let val
		// #ifdef VUE2
		val = args.value
		// #endif
		// #ifdef VUE3
		val = args.modelValue
		// #endif
		if ("ymdhis".indexOf(mode) === -1)
			throw new Error("illegal 'mode'")

		if (getLocalTime(mode) == undefined)
			throw new Error("'mode' is not found")

		if (val) {
			if (val.length !== getLocalTime(mode).length) {
				// #ifdef VUE2
				throw new Error("'value' cannot be formatted as 'mode'")
				// #endif
				// #ifdef VUE3
				throw new Error("'modelValue' cannot be formatted as 'mode'")
				// #endif
			}
			const arr = val.split(/-|:|\s/)
			if (arr.length != mode.length) {
				// #ifdef VUE2
				throw new Error("'value' cannot be formatted as 'mode'")
				// #endif
				// #ifdef VUE3
				throw new Error("'modelValue' cannot be formatted as 'mode'")
				// #endif
			}

		}
		if (yearRange.length !== 2)
			throw new Error("the length of array 'year-rang' must be 2")
	}
}
