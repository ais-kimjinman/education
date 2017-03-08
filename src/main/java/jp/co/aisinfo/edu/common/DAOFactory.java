package jp.co.aisinfo.edu.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.aisinfo.edu.exception.DBException;

/**
 * CommonDAOを生成するためのFactoryクラス
 *
 * @author ais-info
 */
public class DAOFactory {

	/**
	 * 環境省略時キー
	 */
	public static final String DEFAULT = "development";

	/**
	 * バッチフラグ。
	 */
	private static boolean isBatch = true;

	/**
	 * 大域キャッシュ。
	 */
	 private static final HashMap<String, WrappedDao> DAO_ON_GLOBAL = new HashMap<>();

	/**
	 * スレッド上で取得され、closeされていないDAOインスタンス。
	 */
	 private static final ThreadLocal<Map<String, WrappedDao>> DAO_ON_THREAD =  new ThreadLocal<>();

	/**
	 * コンストラクタ 本クラスのインスタンスを作成させないため、
	 * privateのコンストラクタ
	 */
	private DAOFactory() {
	}

	/**
	 * 初期化処理
	 *
	 * @param isBatch
	 *            バッチか否か
	 */
	protected static void init(final boolean isBatch) {
		DAOFactory.isBatch = isBatch;
	}

	/**
	 * デフォルトの共通DAOを取得スレッド上でDAOを管理します。
	 * 返却されたDAOをcommit,rollback,closeしてはいけません。
	 * 返却されたDAOは、open済みです。
	 *
	 * @return CommonDAOインスタンス
	 * @throws Exception
	 *             初期化例外
	 */
	public static CommonDao getDAO() throws Exception {
		return getDAO(DEFAULT);
	}

	/**
	 * 指定した環境の共通DAOを取得。
	 * スレッド上でDAOを管理します。
	 * 返却されたDAOをcommit,rollback,closeしてはいけません。 返却されたDAOは、open済みです。
	 *
	 * @return CommonDAOインスタンス
	 * @throws Exception
	 *             初期化例外
	 */
	public static CommonDao getDAO(final String environment) throws DBException {

		WrappedDao dao = getDaoOnThread(environment);
		if (dao != null) {
			return dao;
		}
		synchronized (DAO_ON_GLOBAL) {
			System.out.println("getDAO#1-DAO_ON_GLOBAL:"+DAO_ON_GLOBAL.size());
			dao = DAO_ON_GLOBAL.get(environment);
			if (dao == null) {
				dao = new WrappedDao(true, "development");
				DAO_ON_GLOBAL.put(environment, dao);
			}
		}
		dao.open();
		setDaoOnThread(environment, dao);
		return dao;
	}

	/**
	 * DAO操作インタフェース。
	 *
	 * @author demo.local
	 *
	 */
	private interface DAOOperation {
		void apply(WrappedDao dao) throws Exception;
	}

	/**
	 * スレッド上のDAOを列挙して処理します。
	 *
	 * @param func
	 *            処理関数
	 * @throws Exception
	 */
	private static void operation(final DAOOperation func) throws Exception {
		final Map<String, WrappedDao> daos = DAO_ON_THREAD.get();
		if (daos == null) {
			return;
		}
		Exception firstException = null;
		for (final WrappedDao dao : daos.values()) {
			try {
				func.apply(dao);
			} catch (final Exception ex) {
				if (firstException == null) {
					firstException = ex;
				}
			}
		}
		if (firstException != null) {
			throw firstException;
		}
	}

	/**
	 * スレッド上で{@link #getDAO()},{@link #getDAO(String)}により取得した全てのDAOをcommitします。
	 * スレッド上にDAOが存在しない場合、何もしません。
	 *
	 * @throws Exception
	 */
	protected static void commit() throws Exception {
		operation(WrappedDao::realCommit);
	}

	/**
	 * スレッド上で{@link #getDAO()},{@link #getDAO(String)}により取得した全てのDAOをrollbackします。
	 * スレッド上にDAOが存在しない場合、何もしません。
	 *
	 * @throws Exception
	 */
	protected static void rollback() throws Exception {
		operation(WrappedDao::realRollback);
	}

	/**
	 * スレッド上で{@link #getDAO()},{@link #getDAO(String)}により取得した全てのDAOをcloseし、スレッド上から取り除きます。
	 * スレッド上にDAOが存在しない場合、何もしません。
	 *
	 * @throws Exception
	 */
	protected static void close() throws Exception {
		try {
			operation(WrappedDao::realClose);
		} finally {
			DAO_ON_THREAD.remove();
		}
	}

	/**
	 * スレッド上のDAOを取得。
	 *
	 * @param key
	 * @return
	 */
	private static WrappedDao getDaoOnThread(final String key) {
		final Map<String, WrappedDao> daos = DAO_ON_THREAD.get();
		return daos == null ? null : daos.get(key);
	}

	/**
	 * スレッド上にDAOを設定。
	 *
	 * @param key
	 * @param dao
	 */
	private static void setDaoOnThread(final String key, final WrappedDao dao) {
		Map<String, WrappedDao> daos = DAO_ON_THREAD.get();
		if (daos == null) {
			daos = new LinkedHashMap<>();
			DAO_ON_THREAD.set(daos);
		}
		daos.put(key, dao);
	}

}
