package top.frankyang.pre.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StackTraces {
    private StackTraces() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String toString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    public static String translate(Throwable throwable) {
        return translate(toString(throwable));
    }

    public static String translate(String stackTrace) {
        stackTrace = stackTrace.replaceAll(  // at...
            "\\n\\tat\\s+", "\n\t在方法："
        );
        stackTrace = stackTrace.replaceAll(  // Caused by...
            "\\nCaused by: ", "\n因为："
        );
        stackTrace = stackTrace.replace(  // (Native Method)
            "(Native Method)", "（原生方法）"
        );

        Pattern pattern = Pattern.compile("\\((.+):([0-9]+)\\)");
        Matcher matcher = pattern.matcher(stackTrace);
        while (matcher.find()) {
            stackTrace = stackTrace.replace(
                matcher.group(0), String.format("中（文件%s，第%s行）", matcher.group(1), matcher.group(2))
            );
        }

        pattern = Pattern.compile("\\n\\t... ([0-9]+) more");
        matcher = pattern.matcher(stackTrace);
        while (matcher.find()) {
            stackTrace = stackTrace.replace(
                matcher.group(0), "\n\t……省略了" + matcher.group(1) + "条调用"
            );
        }
        pattern = Pattern.compile("\\n\\s+File \"([\\s\\S]+?)\", line ([0-9]+), in (.+)");
        matcher = pattern.matcher(stackTrace);
        while (matcher.find()) {
            stackTrace = stackTrace.replace(
                matcher.group(0), String.format(
                    "\n\tPython文件“%s”，第%s行，模块%s：", matcher.group(1), matcher.group(2), matcher.group(3)
                )
            );
        }
        String tmpdir = System.getProperty("java.io.tmpdir");
        stackTrace = stackTrace.replaceAll(
            tmpdir.replace("\\", "\\\\") +
                "[\\\\/]?[a-f0-9]{32}-[0-9]+", "<包的根目录>"
        );
        stackTrace = stackTrace.replace(
            "Traceback (most recent call last):", "\n\n[Python异常栈（通常是最后调用的）]"
        );
        stackTrace = stackTrace.replace("\t", "  ");
        stackTrace = stackTrace.replace("\r", "");
        return stackTrace;
    }
}
