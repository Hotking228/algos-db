# Задача о рюкзаке (0/1) (0/1 Knapsack Problem)

## Intuition
Задача о рюкзаке (0/1) — это классическая задача динамического программирования. У нас есть рюкзак вместимостью W и набор предметов, каждый из которых имеет вес и стоимость. Нужно выбрать предметы так, чтобы их суммарный вес не превышал W, а суммарная стоимость была максимальной. "0/1" означает, что каждый предмет можно либо взять целиком, либо не брать (нельзя взять часть предмета). Представьте, что вы собираетесь в поход и хотите взять с собой самые ценные вещи, но ваш рюкзак ограничен по весу.

## Approach
1. Создаем таблицу dp[n+1][W+1], где dp[i][w] — максимальная стоимость для первых i предметов при вместимости w
2. Базовый случай: dp[0][w] = 0 для всех w (нет предметов — нет стоимости)
3. Для каждого предмета i (1..n):
    - Если вес предмета > w: dp[i][w] = dp[i-1][w] (не берем)
    - Иначе: dp[i][w] = max(dp[i-1][w], dp[i-1][w - weight[i]] + value[i])
4. Ответ находится в dp[n][W]

## Complexity
- Time complexity: **O(n × W)**
- Space complexity: **O(n × W)** или **O(W)** с оптимизацией

## Code

```java
import java.util.*;

public class Knapsack01 {
    
    // Базовое решение с полной матрицей
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        dp[i - 1][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        return dp[n][capacity];
    }
    
    // Оптимизация по памяти: O(capacity)
    public static int knapsackOptimized(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            // Идем справа налево, чтобы не использовать один предмет дважды
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        
        return dp[capacity];
    }
    
    // Восстановление выбранных предметов
    public static List<Integer> knapsackWithItems(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        // Заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        dp[i - 1][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        // Восстанавливаем выбранные предметы
        List<Integer> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(i - 1); // индекс предмета
                w -= weights[i - 1];
            }
        }
        
        Collections.reverse(selected);
        return selected;
    }
    
    // Рекурсивное решение с мемоизацией
    public static int knapsackMemo(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Integer[][] memo = new Integer[n][capacity + 1];
        return knapsackMemoHelper(weights, values, capacity, 0, memo);
    }
    
    private static int knapsackMemoHelper(int[] weights, int[] values, int remaining, 
                                          int index, Integer[][] memo) {
        if (index >= weights.length || remaining <= 0) {
            return 0;
        }
        
        if (memo[index][remaining] != null) {
            return memo[index][remaining];
        }
        
        // Не берем текущий предмет
        int skip = knapsackMemoHelper(weights, values, remaining, index + 1, memo);
        
        // Берем текущий предмет
        int take = 0;
        if (weights[index] <= remaining) {
            take = values[index] + knapsackMemoHelper(weights, values, 
                       remaining - weights[index], index + 1, memo);
        }
        
        memo[index][remaining] = Math.max(skip, take);
        return memo[index][remaining];
    }
    
    // Для больших вместимостей (W большое, n маленькое) - встречается в задачах
    public static int knapsackLargeCapacity(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int totalValue = Arrays.stream(values).sum();
        
        // dp[v] = минимальный вес для достижения стоимости v
        int[] dp = new int[totalValue + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        for (int i = 0; i < n; i++) {
            for (int v = totalValue; v >= values[i]; v--) {
                if (dp[v - values[i]] != Integer.MAX_VALUE) {
                    dp[v] = Math.min(dp[v], dp[v - values[i]] + weights[i]);
                }
            }
        }
        
        int maxValue = 0;
        for (int v = totalValue; v >= 0; v--) {
            if (dp[v] <= capacity) {
                maxValue = v;
                break;
            }
        }
        
        return maxValue;
    }
    
    // Двумерная версия с восстановлением (слайсинг)
    public static class KnapsackResult {
        int maxValue;
        List<Integer> selectedIndices;
        int[] selectedWeights;
        int[] selectedValues;
        
        KnapsackResult(int maxValue, List<Integer> selectedIndices, 
                       int[] weights, int[] values) {
            this.maxValue = maxValue;
            this.selectedIndices = selectedIndices;
            this.selectedWeights = new int[selectedIndices.size()];
            this.selectedValues = new int[selectedIndices.size()];
            for (int i = 0; i < selectedIndices.size(); i++) {
                int idx = selectedIndices.get(i);
                this.selectedWeights[i] = weights[idx];
                this.selectedValues[i] = values[idx];
            }
        }
    }
    
    public static KnapsackResult knapsackDetailed(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        boolean[][] taken = new boolean[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w && 
                    dp[i - 1][w - weights[i - 1]] + values[i - 1] > dp[i - 1][w]) {
                    dp[i][w] = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                    taken[i][w] = true;
                } else {
                    dp[i][w] = dp[i - 1][w];
                    taken[i][w] = false;
                }
            }
        }
        
        List<Integer> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (taken[i][w]) {
                selected.add(i - 1);
                w -= weights[i - 1];
            }
        }
        Collections.reverse(selected);
        
        return new KnapsackResult(dp[n][capacity], selected, weights, values);
    }
}