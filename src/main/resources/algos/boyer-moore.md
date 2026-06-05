# Алгоритм Бойера-Мура (Boyer-Moore Algorithm)

## Intuition
Алгоритм Бойера-Мура — это один из наиболее эффективных алгоритмов поиска подстроки, особенно для больших алфавитов и длинных образцов. В отличие от KMP, который сравнивает символы слева направо, Бойер-Мур сравнивает справа налево. При несовпадении он использует две эвристики, чтобы сдвинуть образец максимально далеко: эвристику стоп-символа (bad character) и эвристику хорошего суффикса (good suffix). Представьте, что вы ищете слово в книге: если последняя буква не совпадает, вы знаете, на сколько можно безопасно сдвинуть слово, глядя на несовпавший символ в тексте.

## Approach
1. **Препроцессинг эвристики стоп-символа**:
    - Для каждого символа запоминаем его самое правое вхождение в образце
    - При несовпадении сдвигаем образец, чтобы этот символ совпал с таким же в образце
2. **Препроцессинг эвристики хорошего суффикса**:
    - При несовпадении после частичного совпадения, используем информацию о совпавшем суффиксе
    - Вычисляем, на сколько можно сдвинуть образец, чтобы совпавший суффикс снова совпал
3. **Поиск**:
    - Сравниваем символы справа налево
    - При несовпадении используем максимальный сдвиг из двух эвристик
    - При полном совпадении — нашли вхождение

## Complexity
- Time complexity (лучшая): **O(n/m)**
- Time complexity (средняя): **O(n + m)**
- Time complexity (худшая): **O(n × m)** с плохими эвристиками, но обычно O(n + m)
- Space complexity: **O(m + |алфавит|)**

## Code

```java
import java.util.*;

public class BoyerMoore {
    
    // Класс для результата поиска
    static class SearchResult {
        int index;
        int comparisons;
        
        SearchResult(int index, int comparisons) {
            this.index = index;
            this.comparisons = comparisons;
        }
    }
    
    // Построение таблицы стоп-символов (bad character)
    private static int[] buildBadCharTable(String pattern) {
        int m = pattern.length();
        int[] badChar = new int[256]; // ASCII таблица
        
        Arrays.fill(badChar, -1);
        
        for (int i = 0; i < m; i++) {
            badChar[pattern.charAt(i)] = i;
        }
        
        return badChar;
    }
    
    // Построение таблицы хорошего суффикса (good suffix)
    private static int[] buildGoodSuffixTable(String pattern) {
        int m = pattern.length();
        int[] suffix = new int[m];
        int[] goodSuffix = new int[m + 1];
        
        // Вычисляем суффиксные границы
        Arrays.fill(suffix, -1);
        int j = 0;
        
        for (int i = 0; i < m - 1; i++) {
            j = i;
            int k = 0;
            while (j >= 0 && pattern.charAt(j) == pattern.charAt(m - 1 - k)) {
                k++;
                j--;
            }
            suffix[i] = j;
        }
        
        // Инициализация goodSuffix
        for (int i = 0; i <= m; i++) {
            goodSuffix[i] = m;
        }
        
        int last = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (suffix[i] != -1) {
                for (int k = last; k <= i - suffix[i]; k++) {
                    goodSuffix[k] = i;
                }
                last = i + 1;
            }
        }
        
        // Случай, когда суффикс совпадает с префиксом
        for (int i = 0; i <= m; i++) {
            if (goodSuffix[i] == m) {
                for (int k = 0; k < i; k++) {
                    if (m - 1 - k >= 0 && goodSuffix[k] > m - 1 - k) {
                        goodSuffix[k] = m - 1 - k;
                    }
                }
            }
        }
        
        return goodSuffix;
    }
    
    // Основной алгоритм поиска
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern.isEmpty() || pattern.length() > text.length()) {
            return occurrences;
        }
        
        int m = pattern.length();
        int n = text.length();
        
        int[] badChar = buildBadCharTable(pattern);
        int[] goodSuffix = buildGoodSuffixTable(pattern);
        
        int shift = 0;
        
        while (shift <= n - m) {
            int j = m - 1;
            
            // Сравнение справа налево
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            
            if (j < 0) {
                // Нашли вхождение
                occurrences.add(shift);
                shift += goodSuffix[0];
            } else {
                // Вычисляем сдвиг по стоп-символу
                int badCharShift = j - badChar[text.charAt(shift + j)];
                if (badCharShift < 1) badCharShift = 1;
                
                // Вычисляем сдвиг по хорошему суффиксу
                int goodSuffixShift = goodSuffix[j + 1];
                
                // Берем максимальный сдвиг
                shift += Math.max(badCharShift, goodSuffixShift);
            }
        }
        
        return occurrences;
    }
    
    // Оптимизированная версия только с эвристикой стоп-символа (проще, но медленнее)
    public static List<Integer> searchWithBadCharOnly(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern.isEmpty()) return occurrences;
        
        int m = pattern.length();
        int n = text.length();
        int[] badChar = buildBadCharTable(pattern);
        
        int shift = 0;
        
        while (shift <= n - m) {
            int j = m - 1;
            
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            
            if (j < 0) {
                occurrences.add(shift);
                shift++;
            } else {
                shift += Math.max(1, j - badChar[text.charAt(shift + j)]);
            }
        }
        
        return occurrences;
    }
    
    // Поиск всех совпадений с подсчетом количества сравнений
    public static SearchResult searchWithCount(String text, String pattern) {
        int comparisons = 0;
        
        if (pattern.isEmpty() || pattern.length() > text.length()) {
            return new SearchResult(-1, comparisons);
        }
        
        int m = pattern.length();
        int n = text.length();
        
        int[] badChar = buildBadCharTable(pattern);
        int[] goodSuffix = buildGoodSuffixTable(pattern);
        
        int shift = 0;
        
        while (shift <= n - m) {
            int j = m - 1;
            
            while (j >= 0) {
                comparisons++;
                if (pattern.charAt(j) != text.charAt(shift + j)) {
                    break;
                }
                j--;
            }
            
            if (j < 0) {
                return new SearchResult(shift, comparisons);
            } else {
                int badCharShift = j - badChar[text.charAt(shift + j)];
                if (badCharShift < 1) badCharShift = 1;
                int goodSuffixShift = goodSuffix[j + 1];
                shift += Math.max(badCharShift, goodSuffixShift);
            }
        }
        
        return new SearchResult(-1, comparisons);
    }
    
    // Поиск в тексте с использованием только эвристики стоп-символа (простой вариант)
    public static int simpleSearch(String text, String pattern) {
        int m = pattern.length();
        int n = text.length();
        
        if (m == 0) return 0;
        if (m > n) return -1;
        
        int[] last = new int[256];
        Arrays.fill(last, -1);
        
        for (int i = 0; i < m; i++) {
            last[pattern.charAt(i)] = i;
        }
        
        int shift = 0;
        
        while (shift <= n - m) {
            int j = m - 1;
            
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            
            if (j < 0) {
                return shift;
            }
            
            shift += Math.max(1, j - last[text.charAt(shift + j)]);
        }
        
        return -1;
    }
}