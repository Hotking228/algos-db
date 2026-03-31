# Алгоритм Дейкстры (Dijkstra's Algorithm)

## Intuition
Алгоритм Дейкстры находит кратчайшие пути от одной стартовой вершины до всех остальных вершин во взвешенном графе с неотрицательными весами рёбер. Основная идея: жадно выбираем вершину с наименьшим известным расстоянием, "расслабляем" (relax) все её рёбра и обновляем расстояния до соседей. Можно представить это как распространение волны от стартовой точки, где волна достигает каждой вершины за минимальное время.

## Approach
1. Инициализируем расстояния: для стартовой вершины = 0, для остальных = ∞
2. Используем приоритетную очередь (min-heap) для выбора вершины с минимальным расстоянием
3. Пока очередь не пуста:
    - Извлекаем вершину с наименьшим расстоянием
    - Если текущее расстояние больше сохранённого, пропускаем
    - Для каждого соседа вычисляем новое расстояние = расстояние_до_текущей + вес_ребра
    - Если новое расстояние меньше сохранённого, обновляем и добавляем в очередь
4. После завершения имеем кратчайшие расстояния от старта до всех вершин

## Complexity
- Time complexity: $$O((V + E) \log V)$$ с использованием двоичной кучи
- Time complexity: $$O(V^2)$$ с использованием простого массива (для плотных графов)

- Space complexity: $$O(V)$$ для хранения расстояний и очереди

## Code

### Базовая реализация с PriorityQueue
```java
import java.util.*;

public class Dijkstra {
    
    static class Edge {
        int target;
        int weight;
        
        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }
    
    static class Node implements Comparable<Node> {
        int vertex;
        int distance;
        
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    public int[] dijkstra(List<List<Edge>> graph, int start, int vertices) {
        // Расстояния от старта до всех вершин
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;
        
        // Массив посещённых вершин
        boolean[] visited = new boolean[vertices];
        
        // Приоритетная очередь (min-heap)
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int vertex = current.vertex;
            int currentDist = current.distance;
            
            // Пропускаем, если уже обработали
            if (visited[vertex]) continue;
            visited[vertex] = true;
            
            // Если расстояние в очереди устарело, пропускаем
            if (currentDist > distances[vertex]) continue;
            
            // Рассматриваем всех соседей
            for (Edge edge : graph.get(vertex)) {
                int neighbor = edge.target;
                int newDist = currentDist + edge.weight;
                
                if (newDist < distances[neighbor]) {
                    distances[neighbor] = newDist;
                    pq.offer(new Node(neighbor, newDist));
                }
            }
        }
        
        return distances;
    }
}