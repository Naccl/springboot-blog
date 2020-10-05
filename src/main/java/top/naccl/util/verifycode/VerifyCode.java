package top.naccl.util.verifycode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: 验证码
 * @Author: Naccl
 * @Date: 2020-05-09
 */

@NoArgsConstructor
@Getter
@Setter
public class VerifyCode {
	private String code;

	private byte[] imgBytes;

	private long expireTime;
}
