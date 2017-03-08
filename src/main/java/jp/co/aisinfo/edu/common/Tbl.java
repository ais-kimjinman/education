package jp.co.aisinfo.edu.common;

import jp.co.aisinfo.edu.exception.DBException;

public enum Tbl {

	TblZip("development", "jp.co.aisinfo.edu.dao.mapper.TblZipMapper",""),
	TmpZipCode("development","jp.co.aisinfo.edu.dao.mapper.TmpZipcodeMapper","");

	/**
	 * データベース名。
	 */
	private final String database;
	/**
	 * 自動生成マッパーのネームスペース
	 */
	private final String genNamespace;
	/**
	 * カスタムマッパーのネームスペース
	 */
	private final String customNamespace;

	/**
	 * コンストラクタ。
	 *
	 * @param database
	 *            データベース名
	 * @param genNamespace
	 *            自動生成マッパーのネームスペース
	 * @param customNamespace
	 *            カスタムマッパーのネームスペース
	 */
	Tbl(final String database, final String genNamespace, final String customNamespace) {
		this.database = database;
		System.out.println("database =>"+database);

		this.genNamespace = genNamespace + ".";
		this.customNamespace = customNamespace + ".";
	}

	/**
	 * DAOの取得。
	 *
	 * @return DAO
	 * @throws Exception
	 */
	public CommonDao getDAO() throws DBException {
		System.out.println("database --->"+database);
		return DAOFactory.getDAO(this.database);
	}

	/**
	 * データベース名の取得。
	 *
	 * @return データベース名
	 */
	public String getDatabase() {
		return this.database;
	}

	/**
	 * カスタムマッパーのフルsql-idの取得。
	 *
	 * @param sqlId
	 *            ネームスペース内のsql-id
	 * @return フルsql-id
	 */
	public String custom(final String sqlId) {
		return this.customNamespace + sqlId;
	}

	/**
	 * 自動生成マッパーのフルsql-idの取得。
	 *
	 * @param sqlId
	 *            ネームスペース内のsql-id
	 * @return フルsql-id
	 */
	public String gen(final String sqlId) {
		return this.genNamespace + sqlId;
	}
}