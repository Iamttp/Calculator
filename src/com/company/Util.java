package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Util {
    static String readSrc(String filename) {
        String str = "";
        File file = new File(filename);
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "GB2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
