package com.timedancing.easyfirewall.hooker;

import android.util.Log;


import com.timedancing.easyfirewall.core.nat.NatSession;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.zip.GZIPOutputStream;

public class TcpHooker {
    static LinkedHashMap<String, ResponseMock> map;
    NatSession session;
    static {
       map = new LinkedHashMap<>();
        ResponseMock heartMock = new ResponseMock("106.53.15.199/index.php/api/SingleCard/heart",
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 25\r\n" +
                "\r\n",
                "{\"code\":\"1807\",\"data\":\"\"}");
        ResponseMock loginMock = new ResponseMock("106.53.15.199/index.php/api/SingleCard/login",
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 71\r\n" +
                "\r\n",
                "{\"code\":\"1707\",\"data\":{\"endtime\":\"2000000000\",\"point\":\"1\",\"token\":\"S\"}}");
        ResponseMock baiduMock = new ResponseMock("www.baidu.com/",
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html;charset=utf-8\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 71\r\n" +
                        "\r\n",
                "{\"code\":\"1707\",\"data\":{\"endtime\":\"2000000000\",\"point\":\"1\",\"token\":\"S\"}}");
        map.put(heartMock.url,heartMock);
        map.put(loginMock.url,loginMock);
        map.put(baiduMock.url,baiduMock);
    }

    public TcpHooker(NatSession session) {
        this.session = session;
    }

    public ByteBuffer afterReceived(ByteBuffer buffer){
        try {
            String url = session.RequestUrl;
            if (map.containsKey(url)){
                ResponseMock responseMock = map.get(url);
                Log.e("zxj", "tcp mock : \n" + url + "\n" + responseMock);
                byte[] bytes = (responseMock.header + responseMock.body).getBytes();
                buffer = ByteBuffer.wrap(bytes);
            }
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }

        return buffer;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

}
