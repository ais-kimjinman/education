package jp.co.aisinfo.edu.batch.a10pg10b;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.aisinfo.edu.batch.BatchProcess;
import jp.co.aisinfo.edu.common.Constant;

/**
 * 郵便番号情報取込バッチ
 *
 * @author ais-info
 */
public class A10PG10B extends BatchProcess {

	/**
	 * ログの定義
	 */
	private static Logger logger = LoggerFactory.getLogger("A10PG10B");
	/**
	 * バッチ名
	 */
	private static final String BATCH_NAME = "郵便番号情報取込処理";

	/**
	 * 郵便番号情報取込処理実行
	 *
	 * @param args
	 *            WINDOWSスクリプトから入力パラメータ
	 */
	public static void main(final String[] args) {

		A10PG10B obj = new A10PG10B();
		// 当クラスを設定
		obj.setGymClass(A10PG10B.class);
		// 実行
		int ret = obj.execMain(args);
		// WINDOWSスクリプトへの返却値を設定
		System.exit(ret);

	}

	/**
	 * 業務主処理
	 *
	 * @param args
	 *            入力パラメータ
	 * @return 処理結果コード
	 * @throws Exception
	 *             例外
	 */
	protected final int mainProcess(String[] args) throws Exception {
		try {

			//主処理
			//TODO: ① ファイルパス作成


			//TODO: ② ファイルチェック


			//TODO: ③ 郵便番号マスタ登録

			//郵便番号マスタ登録
			//TODO: ① ファイルオープン


			//TODO: ② 郵便番号TEMPテーブルの初期化を行う。


			//TODO: ③ CSVファイル読込


			//TODO: ④ 郵便番号TEMPテーブル登録


			//TODO: ⑤ 郵便番号マスタテーブルの初期化を行う。


			//TODO: ⑥ 郵便番号マスタテーブル登録


			//TODO: ⑦ コミット


			//⑧ 処理終了
			logger.info(BATCH_NAME+"処理が正常終了しました。");
			return Constant.RET_CODE_OK;

		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

}