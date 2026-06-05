# Поиск в глубину (DFS) на матрице (DFS on Matrix)

## Intuition
Поиск в глубину на матрице — это алгоритм обхода или поиска в двумерной сетке, который идет как можно глубже по одному пути, прежде чем вернуться и исследовать другие. Представьте, что вы исследуете лабиринт: вы идете по коридору до конца, затем возвращаетесь к последнему развилку и идете по другому пути. DFS на матрице особенно полезен для задач, связанных с поиском путей, подсчетом компонент связности, поиском выходов из лабиринта и решением головоломок.

## Approach
1. **Рекурсивный подход**:
    - Начинаем с ячейки (startRow, startCol)
    - Отмечаем её как посещенную
    - Рекурсивно вызываем DFS для всех соседних валидных ячеек
2. **Итеративный подход со стеком**:
    - Используем стек вместо рекурсии для избежания переполнения
    - Помещаем стартовую ячейку в стек
    - Пока стек не пуст, извлекаем ячейку и обрабатываем её соседей
3. Для нахождения кратчайшего пути DFS не подходит (используйте BFS), но для проверки существования пути — да

## Complexity
- Time complexity: **O(rows × cols)** — каждая ячейка посещается не более одного раза
- Space complexity: **O(rows × cols)** — для стека рекурсии или visited массива

## Code

