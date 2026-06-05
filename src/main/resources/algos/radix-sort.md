# Поразрядная сортировка (Radix Sort)

## Intuition
Поразрядная сортировка — это алгоритм, который сортирует числа, обрабатывая их по разрядам (единицы, десятки, сотни и т.д.). Вместо сравнения элементов целиком, алгоритм многократно применяет устойчивую сортировку (обычно сортировку подсчетом) для каждого разряда. Представьте, что вы сортируете стопку карточек с числами: сначала вы раскладываете их по последней цифре, затем внутри каждой группы — по предпоследней, и так далее. После обработки всех разрядов карточки оказываются полностью отсортированными.

## Approach
1. Находим максимальное число, чтобы определить количество разрядов
2. Для каждого разряда (единицы, десятки, сотни...):
    - Применяем устойчивую сортировку к массиву по текущему разряду
    - Обычно используется сортировка подсчетом (Counting Sort)
3. После обработки всех разрядов массив становится полностью отсортированным
4. Алгоритм может сортировать от младшего разряда к старшему (LSD) или от старшего к младшему (MSD)

## Complexity
- Time complexity: **O(d * (n + b))**, где d — количество разрядов, b — основание системы счисления
- Time complexity (LSD): **O(n * k)**, где k — максимальное количество разрядов
- Space complexity: **O(n + b)**
- Стабильная сортировка (при использовании стабильной промежуточной сортировки)

## Code

```java
public class RadixSort {
    
    // Поразрядная сортировка (LSD - от младшего разряда к старшему)
    public static void sort(int[] arr) {
        if (arr.length == 0) return;
        
        // Находим максимальное число для определения количества разрядов
        int max = arr[0];
        for (int num : arr) {
            max = Math.max(max, num);
        }
        
        // Сортируем по каждому разряду
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }
    
    // Сортировка подсчетом по указанному разряду
    private static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10]; // цифры от 0 до 9
        
        // Подсчитываем количество цифр в текущем разряде
        for (int i = 0; i < n; i++) {
            int digit = (arr[i] / exp) % 10;
            count[digit]++;
        }
        
        // Преобразуем в префиксные суммы
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        // Строим выходной массив (идем справа налево для стабильности)
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }
        
        // Копируем обратно в исходный массив
        System.arraycopy(output, 0, arr, 0, n);
    }
    
    // Поразрядная сортировка для отрицательных чисел
    public static void sortWithNegatives(int[] arr) {
        if (arr.length == 0) return;
        
        // Разделяем отрицательные и положительные числа
        int[] negatives = new int[arr.length];
        int[] positives = new int[arr.length];
        int negCount = 0;
        int posCount = 0;
        
        for (int num : arr) {
            if (num < 0) {
                negatives[negCount++] = -num; // сохраняем как положительные
            } else {
                positives[posCount++] = num;
            }
        }
        
        // Сортируем обе части
        sort(negatives, 0, negCount);
        sort(positives, 0, posCount);
        
        // Собираем результат (отрицательные в обратном порядке)
        int index = 0;
        for (int i = negCount - 1; i >= 0; i--) {
            arr[index++] = -negatives[i];
        }
        for (int i = 0; i < posCount; i++) {
            arr[index++] = positives[i];
        }
    }
    
    // Вспомогательный метод для сортировки части массива
    private static void sort(int[] arr, int start, int length) {
        if (length <= 1) return;
        
        int max = arr[start];
        for (int i = start; i < start + length; i++) {
            max = Math.max(max, arr[i]);
        }
        
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp, start, length);
        }
    }
    
    private static void countingSortByDigit(int[] arr, int exp, int start, int length) {
        int[] output = new int[length];
        int[] count = new int[10];
        
        for (int i = 0; i < length; i++) {
            int digit = (arr[start + i] / exp) % 10;
            count[digit]++;
        }
        
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        for (int i = length - 1; i >= 0; i--) {
            int digit = (arr[start + i] / exp) % 10;
            output[count[digit] - 1] = arr[start + i];
            count[digit]--;
        }
        
        System.arraycopy(output, 0, arr, start, length);
    }
    
    // Поразрядная сортировка для массива строк
    public static void sortStrings(String[] arr) {
        if (arr.length == 0) return;
        
        // Находим максимальную длину строки
        int maxLen = 0;
        for (String s : arr) {
            maxLen = Math.max(maxLen, s.length());
        }
        
        // Сортируем по каждому символу (от последнего к первому)
        for (int pos = maxLen - 1; pos >= 0; pos--) {
            countingSortByChar(arr, pos);
        }
    }
    
    private static void countingSortByChar(String[] arr, int position) {
        int n = arr.length;
        String[] output = new String[n];
        int[] count = new int[256]; // ASCII символы
        
        // Подсчитываем символы на указанной позиции
        for (int i = 0; i < n; i++) {
            int ch = position < arr[i].length() ? arr[i].charAt(position) : 0;
            count[ch]++;
        }
        
        // Префиксные суммы
        for (int i = 1; i < 256; i++) {
            count[i] += count[i - 1];
        }
        
        // Строим выходной массив
        for (int i = n - 1; i >= 0; i--) {
            int ch = position < arr[i].length() ? arr[i].charAt(position) : 0;
            output[count[ch] - 1] = arr[i];
            count[ch]--;
        }
        
        System.arraycopy(output, 0, arr, 0, n);
    }
}