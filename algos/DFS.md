# Поиск в глубину (DFS - Depth-First Search)

## Intuition
Поиск в глубину — это алгоритм обхода графа, который идёт "вглубь" прежде чем "вширь". Представьте, что вы исследуете лабиринт и всегда идёте до конца по выбранному пути, а если путь заканчивается, возвращаетесь назад (backtracking) и пробуете другой путь. DFS можно реализовать как рекурсивно, так и с использованием стека.

## Approach
1. **Рекурсивный подход**:
    - Начинаем со стартовой вершины
    - Помечаем её как посещённую
    - Рекурсивно вызываем DFS для каждого непосещённого соседа

2. **Итеративный подход (со стеком)**:
    - Создаём стек и помещаем в него стартовую вершину
    - Пока стек не пуст:
        - Извлекаем вершину из стека
        - Если она не посещена, помечаем как посещённую
        - Добавляем всех непосещённых соседей в стек

## Complexity
- Time complexity: $$O(V + E)$$, где V — количество вершин, E — количество рёбер

- Space complexity: $$O(V)$$ для стека рекурсии или явного стека

## Code

### Реализация для графа через список смежности
```java
import java.util.*;

public class GraphDFS {
    
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
        
        public void addEdge(int src, int dest) {
            adjList.get(src).add(dest);
            adjList.get(dest).add(src); // для ориентированного графа эту строку убрать
        }
        
        // Рекурсивный DFS
        public void dfsRecursive(int start) {
            boolean[] visited = new boolean[vertices];
            dfsRecursiveHelper(start, visited);
        }
        
        private void dfsRecursiveHelper(int vertex, boolean[] visited) {
            visited[vertex] = true;
            System.out.print(vertex + " ");
            
            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    dfsRecursiveHelper(neighbor, visited);
                }
            }
        }
        
        // Итеративный DFS (со стеком)
        public void dfsIterative(int start) {
            boolean[] visited = new boolean[vertices];
            Stack<Integer> stack = new Stack<>();
            
            stack.push(start);
            
            while (!stack.isEmpty()) {
                int vertex = stack.pop();
                
                if (!visited[vertex]) {
                    visited[vertex] = true;
                    System.out.print(vertex + " ");
                    
                    // Добавляем соседей в стек (обратный порядок для сохранения порядка)
                    List<Integer> neighbors = adjList.get(vertex);
                    for (int i = neighbors.size() - 1; i >= 0; i--) {
                        int neighbor = neighbors.get(i);
                        if (!visited[neighbor]) {
                            stack.push(neighbor);
                        }
                    }
                }
            }
        }
        
        // DFS для поиска пути
        public List<Integer> findPath(int start, int end) {
            boolean[] visited = new boolean[vertices];
            List<Integer> path = new ArrayList<>();
            if (findPathHelper(start, end, visited, path)) {
                return path;
            }
            return new ArrayList<>();
        }
        
        private boolean findPathHelper(int current, int end, boolean[] visited, List<Integer> path) {
            visited[current] = true;
            path.add(current);
            
            if (current == end) {
                return true;
            }
            
            for (int neighbor : adjList.get(current)) {
                if (!visited[neighbor]) {
                    if (findPathHelper(neighbor, end, visited, path)) {
                        return true;
                    }
                }
            }
            
            path.remove(path.size() - 1); // backtracking
            return false;
        }
        
        // Поиск всех путей между двумя вершинами
        public List<List<Integer>> findAllPaths(int start, int end) {
            List<List<Integer>> allPaths = new ArrayList<>();
            boolean[] visited = new boolean[vertices];
            List<Integer> currentPath = new ArrayList<>();
            findAllPathsHelper(start, end, visited, currentPath, allPaths);
            return allPaths;
        }
        
        private void findAllPathsHelper(int current, int end, boolean[] visited, 
                                       List<Integer> currentPath, List<List<Integer>> allPaths) {
            visited[current] = true;
            currentPath.add(current);
            
            if (current == end) {
                allPaths.add(new ArrayList<>(currentPath));
            } else {
                for (int neighbor : adjList.get(current)) {
                    if (!visited[neighbor]) {
                        findAllPathsHelper(neighbor, end, visited, currentPath, allPaths);
                    }
                }
            }
            
            currentPath.remove(currentPath.size() - 1);
            visited[current] = false;
        }
    }
}
```
