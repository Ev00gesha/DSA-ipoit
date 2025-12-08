package by.it.group410972.rak.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x.parent.left == x) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x.parent.left == x) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left;
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size = 1;
            return null;
        }
        Node current = root;
        Node parent = null;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                String old = current.value;
                current.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    private Node getNode(Integer key) {
        Node node = root;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maximum(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private Node successor(Node node) {
        if (node.right != null) return minimum(node.right);
        Node parent = node.parent;
        while (parent != null && node == parent.right) {
            node = parent;
            parent = parent.parent;
        }
        return parent;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode((Integer) key);
        if (node == null) return null;
        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node z) {
        Node y = (z.left == null || z.right == null) ? z : successor(z);
        Node x = (y.left != null) ? y.left : y.right;
        if (x != null) x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        if (y != z) z.key = y.key; z.value = y.value;
        // Для уровня A/B балансировка после удаления можно пропустить
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Integer firstKey() {
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        return maximum(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, map, toKey);
        return map;
    }

    private void headMap(Node node, MyRbMap map, Integer toKey) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
            headMap(node.left, map, toKey);
            headMap(node.right, map, toKey);
        } else {
            headMap(node.left, map, toKey);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, map, fromKey);
        return map;
    }

    private void tailMap(Node node, MyRbMap map, Integer fromKey) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            tailMap(node.left, map, fromKey);
            tailMap(node.right, map, fromKey);
        } else {
            tailMap(node.right, map, fromKey);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap map = new MyRbMap();
        subMap(root, map, fromKey, toKey);
        return map;
    }

    private void subMap(Node node, MyRbMap map, Integer fromKey, Integer toKey) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
        }

        if (fromKey < node.key) subMap(node.left, map, fromKey, toKey);
        if (toKey > node.key) subMap(node.right, map, fromKey, toKey);
    }

    // Остальные методы Map/SortedMap оставляем пустыми или с null для уровня A/B
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {}
}