package jp.co.aisinfo.edu.batch.a10pg10b;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import jp.co.aisinfo.edu.batch.BatchProcess;
import jp.co.aisinfo.edu.common.CommonDaoUtil;
import jp.co.aisinfo.edu.common.ConfigLoader;
import jp.co.aisinfo.edu.common.Constant;
import jp.co.aisinfo.edu.common.Tbl;
import jp.co.aisinfo.edu.dao.model.TblZip;
import jp.co.aisinfo.edu.dao.model.TmpZipcode;
import jp.co.aisinfo.edu.exception.DBException;
import jp.co.aisinfo.edu.util.StringUtils;

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
	 *
	 */
	private static final String INPUT_FILE_NAME = "zip.temp.file.name";

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
	@SuppressWarnings("resource")
	protected final int mainProcess(String[] args) throws Exception {

		// 初期化
		InputStream input = null;
		InputStreamReader ireader = null;
		CSVReader reader = null;

		// ファイル読込件す
		int dataReadCnt = 0;
		// DB登録失敗件数
		int dataRegOKCnt = 0;
		// DB登録成功件数
		int dataRegNGCnt = 0;
		// 移管件数
		int dataIkanCnt = 0;

		try {
			// 主処理
			// ① ファイルパス作成
			String filePath = ConfigLoader.getParameter(Constant.INPUT_FILE_PATH);
			logger.debug("ファイルパス：" + filePath);
			// ファイルパスをチェック
			if (StringUtils.isEmpty(filePath)) {
				logger.error("ファイルパスが存在しません。");
				return Constant.RET_CODE_NG;
			}
			// ファイル名をチェック
			String fileName = ConfigLoader.getParameter(INPUT_FILE_NAME);
			logger.debug("ファイル名：" + fileName);
			if (StringUtils.isEmpty(fileName)) {
				logger.error("ファイルが存在しません。");
				return Constant.RET_CODE_NG;
			}

			String path = filePath + fileName;
			logger.debug("対象ファイル：" + path);

			// TODO: ファイルデータ取得とチェック
			// ファイル存在チェック:処理なし(Skip)

			// 郵便番号マスタ登録
			// 郵便番号TEMPテーブルの初期化を行う。
			deleteTempZipTbl();

			// CSVファイル読込
			input = new FileInputStream(path);
			ireader = new InputStreamReader(input, Constant.FILE_ENCODE_SJIS);
			reader = new CSVReader(ireader);

			// 行単位の読込み
			String[] nextLine = null;

			while ((nextLine = reader.readNext()) != null) {
				dataReadCnt++;
				// 郵便番号TEMPテーブル登録
				if (insertZipTbl(nextLine)) {
					logger.debug("TEMPテーブル登録処理OK");
					dataRegOKCnt++;
				} else {
					logger.debug("TEMPテーブル登録処理NG");
					dataRegNGCnt++;
				}

			}

			// 郵便番号マスタ移管処理を行う。
			// 郵便番号マスタテーブルの初期化を行う。
			deleteZipTbl();

			// 郵便番号マスタテーブル登録
			@SuppressWarnings("unchecked")
			Cursor<TmpZipcode> tmpZipList = (Cursor<TmpZipcode>) Tbl.TmpZipCode.getDAO()
					.selectCursor(Tbl.TmpZipCode.gen("selectCursor"));

			for (TmpZipcode item : tmpZipList) {
				// 移管処理を行う。
				if (moveZipTbl(item)) {
					dataIkanCnt++;
				}
			}

			// コミット
			CommonDaoUtil.commit();

			logger.info("データ取得件数：" + dataReadCnt);
			logger.info("登録正常件数：" + dataRegOKCnt);
			logger.info("登録エラー件数：" + dataRegNGCnt);
			logger.info("移管データ件数：" + dataIkanCnt);

			// 処理終了
			logger.info(BATCH_NAME + "処理が正常終了しました。");
			// 結果返却
			return Constant.RET_CODE_OK;

		} catch (Exception e) {
			input.close();
			ireader.close();
			reader.close();

			CommonDaoUtil.rollback();
			logger.debug(BATCH_NAME + "処理中エラーが発生しました。");

			throw e;
		} finally {
			input.close();
			ireader.close();
			reader.close();
		}
	}

	/**
	 * TEMPテーブルの初期化
	 *
	 * @throws DBException
	 */
	private final static void deleteTempZipTbl() throws DBException {

		@SuppressWarnings("unused")
		int result = Tbl.TmpZipCode.getDAO().deleteAll(Tbl.TmpZipCode.gen("deleteByAll"));
		logger.debug("郵便番号TEMPテーブルの初期化処理が正常に完了しました。");

	}

	/**
	 * TEMPテーブルに登録処理
	 *
	 * @param item
	 * @return 登録処理結果(true/false)
	 * @throws DBException
	 */
	private final static boolean insertZipTbl(String[] lineData) throws DBException {

		boolean result = false;

		TmpZipcode params = new TmpZipcode();
		params.setJiscode(lineData[0]);
		params.setCitycode(lineData[1]);
		params.setZipold(lineData[2]);
		params.setPrefkana(lineData[3]);
		params.setCitykana(lineData[4]);
		params.setAreakana(lineData[5]);
		params.setPref(lineData[6]);
		params.setCity(lineData[7]);
		params.setArea(lineData[8]);
		params.setFlag1(Short.parseShort(lineData[9]));
		params.setFlag2(Short.parseShort(lineData[10]));
		params.setFlag3(Short.parseShort(lineData[11]));
		params.setFlag4(Short.parseShort(lineData[12]));
		params.setFlag5(Short.parseShort(lineData[13]));
		params.setFlag6(Short.parseShort(lineData[14]));

		int retCnt = Tbl.TmpZipCode.getDAO().insertNoSet(Tbl.TmpZipCode.gen("insertSelective"), params);

		if (retCnt > 0) {
			result = true;
		}

		return result;
	}

	/**
	 * 郵便番号マスタテーブルの初期化
	 *
	 * @throws DBException
	 */
	private final static void deleteZipTbl() throws DBException {

		@SuppressWarnings("unused")
		int result = Tbl.TblZip.getDAO().deleteAll(Tbl.TblZip.gen("deleteByAll"));
		logger.debug("郵便番号マスタテーブルの初期化処理が正常に完了しました。");

	}

	/**
	 * 郵便番号マスタテーブル登録処理
	 *
	 * @param tmpData
	 * @return 登録処理結果(true/false)
	 * @throws DBException
	 */
	private final static boolean moveZipTbl(TmpZipcode tmpData) throws DBException {

		boolean result = false;

		TblZip params = new TblZip();
		params.setPrefcode(tmpData.getJiscode().trim().substring(0, 2));
		params.setArea(tmpData.getArea().trim());
		params.setAreakana(tmpData.getAreakana().trim());
		params.setCity(tmpData.getCity().trim());
		params.setCitycode(Integer.parseInt(tmpData.getCitycode().trim()));
		params.setCitykana(tmpData.getCitykana().trim());
		params.setPref(tmpData.getPref().trim());
		params.setPrefkana(tmpData.getPrefkana().trim());
		params.setZip7(tmpData.getZipold().trim());

		int retCnt = Tbl.TblZip.getDAO().insertNoSet(Tbl.TblZip.gen("insertSelective"), params);

		if (retCnt > 0) {
			result = true;
		}

		return result;
	}

}