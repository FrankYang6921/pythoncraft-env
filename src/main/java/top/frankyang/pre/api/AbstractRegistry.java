package top.frankyang.pre.api;

import net.minecraft.util.Identifier;

public abstract class AbstractRegistry {
    private final String namespace;

    protected AbstractRegistry(String namespace) {
        this.namespace = namespace;
    }

    protected Identifier getIdentifier(String identifier) {
        return new Identifier(namespace, identifier);
    }
}
