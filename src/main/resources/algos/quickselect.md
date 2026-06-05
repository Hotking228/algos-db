# Алгоритм поиска k-й порядковой статистики (QuickSelect)

## Intuition
QuickSelect — это алгоритм выбора, который находит k-й наименьший элемент в неотсортированном массиве. Он основан на идее быстрой сортировки, но вместо полной сортировки рекурсивно обрабатывает только одну часть массива. Представьте, что вы ищете 5-й по величине элемент в списке чисел. Вы выбираете случайный опорный элемент, разделяете массив на элементы меньше и больше опорного, и понимаете, в какой половине находится искомый элемент. Затем повторяете процесс только в этой половине.

## Approach
1. Выбираем опорный элемент (pivot) — обычно случайный или медиана из трех
2. Разбиваем массив так, чтобы все элементы меньше pivot оказались слева, а больше — справа
3. Получаем позицию pivot'а после разбиения
4. Если позиция pivot == k — нашли ответ
5. Если позиция > k — ищем в левой половине
6. Если позиция < k — ищем в правой половине

## Complexity
- Time complexity (средняя): **O(n)**
- Time complexity (худшая): **O(n²)**
- Space complexity: **O(1)** (in-place)

## Code

```java
import java.util.Random;

public class QuickSelect {
    
    private static Random random = new Random();
    
    // Нахождение k-го наименьшего элемента (0-индексация)
    public static int quickSelect(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelect(arr, 0, arr.length - 1, k);
    }
    
    private static int quickSelect(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        
        // Выбираем случайный опорный элемент
        int pivotIndex = partition(arr, left, right);
        
        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return quickSelect(arr, left, pivotIndex - 1, k);
        } else {
            return quickSelect(arr, pivotIndex + 1, right, k);
        }
    }
    
    // Разбиение с выбором последнего элемента как опорного
    private static int partition(int[] arr, int left, int right) {
        // Выбираем случайный опорный для избежания худшего случая
        int randomIndex = left + random.nextInt(right - left + 1);
        swap(arr, randomIndex, right);
        
        int pivot = arr[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        
        swap(arr, i, right);
        return i;
    }
    
    // Нахождение k-го наименьшего с медианой из трех
    public static int quickSelectMedianOfThree(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelectMedianOfThree(arr, 0, arr.length - 1, k);
    }
    
    private static int quickSelectMedianOfThree(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        
        int pivotIndex = partitionMedianOfThree(arr, left, right);
        
        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return quickSelectMedianOfThree(arr, left, pivotIndex - 1, k);
        } else {
            return quickSelectMedianOfThree(arr, pivotIndex + 1, right, k);
        }
    }
    
    private static int partitionMedianOfThree(int[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        
        // Медиана из трех: arr[left], arr[mid], arr[right]
        if (arr[left] > arr[mid]) swap(arr, left, mid);
        if (arr[left] > arr[right]) swap(arr, left, right);
        if (arr[mid] > arr[right]) swap(arr, mid, right);
        
        // Помещаем медиану в конец
        swap(arr, mid, right);
        
        return partition(arr, left, right);
    }
    
    // Поиск k-го наибольшего элемента
    public static int quickSelectLargest(int[] arr, int k) {
        // k-й наибольший = (n - k)-й наименьший
        return quickSelect(arr, arr.length - k - 1);
    }
    
    // Нахождение медианы
    public static double findMedian(int[] arr) {
        int n = arr.length;
        if (n % 2 == 1) {
            return quickSelect(arr, n / 2);
        } else {
            int left = quickSelect(arr, n / 2 - 1);
            int right = quickSelect(arr, n / 2);
            return (left + right) / 2.0;
        }
    }
    
    // Итеративная версия (без рекурсии)
    public static int quickSelectIterative(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            if (left == right) {
                return arr[left];
            }
            
            int pivotIndex = partition(arr, left, right);
            
            if (k == pivotIndex) {
                return arr[k];
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }
        
        return -1;
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // Вариант для работы с дубликатами (3-х стороннее разбиение)
    public static int quickSelectWithDuplicates(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelectWithDuplicates(arr, 0, arr.length - 1, k);
    }
    
    private static int quickSelectWithDuplicates(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        
        int pivot = arr[left + random.nextInt(right - left + 1)];
        int lt = left;      // последний элемент < pivot
        int gt = right;     // первый элемент > pivot
        int i = left;
        
        // 3-стороннее разбиение: < pivot | = pivot | > pivot
        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, i, lt);
                lt++;
                i++;
            } else if (arr[i] > pivot) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        
        // Теперь: [left, lt-1] < pivot, [lt, gt] = pivot, [gt+1, right] > pivot
        if (k < lt) {
            return quickSelectWithDuplicates(arr, left, lt - 1, k);
        } else if (k <= gt) {
            return pivot;
        } else {
            return quickSelectWithDuplicates(arr, gt + 1, right, k);
        }
    }
}