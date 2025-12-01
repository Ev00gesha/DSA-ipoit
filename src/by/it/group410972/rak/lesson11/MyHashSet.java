package by.it.group410972.rak.lesson11;

import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<E>[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7fffffff) % table.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> node = table[idx];
        while (node != null) {
            if (o == null ? node.value == null : o.equals(node.value)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> node = table[idx];
        while (node != null) {
            if (e == null ? node.value == null : e.equals(node.value)) return false;
            node = node.next;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[idx];
        table[idx] = newNode;
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
                if (prev == null) table[idx] = node.next;
                else prev.next = node.next;
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> current = null;

            private void advance() {
                while ((current == null) && bucket < table.length) {
                    current = table[bucket++];
                }
            }

            @Override
            public boolean hasNext() {
                advance();
                return current != null;
            }

            @Override
            public E next() {
                advance();
                E val = current.value;
                current = current.next;
                return val;
            }
        };
    }

    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
