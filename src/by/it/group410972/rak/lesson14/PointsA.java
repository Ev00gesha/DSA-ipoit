package by.it.group410972.rak.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int pa = find(a);
            int pb = find(b);
            if (pa == pb) return;
            if (size[pa] < size[pb]) {
                parent[pa] = pb;
                size[pb] += size[pa];
            } else {
                parent[pb] = pa;
                size[pa] += size[pb];
            }
        }
    }

    static class Point {
        int x, y, z;
        Point(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
        double dist(Point other) {
            long dx = x - other.x;
            long dy = y - other.y;
            long dz = z - other.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        sc.nextLine();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            String[] parts = sc.nextLine().split(" ");
            points[i] = new Point(Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]));
        }

        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].dist(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort(Collections.reverseOrder());
        for (int sz : sizes) {
            System.out.print(sz + " ");
        }
    }
}
