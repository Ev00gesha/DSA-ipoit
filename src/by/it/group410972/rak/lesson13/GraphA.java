package by.it.group410972.rak.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");

            for (String p : parts) {
                p = p.trim();
                if (p.isEmpty()) continue;

                // example: "A -> B"
                String[] lr = p.split("->");
                String from = lr[0].trim();
                String to = lr[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                indeg.putIfAbsent(from, 0);
                indeg.put(to, indeg.getOrDefault(to, 0) + 1);
            }
        }

        for (String v : graph.keySet()) {
            indeg.putIfAbsent(v, 0);
            for (String u : graph.get(v)) indeg.putIfAbsent(u, 0);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();

        for (var e : indeg.entrySet()) {
            if (e.getValue() == 0)
                pq.add(e.getKey());
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);

            if (graph.containsKey(v)) {
                for (String u : graph.get(v)) {
                    indeg.put(u, indeg.get(u) - 1);
                    if (indeg.get(u) == 0)
                        pq.add(u);
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}