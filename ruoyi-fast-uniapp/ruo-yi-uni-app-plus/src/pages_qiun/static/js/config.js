
const COLOR = [
    "#EE6A66", "#6BC588", "#FFC300", "#24ABFD"
];

var ISCANVAS2D = true;

switch (uni.getSystemInfoSync().platform) {
	case 'android':
		ISCANVAS2D = true
		break;
	case 'ios':
		ISCANVAS2D = true
		break;
	default:
		ISCANVAS2D = false
		break;
}

const RESPOND = {
    success: 0,
    warn: 301,
    error: 500,
};

const TIMEARRAY = [
    {
        text: '当天',
        value: 'today'
    },
    {
        text: '昨天',
        value: 'yesterday'
    },
    {
        text: '本周',
        value: 'week'
    },
    {
        text: '上周',
        value: 'weeklast'
    },
    {
        text: '本月',
        value: 'month'
    },
    {
        text: '上月',
        value: 'monthlast'
    },
	{
	    text: '指定日期',
	    value: 'auto'
	}
];
const TABLIST = [
    {name:"企业微信",type:"WECHAT"},
    {name:"会员运营",type:"OPERATE"},
    {name:"会员健康",type:"GJJK"},
    {name:"会员服务",type:"SERVICE"},
];

const CARD_MENU = [
	{title:"会员报表中心",author:"howcode",img:"https://s1.ax1x.com/2023/03/31/ppRp4iV.jpg",url:"/myPackageA/pages/main/index"},
	{title:"智慧教育报表中心",author:"howcode",img:"https://s1.ax1x.com/2023/03/31/ppRp5GT.jpg",url:"/myPackageA/pages/school/index"},
	{title:"差旅报表中心",author:"秋云",img:"https://s1.ax1x.com/2023/03/31/ppRpfI0.jpg",url:""},
	{title:"运动报表中心",author:"howcode",img:"https://s1.ax1x.com/2023/03/31/ppRpWaq.jpg",url:"/myPackageA/pages/sport/index"},
	{title:"财务报表中心",author:"howcode",img:"https://s1.ax1x.com/2023/03/31/ppRpozF.jpg",url:"/myPackageA/pages/finance/index"},
]


export default {
    COLOR,
    TIMEARRAY,
    TABLIST,
    RESPOND,
		ISCANVAS2D,
		CARD_MENU
}