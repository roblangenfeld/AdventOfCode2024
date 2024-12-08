package Day5.src.main.java;

import java.io.*;
import java.util.*;

public class Day5 {

    public static void main(String[] args) throws IOException {
        System.out.println("Part 1 - Sum of middle page numbers for correctly-ordered updates: " + part1());
        System.out.println("Part 2 - Sum of middle page numbers after fixing incorrectly-ordered updates: " + part2());
    }

    public static int part1() throws IOException {
        String filePath = "Day5/src/main/java/input.txt";
        List<String> orderingRules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isUpdateSection = false;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    isUpdateSection = true;
                    continue;
                }

                if (isUpdateSection) {
                    String[] pages = line.split(",");
                    List<Integer> update = new ArrayList<>();
                    for (String page : pages) {
                        update.add(Integer.parseInt(page.trim()));
                    }
                    updates.add(update);
                } else {
                    orderingRules.add(line.trim());
                }
            }
        }

        Map<Integer, List<Integer>> precedenceMap = new HashMap<>();
        for (String rule : orderingRules) {
            String[] parts = rule.split("\\|");
            int before = Integer.parseInt(parts[0]);
            int after = Integer.parseInt(parts[1]);
            precedenceMap.computeIfAbsent(before, k -> new ArrayList<>()).add(after);
        }

        List<Integer> validMiddlePages = new ArrayList<>();

        for (List<Integer> update : updates) {
            if (isValidOrder(update, precedenceMap)) {
                int middlePage = update.get(update.size() / 2);
                validMiddlePages.add(middlePage);
            }
        }

        return validMiddlePages.stream().mapToInt(Integer::intValue).sum();
    }

    public static int part2() throws IOException {
        String filePath = "Day5/src/main/java/input.txt";
        List<String> orderingRules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isUpdateSection = false;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    isUpdateSection = true;
                    continue;
                }

                if (isUpdateSection) {
                    String[] pages = line.split(",");
                    List<Integer> update = new ArrayList<>();
                    for (String page : pages) {
                        update.add(Integer.parseInt(page.trim()));
                    }
                    updates.add(update);
                } else {
                    orderingRules.add(line.trim());
                }
            }
        }

        Map<Integer, List<Integer>> precedenceMap = new HashMap<>();
        for (String rule : orderingRules) {
            String[] parts = rule.split("\\|");
            int before = Integer.parseInt(parts[0]);
            int after = Integer.parseInt(parts[1]);
            precedenceMap.computeIfAbsent(before, k -> new ArrayList<>()).add(after);
        }

        List<Integer> fixedMiddlePages = new ArrayList<>();

        for (List<Integer> update : updates) {
            if (!isValidOrder(update, precedenceMap)) {
                List<Integer> fixedUpdate = sortUpdate(update, precedenceMap);
                int middlePage = fixedUpdate.get(fixedUpdate.size() / 2);
                fixedMiddlePages.add(middlePage);
            }
        }

        return fixedMiddlePages.stream().mapToInt(Integer::intValue).sum();
    }

    private static boolean isValidOrder(List<Integer> update, Map<Integer, List<Integer>> precedenceMap) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < update.size(); i++) {
            indexMap.put(update.get(i), i);
        }

        for (Map.Entry<Integer, List<Integer>> entry : precedenceMap.entrySet()) {
            int before = entry.getKey();
            if (!indexMap.containsKey(before)) continue;

            for (int after : entry.getValue()) {
                if (indexMap.containsKey(after) && indexMap.get(before) > indexMap.get(after)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> sortUpdate(List<Integer> update, Map<Integer, List<Integer>> precedenceMap) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        for (int page : update) {
            graph.put(page, new ArrayList<>());
            inDegree.put(page, 0);
        }

        for (Map.Entry<Integer, List<Integer>> entry : precedenceMap.entrySet()) {
            int before = entry.getKey();
            if (!graph.containsKey(before)) continue;

            for (int after : entry.getValue()) {
                if (graph.containsKey(after)) {
                    graph.get(before).add(after);
                    inDegree.put(after, inDegree.get(after) + 1);
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<Integer> sortedUpdate = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            sortedUpdate.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return sortedUpdate;
    }
}
