package jp.co.aisinfo.edu.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConvertUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testChangeNumFullToHalf() {

		// 期待値
		String expected1 = "1234567890";
		String expected2 = "abcde";
		String expected3 = null;

		// 準備
		String param1 = "１２３４５６７８９０";
		String param2 = "abcde";

		// 実行
		String result1 = ConvertUtils.changeNumFullToHalf(param1);
		String result2 = ConvertUtils.changeNumFullToHalf(param2);
		String result3 = ConvertUtils.changeNumFullToHalf(null);

		// 検証
		assertEquals("文字列1が一致していません。", expected1, result1);
		assertEquals("文字列2が一致していません。", expected2, result2);
		assertEquals("文字列3が一致していません。", expected3, result3);
	}

}
