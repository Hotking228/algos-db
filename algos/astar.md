# Алгоритм A* (A-Star)

## Intuition
A* — это алгоритм поиска кратчайшего пути, который combines лучшее из алгоритма Дейкстры (гарантия кратчайшего пути) и жадного поиска (эффективность). Он использует эвристическую функцию (оценку расстояния до цели), чтобы направлять поиск и находить путь быстрее, чем обычный BFS или Дейкстра. Представьте, что вы ищете путь в незнакомом городе: вы не просто исследуете все улицы равномерно (как Дейкстра), а двигаетесь в сторону цели (как жадный поиск), но с учетом уже пройденного расстояния, чтобы не попасть в ловушку.

## Approach
1. Каждая вершина имеет:
    - `g` — стоимость пути от старта до текущей вершины
    - `h` — эвристическая оценка стоимости от текущей вершины до цели
    - `f = g + h` — общая оценка стоимости
2. Используем приоритетную очередь для выбора вершины с минимальным `f`
3. На каждом шаге:
    - Извлекаем вершину с наименьшим `f`
    - Если это цель — путь найден
    - Для каждого соседа вычисляем новую `g` и обновляем, если она лучше
4. Эвристика должна быть допустимой (не переоценивать стоимость до цели) для гарантии оптимальности

## Complexity
- Time complexity: **O(b^d)**, где b — фактор ветвления, d — глубина
- С хорошей эвристикой работает значительно быстрее Дейкстры
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class AStar {
    
    // Класс для узла в поиске
    static class Node implements Comparable<Node> {
        int id;
        double g; // стоимость от старта
        double h; // эвристика до цели
        double f; // g + h
        Node parent;
        
        Node(int id) {
            this.id = id;
            this.g = Double.POSITIVE_INFINITY;
            this.h = 0;
            this.f = Double.POSITIVE_INFINITY;
            this.parent = null;
        }
        
        Node(int id, double g, double h, Node parent) {
            this.id = id;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
        
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f, other.f);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return id == node.id;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
    
    // Граф с координатами для эвклидовой эвристики
    static class GraphWithCoords {
        private int vertices;
        private List<List<Edge>> adj;
        private double[] x, y; // координаты вершин
        
        static class Edge {
            int to;
            double weight;
            
            Edge(int to, double weight) {
                this.to = to;
                this.weight = weight;
            }
        }
        
        public GraphWithCoords(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            this.x = new double[vertices];
            this.y = new double[vertices];
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        public void setCoordinates(int vertex, double x, double y) {
            this.x[vertex] = x;
            this.y[vertex] = y;
        }
        
        public void addEdge(int from, int to, double weight) {
            adj.get(from).add(new Edge(to, weight));
            adj.get(to).add(new Edge(from, weight)); // для неориентированного графа
        }
        
        // Эвклидова эвристика (расстояние по прямой)
        private double euclideanHeuristic(int from, int to) {
            double dx = x[from] - x[to];
            double dy = y[from] - y[to];
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        // Манхэттенская эвристика (для сеток)
        private double manhattanHeuristic(int from, int to) {
            return Math.abs(x[from] - x[to]) + Math.abs(y[from] - y[to]);
        }
        
        // Основной алгоритм A*
        public List<Integer> findPath(int start, int goal) {
            return findPath(start, goal, this::euclideanHeuristic);
        }
        
        public List<Integer> findPath(int start, int goal, Heuristic heuristic) {
            PriorityQueue<Node> openSet = new PriorityQueue<>();
            Map<Integer, Double> gScore = new HashMap<>();
            Map<Integer, Node> allNodes = new HashMap<>();
            
            Node startNode = new Node(start, 0, heuristic.estimate(start, goal), null);
            openSet.offer(startNode);
            gScore.put(start, 0.0);
            allNodes.put(start, startNode);
            
            Set<Integer> closedSet = new HashSet<>();
            
            while (!openSet.isEmpty()) {
                Node current = openSet.poll();
                
                if (current.id == goal) {
                    return reconstructPath(current);
                }
                
                closedSet.add(current.id);
                
                for (Edge edge : adj.get(current.id)) {
                    if (closedSet.contains(edge.to)) continue;
                    
                    double tentativeG = current.g + edge.weight;
                    
                    if (tentativeG < gScore.getOrDefault(edge.to, Double.POSITIVE_INFINITY)) {
                        Node neighbor = allNodes.getOrDefault(edge.to, new Node(edge.to));
                        neighbor.g = tentativeG;
                        neighbor.h = heuristic.estimate(edge.to, goal);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.parent = current;
                        
                        openSet.remove(neighbor); // обновляем в очереди
                        openSet.offer(neighbor);
                        gScore.put(edge.to, tentativeG);
                        allNodes.put(edge.to, neighbor);
                    }
                }
            }
            
            return new ArrayList<>(); // путь не найден
        }
        
        private List<Integer> reconstructPath(Node node) {
            List<Integer> path = new ArrayList<>();
            while (node != null) {
                path.add(node.id);
                node = node.parent;
            }
            Collections.reverse(path);
            return path;
        }
        
        @FunctionalInterface
        interface Heuristic {
            double estimate(int from, int to);
        }
    }
    
    // Простой граф без координат (с пользовательской эвристикой)
    static class SimpleGraph {
        private int vertices;
        private List<List<Edge>> adj;
        
        static class Edge {
            int to;
            double weight;
            Edge(int to, double weight) {
                this.to = to;
                this.weight = weight;
            }
        }
        
        public SimpleGraph(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int from, int to, double weight) {
            adj.get(from).add(new Edge(to, weight));
        }
        
        public List<Integer> findPath(int start, int goal, Map<Integer, Double> heuristic) {
            PriorityQueue<Node> openSet = new PriorityQueue<>();
            Map<Integer, Double> gScore = new HashMap<>();
            Map<Integer, Node> allNodes = new HashMap<>();
            
            Node startNode = new Node(start, 0, heuristic.getOrDefault(start, 0.0), null);
            openSet.offer(startNode);
            gScore.put(start, 0.0);
            allNodes.put(start, startNode);
            
            Set<Integer> closedSet = new HashSet<>();
            
            while (!openSet.isEmpty()) {
                Node current = openSet.poll();
                
                if (current.id == goal) {
                    return reconstructPath(current);
                }
                
                closedSet.add(current.id);
                
                for (Edge edge : adj.get(current.id)) {
                    if (closedSet.contains(edge.to)) continue;
                    
                    double tentativeG = current.g + edge.weight;
                    
                    if (tentativeG < gScore.getOrDefault(edge.to, Double.POSITIVE_INFINITY)) {
                        Node neighbor = allNodes.getOrDefault(edge.to, new Node(edge.to));
                        neighbor.g = tentativeG;
                        neighbor.h = heuristic.getOrDefault(edge.to, 0.0);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.parent = current;
                        
                        openSet.remove(neighbor);
                        openSet.offer(neighbor);
                        gScore.put(edge.to, tentativeG);
                        allNodes.put(edge.to, neighbor);
                    }
                }
            }
            
            return new ArrayList<>();
        }
        
        private List<Integer> reconstructPath(Node node) {
            List<Integer> path = new ArrayList<>();
            while (node != null) {
                path.add(node.id);
                node = node.parent;
            }
            Collections.reverse(path);
            return path;
        }
    }
}