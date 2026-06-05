# Наибольшая общая подпоследовательность (LCS - Longest Common Subsequence)

## Intuition
Наибольшая общая подпоследовательность (LCS) — это классическая задача динамического программирования, которая находит самую длинную последовательность символов, которая встречается в обеих строках в одинаковом порядке (но не обязательно непрерывно). В отличие от подстроки, подпоследовательность может иметь пропуски. Например, для строк "ABCD" и "ACBD" LCS будет "ABD" (длина 3). Представьте, что вы ищете эволюционные изменения в ДНК: общая подпоследовательность показывает консервативные участки, которые не изменились в процессе эволюции.

## Approach
1. Создаем таблицу dp[n+1][m+1], где dp[i][j] — длина LCS для префиксов строк s1[0..i-1] и s2[0..j-1]
2. Базовый случай: dp[0][j] = 0, dp[i][0] = 0
3. Заполняем таблицу:
    - Если s1[i-1] == s2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
    - Иначе: dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1])
4. Ответ находится в dp[n][m]
5. Для восстановления подпоследовательности идем от dp[n][m] обратно

## Complexity
- Time complexity: **O(n × m)**
- Space complexity: **O(n × m)** или **O(min(n,m))** с оптимизацией

## Code

```java
import java.util.*;

public class LCS {
    
    // Базовое решение с полной матрицей
    public static int lcsLength(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[n][m];
    }
    
    // Восстановление самой подпоследовательности
    public static String lcsString(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // Восстановление строки
        StringBuilder lcs = new StringBuilder();
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return lcs.reverse().toString();
    }
    
    // Оптимизация по памяти (две строки)
    public static int lcsLengthOptimized(String s1, String s2) {
        // Выбираем более короткую строку для внутреннего цикла
        if (s1.length() < s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }
        
        int n = s1.length();
        int m = s2.length();
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            // Swap
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[m];
    }
    
    // Нахождение всех LCS (может быть много)
    public static Set<String> findAllLCS(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        Set<String> allLCS = new HashSet<>();
        findAllLCSRecursive(s1, s2, n, m, dp, new StringBuilder(), allLCS);
        return allLCS;
    }
    
    private static void findAllLCSRecursive(String s1, String s2, int i, int j, 
                                            int[][] dp, StringBuilder current, 
                                            Set<String> result) {
        if (i == 0 || j == 0) {
            result.add(current.reverse().toString());
            current.reverse(); // возвращаем обратно
            return;
        }
        
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            current.append(s1.charAt(i - 1));
            findAllLCSRecursive(s1, s2, i - 1, j - 1, dp, current, result);
            current.deleteCharAt(current.length() - 1);
        } else {
            if (dp[i - 1][j] > dp[i][j - 1]) {
                findAllLCSRecursive(s1, s2, i - 1, j, dp, current, result);
            } else if (dp[i][j - 1] > dp[i - 1][j]) {
                findAllLCSRecursive(s1, s2, i, j - 1, dp, current, result);
            } else {
                findAllLCSRecursive(s1, s2, i - 1, j, dp, current, result);
                findAllLCSRecursive(s1, s2, i, j - 1, dp, current, result);
            }
        }
    }
    
    // LCS для массивов (не только строк)
    public static <T> int lcsArray(T[] arr1, T[] arr2) {
        int n = arr1.length;
        int m = arr2.length;
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (arr1[i - 1].equals(arr2[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[n][m];
    }
    
    // LCS для списков
    public static <T> int lcsList(List<T> list1, List<T> list2) {
        return lcsArray(list1.toArray(), list2.toArray());
    }
    
    // Коэффициент сходства (нормализованный LCS)
    public static double similarity(String s1, String s2) {
        int lcs = lcsLength(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        return (double) lcs / maxLen;
    }
    
    // LCS с запоминанием всех совпадений (для отладки)
    public static class LCSResult {
        int length;
        String sequence;
        int[][] dp;
        
        LCSResult(int length, String sequence, int[][] dp) {
            this.length = length;
            this.sequence = sequence;
            this.dp = dp;
        }
        
        void printMatrix() {
            for (int[] row : dp) {
                for (int val : row) {
                    System.out.print(val + " ");
                }
                System.out.println();
            }
        }
    }
    
    public static LCSResult lcsDetailed(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // Восстановление строки
        StringBuilder lcs = new StringBuilder();
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return new LCSResult(dp[n][m], lcs.reverse().toString(), dp);
    }
}