package jp.co.aisinfo.edu.exception;

/**
 * DBアクセスの例外
 *
 * @author ais-info
 */
public final class DBException extends Exception {

	/**
	 * 詳細メッセージを指定しないで DBException を構築します。
	 */
	public DBException() {
		super();
	}

	/**
	 * 指定された詳細メッセージと原因を使用して、DBExceptionを構築します。
	 *
	 * @param message
	 *            詳細メッセージ
	 * @param cause
	 *            原因
	 */
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された詳細メッセージを使用して、DBExceptionを構築します。
	 *
	 * @param message
	 *            詳細メッセージ
	 */
	public DBException(String message) {
		super(message);
	}

	/**
	 * DBアクセスで例外的条件が発生した場合にスローされます。
	 *
	 * @param cause
	 *            原因
	 */
	public DBException(Throwable cause) {
		super(cause);
	}
}
