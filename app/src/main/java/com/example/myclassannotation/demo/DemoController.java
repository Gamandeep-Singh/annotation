package com.example.myclassannotation.demo;

import com.example.classannotation.annotations.MyLogger;
import org.springframework.stereotype.Component;

@MyLogger
@Component
public class DemoController {
    public void fun(){
//        myLogger.info("hello world");
    }
}
