package com.seuic.app.store.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created on 2017/8/17.
 *
 * @author dpuntu
 */

public class FileUtils {
    private static String logPath = null;
    // log 日志存放最大天数
    private static final int MAX_DAY = 7;

    /**
     * 下载的 zip 文件放在包名/files目录下
     *
     * @return 文件路径
     */
    public static String getDownloadPath(Context context) {
        return getFilePath(context) + "/download/";
    }


    /**
     * 图片缓存路径
     *
     * @return 文件路径
     */
    public static String getCachePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/AppStore/cache";
    }

    /**
     * 图片缓存路径
     *
     * @return 文件路径
     */
    public static String getWebCachePath(Context context) {
        return getFilePath(context) + "/web/";
    }

    /**
     * 根据路径删除文件
     *
     * @param filePath
     *         路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                Loger.i("删除" + filePath + "文件成功");
            }
        }
    }

    /**
     * 根据路径删除文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.delete()) {
                Loger.i("删除" + file.getPath() + "文件成功");
            }
        }
    }

    /**
     * 判断SD卡是否存在
     */
    private static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 初始化log日志保存路径
     */
    public static void initFileLoger(Context context) {
        logPath = getFilePath(context) + "/log/";
        deleteLogFile();
        createDownloadPath(getDownloadPath(context));
    }

    public static boolean createDownloadPath(String loadPath) {
        File dic = new File(loadPath);
        if (!dic.exists()) {
            if (dic.mkdirs()) {
                Loger.i("下载目录创建成功");
                return true;
            }
            return true;
        }
        return false;
    }

    /**
     * 判断外部SD卡是否存在
     *
     * @return boolean 是否存在
     */
    public static boolean isExternalStorage() {
        if (isSdCardExist() || !Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获得文件存储路径
     *
     * @return String
     * 保存的路径
     */
    private static String getFilePath(Context context) {
        if (isExternalStorage()) {
            try {
                return context.getExternalFilesDir(null).getPath();
                // 获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.xx.xx.xx/xx/xx/xx.xx
            } catch (NullPointerException e) {
                Loger.i("获取存储路径失败");
                // 获得外部存储路径,路径为 /storage/emulated/0/AppStore
                return context.getFilesDir().getPath();
            }
        } else {
            return context.getFilesDir().getPath();
            //直接存在/data/data里，非root手机是看不到的
        }
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param filePath
     *         文件路径
     *
     * @return long 文件大小
     */
    public static long getFolderSize(String filePath) {
        long size = 0;
        try {
            File dir = new File(filePath);
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    size = size + getFolderSize(file.getPath());
                } else {
                    size = size + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return size;
        }
        return size;
    }

    /**
     * log日志保存
     *
     * @param str
     *         日志
     */
    public static void writeLog(String str) {
        if (logPath == null) {
            return;
        }

        try {
            File dic = new File(logPath);
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String fileName = dateFormat.format(curDate) + ".txt";
            File file = new File(logPath + fileName);
            if (!dic.exists()) {
                if (dic.mkdirs()) {
                    Loger.i("目录创建成功");
                }
            }

            if (!file.exists()) {
                if (file.createNewFile()) {
                    Loger.i("文件创建成功");
                }
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
            String ss = "[" + timeFormat.format(curDate) + "] " + str + "\n";
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(ss.getBytes("utf-8"));
            fos.flush();
            fos.close();
        } catch (IOException e) {
//            Loger.e(android.util.Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }

    /**
     * 删除文件里面的日志文件，文件的名字为20170204.TXT，共8个字节的长度
     * 功能：删除目录中，日期间隔大于7天的日志，日期的格式是yyyyMMdd
     */
    private static void deleteLogFile() {
        if (logPath == null) {
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String currentTime = formatter.format(curDate);
        File dic = new File(logPath);
        if (dic.exists() && dic.isDirectory()) {
            File[] childFile = dic.listFiles();
            for (File f : childFile) {
                String fileName = f.getName();
                //判断日期是否合法，不合法就需要清空这个文件
                Date logDate = null;
                try {
                    formatter.setLenient(false);
                    logDate = formatter.parse(fileName);
                } catch (ParseException e) {
                    logDate = null;
                }

                if (logDate == null) {
                    if (f.delete()) {
                        Loger.i("文件被删除");
                    }
                    continue;
                }

                Date today = null;
                try {
                    formatter.setLenient(false);
                    today = formatter.parse(currentTime);
                } catch (ParseException e) {
                    today = null;
                }

                if (today == null) {
                    today = curDate;
                }

                GregorianCalendar logCal = new GregorianCalendar();
                GregorianCalendar curCal = new GregorianCalendar();
                logCal.setTime(logDate);
                curCal.setTime(today);
                double dayCount = (curCal.getTimeInMillis() - logCal.getTimeInMillis()) / (1000 * 3600 * 24);// 从间隔
                if (dayCount > MAX_DAY || dayCount < -MAX_DAY) {
                    if (f.delete()) {
                        Loger.d(fileName + " 日志文件已删除");
                    }
                }
            }
        }
    }
}
