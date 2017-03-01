package jp.co.aisinfo.edu.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.aisinfo.edu.common.CommonDaoUtil;
import jp.co.aisinfo.edu.common.ConfigLoader;
import jp.co.aisinfo.edu.common.Constant;
import jp.co.aisinfo.edu.exception.BatchException;
import jp.co.aisinfo.edu.exception.DBException;

/**
 * バッチ共通処理クラス
 *
 * @author ais-info
 */
public abstract class BatchProcess {

	/**
	 * ログの定義
	 */
	private static Logger comLog = LoggerFactory.getLogger("BatchProcess");

	/**
	 * 業務クラス
	 */
	@SuppressWarnings("rawtypes")
	private Class gymClass = null;

	/**
	 * バッチ共通処理
	 *
	 * @param args
	 *            Windowsスクリプトからの入力パラメータ
	 * @return 処理結果コード
	 */
	protected int execMain(String[] args) {

		// 戻り値
		int intRet = Constant.ERR_CODE_OTHER;
		try {
			// 開始ログ(メソッド名＋入力パラメータ)
			comLog.info("START:" + gymClass.getName() + makeInparamLog(args));
			// バッチ共通初期処理
			initProcess();
			// 業務個別処理
			intRet = mainProcess(args);

		} catch (BatchException e) {
			// BatchException発生
			intRet = e.getErrCode();
			// エラーログ
			comLog.error("BatchExceptionエラー発生", e);

		} catch (DBException e) {
			// DBException発生
			intRet = Constant.RET_CODE_NG;
			// エラーログ
			comLog.error("DBExceptionエラー発生", e);

		} catch (Throwable e) {
			// Exception発生
			intRet = Constant.RET_CODE_NG;
			// エラーログ
			comLog.error("Throwableエラー発生", e);

		} finally {
			// バッチ共通終期処理
			finalProcess();
			// 終了ログ
			comLog.info("END:" + gymClass.getName() + ":ret=" + String.valueOf(intRet));
		}
		return intRet;
	}

	/**
	 * 業務個別処理
	 *
	 * @param args
	 *            Windowsスクリプトからの入力パラメータ
	 * @return 処理結果コード
	 * @throws Exception
	 *             例外
	 */
	protected abstract int mainProcess(final String[] args) throws Exception;

	/**
	 * ログ用入力パラメータ作成
	 * @param args Windowsスクリプトからの入力パラメータ
	 * @return 入力パラメータ文字列
     */
    private String makeInparamLog(final String[] args) {
    	StringBuilder buf = new StringBuilder(":");
    	if (args ==  null || args.length == 0) {
            return buf.toString();
        }
    	for (int i = 0; i < args.length; i++) {
            if (i != 0) {
                buf.append(",");
            }
            buf.append("[");
            buf.append(args[i]);
            buf.append("]");
        }
        return buf.toString();
    }

	/**
	 * バッチ共通初期処理 ・SqlSeccionのオープン ・アプリケーション定義ファイルの取得
	 *
	 * @throws Exception 例外
	 */
	private void initProcess() throws Exception {
		// 設定ファイルの読み込み
		readConfigFile();
		// CommonDAO初期化：バッチ用
		CommonDaoUtil.init(true);
		// SqlSessionのオープン
		//DAOFactory.getDAO().open();
	}

	/**
	 * 設定ファイルの読み込み
	 *
	 * @throws Exception
	 *             例外
	 */
	private void readConfigFile() throws Exception {
		String path = "";
		try {
			path = System.getProperty(Constant.CONFIG_PATH);
			// 設定ファイルの読み込み
			ConfigLoader.init(path);
		} catch (Exception e) {
			comLog.error("設定ファイル読み込み中にエラーが発生しました[" + path + "]");
			throw e;
		}
	}

	/**
	 * バッチ共通終了処理 ・SqlSeccionのクローズ
	 */
	private void finalProcess() {
		// SqlSessionのクローズ
		try {
			CommonDaoUtil.close();
		} catch (Exception e) {
			comLog.error("CommonDAO.close()でエラーが発生しました", e);
		}
	}

	/**
	 * 業務クラスを設定する。
	 *
	 * @param cls クラス
	 */
	@SuppressWarnings("rawtypes")
	public final void setGymClass(final Class cls) {
		this.gymClass = cls;
	}

}