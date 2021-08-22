package top.frankyang.pre.api.misc;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * 该接口用于创建一个列表，可以将上游列表的访问映射到另一个类。
 *
 * @param <S> 上游列表所持有的类。
 * @param <D> 该列表所要映射的类。
 */
@SuppressWarnings("NullableProblems")
public interface CastingList<S, D> extends List<D> {
    List<S> getDelegateList();

    D toDst(S s);

    S toSrc(D d);

    Class<? extends D> getDstClass();

    @Override
    default int size() {
        return getDelegateList().size();
    }

    @Override
    default boolean isEmpty() {
        return getDelegateList().isEmpty();
    }

    @Override
    default boolean contains(Object o) {
        if (getDstClass().isInstance(o))
            return getDelegateList().contains(toSrc(getDstClass().cast(o)));
        return false;
    }


    @Override
    default Iterator<D> iterator() {
        return new Iterator<D>() {
            private final Iterator<S> iterator = getDelegateList().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public D next() {
                return toDst(iterator.next());
            }
        };
    }

    @Override
    default Object[] toArray() {
        return getDelegateList().stream().map(this::toDst).toArray();
    }

    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    @Override
    default <T> T[] toArray(T[] ts) {
        return getDelegateList().stream().map(this::toDst).toArray(
            l -> (T[]) Array.newInstance(ts.getClass().getComponentType(), l)
        );
    }

    @Override
    default boolean add(D d) {
        return getDelegateList().add(toSrc(d));
    }

    @Override
    default boolean remove(Object o) {
        if (getDstClass().isInstance(o))
            return getDelegateList().contains(toSrc(getDstClass().cast(o)));
        return false;
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        return getDelegateList().containsAll(
            c.stream()
                .filter(getDstClass()::isInstance)
                .map(getDstClass()::cast)
                .map(this::toSrc)
                .collect(Collectors.toSet())
        );
    }

    @Override
    default boolean addAll(Collection<? extends D> c) {
        return getDelegateList().addAll(c.stream().map(this::toSrc).collect(Collectors.toList()));
    }

    @Override
    default boolean addAll(int index, Collection<? extends D> c) {
        return getDelegateList().addAll(index, c.stream().map(this::toSrc).collect(Collectors.toList()));
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        return getDelegateList().removeAll(
            c.stream()
                .filter(getDstClass()::isInstance)
                .map(getDstClass()::cast)
                .map(this::toSrc)
                .collect(Collectors.toSet())
        );
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        return getDelegateList().retainAll(
            c.stream()
                .filter(getDstClass()::isInstance)
                .map(getDstClass()::cast)
                .map(this::toSrc)
                .collect(Collectors.toSet())
        );
    }

    @Override
    default void clear() {
        getDelegateList().clear();
    }

    @Override
    default D get(int index) {
        return toDst(getDelegateList().get(index));
    }

    @Override
    default D set(int index, D element) {
        return toDst(getDelegateList().set(index, toSrc(element)));
    }

    @Override
    default void add(int index, D element) {
        getDelegateList().add(index, toSrc(element));
    }

    @Override
    default D remove(int index) {
        return toDst(getDelegateList().remove(index));
    }

    @Override
    default int indexOf(Object o) {
        if (getDstClass().isInstance(o))
            return getDelegateList().indexOf(toSrc(getDstClass().cast(o)));
        return -1;
    }

    @Override
    default int lastIndexOf(Object o) {
        if (getDstClass().isInstance(o))
            return getDelegateList().lastIndexOf(toSrc(getDstClass().cast(o)));
        return -1;
    }


    @Override
    default ListIterator<D> listIterator() {
        return listIterator(0);
    }


    @Override
    default ListIterator<D> listIterator(int index) {
        return new ListIterator<D>() {
            private final ListIterator<S> delegate = getDelegateList().listIterator(index);

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public D next() {
                return toDst(delegate.next());
            }

            @Override
            public boolean hasPrevious() {
                return delegate.hasPrevious();
            }

            @Override
            public D previous() {
                return toDst(delegate.previous());
            }

            @Override
            public int nextIndex() {
                return delegate.nextIndex();
            }

            @Override
            public int previousIndex() {
                return delegate.previousIndex();
            }

            @Override
            public void remove() {
                delegate.remove();
            }

            @Override
            public void set(D d) {
                delegate.set(toSrc(d));
            }

            @Override
            public void add(D d) {
                delegate.add(toSrc(d));
            }
        };
    }


    @Override
    default List<D> subList(int fromIndex, int toIndex) {
        return new CastingList<S, D>() {
            private final List<S> delegate = getDelegateList().subList(fromIndex, toIndex);

            @Override
            public List<S> getDelegateList() {
                return delegate;
            }

            @Override
            public D toDst(S s) {
                return CastingList.this.toDst(s);
            }

            @Override
            public S toSrc(D d) {
                return CastingList.this.toSrc(d);
            }

            @Override
            public Class<? extends D> getDstClass() {
                return CastingList.this.getDstClass();
            }
        };
    }
}
