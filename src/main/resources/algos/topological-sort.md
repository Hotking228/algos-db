# Топологическая сортировка (Topological Sort)

## Intuition
Топологическая сортировка — это упорядочивание вершин направленного ациклического графа (DAG) таким образом, что для каждого ребра (u, v) вершина u предшествует вершине v. Представьте список задач, где некоторые задачи должны быть выполнены до других (например, сначала надеть носки, затем обувь). Топологическая сортировка дает допустимый порядок выполнения. Граф обязательно должен быть ациклическим — иначе порядок невозможен (циклическая зависимость).

## Approach
**Метод 1: Алгоритм Кана (Kahn's Algorithm) — на основе удаления вершин**
1. Вычисляем входящую степень (indegree) для каждой вершины
2. Добавляем в очередь все вершины с indegree = 0
3. Пока очередь не пуста:
    - Извлекаем вершину u, добавляем в результат
    - Для каждого соседа v: уменьшаем indegree[v] на 1
    - Если indegree[v] стал 0, добавляем v в очередь
4. Если результат содержит не все вершины — в графе есть цикл

**Метод 2: DFS с цветовой маркировкой**
1. Используем три состояния: 0=не посещена, 1=в обработке, 2=обработана
2. Для каждой непосещенной вершины запускаем DFS
3. При выходе из вершины добавляем её в стек
4. В конце извлекаем вершины из стека — это топологический порядок
5. Если встречаем вершину в состоянии 1 (в обработке) — обнаружен цикл

## Complexity
- Time complexity: **O(V + E)**
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class TopologicalSort {
    
    // Алгоритм Кана (Kahn's Algorithm) - на основе очереди
    public static List<Integer> kahnsAlgorithm(List<List<Integer>> graph, int vertices) {
        int[] indegree = new int[vertices];
        
        // Вычисляем входящие степени
        for (int u = 0; u < vertices; u++) {
            for (int v : graph.get(u)) {
                indegree[v]++;
            }
        }
        
        // Очередь для вершин с indegree = 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);
            
            for (int v : graph.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        // Проверка на цикл
        if (result.size() != vertices) {
            return null; // Граф содержит цикл
        }
        
        return result;
    }
    
    // DFS-based топологическая сортировка
    public static List<Integer> dfsTopologicalSort(List<List<Integer>> graph, int vertices) {
        int[] state = new int[vertices]; // 0-unvisited, 1-visiting, 2-visited
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < vertices; i++) {
            if (state[i] == 0) {
                if (!dfs(i, graph, state, stack)) {
                    return null; // Обнаружен цикл
                }
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }
    
    private static boolean dfs(int u, List<List<Integer>> graph, int[] state, Stack<Integer> stack) {
        state[u] = 1; // В обработке
        
        for (int v : graph.get(u)) {
            if (state[v] == 1) {
                return false; // Цикл
            }
            if (state[v] == 0) {
                if (!dfs(v, graph, state, stack)) {
                    return false;
                }
            }
        }
        
        state[u] = 2; // Обработана
        stack.push(u);
        return true;
    }
    
    // Топологическая сортировка для задач с зависимостями (класс задач)
    static class TaskScheduler {
        private int tasks;
        private List<List<Integer>> dependencies;
        
        public TaskScheduler(int tasks) {
            this.tasks = tasks;
            this.dependencies = new ArrayList<>(tasks);
            for (int i = 0; i < tasks; i++) {
                dependencies.add(new ArrayList<>());
            }
        }
        
        public void addDependency(int task, int dependsOn) {
            dependencies.get(dependsOn).add(task); // dependsOn -> task
        }
        
        public List<Integer> getExecutionOrder() {
            return kahnsAlgorithm(dependencies, tasks);
        }
        
        // Нахождение всех возможных топологических порядков (для маленьких графов)
        public List<List<Integer>> getAllOrders() {
            List<List<Integer>> allOrders = new ArrayList<>();
            int[] indegree = new int[tasks];
            
            for (int u = 0; u < tasks; u++) {
                for (int v : dependencies.get(u)) {
                    indegree[v]++;
                }
            }
            
            List<Integer> current = new ArrayList<>();
            generateOrders(0, indegree, current, allOrders);
            return allOrders;
        }
        
        private void generateOrders(int count, int[] indegree, 
                                    List<Integer> current, List<List<Integer>> allOrders) {
            if (count == tasks) {
                allOrders.add(new ArrayList<>(current));
                return;
            }
            
            for (int i = 0; i < tasks; i++) {
                if (indegree[i] == 0 && !current.contains(i)) {
                    current.add(i);
                    
                    // Уменьшаем indegree соседей
                    for (int v : dependencies.get(i)) {
                        indegree[v]--;
                    }
                    
                    generateOrders(count + 1, indegree, current, allOrders);
                    
                    // Откат изменений
                    current.remove(current.size() - 1);
                    for (int v : dependencies.get(i)) {
                        indegree[v]++;
                    }
                }
            }
        }
    }
    
    // Построение графа из ребер
    public static List<List<Integer>> buildGraph(int vertices, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
        }
        return graph;
    }
    
    // Проверка, является ли порядок валидной топологической сортировкой
    public static boolean isValidTopologicalOrder(List<List<Integer>> graph, List<Integer> order) {
        int[] position = new int[graph.size()];
        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }
        
        for (int u = 0; u < graph.size(); u++) {
            for (int v : graph.get(u)) {
                if (position[u] > position[v]) {
                    return false; // Ребро (u,v) нарушает порядок
                }
            }
        }
        return true;
    }
}