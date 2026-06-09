# Сортировка подсчетом (Counting Sort)

## Intuition
Сортировка подсчетом — это алгоритм сортировки, основанный на подсчете количества вхождений каждого элемента. В отличие от сравнений, этот алгоритм использует дополнительное знание о диапазоне входных данных. Представьте, что у вас есть коробка с цветными шариками, и вы знаете, что цвета только красный, синий и зеленый. Вы просто считаете, сколько шариков каждого цвета, а затем выкладываете их в порядке: все красные, затем синие, затем зеленые. Алгоритм работает за линейное время, но требует, чтобы диапазон значений был не слишком большим.

## Approach
1. Находим минимальное и максимальное значения в массиве
2. Создаем вспомогательный массив `count` размера `max - min + 1`
3. Подсчитываем количество вхождений каждого элемента: `count[arr[i] - min]++`
4. Преобразуем массив `count` в префиксные суммы (накопительные частоты)
5. Строим выходной массив, помещая каждый элемент на правильную позицию
6. Копируем отсортированный массив обратно

## Complexity
- Time complexity: **O(n + k)**, где k — диапазон значений (max - min + 1)
- Space complexity: **O(n + k)**
- Стабильная сортировка (при правильной реализации)
- Не сравнивает элементы между собой

## Code

```java
public class CountingSort {
    
    // Базовая сортировка подсчетом для неотрицательных чисел
    public static void sort(int[] arr) {
        if (arr.length == 0) return;
        
        // Находим максимальное значение
        int max = arr[0];
        for (int num : arr) {
            max = Math.max(max, num);
        }
        
        // Массив для подсчета
        int[] count = new int[max + 1];
        
        // Подсчитываем частоту каждого элемента
        for (int num : arr) {
            count[num]++;
        }
        
        // Перестраиваем массив на основе подсчетов
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                arr[index++] = i;
                count[i]--;
            }
        }
    }
    
    // Сортировка подсчетом с поддержкой отрицательных чисел
    public static void sortWithNegatives(int[] arr) {
        if (arr.length == 0) return;
        
        // Находим min и max
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        
        // Размер массива подсчета
        int range = max - min + 1;
        int[] count = new int[range];
        
        // Подсчитываем частоту
        for (int num : arr) {
            count[num - min]++;
        }
        
        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 0; i < range; i++) {
            while (count[i] > 0) {
                arr[index++] = i + min;
                count[i]--;
            }
        }
    }
    
    // Стабильная сортировка подсчетом (сохраняет порядок равных элементов)
    public static int[] stableSort(int[] arr) {
        if (arr.length == 0) return arr;
        
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];
        
        // Подсчет частоты
        for (int num : arr) {
            count[num - min]++;
        }
        
        // Преобразование в префиксные суммы (накопительные частоты)
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }
        
        // Построение выходного массива (проход справа налево для стабильности)
        for (int i = arr.length - 1; i >= 0; i--) {
            int value = arr[i];
            int position = count[value - min] - 1;
            output[position] = value;
            count[value - min]--;
        }
        
        return output;
    }
}