/**
 * 加解密的工具类
 * 使用：https://github.com/JuneAndGreen/sm-crypto
 *
 * @author yubaoshan
 */
import smCrypto from 'sm-crypto'

const sm2 = smCrypto.sm2
const cipherMode = 1 // 1 - C1C3C2，0 - C1C2C3，默认为1
const publicKey =
	'04298364ec840088475eae92a591e01284d1abefcda348b47eb324bb521bb03b0b2a5bc393f6b71dabb8f15c99a0050818b56b23f31743b93df9cf8948f15ddb54'

/**
 * 国密加解密工具类
 */
export default {
	// SM2加密
	doSm2Encrypt(msgString) {
		return sm2.doEncrypt(msgString, publicKey, cipherMode)
	},
	// SM2数组加密
	doSm2ArrayEncrypt(msgString) {
		return sm2.doEncrypt(msgString, publicKey, cipherMode)
	}
}
