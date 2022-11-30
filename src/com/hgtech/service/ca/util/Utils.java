package com.hgtech.service.ca.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:26
 **/
public class Utils {

    public static void writeObjectToFile(Object obj, String filename)
    {
        File file = new File(filename);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String filename)
    {
        Object temp = null;
        try {
            File file =new File(filename);
            FileInputStream in;
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String mapToString(Map map) {
        StringBuilder res = new StringBuilder();
        for (Object key : map.keySet()) {
            res.append(" ").append(key.toString()).append(": ").append(map.get(key).toString());
        }
        return res.toString();
    }
}
