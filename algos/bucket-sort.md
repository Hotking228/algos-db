# Блочная сортировка (Bucket Sort)

## Intuition
Блочная сортировка (Bucket Sort) распределяет элементы по нескольким "корзинам" (bucket), а затем сортирует каждую корзину отдельно. Представьте, что у вас есть мешок с монетами разного достоинства, и вы раскладываете их по разным карманам: 1 рубль в один карман, 2 рубля в другой, 5 рублей в третий. Затем вы сортируете монеты внутри каждого кармана (что быстро, так как монет мало) и просто вынимаете их по порядку. Идея в том, чтобы равномерно распределить элементы, чтобы в каждой корзине было мало элементов.

## Approach
1. Определяем количество корзин (обычно sqrt(n) или n)
2. Вычисляем диапазон значений для каждой корзины
3. Распределяем элементы по корзинам
4. Сортируем каждую корзину (обычно сортировкой вставками или быстрой)
5. Собираем элементы из корзин в исходный массив по порядку

## Complexity
- Time complexity (средняя): **O(n + k)**, где k — количество корзин
- Time complexity (худшая): **O(n²)**, когда все элементы попадают в одну корзину
- Space complexity: **O(n + k)**
- Лучше всего работает для равномерно распределенных данных

## Code

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort {
    
    // Блочная сортировка для чисел с плавающей точкой в диапазоне [0, 1)
    public static void sort(float[] arr) {
        if (arr.length == 0) return;
        
        int n = arr.length;
        
        // Создаем n корзин
        @SuppressWarnings("unchecked")
        List<Float>[] buckets = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Распределяем элементы по корзинам
        for (int i = 0; i < n; i++) {
            int bucketIndex = (int) (arr[i] * n);
            buckets[bucketIndex].add(arr[i]);
        }
        
        // Сортируем каждую корзину и собираем результат
        int index = 0;
        for (int i = 0; i < n; i++) {
            Collections.sort(buckets[i]); // сортировка вставками по сути
            for (float num : buckets[i]) {
                arr[index++] = num;
            }
        }
    }
    
    // Блочная сортировка для целых чисел
    public static void sort(int[] arr) {
        if (arr.length == 0) return;
        
        int n = arr.length;
        
        // Находим min и max
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        
        // Определяем количество корзин (обычно n)
        int bucketCount = n;
        int range = max - min + 1;
        
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Распределяем элементы по корзинам
        for (int num : arr) {
            // Нормализуем значение и определяем корзину
            int bucketIndex = (int) ((double) (num - min) / range * bucketCount);
            bucketIndex = Math.min(bucketIndex, bucketCount - 1);
            buckets[bucketIndex].add(num);
        }
        
        // Сортируем каждую корзину и собираем результат
        int index = 0;
        for (int i = 0; i < bucketCount; i++) {
            Collections.sort(buckets[i]); // или сортировка вставками
            for (int num : buckets[i]) {
                arr[index++] = num;
            }
        }
    }
    
    // Блочная сортировка с пользовательской хеш-функцией
    public static void sortWithHash(int[] arr, int bucketCount, java.util.function.Function<Integer, Integer> hashFunction) {
        if (arr.length == 0) return;
        
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Распределяем по корзинам с помощью хеш-функции
        for (int num : arr) {
            int bucketIndex = hashFunction.apply(num) % bucketCount;
            bucketIndex = Math.abs(bucketIndex);
            buckets[bucketIndex].add(num);
        }
        
        // Сортируем каждую корзину
        int index = 0;
        for (int i = 0; i < bucketCount; i++) {
            insertionSort(buckets[i]); // свой метод для списка
            for (int num : buckets[i]) {
                arr[index++] = num;
            }
        }
    }
    
    // Сортировка вставками для списка
    private static void insertionSort(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            int key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j) > key) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
    
    // Блочная сортировка для массива строк (по длине)
    public static void sortByLength(String[] arr) {
        if (arr.length == 0) return;
        
        // Находим максимальную длину строки
        int maxLen = 0;
        for (String s : arr) {
            maxLen = Math.max(maxLen, s.length());
        }
        
        // Корзины по длине строк (от 0 до maxLen)
        @SuppressWarnings("unchecked")
        List<String>[] buckets = new ArrayList[maxLen + 1];
        for (int i = 0; i <= maxLen; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Распределяем строки по корзинам по их длине
        for (String s : arr) {
            buckets[s.length()].add(s);
        }
        
        // Сортируем каждую корзину (обычной сортировкой)
        int index = 0;
        for (int i = 0; i <= maxLen; i++) {
            Collections.sort(buckets[i]); // лексикографическая сортировка внутри корзины
            for (String s : buckets[i]) {
                arr[index++] = s;
            }
        }
    }
    
    // Оптимизированная блочная сортировка с пользовательской сортировкой корзин
    public static void sortOptimized(int[] arr) {
        if (arr.length < 2) return;
        
        int n = arr.length;
        int min = arr[0];
        int max = arr[0];
        
        for (int num : arr) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        
        // Если диапазон маленький, используем сортировку подсчетом
        if (max - min < n) {
            countingSort(arr, min, max);
            return;
        }
        
        int bucketCount = (int) Math.sqrt(n) + 1;
        int range = max - min;
        
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Распределение с равномерным интервалом
        for (int num : arr) {
            int bucketIndex = (int) ((double) (num - min) / range * (bucketCount - 1));
            buckets[bucketIndex].add(num);
        }
        
        int index = 0;
        for (int i = 0; i < bucketCount; i++) {
            if (buckets[i].size() > 0) {
                // Если в корзине мало элементов, используем вставки, иначе рекурсивно
                if (buckets[i].size() < 20) {
                    insertionSort(buckets[i]);
                } else {
                    int[] bucketArr = buckets[i].stream().mapToInt(Integer::intValue).toArray();
                    sortOptimized(bucketArr); // рекурсивная блочная сортировка
                    for (int val : bucketArr) {
                        arr[index++] = val;
                    }
                    continue;
                }
                for (int val : buckets[i]) {
                    arr[index++] = val;
                }
            }
        }
    }
    
    private static void countingSort(int[] arr, int min, int max) {
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];
        
        for (int num : arr) {
            count[num - min]++;
        }
        
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }
        
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }
        
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}