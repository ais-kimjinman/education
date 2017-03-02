package jp.co.aisinfo.edu.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * CSVファイルの入出力サンプル
 *
 * @author ais-info
 */
public class csvFileReadWriteSample {

	private static Logger logger = LoggerFactory.getLogger("csvFileReadWriteSample");

	public static void main(String[] args) throws IOException {

		// 初期化
		String filePath = "C:\\AIS\\workspace\\education\\file\\sample\\";
		String outputFileName = "testOutput.csv";

		// ファイル書き込む実行
		csvFileWriter01(filePath + outputFileName);

		// ファイル読み込む実行
		csvFileReader01(filePath + outputFileName);

	}

	/**
	 * CSVファイル読み込む
	 *
	 * @param path
	 *            ファイルパス
	 * @throws IOException
	 */
	private static void csvFileReader01(String path) throws IOException {

		File filePath = new File(path);
		CSVReader reader = null;

		try {
			reader = new CSVReader(new FileReader(filePath));

			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				for (String item : nextLine) {
					logger.debug(item);
				}
				logger.debug("");
			}
			reader.close();
			logger.info("処理が正常に処理されました。");

		} catch (FileNotFoundException e) {
			logger.error("ファイルが存在しません。。:" + e);
			reader.close();
		} catch (IOException e) {
			logger.error("処理中例外が発生しました。:" + e);
			reader.close();
		} finally {
			reader.close();
		}
	}

	/**
	 * CSVファイル書き込む
	 *
	 * @param path
	 *            ファイルパス
	 * @throws IOException
	 */
	private static void csvFileWriter01(String path) throws IOException {

		File filePath = new File(path);
		FileOutputStream csvOut = null;
		Writer writer = null;
		CSVWriter csvWriter = null;

		try {
			List<String[]> csv = Arrays.asList(new String[] { "1行目テキスト①", "1行目テスト太郎", "111-999" },
					new String[] {"2行目テキスト②", "2行目テスト太郎", "222-999" });

			csvOut = new FileOutputStream(filePath);
			writer = new OutputStreamWriter(csvOut);
			csvWriter = new CSVWriter(writer);
			csvWriter.writeAll(csv);

			csvWriter.close();
			writer.close();
			csvOut.close();

			logger.info("処理が正常に処理されました。");

		} catch (IOException e) {

			logger.error("処理中例外が発生しました。:" + e);
			csvWriter.close();
			writer.close();
			csvOut.close();

		} finally {
			writer.close();
			csvOut.close();
		}

	}

}
