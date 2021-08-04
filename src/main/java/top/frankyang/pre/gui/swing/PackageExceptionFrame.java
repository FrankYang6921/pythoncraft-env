package top.frankyang.pre.gui.swing;

import top.frankyang.pre.loader.ExceptionStatus;
import top.frankyang.pre.util.StackTraces;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;

public final class PackageExceptionFrame extends CloseWaitingFrame {
    public static final int PADDING_WIDTH = 16;
    public static final int PADDING_HEIGHT = 16;
    private static ImageIcon icon;
    private ExceptionStatus result = ExceptionStatus.CRASH;

    private PackageExceptionFrame(String stackTrace, Path src) {
        super();

        setSize(800, 600);
        setIconImage(getIcon().getImage());
        setTitle("PythonCraft Runtime Environment");

        String string = "PythonCraftRE出错了：在加载或运行" +
            (src == null ? "未知的包" : "来自“" + src + "”的包") +
            "时，PythonCraft捕获了一个异常。你可以禁用这个包，然后重新加载其余的包，" +
            "或者不禁用这个包，然后重新加载所有的包，亦或是退出Minecraft。下方的文本" +
            "框中是所捕获异常的调用栈。调用栈也会被记录在Minecraft的日志文件中供参考。" +
            "如果你认为这个异常是因PythonCraft的漏洞而产生的，请联系原作者汇报漏洞:)";

        Box labelWrapper = Box.createHorizontalBox();
        JLabel label = new JLabel("<html>" + string + "</html>");
        labelWrapper.add(label);

        JTextArea innerText = new JTextArea();
        innerText.setLineWrap(false);
        innerText.setEditable(false);
        innerText.setText(stackTrace);
        JScrollPane textWrapper = new JScrollPane(innerText);

        JButton ignore = new JButton("忽略这个错误");
        JButton disable = new JButton("禁用这个包");
        JButton crash = new JButton("退出Minecraft");
        if (src == null) {
            disable.setEnabled(false);
        }

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new FlowLayout() {{
            setHgap(PADDING_WIDTH);
            setVgap(0);
        }});
        buttonWrapper.add(ignore);
        buttonWrapper.add(disable);
        buttonWrapper.add(crash);

        Box outer = Box.createHorizontalBox();
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(PADDING_HEIGHT));
        box.add(labelWrapper);
        box.add(Box.createVerticalStrut(PADDING_HEIGHT));
        box.add(textWrapper);
        box.add(Box.createVerticalStrut(PADDING_HEIGHT));
        box.add(buttonWrapper);
        box.add(Box.createVerticalStrut(PADDING_HEIGHT));
        outer.add(Box.createHorizontalStrut(PADDING_WIDTH));
        outer.add(box);
        outer.add(Box.createHorizontalStrut(PADDING_WIDTH));

        add(outer);

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

    public static ExceptionStatus open(Throwable throwable, Path src) {
        PackageExceptionFrame frame = new PackageExceptionFrame(StackTraces.translate(throwable), src);
        frame.waitForClose();
        return frame.result;
    }

    public static ImageIcon getIcon() {
        if (icon != null)
            return icon;
        return icon = new ImageIcon(
            Objects.requireNonNull(PackageExceptionFrame.class.getResource("/assets/pre/icon_trans.png"))
        );
    }
}
