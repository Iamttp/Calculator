package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    static final String filename = "test";

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(filename);
        Expression e = new Expression(new FileInputStream(file));
        e.delay_time = 0;
//        Expression e = new Expression(System.in);
        TreeNode treeNode = new TreeNode("evalue");
        try {
            System.out.println(e.evalue(treeNode.child));
            System.out.println(treeNode);
            TreeNode.writeImage("jpg", new File("pic.jpg"), treeNode, "evalue");
        } catch (Exception err) {
            System.out.println(err);
            System.out.println(treeNode);
        }
    }
}
