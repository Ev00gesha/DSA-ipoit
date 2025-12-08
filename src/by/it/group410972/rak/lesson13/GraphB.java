package by.it.group410972.rak.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        sc.close();

        Map<Integer, List<Integer>> graph = new HashMap<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            for (String p : parts) {
                p = p.trim();
                if (p.isEmpty()) continue;

                String[] lr = p.split("->");
                int from = Integer.parseInt(lr[0].trim());
                int to = Integer.parseInt(lr[1].trim());

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);


                graph.putIfAbsent(to, new ArrayList<>());
            }
        }


        Map<Integer, Integer> color = new HashMap<>();
        for (int node : graph.keySet()) {
            color.put(node, 0);
        }

        boolean[] hasCycle = {false};

        for (int node : graph.keySet()) {
            if (color.get(node) == 0) {
                dfs(node, graph, color, hasCycle);
                if (hasCycle[0]) break;
            }
        }

        System.out.println(hasCycle[0] ? "yes" : "no");
    }

    private static void dfs(int u, Map<Integer, List<Integer>> graph, Map<Integer, Integer> color, boolean[] hasCycle) {
        color.put(u, 1);
        for (int v : graph.get(u)) {
            if (color.get(v) == 1) {

                hasCycle[0] = true;
                return;
            } else if (color.get(v) == 0) {
                dfs(v, graph, color, hasCycle);
                if (hasCycle[0]) return;
            }
        }
        color.put(u, 2);
    }
}