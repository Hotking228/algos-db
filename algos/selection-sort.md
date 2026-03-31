# Сортировка выбором (Selection Sort)

## Intuition
Сортировка выбором основана на очень простой идее: на каждом шаге находим минимальный (или максимальный) элемент в неотсортированной части массива и ставим его на своё место. Представьте, что вы сортируете колоду карт, каждый раз выбирая самую маленькую карту из оставшихся и кладя её в конец отсортированной стопки.

## Approach
1. Делим массив на две части: отсортированную (слева) и неотсортированную (справа)
2. Находим минимальный элемент в неотсортированной части
3. Меняем его местами с первым элементом неотсортированной части
4. Увеличиваем границу отсортированной части на один элемент
5. Повторяем шаги 2-4, пока весь массив не будет отсортирован

## Complexity
- Time complexity: $$O(n^2)$$ во всех случаях (лучший, средний, худший)

- Space complexity: $$O(1)$$ (сортировка на месте)

## Code

### Базовая реализация
```java
public class SelectionSort {
    
    // Сортировка по возрастанию
    public void selectionSort(int[] arr) {
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            // Находим индекс минимального элемента в неотсортированной части
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // Меняем местами найденный минимальный элемент с первым элементом
            swap(arr, i, minIndex);
        }
    }
    
    // Сортировка по убыванию
    public void selectionSortDescending(int[] arr) {
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] > arr[maxIndex]) {
                    maxIndex = j;
                }
            }
            swap(arr, i, maxIndex);
        }
    }
    
    // Сортировка с дженериками
    public <T extends Comparable<T>> void selectionSortGeneric(T[] arr) {
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].compareTo(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            swapGeneric(arr, i, minIndex);
        }
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private <T> void swapGeneric(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}