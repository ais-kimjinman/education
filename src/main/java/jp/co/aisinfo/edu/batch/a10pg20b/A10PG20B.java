package jp.co.aisinfo.edu.batch.a10pg20b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

import jp.co.aisinfo.edu.batch.BatchProcess;
import jp.co.aisinfo.edu.common.ConfigLoader;
import jp.co.aisinfo.edu.common.Constant;
import jp.co.aisinfo.edu.common.Tbl;
import jp.co.aisinfo.edu.dao.model.TblZip;
import jp.co.aisinfo.edu.util.StringUtils;

/**
 * 郵便番号ファイル作成処理
 *
 * @author ais-info
 */
public class A10PG20B extends BatchProcess {

	/**
	 * ログの定義
	 */
	private static Logger logger = LoggerFactory.getLogger("A10PG20B");
	/**
	 * バッチ名
	 */
	private static final String BATCH_NAME = "郵便番号ファイル作成処理";
	/**
	 *ファイル名定数
	 */
	private static final String OUTPUT_FILE_NAME = "zip.out.file.name";

	/**
	 * 郵便番号情報取込処理実行
	 *
	 * @param args
	 *            WINDOWSスクリプトから入力パラメータ
	 */
	public static void main(final String[] args) {

		A10PG20B obj = new A10PG20B();
		// 当クラスを設定
		obj.setGymClass(A10PG20B.class);
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

		// 初期化
        Writer iowriter = null;
        CSVWriter writer = null;

		// 処理件数
		int wirteCnt = 0;


		try {
			// 主処理
			// ① ファイルパス作成
			String filePath = ConfigLoader.getParameter(Constant.OUTPUNT_FILE_PATH);
			logger.debug("ファイルパス：" + filePath);

			// ファイルパスをチェック
			if (StringUtils.isEmpty(filePath)) {
				logger.error("ファイルパスが存在しません。");
				return Constant.RET_CODE_NG;
			}
			// ファイル名をチェック
			String fileName = ConfigLoader.getParameter(OUTPUT_FILE_NAME);
			String path = filePath + fileName;
			logger.debug("対象ファイル：" + path);

			File csvPath = new File(path);
			iowriter = new OutputStreamWriter(new FileOutputStream(csvPath), Constant.FILE_ENCODE_SJIS);
            writer = new CSVWriter(iowriter, ',', CSVWriter.NO_QUOTE_CHARACTER);

			// 郵便番号マスタテーブル取得
			@SuppressWarnings("unchecked")
			Cursor<TblZip> TblZipList = (Cursor<TblZip>) Tbl.TblZip.getDAO()
					.selectCursor(Tbl.TblZip.gen("selectCursor"));

			for (TblZip item : TblZipList) {

				// レコード別の処理
				String[] entries = new String[]{
						 item.getZipSeq().toString(),
						 item.getPrefcode(),
						 item.getCitycode().toString(),
						 item.getZip7(),
						 item.getPrefkana(),
						 item.getCitykana(),
						 item.getAreakana(),
						 item.getPref(),
						 item.getCity(),
						 item.getArea()
				 };
				 writer.writeNext(entries);
				 wirteCnt++;
			}

			// 処理終了
			logger.info(wirteCnt+"件を作成しました。");
			logger.info(BATCH_NAME + "処理が正常終了しました。");

			// 結果返却
			return Constant.RET_CODE_OK;

		} catch (Exception e) {
			writer.close();
			logger.debug(BATCH_NAME + "処理中エラーが発生しました。");
			throw e;
		} finally {
			writer.close();

		}
	}

}