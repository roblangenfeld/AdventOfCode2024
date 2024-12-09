package Day8.src.main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day8 {

    private static class Point {
        int x, y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
    
    public static int calculateAntiNodes(String[] grid) {
        // Find antenna locations
        Map<Character, List<Point>> antennaFrequencies = findAntennas(grid);
        
        // Set to track unique antinode locations
        Set<Point> antinodes = new HashSet<>();
        
        int maxX = grid[0].length() - 1;
        int maxY = grid.length - 1;
        
        // For each frequency, find antinodes
        for (Map.Entry<Character, List<Point>> entry : antennaFrequencies.entrySet()) {
            List<Point> frequencyAntennas = entry.getValue();
            
            for (int i = 0; i < frequencyAntennas.size(); i++) {
                for (int j = i + 1; j < frequencyAntennas.size(); j++) {
                    Point a1 = frequencyAntennas.get(i);
                    Point a2 = frequencyAntennas.get(j);
                    
                    findAntinodes(a1, a2, antinodes, maxX, maxY);
                }
            }
        }
        
        // Debugging
        System.out.println("Detailed Antinodes:");
        for (Point p : antinodes) {
            System.out.println(p);
        }
        
        return antinodes.size();
    }
    
    private static void findAntinodes(Point a1, Point a2, Set<Point> antinodes, int maxX, int maxY) {
        // Calculate the distance between the antennas
        int dx = a2.x - a1.x;
        int dy = a2.y - a1.y;
        
        // Calculate potential antinode positions based on the distance
        Point antinode1 = new Point(a1.x + 2 * dx, a1.y + 2 * dy);
        Point antinode2 = new Point(a2.x - 2 * dx, a2.y - 2 * dy);
        
        // Only add antinodes within grid bounds
        if (isWithinBounds(antinode1, maxX, maxY)) {
            antinodes.add(antinode1);
        }
        if (isWithinBounds(antinode2, maxX, maxY)) {
            antinodes.add(antinode2);
        }
        // Debugging output to track added antinodes
        System.out.println("Antinode1: " + antinode1);
        System.out.println("Antinode2: " + antinode2);
    }
    
    private static boolean isWithinBounds(Point p, int maxX, int maxY) {
        return p.x >= 0 && p.x <= maxX && p.y >= 0 && p.y <= maxY;
    }
    
    private static Map<Character, List<Point>> findAntennas(String[] grid) {
        Map<Character, List<Point>> antennaFrequencies = new HashMap<>();
        
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length(); x++) {
                char c = grid[y].charAt(x);
                if (c != '.') {
                    antennaFrequencies.computeIfAbsent(c, k -> new ArrayList<>())
                        .add(new Point(x, y));
                }
            }
        }
        
        return antennaFrequencies;
    }
    
    public static int part2(String[] grid) {
    Map<Character, List<Point>> antennaFrequencies = findAntennas(grid);
    Set<Point> antinodes = new HashSet<>();
    
    for (Map.Entry<Character, List<Point>> entry : antennaFrequencies.entrySet()) {
        List<Point> frequencyAntennas = entry.getValue();
        
        // Only process frequencies with at least 2 antennas
        if (frequencyAntennas.size() < 2) continue;
        
        // Check every pair of antennas
        for (int i = 0; i < frequencyAntennas.size(); i++) {
            for (int j = i + 1; j < frequencyAntennas.size(); j++) {
                Point a1 = frequencyAntennas.get(i);
                Point a2 = frequencyAntennas.get(j);
                
                // Check all points between and including the antennas
                findStrictLinePoints(a1, a2, antinodes, grid);
            }
        }
    }
    
    return antinodes.size();
}

private static void findStrictLinePoints(Point a1, Point a2, Set<Point> antinodes, String[] grid) {
    int maxX = grid[0].length() - 1;
    int maxY = grid.length - 1;
    
    // Check if points are perfectly aligned
    boolean alignedX = a1.x == a2.x;
    boolean alignedY = a1.y == a2.y;
    
    if (!alignedX && !alignedY) {
        // Check if diagonal line is perfectly aligned
        double slope = Math.abs((double)(a2.y - a1.y) / (a2.x - a1.x));
        if (Math.abs(slope - Math.round(slope)) > 1e-10) {
            return;  // Not perfectly aligned
        }
    }
    
    // Determine start and end points
    int startX = Math.min(a1.x, a2.x);
    int endX = Math.max(a1.x, a2.x);
    int startY = Math.min(a1.y, a2.y);
    int endY = Math.max(a1.y, a2.y);
    
    // Add points along the line
    if (alignedX) {
        for (int y = startY; y <= endY; y++) {
            Point p = new Point(a1.x, y);
            if (isValidPoint(p, maxX, maxY)) {
                antinodes.add(p);
            }
        }
    } else if (alignedY) {
        for (int x = startX; x <= endX; x++) {
            Point p = new Point(x, a1.y);
            if (isValidPoint(p, maxX, maxY)) {
                antinodes.add(p);
            }
        }
    } else {
        // Diagonal alignment
        int stepX = startX < endX ? 1 : -1;
        int stepY = startY < endY ? 1 : -1;
        
        int x = startX;
        int y = startY;
        
        while (x != endX + stepX && y != endY + stepY) {
            Point p = new Point(x, y);
            if (isValidPoint(p, maxX, maxY)) {
                antinodes.add(p);
            }
            x += stepX;
            y += stepY;
        }
    }
}

private static boolean isValidPoint(Point p, int maxX, int maxY) {
    return p.x >= 0 && p.x <= maxX && p.y >= 0 && p.y <= maxY;
}
    
    public static void main(String[] args) {
        String filePath = "Day8/src/main/java/input.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }
        
        // Convert List<String> to String[]
        String[] grid = lines.toArray(new String[0]);
        
        int result = calculateAntiNodes(grid);
        System.out.println("Number of unique antinode locations: " + result);
        
        int result2 = part2(grid);
        System.out.println("Number of unique antinode locations (part 2): " + result2);
    }
    
}
