# Суффиксный массив (Suffix Array)

## Intuition
Суффиксный массив — это массив всех суффиксов строки, отсортированных в лексикографическом порядке. Вместо хранения самих суффиксов (что потребовало бы O(n²) памяти), массив хранит начальные индексы суффиксов. Это мощная структура данных для решения множества строковых задач: поиск подстроки, нахождение наибольшей общей подстроки, поиск повторяющихся паттернов и многое другое. Представьте, что вы выписали все окончания словаря в столбик и отсортировали их — суффиксный массив дает быстрый доступ к любой подстроке через бинарный поиск.

## Approach
1. **Создание суффиксов**: для строки длины n создаем n суффиксов (индексы от 0 до n-1)
2. **Сортировка**: сортируем суффиксы лексикографически
3. **Построение LCP массива** (Longest Common Prefix):
    - LCP[i] = длина наибольшего общего префикса между суффиксами в позициях i и i-1 в суффиксном массиве
    - Используется алгоритм Касаи (Kasai) для построения за O(n)
4. **Поиск подстроки**: бинарный поиск в суффиксном массиве

## Complexity
- Time complexity (построение суффиксного массива): **O(n log n)** или **O(n)** для алгоритма DC3
- Time complexity (построение LCP): **O(n)**
- Time complexity (поиск подстроки): **O(m log n)**, где m — длина образца
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class SuffixArray {
    
    private String text;
    private int n;
    private Integer[] suffixArray;
    private int[] rank;
    private int[] lcp;
    
    public SuffixArray(String text) {
        this.text = text;
        this.n = text.length();
        this.suffixArray = new Integer[n];
        this.rank = new int[n];
        
        buildSuffixArray();
        buildLCP();
    }
    
    // Построение суффиксного массива за O(n log n) с использованием сортировки
    private void buildSuffixArray() {
        for (int i = 0; i < n; i++) {
            suffixArray[i] = i;
        }
        
        // Начальная сортировка по первому символу
        Arrays.sort(suffixArray, (a, b) -> 
            Character.compare(text.charAt(a), text.charAt(b)));
        
        // Назначаем начальные ранги
        rank[suffixArray[0]] = 0;
        for (int i = 1; i < n; i++) {
            rank[suffixArray[i]] = rank[suffixArray[i - 1]];
            if (text.charAt(suffixArray[i]) != text.charAt(suffixArray[i - 1])) {
                rank[suffixArray[i]]++;
            }
        }
        
        // k-сортировка
        int k = 1;
        int[] tempRank = new int[n];
        Integer[] tempSA = new Integer[n];
        
        while (k < n) {
            // Сортировка по (rank[i], rank[i + k])
            final int kFinal = k;
            Arrays.sort(suffixArray, (a, b) -> {
                if (rank[a] != rank[b]) return rank[a] - rank[b];
                int rankA = a + kFinal < n ? rank[a + kFinal] : -1;
                int rankB = b + kFinal < n ? rank[b + kFinal] : -1;
                return rankA - rankB;
            });
            
            // Пересчет рангов
            tempRank[suffixArray[0]] = 0;
            for (int i = 1; i < n; i++) {
                tempRank[suffixArray[i]] = tempRank[suffixArray[i - 1]];
                int prevA = suffixArray[i - 1];
                int prevB = suffixArray[i - 1] + kFinal < n ? rank[suffixArray[i - 1] + kFinal] : -1;
                int currA = suffixArray[i];
                int currB = suffixArray[i] + kFinal < n ? rank[suffixArray[i] + kFinal] : -1;
                
                if (rank[currA] != rank[prevA] || prevB != currB) {
                    tempRank[suffixArray[i]]++;
                }
            }
            
            System.arraycopy(tempRank, 0, rank, 0, n);
            k *= 2;
        }
    }
    
    // Построение LCP массива (алгоритм Касаи)
    private void buildLCP() {
        lcp = new int[n];
        int[] invSuffix = new int[n];
        
        for (int i = 0; i < n; i++) {
            invSuffix[suffixArray[i]] = i;
        }
        
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (invSuffix[i] == n - 1) {
                k = 0;
                continue;
            }
            
            int j = suffixArray[invSuffix[i] + 1];
            
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }
            
            lcp[invSuffix[i]] = k;
            
            if (k > 0) k--;
        }
    }
    
    // Поиск подстроки в тексте
    public List<Integer> search(String pattern) {
        List<Integer> result = new ArrayList<>();
        int m = pattern.length();
        
        // Бинарный поиск
        int left = 0;
        int right = n - 1;
        
        // Находим левую границу (первое вхождение)
        int first = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int suffixIndex = suffixArray[mid];
            
            String suffix = text.substring(suffixIndex, 
                Math.min(suffixIndex + m, n));
            int cmp = pattern.compareTo(suffix);
            
            if (cmp <= 0) {
                if (cmp == 0) first = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        if (first == -1) return result;
        
        // Находим правую границу (последнее вхождение)
        left = first;
        right = n - 1;
        int last = first;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int suffixIndex = suffixArray[mid];
            
            String suffix = text.substring(suffixIndex,
                Math.min(suffixIndex + m, n));
            
            if (suffix.startsWith(pattern)) {
                last = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        // Собираем все вхождения
        for (int i = first; i <= last; i++) {
            result.add(suffixArray[i]);
        }
        
        return result;
    }
    
    // Нахождение наибольшей повторяющейся подстроки
    public String longestRepeatedSubstring() {
        int maxLen = 0;
        int index = -1;
        
        for (int i = 1; i < n; i++) {
            if (lcp[i - 1] > maxLen) {
                maxLen = lcp[i - 1];
                index = suffixArray[i];
            }
        }
        
        if (index == -1) return "";
        return text.substring(index, index + maxLen);
    }
    
    // Нахождение наибольшей общей подстроки двух строк
    public static String longestCommonSubstring(String s1, String s2) {
        String combined = s1 + "#" + s2 + "$";
        SuffixArray sa = new SuffixArray(combined);
        
        int maxLen = 0;
        int maxIndex = -1;
        int len1 = s1.length();
        
        for (int i = 1; i < sa.n; i++) {
            int idx1 = sa.suffixArray[i - 1];
            int idx2 = sa.suffixArray[i];
            
            // Проверяем, что суффиксы из разных строк
            if ((idx1 < len1 && idx2 > len1) || (idx2 < len1 && idx1 > len1)) {
                if (sa.lcp[i - 1] > maxLen) {
                    maxLen = sa.lcp[i - 1];
                    maxIndex = Math.min(idx1, idx2);
                }
            }
        }
        
        if (maxIndex == -1) return "";
        return combined.substring(maxIndex, maxIndex + maxLen);
    }
    
    // Подсчет количества уникальных подстрок
    public int countDistinctSubstrings() {
        int total = n * (n + 1) / 2; // всего подстрок
        int sumLCP = 0;
        
        for (int i = 0; i < n - 1; i++) {
            sumLCP += lcp[i];
        }
        
        return total - sumLCP;
    }
    
    // Получение k-го наименьшего суффикса
    public int getKthSuffix(int k) {
        if (k < 0 || k >= n) return -1;
        return suffixArray[k];
    }
    
    // Сравнение рангов двух суффиксов
    public int compareSuffixes(int i, int j) {
        if (rank[i] != rank[j]) return rank[i] - rank[j];
        int len1 = n - i;
        int len2 = n - j;
        return Integer.compare(len1, len2);
    }
    
    // Печать суффиксного массива
    public void printSuffixArray() {
        System.out.println("Suffix Array для \"" + text + "\":");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d: %2d - %s%n", 
                i, suffixArray[i], text.substring(suffixArray[i]));
        }
    }
    
    // Печать LCP массива
    public void printLCP() {
        System.out.println("LCP Array:");
        for (int i = 0; i < n - 1; i++) {
            System.out.print(lcp[i] + " ");
        }
        System.out.println();
    }
    
    // Простая версия суффиксного массива (понятнее, но медленнее)
    public static class SimpleSuffixArray {
        private String text;
        private Integer[] suffixArray;
        
        public SimpleSuffixArray(String text) {
            this.text = text;
            int n = text.length();
            suffixArray = new Integer[n];
            for (int i = 0; i < n; i++) {
                suffixArray[i] = i;
            }
            
            Arrays.sort(suffixArray, (a, b) -> 
                text.substring(a).compareTo(text.substring(b)));
        }
        
        public List<Integer> search(String pattern) {
            List<Integer> result = new ArrayList<>();
            int left = 0;
            int right = suffixArray.length - 1;
            
            while (left <= right) {
                int mid = (left + right) / 2;
                String suffix = text.substring(suffixArray[mid]);
                int cmp = pattern.compareTo(suffix);
                
                if (cmp == 0) {
                    result.add(suffixArray[mid]);
                    // Поиск соседних
                    int temp = mid - 1;
                    while (temp >= 0 && text.substring(suffixArray[temp])
                        .startsWith(pattern)) {
                        result.add(suffixArray[temp]);
                        temp--;
                    }
                    temp = mid + 1;
                    while (temp < suffixArray.length && text.substring(suffixArray[temp])
                        .startsWith(pattern)) {
                        result.add(suffixArray[temp]);
                        temp++;
                    }
                    break;
                } else if (cmp < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            Collections.sort(result);
            return result;
        }
    }
}