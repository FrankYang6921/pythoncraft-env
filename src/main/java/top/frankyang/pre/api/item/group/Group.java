package top.frankyang.pre.api.item.group;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.python.core.PyDictionary;
import org.python.core.PySequenceList;
import top.frankyang.pre.api.item.ItemRegistry;
import top.frankyang.pre.api.misc.DelegatedConvertable;
import top.frankyang.pre.api.util.TypedDictionary;

import java.util.stream.Stream;

/**
 * 包装类，包装原版类<code>ItemGroup</code>。
 */
public class Group extends DelegatedConvertable<ItemGroup> implements GroupLike {
    protected Group(ItemGroup delegate) {
        super(delegate);
    }

    /**
     * 创建一个物品组。
     *
     * @param dictionary 所要创建的物品组的属性。
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static Group of(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);

        FabricItemGroupBuilder fabricItemGroupBuilder =
            FabricItemGroupBuilder.create(new Identifier(dict.getRequired("id", String.class)));
        dict.ifPresent("icon", String.class, s -> {
            fabricItemGroupBuilder.icon(() -> new ItemStack(ItemRegistry.lookup(new Identifier(s))));
        });
        dict.ifPresent("items", PySequenceList.class, p -> {
            fabricItemGroupBuilder.appendItems(s -> {
                ((Stream<?>) p.stream())
                    .map(Object::toString)
                    .map(Identifier::new)
                    .map(ItemRegistry::lookup)
                    .map(ItemStack::new)
                    .forEach(s::add);
            });
        });

        return new Group(fabricItemGroupBuilder.build());
    }
}
