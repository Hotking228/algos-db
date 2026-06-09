# Поиск компонент сильной связности (Алгоритм Косарайю - Kosaraju's Algorithm)

## Intuition
Алгоритм Косарайю находит компоненты сильной связности (Strongly Connected Components - SCC) в ориентированном графе за два прохода DFS. Компонента сильной связности — это максимальное множество вершин, где из любой вершины можно достичь любую другую. Идея в том, что если запустить DFS на исходном графе и на транспонированном (обратном) графе в определенном порядке, то деревья DFS во втором проходе будут соответствовать SCC.

## Approach
1. **Первый проход (сортировка)**:
    - Выполняем DFS на исходном графе
    - Записываем вершины в стек в порядке завершения их обработки
2. **Транспонирование графа**:
    - Создаем обратный граф, где все ребра развернуты
3. **Второй проход (поиск SCC)**:
    - Извлекаем вершины из стека (в порядке убывания времени завершения)
    - Для каждой непосещенной вершины запускаем DFS на транспонированном графе
    - Все вершины, достигнутые в одном DFS, образуют одну SCC

## Complexity
- Time complexity: **O(V + E)** — два прохода DFS
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class KosarajuSCC {
    
    static class Graph {
        private int vertices;
        private List<List<Integer>> adj;
        private List<List<Integer>> adjTranspose;
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            this.adjTranspose = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
                adjTranspose.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int from, int to) {
            adj.get(from).add(to);
            adjTranspose.get(to).add(from); // Обратное ребро для транспонированного графа
        }
        
        // Алгоритм Косарайю
        public List<List<Integer>> findSCCs() {
            boolean[] visited = new boolean[vertices];
            Stack<Integer> stack = new Stack<>();
            
            // Шаг 1: DFS и заполнение стека (порядок завершения)
            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    dfs1(i, visited, stack);
                }
            }
            
            // Шаг 2: Сброс visited
            Arrays.fill(visited, false);
            
            // Шаг 3: Обработка вершин в порядке из стека на транспонированном графе
            List<List<Integer>> sccs = new ArrayList<>();
            while (!stack.isEmpty()) {
                int v = stack.pop();
                if (!visited[v]) {
                    List<Integer> scc = new ArrayList<>();
                    dfs2(v, visited, scc);
                    sccs.add(scc);
                }
            }
            
            return sccs;
        }
        
        // DFS на исходном графе (заполнение стека)
        private void dfs1(int v, boolean[] visited, Stack<Integer> stack) {
            visited[v] = true;
            for (int neighbor : adj.get(v)) {
                if (!visited[neighbor]) {
                    dfs1(neighbor, visited, stack);
                }
            }
            stack.push(v); // Записываем в стек после обработки
        }
        
        // DFS на транспонированном графе (сбор SCC)
        private void dfs2(int v, boolean[] visited, List<Integer> scc) {
            visited[v] = true;
            scc.add(v);
            for (int neighbor : adjTranspose.get(v)) {
                if (!visited[neighbor]) {
                    dfs2(neighbor, visited, scc);
                }
            }
        }
        
        // Проверка, является ли граф сильно связным (одна SCC)
        public boolean isStronglyConnected() {
            return findSCCs().size() == 1;
        }
        
        // Получение графа SCC (каждая SCC сжимается в одну вершину)
        public Graph getCondensationGraph() {
            List<List<Integer>> sccs = findSCCs();
            int[] sccId = new int[vertices];
            for (int i = 0; i < sccs.size(); i++) {
                for (int v : sccs.get(i)) {
                    sccId[v] = i;
                }
            }
            
            Graph condensation = new Graph(sccs.size());
            for (int u = 0; u < vertices; u++) {
                for (int v : adj.get(u)) {
                    if (sccId[u] != sccId[v]) {
                        condensation.addEdge(sccId[u], sccId[v]);
                    }
                }
            }
            
            return condensation;
        }
    }
    
    // Версия с итеративным DFS для избежания переполнения стека
    static class KosarajuIterative {
        
        public static List<List<Integer>> findSCCs(List<List<Integer>> graph, int vertices) {
            // Строим транспонированный граф
            List<List<Integer>> transpose = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                transpose.add(new ArrayList<>());
            }
            for (int u = 0; u < vertices; u++) {
                for (int v : graph.get(u)) {
                    transpose.get(v).add(u);
                }
            }
            
            // Первый проход: итеративный DFS для порядка завершения
            boolean[] visited = new boolean[vertices];
            Stack<Integer> order = new Stack<>();
            
            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    iterativeDFS1(i, graph, visited, order);
                }
            }
            
            // Второй проход
            Arrays.fill(visited, false);
            List<List<Integer>> sccs = new ArrayList<>();
            
            while (!order.isEmpty()) {
                int v = order.pop();
                if (!visited[v]) {
                    List<Integer> scc = new ArrayList<>();
                    iterativeDFS2(v, transpose, visited, scc);
                    sccs.add(scc);
                }
            }
            
            return sccs;
        }
        
        private static void iterativeDFS1(int start, List<List<Integer>> graph, 
                                          boolean[] visited, Stack<Integer> order) {
            Stack<Integer> stack = new Stack<>();
            stack.push(start);
            
            while (!stack.isEmpty()) {
                int v = stack.peek();
                if (!visited[v]) {
                    visited[v] = true;
                    for (int neighbor : graph.get(v)) {
                        if (!visited[neighbor]) {
                            stack.push(neighbor);
                        }
                    }
                } else {
                    stack.pop();
                    order.push(v);
                }
            }
        }
        
        private static void iterativeDFS2(int start, List<List<Integer>> graph,
                                          boolean[] visited, List<Integer> scc) {
            Stack<Integer> stack = new Stack<>();
            stack.push(start);
            visited[start] = true;
            
            while (!stack.isEmpty()) {
                int v = stack.pop();
                scc.add(v);
                for (int neighbor : graph.get(v)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        stack.push(neighbor);
                    }
                }
            }
        }
    }
}