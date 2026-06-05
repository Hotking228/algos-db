# Алгоритм Джонсона (Johnson's Algorithm)

## Intuition
Алгоритм Джонсона находит кратчайшие пути между всеми парами вершин во взвешенном ориентированном графе, эффективно комбинируя алгоритмы Беллмана-Форда и Дейкстры. Он работает с отрицательными весами, но не с отрицательными циклами. Идея в том, чтобы переназначить веса ребер, сделав их неотрицательными (с помощью потенциалов вершин), после чего применить V раз алгоритм Дейкстры. Это быстрее, чем Флойд-Уоршелл для разреженных графов.

## Approach
1. Добавляем фиктивную вершину s, соединяем её со всеми вершинами ребрами веса 0
2. Запускаем Беллмана-Форда от s, получаем потенциалы h(v) = кратчайшее расстояние от s до v
3. Если обнаружен отрицательный цикл — алгоритм завершается
4. Переназначаем веса всех ребер: w'(u,v) = w(u,v) + h(u) - h(v) (все веса становятся неотрицательными)
5. Для каждой вершины v запускаем Дейкстру на графе с весами w' для поиска кратчайших путей
6. Восстанавливаем реальные расстояния: d(u,v) = d'(u,v) - h(u) + h(v)

## Complexity
- Time complexity: **O(V² log V + V × E)** для разреженных графов
- Лучше Флойда-Уоршелла для разреженных графов (O(V×E) vs O(V³))
- Space complexity: **O(V²)**

## Code

