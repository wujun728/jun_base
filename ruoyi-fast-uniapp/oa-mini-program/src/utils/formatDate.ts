// 格式化时间
export  const formateDate = (date: any, formater: any) => {
    if (!date) {
        return '-- : --'
    }
    let dateTemp = date.toString().replace(/-/g, '/')
    let nowDate = new Date(dateTemp)
    let year: any = nowDate.getFullYear()
    let month: any = nowDate.getMonth() + 1
    month = month < 10 ? '0' + month : month
    let day: any = nowDate.getDate()
    day = day < 10 ? '0' + day : day
    let hour: any = nowDate.getHours()
    hour = hour < 10 ? '0' + hour : hour
    let minute: any = nowDate.getMinutes()
    minute = minute < 10 ? '0' + minute : minute
    let second: any = nowDate.getSeconds()
    second = second < 10 ? '0' + second : second
    let _after = year + '-' + month + '-' + day
    let week: any = nowDate.getDay();
    let weeks = new Array(
        "周日",
        "周一",
        "周二",
        "周三",
        "周四",
        "周五",
        "周六"
    );
    week = weeks[week];
    if (formater) {
        _after = formater
            .replace('yyyy', year)
            .replace('MM', month)
            .replace('dd', day)
            .replace('HH', hour)
            .replace('mm', minute)
            .replace('ss', second)
            .replace('week', week)

    }
    return _after
}
// let time1 = formateDate("2022-07-26 14:32:36.2222","yyyy-MM-dd HH:mm:ss");
// console.log(time1);//结果是：2022-07-26 14:32:36

// let time2 = formateDate("2022-07-26 14:32:36.2222","yyyy-MM-dd HH:mm");
// console.log(time2);//结果是：2022-07-26 14:32

// let time3 = formateDate("2022-07-26 14:32:36.2222","yyyy-MM-dd");
// console.log(time3);//结果是：2022-07-26

// let time4 = formateDate("2022-07-26 14:32:36.2222","yyyy年MM月dd日 HH时mm分ss秒");
// console.log(time4);//结果是：2022年07月26日 14时32分36秒

// let time5 = formateDate("2022-07-26 14:32:36.2222","yyyy年MM月dd日");
// console.log(time5);//结果是：2022年07月26日

// //等

// //新增加了根据当前日期推算是周几
// let time6= formateDate("2022-10-26 14:32:36.2222","yyyy-MM-dd （week）");
// console.log(time6);//结果是：2022-10-26 （周三）