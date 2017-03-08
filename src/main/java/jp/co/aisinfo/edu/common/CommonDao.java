package jp.co.aisinfo.edu.common;

import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.aisinfo.edu.exception.DBException;

/**
 * 共通で使用するSQLセッションクラスです
 *
 * @author ais-info
 */
public class CommonDao {

	/**
	 * ログ出力
	 */
	private static Logger logger = LoggerFactory.getLogger("commonDao");

	/**
	 * セッションファクトリー
	 */
	private SqlSessionFactory sqlSessionFactory = null;
	/**
	 * プールしているSQLSession
	 */
	private Hashtable<String, SqlSession> sqlSessionMap = new Hashtable<>();
	/**
	 * environment設定
	 */
	private String environment = "development";
	/**
	 * SQLセッション
	 */
	private SqlSession sqlSession;

	public CommonDao(boolean isBatch, String environment) {
	}

	/**
	 * SQLSessionを開く
	 *
	 * @throws Exception
	 *             例外
	 */
	public void open() throws DBException {
		open(null);
	}

	/**
	 * SQLSessionを開く
	 *
	 * @param key
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。
	 * @throws Exception
	 *             例外
	 */
	public void open(String key) throws DBException {

		if (sqlSession == null) {
			logger.info(environment + "接続を開始します。");
			setSqlSession(sqlSession, key);
			sqlSession = getSqlSession(key);
		}else{
			setSqlSession(sqlSession, key);
			sqlSession = getSqlSession(key);
		}
	}

	/**
	 * SQLSessionをプールする
	 *
	 * @param sqlSession
	 *            オープンしたSQLSession
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             例外
	 */
	@SuppressWarnings("unused")
	private void setSqlSession() throws DBException {
		String configName = null;
		Reader reader = null;
		try {
			logger.debug("MyBatisConfigを読み込みます。");
			Resources.setCharset(Charset.forName("UTF-8"));
			configName = ConfigLoader.getParameter(Constant.MYBATIS_FILE_NAME, true);
			reader = Resources.getResourceAsReader(configName);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			logger.debug(environment + " SqlSessionFactoryを生成しました。");
		} catch (Exception e) {
			String message = e.getMessage();
			if (message != null && configName != null) {
				message = message + ":設定ファイル名＝[" + configName + "]";
			} else if (message == null && configName != null) {
				message = configName;
			}
			throw new DBException(message, e);
		}
	}

	/**
	 * SQLSessionをプールする
	 *
	 * @param sqlSession
	 *            オープンしたSQLSession
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             例外
	 */
	private void setSqlSession(SqlSession sqlSession, String sessionKey) throws DBException {
		String id = getKey(sessionKey);
		String configName = null;
		Reader reader = null;
		try {
			logger.debug("MyBatisConfigを読み込みます。");
			Resources.setCharset(Charset.forName("UTF-8"));
			configName = ConfigLoader.getParameter(Constant.MYBATIS_FILE_NAME, true);
			reader = Resources.getResourceAsReader(configName);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			SqlSession session = sqlSessionFactory.openSession();
			sqlSessionMap.put(id, session);
			logger.debug(environment + " SqlSessionFactoryを生成しました。");
		} catch (Exception e) {
			String message = e.getMessage();
			if (message != null && configName != null) {
				message = message + ":設定ファイル名＝[" + configName + "]";
			} else if (message == null && configName != null) {
				message = configName;
			}
			throw new DBException(message, e);
		}
	}

	/**
	 * プールしたSQLSessionを返す
	 *
	 * @return sqlSession オープンしたSQLSession
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             例外
	 */
	private SqlSession getSqlSession(String sessionKey) throws DBException {
		String id = sessionKey;
		if(sessionKey == null){
			id = getKey(sessionKey);
		}
		SqlSession sqlSession = sqlSessionMap.get(id);
		return sqlSession;
	}

