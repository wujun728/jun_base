export const useFormatTime = (times: number, showTime = true) => {
    try {
        const time = new Date(times)
        const year = time.getFullYear();
        const month = time.getMonth() + 1;
        const date = time.getDate();
        const hour = time.getHours();
        const minute = time.getMinutes();
        const second = time.getSeconds();
        const NewDate = year + "-" + (month < 10 ? '0' + month : month) + "-" + (date < 10 ? '0' + date : date)
        const NewTime = hour + ":" + (minute < 10 ? '0' + minute : minute) + ":" + (second < 10 ? '0' + second : second);
        if (showTime) return `${NewDate} ${NewTime}`
        return NewDate
    } catch (e) {
        console.error('时间戳不合法')
    }
}