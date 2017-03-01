package jp.co.aisinfo.edu.exception;

/**
 * バッチ処理の例外
 *
 * @author ais-info
 */
public final class BatchException extends Exception {

	/**
	 * 詳細メッセージを指定しないで BatchException を構築します。
	 */
	public BatchException() {
		super();
	}

	/**
	 * 指定された詳細メッセージと原因を使用して、BatchExceptionを構築します。
	 *
	 * @param message
	 *            詳細メッセージ
	 * @param cause
	 *            原因
	 */
	public BatchException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された詳細メッセージを使用して、BatchExceptionを構築します。
	 *
	 * @param message
	 *            詳細メッセージ
	 */
	public BatchException(String message) {
		super(message);
	}

	/**
	 * バッチ処理で例外的条件が発生した場合にスローされます。
	 *
	 * @param cause
	 *            原因
	 */
	public BatchException(Throwable cause) {
		super(cause);
	}

	public int getErrCode() {
		return 1;
	}
}
