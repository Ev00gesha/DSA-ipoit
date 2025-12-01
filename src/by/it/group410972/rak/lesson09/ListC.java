package by.it.group410972.rak.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private Object[] elementData;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ListC() {
        this.elementData = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ListC(int initialCapacity) {
        if (initialCapacity < 0) throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        this.elementData = new Object[Math.max(DEFAULT_CAPACITY, initialCapacity)];
        this.size = 0;
    }

    // ---- Utility ----
    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elementData.length) return;
        int newCapacity = elementData.length + (elementData.length >> 1); // 1.5x
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        Object[] newArr = new Object[newCapacity];
        for (int i = 0; i < size; i++) newArr[i] = elementData[i];
        elementData = newArr;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) {
        return (E) elementData[index];
    }

    // ----------------- Обязательные методы -----------------

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elementData[i] == this ? "(this Collection)" : String.valueOf(elementData[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            for (int i = index; i < index + numMoved; i++) {
                elementData[i] = elementData[i + 1];
            }
        }
        elementData[--size] = null; // help GC
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    // -------------- Опциональные (реализованные) ----------------

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elementData[index];
        elementData[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elementData[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) if (elementData[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++) if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) if (elementData[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--) if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        Object[] a = new Object[c.size()];
        int i = 0;
        for (E e : c) a[i++] = e;
        ensureCapacity(size + a.length);
        for (Object o : a) elementData[size++] = o;
        return a.length != 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        if (c == null) throw new NullPointerException();
        int numNew = c.size();
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        // move tail
        for (int i = size - 1; i >= index; i--) elementData[i + numNew] = elementData[i];
        int j = index;
        for (E e : c) elementData[j++] = e;
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object o = elementData[i];
            if (!c.contains(o)) {
                elementData[newSize++] = o;
            } else {
                modified = true;
            }
        }
        for (int i = newSize; i < size; i++) elementData[i] = null;
        size = newSize;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object o = elementData[i];
            if (c.contains(o)) {
                elementData[newSize++] = o;
            } else {
                modified = true;
            }
        }
        for (int i = newSize; i < size; i++) elementData[i] = null;
        size = newSize;
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        ListA<E> sub = new ListA<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) sub.add(elementData(i));
        return sub;
    }

    // ---- Iterators and ListIterators ----

    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListC.Itr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListC.Itr(0);
    }

    private class Itr implements ListIterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if none

        Itr(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            if (cursor >= size) throw new NoSuchElementException();
            lastRet = cursor;
            return elementData(cursor++);
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            if (cursor <= 0) throw new NoSuchElementException();
            lastRet = --cursor;
            return elementData(lastRet);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            int i = cursor;
            ListC.this.add(i, e);
            cursor = i + 1;
            lastRet = -1;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) result[i] = elementData[i];
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // create new array of runtime type of a
            T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) newArr[i] = (T) elementData[i];
            return newArr;
        } else {
            for (int i = 0; i < size; i++) a[i] = (T) elementData[i];
            if (a.length > size) a[size] = null;
            return a;
        }
    }

    // Iterator
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor != size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return elementData(cursor++);
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListC.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }
}
