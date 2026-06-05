# Алгоритм Беллмана-Форда (Bellman-Ford Algorithm)

## Intuition
Алгоритм Беллмана-Форда находит кратчайшие пути от одной вершины до всех остальных во взвешенном графе, даже если в графе есть ребра с отрицательным весом. В отличие от алгоритма Дейкстры, он может обрабатывать отрицательные веса и обнаруживать циклы с отрицательным весом. Представьте, что вы пытаетесь найти самый дешевый маршрут, но некоторые дороги могут "платить вам" за проезд (отрицательный вес). Алгоритм многократно ослабляет ребра, постепенно улучшая оценки расстояний.

## Approach
1. Инициализируем расстояния: dist[start] = 0, все остальные = ∞
2. Повторяем V-1 раз (V — количество вершин):
    - Для каждого ребра (u, v) с весом w:
        - Если dist[u] + w < dist[v], обновляем dist[v]
3. Проверяем наличие циклов с отрицательным весом:
    - Проходим по всем ребрам еще раз
    - Если находим возможность улучшить расстояние — есть отрицательный цикл

## Complexity
- Time complexity: **O(V * E)**
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class BellmanFord {
    
    // Класс для представления ребра
    static class Edge {
        int src, dest, weight;
        
        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }
    
    static class Graph {
        int vertices;
        List<Edge> edges;
        
        Graph(int vertices) {
            this.vertices = vertices;
            edges = new ArrayList<>();
        }
        
        void addEdge(int src, int dest, int weight) {
            edges.add(new Edge(src, dest, weight));
        }
        
        // Алгоритм Беллмана-Форда
        public void bellmanFord(int start) {
            int[] dist = new int[vertices];
            int[] parent = new int[vertices];
            
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[start] = 0;
            
            // Шаг 1: расслабляем ребра V-1 раз
            for (int i = 1; i < vertices; i++) {
                boolean updated = false;
                for (Edge edge : edges) {
                    if (dist[edge.src] != Integer.MAX_VALUE && 
                        dist[edge.src] + edge.weight < dist[edge.dest]) {
                        dist[edge.dest] = dist[edge.src] + edge.weight;
                        parent[edge.dest] = edge.src;
                        updated = true;
                    }
                }
                if (!updated) break; // Раннее завершение
            }
            
            // Шаг 2: проверка отрицательных циклов
            boolean hasNegativeCycle = false;
            for (Edge edge : edges) {
                if (dist[edge.src] != Integer.MAX_VALUE && 
                    dist[edge.src] + edge.weight < dist[edge.dest]) {
                    hasNegativeCycle = true;
                    break;
                }
            }
            
            if (hasNegativeCycle) {
                System.out.println("Граф содержит цикл с отрицательным весом!");
                return;
            }
            
            // Вывод результатов
            System.out.println("Кратчайшие расстояния от вершины " + start + ":");
            for (int i = 0; i < vertices; i++) {
                System.out.println("Вершина " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]));
            }
        }
        
        // Нахождение кратчайшего пути между двумя вершинами
        public List<Integer> shortestPath(int start, int end) {
            int[] dist = new int[vertices];
            int[] parent = new int[vertices];
            
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[start] = 0;
            
            for (int i = 1; i < vertices; i++) {
                boolean updated = false;
                for (Edge edge : edges) {
                    if (dist[edge.src] != Integer.MAX_VALUE && 
                        dist[edge.src] + edge.weight < dist[edge.dest]) {
                        dist[edge.dest] = dist[edge.src] + edge.weight;
                        parent[edge.dest] = edge.src;
                        updated = true;
                    }
                }
                if (!updated) break;
            }
            
            // Проверка отрицательного цикла
            for (Edge edge : edges) {
                if (dist[edge.src] != Integer.MAX_VALUE && 
                    dist[edge.src] + edge.weight < dist[edge.dest]) {
                    return null; // Отрицательный цикл
                }
            }
            
            // Восстановление пути
            List<Integer> path = new ArrayList<>();
            if (dist[end] == Integer.MAX_VALUE) return path;
            
            for (int v = end; v != -1; v = parent[v]) {
                path.add(v);
            }
            Collections.reverse(path);
            return path;
        }
        
        // Поиск всех вершин, достижимых с отрицательными циклами
        public boolean[] detectNegativeCycleAffected() {
            int[] dist = new int[vertices];
            Arrays.fill(dist, 0); // Все расстояния 0 для поиска циклов
            
            // V итераций релаксации
            for (int i = 0; i < vertices; i++) {
                for (Edge edge : edges) {
                    if (dist[edge.src] + edge.weight < dist[edge.dest]) {
                        dist[edge.dest] = dist[edge.src] + edge.weight;
                        if (i == vertices - 1) {
                            // Вершины, достижимые из цикла
                            markAffectedVertices(edge.dest);
                        }
                    }
                }
            }
            
            return affected;
        }
        
        private boolean[] affected;
        
        private void markAffectedVertices(int start) {
            affected = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            affected[start] = true;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge edge : edges) {
                    if (edge.src == u && !affected[edge.dest]) {
                        affected[edge.dest] = true;
                        queue.offer(edge.dest);
                    }
                }
            }
        }
    }
}