package com.bikesharing.app.log;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LoggerConfig {

    public static void configure(String szDataDir) {
        final LogConfigurator logConfigurator = new LogConfigurator();

        PropertyConfigurator.configure(LoggerConfig.class.getResource("log.properties"));

        logConfigurator.setFileName(szDataDir + File.separator + "Log" + File.separator + "myapp.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();
    }

}
