package jp.co.aisinfo.edu.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日付操作のユーティリティクラス
 *
 * @author Ais-Info
 */
public class DateUtils {

	private static Logger logger = LoggerFactory.getLogger("DateUtils");

	/** 基本日付のフォーマット文字列 */
	public static final String BASIC_DATETIME_FORMAT = "yyyyMMdd";
	/** 区切りの無い日付フォーマット */
	public static final String SECOND_FORMAT_STR = "yyyyMMddHHmmss";
	/** 長い年号 */
	public static final String NENGO_FORMAT_1 = "GGGGyy年 MM月 dd日";
	/** 短い年号 */
	public static final String NENGO_FORMAT_2 = "GGyy年 MM月 dd日";


	/**
	 * 基本日付のフォーマット
	 *
	 * @return 基本で利用する日付のフォーマット
	 */
	public static DateFormat getBasicDateFormat() {
		return new SimpleDateFormat(BASIC_DATETIME_FORMAT);
	}

	/**
	 * DateFormatを取得
	 *
	 * @return Dateformat
	 */
	public static final DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG);
	}

	/**
	 * 日付のみを表示するフォーマット(ロケールで切り替えない際に利用)
	 *
	 * @return 日付のみを表示するフォーマット
	 */
	public static final DateFormat gateDayFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	/**
	 * 短い形式のDateformatを取得
	 *
	 * @return 短い形式のDateformat
	 */
	public static final DateFormat getShortDayFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}

	/**
	 * 区切りの無い日付フォーマットを取得
	 *
	 * @return 区切りの無い日付フォーマット
	 */
	public static final DateFormat getSimpleFormat() {
		return new SimpleDateFormat(SECOND_FORMAT_STR);
	}

	/**
	 * 通信に使用する日付フォーマットによる日付文字列の取得
	 *
	 * @param d
	 *            date
	 * @return String
	 */
	public static String formatTransferDateTime(Date d) {
		return getBasicDateFormat().format(d);
	}

	/**
	 * 通信に使用する日付フォーマットによる日付文字列の取得
	 *
	 * @param d
	 *            string
	 * @return Date
	 * @throws ParseException
	 *             ParseException
	 */
	public static Date parseTransferDateTime(String d) throws ParseException {
		return getBasicDateFormat().parse(d);
	}

	/**
	 * 2つの日付の差を求めます。 日付文字列 strDate1 - strDate2 が何日かを返します。
	 *
	 * @param strDate1
	 *            日付文字列 yyyy/MM/dd
	 * @param strDate2
	 *            日付文字列 yyyy/MM/dd
	 * @return 2つの日付の差
	 * @throws ParseException
	 *             日付フォーマットが不正な場合
	 */
	public static int differenceDays(String strDate1, String strDate2) throws ParseException {
		Date date1 = gateDayFormat().parse(strDate1);
		Date date2 = gateDayFormat().parse(strDate2);
		return differenceDays(date1, date2);
	}

	/**
	 * 2つの日付の差を求めます。 java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 *
	 * 計算方法は以下となります。 1.最初に2つの日付を long 値に変換します。 ※この long 値は 1970 年 1 月 1 日
	 * 00:00:00 GMT からの 経過ミリ秒数となります。 2.次にその差を求めます。 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 日付の差を求めることができます。 ※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 *
	 * @param date1
	 *            日付 java.util.Date
	 * @param date2
	 *            日付 java.util.Date
	 * @return 2つの日付の差
	 */
	public static int differenceDays(Date date1, Date date2) {

		StringBuilder builder = new StringBuilder();
		builder.append("********* 日付の差分計算のインプット ********");
		builder.append("\n\t入力１ : " + getBasicDateFormat().format(date1));
		builder.append("\n\t入力２ : " + getBasicDateFormat().format(date2));
		logger.debug(builder.toString());

		date1 = getRoundsDate(date1);
		date2 = getRoundsDate(date2);

		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();

		long one_date_time = 1000 * 60 * 60 * 24;
		long diff = datetime1 - datetime2;
		long diffDays = diff / one_date_time;

		builder = new StringBuilder();
		builder.append("********* 日付の差分計算 ********");
		builder.append("\n\t計算値１ : " + getBasicDateFormat().format(date1));
		builder.append("\n\t計算値２ : " + getBasicDateFormat().format(date2));
		builder.append("\n\t差分   : " + diff);
		builder.append("\n\t一日のミリ秒   : " + one_date_time);
		builder.append("\n\t日付の差分   : " + diffDays);
		logger.debug(builder.toString());

		return (int) diffDays;
	}

	/**
	 * Dateは、日付(yyyy/MM/dd)以外に、時刻(hh:mm:ss.SSS)を持っている。
	 * 日付の情報のみを使いたい場合、この時刻の情報が邪魔になることがある。 そこで、時刻の情報を固定値をセットしたDateを取得する。
	 *
	 * @param date
	 *            Date
	 * @return rounds date
	 */
	public static Date getRoundsDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 12, 0, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		StringBuilder builder = new StringBuilder();
		builder.append("********* 日付の丸めの処理 ********");
		builder.append("\n\t入力 : " + getBasicDateFormat().format(date));
		builder.append("\n\t出力 : " + getBasicDateFormat().format(calendar1.getTime()));
		logger.debug(builder.toString());

		return calendar1.getTime();
	}

	/**
	 * 長い年号を取得
	 *
	 * @return　result
	 */
	public static String getWareiNowDateLong() {
		String result = "";

		Locale.setDefault(new Locale("ja", "JP", "JP"));
		DateFormat format = new SimpleDateFormat(NENGO_FORMAT_1);
		result = format.format(new Date());
		return result;
	}

	/**
	 * 短い年号を取得
	 *
	 * @return　result
	 */
	public static String getWareiNowDateShort() {
		String result = "";

		Locale.setDefault(new Locale("ja", "JP", "JP"));
		DateFormat format = new SimpleDateFormat(NENGO_FORMAT_1);
		result = format.format(new Date());
		return result;
	}

}
