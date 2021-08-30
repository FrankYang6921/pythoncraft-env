package top.frankyang.pre.api.block.entity;

import sun.misc.Unsafe;
import top.frankyang.pre.api.util.LooseSupplier;
import top.frankyang.pre.api.util.ReflectUtils;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

/**
 * 对象方块实体的工厂。
 *
 * @param <T> 该方块实体所持有对象的类型。
 */
public class ObjectBlockEntityFactory<T> extends BlockEntityFactory {
    private final Supplier<? extends T> supplier;

    public ObjectBlockEntityFactory(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * 按指定的类创建一个工厂。该工厂会用正常的方式创建该类的实例。
     *
     * @param clazz 指定的类对象。
     * @param <T>   指定的类。
     * @return 创建的工厂。
     */
    public static <T> ObjectBlockEntityFactory<T> ofMild(Class<T> clazz) {
        Constructor<T> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new ObjectBlockEntityFactory<>((LooseSupplier<T>) constructor::newInstance);
    }

    /**
     * 按指定的类创建一个工厂。该工厂会用{@link Unsafe}创建该类的实例。正如其名，该方法可以通过黑魔法强制创建一个实例，即便它没有无参构造器（哪怕有无参构造器也不会调用它）。一般不推荐使用这个方法，除非您使用的不是一个您自己创建的类。
     *
     * @param clazz 指定的类对象。
     * @param <T>   指定的类。
     * @return 创建的工厂。
     */
    public static <T> ObjectBlockEntityFactory<T> ofForce(Class<T> clazz) {
        return new ObjectBlockEntityFactory<>(() -> ReflectUtils.forceNew(clazz));
    }

    @Override
    public BlockEntityLike newBlockEntity(BlockEntityFactory factory) {
        return new ObjectBlockEntityImpl<>(this, supplier);
    }
}
