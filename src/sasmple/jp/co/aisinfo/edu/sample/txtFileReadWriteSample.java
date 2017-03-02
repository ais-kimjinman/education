package jp.co.aisinfo.edu.sample;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.aisinfo.edu.util.FileUtils;

/**
 * テキストファイルの入出力サンプル
 *
 * @author ais-info
 */
public class txtFileReadWriteSample {

	private static Logger logger = LoggerFactory.getLogger("txtFileReadWriteSample");

	/*
	 * テキストファイルの入出力 「FileWriter」はファイルへの文字単位の出力を行うクラスです。
	 * Unicodeで管理されている文字データをOSのデフォルトの文字コードに変換し、指定したファイルへ書き込みを行います。
	 */

	public static void main(String[] args) throws IOException {

		//初期化
		String filePath = "C:\\AIS\\workspace\\education\\file\\sample\\";
		String outputFileName = "testOutput.txt";

		//ファイル書き込む実行
		txtFileWriter01(filePath + outputFileName);

		//ファイル読み込む実行
		txtFileReader01(filePath + outputFileName);

        InputStream input = new BufferedInputStream( new FileInputStream(filePath + outputFileName));

        String str = FileUtils.read(input);
        logger.debug("\n"+str);

	}

	/**
	 * テキストファイル書き込む
	 *
	 * @param path
	 *            ファイルパス
	 * @throws IOException
	 */
	private static void txtFileReader01(String path) throws IOException {

		File filePath = new File(path);
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filePath));

			String str = br.readLine();
			while (str != null) {
				logger.debug(str);
				str = br.readLine();
			}
			br.close();
			logger.info("処理が正常に処理されました。");

		} catch (FileNotFoundException e) {
			logger.error("ファイルが存在しません。。:" + e);
		} catch (IOException e) {
			logger.error("処理中例外が発生しました。:" + e);
			br.close();
		} finally {
			br.close();
		}
	}

	/**
	 * テキストファイル読み込む(PrintWriterクラスを使用)
	 *
	 * @param path
	 *            ファイルパス
	 * @throws IOException
	 */
	private static void txtFileWriter01(String path) throws IOException {

		// FileWriterクラスはOutputStreamクラスを継承しています。
		// このクラスはファイルへの出力を行います。

		File filePath = new File(path);
		// オブジェクト生成（出力ファイルの指定）
		FileWriter filewriter = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;

		try {

			// ファイル存在チェック
			if (!FileUtils.checkBeforeFile(filePath)) {
				// ファイルを作成
				filePath.createNewFile();
			}

			// ファイル出力を行う
			filewriter = new FileWriter(filePath);
			bw = new BufferedWriter(filewriter);
			pw = new PrintWriter(bw);

			pw.println("開始テスト文字列");
			pw.println(10);
			pw.println("終了テスト文字例");

			// ファイルを閉じる
			pw.close();
			bw.close();
			filewriter.close();

			logger.info("処理が正常に処理されました。");

		} catch (IOException e) {
			logger.error("処理中例外が発生しました。:" + e);

		} finally {

			// ファイルを閉じる
			pw.close();
			bw.close();
			filewriter.close();

		}

	}

}
