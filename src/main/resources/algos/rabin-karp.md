# Алгоритм Рабина-Карпа (Rabin-Karp Algorithm)

## Intuition
Алгоритм Рабина-Карпа использует хеширование для поиска подстроки в строке. Вместо того чтобы сравнивать символы по одному, он вычисляет хеш-значение для каждого окна текста и сравнивает его с хешем образца. Если хеши совпадают, только тогда выполняется проверка на реальное совпадение (из-за возможных коллизий). Представьте, что вы ищете книгу по отпечатку пальца: вы быстро отбраковываете большинство окон, сравнивая только "отпечатки", и только при совпадении проверяете детали. Это особенно эффективно для поиска множества образцов одновременно.

## Approach
1. Вычисляем хеш образца
2. Вычисляем хеш первого окна текста (первые m символов)
3. Скользящим окном проходим по тексту:
    - Если хеши совпадают, проверяем реальное совпадение
    - Вычисляем хеш следующего окна, используя скользящий хеш: удаляем левый символ, добавляем правый
4. Для избежания больших чисел используем модульную арифметику

## Complexity
- Time complexity (средняя): **O(n + m)**
- Time complexity (худшая): **O(n × m)** (при многих коллизиях)
- Space complexity: **O(1)**

## Code

```java
import java.util.*;

public class RabinKarp {
    
    private static final int BASE = 256;      // основание (количество символов)
    private static final int MOD = 101;       // простое число для модуля
    
    // Поиск одного образца
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int m = pattern.length();
        int n = text.length();
        
        if (m > n) return occurrences;
        
        // Вычисляем хеш образца
        int patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
        }
        
        // Вычисляем хеш первого окна
        int textHash = 0;
        for (int i = 0; i < m; i++) {
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
        }
        
        // Вычисляем BASE^(m-1) % MOD
        int hashMultiplier = 1;
        for (int i = 0; i < m - 1; i++) {
            hashMultiplier = (hashMultiplier * BASE) % MOD;
        }
        
        // Скользящее окно
        for (int i = 0; i <= n - m; i++) {
            // Если хеши совпадают, проверяем реальное совпадение
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                }
            }
            
            // Вычисляем хеш следующего окна (если не последнее)
            if (i < n - m) {
                textHash = (textHash - text.charAt(i) * hashMultiplier) % MOD;
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
                if (textHash < 0) textHash += MOD;
            }
        }
        
        return occurrences;
    }
    
    // Поиск с большим простым модулем для уменьшения коллизий
    public static List<Integer> searchWithLargeMod(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int m = pattern.length();
        int n = text.length();
        
        if (m > n) return occurrences;
        
        long patternHash = 0;
        long textHash = 0;
        long hashMultiplier = 1;
        long MOD = 1_000_000_007L; // Большое простое число
        
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
            if (i < m - 1) {
                hashMultiplier = (hashMultiplier * BASE) % MOD;
            }
        }
        
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                }
            }
            
            if (i < n - m) {
                textHash = (textHash - text.charAt(i) * hashMultiplier) % MOD;
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
                if (textHash < 0) textHash += MOD;
            }
        }
        
        return occurrences;
    }
    
    // Поиск нескольких образцов одновременно
    public static Map<String, List<Integer>> searchMultiple(String text, List<String> patterns) {
        Map<String, List<Integer>> results = new HashMap<>();
        
        if (patterns.isEmpty()) return results;
        
        int minLen = Integer.MAX_VALUE;
        for (String p : patterns) {
            minLen = Math.min(minLen, p.length());
            results.put(p, new ArrayList<>());
        }
        
        int n = text.length();
        
        // Для каждого возможного окна
        for (int i = 0; i <= n - minLen; i++) {
            for (String pattern : patterns) {
                int m = pattern.length();
                if (i + m <= n) {
                    // Быстрая проверка по первому символу
                    if (text.charAt(i) == pattern.charAt(0) &&
                        text.charAt(i + m - 1) == pattern.charAt(m - 1)) {
                        // Проверка полного совпадения
                        boolean match = true;
                        for (int j = 1; j < m - 1; j++) {
                            if (text.charAt(i + j) != pattern.charAt(j)) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            results.get(pattern).add(i);
                        }
                    }
                }
            }
        }
        
        return results;
    }
    
    // С оптимизированным скользящим хешем для всех образцов (Rolling Hash)
    public static class RollingHash {
        private long[] hashes;
        private long[] powers;
        private long MOD = 1_000_000_007L;
        private int base = 91138233;
        
        public RollingHash(String text) {
            int n = text.length();
            hashes = new long[n + 1];
            powers = new long[n + 1];
            
            powers[0] = 1;
            for (int i = 1; i <= n; i++) {
                hashes[i] = (hashes[i - 1] * base + text.charAt(i - 1)) % MOD;
                powers[i] = (powers[i - 1] * base) % MOD;
            }
        }
        
        // Получение хеша подстроки [l, r) (0-индексация)
        public long getHash(int l, int r) {
            long hash = hashes[r] - hashes[l] * powers[r - l] % MOD;
            if (hash < 0) hash += MOD;
            return hash;
        }
    }
    
    // Поиск с предварительным вычислением хешей (быстрее для множественных запросов)
    public static List<Integer> searchWithPrecomputed(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int m = pattern.length();
        int n = text.length();
        
        if (m > n) return occurrences;
        
        RollingHash rh = new RollingHash(text);
        RollingHash rhPattern = new RollingHash(pattern);
        
        long patternHash = rhPattern.getHash(0, m);
        
        for (int i = 0; i <= n - m; i++) {
            long textHash = rh.getHash(i, i + m);
            if (textHash == patternHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                }
            }
        }
        
        return occurrences;
    }
    
    // Поиск с разными основаниями для уменьшения коллизий (двойной хеш)
    public static List<Integer> searchWithDoubleHash(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int m = pattern.length();
        int n = text.length();
        
        if (m > n) return occurrences;
        
        int BASE1 = 257, MOD1 = 1_000_000_007;
        int BASE2 = 263, MOD2 = 1_000_000_009;
        
        long patternHash1 = 0, patternHash2 = 0;
        long textHash1 = 0, textHash2 = 0;
        long hashMultiplier1 = 1, hashMultiplier2 = 1;
        
        for (int i = 0; i < m; i++) {
            patternHash1 = (patternHash1 * BASE1 + pattern.charAt(i)) % MOD1;
            patternHash2 = (patternHash2 * BASE2 + pattern.charAt(i)) % MOD2;
            textHash1 = (textHash1 * BASE1 + text.charAt(i)) % MOD1;
            textHash2 = (textHash2 * BASE2 + text.charAt(i)) % MOD2;
            
            if (i < m - 1) {
                hashMultiplier1 = (hashMultiplier1 * BASE1) % MOD1;
                hashMultiplier2 = (hashMultiplier2 * BASE2) % MOD2;
            }
        }
        
        for (int i = 0; i <= n - m; i++) {
            if (patternHash1 == textHash1 && patternHash2 == textHash2) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                }
            }
            
            if (i < n - m) {
                textHash1 = (textHash1 - text.charAt(i) * hashMultiplier1) % MOD1;
                textHash1 = (textHash1 * BASE1 + text.charAt(i + m)) % MOD1;
                if (textHash1 < 0) textHash1 += MOD1;
                
                textHash2 = (textHash2 - text.charAt(i) * hashMultiplier2) % MOD2;
                textHash2 = (textHash2 * BASE2 + text.charAt(i + m)) % MOD2;
                if (textHash2 < 0) textHash2 += MOD2;
            }
        }
        
        return occurrences;
    }
}