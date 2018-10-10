package com.kevin.spring.async.xml;

/**
 * @类名: ReceiveMsgHandler
 * @包名：com.kevin.spring.async.xml
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/9 21:16
 * @版本：1.0
 * @描述：
 */
public class ReceiveMsgHandler {

    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }
}
