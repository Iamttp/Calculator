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
    // 创建一个滑块，最小值、最大值、初始值 分别为 0、100、10
    JSlider slider = new JSlider(0, 30, 2);

    int repaintInterval = 100;
    final int FORMWIDTH = 1800;
    final int FORMHEIGHT = 1000;

    private int canvasWidth = FORMWIDTH + 500;
    private int canvasHeight = FORMHEIGHT + 500;
    TreeNode treeNode;
    String str;
    boolean is_begin = false;
    boolean is_finish = false;

    public MainForm(String title) {
        super(title);

        setTitle("");
        setSize(FORMWIDTH, FORMHEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("开始分析");
        button.addActionListener(e -> {
            if (textField.getText().isEmpty()) {
                plainMessage("", "请向输入框输入数据！！！");
                return;
            }

            is_finish = false;
            treeNode = new TreeNode("evalue");
            str = textField.getText();
            Expression exp = new Expression(str.getBytes());

            // 计算线程开启
            new Thread(() -> {
                try {
                    textField2.setText(String.valueOf(exp.evalue(treeNode.child)));
                    Thread.sleep(repaintInterval);
                    is_finish = true;
                } catch (IOException | InterruptedException ex) {
                    plainMessage("错误", ex.getMessage());
//                    ex.printStackTrace();
                }
            }).start();

            // 重绘线程开启
            new Thread(() -> {
                try {
                    while (!is_finish) {
                        repaint();
                        Thread.sleep(repaintInterval);
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

        // 设置主刻度间隔
        slider.setMajorTickSpacing(5);
        // 设置次刻度间隔
        slider.setMinorTickSpacing(1);
        // 绘制 刻度 和 标签
//        slider.setPaintTicks(true);
//        slider.setPaintLabels(true);
        // 添加刻度改变监听器
        slider.addChangeListener(e -> {
            canvasWidth = FORMWIDTH + 500 * slider.getValue();
            canvasHeight = FORMHEIGHT + 500 * slider.getValue();
            repaint();
        });


        JSplitPane jp01 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label, textField);
        jp01.setResizeWeight(0.01);
        JSplitPane jp02 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label2, textField2);
        jp02.setResizeWeight(0.01);
        JSplitPane jp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp01, jp02);
        jp.setResizeWeight(0.5);
        JSplitPane jp03 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, slider);
        jp03.setResizeWeight(0.7);
        JSplitPane jp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp03, button);
        jp2.setResizeWeight(0.7);

        canvas.setAutoscrolls(false);
        JScrollPane js = new JScrollPane(canvas);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JSplitPane jpp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jp2, js);
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
        new MainForm("递归下降分析示例程序 -- iamttp");
    }
}
