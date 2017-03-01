package jp.co.aisinfo.edu.common;

import org.junit.Test;

public class ConfigLoaderTest {

	private static final String fileName = "C:\\AIS\\workspace\\AisEducation\\src\\main\\resources\\education.properties";

	@Test
	public void testInit() throws Exception {

		// 期待値
		String str = "mybatisConfig.xml";

		// 実行
		ConfigLoader.init(fileName);
		String result = ConfigLoader.getParameter("mybatis.config.name");

		// 検証
		//assertEquals(str, result);

	}

}
