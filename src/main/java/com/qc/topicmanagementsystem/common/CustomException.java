package com.qc.topicmanagementsystem.common;

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg){
        super(msg + "[来自毕业设计选题服务器]");
    }
}
