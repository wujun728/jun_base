
// let baseUrl = "https://captcha.anji-plus.com/captcha-api"
import request from '@/utils/request'

interface Point {
    x?: number;
    y?: number;
    [key: string]: any;
}

interface CaptchaVO {
    captchaId?: string;
    projectCode?: string;
    captchaType?: string;
    captchaOriginalPath?: string;
    captchaFontType?: string;
    secretKey?: string;
    originalImageBase64?: string;
    jigsawImageBase64?: string;
    pointJson?: string;
    token?: string;
    captchaVerification?: string;
    clientUid?: string;
    browserInfo?: string;
    captchaFontSize?: number;
    ts?: number;
    result?: boolean;
    point?: Point;
    wordList?: string[];
    pointList?: Point[];
}

export const myRequest = (option:any={})=>{
	return request<CaptchaVO>({
		url: option.url,
		data: option.data,
		method:option.method || "GET"
	})
}
