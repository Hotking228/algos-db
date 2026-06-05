# Алгоритм Дейкстры для разреженных графов (Dijkstra's Algorithm for Sparse Graphs)

## Intuition
Алгоритм Дейкстры находит кратчайшие пути от одной вершины до всех остальных во взвешенном графе с неотрицательными весами. Для разреженных графов (где количество ребер E ≈ V) стандартная реализация с массивом O(V²) неэффективна. Вместо этого используется очередь с приоритетом (бинарная куча), что дает время O((V + E) log V). Представьте, что вы ищете самый быстрый маршрут на карте с большим количеством городов, но малым количеством дорог между ними. Приоритетная очередь позволяет всегда выбирать город с наименьшим текущим расстоянием.

## Approach
1. Инициализируем расстояния: dist[start] = 0, все остальные = ∞
2. Создаем очередь с приоритетом (min-heap) пар (расстояние, вершина)
3. Добавляем стартовую вершину в очередь
4. Пока очередь не пуста:
    - Извлекаем вершину с наименьшим расстоянием
    - Если текущее расстояние больше сохраненного — пропускаем
    - Для каждого соседа: если новое расстояние меньше сохраненного, обновляем и добавляем в очередь

## Complexity
- Time complexity: **O((V + E) log V)** — с бинарной кучей
- Time complexity: **O(E + V log V)** — с фибоначчиевой кучей
- Space complexity: **O(V + E)**

## Code

```java
import java.util.*;

public class DijkstraSparse {
    
    // Класс для представления ребра
    static class Edge {
        int to;
        int weight;
        
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    // Представление графа через список смежности
    static class Graph {
        private int vertices;
        private List<List<Edge>> adj;
        
        public Graph(int vertices) {
            this.vertices = vertices;
            adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int from, int to, int weight) {
            adj.get(from).add(new Edge(to, weight));
        }
        
        public void addUndirectedEdge(int from, int to, int weight) {
            adj.get(from).add(new Edge(to, weight));
            adj.get(to).add(new Edge(from, weight));
        }
        
        // Алгоритм Дейкстры с приоритетной очередью
        public int[] shortestPaths(int start) {
            int[] dist = new int[vertices];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[start] = 0;
            
            // Минимальная куча: (расстояние, вершина)
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.offer(new int[]{0, start});
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int currentDist = current[0];
                int u = current[1];
                
                // Если расстояние устарело, пропускаем
                if (currentDist > dist[u]) continue;
                
                for (Edge edge : adj.get(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pq.offer(new int[]{newDist, v});
                    }
                }
            }
            
            return dist;
        }
        
        // Восстановление кратчайшего пути
        public List<Integer> shortestPath(int start, int end) {
            int[] dist = new int[vertices];
            int[] prev = new int[vertices];
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(prev, -1);
            dist[start] = 0;
            
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.offer(new int[]{0, start});
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int currentDist = current[0];
                int u = current[1];
                
                if (currentDist > dist[u]) continue;
                if (u == end) break;
                
                for (Edge edge : adj.get(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                        pq.offer(new int[]{newDist, v});
                    }
                }
            }
            
            // Восстановление пути
            List<Integer> path = new ArrayList<>();
            if (dist[end] == Integer.MAX_VALUE) return path;
            
            for (int v = end; v != -1; v = prev[v]) {
                path.add(v);
            }
            Collections.reverse(path);
            return path;
        }
        
        // Получение расстояния до всех вершин в виде массива
        public int[] getDistances(int start) {
            return shortestPaths(start);
        }
    }
    
    // Альтернативная реализация с Pair классом
    public static class DijkstraWithPair {
        static class Pair implements Comparable<Pair> {
            int vertex;
            int distance;
            
            Pair(int vertex, int distance) {
                this.vertex = vertex;
                this.distance = distance;
            }
            
            @Override
            public int compareTo(Pair other) {
                return Integer.compare(this.distance, other.distance);
            }
        }
        
        public static int[] dijkstra(List<List<Edge>> graph, int start) {
            int n = graph.size();
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[start] = 0;
            
            PriorityQueue<Pair> pq = new PriorityQueue<>();
            pq.offer(new Pair(start, 0));
            
            while (!pq.isEmpty()) {
                Pair current = pq.poll();
                int u = current.vertex;
                int d = current.distance;
                
                if (d > dist[u]) continue;
                
                for (Edge edge : graph.get(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pq.offer(new Pair(v, newDist));
                    }
                }
            }
            
            return dist;
        }
    }
    
    // Для больших графов с long расстояниями
    public static class DijkstraLong {
        static class Edge {
            int to;
            long weight;
            
            Edge(int to, long weight) {
                this.to = to;
                this.weight = weight;
            }
        }
        
        static class Pair implements Comparable<Pair> {
            int vertex;
            long distance;
            
            Pair(int vertex, long distance) {
                this.vertex = vertex;
                this.distance = distance;
            }
            
            @Override
            public int compareTo(Pair other) {
                return Long.compare(this.distance, other.distance);
            }
        }
        
        public static long[] dijkstra(List<List<Edge>> graph, int start) {
            int n = graph.size();
            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[start] = 0;
            
            PriorityQueue<Pair> pq = new PriorityQueue<>();
            pq.offer(new Pair(start, 0));
            
            while (!pq.isEmpty()) {
                Pair current = pq.poll();
                int u = current.vertex;
                long d = current.distance;
                
                if (d > dist[u]) continue;
                
                for (Edge edge : graph.get(u)) {
                    int v = edge.to;
                    long newDist = dist[u] + edge.weight;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pq.offer(new Pair(v, newDist));
                    }
                }
            }
            
            return dist;
        }
    }
}