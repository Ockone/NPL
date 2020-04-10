package main;

import summary.TextRankKeyword;
import summary.TextRankSentence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NPL extends JFrame{
    //定义组件
    //上部组件
    JPanel jp0;
    JPanel jp1;		//定义面板
    JSplitPane jsp;	//定义拆分窗格
    JTextArea jta1;	//定义文本域
    JScrollPane jspane1;	//定义滚动窗格
    JTextArea jta2;
    JScrollPane jspane2;
    JTextArea jta3;
    JScrollPane jspane3;
    //下部组件
    JPanel jp2;
    JButton jb1,jb2;	//定义按钮
    JComboBox jcb1;		//定义下拉框

    public static void main(String[] args)  {
        NPL a=new NPL();	//显示界面
    }
    public NPL()		//构造函数
    {
        Font font = new Font(Font.SANS_SERIF,Font.PLAIN,20);
        //jp0 = new JPanel();
        jta3=new JTextArea(13,80);
        jta3.setFont(font);
        jta3.setLineWrap(true);
        jspane3=new JScrollPane(jta3);
        JPanel part1 = new JPanel();
        JLabel lable1 = new JLabel("待检索文本");
        lable1.setFont(font);
        part1.add(lable1,BorderLayout.WEST);
        part1.add(jspane3,BorderLayout.CENTER);
        //创建组件
        //上部组件
        jp1=new JPanel();	//创建面板
        jta1=new JTextArea(5,80);	//创建多行文本框
        jta1.setFont(font);
        jta1.setLineWrap(true);	//设置多行文本框自动换行
        jspane1=new JScrollPane(jta1);	//创建滚动窗格
        JPanel part2 = new JPanel();
        JLabel lable2 = new JLabel("摘要结果");
        lable2.setFont(font);
        part2.add(lable2,BorderLayout.WEST);
        part2.add(jspane1,BorderLayout.CENTER);
        jta2=new JTextArea(3,80);
        jta2.setFont(font);
        jta2.setLineWrap(true);
        jspane2=new JScrollPane(jta2);
        JPanel part3 = new JPanel();
        JLabel lable3 = new JLabel("关键词");
        lable3.setFont(font);
        part3.add(lable3,BorderLayout.WEST);
        part3.add(jspane2,BorderLayout.CENTER);

        jsp=new JSplitPane(JSplitPane.VERTICAL_SPLIT,part2,part3); //创建拆分窗格
        jsp.setDividerSize(1);			//设置分频器大小
        jsp.setEnabled(true);
        //下部组件
        jp2=new JPanel();
        jb1=new JButton("提取");		//创建按钮
        jb1.setFont(font);
        jb2=new JButton("重置");
        jb2.setFont(font);
        //设置布局管理
        jp1.setLayout(new BorderLayout());	//设置面板布局
        jp2.setLayout(new FlowLayout(FlowLayout.RIGHT));

        /*
         * 表单数据
         */
        JPanel summary_len = new JPanel();
        summary_len.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel j1 = new JLabel("摘要长度：");
        j1.setFont(font);
        JTextField jtf1 = new JTextField(5);
        jtf1.setFont(font);
        jtf1.setText("150");
        JLabel j2 = new JLabel("字   关键字数：");
        j2.setFont(font);
        JTextField jtf2 = new JTextField(3);
        jtf2.setText("10");
        jtf2.setFont(font);
        summary_len.add(j1);summary_len.add(jtf1);summary_len.add(j2);summary_len.add(jtf2);


        //添加组件
        jp1.add(jsp);
        jp2.add(jb1);
        jp2.add(jb2);

        JSplitPane jp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,summary_len,jp2);
        jp.setDividerSize(0);
        this.add(part1,BorderLayout.NORTH);
        this.add(jp1,BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);




        /*
         * 添加动作
         */
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = jta3.getText();
                String summary = TextRankSentence.getSummary(text,Integer.parseInt(jtf1.getText()));
                jta1.setText(summary);
                String keywords = TextRankKeyword.getKeywordList(text,Integer.parseInt(jtf2.getText())).toString();
                jta2.setText(keywords);
            }
        });
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jta1.setText("");
                jta2.setText("");
                jta3.setText("");
            }
        });

        //设置窗体实行
        this.setTitle("文本摘要与关键词提取");		//设置界面标题
        this.setIconImage(new ImageIcon("image/qq.gif").getImage()); //设置标题图片，就是上面的小企鹅
        this.setSize(1320, 720);				//设置界面像素
        this.setLocation(200, 200);			//设置界面初始位置
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//设置虚拟机和界面一同关闭
        this.setVisible(true);				//设置界面可视化
    }

}
