# Расстояние Левенштейна (Levenshtein Distance)

## Intuition
Расстояние Левенштейна — это метрика, измеряющая минимальное количество односимвольных операций (вставки, удаления, замены), необходимых для преобразования одной строки в другую. Представьте, что вы исправляете опечатки: сколько минимальных правок нужно сделать, чтобы слово "кот" превратилось в "ток"? Это расстояние широко используется в автокоррекции, поиске дубликатов, анализе ДНК и проверке орфографии.

## Approach
1. Создаем матрицу dp размером (n+1) × (m+1), где n и m — длины строк
2. Инициализируем первую строку и первый столбец:
    - dp[i][0] = i (удаление i символов)
    - dp[0][j] = j (вставка j символов)
3. Для каждого i от 1 до n и j от 1 до m:
    - Если символы равны: dp[i][j] = dp[i-1][j-1]
    - Иначе: dp[i][j] = 1 + min(
      dp[i-1][j],   // удаление
      dp[i][j-1],   // вставка
      dp[i-1][j-1]  // замена
      )
4. Ответ находится в dp[n][m]

## Complexity
- Time complexity: **O(n × m)**
- Space complexity: **O(n × m)** или **O(min(n,m))** с оптимизацией

## Code

```java
public class LevenshteinDistance {
    
    // Базовая версия с полной матрицей
    public static int distance(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        
        int n = s1.length();
        int m = s2.length();
        
        int[][] dp = new int[n + 1][m + 1];
        
        // Инициализация
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        
        // Заполнение матрицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], 
                                    Math.min(dp[i][j - 1], 
                                             dp[i - 1][j - 1]));
                }
            }
        }
        
        return dp[n][m];
    }
    
    // Оптимизированная версия с двумя строками (O(min(n,m)) памяти)
    public static int distanceOptimized(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        
        // Работаем с более короткой строкой как с "внутренней" для экономии памяти
        if (s1.length() > s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }
        
        int n = s1.length();
        int m = s2.length();
        
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        // Инициализация
        for (int i = 0; i <= n; i++) {
            prev[i] = i;
        }
        
        for (int j = 1; j <= m; j++) {
            curr[0] = j;
            for (int i = 1; i <= n; i++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[i] = prev[i - 1];
                } else {
                    curr[i] = 1 + Math.min(prev[i], 
                                   Math.min(curr[i - 1], 
                                            prev[i - 1]));
                }
            }
            // Swap
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[n];
    }
    
    // Версия с возможностью настройки весов операций
    public static int distanceWithWeights(String s1, String s2, 
                                          int insertCost, int deleteCost, 
                                          int replaceCost) {
        if (s1 == null || s2 == null) return -1;
        
        int n = s1.length();
        int m = s2.length();
        
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i * deleteCost;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j * insertCost;
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1] + insertCost;
                    int delete = dp[i - 1][j] + deleteCost;
                    int replace = dp[i - 1][j - 1] + replaceCost;
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        
        return dp[n][m];
    }
    
    // Версия с ограничением максимального расстояния (ранний выход)
    public static int distanceWithLimit(String s1, String s2, int maxDistance) {
        if (s1 == null || s2 == null) return -1;
        if (Math.abs(s1.length() - s2.length()) > maxDistance) {
            return maxDistance + 1;
        }
        
        int n = s1.length();
        int m = s2.length();
        
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            prev[i] = i;
        }
        
        for (int j = 1; j <= m; j++) {
            curr[0] = j;
            int minInRow = curr[0];
            
            for (int i = 1; i <= n; i++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[i] = prev[i - 1];
                } else {
                    curr[i] = 1 + Math.min(prev[i], 
                                   Math.min(curr[i - 1], 
                                            prev[i - 1]));
                }
                minInRow = Math.min(minInRow, curr[i]);
            }
            
            if (minInRow > maxDistance) {
                return maxDistance + 1;
            }
            
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[n];
    }
    
    // Восстановление последовательности операций
    public static List<String> getOperations(String s1, String s2) {
        List<String> operations = new ArrayList<>();
        int n = s1.length();
        int m = s2.length();
        
        int[][] dp = new int[n + 1][m + 1];
        String[][] op = new String[n + 1][m + 1];
        
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = i > 0 ? "DELETE " + s1.charAt(i - 1) : "";
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = j > 0 ? "INSERT " + s2.charAt(j - 1) : "";
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = "KEEP " + s1.charAt(i - 1);
                } else {
                    int delete = dp[i - 1][j];
                    int insert = dp[i][j - 1];
                    int replace = dp[i - 1][j - 1];
                    
                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete + 1;
                        op[i][j] = "DELETE " + s1.charAt(i - 1);
                    } else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert + 1;
                        op[i][j] = "INSERT " + s2.charAt(j - 1);
                    } else {
                        dp[i][j] = replace + 1;
                        op[i][j] = "REPLACE " + s1.charAt(i - 1) + " -> " + s2.charAt(j - 1);
                    }
                }
            }
        }
        
        // Восстановление операций
        int i = n, j = m;
        while (i > 0 || j > 0) {
            operations.add(0, op[i][j]);
            if (op[i][j].startsWith("KEEP") || op[i][j].startsWith("REPLACE")) {
                i--; j--;
            } else if (op[i][j].startsWith("DELETE")) {
                i--;
            } else if (op[i][j].startsWith("INSERT")) {
                j--;
            }
        }
        
        return operations;
    }
    
    // Нормализованное расстояние (0-1)
    public static double normalizedDistance(String s1, String s2) {
        int distance = distance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 0.0;
        return (double) distance / maxLen;
    }
    
    // Сходство строк (0-1)
    public static double similarity(String s1, String s2) {
        return 1.0 - normalizedDistance(s1, s2);
    }
    
    // Расстояние Дамерау-Левенштейна (с учетом транспозиции соседних символов)
    public static int damerauLevenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        
        int n = s1.length();
        int m = s2.length();
        
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                
                dp[i][j] = Math.min(dp[i - 1][j] + 1,
                           Math.min(dp[i][j - 1] + 1,
                                    dp[i - 1][j - 1] + cost));
                
                // Транспозиция соседних символов
                if (i > 1 && j > 1 && 
                    s1.charAt(i - 1) == s2.charAt(j - 2) && 
                    s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }
        
        return dp[n][m];
    }
}