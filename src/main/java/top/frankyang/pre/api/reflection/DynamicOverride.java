package top.frankyang.pre.api.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicOverride {
    /**
     * 该方法所要重写的方法名。如果为空，则表示要重写同名的方法。
     *
     * @return 该方法所要重写的方法名。
     */
    String value() default "";

    /**
     * 表明该方法是一个常量方法。这意味着此方法的返回值不因参数（如果有）而改变。动态重写系统在处理此方法时仅会调用它一次，然后将它的返回值缓存。该方法可极大程度上提高一个假getter的性能。
     *
     * @return 该方法是否是一个常量方法。
     */
    boolean constant() default false;

    /**
     * 表明该方法必须重写某个方法。如果该字段设为{@code false}，动态重写器在处理这个方法时倘若没有找到相应的父类方法，不会发生任何事，反之则会抛出一个异常，迫使程序崩溃。
     *
     * @return 该方法是否必须重写某个方法。
     */
    boolean required() default true;
}
