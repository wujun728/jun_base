// 应用全局配置
const config = {
   // baseUrl: 'https://vue.ruoyi.vip/prod-api',
   //cloud后台网关地址
   baseUrl: 'http://localhost:8080',
   clientID: '428a8310cd442757ae699df5d894f051',
   // 应用信息
   appInfo: {
     // 应用名称
     name: "ruoyi-app-vue3",
     // 应用版本
     version: "1.1.0",
     // 应用logo
     logo: "/static/logo.png",
     // 官方网站
     site_url: "http://ruoyi.vip",
     // 政策协议
     agreements: [{
         title: "隐私政策",
         url: "https://ruoyi.vip/protocol.html"
       },
       {
         title: "用户服务协议",
         url: "https://ruoyi.vip/protocol.html"
       }
     ]
   },
   /** 接口加密功能开关(如需关闭 后端也必须对应关闭) */
   appEncrypt: true,
   /** 接口加密传输 RSA 公钥与后端解密私钥对应 如更换需前后端一同更换 */
   rsaPublicKey: 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ==',
   /** 接口响应解密 RSA 私钥与后端加密公钥对应 如更换需前后端一同更换 */
   rsaPrivateKey: 'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmc3CuPiGL/LcIIm7zryCEIbl1SPzBkr75E2VMtxegyZ1lYRD+7TZGAPkvIsBcaMs6Nsy0L78n2qh+lIZMpLH8wIDAQABAkEAk82Mhz0tlv6IVCyIcw/s3f0E+WLmtPFyR9/WtV3Y5aaejUkU60JpX4m5xNR2VaqOLTZAYjW8Wy0aXr3zYIhhQQIhAMfqR9oFdYw1J9SsNc+CrhugAvKTi0+BF6VoL6psWhvbAiEAxPPNTmrkmrXwdm/pQQu3UOQmc2vCZ5tiKpW10CgJi8kCIFGkL6utxw93Ncj4exE/gPLvKcT+1Emnoox+O9kRXss5AiAMtYLJDaLEzPrAWcZeeSgSIzbL+ecokmFKSDDcRske6QIgSMkHedwND1olF8vlKsJUGK3BcdtM8w4Xq7BpSBwsloE='
 }

 export default config
