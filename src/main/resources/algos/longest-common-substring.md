# Поиск наибольшей общей подстроки (Longest Common Substring)

## Intuition
Задача нахождения наибольшей общей подстроки (LCS - Longest Common Substring, не путать с Longest Common Subsequence) — это поиск самой длинной строки, которая встречается как непрерывная подстрока в обеих входных строках. В отличие от общей подпоследовательности, подстрока должна быть непрерывной. Представьте, что у вас есть два текста, и вы хотите найти самый длинный фрагмент, который встречается слово в слово в обоих. Это полезно для обнаружения плагиата, поиска общих участков в ДНК или нахождения повторяющихся паттернов.

## Approach
**Метод 1: Динамическое программирование (O(n×m) по времени и памяти)**
- Создаем матрицу dp[n+1][m+1]
- dp[i][j] = длина наибольшей общей подстроки, заканчивающейся в s1[i-1] и s2[j-1]
- Обновляем: если s1[i-1] == s2[j-1], то dp[i][j] = dp[i-1][j-1] + 1, иначе 0
- Отслеживаем максимальную длину и ее позицию

**Метод 2: Бинарный поиск + роллинг хеш (O((n+m) log min(n,m)))**
- Используем бинарный поиск по длине подстроки
- Для каждой длины проверяем наличие общей подстроки с помощью хешей

**Метод 3: Суффиксный массив + LCP (O(n+m))**
- Строим суффиксный массив для объединенной строки
- Находим максимальный LCP между суффиксами из разных строк

## Complexity
- Time complexity (DP): **O(n × m)**
- Time complexity (Binary Search + Hash): **O((n+m) log(min(n,m)))**
- Time complexity (Suffix Array + LCP): **O(n+m)**
- Space complexity: **O(n × m)** для DP, **O(n+m)** для остальных

## Code

