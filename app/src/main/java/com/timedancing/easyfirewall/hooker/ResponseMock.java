package com.timedancing.easyfirewall.hooker;

public class ResponseMock {
    String url;
    String header;
    String body;
    public ResponseMock(String url, String header, String body) {
        this.url = url;
        this.header = header;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseMock{" +
                "url='" + url + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
