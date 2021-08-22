package top.frankyang.pre.api.item.group;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import top.frankyang.pre.api.misc.DelegatedCastable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 包装类，包装原版类{@link ItemGroup}。
 */
public class Group extends DelegatedCastable<ItemGroup> implements GroupLike {
    protected Group(ItemGroup delegate) {
        super(delegate);
    }

    public static Builder of(String id) {
        return new Builder(FabricItemGroupBuilder.create(new Identifier(id)));
    }

    public static class Builder {
        private final FabricItemGroupBuilder fabricItemGroupBuilder;

        public Builder(FabricItemGroupBuilder fabricItemGroupBuilder) {
            this.fabricItemGroupBuilder = fabricItemGroupBuilder;
        }

        public Builder icon(Supplier<ItemStack> stackSupplier) {
            fabricItemGroupBuilder.icon(stackSupplier);
            return this;
        }

        public Builder stacksForDisplay(Consumer<List<ItemStack>> appender) {
            fabricItemGroupBuilder.stacksForDisplay(appender);
            return this;
        }

        public Builder appendItems(Consumer<List<ItemStack>> stacksForDisplay) {
            fabricItemGroupBuilder.appendItems(stacksForDisplay);
            return this;
        }

        public Group build() {
            return new Group(fabricItemGroupBuilder.build());
        }
    }
}
