package by.it.group410972.rak.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        Node(Integer k, String v, Node p) { key = k; value = v; parent = p; }
    }

    private Node root;
    private int size = 0;

    public MySplayMap() {}

    private void rotateRight(Node x) {
        Node p = x.parent;
        if (p == null) return;
        Node g = p.parent;

        p.left = x.right;
        if (x.right != null) x.right.parent = p;

        x.right = p;
        p.parent = x;

        x.parent = g;
        if (g != null) {
            if (g.left == p) g.left = x; else g.right = x;
        } else {
            root = x;
        }
    }

    private void rotateLeft(Node x) {
        Node p = x.parent;
        if (p == null) return;
        Node g = p.parent;

        p.right = x.left;
        if (x.left != null) x.left.parent = p;

        x.left = p;
        p.parent = x;

        x.parent = g;
        if (g != null) {
            if (g.left == p) g.left = x; else g.right = x;
        } else {
            root = x;
        }
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // zig
                if (p.left == x) rotateRight(x); else rotateLeft(x);
            } else if (g.left == p && p.left == x) {
                // zig-zig
                rotateRight(p);
                rotateRight(x);
            } else if (g.right == p && p.right == x) {
                // zig-zig
                rotateLeft(p);
                rotateLeft(x);
            } else if (g.left == p && p.right == x) {
                // zig-zag
                rotateLeft(x);
                rotateRight(x);
            } else {
                // zig-zag
                rotateRight(x);
                rotateLeft(x);
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return null;
    }

    private Node findClosestNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            if (cmp < 0) cur = cur.left; else cur = cur.right;
        }
        return last;
    }


    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }
        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        Node node = new Node(key, value, parent);
        if (key.compareTo(parent.key) < 0) parent.left = node; else parent.right = node;
        splay(node);
        size++;
        return null;
    }

    @Override
    public String remove(Object k) {
        if (!(k instanceof Integer)) return null;
        Integer key = (Integer) k;
        Node node = findNode(key);
        if (node == null) return null;
        splay(node);
        String old = node.value;

        if (node.left == null) {
            transplant(node, node.right);
        } else if (node.right == null) {
            transplant(node, node.left);
        } else {
            Node leftSub = node.left;
            leftSub.parent = null;
            Node rightSub = node.right;
            rightSub.parent = null;
            Node max = leftSub;
            while (max.right != null) max = max.right;
            splay(max);
            max.right = rightSub;
            if (rightSub != null) rightSub.parent = max;
            root = max;
        }
        size--;
        if (size == 0) root = null;
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) v.parent = u.parent;
    }

    @Override
    public String get(Object k) {
        if (!(k instanceof Integer)) return null;
        Node node = findNode((Integer) k);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object k) {
        if (!(k instanceof Integer)) return false;
        Node n = findNode((Integer) k);
        return n != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
        // inorder traversal search
        Deque<Node> stack = new ArrayDeque<>();
        Node cur = root;
        while (!stack.isEmpty() || cur != null) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            if (Objects.equals(cur.value, value)) {
                splay(cur); // splay where found (optional)
                return true;
            }
            cur = cur.right;
        }
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer candidate = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp <= 0) {
                cur = cur.left;
            } else {
                candidate = cur.key;
                cur = cur.right;
            }
        }
        if (candidate != null) {
            Node found = findNode(candidate);
            // findNode splays found
        }
        return candidate;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer candidate = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur.key;
            }
            if (cmp < 0) {
                cur = cur.left;
            } else {
                candidate = cur.key;
                cur = cur.right;
            }
        }
        if (candidate != null) findNode(candidate);
        return candidate;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer candidate = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur.key;
            }
            if (cmp > 0) {
                cur = cur.right;
            } else {
                candidate = cur.key;
                cur = cur.left;
            }
        }
        if (candidate != null) findNode(candidate);
        return candidate;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer candidate = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp >= 0) {
                cur = cur.right;
            } else {
                candidate = cur.key;
                cur = cur.left;
            }
        }
        if (candidate != null) findNode(candidate);
        return candidate;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap m = new MySplayMap();
        inOrderInsert(root, m, (k) -> k.compareTo(toKey) < 0);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        inOrderInsert(root, m, (k) -> k.compareTo(fromKey) >= 0);
        return m;
    }

    private interface KeyFilter { boolean accept(Integer k); }

    private void inOrderInsert(Node node, MySplayMap map, KeyFilter filter) {
        if (node == null) return;
        inOrderInsert(node.left, map, filter);
        if (filter.accept(node.key)) map.put(node.key, node.value);
        inOrderInsert(node.right, map, filter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        Deque<Node> stack = new ArrayDeque<>();
        Node cur = root;
        while (!stack.isEmpty() || cur != null) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            if (!first) sb.append(", ");
            first = false;
            sb.append(String.valueOf(cur.key)).append("=").append(String.valueOf(cur.value));
            cur = cur.right;
        }
        sb.append("}");
        return sb.toString();
    }


    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    private static class EntryImpl implements Entry<Integer, String> {
        private final Integer k;
        private final String v;
        EntryImpl(Integer k, String v) { this.k = k; this.v = v; }

        @Override public Integer getKey() { return k; }
        @Override public String getValue() { return v; }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }
    }


    @Override
    public Entry<Integer, String> firstEntry() {
        if (root == null) return null;
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return new EntryImpl(cur.key, cur.value);
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (root == null) return null;
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return new EntryImpl(cur.key, cur.value);
    }

    // Methods from NavigableMap not needed by tests -> simple UnsupportedOperationException or minimal
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }

    // Other Map methods
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        Map<?,?> m = (Map<?,?>) o;
        if (m.size() != size()) return false;
        try {
            for (Map.Entry<Integer,String> e : entrySet()) {
                Integer k = e.getKey();
                String v = e.getValue();
                if (!Objects.equals(v, m.get(k))) return false;
            }
        } catch (UnsupportedOperationException ex) {
            // entrySet unsupported -> fallback scan by keys via traversal
            Deque<Node> stack = new ArrayDeque<>();
            Node cur = root;
            while (!stack.isEmpty() || cur != null) {
                while (cur != null) { stack.push(cur); cur = cur.left; }
                cur = stack.pop();
                if (!Objects.equals(cur.value, m.get(cur.key))) return false;
                cur = cur.right;
            }
        }
        return true;
    }

    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }

    // -------------------- End --------------------
}
