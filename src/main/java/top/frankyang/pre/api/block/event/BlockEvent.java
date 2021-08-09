package top.frankyang.pre.api.block.event;

import top.frankyang.pre.api.event.SuperMethodEvent;
import top.frankyang.pre.api.util.reflection.MethodContainer;

public abstract class BlockEvent<T> extends SuperMethodEvent<T> {
    protected BlockEvent(MethodContainer superMethod, Object... args) {
        super(superMethod);
    }
}
