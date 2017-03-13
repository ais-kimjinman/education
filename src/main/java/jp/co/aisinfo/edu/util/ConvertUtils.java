package jp.co.aisinfo.edu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 変換のユーティリティクラス
 *
 * @author Ais-Info
 */
public class ConvertUtils {

	private static Logger logger = LoggerFactory.getLogger("ConvertUtils");

	/**
	 * コンストラクタです。
	 * インスタンス化禁止です。
	 */
	private ConvertUtils() {
	}

	/**
     * 全角数字⇒半角数字への変換
     *
     * @param  str 変換対象文字列
     * @return 変換後文字列
     */
	public static String changeNumFullToHalf(String str) {
		String result = null;
		if(str != null) {
		    StringBuilder sb = new StringBuilder(str);
		    for (int i = 0; i < sb.length(); i++) {
		        int c = (int) sb.charAt(i);
		        if (c >= 0xFF10 && c <= 0xFF19) {
		            sb.setCharAt(i, (char) (c - 0xFEE0));
		        }
		    }
		    result = sb.toString();
		}
	    return result;
	}

}
