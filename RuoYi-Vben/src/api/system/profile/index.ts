import { defHttp } from '@/utils/http/axios';
import { UserProfile } from './model';
import { buildUUID } from '@/utils/uuid';
// import { ContentTypeEnum } from '@/enums/httpEnum';

enum Api {
  root = '/system/user/profile',
  updatePassword = '/system/user/profile/updatePwd',
  updateAvatar = '/system/user/profile/avatar',
}

/**
 * 用户个人主页信息
 * @returns
 */
export function userProfile() {
  return defHttp.get<UserProfile>({ url: Api.root });
}

/**
 * 更新用户个人主页信息
 * @param data
 * @returns
 */
export function userProfileUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

/**
 * 用户修改密码
 * @param data
 * @returns
 */
export function userUpdatePassword(data: any) {
  return defHttp.put<void>({ url: Api.updatePassword, data });
}

interface FileCallBack {
  name: string;
  file: File | Blob;
  filename: string;
}

/**
 * 用户更新个人头像
 * @param data
 * @returns
 */
export function userUpdateAvatar(fileCallback: FileCallBack) {
  // const { file, filename } = fileCallback;
  // const imageFile = new window.File([file], filename, { type: file.type });
  // const data = {
  //   avatarfile: imageFile,
  // };
  // return defHttp.post({
  //   url: Api.updateAvatar,
  //   data,
  //   headers: { 'Content-Type': ContentTypeEnum.FORM_DATA },
  // });
  // console.log(fileCallback);
  // return;
  /** 直接点击头像上传 filename为空 由于后台通过拓展名判断(默认文件名blob) 会上传失败 */
  let { file } = fileCallback;
  const { filename } = fileCallback;
  /** 更改文件名 */
  if (!filename && file instanceof Blob) {
    file = new File([file], `${buildUUID()}.png`);
  }
  return defHttp.uploadFile<{ imgUrl: string }>(
    {
      url: Api.updateAvatar,
      timeout: 30 * 1000,
    },
    { file, name: 'avatarfile', filename },
  );
}
