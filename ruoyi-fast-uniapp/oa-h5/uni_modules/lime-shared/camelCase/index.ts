/**
 * 字符串转驼峰
 * @param {string} str 
 * @return string
 */ 
export function camelCase(str : string) : string {
	// 将字符串分割成单词数组
	let words : string[] = str.split(/[\s_-]+/);

	// 将数组中的每个单词首字母大写（除了第一个单词）
	let camelCased : string[] = words.map((word, index) => {
		if (index === 0) {
			return word.toLowerCase(); // 第一个单词全小写
		}
		return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
	});

	// 将数组中的单词拼接成一个字符串
	return camelCased.join('');
}