import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvVwyAyLabhi5SeUdgcVd"+
"GfDuaFmCLOS0MCgKpvlLNPh7lp/lXoj4XERSSQgzh7wBBIInlqytYZgOZAq+hdSn"+
"oKM+hhE7ytowK77kdbZw418XxK3Lx+Jft2+5l6SxsZAE916BJ46U6EK8QpABq9tL"+
"P1rUKTowTygFy6SWlBl+RbulRVPbt4m0gFObXM/eh5ZIm97hLsQcAuadQ0Kqb+02"+
"nsF2bmViBtMMK2Yj6IRwX9YV+N8cjsJXeSdsVGwF/fGtJ2xKq/FXE4rrtLK99BKw"+
"1L7ru2Po6tZKIVOGZgZnrRX6Iw/vMqSa+FnTxgfsNZDcWOqZ8K+aqQ7YdveGzvZk"+
"cwIDAQAB"

const privateKey = ''

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

