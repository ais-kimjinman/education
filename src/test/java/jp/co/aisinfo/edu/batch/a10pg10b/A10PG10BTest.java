package jp.co.aisinfo.edu.batch.a10pg10b;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class A10PG10BTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void subStringTest01() {

		// 期待値
		String str = "11";

		// 実行
		String result = "1101".substring(0, 2);

		// 検証
		assertEquals(str, result);

	}

}
