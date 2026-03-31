# Граф (BFS - Поиск в ширину)

## Intuition
Поиск в ширину (BFS) — это алгоритм обхода графа, который исследует все вершины на текущем "расстоянии" (уровне), прежде чем переходить к следующему. Представьте, что вы бросаете камень в воду — волны расходятся равномерно во все стороны. BFS работает так же: сначала посещаются все соседние вершины, затем все вершины на расстоянии 2, затем на расстоянии 3 и так далее.

## Approach
1. Выбираем стартовую вершину и помещаем её в очередь
2. Помечаем стартовую вершину как посещённую
3. Пока очередь не пуста:
    - Извлекаем вершину из начала очереди
    - Обрабатываем её (например, выводим на экран)
    - Все непосещённые соседние вершины добавляем в конец очереди
    - Помечаем их как посещённые
4. Повторяем, пока не обойдём все достижимые вершины

## Complexity
- Time complexity: $$O(V + E)$$, где V — количество вершин, E — количество рёбер

- Space complexity: $$O(V)$$ для очереди и массива посещённых вершин

## Code

### Реализация для графа через список смежности
```java
import java.util.*;

public class GraphBFS {
    
    // Класс для представления графа
    static class Graph {
        private int vertices;
        private List<List<Integer>> adjList;
        
        public Graph(int vertices) {
            this.vertices = vertices;
            adjList = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adjList.add(new LinkedList<>());
            }
        }
        
        // Добавление ребра (неориентированный граф)
        public void addEdge(int src, int dest) {
            adjList.get(src).add(dest);
            adjList.get(dest).add(src); // для ориентированного графа эту строку убрать
        }
        
        // BFS от заданной вершины
        public void bfs(int start) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            visited[start] = true;
            queue.offer(start);
            
            while (!queue.isEmpty()) {
                int vertex = queue.poll();
                System.out.print(vertex + " ");
                
                // Исследуем всех соседей
                for (int neighbor : adjList.get(vertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
        }
        
        // BFS с расстояниями
        public int[] bfsWithDistances(int start) {
            int[] distances = new int[vertices];
            Arrays.fill(distances, -1);
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            
            visited[start] = true;
            distances[start] = 0;
            queue.offer(start);
            
            while (!queue.isEmpty()) {
                int vertex = queue.poll();
                
                for (int neighbor : adjList.get(vertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        distances[neighbor] = distances[vertex] + 1;
                        queue.offer(neighbor);
                    }
                }
            }
            
            return distances;
        }
        
        // BFS для поиска пути между двумя вершинами
        public List<Integer> findPath(int start, int end) {
            boolean[] visited = new boolean[vertices];
            int[] parent = new int[vertices];
            Arrays.fill(parent, -1);
            Queue<Integer> queue = new LinkedList<>();
            
            visited[start] = true;
            queue.offer(start);
            
            while (!queue.isEmpty()) {
                int vertex = queue.poll();
                
                if (vertex == end) {
                    break;
                }
                
                for (int neighbor : adjList.get(vertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        parent[neighbor] = vertex;
                        queue.offer(neighbor);
                    }
                }
            }
            
            // Восстанавливаем путь
            List<Integer> path = new ArrayList<>();
            if (parent[end] == -1 && start != end) {
                return path; // путь не найден
            }
            
            for (int v = end; v != -1; v = parent[v]) {
                path.add(v);
            }
            Collections.reverse(path);
            return path;
        }
    }
}
```