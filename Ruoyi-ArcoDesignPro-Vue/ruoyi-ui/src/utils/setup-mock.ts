import debug from './env';

interface CommonRecord {
    total?: number,
    rows?: Array<unknown>,
    data?: unknown,
    msg: string,
    code: number,
    status: string,
}

export const successResponseWrap = (data: unknown | Array<unknown>) => {
    let record: CommonRecord;
    if (data instanceof Array) {
        record = {
            rows: data,
            total: data.length,
            data,
            msg: '请求成功',
            code: 200,
            status: 'ok'
        }
    } else {
        record = {
            data,
            msg: '请求成功',
            code: 200,
            status: 'ok'
        } as CommonRecord
    }
    return new Promise((resolve, reject) => {
        resolve(record);
    });
};

export const failResponseWrap = (data: unknown, msg: string, code = 500) => {
  return {
    data,
    status: 'fail',
    msg,
    code,
  };
};
