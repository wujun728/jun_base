// const apiEnv: ApiEnv = 'beta';
const apiEnv: ApiEnv = 'prod';
// const apiEnv: ApiEnv = 'dev';

const envMap = {
  dev: {
    baseUrl: 'http://m.dev.xxx.com',
    apiBaseUrl: 'http://192.168.10.66:8666/ngaq-oa'
  },
  beta: {
    baseUrl: 'http://m.beta.xxx.com',
    apiBaseUrl: 'http://192.168.10.50:9999/ngaq-oa'
  },
  prod: {
    baseUrl: 'https://m.xxx.com',
    apiBaseUrl: 'https://oa.custma.top/ngaq-oa'
  },
  local: {
    baseUrl: 'http://m.dev.xxx.com',
    apiBaseUrl: 'https://m.devapi.xxx.com'
  }
};

type ApiEnv = keyof typeof envMap;
type Env<T extends ApiEnv> = {
  apiEnv: T;
} & (typeof envMap)[T];

function createEnv(apiEnv: ApiEnv): Env<typeof apiEnv> {
  // return Object.assign({ apiEnv }, envMap[apiEnv]);
  return { apiEnv, ...envMap[apiEnv] };
}

const env = createEnv(apiEnv);
export default env;
