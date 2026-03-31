# Сортировка слиянием (Merge Sort)

## Intuition
Сортировка слиянием — это классический алгоритм "разделяй и властвуй". Идея проста: если массив можно разбить на две половинки, отсортировать каждую из них отдельно, а затем слить в один отсортированный массив, то мы получим полностью отсортированный массив. Этот принцип рекурсивно применяется до тех пор, пока не останутся массивы из одного элемента (которые уже отсортированы).

## Approach
1. **Разделение (Divide)**: рекурсивно делим массив пополам, пока не получим подмассивы из одного элемента
2. **Властвование (Conquer)**: каждый подмассив из одного элемента уже отсортирован
3. **Слияние (Merge)**: попарно сливаем отсортированные подмассивы в один отсортированный массив
4. Процесс повторяется, пока не получим один отсортированный массив

## Complexity
- Time complexity: $$O(n \log n)$$ во всех случаях (лучший, средний, худший)

- Space complexity: $$O(n)$$ для временного массива при слиянии

## Code
```java
public class MergeSort {
    
    public void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            // Рекурсивно сортируем левую и правую половины
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            // Сливаем отсортированные половины
            merge(arr, left, mid, right);
        }
    }
    
    private void merge(int[] arr, int left, int mid, int right) {
        // Размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        // Создаём временные массивы
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];
        
        // Копируем данные
        for (int i = 0; i < n1; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }
        
        // Слияние временных массивов
        int i = 0, j = 0;
        int k = left;
        
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        
        // Копируем оставшиеся элементы
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
    
    // Оптимизированная версия с одним временным массивом
    public void mergeSortOptimized(int[] arr) {
        int[] temp = new int[arr.length];
        mergeSortOptimized(arr, temp, 0, arr.length - 1);
    }
    
    private void mergeSortOptimized(int[] arr, int[] temp, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortOptimized(arr, temp, left, mid);
            mergeSortOptimized(arr, temp, mid + 1, right);
            mergeOptimized(arr, temp, left, mid, right);
        }
    }
    
    private void mergeOptimized(int[] arr, int[] temp, int left, int mid, int right) {
        // Копируем во временный массив
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }
        
        int i = left;
        int j = mid + 1;
        int k = left;
        
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k] = temp[i];
                i++;
            } else {
                arr[k] = temp[j];
                j++;
            }
            k++;
        }
        
        while (i <= mid) {
            arr[k] = temp[i];
            i++;
            k++;
        }
        
        // Правую часть копировать не нужно, она уже на месте
    }
}