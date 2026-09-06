import * as UUID from "uuid";

export class Message {
  /** 消息唯一标识符 */
  messageId: string;
  /** 发送者标识 */
  sender: string;
  /** 接收者标识 */
  receiver: string;
  /** 消息时间戳 */
  timestamp: string | number | Date;
  /** 消息类型（如命令、聊天、日志、事件等） */
  type: MessageType;
  /** 消息主题或事件名称 */
  subject: string;
  /** 消息内容 */
  content: string;
  /** 消息数据负载 */
  payload: Record<string, any>;
  /** 元数据，用于存储额外的信息 */
  metadata: Record<string, any>;
  /** 消息状态（如成功、失败、重试等） */
  status: string;
  /** 重试次数 */
  retryCount: number;
  /** 最大重试次数 */
  maxRetries: number;
  /** 重试间隔 */
  retryInterval: string;

  constructor() {
    this.messageId = UUID.v4();
    this.sender = '';
    this.receiver = '';
    this.timestamp = new Date();
    this.type = MessageType.MESSAGE;
    this.content = '';
    this.subject = '';
    this.payload = {};
    this.metadata = {};
    this.status = '';
    this.retryCount = 0;
    this.maxRetries = 3;
    this.retryInterval = '1000ms';
  }
}

export enum MessageType {
  EVENT = 'event',
  MESSAGE = 'message',
  ASYNC_MESSAGE = 'asyncMessage',
}

export function createMessage(receiver: string, params: Partial<Message>): Message {
  const message = new Message();
  Object.assign(message, params);
  message.type = MessageType.MESSAGE;
  message.receiver = receiver;
  return message;
}

export function createEventMessage(eventName: string, params: Partial<Message>): Message {
  const message = new Message();
  Object.assign(message, params);
  message.type = MessageType.EVENT;
  message.subject = eventName;
  return message;
}

export function createAsyncMessage(receiver: string, params: Partial<Message>): Message {
  const message = new Message();
  Object.assign(message, params);
  message.type = MessageType.ASYNC_MESSAGE;
  message.receiver = receiver;
  return message;
}