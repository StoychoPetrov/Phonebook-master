package com.example.stoycho.phonebook.loggerClasses;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by kamen.troshev on 17.12.2015 г..
 */
public class DataLogger {
    private static final String FILENAME    = "dataLog.txt";

    public static void log(String messageType, long size, String message) {
        if( true )
            return;

        File storageType        = Environment.getExternalStorageDirectory();
        File storageDir         = new File(storageType + "/MyPos/logs/");
        storageDir.mkdirs();
        File logFile = new File(storageDir.getPath(), FILENAME);
        if( !logFile.exists() ){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter out = new FileWriter(logFile, true);

            Calendar calendar = Calendar.getInstance();
            String date = calendar.get(Calendar.DAY_OF_MONTH)
                    + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.YEAR)
                    + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE) + ":\r\n";

            String strSize;
            if ( size < 1024 )
                strSize = size + "B";
            else if ( size > 1024 && size < 1024 * 1024)
                strSize = size/1024 + "KB";
            else
                strSize = size/(1024*1024) + "MB";

            if( message != null )
                message = "\r\n" + message;

            out.write(date);
            out.write(messageType + " - " + strSize + message + "\r\n\r\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
