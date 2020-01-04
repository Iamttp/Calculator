package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
    expr := term (+|-) term (+|-) ... (+|-) term
    term := factor (*|/) factor (* | /) ... (*|/) factor
    factor := INT | "(" expr ")"
 */

class TreeNode {
    Stack<TreeNode> child;
    String val;

    public TreeNode(String val) {
        this.val = val;
        child = new Stack<>();
    }

    public static void dfs(TreeNode treeNode) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(treeNode);
        while (q.size() > 0) {
            int len = q.size();
            for (int i = 0; i < len; i++) {
                TreeNode node = q.poll();

                assert node != null;
                for (TreeNode nodeChild : node.child)
                    q.offer(nodeChild);
            }
        }
    }

    public String toDepthString(int depth) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            str.append("\t");
        }
        str.append(val).append("\n");
        for (TreeNode node : child)
            str.append(node.toDepthString(depth + 1));
        return str.toString();
    }

    @Override
    public String toString() {
        return toDepthString(0);
    }

    /**
     * 生成树图写到磁盘
     *
     * @param picType  图片类型JPG GIF JPEG PNG
     * @param file     图片文件
//     * @param list     数据模型 [{name: '', children: [{name: '', children: [{}]}]}]
     * @param rootName 根名称
     * @return
     */
    public static boolean writeImage(String picType, File file, TreeNode treeNode, String rootName) {
        BufferedImage bimg = new BufferedImage(2002, 2002, BufferedImage.TYPE_INT_BGR);
        // 拿到画笔
        Graphics2D g = bimg.createGraphics();
        // 画一个图形系统默认是白色
        int imgWidth = 2000;
        int imgHeight = 2000;
        g.fillRect(1, 1, imgWidth, imgHeight);
        // 设置画笔颜色
        g.setColor(new Color(12, 123, 88));
        int fontSize = 40;
        // 设置画笔画出的字体风格
        g.setFont(new Font("隶书", Font.ITALIC, fontSize));
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        //消除文字锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 写一个字符串
        int margin = 60;
        int parentY = imgHeight / 2;
        g.drawString(rootName, fontSize, parentY);
        int parentX = computeParentX(rootName, fontSize, fontSize);
        int heightL2 = (imgHeight - fontSize) / treeNode.child.size();
        for (int i = 0; i < treeNode.child.size(); i++) {
            TreeNode shareHolderDto = treeNode.child.get(i);
            int height = heightL2 / 2;
            String name = shareHolderDto.val;
            g.drawString(name, parentX + margin, height + heightL2 * i);
            // TODO 这里可以写其他属性， y随属性个数增加而增加 fontSize * i
            // g.drawString("注册资本：" + (shareHolderDto.getRegCapital() == null?"-":shareHolderDto.getRegCapital()), parentX + margin, height + heightL2 * i + fontSize);
            // 设置画笔颜色
            g.setColor(new Color(212, 123, 88));
            g.drawLine(parentX, parentY, parentX + margin, height + heightL2 * i);
            // 设置画笔颜色
            g.setColor(new Color(12, 123, 88));
            if (shareHolderDto.child != null && !shareHolderDto.child.isEmpty()) {
                int myX = computeParentX(shareHolderDto.val, parentX + margin, fontSize);
                drawChildrenTransverse(g, shareHolderDto.child, height + heightL2 * i, heightL2, heightL2 * i, fontSize, margin, myX);
            }
        }
        // 释放画笔
        g.dispose();
        // 将画好的图片通过流形式写到硬盘上
        boolean val = false;
        try {
            val = ImageIO.write(bimg, picType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }

    /**
     * 循环子树
     *
     * @param g            Graphics2D
     * @param children     子节点
     * @param parentY      父节点的Y坐标
     * @param parentHeight 父区域的高度
     * @param startY       父区域起始Y坐标
     * @param fontSize     字体大小
     * @param margin       兄弟节点的间距
     * @param parentX      父节点的X坐标
     */
    private static void drawChildrenTransverse(Graphics2D g, Stack<TreeNode> children, int parentY, int parentHeight, int startY, int fontSize, int margin, int parentX) {
        int heightLn = parentHeight / children.size();
        for (int i = 0; i < children.size(); i++) {
            TreeNode shareHolderDto = children.get(i);
            int y = heightLn / 2 + heightLn * i + startY;
            int x = parentX + margin;
            String name = shareHolderDto.val;
            g.drawString(name, x, y);
            // 设置画笔颜色
            g.setColor(new Color(212, 123, 88));
            g.drawLine(parentX, parentY, x, y);
            // 设置画笔颜色
            g.setColor(new Color(12, 123, 88));
            if (shareHolderDto.child != null && !shareHolderDto.child.isEmpty()) {
                int myX = computeParentX(shareHolderDto.val, x, fontSize);
                int myStartY = heightLn * i + startY;
                drawChildrenTransverse(g, shareHolderDto.child, y, heightLn, myStartY, fontSize, margin, myX);
            }
        }
    }

    /**
     * 计算父节点名字末尾X坐标
     *
     * @param str      文本
     * @param patentX  父节点起点X
     * @param fontSize 字体大小
     * @return
     */
    private static int computeParentX(String str, int patentX, int fontSize) {
        return patentX + str.length() * fontSize;
    }
}

public class Expression {
    public InputTokenStream ts;

    public static void main(String[] args) {
        Expression e = new Expression();
        TreeNode treeNode = new TreeNode("evalue");
        try {
            System.out.println(e.evalue(treeNode.child));
            System.out.println(treeNode);
        } catch (Exception err) {
            System.out.println(err);
            System.out.println(treeNode);
        }
        TreeNode.writeImage("jpg", new File("pic.jpg"),treeNode,"evalue");
    }

    public Expression() {
        ts = new InputTokenStream(new BufferedInputStream(System.in));
    }

    // expr := term (+|-) term (+|-) ... (+|-) term
    public int evalue(Stack<TreeNode> treeNodeList) throws IOException {
        treeNodeList.push(new TreeNode("term"));
        int t = term(treeNodeList.peek().child);
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.PLUS || op.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            if (op.tokenType == Token.TokenType.PLUS) {
                treeNodeList.push(new TreeNode("+"));
            } else {
                treeNodeList.push(new TreeNode("-"));
            }
            treeNodeList.push(new TreeNode("term"));
            int t2 = term(treeNodeList.peek().child);
            if (op.tokenType == Token.TokenType.PLUS) {
                t += t2;
            } else {
                t -= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // term := factor (*|/) factor (* | /) ... (*|/) factor
    private int term(Stack<TreeNode> treeNodeList) throws IOException {
        treeNodeList.push(new TreeNode("factor"));
        int t = factor(treeNodeList.peek().child);
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.MULT || op.tokenType == Token.TokenType.DIV) {
            ts.consumeToken();
            if (op.tokenType == Token.TokenType.MULT) {
                treeNodeList.push(new TreeNode("*"));
            } else {
                treeNodeList.push(new TreeNode("/"));
            }
            treeNodeList.push(new TreeNode("factor"));
            int t2 = factor(treeNodeList.peek().child);
            if (op.tokenType == Token.TokenType.MULT) {
                t *= t2;
            } else {
                t /= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // factor := INT | "(" expr ")"
    private int factor(Stack<TreeNode> treeNodeList) throws IOException {
        Token t = ts.getToken();
        if (t.tokenType == Token.TokenType.INT) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode(t.value.toString()));
            return ((Integer) t.value);
        } else if (t.tokenType == Token.TokenType.LPAR) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode("("));
            treeNodeList.push(new TreeNode("evalue"));
            int v = evalue(treeNodeList.peek().child);
            treeNodeList.push(new TreeNode(")"));
            match(ts.getToken(), Token.TokenType.RPAR);
            return v;
        } else if (t.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode("-"));
            return -factor(treeNodeList.peek().child);
        } else {
            String error = "发生错误！" +
                    "\t错误token为\t" + t +
                    "\t错误发生位置\t" + ts.pos;
            throw new IOException(error);
        }
    }

    private void match(Token t, Token.TokenType tt) {
        assert t.tokenType == tt;
        ts.consumeToken();
    }
}