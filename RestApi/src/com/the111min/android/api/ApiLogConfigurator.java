package com.the111min.android.api;

import android.os.Environment;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import org.apache.log4j.Level;

import java.io.File;

public class ApiLogConfigurator {

    private static LogConfigurator mLogConfigurator = new LogConfigurator();

    public static void configure(String fileName) {

        mLogConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + fileName);
        mLogConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        mLogConfigurator.setLevel("com.the111min.android.api", Level.ERROR);
        mLogConfigurator.configure();
    }

    public void disableLogging() {
        mLogConfigurator.setRootLevel(Level.OFF);
        mLogConfigurator.configure();
    }

}
