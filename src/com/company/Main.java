package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.company.Util.readSrc;

public class Main {
    static final String filename = "test";

    public static void main(String[] args) throws FileNotFoundException {
        String strAll = readSrc(filename);
        String[] strArray = strAll.split("\n");
        for (String str : strArray) {
            Expression e = new Expression(str.getBytes());
            e.delay_time = 0;
            TreeNode treeNode = new TreeNode("evalue");
            try {
                System.out.println(e.evalue(treeNode.child));
                System.out.println(treeNode);
//            TreeNode.writeImage("jpg", new File("pic.jpg"), treeNode, "evalue");
                System.out.println(TreeNode.getPre(treeNode));
                System.out.println(TreeNode.getSuf(treeNode));
            } catch (IOException | InterruptedException err) {
                System.out.println(err);
                System.out.println(treeNode);
            }
        }
    }
}
