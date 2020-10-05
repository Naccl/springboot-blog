package top.naccl.util.verifycode;

import top.naccl.util.RandomUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: 字符串验证码实现类
 * @Author: Naccl
 * @Date: 2020-05-11
 */

@AllArgsConstructor
public class StringVerifyCodeGenerateImpl implements VerifyCodeGenerate {
	private int width;
	private int height;
	private int length;

	private static final Logger logger = LoggerFactory.getLogger(StringVerifyCodeGenerateImpl.class);

	@Override
	public VerifyCode generate() {
		VerifyCode verifyCode;
		try (
				//将流的初始化放到这里就不需要手动关闭流
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		) {
			String code = generateStringVerifyCode(width, height, baos);
			verifyCode = new VerifyCode();
			verifyCode.setCode(code);
			verifyCode.setImgBytes(baos.toByteArray());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			verifyCode = null;
		}
		return verifyCode;
	}

	/**
	 * 生成字符串验证码并返回code，将图片写到os中
	 */
	public String generateStringVerifyCode(int width, int height, OutputStream os) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		fillBackground(graphics, width, height);
		String randomStr = RandomUtils.randomString(length);
		createCharacter(graphics, randomStr);
		graphics.dispose();
		//设置JPEG格式
		ImageIO.write(image, "JPEG", os);
		return randomStr;
	}
}