```java
import java.util.*;

public class DFSMatrix {
    
    // Направления движения: вверх, вниз, влево, вправо
    private static final int[][] DIRECTIONS = {
        {-1, 0},  // вверх
        {1, 0},   // вниз
        {0, -1},  // влево
        {0, 1}    // вправо
    };
    
    // Рекурсивный DFS для поиска пути
    public static boolean findPathRecursive(int[][] grid, int[] start, int[] target) {
        if (grid == null || grid.length == 0) return false;
        
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        
        return dfsRecursive(grid, visited, start[0], start[1], target[0], target[1]);
    }
    
    private static boolean dfsRecursive(int[][] grid, boolean[][] visited, 
                                        int row, int col, int targetRow, int targetCol) {
        // Проверяем выход за границы или посещение
        if (!isValid(grid, row, col, visited)) return false;
        
        // Нашли цель
        if (row == targetRow && col == targetCol) return true;
        
        visited[row][col] = true;
        
        // Рекурсивно исследуем всех соседей
        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (dfsRecursive(grid, visited, newRow, newCol, targetRow, targetCol)) {
                return true;
            }
        }
        
        // Если хотим найти все пути, не делаем visited[row][col] = false
        // Но для поиска одного пути это закомментировано
        // visited[row][col] = false; // для поиска всех путей раскомментировать
        
        return false;
    }
    
    // Итеративный DFS со стеком
    public static boolean findPathIterative(int[][] grid, int[] start, int[] target) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Stack<int[]> stack = new Stack<>();
        
        stack.push(new int[]{start[0], start[1]});
        
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int row = current[0];
            int col = current[1];
            
            if (!isValid(grid, row, col, visited)) continue;
            
            visited[row][col] = true;
            
            if (row == target[0] && col == target[1]) return true;
            
            // Добавляем соседей в стек (обратный порядок для сохранения направления)
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (isValid(grid, newRow, newCol, visited)) {
                    stack.push(new int[]{newRow, newCol});
                }
            }
        }
        
        return false;
    }
    
    // Поиск всех путей от start до target (с возвратами)
    public static List<List<int[]>> findAllPaths(int[][] grid, int[] start, int[] target) {
        List<List<int[]>> allPaths = new ArrayList<>();
        List<int[]> currentPath = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        
        currentPath.add(start);
        dfsFindAllPaths(grid, visited, start[0], start[1], target[0], target[1], 
                       currentPath, allPaths);
        
        return allPaths;
    }
    
    private static void dfsFindAllPaths(int[][] grid, boolean[][] visited,
                                        int row, int col, int targetRow, int targetCol,
                                        List<int[]> currentPath, List<List<int[]>> allPaths) {
        if (row == targetRow && col == targetCol) {
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }
        
        visited[row][col] = true;
        
        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (isValid(grid, newRow, newCol, visited)) {
                currentPath.add(new int[]{newRow, newCol});
                dfsFindAllPaths(grid, visited, newRow, newCol, targetRow, targetCol,
                               currentPath, allPaths);
                currentPath.remove(currentPath.size() - 1);
            }
        }
        
        visited[row][col] = false; // Важно для поиска всех путей
    }
    
    // Подсчет количества островов (компонент связности)
    public static int countIslands(int[][] grid, int landValue) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int islands = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == landValue && !visited[i][j]) {
                    dfsFill(grid, visited, i, j, landValue);
                    islands++;
                }
            }
        }
        
        return islands;
    }
    
    private static void dfsFill(int[][] grid, boolean[][] visited, int row, int col, int landValue) {
        if (!isValid(grid, row, col, visited) || grid[row][col] != landValue) return;
        
        visited[row][col] = true;
        
        for (int[] dir : DIRECTIONS) {
            dfsFill(grid, visited, row + dir[0], col + dir[1], landValue);
        }
    }
    
    // Поиск максимальной площади острова
    public static int maxIslandArea(int[][] grid, int landValue) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int maxArea = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == landValue && !visited[i][j]) {
                    int area = dfsArea(grid, visited, i, j, landValue);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        
        return maxArea;
    }
    
    private static int dfsArea(int[][] grid, boolean[][] visited, int row, int col, int landValue) {
        if (!isValid(grid, row, col, visited) || grid[row][col] != landValue) return 0;
        
        visited[row][col] = true;
        int area = 1;
        
        for (int[] dir : DIRECTIONS) {
            area += dfsArea(grid, visited, row + dir[0], col + dir[1], landValue);
        }
        
        return area;
    }
    
    // Проверка, есть ли путь через все ячейки (гамильтонов путь)
    public static boolean hasHamiltonianPath(int[][] grid, int[] start) {
        int rows = grid.length;
        int cols = grid[0].length;
        int totalCells = rows * cols;
        boolean[][] visited = new boolean[rows][cols];
        int[] count = {0};
        
        dfsHamiltonian(grid, visited, start[0], start[1], 1, totalCells, count);
        return count[0] > 0;
    }
    
    private static void dfsHamiltonian(int[][] grid, boolean[][] visited,
                                      int row, int col, int visitedCount, int totalCells, int[] count) {
        if (visitedCount == totalCells) {
            count[0]++;
            return;
        }
        
        visited[row][col] = true;
        
        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (isValidCell(grid, newRow, newCol) && !visited[newRow][newCol]) {
                dfsHamiltonian(grid, visited, newRow, newCol, visitedCount + 1, totalCells, count);
            }
        }
        
        visited[row][col] = false;
    }
    
    // Обход матрицы в порядке DFS (спираль через рекурсию - неэффективно)
    public static List<Integer> dfsOrder(int[][] grid) {
        List<Integer> order = new ArrayList<>();
        if (grid == null || grid.length == 0) return order;
        
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        dfsOrderRecursive(grid, visited, 0, 0, order);
        
        return order;
    }
    
    private static void dfsOrderRecursive(int[][] grid, boolean[][] visited,
                                         int row, int col, List<Integer> order) {
        if (!isValidCell(grid, row, col) || visited[row][col]) return;
        
        visited[row][col] = true;
        order.add(grid[row][col]);
        
        for (int[] dir : DIRECTIONS) {
            dfsOrderRecursive(grid, visited, row + dir[0], col + dir[1], order);
        }
    }
    
    // Проверка, можно ли выйти из лабиринта
    public static boolean canEscape(int[][] grid, int[] start) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        
        return dfsEscape(grid, visited, start[0], start[1], rows, cols);
    }
    
    private static boolean dfsEscape(int[][] grid, boolean[][] visited,
                                    int row, int col, int rows, int cols) {
        if (!isValid(grid, row, col, visited)) return false;
        
        // На границе
        if (row == 0 || row == rows - 1 || col == 0 || col == cols - 1) {
            return true;
        }
        
        visited[row][col] = true;
        
        for (int[] dir : DIRECTIONS) {
            if (dfsEscape(grid, visited, row + dir[0], col + dir[1], rows, cols)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Вспомогательные методы
    private static boolean isValid(int[][] grid, int row, int col, boolean[][] visited) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
               grid[row][col] != 0 && !visited[row][col];
    }
    
    private static boolean isValidCell(int[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
               grid[row][col] != 0;
    }
}