package top.frankyang.pre.misc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

@FunctionalInterface
public interface FileOnlyVisitor<T> extends FileVisitor<T> {
    void visit(T t) throws IOException;

    @Override
    default FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        return FileVisitResult.CONTINUE;
    }

    @Override
    default FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
        visit(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    default FileVisitResult visitFileFailed(T file, IOException exc) throws IOException {
        Objects.requireNonNull(file);
        throw exc;
    }

    @Override
    default FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException {
        Objects.requireNonNull(dir);
        if (exc != null)
            throw exc;
        return FileVisitResult.CONTINUE;
    }
}
