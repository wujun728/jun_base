/**
 * 数字格式化
 */
 module.exports = {
    numberFormat(value) {  
        if (!value && value !== 0) return '-';
        var intPart = Number(value) | 0; //获取整数部分
        var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,'); //将整数部分逢三一断
    
        var floatPart = ".00"; //预定义小数部分
        var value2Array = value.toString().split(".");
    
        //=2表示数据有小数位
        if (value2Array.length == 2) {
            floatPart = value2Array[1].toString(); //拿到小数部分
    
            if (floatPart.length == 1) { //补0,实际上用不着
                return intPartFormat + "." + floatPart + '0';
            } else {
                return intPartFormat + "." + floatPart;
            }
        } else {
            return intPartFormat + floatPart;
        }
        },
}