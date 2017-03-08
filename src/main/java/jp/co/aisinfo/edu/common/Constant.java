package jp.co.aisinfo.edu.common;

public class Constant {

	/**
	 * システムの設定ファイルの名称
	 */
	public static final String CONFIG_PATH = "system.properties.path";

	/**
	 * MyBatisの設定ファイルの名称
	 */
	public static final String MYBATIS_FILE_NAME = "mybatis.config.name";
    /**
     * ファイル入力パス
     */
	public static final String INPUT_FILE_PATH = "file.input.path";
	/**
	 * ファイル出力パス
	 */
	public static final String OUTPUNT_FILE_PATH = "file.output.path";
	/**
	 * 対応する文字コード:SJIS
	 */
	public static final String FILE_ENCODE_SJIS = "Shift_JIS";
	/**
	 * 対応する文字コード:UTF-8
	 */
	public static final String FILE_ENCODE_UTF8 = "UTF-8";

	public static final int RET_CODE_OK = 0;
	public static final int RET_CODE_NG = 1;

	public static final int ERR_CODE_NO_FILE = 01;
	public static final int ERR_CODE_OTHER = 99;


}
