# Introsort (Introspective Sort)

## Intuition
Introsort — это гибридный алгоритм сортировки, который объединяет лучшие стороны быстрой сортировки, пирамидальной сортировки и сортировки вставками. Он начинается как быстрая сортировка, но если рекурсия становится слишком глубокой (что указывает на риск квадратичного времени), переключается на пирамидальную сортировку, которая гарантирует O(n log n). Для маленьких подмассивов используется сортировка вставками. Это стандартный алгоритм сортировки в многих реализациях стандартных библиотек (C++ std::sort, Java Arrays.sort для примитивов).

## Approach
1. Задаем максимальную глубину рекурсии: `2 * log2(n)`
2. Запускаем рекурсивную сортировку:
    - Если размер подмассива меньше порога (обычно 16), используем сортировку вставками
    - Если глубина рекурсии достигла предела, используем пирамидальную сортировку
    - Иначе выполняем быструю сортировку (выбираем опорный элемент, разбиваем, рекурсивно сортируем части)
3. Этот подход гарантирует O(n log n) в худшем случае и сохраняет скорость быстрой сортировки в среднем

## Complexity
- Time complexity (лучшая): **O(n log n)**
- Time complexity (средняя): **O(n log n)**
- Time complexity (худшая): **O(n log n)** (в отличие от быстрой сортировки)
- Space complexity: **O(log n)** для рекурсии

## Code

```java
public class Introsort {
    private static final int INSERTION_SORT_THRESHOLD = 16;
    
    // Основной метод сортировки
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        int maxDepth = (int) (2 * Math.log(arr.length) / Math.log(2));
        introsort(arr, 0, arr.length - 1, maxDepth);
    }
    
    // Рекурсивная интроспективная сортировка
    private static void introsort(int[] arr, int left, int right, int maxDepth) {
        int size = right - left + 1;
        
        // Для маленьких подмассивов используем сортировку вставками
        if (size <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, left, right);
            return;
        }
        
        // Если глубина рекурсии превышена, используем пирамидальную сортировку
        if (maxDepth == 0) {
            heapSort(arr, left, right);
            return;
        }
        
        // Иначе - быстрая сортировка
        int pivotIndex = partition(arr, left, right);
        
        // Рекурсивно сортируем левую и правую части
        introsort(arr, left, pivotIndex - 1, maxDepth - 1);
        introsort(arr, pivotIndex + 1, right, maxDepth - 1);
    }
    
    // Разбиение для быстрой сортировки (медиана из трех)
    private static int partition(int[] arr, int left, int right) {
        // Выбираем медиану из трех в качестве опорного элемента
        int mid = left + (right - left) / 2;
        medianOfThree(arr, left, mid, right);
        
        int pivot = arr[mid];
        swap(arr, mid, right); // перемещаем опорный в конец
        
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        
        swap(arr, i, right); // ставим опорный на место
        return i;
    }
    
    // Медиана из трех элементов
    private static void medianOfThree(int[] arr, int a, int b, int c) {
        if (arr[a] > arr[b]) swap(arr, a, b);
        if (arr[b] > arr[c]) swap(arr, b, c);
        if (arr[a] > arr[b]) swap(arr, a, b);
    }
    
    // Пирамидальная сортировка для подмассива
    private static void heapSort(int[] arr, int left, int right) {
        int size = right - left + 1;
        
        // Построение кучи
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(arr, left, i, size);
        }
        
        // Извлечение элементов из кучи
        for (int i = size - 1; i > 0; i--) {
            swap(arr, left, left + i);
            heapify(arr, left, 0, i);
        }
    }
    
    // Восстановление свойства кучи
    private static void heapify(int[] arr, int offset, int index, int size) {
        int largest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        
        if (leftChild < size && arr[offset + leftChild] > arr[offset + largest]) {
            largest = leftChild;
        }
        if (rightChild < size && arr[offset + rightChild] > arr[offset + largest]) {
            largest = rightChild;
        }
        
        if (largest != index) {
            swap(arr, offset + index, offset + largest);
            heapify(arr, offset, largest, size);
        }
    }
    
    // Сортировка вставками для маленьких подмассивов
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            arr[j + 1] = key;
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // Перегруженный метод для сравнимых объектов
    public static <T extends Comparable<T>> void sort(T[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        int maxDepth = (int) (2 * Math.log(arr.length) / Math.log(2));
        introsort(arr, 0, arr.length - 1, maxDepth);
    }
    
    private static <T extends Comparable<T>> void introsort(T[] arr, int left, int right, int maxDepth) {
        int size = right - left + 1;
        
        if (size <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, left, right);
            return;
        }
        
        if (maxDepth == 0) {
            heapSort(arr, left, right);
            return;
        }
        
        int pivotIndex = partition(arr, left, right);
        introsort(arr, left, pivotIndex - 1, maxDepth - 1);
        introsort(arr, pivotIndex + 1, right, maxDepth - 1);
    }
    
    private static <T extends Comparable<T>> int partition(T[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        medianOfThree(arr, left, mid, right);
        
        T pivot = arr[mid];
        swap(arr, mid, right);
        
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j].compareTo(pivot) < 0) {
                swap(arr, i, j);
                i++;
            }
        }
        
        swap(arr, i, right);
        return i;
    }
    
    private static <T extends Comparable<T>> void medianOfThree(T[] arr, int a, int b, int c) {
        if (arr[a].compareTo(arr[b]) > 0) swap(arr, a, b);
        if (arr[b].compareTo(arr[c]) > 0) swap(arr, b, c);
        if (arr[a].compareTo(arr[b]) > 0) swap(arr, a, b);
    }
    
    private static <T extends Comparable<T>> void heapSort(T[] arr, int left, int right) {
        int size = right - left + 1;
        
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(arr, left, i, size);
        }
        
        for (int i = size - 1; i > 0; i--) {
            swap(arr, left, left + i);
            heapify(arr, left, 0, i);
        }
    }
    
    private static <T extends Comparable<T>> void heapify(T[] arr, int offset, int index, int size) {
        int largest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        
        if (leftChild < size && arr[offset + leftChild].compareTo(arr[offset + largest]) > 0) {
            largest = leftChild;
        }
        if (rightChild < size && arr[offset + rightChild].compareTo(arr[offset + largest]) > 0) {
            largest = rightChild;
        }
        
        if (largest != index) {
            swap(arr, offset + index, offset + largest);
            heapify(arr, offset, largest, size);
        }
    }
    
    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private static <T extends Comparable<T>> void insertionSort(T[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            T key = arr[i];
            int j = i - 1;
            
            while (j >= left && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            arr[j + 1] = key;
        }
    }
}