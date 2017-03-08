package jp.co.aisinfo.edu.common;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryManager {

	private static final SqlSessionFactory sqlSessionFactory;

	static {
		String resource = "jp/co/aisinfo/edu/query/mybatisConfig.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory  = new SqlSessionFactoryBuilder().build(reader);

		} catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
	}

	public SqlSessionFactoryManager(){}

	/**
	* @return SqlSessionFactory
	*/
	public static SqlSessionFactory getSqlMapper() {
		  return sqlSessionFactory;
	}

}
