# Поиск в ширину (BFS) на матрице (BFS on Matrix)

## Intuition
Поиск в ширину на матрице — это алгоритм обхода или поиска кратчайшего пути в двумерной сетке, где каждая ячейка является вершиной, а переходы возможны по соседним ячейкам (вверх, вниз, влево, вправо, иногда по диагонали). BFS идеально подходит для нахождения кратчайшего пути в невзвешенном графе, каким и является сетка. Представьте, что вы находитесь на шахматной доске и хотите найти кратчайший маршрут до цели, делая шаги только на соседние клетки. BFS исследует все клетки на расстоянии 1, затем на расстоянии 2 и так далее, пока не найдет цель.

## Approach
1. Инициализируем очередь для хранения координат ячеек
2. Создаем массив visited для отслеживания посещенных ячеек
3. Помещаем стартовую ячейку в очередь и отмечаем её как посещенную
4. Пока очередь не пуста:
    - Извлекаем ячейку из очереди
    - Проверяем все допустимые направления (соседние ячейки)
    - Если соседняя ячейка валидна (в пределах границ, не стена, не посещена)
        - Отмечаем её как посещенную
        - Записываем расстояние (parent для восстановления пути)
        - Добавляем в очередь
5. Если нашли целевую ячейку — возвращаем результат

## Complexity
- Time complexity: **O(rows × cols)** — каждая ячейка посещается не более одного раза
- Space complexity: **O(rows × cols)** — для очереди и visited

## Code

```java
import java.util.*;

public class BFSMatrix {
    
    // Направления движения: вверх, вниз, влево, вправо
    private static final int[][] DIRECTIONS = {
        {-1, 0},  // вверх
        {1, 0},   // вниз
        {0, -1},  // влево
        {0, 1}    // вправо
    };
    
    // Направления с диагоналями
    private static final int[][] DIRECTIONS_WITH_DIAG = {
        {-1, 0}, {-1, 1}, {0, 1}, {1, 1},
        {1, 0}, {1, -1}, {0, -1}, {-1, -1}
    };
    
    static class Cell {
        int row, col;
        int distance;
        
        Cell(int row, int col) {
            this.row = row;
            this.col = col;
            this.distance = 0;
        }
        
        Cell(int row, int col, int distance) {
            this.row = row;
            this.col = col;
            this.distance = distance;
        }
    }
    
    // BFS для поиска кратчайшего пути от start до target
    public static int shortestPath(int[][] grid, int[] start, int[] target) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        boolean[][] visited = new boolean[rows][cols];
        Queue<Cell> queue = new LinkedList<>();
        
        queue.offer(new Cell(start[0], start[1]));
        visited[start[0]][start[1]] = true;
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            
            // Проверяем, достигли ли цели
            if (current.row == target[0] && current.col == target[1]) {
                return current.distance;
            }
            
            // Исследуем соседей
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValid(grid, newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    queue.offer(new Cell(newRow, newCol, current.distance + 1));
                }
            }
        }
        
        return -1; // Путь не найден
    }
    
    // BFS для поиска всех кратчайших путей
    public static List<List<int[]>> findAllShortestPaths(int[][] grid, int[] start, int[] target) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        
        List<int[]>[][] parent = new ArrayList[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                parent[i][j] = new ArrayList<>();
            }
        }
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start[0], start[1]});
        dist[start[0]][start[1]] = 0;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            
            if (r == target[0] && c == target[1]) continue;
            
            for (int[] dir : DIRECTIONS) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];
                
                if (isValidCell(grid, newRow, newCol)) {
                    if (dist[newRow][newCol] > dist[r][c] + 1) {
                        dist[newRow][newCol] = dist[r][c] + 1;
                        parent[newRow][newCol].clear();
                        parent[newRow][newCol].add(new int[]{r, c});
                        queue.offer(new int[]{newRow, newCol});
                    } else if (dist[newRow][newCol] == dist[r][c] + 1) {
                        parent[newRow][newCol].add(new int[]{r, c});
                    }
                }
            }
        }
        
        // Восстанавливаем все пути
        List<List<int[]>> allPaths = new ArrayList<>();
        List<int[]> currentPath = new ArrayList<>();
        currentPath.add(target);
        buildPaths(parent, target[0], target[1], currentPath, allPaths);
        
        return allPaths;
    }
    
    private static void buildPaths(List<int[]>[][] parent, int r, int c, 
                                   List<int[]> currentPath, List<List<int[]>> allPaths) {
        if (parent[r][c].isEmpty()) {
            List<int[]> path = new ArrayList<>(currentPath);
            Collections.reverse(path);
            allPaths.add(path);
            return;
        }
        
        for (int[] p : parent[r][c]) {
            currentPath.add(p);
            buildPaths(parent, p[0], p[1], currentPath, allPaths);
            currentPath.remove(currentPath.size() - 1);
        }
    }
    
    // BFS для нахождения расстояния до ближайшего препятствия
    public static int[][] distanceToObstacle(int[][] grid, int obstacleValue) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, -1);
        
        Queue<int[]> queue = new LinkedList<>();
        
        // Добавляем все препятствия в очередь
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == obstacleValue) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j});
                }
            }
        }
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            
            for (int[] dir : DIRECTIONS) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];
                
                if (isValidCell(grid, newRow, newCol) && dist[newRow][newCol] == -1) {
                    dist[newRow][newCol] = dist[r][c] + 1;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        
        return dist;
    }
    
    // BFS для нахождения максимального расстояния до любой ячейки (диаметр сетки)
    public static int maxDistanceFromStart(int[][] grid, int[] start) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Cell> queue = new LinkedList<>();
        
        queue.offer(new Cell(start[0], start[1]));
        visited[start[0]][start[1]] = true;
        
        int maxDist = 0;
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            maxDist = Math.max(maxDist, current.distance);
            
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValid(grid, newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    queue.offer(new Cell(newRow, newCol, current.distance + 1));
                }
            }
        }
        
        return maxDist;
    }
    
    // BFS для подсчета количества островов (компонент связности)
    public static int countIslands(int[][] grid, int landValue) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int islands = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == landValue && !visited[i][j]) {
                    bfsFill(grid, visited, i, j, landValue);
                    islands++;
                }
            }
        }
        
        return islands;
    }
    
    private static void bfsFill(int[][] grid, boolean[][] visited, int startRow, int startCol, int landValue) {
        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            
            for (int[] dir : DIRECTIONS) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];
                
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                    grid[newRow][newCol] == landValue && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
    }
    
    // Вспомогательные методы
    private static boolean isValid(int[][] grid, int row, int col, boolean[][] visited) {
        int rows = grid.length;
        int cols = grid[0].length;
        return row >= 0 && row < rows && col >= 0 && col < cols && 
               grid[row][col] != 0 && !visited[row][col];
    }
    
    private static boolean isValidCell(int[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;
        return row >= 0 && row < rows && col >= 0 && col < cols && grid[row][col] != 0;
    }
}