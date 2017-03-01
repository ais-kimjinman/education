package jp.co.aisinfo.edu.common;

/**
 * DAOクラス内で共通に利用するクラス
 *
 * @author ais-info
 */
public class CommonDaoUtil {

	/*
	 * 呼び元クラス名、メソッド名を取得
	 *
	 * @return String クラス名.メソッド名
	 */
	public String getClassMethodName() {
		String[] sWork = null;
		String clsName = null;
		String mtdName = null;
		Throwable thrw = new Throwable();

		int maxLen = thrw.getStackTrace().length;
		if (maxLen >= 1) {
			int no = 1;
			for (; no < maxLen; no++) {
				if (thrw.getStackTrace()[no].getClassName().endsWith("Logic")) {
					break;
				}
			}
			if (no < maxLen) {
				sWork = thrw.getStackTrace()[no].getClassName().split("\\.");
				if (sWork.length >= 1) {
					clsName = sWork[sWork.length - 1];
				} else {
					clsName = null;
				}
				mtdName = thrw.getStackTrace()[no].getMethodName();
			}
		}
		return clsName + "." + mtdName;
	}

	/**
	 * 自分自身のクラス名、メソッド名を取得
	 *
	 * @return String クラス名.メソッド名
	 */
	public String getThisClassMethodName() {
		String[] sWork = null;
		String clsName = null;
		String mtdName = null;
		Throwable thrw = new Throwable();
		sWork = thrw.getStackTrace()[1].getClassName().split("\\.");
		if (sWork.length >= 1) {
			clsName = sWork[sWork.length - 1];
		} else {
			clsName = null;
		}
		mtdName = thrw.getStackTrace()[1].getMethodName();
		return clsName + "." + mtdName;
	}

	/**
	 * 初期化
	 *
	 * @param isBatch
	 *            バッチかどうか
	 */
	public static void init(final boolean isBatch) {
		// DAOFactory.init(isBatch);
	}

	/**
	 * commit
	 */
	public static void commit() throws Exception {
		// DAOFactory.commit();
	}

	/**
	 * rollback
	 */
	public static void rollback() throws Exception {
		// DAOFactory.rollback();
	}

	/**
	 * close
	 */
	public static void close() throws Exception {
		// DAOFactory.close();
	}

}
