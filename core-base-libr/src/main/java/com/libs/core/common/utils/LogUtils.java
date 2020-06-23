/**
 * Copyright (c) 2013 Cangol
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.libs.core.common.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Cangol
 * @modify zoubangyue 新增打印日志到磁盘目录，jindashi\jindashi.log。test和aphla渠道输出全部日志到磁盘，其他渠道打印error以上级别日志
 */
public class LogUtils {
    private static int LEVEL = Log.VERBOSE;

    private static boolean FORMAT = false;

    public static int getLevel() {
        return LEVEL;
    }

    public static void setLEVEL(int LEVEL) {
        LogUtils.LEVEL = LEVEL;
    }

    public static boolean isFormat() {
        return FORMAT;
    }
    // VERBOSE log

    public static void v(String msg) {
        formatLog(Log.VERBOSE, null, msg, null);
    }

    public static void v(String tag, String msg) {
        formatLog(Log.VERBOSE, tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable t) {
        formatLog(Log.VERBOSE, tag, msg, t);
    }

    // INFO log

    public static void i(String msg) {
        formatLog(Log.INFO, null, msg, null);
    }

    public static void i(String tag, String msg) {
        formatLog(Log.INFO, tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable t) {
        formatLog(Log.INFO, tag, msg, t);
    }

    // DEBUG log

    public static void d(String msg) {
        formatLog(Log.DEBUG, null, msg, null);
    }

    public static void d(String tag, String msg) {
        formatLog(Log.DEBUG, tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable t) {
        formatLog(Log.DEBUG, tag, msg, t);
    }

    // WARN log
    public static void w(String msg) {
        formatLog(Log.WARN, null, msg, null);
    }

    public static void w(String tag, String msg) {
        formatLog(Log.WARN, tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable t) {
        formatLog(Log.WARN, tag, msg, t);
    }

    // ERROR log

    public static void e(String msg) {
        formatLog(Log.ERROR, null, msg, null);
    }

    public static void e(String tag, String msg) {
        formatLog(Log.ERROR, tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable t) {
        formatLog(Log.ERROR, tag, msg, t);
    }

    private static void formatLog(int logLevel, String tag, String msg, Throwable error) {
        if (LEVEL > logLevel) {
            return;
        }
        StackTraceElement stackTrace = new Throwable().getStackTrace()[2];
        String classname = stackTrace.getClassName();
        String filename = stackTrace.getFileName();
        String methodname = stackTrace.getMethodName();
        int linenumber = stackTrace.getLineNumber();
        String output = null;

        if (FORMAT) {
            output = String.format("%s.%s(%s:%d)-->%s", classname, methodname, filename, linenumber, msg);
        } else {
            output = msg;
        }
        if (null == tag) {
            tag = (filename != null && filename.contains(".java")) ? filename.replace(".java", "") : "";
        }
        if (output == null) {
            output = "" + null;
        }
        switch (logLevel) {
            case Log.VERBOSE:
                if (error == null) {
                    Log.v(tag, output);
                } else {
                    Log.v(tag, output, error);
                }
                break;
            case Log.DEBUG:
                if (error == null) {
                    Log.d(tag, output);
                } else {
                    Log.d(tag, output, error);
                }
                break;
            case Log.INFO:
                if (error == null) {
                    Log.i(tag, output);
                } else {
                    Log.i(tag, output, error);
                }
                break;
            case Log.WARN:
                if (error == null) {
                    Log.w(tag, output);
                } else {
                    Log.w(tag, output, error);
                }
                break;
            case Log.ERROR:
                if (error == null) {
                    Log.e(tag, output);
                } else {
                    Log.e(tag, output, error);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 解析日志输出的TAG
     */
    private static String parseLogTag(@NonNull Object obj) {
        // 如果objTag是String，则直接使用
        // 如果objTag不是String，则使用它的类名
        // 如果在匿名内部类，写this的话是识别不了该类，所以获取当前对象全类名来分隔
        String tag;
        if (obj instanceof String) {
            tag = (String) obj;
        } else if (obj instanceof Class) {
            tag = ((Class) obj).getSimpleName();
        } else {
            tag = obj.getClass().getName();
            String[] split = tag.split("\\.");
            tag = split[split.length - 1].split("\\$")[0];
        }
        return tag;
    }

    /**
     * 解析日志输出的MSG
     */
    private static String parseLogMsg(String msg) {
        return !TextUtils.isEmpty(msg) ? msg : "null";
    }

    public static void v(Object tag, String msg) {
        v(parseLogTag(tag), parseLogMsg(msg));
    }

    public static void d(Object tag, String msg) {
        d(parseLogTag(tag), parseLogMsg(msg));
    }

    public static void i(Object tag, String msg) {
        i(parseLogTag(tag), parseLogMsg(msg));
    }

    public static void w(Object tag, String msg) {
        w(parseLogTag(tag), parseLogMsg(msg));
    }

    public static void e(Object tag, String msg) {
        e(parseLogTag(tag), parseLogMsg(msg));
    }
}
