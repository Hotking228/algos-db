# Алгоритм Крускала (Kruskal's Algorithm)

## Intuition
Алгоритм Крускала находит минимальное остовное дерево, сортируя все ребра по весу и добавляя их в дерево, если они не создают цикл. Представьте, что у вас есть список всех возможных дорог между городами с указанием их длины. Вы начинаете с самого короткого маршрута и добавляете его, затем следующий короткий, пропуская те, которые соединяют уже связанные города. Это похоже на построение "скелета" графа, где вы всегда выбираете самое дешевое безопасное ребро.

## Approach
1. Сортируем все ребра по весу (от меньшего к большему)
2. Инициализируем DSU (систему непересекающихся множеств) для всех вершин
3. Проходим по отсортированным ребрам:
    - Если ребро соединяет вершины из разных компонент, добавляем его в MST
    - Объединяем компоненты через DSU
    - Если ребро соединяет вершины из одной компоненты, пропускаем (создаст цикл)
4. Продолжаем, пока в MST не будет V-1 ребер

## Complexity
- Time complexity (сортировка): **O(E log E)**
- Time complexity (DSU операции): **O(E α(V))**
- Total time complexity: **O(E log E)** или **O(E log V)**
- Space complexity: **O(V + E)**

## Code

```java
import java.util.*;

public class KruskalAlgorithm {
    
    // Класс для представления ребра
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;
        
        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }
    
    // DSU для отслеживания компонент
    static class DSU {
        int[] parent;
        int[] rank;
        
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return false;
            
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }
    
    // Граф для алгоритма Крускала
    static class Graph {
        int vertices;
        List<Edge> edges;
        
        Graph(int vertices) {
            this.vertices = vertices;
            edges = new ArrayList<>();
        }
        
        void addEdge(int src, int dest, int weight) {
            edges.add(new Edge(src, dest, weight));
        }
        
        // Алгоритм Крускала
        public void kruskalMST() {
            // Сортируем ребра по весу
            Collections.sort(edges);
            
            DSU dsu = new DSU(vertices);
            List<Edge> mst = new ArrayList<>();
            int totalWeight = 0;
            
            for (Edge edge : edges) {
                // Если ребро соединяет разные компоненты
                if (dsu.union(edge.src, edge.dest)) {
                    mst.add(edge);
                    totalWeight += edge.weight;
                    
                    // Останавливаемся, когда набрали V-1 ребер
                    if (mst.size() == vertices - 1) break;
                }
            }
            
            // Вывод результата
            System.out.println("Минимальное остовное дерево (Алгоритм Крускала):");
            for (Edge edge : mst) {
                System.out.println(edge.src + " - " + edge.dest + " : " + edge.weight);
            }
            System.out.println("Общий вес: " + totalWeight);
        }
        
        // Проверка, является ли граф связным
        public boolean isConnected() {
            DSU dsu = new DSU(vertices);
            for (Edge edge : edges) {
                dsu.union(edge.src, edge.dest);
            }
            
            int root = dsu.find(0);
            for (int i = 1; i < vertices; i++) {
                if (dsu.find(i) != root) return false;
            }
            return true;
        }
        
        // Получение веса MST (если граф несвязный - вернем -1)
        public int getMSTWeight() {
            Collections.sort(edges);
            DSU dsu = new DSU(vertices);
            int totalWeight = 0;
            int edgesUsed = 0;
            
            for (Edge edge : edges) {
                if (dsu.union(edge.src, edge.dest)) {
                    totalWeight += edge.weight;
                    edgesUsed++;
                    if (edgesUsed == vertices - 1) break;
                }
            }
            
            return edgesUsed == vertices - 1 ? totalWeight : -1;
        }
    }
    
    // Альтернативная реализация с использованием приоритетной очереди
    static class KruskalPQ {
        int vertices;
        PriorityQueue<Edge> pq;
        DSU dsu;
        
        KruskalPQ(int vertices) {
            this.vertices = vertices;
            this.pq = new PriorityQueue<>();
            this.dsu = new DSU(vertices);
        }
        
        void addEdge(int src, int dest, int weight) {
            pq.offer(new Edge(src, dest, weight));
        }
        
        List<Edge> getMST() {
            List<Edge> mst = new ArrayList<>();
            
            while (!pq.isEmpty() && mst.size() < vertices - 1) {
                Edge edge = pq.poll();
                if (dsu.union(edge.src, edge.dest)) {
                    mst.add(edge);
                }
            }
            
            return mst;
        }
    }
}