package by.it.group410972.rak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> nextInBucket;
        Node<E> prevInsertion;
        Node<E> nextInsertion;

        Node(E value) { this.value = value; }
    }

    private Node<E>[] table;
    private Node<E> headInsertion;
    private Node<E> tailInsertion;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7fffffff) % table.length;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        headInsertion = null;
        tailInsertion = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> node = table[idx];
        while (node != null) {
            if (o == null ? node.value == null : o.equals(node.value)) return true;
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        int idx = index(e);
        Node<E> newNode = new Node<>(e);

        // Вставка в bucket
        newNode.nextInBucket = table[idx];
        table[idx] = newNode;

        // Вставка в порядок добавления
        if (tailInsertion == null) {
            headInsertion = tailInsertion = newNode;
        } else {
            tailInsertion.nextInsertion = newNode;
            newNode.prevInsertion = tailInsertion;
            tailInsertion = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> prev = null;
        Node<E> node = table[idx];
        while (node != null) {
            if (o == null ? node.value == null : o.equals(node.value)) {
                // Убираем из bucket
                if (prev == null) table[idx] = node.nextInBucket;
                else prev.nextInBucket = node.nextInBucket;

                // Убираем из порядка добавления
                if (node.prevInsertion != null) node.prevInsertion.nextInsertion = node.nextInsertion;
                else headInsertion = node.nextInsertion;

                if (node.nextInsertion != null) node.nextInsertion.prevInsertion = node.prevInsertion;
                else tailInsertion = node.prevInsertion;

                size--;
                return true;
            }
            prev = node;
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = headInsertion;
        boolean first = true;
        while (node != null) {
            if (!first) sb.append(", ");
            sb.append(node.value);
            first = false;
            node = node.nextInsertion;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = headInsertion;

            @Override
            public boolean hasNext() { return current != null; }

            @Override
            public E next() {
                E val = current.value;
                current = current.nextInsertion;
                return val;
            }
        };
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = headInsertion;
        while (node != null) {
            Node<E> next = node.nextInsertion;
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
