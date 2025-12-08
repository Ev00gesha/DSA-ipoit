package by.it.group410972.rak.lesson14;

import java.util.*;

public class SitesB {

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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> siteIndex = new HashMap<>();
        List<String> sites = new ArrayList<>();
        List<int[]> connections = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            for (String part : parts) {
                if (!siteIndex.containsKey(part)) {
                    siteIndex.put(part, sites.size());
                    sites.add(part);
                }
            }
            connections.add(new int[]{siteIndex.get(parts[0]), siteIndex.get(parts[1])});
        }

        int N = sites.size();
        DSU dsu = new DSU(N);
        for (int[] conn : connections) {
            dsu.union(conn[0], conn[1]);
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