	/**
	 * 管理用のキーを返す
	 *
	 * @param sessionKey
	 *            sessionKey 同一スレッドで別のSqlSessionにしたい場合に指定する。
	 * @return 管理用のキー
	 */
	private String getKey(String sessionKey) {
		String id = getThredId();
		if (sessionKey != null) {
			id = id + sessionKey;
		}
		return id;
	}

	/**
	 * スレッドIDを返す
	 *
	 * @return スレッドID
	 */
	private String getThredId() {
		Thread tread = Thread.currentThread();
		long id = tread.getId();
		return (Long.toString(id));
	}

	/**
	 * SQLSessionを閉じる
	 *
	 * @throws Exception
	 *             例外
	 */
	public void close() throws Exception {
		close(null);
	}

	/**
	 * SQLSessionを閉じる
	 *
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。
	 * @throws Exception
	 *             例外
	 */
	public void close(String sessionKey) throws Exception {
		SqlSession sqlSession = getSqlSession(sessionKey);
		if (sqlSession != null) {
			logger.info(environment + "接続 を切断します。");
			sqlSession.close();
			logger.info(environment + "接続 を切断しました。");
			sqlSession = null;
		}
		String id = getKey(sessionKey);
		sqlSessionMap.remove(id);
	}

	/**
	 * SQLSessionをロールバック
	 *
	 * @throws Exception
	 *             例外
	 */
	public void rollback() throws Exception {
		rollback(null);
	}

	/**
	 * SQLSessionをロールバック
	 *
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws Exception
	 *             例外
	 */
	public void rollback(String sessionKey) throws Exception {
		SqlSession sqlSession = getSqlSession(sessionKey);
		if (sqlSession != null) {
			logger.info(environment + "接続 でロールバックします。");
			sqlSession.rollback(true);
			logger.info(environment + "接続 でロールバックしました。");
			sqlSession = null;
		}
	}

	/**
	 * SQLSessionをコミット
	 *
	 * @throws Exception
	 *             例外
	 */
	public void commit() throws Exception {
		commit(null);
	}

	/**
	 * SQLSessionをコミット
	 *
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws Exception
	 *             例外
	 */
	public void commit(String sessionKey) throws Exception {
		SqlSession sqlSession = getSqlSession(sessionKey);
		if (sqlSession != null) {
			logger.info(environment + "接続 でコミットします。");
			sqlSession.commit(true);
			logger.info(environment + "接続 でコミットしました。");
			sqlSession = null;
		}
	}

