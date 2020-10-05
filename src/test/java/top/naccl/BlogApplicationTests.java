package top.naccl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.naccl.util.MailUtils;

@SpringBootTest
class BlogApplicationTests {
	@Autowired
	private MailUtils mailUtils;

	@Test
	public void test() {

	}

}
