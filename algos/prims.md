# Алгоритм Прима (Prim's Algorithm)

## Intuition
Алгоритм Прима находит минимальное остовное дерево (Minimum Spanning Tree) взвешенного неориентированного графа. Он работает по принципу "жадного" роста дерева: начинаем с произвольной вершины и на каждом шаге добавляем самое дешевое ребро, которое соединяет уже построенное дерево с новой вершиной. Представьте, что вы строите дорожную сеть, соединяющую города, и хотите минимизировать общую длину дорог. Вы начинаете с одного города и каждый раз выбираете самый короткий маршрут к еще не подключенному городу.

## Approach
1. Выбираем произвольную стартовую вершину
2. Создаем массивы:
    - `key[]` — минимальный вес ребра для подключения вершины к дереву
    - `inMST[]` — флаг, добавлена ли вершина в дерево
    - `parent[]` — для восстановления дерева
3. Инициализируем все ключи бесконечностью, ключ стартовой вершины = 0
4. Повторяем для всех вершин:
    - Выбираем вершину с минимальным ключом, еще не добавленную в MST
    - Добавляем её в MST
    - Для всех смежных вершин обновляем ключи, если ребро меньше текущего
5. Для эффективного выбора минимума используется очередь с приоритетом

## Complexity
- Time complexity (с матрицей смежности): **O(V²)**
- Time complexity (с бинарной кучей): **O((V + E) log V)**
- Time complexity (с фибоначчиевой кучей): **O(E + V log V)**
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class PrimsAlgorithm {
    
    // Ребра графа
    static class Edge {
        int dest;
        int weight;
        
        Edge(int dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }
    
    // Представление графа через список смежности
    static class Graph {
        int vertices;
        List<List<Edge>> adjList;
        
        Graph(int vertices) {
            this.vertices = vertices;
            adjList = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adjList.add(new ArrayList<>());
            }
        }
        
        void addEdge(int src, int dest, int weight) {
            adjList.get(src).add(new Edge(dest, weight));
            adjList.get(dest).add(new Edge(src, weight)); // неориентированный граф
        }
        
        // Алгоритм Прима с очередью с приоритетом
        public void primMST() {
            boolean[] inMST = new boolean[vertices];
            int[] parent = new int[vertices];
            int[] key = new int[vertices];
            
            // Минимальная куча (вершина, вес)
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            
            Arrays.fill(key, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            
            // Начинаем с вершины 0
            key[0] = 0;
            pq.offer(new int[]{0, 0});
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                
                if (inMST[u]) continue;
                inMST[u] = true;
                
                // Обновляем ключи соседей
                for (Edge edge : adjList.get(u)) {
                    int v = edge.dest;
                    int weight = edge.weight;
                    
                    if (!inMST[v] && weight < key[v]) {
                        key[v] = weight;
                        parent[v] = u;
                        pq.offer(new int[]{v, key[v]});
                    }
                }
            }
            
            // Вывод результата
            System.out.println("Минимальное остовное дерево (Алгоритм Прима):");
            int totalWeight = 0;
            for (int i = 1; i < vertices; i++) {
                System.out.println(parent[i] + " - " + i + " : " + key[i]);
                totalWeight += key[i];
            }
            System.out.println("Общий вес: " + totalWeight);
        }
        
        // Версия с массивом для плотных графов (O(V²))
        public void primMSTDense() {
            boolean[] inMST = new boolean[vertices];
            int[] parent = new int[vertices];
            int[] key = new int[vertices];
            
            Arrays.fill(key, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            
            key[0] = 0;
            
            for (int count = 0; count < vertices - 1; count++) {
                // Находим вершину с минимальным ключом
                int u = -1;
                int minKey = Integer.MAX_VALUE;
                for (int v = 0; v < vertices; v++) {
                    if (!inMST[v] && key[v] < minKey) {
                        minKey = key[v];
                        u = v;
                    }
                }
                
                if (u == -1) break;
                inMST[u] = true;
                
                // Обновляем ключи смежных вершин
                for (Edge edge : adjList.get(u)) {
                    int v = edge.dest;
                    int weight = edge.weight;
                    
                    if (!inMST[v] && weight < key[v]) {
                        key[v] = weight;
                        parent[v] = u;
                    }
                }
            }
            
            // Вывод результата
            int totalWeight = 0;
            for (int i = 1; i < vertices; i++) {
                totalWeight += key[i];
            }
            System.out.println("Общий вес MST: " + totalWeight);
        }
    }
}