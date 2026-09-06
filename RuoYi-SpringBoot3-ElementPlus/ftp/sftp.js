/*
  SFTP 上传脚本（ssh2-sftp-client）
  功能：将 ./dist 目录递归上传到 SFTP 服务器的 /data/www/dist
*/

import path from 'node:path';
import SftpClient from 'ssh2-sftp-client';
import { readdir, stat } from 'node:fs/promises';

// 基本配置（可通过环境变量覆盖）
const config = {
  host: process.env.SFTP_HOST || '',
  port: Number(process.env.SFTP_PORT || 22),
  username: process.env.SFTP_USER || '',
  password: process.env.SFTP_PASSWORD || '',
  localDir: process.env.SFTP_LOCAL || path.resolve(process.cwd(), 'dist'),
  remoteDir: process.env.SFTP_REMOTE || '/dist',
};

function logConfig() {
  console.log('================ SFTP 上传配置 ================');
  console.log('主机:       ', config.host);
  console.log('端口:       ', config.port);
  console.log('用户名:     ', config.username);
  console.log('本地目录:   ', config.localDir);
  console.log('远程目录:   ', config.remoteDir);
  console.log('==============================================');
}

async function ensureRemoteDir(sftp, remoteDir) {
  try {
    await sftp.stat(remoteDir);
  } catch (err) {
    if (err && err.code === 2) {
      await sftp.mkdir(remoteDir, true);
    } else {
      throw err;
    }
  }
}

async function main() {
  const sftp = new SftpClient();
  logConfig();

  try {
    // 统计本地将要上传的文件数量，用于进度显示
    async function countLocalFiles(dir) {
      let count = 0;
      const entries = await readdir(dir, { withFileTypes: true });
      for (const entry of entries) {
        const fullPath = path.join(dir, entry.name);
        if (entry.isDirectory()) {
          count += await countLocalFiles(fullPath);
        } else {
          // 仅统计普通文件
          const st = await stat(fullPath);
          if (st.isFile()) count += 1;
        }
      }
      return count;
    }

    const totalFiles = await countLocalFiles(config.localDir).catch(() => 0);
    let uploadedFiles = 0;
    const startTime = Date.now();

    // 监听 uploadDir 的上传事件
    sftp.on('upload', info => {
      uploadedFiles += 1;
      const percent = totalFiles > 0 ? ((uploadedFiles / totalFiles) * 100).toFixed(1) : '0.0';
      const elapsed = (Date.now() - startTime) / 1000;
      const rate = elapsed > 0 ? (uploadedFiles / elapsed).toFixed(2) : '0.00';
      const line = `已上传 ${uploadedFiles}/${totalFiles} (${percent}%) 速率: ${rate}/s 当前: ${info.source}`;
      process.stdout.write('\r' + line);
    });

    await sftp.connect({
      host: config.host,
      port: config.port,
      username: config.username,
      password: config.password,
      algorithms: {
        // 兼容性更好的一组算法（根据实际需要调整）
        kex: [
          'ecdh-sha2-nistp521',
          'ecdh-sha2-nistp384',
          'ecdh-sha2-nistp256',
          'diffie-hellman-group-exchange-sha256',
          'diffie-hellman-group14-sha1',
        ],
        serverHostKey: ['ssh-rsa', 'ssh-ed25519', 'rsa-sha2-512', 'rsa-sha2-256'],
        cipher: [
          'aes256-ctr',
          'aes192-ctr',
          'aes128-ctr',
          'aes256-gcm',
          'aes128-gcm',
        ],
        hmac: ['hmac-sha2-512', 'hmac-sha2-256', 'hmac-sha1'],
      },
      readyTimeout: 20000,
    });

    await ensureRemoteDir(sftp, config.remoteDir);

    console.log('开始上传...');
    // uploadDir 会递归上传目录，自动创建缺失目录
    const result = await sftp.uploadDir(config.localDir, config.remoteDir);
    // result 在某些版本返回 true/undefined，主要以成功不抛错为准
    process.stdout.write('\n');
    console.log('上传完成');
  } catch (err) {
    console.error('上传失败:', err.message || err);
    process.exitCode = 1;
  } finally {
    try { await sftp.end(); } catch (e) {}
  }
}

main();


