package com.company;

import java.awt.*;
import javax.swing.*;
import java.io.*;

import static com.company.Main.filename;
import static com.company.Util.readSrc;

public class AlgoFrame extends JFrame {

    private int canvasWidth;
    private int canvasHeight;
    TreeNode treeNode;
    String info;

    public AlgoFrame(String title, int canvasWidth, int canvasHeight, TreeNode treeNode, String info) {

        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.treeNode = treeNode;
        this.info = info;

        AlgoCanvas canvas = new AlgoCanvas();
        setContentPane(canvas);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    public AlgoFrame(String title, TreeNode treeNode, String info) {
        this(title, 1024, 768, treeNode, info);
    }

    private class AlgoCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;//强制类型转换
            TreeNode.drawTree(g2d, treeNode, "evalue", canvasHeight, canvasWidth);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(canvasWidth, canvasHeight);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String strAll = readSrc(filename);
        String[] strArray = strAll.split("\n");
        for(String str:strArray){
            TreeNode treeNode = new TreeNode("evalue");
            Expression e = new Expression(str.getBytes());
            AlgoFrame algoFrame = new AlgoFrame("", 1800, 1000, treeNode, str);
            new Thread(() -> {
                try {
                    while (true) {
                        algoFrame.repaint();
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }).start();

            try {
                System.out.println(e.evalue(treeNode.child));
                // System.out.println(treeNode);
            } catch (Exception err) {
                System.out.println(err);
                System.out.println(treeNode);
            }
        }
    }
}
