package com.oracle.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.oracle.util.DBUtils;
import java.awt.Font;

public class ToolFrame extends JFrame {

	private DBUtils db = new DBUtils();
	private JTextField txtPackage;

	public ToolFrame() {
		// 绝对布局
		getContentPane().setLayout(null);
		// 设置大小
		setSize(484, 458);
		// 设置标题
		setTitle("java生成实体类工具");
		// ip提示
		JLabel lblIp = new JLabel("IP：");
		lblIp.setFont(new Font("黑体", Font.PLAIN, 20));
		lblIp.setLocation(23, 26);
		lblIp.setSize(89, 30);
		getContentPane().add(lblIp);

		JTextField txtIp = new JTextField("localhost");
		txtIp.setFont(new Font("黑体", Font.PLAIN, 20));
		txtIp.setLocation(172, 22);
		txtIp.setSize(220, 30);
		getContentPane().add(txtIp);

		// name提示
		JLabel lblName = new JLabel("NAME：");
		lblName.setFont(new Font("黑体", Font.PLAIN, 20));
		lblName.setLocation(30, 66);
		lblName.setSize(87, 30);
		getContentPane().add(lblName);

		JTextField txtName = new JTextField("root");
		txtName.setFont(new Font("黑体", Font.PLAIN, 20));
		txtName.setLocation(174, 69);
		txtName.setSize(220, 30);
		getContentPane().add(txtName);

		// pass提示
		JLabel lblPass = new JLabel("PASS：");
		lblPass.setFont(new Font("黑体", Font.PLAIN, 20));
		lblPass.setLocation(28, 120);
		lblPass.setSize(74, 30);
		getContentPane().add(lblPass);

		JTextField txtPass = new JTextField("root");
		txtPass.setFont(new Font("黑体", Font.PLAIN, 20));
		txtPass.setLocation(173, 118);
		txtPass.setSize(220, 30);
		getContentPane().add(txtPass);

		// 数据库名称
		JLabel lblDB = new JLabel("请选择：");
		lblDB.setFont(new Font("黑体", Font.PLAIN, 20));
		lblDB.setLocation(25, 181);
		lblDB.setSize(94, 30);
		getContentPane().add(lblDB);

		JComboBox<String> com = new JComboBox<String>();
		com.setFont(new Font("黑体", Font.PLAIN, 20));
		com.setSize(220, 30);
		com.setLocation(175, 176);
		getContentPane().add(com);

		// 链接数据库
		JButton btn2 = new JButton("连接数据库");
		btn2.setSize(120, 30);
		btn2.setLocation(32, 298);
		getContentPane().add(btn2);
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 取值
				String name = txtName.getText();
				String pass = txtPass.getText();
				String ip = txtIp.getText();
				// 调用
				try {
					List<String> list = db.joinDB(ip, name, pass);
					for (String string : list) {
						com.addItem(string);
					}
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				// 显示结果
			}
		});

		// 一键生成
		JButton btn1 = new JButton("一键生成");
		btn1.setSize(120, 30);
		btn1.setLocation(185, 298);
		getContentPane().add(btn1);
		
		JLabel label = new JLabel("请输入包名：");
		label.setFont(new Font("黑体", Font.PLAIN, 20));
		label.setBounds(15, 245, 138, 30);
		getContentPane().add(label);
		
		txtPackage = new JTextField("com.oracle.entity");
		txtPackage.setFont(new Font("黑体", Font.PLAIN, 20));
		txtPackage.setBounds(176, 238, 220, 30);
		getContentPane().add(txtPackage);
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String sel = com.getSelectedItem().toString();
				String packagename=txtPackage.getText();
				// 获得数据库中所有的表

				// 获得表中的列和数据类型

				// 将以上信息拼接为java代码

				// 将java代码生成文件 输出即可
				try {
					db.scJavaCode(sel,packagename);
				} catch ( Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());

				}
			}
		});

		// 不允许改变大小
		setResizable(false);
		// 显示在屏幕中央
		setLocationRelativeTo(null);

	}
}