	public int insert(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			// SQLの実行
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.insert(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * 登録のSQLを実行(レコード作成日時、レコード作成者、レコード作成PRG設定なし)
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int insertNoSet(String statement, Object parameter) throws DBException {
		return insertNoSet(statement, parameter, null);
	}

	/**
	 * 登録のSQLを実行(レコード作成日時、レコード作成者、レコード作成PRG設定なし)
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int insertNoSet(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			// SQLの実行
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.insert(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * 更新のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int update(String statement, Object parameter) throws DBException {
		return update(statement, parameter, null);
	}

	/**
	 * 更新のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int update(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			// SQLの実行
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.update(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	public Object selectOne(String statement, Object parameter) throws DBException {
		return selectOne(statement, parameter, null);
	}

	public Object selectOne(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.selectOne(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	public Object selectOne(String statement) throws DBException {
		return selectOne(statement, null);
	}

	public Object selectOne(String statement, String sessionKey) throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.selectOne(statement);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement) throws DBException {
		return selectList(statement, null);
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement, String sessionKey) throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			List result = sqlSession.selectList(statement);
			// nullレコード対策
			if (result.size() >= 1) {
				if (result.get(0) == null) {
					result = new ArrayList();
				}
			}
			return result;
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement, Object parameter) throws DBException {
		return selectList(statement, parameter, (String) null);
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			List result = sqlSession.selectList(statement, parameter);
			// nullレコード対策
			if (result.size() >= 1) {
				if (result.get(0) == null) {
					result = new ArrayList();
				}
			}
			return result;
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement, Object parameter, RowBounds rowBounds) throws DBException {
		return selectList(statement, parameter, rowBounds, null);
	}

	@SuppressWarnings("rawtypes")
	public List selectList(String statement, Object parameter, RowBounds rowBounds, String sessionKey)
			throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			List result = sqlSession.selectList(statement, parameter, rowBounds);
			// nullレコード対策
			if (result.size() >= 1) {
				if (result.get(0) == null) {
					result = new ArrayList();
				}
			}
			return result;
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public void select(String statement, Object parameter, ResultHandler handler, String sessionKey)
			throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			sqlSession.select(statement, parameter, handler);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * SQLを実行
	 *
	 * @param statement
	 *            実行するステートメント 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	@SuppressWarnings("rawtypes")
	public Cursor selectCursor(String statement) throws DBException {
		return selectCursor(statement, null, null);
	}

	/**
	 * SQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	@SuppressWarnings("rawtypes")
	public Cursor selectCursor(String statement, Object parameter) throws DBException {
		return selectCursor(statement, parameter, null);
	}

	/**
	 * SQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	@SuppressWarnings("rawtypes")
	public Cursor selectCursor(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.selectCursor(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * ヒントを付与してupdateを実行。 mapper.xml内で参照する場合、レコードは"record.",ヒントは"hint."で修飾します。
	 * where OshiraseID = #{record.oshiraseID,jdbcType=BIGINT} and UpdateRecDate
	 * = #{hint.updateRecDate, jdbcType=TIMESTAMP}
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param record
	 *            更新対象レコード
	 * @param hint
	 *            追加するヒント
	 * @return 更新件数
	 * @throws DBException
	 */
	public int updateWithHint(final String statement, final Object record, final Object hint) throws DBException {
		try {
			final Map<String, Object> parameter = new HashMap<String, Object>(3);
			parameter.put("record", record);
			parameter.put("hint", hint);
			// SQLの実行
			return getSqlSession(null).update(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * ヒントにタイムスタンプ値を付与してupdateを実行します。 更新日付は、"hint.updateRecDate"で参照できます。 where
	 * OshiraseID = #{record.oshiraseID,jdbcType=BIGINT} and UpdateRecDate =
	 * #{hint.updateRecDate, jdbcType=TIMESTAMP}
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param record
	 *            更新対象レコード
	 * @param expectedTimestamp
	 *            期待されるタイムスタンプ値
	 * @return 更新件数
	 * @throws DBException
	 */
	public int updateWithTimestamp(final String statement, final Object record, final Date expectedTimestamp)
			throws DBException {
		final HashMap<String, Object> hint = new HashMap<String, Object>(2);
		hint.put("updateRecDate", expectedTimestamp);
		return updateWithHint(statement, record, hint);
	}


	/**
	 * 削除のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int delete(String statement, Object parameter) throws DBException {
		return delete(statement, parameter, null);
	}

	/**
	 * 削除のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int delete(String statement, Object parameter, String sessionKey) throws DBException {
		try {
			// SQLの実行
			SqlSession sqlSession = getSqlSession(sessionKey);
			return sqlSession.delete(statement, parameter);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}

	/**
	 * 削除のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int deleteAll(String statement) throws DBException {
		return deleteAll(statement, null);
	}

	/**
	 * 削除のSQLを実行
	 *
	 * @param statement
	 *            実行するステートメント
	 * @param parameter
	 *            パラメータ
	 * @param sessionKey
	 *            同一スレッドで別のSqlSessionにしたい場合に指定する。 数字は設定不可とする
	 * @throws DBException
	 *             DB例外
	 * @return 処理結果
	 */
	public int deleteAll(String statement, String sessionKey) throws DBException {
		try {
			// SQLの実行
			SqlSession sqlSession = getSqlSession(sessionKey);
			System.out.println("deleteAll-->"+statement);
			return sqlSession.delete(statement);
		} catch (Exception ex) {
			throw new DBException(ex);
		}
	}


}