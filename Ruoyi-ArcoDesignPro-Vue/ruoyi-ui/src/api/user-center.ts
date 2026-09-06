import axios from 'axios';
import {successResponseWrap} from "@/utils/setup-mock";

export interface MyProjectRecord {
  id: number;
  name: string;
  description: string;
  peopleNumber: number;
  contributors: {
    name: string;
    email: string;
    avatar: string;
  }[];
}

export function queryMyProjectList() {
    const contributors = [
        {
            name: '秦臻宇',
            email: 'qingzhenyu@arco.design',
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a8c8cdb109cb051163646151a4a5083b.png~tplv-uwbnlip3yd-webp.webp',
        },
        {
            name: '于涛',
            email: 'yuebao@arco.design',
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a8c8cdb109cb051163646151a4a5083b.png~tplv-uwbnlip3yd-webp.webp',
        },
        {
            name: '宁波',
            email: 'ningbo@arco.design',
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp',
        },
        {
            name: '郑曦月',
            email: 'zhengxiyue@arco.design',
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/8361eeb82904210b4f55fab888fe8416.png~tplv-uwbnlip3yd-webp.webp',
        },
        {
            name: '宁波',
            email: 'ningbo@arco.design',
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp',
        },
    ];
    const units = [
        {
            name: '企业级产品设计系统',
            description: 'Arco Design System',
        },
        {
            name: '火山引擎智能应用',
            description: 'The Volcano Engine',
        },
        {
            name: 'OCR文本识别',
            description: 'OCR text recognition',
        },
        {
            name: '内容资源管理',
            description: 'Content resource management ',
        },
        {
            name: '今日头条内容管理',
            description: 'Toutiao content management',
        },
        {
            name: '智能机器人',
            description: 'Intelligent Robot Project',
        },
    ];
    return successResponseWrap(
        new Array(6).fill(null).map((_item, index) => ({
            id: index,
            name: units[index].name,
            description: units[index].description,
            peopleNumber: 2560,
            contributors,
        }))
    );
}

export interface MyTeamRecord {
  id: number;
  avatar: string;
  name: string;
  peopleNumber: number;
}

export function queryMyTeamList() {
    return successResponseWrap([
        {
            id: 1,
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a8c8cdb109cb051163646151a4a5083b.png~tplv-uwbnlip3yd-webp.webp',
            name: '火山引擎智能应用团队',
            peopleNumber: 56,
        },
        {
            id: 2,
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp',
            name: '企业级产品设计团队',
            peopleNumber: 5620,
        },
        {
            id: 3,
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp',
            name: '前端/UE小分队',
            peopleNumber: 3520,
        },
        {
            id: 4,
            avatar:
                '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/8361eeb82904210b4f55fab888fe8416.png~tplv-uwbnlip3yd-webp.webp',
            name: '内容识别插件小分队',
            peopleNumber: 6520,
        },
    ]);
}

export interface LatestActivity {
  id: number;
  title: string;
  description: string;
  avatar: string;
}

export function queryLatestActivity() {
    return successResponseWrap(
        new Array(7).fill(null).map((_item, index) => ({
            id: index,
            title: '发布了项目 Arco Design System',
            description: '企业级产品设计系统',
            avatar:
                '//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png',
        }))
    );
}

export function saveUserInfo() {
    return successResponseWrap('ok');
}

export interface BasicInfoModel {
  email: string;
  nickname: string;
  countryRegion: string;
  area: string;
  address: string;
  profile: string;
}

export interface EnterpriseCertificationModel {
  accountType: number;
  status: number;
  time: string;
  legalPerson: string;
  certificateType: string;
  authenticationNumber: string;
  enterpriseName: string;
  enterpriseCertificateType: string;
  organizationCode: string;
}

export type CertificationRecord = Array<{
  certificationType: number;
  certificationContent: string;
  status: number;
  time: string;
}>;

export interface UnitCertification {
  enterpriseInfo: EnterpriseCertificationModel;
  record: CertificationRecord;
}

export function queryCertification() {
    return successResponseWrap({
        enterpriseInfo: {
            accountType: '企业账号',
            status: 0,
            time: '2018-10-22 14:53:12',
            legalPerson: '李**',
            certificateType: '中国身份证',
            authenticationNumber: '130************123',
            enterpriseName: '低调有实力的企业',
            enterpriseCertificateType: '企业营业执照',
            organizationCode: '7*******9',
        },
        record: [
            {
                certificationType: 1,
                certificationContent: '企业实名认证，法人姓名：李**',
                status: 0,
                time: '2021-02-28 10:30:50',
            },
            {
                certificationType: 1,
                certificationContent: '企业实名认证，法人姓名：李**',
                status: 1,
                time: '2020-05-13 08:00:00',
            },
        ],
    });
}

export function userUploadApi(
    data: FormData,
    config: {
        controller: AbortController;
        onUploadProgress?: (progressEvent: any) => void;
    }
) {
    // const controller = new AbortController();
    return axios.post('/system/user/profile/avatar', data, config);
}

export function getUserProfile() {
    return axios.get('/system/user/profile');
}
