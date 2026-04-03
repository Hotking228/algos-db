# Алгоритм Флойда (Floyd-Warshall)

## Intuition
Алгоритм Флойда-Уоршелла находит кратчайшие пути между всеми парами вершин во взвешенном графе. Основная идея: динамическое программирование. Мы постепенно разрешаем использовать промежуточные вершины для построения более коротких путей. На каждой итерации добавляем новую вершину k и проверяем, не станет ли путь от i до j короче, если проходить через k.

## Approach
1. Инициализируем матрицу расстояний dist[i][j]:
    - dist[i][j] = 0, если i == j
    - dist[i][j] = вес ребра, если ребро существует
    - dist[i][j] = ∞, если ребра нет
2. Для каждой промежуточной вершины k от 0 до V-1:
    - Для каждой вершины i от 0 до V-1:
        - Для каждой вершины j от 0 до V-1:
            - Если путь i -> k -> j короче, чем текущий i -> j, обновляем dist[i][j]
3. После обработки всех k, dist[i][j] содержит кратчайшее расстояние

## Complexity
- Time complexity: $$O(V^3)$$ (три вложенных цикла)

- Space complexity: $$O(V^2)$$ для матрицы расстояний

## Code

### Базовая реализация
```java
public class FloydWarshall {
    
    static final int INF = Integer.MAX_VALUE / 2; // избегаем переполнения
    
    public int[][] floydWarshall(int[][] graph, int vertices) {
        // Копируем исходную матрицу
        int[][] dist = new int[vertices][vertices];
        
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
            }
        }
        
        // Основной алгоритм
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        
        return dist;
    }
}
```