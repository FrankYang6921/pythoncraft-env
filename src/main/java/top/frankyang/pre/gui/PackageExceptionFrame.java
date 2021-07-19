package top.frankyang.pre.gui;

import top.frankyang.pre.gui.controls.*;
import top.frankyang.pre.loader.ExceptionStatus;
import top.frankyang.pre.util.StackTraces;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

public final class PackageExceptionFrame extends CloseWaitingFrame {
    private ExceptionStatus result = ExceptionStatus.CRASH;

    private PackageExceptionFrame(String stackTrace, Path src) {
        super();

        setSize(800, 600);
        setResizable(false);

        String string = "PythonCraft Loader出错了：在加载" +
            (src == null ? "未知的包" : "来自“" + src + "”的包") +
            "时，PythonCraft捕获了一个异常。你可以禁用这个包，然后重新加载其余的包，" +
            "或者不禁用这个包，然后重新加载所有的包，亦或是退出Minecraft。下方的文本" +
            "框中是所捕获异常的调用栈。调用栈也会被记录在Minecraft的日志文件中供参考。" +
            "如果你认为这个异常是因PythonCraft的漏洞而产生的，请联系原作者汇报漏洞:)";

        MineText label = new MineText(string);

        MineTextArea innerText = new MineTextArea();
        innerText.setEditable(false);
        innerText.setText(stackTrace);
        JScrollPane outerText = new JScrollPane(innerText);

        MineButton ignore = new MineButton("忽略这个错误");
        MineButton disable = new MineButton("禁用这个包");
        MineButton crash = new MineButton("退出Minecraft");
        if (src == null) {
            disable.setEnabled(false);
        }

        MinePanel buttons = new MinePanel(ignore, disable, crash);

        add(new MineVBox(
            new Dimension(Constants.PADDING_WIDTH, Constants.PADDING_HEIGHT),
            label, outerText, buttons
        ));

        ignore.addActionListener(e -> {
            result = ExceptionStatus.IGNORE;
            closeWindow();
        });
        disable.addActionListener(e -> {
            result = ExceptionStatus.DISABLE;
            closeWindow();
        });
        crash.addActionListener(e -> {
            result = ExceptionStatus.CRASH;
            closeWindow();
        });

        setVisible(true);
    }

    public static ExceptionStatus open(Throwable e, Path src) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        PackageExceptionFrame frame = new PackageExceptionFrame(StackTraces.translate(writer.toString()), src);
        frame.waitForClose();
        return frame.result;
    }
}