```java
import java.util.*;

public class JohnsonAlgorithm {
    
    static class Edge {
        int from, to;
        double weight;
        
        Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
    
    static class Graph {
        private int vertices;
        private List<Edge> edges;
        private List<List<Edge>> adj;
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.edges = new ArrayList<>();
            this.adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int from, int to, double weight) {
            Edge edge = new Edge(from, to, weight);
            edges.add(edge);
            adj.get(from).add(edge);
        }
        
        // Алгоритм Джонсона
        public double[][] findAllPairsShortestPaths() {
            // Шаг 1: добавляем фиктивную вершину
            int newVertices = vertices + 1;
            int dummy = vertices;
            
            List<Edge> tempEdges = new ArrayList<>(edges);
            for (int i = 0; i < vertices; i++) {
                tempEdges.add(new Edge(dummy, i, 0));
            }
            
            // Шаг 2: Беллман-Форд от фиктивной вершины
            double[] h = bellmanFord(tempEdges, vertices + 1, dummy);
            if (h == null) {
                System.out.println("Граф содержит отрицательный цикл!");
                return null;
            }
            
            // Шаг 3: Переназначение весов
            double[][] newWeights = new double[vertices][vertices];
            for (int i = 0; i < vertices; i++) {
                Arrays.fill(newWeights[i], Double.POSITIVE_INFINITY);
                newWeights[i][i] = 0;
            }
            
            for (Edge edge : edges) {
                int u = edge.from;
                int v = edge.to;
                newWeights[u][v] = edge.weight + h[u] - h[v];
            }
            
            // Шаг 4: Запускаем Дейкстру от каждой вершины
            double[][] distances = new double[vertices][vertices];
            for (int i = 0; i < vertices; i++) {
                distances[i] = dijkstra(i, newWeights);
            }
            
            // Шаг 5: Восстанавливаем реальные расстояния
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (distances[i][j] < Double.POSITIVE_INFINITY) {
                        distances[i][j] = distances[i][j] - h[i] + h[j];
                    }
                }
            }
            
            return distances;
        }
        
        // Беллман-Форд для нахождения потенциалов
        private double[] bellmanFord(List<Edge> edges, int vertices, int source) {
            double[] dist = new double[vertices];
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            dist[source] = 0;
            
            // Релаксация V-1 раз
            for (int i = 0; i < vertices - 1; i++) {
                boolean updated = false;
                for (Edge edge : edges) {
                    if (dist[edge.from] < Double.POSITIVE_INFINITY &&
                        dist[edge.from] + edge.weight < dist[edge.to]) {
                        dist[edge.to] = dist[edge.from] + edge.weight;
                        updated = true;
                    }
                }
                if (!updated) break;
            }
            
            // Проверка отрицательных циклов
            for (Edge edge : edges) {
                if (dist[edge.from] < Double.POSITIVE_INFINITY &&
                    dist[edge.from] + edge.weight < dist[edge.to]) {
                    return null; // Отрицательный цикл
                }
            }
            
            return dist;
        }
        
        // Дейкстра с переназначенными весами (все неотрицательные)
        private double[] dijkstra(int source, double[][] weights) {
            int n = vertices;
            double[] dist = new double[n];
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            dist[source] = 0;
            
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
            pq.offer(new int[]{source, 0});
            
            boolean[] visited = new boolean[n];
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                
                if (visited[u]) continue;
                visited[u] = true;
                
                for (int v = 0; v < n; v++) {
                    if (weights[u][v] < Double.POSITIVE_INFINITY) {
                        double newDist = dist[u] + weights[u][v];
                        if (newDist < dist[v]) {
                            dist[v] = newDist;
                            pq.offer(new int[]{v, (int) newDist});
                        }
                    }
                }
            }
            
            return dist;
        }
        
        // Версия для целых весов (оптимизированная)
        public int[][] findAllPairsShortestPathsInt() {
            int n = vertices;
            int dummy = n;
            int INF = Integer.MAX_VALUE / 2;
            
            // Беллман-Форд
            int[] h = new int[n + 1];
            Arrays.fill(h, INF);
            h[dummy] = 0;
            
            // V итераций
            for (int i = 0; i <= n; i++) {
                boolean updated = false;
                // Ребра от фиктивной вершины
                for (int v = 0; v < n; v++) {
                    if (h[dummy] + 0 < h[v]) {
                        h[v] = h[dummy] + 0;
                        updated = true;
                    }
                }
                // Остальные ребра
                for (Edge edge : edges) {
                    if (h[edge.from] < INF && h[edge.from] + edge.weight < h[edge.to]) {
                        h[edge.to] = (int)(h[edge.from] + edge.weight);
                        updated = true;
                    }
                }
                if (!updated) break;
            }
            
            // Проверка отрицательных циклов
            for (Edge edge : edges) {
                if (h[edge.from] < INF && h[edge.from] + edge.weight < h[edge.to]) {
                    return null;
                }
            }
            
            // Переназначение весов
            int[][] newWeights = new int[n][n];
            for (int i = 0; i < n; i++) {
                Arrays.fill(newWeights[i], INF);
                newWeights[i][i] = 0;
            }
            
            for (Edge edge : edges) {
                int u = edge.from;
                int v = edge.to;
                newWeights[u][v] = (int)(edge.weight + h[u] - h[v]);
            }
            
            // Дейкстра от каждой вершины
            int[][] distances = new int[n][n];
            for (int i = 0; i < n; i++) {
                distances[i] = dijkstraInt(i, newWeights);
            }
            
            // Восстановление реальных расстояний
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distances[i][j] < INF) {
                        distances[i][j] = distances[i][j] - h[i] + h[j];
                    }
                }
            }
            
            return distances;
        }
        
        private int[] dijkstraInt(int source, int[][] weights) {
            int n = vertices;
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE / 2);
            dist[source] = 0;
            
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            pq.offer(new int[]{source, 0});
            boolean[] visited = new boolean[n];
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                if (visited[u]) continue;
                visited[u] = true;
                
                for (int v = 0; v < n; v++) {
                    if (weights[u][v] < Integer.MAX_VALUE / 2) {
                        int newDist = dist[u] + weights[u][v];
                        if (newDist < dist[v]) {
                            dist[v] = newDist;
                            pq.offer(new int[]{v, newDist});
                        }
                    }
                }
            }
            
            return dist;
        }
    }
}