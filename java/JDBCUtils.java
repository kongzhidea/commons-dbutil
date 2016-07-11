package com.kk.mybatis.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JDBCUtils {

    private static String url = "jdbc:mysql://0.0.0.0:3306/eby_stat?useUnicode=true&amp;characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true";
    private static String user = "";
    private static String password = "";
    private static String driver = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, password); // jdbc配置
//        return new UnpooledDataSource(driver,url,user,password).getConnection(); //  mybatis 自带
//        return new PooledDataSource(driver,url,user,password).getConnection(); // mybatis 带连接池
        // commons-dbcp ，使用common-pool 连接池
        BasicDataSource database = new BasicDataSource();
        database.setDriverClassName(driver);
        database.setUrl(url);
        database.setUsername(user);
        database.setPassword(password);
        database.setInitialSize(10); // 线上配置
        database.setMaxActive(100);
        database.setDefaultAutoCommit(true);
        database.setTimeBetweenEvictionRunsMillis(3600000);
        database.setMinEvictableIdleTimeMillis(3600000);
        return database.getConnection();
    }

    public static void free(Statement st, Connection conn) {
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {

            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            free(st, conn);
        }
    }

    /**
     * Statement
     */
    public static void testQuery() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String sql = "select id,username,password,realname from eby_user";
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ",");// index从1开始
                System.out.print(rs.getString(2) + ",");
                System.out.print(rs.getString("password") + ",");// 使用name的方式
                System.out.println(rs.getString("realname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(rs, stmt, conn);
        }
    }

    /**
     * PreparedStatement
     */
    public static void testQuery2() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select id,name,sex,age from student";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ",");// index从1开始
                System.out.print(rs.getString(2) + ",");
                System.out.print(rs.getString("sex") + ",");// 使用name的方式
                System.out.println(rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(rs, ps, conn);
        }
    }

    public static void main(String[] args) {
        testQuery();
    }

}






package kk.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JDBCUtils {

	private static String url = "jdbc:mysql://10.2.45.39:3306/test?useUnicode=true&characterEncoding=utf-8";
	private static String user = "purchase";
	private static String password = "agent";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void free(Statement st, Connection conn) {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void free(ResultSet rs, Statement st, Connection conn) {
		try {

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			free(st, conn);
		}
	}

	/**
	 * Statement
	 */
	public static void testQuery() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select id,name,sex,age from student";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.print(rs.getInt(1) + ",");// index从1开始
				System.out.print(rs.getString(2) + ",");
				System.out.print(rs.getString("sex") + ",");// 使用name的方式
				System.out.println(rs.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.free(rs, stmt, conn);
		}
	}

	/**
	 * PreparedStatement
	 */
	public static void testQuery2() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select id,name,sex,age from student";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.print(rs.getInt(1) + ",");// index从1开始
				System.out.print(rs.getString(2) + ",");
				System.out.print(rs.getString("sex") + ",");// 使用name的方式
				System.out.println(rs.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.free(rs, ps, conn);
		}
	}

	/**
	 * Statement
	 */
	public static void testUpdate() {
		Connection conn = null;
		Statement stmt = null;

		String sql = "update student set name='孔' where id=1";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			int ret = stmt.executeUpdate(sql);
			System.out.println("update:" + ret);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.free(stmt, conn);
		}
	}

	/**
	 * PreparedStatement
	 */
	public static void testUpdate2() {
		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "update student set name='孔' where id=1";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			int ret = ps.executeUpdate();
			System.out.println("update:" + ret);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.free(ps, conn);
		}
	}

	/**
	 * PreparedStatement
	 */
	public static void testInsert2() {
		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "insert into student(name,sex,age) values(?,?,?)";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "孔1");
			ps.setString(2, "男");
			ps.setInt(3, 21);
			int ret = ps.executeUpdate();
			System.out.println("update:" + ret);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.free(ps, conn);
		}
	}

	public static void main(String[] args) {
		// testQuery();
//		testQuery2();
		// testUpdate();
//		testUpdate2();
//		testInsert2();
	}

}

/**
Blob:
java.sql.PreparedStatement preparedStatement = 
conn.prepareStatement("insert into administrator.emp_photo values('000180','JPG',?)");
//创建文件对象:
File file=new File("BNet.jpg");
// 创建流对象:
BufferedInputStream imageInput = new BufferedInputStream(new FileInputStream(file));
//参数赋值:
preparedStatement.setBinaryStream(1, imageInput,(int) file.length());
//执行语句
preparedStatement.executeUpdate();
..................
  /*
   //Blob的读取工作:
   Statement st=conn.createStatement();
   ResultSet rs=st.executeQuery("select picture from Administrator.emp_photo");
   int i=0;
   while(rs.next())
   {
    //读取Blob对象
    Blob blob= (Blob) rs.getBlob(1);
    //Blob对象转化为InputStream流
    InputStream inputStream =blob.getBinaryStream();
	
    //要写入的文件
    File fileOutput = new File("back"+i+".jpg");
	i++;
    //文件的写入流的定义
    FileOutputStream fo = new FileOutputStream(fileOutput);
    int c;
    //读取流并写入到文件中
    while ((c = inputStream.read()) != -1)
    fo.write(c);
    //流的关闭:
    fo.close();
*/
