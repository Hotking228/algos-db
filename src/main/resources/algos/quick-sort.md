# Быстрая сортировка

## Intuition
Быстрая сортировка основана на принципе "разделяй и властвуй". Основная идея заключается в том, что отсортировать массив гораздо проще, если разбить его на две части относительно опорного элемента (pivot): элементы меньше опорного и элементы больше опорного. Затем рекурсивно применить тот же подход к каждой из частей.

## Approach
1. Выбрать опорный элемент (pivot). В данной реализации используется последний элемент массива
2. Разделить массив на три части: элементы меньше pivot, pivot, элементы больше pivot
3. Рекурсивно применить быструю сортировку к левой и правой частям
4. Базовый случай: если подмассив состоит из одного элемента или пуст, он уже отсортирован

## Complexity
- Time complexity: $$O(n \log n)$$ в среднем случае, $$O(n^2)$$ в худшем случае (когда pivot всегда минимальный или максимальный элемент)

- Space complexity: $$O(\log n)$$ из-за рекурсивных вызовов в стеке

## Code
```java
public class QuickSort {
    public void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Находим индекс опорного элемента
            int pivotIndex = partition(arr, low, high);
            
            // Рекурсивно сортируем левую и правую части
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
    
    private int partition(int[] arr, int low, int high) {
        // Выбираем последний элемент как опорный
        int pivot = arr[high];
        int i = low - 1; // индекс для элемента меньше pivot
        
        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен pivot
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        
        // Ставим pivot на правильное место
        swap(arr, i + 1, high);
        return i + 1; // возвращаем индекс pivot
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```
