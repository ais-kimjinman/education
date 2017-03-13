package jp.co.aisinfo.edu.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字操作のユーティリティクラス
 *
 * @author ais-info
 */
public class NumberUtils {

	/** フォーマット */
	public static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.##");

	/** フォーマット */
	public static final NumberFormat COST_FORMAT = new DecimalFormat("###,###,###,###,###");

	/**
	 * コンストラクタです。
	 * インスタンス化禁止です。
	 */
	private NumberUtils() {
	}


	/**
	 * 数値オブジェクトの比較（Nullセーフ）
	 *
	 * @param num1
	 *            num1
	 * @param num2
	 *            num2
	 * @return result
	 */
	public static final boolean is(Integer num1, Integer num2) {
		if (num1 == null) {
			if (num2 == null) {
				return true;
			}
		}
		if (num2 == null) {
			return false;
		}
		if (num1.intValue() == num2.intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 数値オブジェクトの比較（Nullセーフ）
	 *
	 * @param num1
	 *            num1
	 * @param num2
	 *            num2
	 * @return result
	 */
	public static final boolean is(Number num1, Number num2) {
		if (num1 == null) {
			if (num2 == null) {
				return true;
			}
		}
		if (num2 == null) {
			return false;
		}
		if (num1.doubleValue() == num2.doubleValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 値の切捨て処理
	 *
	 * @param value
	 *            値
	 * @param point
	 *            切捨て位置 ex1 value = 123.456 point=1の場合 123.4 ex2 value = 123.456
	 *            point=0の場合 123.0 ex3 value = 123.456 point=-1の場合 120.0
	 * @return
	 */
	public static double roundDown(double value, int point) {
		double result = 0.0d;

		if (value % 1 == 0)
			; // 何もしない

		int magnification = 1;
		for (int i = 0; i < Math.abs(point); i++) {
			magnification *= 10;
		}

		if (point >= 0) {
			result = java.lang.Math.floor(value * magnification);
			result /= magnification;
		} else {
			if (value < magnification) {
				result = value;
			} else {
				result = Math.floor((double) (value / magnification)) * magnification;
			}
		}
		return result;
	}

	/**
	 * 値の切捨て処理
	 *
	 * @param value
	 *            値
	 * @param point
	 *            切捨て位置 point >= 0 の場合は値をそのまま返す ex1 value=123 point=-1 result =
	 *            120
	 * @return
	 */
	public static int roundDown(int value, int point) {
		int result = 0;

		int magnification = 1;
		for (int i = 0; i < Math.abs(point); i++) {
			magnification *= 10;
		}

		if (point >= 0) {
			return value;
		} else {
			if (value < magnification) {
				result = value;
			} else {
				double wk = (double) value;
				result = (int) (Math.floor((double) (wk / magnification)) * magnification);
			}
		}
		return result;
	}

	/**
	 * 値の切り上げ処理
	 *
	 * @param value
	 *            値
	 * @param point
	 *            切り上げ位置 ex1 value = 123.456 point=1 result=123.5 ex2 value =
	 *            123.456 point=0 result=124 ex3 value = 123.456 point=-1
	 *            result=130
	 * @return
	 */
	public static double roundUp(double value, int point) {
		double result = 0.0f;

		if (value % 1 == 0)
			;

		int magnification = 1;
		for (int i = 0; i < Math.abs(point); i++) {
			magnification *= 10;
		}

		if (point >= 0) {
			result = Math.ceil(value * magnification);
			result /= magnification;
		} else {
			result = Math.ceil((double) (value / magnification)) * magnification;
		}
		return result;
	}

	/**
	 * 値の切り上げ処理
	 *
	 * @param value
	 * @param point
	 *            point >= 0 の場合はそのまま ex1 value=123 point=-1 result=130.0
	 * @return
	 */
	public static int roundUp(int value, int point) {
		int result = 0;

		int magnification = 1;
		for (int i = 0; i < Math.abs(point); i++) {
			magnification *= 10;
		}

		if (point >= 0) {
			result = value;
		} else {
			double wk = (double) value;
			result = (int) (Math.ceil((double) (wk / magnification)) * magnification);
		}
		return result;
	}

	/**
	 * 指定時間形式(hhmm形式)の時間間隔を返す
	 *
	 * @param reqFrom
	 *            開始時間
	 * @param reqTo
	 *            終了時間
	 * @return 差（分単位）
	 */
	public static int getDiffMinute(int reqFrom, int reqTo) {
		int startMinute = (reqFrom / 100) * 60 + (reqFrom % 100);
		int endMinute = (reqTo / 100) * 60 + (reqTo % 100);

		return Math.abs(endMinute - startMinute);
	}

	/**
	 * 指定時間（hhmm形式)に指定分を加算した値を返す
	 *
	 * @param reqTime
	 *            時分
	 * @param reqMinute
	 *            加算分
	 * @return 時分
	 */
	public static int getAddMinute(int reqTime, int reqMinute) {
		int minute = (reqTime / 100) * 60 + (reqTime % 100) + reqMinute;
		int result = (minute / 60) * 100 + (minute % 60);

		return result;
	}

	/**
	 * 指定時間(hhmm形式)を分単位に変換する
	 *
	 * @param reqTime
	 *            時分
	 * @return 分
	 */
	public static int convertMinute(int reqTime) {
		int result = (reqTime / 100) * 60 + (reqTime % 100);
		return result;
	}

}
