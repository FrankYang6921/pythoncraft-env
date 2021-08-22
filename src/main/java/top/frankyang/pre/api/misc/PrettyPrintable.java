package top.frankyang.pre.api.misc;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 表明一个对象可以被美观打印。
 */
public interface PrettyPrintable {
    /**
     * 获取一个对象的美观字符串。
     *
     * @return 美观字符串。
     */
    default String prettyString() {
        return prettyString(1);
    }

    /**
     * 获取一个对象的美观字符串（具有指定的深度）。该方法一般不应该被使用，它仅在内部调用时负责解决结构体的嵌套问题。每当该方法被递归调用，{@code depth}应当自增。
     *
     * @param depth 递归调用的深度。
     * @return 美观字符串。
     */
    default String prettyString(int depth) {
        return toString();
    }

    /**
     * 打印一个对象的美观字符串。
     *
     * @param printStream 要打印到的流。
     */
    default void prettyPrint(PrintStream printStream) {
        printStream.println(prettyString().trim());
    }

    /**
     * 打印一个对象的美观字符串。
     *
     * @param outputStream 要打印到的流。
     */
    default void prettyPrint(OutputStream outputStream) {
        prettyPrint(new PrintStream(outputStream));
    }

    /**
     * 打印一个对象的美观字符串到标准输出流。
     */
    default void prettyPrint() {
        prettyPrint(System.out);
    }
}
