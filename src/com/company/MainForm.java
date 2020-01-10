package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static com.company.Util.plainMessage;

/**
 * 用于用户交互的主窗口
 */
public class MainForm extends JFrame {
    JLabel label = new JLabel();
    JLabel label2 = new JLabel();
    JTextField textField = new JTextField();
    JTextField textField2 = new JTextField();
    MyCanvas canvas = new MyCanvas();

    private int canvasWidth;
    private int canvasHeight;
    TreeNode treeNode;
    String str;
    boolean is_begin = false;

    public MainForm(String title, int canvasWidth, int canvasHeight) {
        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        setTitle("");
        setSize(canvasWidth, canvasHeight + 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("开始分析");
        button.addActionListener(e -> {
            if (textField.getText().isEmpty()) {
                plainMessage("", "请向输入框输入数据！！！");
                return;
            }

            treeNode = new TreeNode("evalue");
            str = textField.getText();
            Expression exp = new Expression(str.getBytes());

            // 计算线程开启
            new Thread(() -> {
                try {
                    textField2.setText(String.valueOf(exp.evalue(treeNode.child)));
                } catch (IOException | InterruptedException ex) {
                    plainMessage("错误", ex.getMessage());
//                    ex.printStackTrace();
                }
            }).start();

            // 重绘线程开启
            new Thread(() -> {
                try {
                    while (true) {
                        repaint();
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }).start();
            is_begin = true;
        });
        button.setFont(new Font("宋体", Font.BOLD, 30));

        textField.setText("");
        textField.setFont(new Font("宋体", Font.BOLD, 30));
        textField2.setText("");
        textField2.setFont(new Font("宋体", Font.BOLD, 30));

        label.setText("输入:");
        label.setFont(new Font("宋体", Font.BOLD, 30));
        label2.setText("结果:");
        label2.setFont(new Font("宋体", Font.BOLD, 30));

        JSplitPane jp01 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label, textField);
        jp01.setResizeWeight(0.01);
        JSplitPane jp02 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label2, textField2);
        jp02.setResizeWeight(0.01);
        JSplitPane jp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp01, jp02);
        jp.setResizeWeight(0.5);
        JSplitPane jp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, button);
        jp2.setResizeWeight(0.7);

        JSplitPane jpp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jp2, canvas);
        jpp.setResizeWeight(0.01);

        Container cp = getContentPane();
        cp.add(jpp, BorderLayout.CENTER);
        setVisible(true);
    }

    private class MyCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (!is_begin) return;

            Graphics2D g2d = (Graphics2D) g;//强制类型转换
            TreeNode.drawTree(g2d, treeNode, "evalue", canvasHeight, canvasWidth);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(canvasWidth, canvasHeight);
        }
    }

    public static void main(String[] args) {
        new MainForm("", 1800, 1000);
    }
}
