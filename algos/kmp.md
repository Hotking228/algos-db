# Алгоритм Кнута-Морриса-Пратта (КМП)

## Intuition
Алгоритм Кнута-Морриса-Пратта (KMP) — это эффективный алгоритм поиска подстроки в строке. В отличие от наивного алгоритма, который при несовпадении откатывается на один символ, KMP использует информацию из предыдущих сравнений, чтобы избежать повторной проверки уже сопоставленных символов. Представьте, что вы ищете слово в тексте и уже увидели его начало. Когда происходит несовпадение, вы знаете, какая часть найденного префикса может совпадать с суффиксом уже проверенной части текста, и сдвигаете образец не до конца, а только на эту величину.

## Approach
1. **Построение префикс-функции (π-функции)** для образца:
    - π[i] — длина наибольшего собственного префикса, который также является суффиксом подстроки pattern[0..i]
    - Это "память" алгоритма, показывающая, куда откатиться при несовпадении
2. **Поиск**:
    - Проходим по тексту, поддерживая длину совпавшего префикса образца
    - При совпадении символов увеличиваем длину
    - При несовпадении уменьшаем длину используя π-функцию
    - Если длина достигла длины образца — нашли вхождение

## Complexity
- Time complexity (префикс-функция): **O(m)**, где m — длина образца
- Time complexity (поиск): **O(n)**, где n — длина текста
- Total: **O(n + m)**
- Space complexity: **O(m)**

## Code

```java
public class KMP {
    
    // Построение префикс-функции
    public static int[] buildPrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        
        int length = 0; // длина предыдущего наибольшего префикса
        
        for (int i = 1; i < m; i++) {
            // Пока не совпадает и length > 0, откатываемся
            while (length > 0 && pattern.charAt(i) != pattern.charAt(length)) {
                length = pi[length - 1];
            }
            
            // Если совпадает, увеличиваем длину
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
            }
            
            pi[i] = length;
        }
        
        return pi;
    }
    
    // Поиск всех вхождений образца в текст
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern.isEmpty()) return occurrences;
        
        int n = text.length();
        int m = pattern.length();
        int[] pi = buildPrefixFunction(pattern);
        
        int j = 0; // длина совпавшего префикса
        
        for (int i = 0; i < n; i++) {
            // Пока не совпадает и j > 0, откатываемся
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            
            // Если совпадает, увеличиваем j
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            
            // Нашли полное совпадение
            if (j == m) {
                occurrences.add(i - m + 1);
                j = pi[j - 1]; // готовимся к следующему поиску
            }
        }
        
        return occurrences;
    }
    
    // Проверка, содержится ли образец в тексте
    public static boolean contains(String text, String pattern) {
        return !search(text, pattern).isEmpty();
    }
    
    // Подсчет количества вхождений (без пересечений)
    public static int countOccurrences(String text, String pattern) {
        List<Integer> matches = search(text, pattern);
        return matches.size();
    }
    
    // Поиск первого вхождения
    public static int firstIndexOf(String text, String pattern) {
        List<Integer> matches = search(text, pattern);
        return matches.isEmpty() ? -1 : matches.get(0);
    }
    
    // Поиск последнего вхождения
    public static int lastIndexOf(String text, String pattern) {
        List<Integer> matches = search(text, pattern);
        return matches.isEmpty() ? -1 : matches.get(matches.size() - 1);
    }
    
    // Оптимизированная версия для char[] (быстрее)
    public static List<Integer> search(char[] text, char[] pattern) {
        List<Integer> occurrences = new ArrayList<>();
        
        if (pattern.length == 0) return occurrences;
        
        int n = text.length;
        int m = pattern.length;
        int[] pi = buildPrefixFunction(pattern);
        
        int j = 0;
        for (int i = 0; i < n; i++) {
            while (j > 0 && text[i] != pattern[j]) {
                j = pi[j - 1];
            }
            if (text[i] == pattern[j]) {
                j++;
            }
            if (j == m) {
                occurrences.add(i - m + 1);
                j = pi[j - 1];
            }
        }
        
        return occurrences;
    }
    
    private static int[] buildPrefixFunction(char[] pattern) {
        int m = pattern.length;
        int[] pi = new int[m];
        int length = 0;
        
        for (int i = 1; i < m; i++) {
            while (length > 0 && pattern[i] != pattern[length]) {
                length = pi[length - 1];
            }
            if (pattern[i] == pattern[length]) {
                length++;
            }
            pi[i] = length;
        }
        
        return pi;
    }
    
    // Поиск с периодом (проверка, является ли образец периодическим)
    public static boolean isPeriodic(String pattern) {
        int[] pi = buildPrefixFunction(pattern);
        int m = pattern.length();
        int period = m - pi[m - 1];
        return m % period == 0 && period < m;
    }
    
    // Получение периода строки
    public static int getPeriod(String pattern) {
        int[] pi = buildPrefixFunction(pattern);
        int m = pattern.length();
        return m - pi[m - 1];
    }
    
    // Проверка, имеет ли строка границу (border)
    public static int getLongestBorder(String pattern) {
        int[] pi = buildPrefixFunction(pattern);
        return pi[pattern.length() - 1];
    }
    
    // Поиск всех границ (префиксов, которые также являются суффиксами)
    public static List<Integer> getAllBorders(String pattern) {
        List<Integer> borders = new ArrayList<>();
        int[] pi = buildPrefixFunction(pattern);
        int border = pi[pattern.length() - 1];
        
        while (border > 0) {
            borders.add(border);
            border = pi[border - 1];
        }
        
        Collections.reverse(borders);
        return borders;
    }
}