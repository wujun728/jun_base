import {MessageApi} from "naive-ui/es/message";
import {NotificationApi} from "naive-ui/es/notification";
import {DialogApi} from "naive-ui/es/dialog";
import {LoadingBarApi} from "naive-ui/es/loading-bar";

declare global {

    interface Window {
        $message: MessageApi;
        $dialog: DialogApi;
        $notification: NotificationApi;
        $loading: LoadingBarApi;
    }

    declare module '*.svg'
    declare module '*.png'
    declare module '*.jpg'
    declare module '*.jpeg'
    declare module '*.gif'
    declare module '*.bmp'
    declare module '*.tiff'
}


