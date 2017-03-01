package jp.co.aisinfo.edu.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * プロパティファイルの値取得
 *
 * @author ais-info
 */
public final class ConfigLoader {

	/**
	 * ログ出力
	 */
	private static Logger logger = LoggerFactory.getLogger("ConfigLoader");
	/**
	 * マップ
	 */
	private static Properties config = null;
	/**
	 * コンストラクタ 本クラスのインスタンスを作成させないため、privateのコンストラクタ
	 */
	private ConfigLoader() {
	}

	/**
	 * テスト用のダミーメインクラス
	 *
	 * @param args
	 *            引数
	 * @throws IOException
	 *             例外
	 */
	public static void main(String[] args) throws IOException {
		try {
			String fileName = "C:\\AIS\\workspace\\AisEducation\\src\\main\\resources\\education.properties";
			init(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初期化処理
	 *
	 * @param configFileName
	 *            ファイル名
	 * @throws Exception
	 *             例外
	 */
	public static synchronized void init(String configFileName) throws Exception {
		config = load(configFileName);
		// 読みこんだ情報をログに出力
		@SuppressWarnings("rawtypes")
		Set entrySet = config.entrySet();
		@SuppressWarnings("rawtypes")
		Iterator entryIte = entrySet.iterator();
		while (entryIte.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry obj = (Map.Entry) entryIte.next();
			logger.info(obj.getKey() + "=" + obj.getValue());
		}
	}

	/**
	 * 指定されたファイルを設定ファイルとして読み込み、 {@link Properties} にして返します。
	 * 指定されたファイルの各行は「キー=値」となるプロパティ、または#で始まるコメントを含むことが出来ます。
	 *
	 * @param filePath
	 *            読み込むファイルのパス
	 * @return 読み込んだ設定
	 * @throws IOException
	 *             読み込みに失敗した場合
	 */
	private static Properties load(String filePath) throws IOException {
		logger.info("ConfigName=" + filePath);
		Properties properties = new Properties();
		FileInputStream fis = null;   // ファイルを適当に読み込む
		InputStreamReader isr = null; // 文字コードを直す
		try {
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis, "UTF-8");
			properties.load(isr);
		} finally {
			// 問題が起きたらとりあえずストリームを閉じ、残りは呼び出し側に任せる
			if (isr != null) {
				if (fis != null) {
					fis.close();
				}
				isr.close();
			}
		}
		return properties;
	}

	/**
	 * 設定項目を返す。
	 *
	 * @param name
	 *            項目名
	 * @return 値
	 */
	public static synchronized String getParameter(final String name) {
		if (config == null) {
			return null;
		}
		return config.getProperty(name);
	}

	/**
	 * 設定項目を返す。
	 *
	 * @param name
	 *            項目名
	 * @param isRequired
	 *            必須とするか（必須で値が取得できない場合は、例外）
	 * @return 値
	 * @throws Exception
	 *            必須の項目が取得できない場合
	 */
	public static synchronized String getParameter(final String name, boolean isRequired) throws Exception {
		if (config == null) {
			throw new Exception("Configクラスが正しく初期化されていません");
		}
		String rtn = config.getProperty(name);
		if ((rtn == null || "".equals(rtn)) && isRequired) {
			throw new Exception(name + "が設定されていません。");
		}
		return config.getProperty(name);
	}

	/**
	 * 設定項目をセットする。
	 *
	 * @param name
	 *            項目名
	 * @param value
	 *            値
	 */
	public static synchronized void putParameter(final String name, final String value) {
		config.put(name, value);
		logger.info(name + " = " + value);
	}

}