# Алгоритм Эдмондса-Карпа (Edmonds-Karp Algorithm)

## Intuition
Алгоритм Эдмондса-Карпа — это реализация алгоритма Форда-Фалкерсона, использующая BFS для поиска увеличивающих путей. Выбор BFS (вместо DFS) гарантирует, что каждый раз находится кратчайший увеличивающий путь (в терминах количества ребер), что ограничивает количество итераций и делает алгоритм полиномиальным. Представьте, что вместо случайного поиска маршрутов для увеличения потока вы всегда выбираете самый короткий путь от истока к стоку. Это похоже на поиск очереди в узком месте: вы всегда обрабатываете ближайшие возможности.

## Approach
1. Инициализируем поток: 0 для всех ребер
2. Пока существует увеличивающий путь от source до sink в остаточной сети:
    - Находим кратчайший увеличивающий путь с помощью BFS
    - BFS строит уровневую сеть, гарантируя минимальное количество ребер
    - Находим минимальную пропускную способность на этом пути
    - Увеличиваем поток и обновляем остаточные пропускные способности
3. BFS гарантирует, что алгоритм выполнит не более O(V * E) итераций

## Complexity
- Time complexity: **O(V * E²)**
- Space complexity: **O(V²)**

## Code

```java
import java.util.*;

public class EdmondsKarp {
    
    static class Graph {
        private int vertices;
        private int[][] capacity; // пропускные способности
        private int[][] flow;     // текущий поток
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.capacity = new int[vertices][vertices];
            this.flow = new int[vertices][vertices];
        }
        
        public void addEdge(int from, int to, int cap) {
            capacity[from][to] = cap;
        }
        
        // Алгоритм Эдмондса-Карпа (с BFS)
        public int maxFlow(int source, int sink) {
            int maxFlow = 0;
            
            while (true) {
                // BFS для поиска кратчайшего увеличивающего пути
                int[] parent = new int[vertices];
                Arrays.fill(parent, -1);
                Queue<Integer> queue = new LinkedList<>();
                
                queue.offer(source);
                parent[source] = source;
                
                // BFS с построением пути
                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    
                    for (int v = 0; v < vertices; v++) {
                        int available = capacity[u][v] - flow[u][v];
                        if (parent[v] == -1 && v != source && available > 0) {
                            parent[v] = u;
                            queue.offer(v);
                            if (v == sink) break;
                        }
                    }
                }
                
                // Если путь не найден, завершаем
                if (parent[sink] == -1) break;
                
                // Находим узкое место (минимальную пропускную способность)
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
                    flow[v][u] -= pathFlow;
                }
                
                maxFlow += pathFlow;
            }
            
            return maxFlow;
        }
        
        // Альтернативная реализация с явным отслеживанием остаточной сети
        public int maxFlowResidual(int source, int sink) {
            int[][] residual = new int[vertices][vertices];
            for (int i = 0; i < vertices; i++) {
                residual[i] = capacity[i].clone();
            }
            
            int maxFlow = 0;
            int[] parent = new int[vertices];
            
            while (bfsResidual(residual, source, sink, parent)) {
                int pathFlow = Integer.MAX_VALUE;
                
                // Находим узкое место
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    pathFlow = Math.min(pathFlow, residual[u][v]);
                }
                
                // Обновляем остаточную сеть
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    residual[u][v] -= pathFlow;
                    residual[v][u] += pathFlow;
                }
                
                maxFlow += pathFlow;
            }
            
            return maxFlow;
        }
        
        private boolean bfsResidual(int[][] residual, int source, int sink, int[] parent) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            queue.offer(source);
            visited[source] = true;
            parent[source] = -1;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (int v = 0; v < vertices; v++) {
                    if (!visited[v] && residual[u][v] > 0) {
                        visited[v] = true;
                        parent[v] = u;
                        queue.offer(v);
                        if (v == sink) return true;
                    }
                }
            }
            
            return false;
        }
        
        // Получение максимального потока и минимального разреза
        public Result maxFlowWithMinCut(int source, int sink) {
            int maxFlow = maxFlow(source, sink);
            
            // Находим вершины, достижимые из источника в остаточной сети
            boolean[] reachable = getReachableVertices(source);
            
            List<int[]> minCut = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (reachable[i] && !reachable[j] && capacity[i][j] > 0) {
                        minCut.add(new int[]{i, j, capacity[i][j]});
                    }
                }
            }
            
            return new Result(maxFlow, minCut);
        }
        
        private boolean[] getReachableVertices(int source) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            queue.offer(source);
            visited[source] = true;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v = 0; v < vertices; v++) {
                    int available = capacity[u][v] - flow[u][v];
                    if (!visited[v] && available > 0) {
                        visited[v] = true;
                        queue.offer(v);
                    }
                }
            }
            
            return visited;
        }
        
        static class Result {
            int maxFlow;
            List<int[]> minCut;
            
            Result(int maxFlow, List<int[]> minCut) {
                this.maxFlow = maxFlow;
                this.minCut = minCut;
            }
        }
        
        // Печать потока
        public void printFlow() {
            System.out.println("Edmonds-Karp результат:");
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (flow[i][j] > 0) {
                        System.out.println("  " + i + " → " + j + ": " + flow[i][j] + " / " + capacity[i][j]);
                    }
                }
            }
        }
    }
    
    // Пример использования
    public static void main(String[] args) {
        Graph graph = new Graph(6);
        
        // Построение графа
        graph.addEdge(0, 1, 16);
        graph.addEdge(0, 2, 13);
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 12);
        graph.addEdge(2, 1, 4);
        graph.addEdge(2, 4, 14);
        graph.addEdge(3, 2, 9);
        graph.addEdge(3, 5, 20);
        graph.addEdge(4, 3, 7);
        graph.addEdge(4, 5, 4);
        
        int maxFlow = graph.maxFlow(0, 5);
        System.out.println("Максимальный поток: " + maxFlow);
        
        Graph.Result result = graph.maxFlowWithMinCut(0, 5);
        System.out.println("Минимальный разрез:");
        for (int[] edge : result.minCut) {
            System.out.println("  " + edge[0] + " → " + edge[1] + " (capacity: " + edge[2] + ")");
        }
    }
}