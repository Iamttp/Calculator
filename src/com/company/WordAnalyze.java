package com.company;

public class WordAnalyze {
    //保留字 TODO for
    String[] reserve = {"if", "else", "while", "int", "char", "double", "float",
            "static", "default", "switch", "void", "return"};//0~99
    //运算符
    String[] operator = {"+", "-", "*", "/", "++", "--", "==", "!=", ">", "<", ">=",
            "<=", "&&", "||", "!", "&", "|", "^", "~", "<<", ">>", ">>>", "+=", "="};//110~149
    //界符
    char[] divide = {'(', ')', '{', '}', '[', ']', '\'', '"', ',', ';', '?', '/', '\\', ':', '.'};//150~无穷
    int line = 1;

    //判断是否是保留字
    int isReserve(String str) {
        for (int i = 0; i < reserve.length; i++) {
            if (reserve[i].equals(str))
                return i;
        }
        return -1;
    }

    int isOperator(String str) {
        for (int i = 0; i < operator.length; i++) {
            if (operator[i].equals(str))
                return i;
        }
        return -1;
    }

    int isDivide(char str) {
        for (int i = 0; i < divide.length; i++) {
            if (divide[i] == str)
                return i;
        }
        return -1;
    }

    //判断是否是字母
    boolean isLetter(char letter) {
        return (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z');
    }

    //判断是否是数字
    boolean isDigit(char digit) {
        return digit >= '0' && digit <= '9';
    }

    //词法分析
    public void analyze(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            StringBuilder arr = new StringBuilder();

            if (ch == '\n') {
                ++line;
            } else if (ch == '#') {
                while (ch != '\n') ch = chars[++i];
                i--;
            } else if (isLetter(ch)) {
                while (isLetter(ch) || isDigit(ch)) {
                    arr.append(ch);
                    ch = chars[++i];
                }
                //回退一个字符
                i--;
                if (isReserve(arr.toString()) != -1) {
                    //关键字
                    System.out.println(arr + "\t" + "\t关键字");
                } else {
                    //标识符
                    System.out.println(arr + "\t" + "\t标识符");
                }
            } else if (isDigit(ch) || (ch == '.')) {
                while (isDigit(ch) || (ch == '.' && isDigit(chars[++i]))) {
                    if (ch == '.') i--;
                    arr.append(ch);
                    ch = chars[++i];
                }
                //属于无符号常数
                System.out.println(arr + "\t" + "\t常数");
            } else {
                //分界符
                if (isDivide(ch) != -1) {
                    System.out.println(ch + "\t" + "\t分界符");
                } else switch (ch) {
                    //运算符
                    case '+':
                        System.out.println(ch + "\t" + "\t运算符");
                        break;
                    case '-':
                        System.out.println(ch + "\t" + "\t运算符");
                        break;
                    case '*':
                        System.out.println(ch + "\t" + "\t运算符");
                        break;
                    case '/':
                        System.out.println(ch + "\t" + "\t运算符");
                        break;

                    //运算符
                    case '=': {
                        ch = chars[++i];
                        if (ch == '=') System.out.println("==" + "\t" + "\t运算符");
                        else {
                            System.out.println("=" + "\t" + "\t运算符");
                            i--;
                        }
                    }
                    break;
//                    case ':': {
//                        ch = chars[++i];
//                        if (ch == '=') System.out.println(":=" + "\t2" + "\t运算符");
//                        else {
//                            System.out.println(":" + "\t2" + "\t运算符");
//                            i--;
//                        }
//                    }
//                    break;
                    case '>': {
                        ch = chars[++i];
                        if (ch == '=') System.out.println(">=" + "\t" + "\t运算符");
                        else {
                            System.out.println(">" + "\t" + "\t运算符");
                            i--;
                        }
                    }
                    break;
                    case '<': {
                        ch = chars[++i];
                        if (ch == '=') System.out.println("<=" + "\t" + "\t运算符");
                        else {
                            System.out.println("<" + "\t" + "\t运算符");
                            i--;
                        }
                    }
                    break;
                    //无识别
//                default:
//                    System.out.println(ch + "\t6" + "\t无识别符");
                }
            }
        }
    }
}
