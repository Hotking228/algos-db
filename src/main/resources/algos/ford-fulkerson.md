# Алгоритм Форда-Фалкерсона (Ford-Fulkerson Algorithm)

## Intuition
Алгоритм Форда-Фалкерсона находит максимальный поток в транспортной сети. Представьте сеть труб с разной пропускной способностью, по которым вода течет от истока (source) к стоку (sink). Алгоритм ищет пути (увеличивающие пути), по которым можно пропустить дополнительный поток, и увеличивает общий поток до тех пор, пока это возможно. Ключевая идея — использование остаточных сетей и обратных ребер, которые позволяют "отменять" уже отправленный поток, если найден лучший путь.

## Approach
1. Инициализируем поток: 0 для всех ребер
2. Пока существует увеличивающий путь от source до sink в остаточной сети:
    - Находим путь (например, с помощью BFS — это алгоритм Эдмондса-Карпа)
    - Находим минимальную пропускную способность на этом пути (узкое место)
    - Увеличиваем поток на найденное значение
    - Обновляем остаточные пропускные способности: уменьшаем для прямых ребер, увеличиваем для обратных
3. Когда пути не осталось — текущий поток максимален

## Complexity
- Time complexity: **O(E * max_flow)** — зависит от величины максимального потока
- С использованием BFS (Эдмондс-Карп): **O(V * E²)**
- Space complexity: **O(V²)**

## Code

```java
import java.util.*;

public class FordFulkerson {
    
    static class Graph {
        private int vertices;
        private int[][] capacity; // матрица пропускных способностей
        private int[][] flow;     // текущий поток
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.capacity = new int[vertices][vertices];
            this.flow = new int[vertices][vertices];
        }
        
        public void addEdge(int from, int to, int cap) {
            capacity[from][to] = cap;
        }
        
        // Алгоритм Форда-Фалкерсона с DFS
        public int maxFlow(int source, int sink) {
            int maxFlow = 0;
            int[] parent = new int[vertices];
            
            // Повторяем, пока есть увеличивающий путь
            while (bfs(source, sink, parent)) {
                // Находим минимальную пропускную способность на пути
                int pathFlow = Integer.MAX_VALUE;
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    int available = capacity[u][v] - flow[u][v];
                    pathFlow = Math.min(pathFlow, available);
                }
                
                // Обновляем поток
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    flow[u][v] += pathFlow;
                    flow[v][u] -= pathFlow; // обратное ребро
                }
                
                maxFlow += pathFlow;
            }
            
            return maxFlow;
        }
        
        // BFS для поиска увеличивающего пути
        private boolean bfs(int source, int sink, int[] parent) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            queue.offer(source);
            visited[source] = true;
            parent[source] = -1;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (int v = 0; v < vertices; v++) {
                    int available = capacity[u][v] - flow[u][v];
                    if (!visited[v] && available > 0) {
                        visited[v] = true;
                        parent[v] = u;
                        queue.offer(v);
                        
                        if (v == sink) return true;
                    }
                }
            }
            
            return false;
        }
        
        // Получение минимального разреза (вершины, достижимые из source в остаточной сети)
        public Set<Integer> getMinCut(int source) {
            boolean[] visited = new boolean[vertices];
            dfsForMinCut(source, visited);
            
            Set<Integer> minCutSet = new HashSet<>();
            for (int i = 0; i < vertices; i++) {
                if (visited[i]) {
                    minCutSet.add(i);
                }
            }
            return minCutSet;
        }
        
        private void dfsForMinCut(int u, boolean[] visited) {
            visited[u] = true;
            for (int v = 0; v < vertices; v++) {
                int available = capacity[u][v] - flow[u][v];
                if (!visited[v] && available > 0) {
                    dfsForMinCut(v, visited);
                }
            }
        }
        
        // Вывод потока по ребрам
        public void printFlow() {
            System.out.println("Текущий поток по ребрам:");
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (flow[i][j] > 0) {
                        System.out.println(i + " -> " + j + ": " + flow[i][j] + " / " + capacity[i][j]);
                    }
                }
            }
        }
    }
    
    // Версия с использованием списков смежности для разреженных графов
    static class GraphSparse {
        static class Edge {
            int to, capacity, flow;
            Edge reverse;
            
            Edge(int to, int capacity) {
                this.to = to;
                this.capacity = capacity;
                this.flow = 0;
            }
        }
        
        private int vertices;
        private List<Edge>[] adj;
        
        @SuppressWarnings("unchecked")
        public GraphSparse(int vertices) {
            this.vertices = vertices;
            adj = new ArrayList[vertices];
            for (int i = 0; i < vertices; i++) {
                adj[i] = new ArrayList<>();
            }
        }
        
        public void addEdge(int from, int to, int capacity) {
            Edge forward = new Edge(to, capacity);
            Edge backward = new Edge(from, 0);
            forward.reverse = backward;
            backward.reverse = forward;
            adj[from].add(forward);
            adj[to].add(backward);
        }
        
        public int maxFlow(int source, int sink) {
            int maxFlow = 0;
            int[] parent = new int[vertices];
            Edge[] parentEdge = new Edge[vertices];
            
            while (bfs(source, sink, parent, parentEdge)) {
                // Находим узкое место
                int pathFlow = Integer.MAX_VALUE;
                for (int v = sink; v != source; v = parent[v]) {
                    pathFlow = Math.min(pathFlow, parentEdge[v].capacity - parentEdge[v].flow);
                }
                
                // Обновляем поток
                for (int v = sink; v != source; v = parent[v]) {
                    Edge edge = parentEdge[v];
                    edge.flow += pathFlow;
                    edge.reverse.flow -= pathFlow;
                }
                
                maxFlow += pathFlow;
            }
            
            return maxFlow;
        }
        
        private boolean bfs(int source, int sink, int[] parent, Edge[] parentEdge) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            queue.offer(source);
            visited[source] = true;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (Edge edge : adj[u]) {
                    int available = edge.capacity - edge.flow;
                    if (!visited[edge.to] && available > 0) {
                        visited[edge.to] = true;
                        parent[edge.to] = u;
                        parentEdge[edge.to] = edge;
                        queue.offer(edge.to);
                        
                        if (edge.to == sink) return true;
                    }
                }
            }
            
            return false;
        }
    }
}