```java
import java.util.*;

public class LongestCommonSubstring {
    
    // Метод 1: Динамическое программирование (простой, но затратный по памяти)
    public static String longestCommonSubstringDP(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return "";
        }
        
        int n = s1.length();
        int m = s2.length();
        
        int[][] dp = new int[n + 1][m + 1];
        int maxLen = 0;
        int endPos = 0;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLen) {
                        maxLen = dp[i][j];
                        endPos = i; // конец подстроки в s1
                    }
                }
            }
        }
        
        return s1.substring(endPos - maxLen, endPos);
    }
    
    // Метод 2: Динамическое программирование с оптимизацией памяти (O(min(n,m)))
    public static String longestCommonSubstringDPOptimized(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return "";
        }
        
        // Выбираем более короткую строку для минимизации памяти
        if (s1.length() > s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }
        
        int n = s1.length();
        int m = s2.length();
        
        int[] dp = new int[n + 1];
        int maxLen = 0;
        int endPos = 0;
        
        for (int j = 1; j <= m; j++) {
            int prev = 0;
            for (int i = 1; i <= n; i++) {
                int current = dp[i];
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i] = prev + 1;
                    if (dp[i] > maxLen) {
                        maxLen = dp[i];
                        endPos = i;
                    }
                } else {
                    dp[i] = 0;
                }
                prev = current;
            }
        }
        
        return s1.substring(endPos - maxLen, endPos);
    }
    
    // Метод 3: Роллинг хеш + бинарный поиск
    public static String longestCommonSubstringHash(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return "";
        }
        
        int left = 0;
        int right = Math.min(s1.length(), s2.length());
        String result = "";
        
        while (left <= right) {
            int mid = (left + right) / 2;
            String common = hasCommonSubstringOfLength(s1, s2, mid);
            
            if (common != null) {
                result = common;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    private static String hasCommonSubstringOfLength(String s1, String s2, int len) {
        if (len == 0) return "";
        
        Set<Long> hashes = new HashSet<>();
        long base = 91138233;
        long mod = 1_000_000_007L;
        
        // Вычисляем хеши для s1
        long hash = 0;
        long pow = 1;
        
        for (int i = 0; i < len; i++) {
            hash = (hash * base + s1.charAt(i)) % mod;
            pow = (pow * base) % mod;
        }
        hashes.add(hash);
        
        for (int i = len; i < s1.length(); i++) {
            hash = (hash * base + s1.charAt(i)) % mod;
            hash = (hash - s1.charAt(i - len) * pow) % mod;
            if (hash < 0) hash += mod;
            hashes.add(hash);
        }
        
        // Проверяем хеши для s2
        hash = 0;
        for (int i = 0; i < len; i++) {
            hash = (hash * base + s2.charAt(i)) % mod;
        }
        
        if (hashes.contains(hash)) {
            return s2.substring(0, len);
        }
        
        for (int i = len; i < s2.length(); i++) {
            hash = (hash * base + s2.charAt(i)) % mod;
            hash = (hash - s2.charAt(i - len) * pow) % mod;
            if (hash < 0) hash += mod;
            
            if (hashes.contains(hash)) {
                return s2.substring(i - len + 1, i + 1);
            }
        }
        
        return null;
    }
    
    // Метод 4: Суффиксный массив + LCP
    public static String longestCommonSubstringSuffixArray(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return "";
        }
        
        String combined = s1 + "#" + s2 + "$";
        SuffixArray sa = new SuffixArray(combined);
        
        int maxLen = 0;
        int maxIndex = -1;
        int len1 = s1.length();
        
        for (int i = 1; i < sa.n; i++) {
            int idx1 = sa.suffixArray[i - 1];
            int idx2 = sa.suffixArray[i];
            
            // Проверяем, что суффиксы из разных строк
            boolean fromDifferent = (idx1 < len1 && idx2 > len1) || 
                                    (idx2 < len1 && idx1 > len1);
            
            if (fromDifferent && sa.lcp[i - 1] > maxLen) {
                maxLen = sa.lcp[i - 1];
                maxIndex = Math.min(idx1, idx2);
            }
        }
        
        if (maxIndex == -1) return "";
        return combined.substring(maxIndex, maxIndex + maxLen);
    }
    
    // Вспомогательный класс суффиксного массива (упрощенная версия)
    static class SuffixArray {
        int n;
        Integer[] suffixArray;
        int[] lcp;
        
        SuffixArray(String text) {
            this.n = text.length();
            suffixArray = new Integer[n];
            for (int i = 0; i < n; i++) {
                suffixArray[i] = i;
            }
            
            Arrays.sort(suffixArray, (a, b) -> 
                text.substring(a).compareTo(text.substring(b)));
            
            buildLCP(text);
        }
        
        private void buildLCP(String text) {
            lcp = new int[n - 1];
            int[] rank = new int[n];
            
            for (int i = 0; i < n; i++) {
                rank[suffixArray[i]] = i;
            }
            
            int k = 0;
            for (int i = 0; i < n; i++) {
                if (rank[i] == n - 1) {
                    k = 0;
                    continue;
                }
                
                int j = suffixArray[rank[i] + 1];
                
                while (i + k < n && j + k < n && 
                       text.charAt(i + k) == text.charAt(j + k)) {
                    k++;
                }
                
                lcp[rank[i]] = k;
                if (k > 0) k--;
            }
        }
    }
    
    // Поиск всех общих подстрок заданной минимальной длины
    public static Set<String> findAllCommonSubstrings(String s1, String s2, int minLen) {
        Set<String> result = new HashSet<>();
        int n = s1.length();
        int m = s2.length();
        
        // Используем DP подход для нахождения всех совпадений
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] >= minLen) {
                        result.add(s1.substring(i - dp[i][j], i));
                    }
                }
            }
        }
        
        return result;
    }
    
    // Нахождение наибольшей общей подстроки для нескольких строк
    public static String longestCommonSubstringMultiple(List<String> strings) {
        if (strings == null || strings.isEmpty()) return "";
        if (strings.size() == 1) return strings.get(0);
        
        String result = strings.get(0);
        
        for (int i = 1; i < strings.size(); i++) {
            result = longestCommonSubstringDP(result, strings.get(i));
            if (result.isEmpty()) break;
        }
        
        return result;
    }
}