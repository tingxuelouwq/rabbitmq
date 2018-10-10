package com.kevin.spring.async.annotation;

/**
 * @类名: RecvMsgHandle
 * @包名：com.kevin.spring.async.annotation
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2018/10/10 22:17
 * @版本：1.0
 * @描述：
 */
public class RecvMsgHandle {

    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }
}
