# Быстрая сортировка

## Intuition
Быстрая сортировка основана на принципе "разделяй и властвуй". Идея заключается в разделении массива на 2 подмассива и сортировки их раздельно. При выборе рандомномного опорного элемента (pivot) достигается аппроксимированная сложность O(n * log n).

## Approach
1. Выбрать опорный элемент (pivot). Вообще существует несколько реализаций метода выбирающего pivot - partition:
   - C 3 указателями
   - Фиксированная позиция pivot(первый, последний элемент)
   - Метод Хоара
   Наибольшая скорость достигается именно в методе Хоара, поскольку используется всего 2 указателя и выполняется меньшее число перестановок относительно метода с 3 указателями. Метод фиксированной позиции не стоит использовать никогда, он хорош только для понимания принципа работы сортировки. Идея partition заключается в том, чтобы поставить слева все элементы <= опорного, справа - большие опорного. По этой причине рассмотрим метод Хоара:
   ```java
       private int partition(int[] arr, int from, int to) {
          // Выбираем элемент как опорный
          int pivot = arr[(new Random()).next(from, to + 1)];
          
          while(true){
            while(from < to && arr[from] <= pivot)from++;
            while(from < to && arr[to] > pivot)to--;
            if(from >= to) return to;
            swap(arr, from, to);
          }
        }
    }
   ```
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
    
    private int partition(int[] arr, int from, int to) {
          // Выбираем элемент как опорный
          int pivot = arr[(new Random()).next(from, to + 1)];
          
          while(true){
            while(from < to && arr[from] <= pivot)from++;
            while(from < to && arr[to] > pivot)to--;
            if(from >= to) return to;
            swap(arr, from, to);
          }
        }
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```

