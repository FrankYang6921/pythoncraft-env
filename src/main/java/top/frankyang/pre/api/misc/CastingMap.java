package top.frankyang.pre.api.misc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 该接口用于创建一个表，可以将上游表的访问映射到另一个类。
 *
 * @param <K> 该表和上游表的键类型。
 * @param <S> 上游表所持有的值的类。
 * @param <D> 该表所要映射的值的类。
 */
public interface CastingMap<K, S, D> extends Map<K, D> {
    Map<K, S> getDelegateMap();

    D toDst(S s);

    S toSrc(D d);

    Class<? extends D> getDstClass();

    @Override
    default int size() {
        return getDelegateMap().size();
    }

    @Override
    default boolean isEmpty() {
        return getDelegateMap().isEmpty();
    }

    @Override
    default boolean containsKey(Object key) {
        return getDelegateMap().containsKey(key);
    }

    @Override
    default boolean containsValue(Object value) {
        if (getDstClass().isInstance(value))
            return getDelegateMap().containsValue(toSrc(getDstClass().cast(value)));
        return false;
    }

    @Override
    default D get(Object key) {
        return toDst(getDelegateMap().get(key));
    }

    @Override
    default D put(K key, D value) {
        return toDst(getDelegateMap().put(key, toSrc(value)));
    }

    @Override
    default D remove(Object key) {
        return toDst(getDelegateMap().remove(key));
    }

    @Override
    default void putAll(Map<? extends K, ? extends D> m) {
        m.forEach(this::put);
    }

    @Override
    default void clear() {
        getDelegateMap().clear();
    }

    @Override
    default Set<K> keySet() {
        return getDelegateMap().keySet();
    }

    @Override
    default Collection<D> values() {
        return getDelegateMap().values().stream().map(this::toDst).collect(Collectors.toList());
    }

    @Override
    default Set<Entry<K, D>> entrySet() {
        return keySet().stream().map(k -> new CastingEntry<>(this, k)).collect(Collectors.toSet());
    }

    final class CastingEntry<K, S, D> implements Entry<K, D> {
        private final CastingMap<K, S, D> map;
        private final K key;
        private volatile D value;

        private CastingEntry(CastingMap<K, S, D> map, K key) {
            this.map = map;
            this.key = key;
            this.value = map.get(key);
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public D getValue() {
            return value;
        }

        @Override
        public D setValue(D value) {
            map.put(key, value);
            return this.value = value;
        }
    }
}
