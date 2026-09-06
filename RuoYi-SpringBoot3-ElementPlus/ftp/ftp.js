import FtpDeploy from "ftp-deploy";
const ftpDeploy = new FtpDeploy();

const config = {
  user: "", // FTP 用户名
  password: "", // FTP 密码
  host: "", // FTP 主机地址
  port: 21, // FTP 端口，默认是21
  localRoot: "./dist", // 本地要上传的目录，通常为 Vite 构建输出目录
  remoteRoot: "/dist", // 远程服务器上的目标路径
  include: ["*", "**/*"], // 要包含的文件或目录模式
  exclude: [], // 要排除的文件或目录模式
  deleteRemote: false, // 是否在上传前删除远程目录中的文件
  forcePasv: false, // 强制被动模式
};

ftpDeploy
  .deploy(config)
  .then((res) => console.log("finished:", res))
  .catch((err) => console.error(err));

ftpDeploy.on("uploading", (data) => {
  console.log(data);
  console.log(`
    已传输文件数: ${data.transferredFileCount}
    总文件数: ${data.totalFilesCount}
    当前正在传输的文件: ${data.filename}
    完成百分比: ${parseInt(
      (data.transferredFileCount / data.totalFilesCount) * 100
    )}%
    `);
});
ftpDeploy.on("uploaded", (data) => console.log("Upload completed"));
