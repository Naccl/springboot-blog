package top.naccl.util.verifycode;

/**
 * @Description: 验证码工厂
 * @Author: Naccl
 * @Date: 2020-05-11
 */
public class VerifyCodeFactory {
	public VerifyCode getStringVerifyCode(int width, int height, int length) {
		VerifyCodeGenerate stringVerifyCodeGenerate = new StringVerifyCodeGenerateImpl(width, height, length);
		return stringVerifyCodeGenerate.generate();
	}

	public VerifyCode getMathVerifyCode(int width, int height, char sign) {
		VerifyCodeGenerate mathVerifyCodeGenerate = new MathVerifyCodeGenerateImpl(width, height, sign);
		return mathVerifyCodeGenerate.generate();
	}
}
