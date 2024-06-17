package com.example.myclassannotation;

import com.example.myclassannotation.myservice.SstLogger;

class MyLogger {
    private final SstLogger sstlogger
            = new SstLogger(this.getClass().getName());

    public SstLogger getLogger() {
        return sstlogger;
    }
}
