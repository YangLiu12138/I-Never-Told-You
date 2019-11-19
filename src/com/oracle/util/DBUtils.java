package com.oracle.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class DBUtils {
	private static Connection conn;
	private static DatabaseMetaData dmd;
	private static String ip;
	private static String name;
	private static String pass;

	/**
	 * 连接数据库
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<String> joinDB(String ip, String name, String pass) throws SQLException, ClassNotFoundException {
		DBUtils.ip = ip;
		DBUtils.name = name;
		DBUtils.pass = pass;
		List<String> list = new ArrayList<String>();
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/?serverTimezone=UTC", name, pass);
		dmd = conn.getMetaData();
		ResultSet rs = dmd.getCatalogs();
		while (rs.next()) {
			String tname = rs.getString(1);
			list.add(tname);
		}
		return list;
	}

	/**
	 * 通过数据库名称生成java代码
	 * 
	 * @param dbname
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void scJavaCode(String dbname,String pname) throws SQLException, ClassNotFoundException, IOException {
		// 保存生成的一个个文件
		Map<String, String> map = new HashMap<String, String>();

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbname + "?serverTimezone=UTC", name,
				pass);
		dmd = conn.getMetaData();

		// 如何获得canyin这个数据库中有哪些表
		ResultSet rss = dmd.getTables(dbname, null, null, new String[] { "table" });
		// 结果集元数据
		ResultSetMetaData rsmds = rss.getMetaData();
		// 有几列
		int counts = rsmds.getColumnCount();
		while (rss.next()) {
			// 表示一个类的完整字符串
			StringBuffer sb = new StringBuffer();
			// 表名称，类名称
			sb.append("package "+pname+ ";\r\n");
			sb.append("\r\n");
			sb.append("public class " + toUpper(rss.getString("TABLE_NAME")) + "{\r\n");
			String sql = "select * from `" + rss.getString("TABLE_NAME") + "` ";
			Statement st = conn.createStatement();
			ResultSet rs3 = st.executeQuery(sql);
			ResultSetMetaData rd3 = rs3.getMetaData();
			int col3 = rd3.getColumnCount();
			sb.append("\t//属性列表\r\n");
			for (int i = 1; i <= col3; i++) {
				sb.append("\t" + "private\t" + changeType(rd3.getColumnTypeName(i)) + "\t" + rd3.getColumnLabel(i)
						+ ";\r\n");
			}

			for (int i = 1; i <= col3; i++) {
				sb.append("\t//setter\r\n");
				sb.append("\t" + "public void " + "set" + toUpper(rd3.getColumnLabel(i)) + "("
						+ changeType(rd3.getColumnTypeName(i)) + " " + rd3.getColumnLabel(i) + "){\r\n");

				sb.append("\t\tthis." + rd3.getColumnLabel(i) + " = " + rd3.getColumnLabel(i) + ";\r\n");

				sb.append("\t}\r\n");
				sb.append("\t//getter\r\n");
				sb.append("\t" + "public " + changeType(rd3.getColumnTypeName(i)) + " get"
						+ toUpper(rd3.getColumnLabel(i)) + "(){\r\n");
				sb.append("\t\t return " + rd3.getColumnLabel(i) + ";\r\n");
				sb.append("\t}\r\n");
			}

			sb.append("}\r\n");

			map.put(toUpper(rss.getString("TABLE_NAME")), sb.toString());

		}

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(1);
		int result = jfc.showOpenDialog(null);
//		System.out.println(result);
		if (result == 0) {
			File file = jfc.getSelectedFile();
			Set<String> javaNames = map.keySet();
			for (String javaClassname : javaNames) {
				File javafile = new File(file, javaClassname + ".java");
				FileOutputStream fos = new FileOutputStream(javafile);
				fos.write(map.get(javaClassname).getBytes());
				fos.close();
			}
		}

		JOptionPane.showMessageDialog(null, "生成成功！");

	}

	/**
	 * 将首字母转换为大写
	 * 
	 * @param val
	 * @return Order
	 */
	public static String toUpper(String val) {
		String f = val.substring(0, 1);
		String fu = f.toUpperCase();

		return fu + val.substring(1);
	}

	/**
	 * 转换类型
	 * 
	 * @param type
	 * @return
	 */
	public static String changeType(String type) {
		switch (type) {
		case "INT":
			return "int";
		case "DATE":
			return "java.util.Date";
		case "DATETIME":
			return "java.util.Date";
		case "VARCHAR":
			return "String";
		case "TEXT":
			return "String";

		default:
			return "Object";
		}
	}
}
