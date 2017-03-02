package jp.co.aisinfo.edu.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigLoaderTest {

	private static final String fileName = "C:\\AIS\\workspace\\education\\src\\main\\resources\\education.properties";

	@Test
	public void testInit() throws Exception {

		// システムプロパティに追加（パス）
		System.setProperty("system.properties.path", fileName);
		String path = System.getProperty(Constant.CONFIG_PATH);

		// 期待値
		String str = "mybatisConfig.xml";

		// 実行
		ConfigLoader.init(path);
		String result = ConfigLoader.getParameter("mybatis.config.name");

		// 検証
		assertEquals(str, result);

	}

}
