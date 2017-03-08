package jp.co.aisinfo.edu.common;

import jp.co.aisinfo.edu.exception.DBException;

/**
 * commit,rollback,closeを禁止するラッパー。
 *
 * @author ais-info
 */
public class WrappedDao extends CommonDao {

	public WrappedDao(final boolean isBatch, final String environment) throws DBException {
		super(isBatch, environment);
		System.out.println("WrappedDao isBatch:"+isBatch+":environment:"+environment);
    }

	public void realClose() throws Exception {
		super.close(null);
	}

	public void realCommit() throws Exception {
		super.commit(null);
	}

	public void realRollback() throws Exception {
		super.rollback(null);
	}

	@Override
	public void close() throws DBException {
		throw new UnsupportedOperationException("closeは呼び出せません。");
	}

	@Override
	public void close(final String sessionKey) throws DBException {
		throw new UnsupportedOperationException("closeは呼び出せません。");
	}

	@Override
	public void rollback() throws Exception {
		throw new UnsupportedOperationException("rollbackは呼び出せません。");
	}

	@Override
	public void rollback(final String sessionKey) throws Exception {
		throw new UnsupportedOperationException("rollbackは呼び出せません。");
	}

	@Override
	public void commit() throws Exception {
		throw new UnsupportedOperationException("commitは呼び出せません。");
	}

	@Override
	public void commit(final String sessionKey) throws Exception {
		throw new UnsupportedOperationException("commitは呼び出せません。");
	}

	@Override
	public void open() throws DBException {
		super.open(null);
	}

	@Override
	public void open(final String key) throws DBException {
		throw new UnsupportedOperationException("キー付openは呼び出せません。");
	}

}
