# Алгоритм Карпа-Рабина (Karp-Rabin Algorithm)

## Intuition
Алгоритм Карпа-Рабина — это алгоритм поиска подстроки, использующий хеширование. Он похож на алгоритм Рабина-Карпа, но назван в честь Карпа и Рабина, которые независимо разработали его. Для каждого окна текста вычисляется хеш-значение, которое сравнивается с хешем образца. При совпадении хешей выполняется проверка на реальное совпадение. Использование скользящего хеша позволяет вычислять хеш следующего окна за O(1) вместо O(m). Это особенно эффективно для поиска множества образцов одновременно и для задач, где хеш-функция может быть вычислена быстро.

## Approach
1. Вычисляем хеш образца (hashPattern)
2. Вычисляем хеш первого окна текста (hashWindow)
3. Для каждого окна текста (скользящее окно):
    - Если хеши совпадают, проверяем реальное совпадение
    - Вычисляем хеш следующего окна:
      hashWindow = (hashWindow - text[i] * multiplier) * base + text[i+m]
4. Используем модульную арифметику для предотвращения переполнения

## Complexity
- Time complexity (средняя): **O(n + m)**
- Time complexity (худшая): **O(n × m)** (при многих коллизиях)
- Space complexity: **O(1)**

## Code

```java
import java.util.*;

public class KarpRabin {
    
    private static final int BASE = 256;
    private static final int MOD = 1_000_000_007;
    
    // Поиск одного образца
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int n = text.length();
        int m = pattern.length();
        
        if (m > n) return occurrences;
        
        // Предварительное вычисление хеша образца
        long patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
        }
        
        // Вычисление BASE^(m-1) % MOD
        long multiplier = 1;
        for (int i = 0; i < m - 1; i++) {
            multiplier = (multiplier * BASE) % MOD;
        }
        
        // Вычисление хеша первого окна
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }
        
        // Поиск
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash) {
                // Проверка на реальное совпадение
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
            
            // Вычисление хеша следующего окна
            if (i < n - m) {
                windowHash = (windowHash - text.charAt(i) * multiplier) % MOD;
                windowHash = (windowHash * BASE + text.charAt(i + m)) % MOD;
                if (windowHash < 0) windowHash += MOD;
            }
        }
        
        return occurrences;
    }
    
    // Поиск с двойным хешем (меньше коллизий)
    public static List<Integer> searchDoubleHash(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int n = text.length();
        int m = pattern.length();
        
        if (m > n) return occurrences;
        
        int MOD1 = 1_000_000_007;
        int MOD2 = 1_000_000_009;
        
        long patternHash1 = 0, patternHash2 = 0;
        for (int i = 0; i < m; i++) {
            patternHash1 = (patternHash1 * BASE + pattern.charAt(i)) % MOD1;
            patternHash2 = (patternHash2 * BASE + pattern.charAt(i)) % MOD2;
        }
        
        long multiplier1 = 1, multiplier2 = 1;
        for (int i = 0; i < m - 1; i++) {
            multiplier1 = (multiplier1 * BASE) % MOD1;
            multiplier2 = (multiplier2 * BASE) % MOD2;
        }
        
        long windowHash1 = 0, windowHash2 = 0;
        for (int i = 0; i < m; i++) {
            windowHash1 = (windowHash1 * BASE + text.charAt(i)) % MOD1;
            windowHash2 = (windowHash2 * BASE + text.charAt(i)) % MOD2;
        }
        
        for (int i = 0; i <= n - m; i++) {
            if (patternHash1 == windowHash1 && patternHash2 == windowHash2) {
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
                windowHash1 = (windowHash1 - text.charAt(i) * multiplier1) % MOD1;
                windowHash1 = (windowHash1 * BASE + text.charAt(i + m)) % MOD1;
                if (windowHash1 < 0) windowHash1 += MOD1;
                
                windowHash2 = (windowHash2 - text.charAt(i) * multiplier2) % MOD2;
                windowHash2 = (windowHash2 * BASE + text.charAt(i + m)) % MOD2;
                if (windowHash2 < 0) windowHash2 += MOD2;
            }
        }
        
        return occurrences;
    }
    
    // Поиск с использованием массива хешей (быстрее для множественных запросов)
    public static class RollingHash {
        private long[] prefixHash;
        private long[] powers;
        private int base;
        private int mod;
        
        public RollingHash(String text, int base, int mod) {
            this.base = base;
            this.mod = mod;
            int n = text.length();
            prefixHash = new long[n + 1];
            powers = new long[n + 1];
            
            powers[0] = 1;
            for (int i = 0; i < n; i++) {
                prefixHash[i + 1] = (prefixHash[i] * base + text.charAt(i)) % mod;
                powers[i + 1] = (powers[i] * base) % mod;
            }
        }
        
        public long getHash(int left, int right) {
            long hash = prefixHash[right] - prefixHash[left] * powers[right - left] % mod;
            if (hash < 0) hash += mod;
            return hash;
        }
        
        public int getMod() {
            return mod;
        }
    }
    
    public static List<Integer> searchWithPrecomputed(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        int m = pattern.length();
        int n = text.length();
        
        if (m > n) return occurrences;
        
        RollingHash rh1 = new RollingHash(text, 256, 1_000_000_007);
        RollingHash rh2 = new RollingHash(text, 257, 1_000_000_009);
        
        RollingHash rhPattern1 = new RollingHash(pattern, 256, 1_000_000_007);
        RollingHash rhPattern2 = new RollingHash(pattern, 257, 1_000_000_009);
        
        long patternHash1 = rhPattern1.getHash(0, m);
        long patternHash2 = rhPattern2.getHash(0, m);
        
        for (int i = 0; i <= n - m; i++) {
            long textHash1 = rh1.getHash(i, i + m);
            long textHash2 = rh2.getHash(i, i + m);
            
            if (textHash1 == patternHash1 && textHash2 == patternHash2) {
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
    
    // Поиск множества образцов одновременно
    public static Map<String, List<Integer>> searchMultiple(String text, List<String> patterns) {
        Map<String, List<Integer>> results = new HashMap<>();
        
        for (String pattern : patterns) {
            results.put(pattern, new ArrayList<>());
        }
        
        int maxLen = 0;
        for (String p : patterns) {
            maxLen = Math.max(maxLen, p.length());
        }
        
        int n = text.length();
        
        // Для каждого возможного окна
        for (int i = 0; i <= n - 1; i++) {
            for (String pattern : patterns) {
                int m = pattern.length();
                if (i + m <= n) {
                    // Быстрая проверка по первому символу
                    if (text.charAt(i) == pattern.charAt(0)) {
                        boolean match = true;
                        for (int j = 1; j < m; j++) {
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
    
    // Поиск с выводом статистики коллизий
    public static SearchStats searchWithStats(String text, String pattern) {
        int collisions = 0;
        int comparisons = 0;
        
        int n = text.length();
        int m = pattern.length();
        
        if (m > n) return new SearchStats(new ArrayList<>(), collisions, comparisons);
        
        long patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
        }
        
        long multiplier = 1;
        for (int i = 0; i < m - 1; i++) {
            multiplier = (multiplier * BASE) % MOD;
        }
        
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }
        
        List<Integer> occurrences = new ArrayList<>();
        
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                } else {
                    collisions++;
                }
            }
            
            if (i < n - m) {
                windowHash = (windowHash - text.charAt(i) * multiplier) % MOD;
                windowHash = (windowHash * BASE + text.charAt(i + m)) % MOD;
                if (windowHash < 0) windowHash += MOD;
            }
        }
        
        return new SearchStats(occurrences, collisions, comparisons);
    }
    
    static class SearchStats {
        List<Integer> occurrences;
        int collisions;
        int comparisons;
        
        SearchStats(List<Integer> occurrences, int collisions, int comparisons) {
            this.occurrences = occurrences;
            this.collisions = collisions;
            this.comparisons = comparisons;
        }
        
        @Override
        public String toString() {
            return String.format("Найдено %d вхождений, %d коллизий, %d сравнений",
                occurrences.size(), collisions, comparisons);
        }
    }
}