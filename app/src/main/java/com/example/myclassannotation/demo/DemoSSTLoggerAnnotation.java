package com.example.myclassannotation.demo;

import com.example.classannotation.annotations.SSTLogger;
import org.springframework.stereotype.Component;

@SSTLogger
@Component
public class DemoSSTLoggerAnnotation {
    public int id;
    public void fun(){
        greet();
    }
}
