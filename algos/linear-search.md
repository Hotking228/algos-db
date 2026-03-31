# Линейный поиск (Linear Search)

## Intuition
Линейный поиск — это самый простой и интуитивно понятный алгоритм поиска. Идея заключается в последовательном просмотре всех элементов массива (или коллекции) до тех пор, пока не будет найден искомый элемент или не закончатся все элементы. Это именно то, как человек искал бы что-то в неупорядоченном списке — просто просматривая каждый элемент по очереди.

## Approach
1. Начинаем с первого элемента массива (индекс 0)
2. Сравниваем текущий элемент с искомым значением (target)
3. Если элементы равны — возвращаем текущий индекс
4. Если не равны — переходим к следующему элементу
5. Повторяем шаги 2-4 до конца массива
6. Если дошли до конца и ничего не нашли — возвращаем -1

## Complexity
- Time complexity:
    - Лучший случай: $$O(1)$$ (элемент найден в начале)
    - Худший случай: $$O(n)$$ (элемент в конце или отсутствует)
    - Средний случай: $$O(n)$$

- Space complexity: $$O(1)$$ (не требует дополнительной памяти)

## Code
```java
public class LinearSearch {
    
    // Поиск в массиве
    public int search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // элемент найден, возвращаем индекс
            }
        }
        return -1; // элемент не найден
    }
    
    // Поиск в массиве с дженериками
    public <T> int searchGeneric(T[] arr, T target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }
    
    // Поиск всех вхождений
    public List<Integer> searchAll(int[] arr, int target) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                indices.add(i);
            }
        }
        return indices;
    }
    
    // Поиск в двумерном массиве
    public int[] searchIn2D(int[][] matrix, int target) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    return new int[]{i, j}; // возвращаем [строка, столбец]
                }
            }
        }
        return new int[]{-1, -1};
    }
    
    // Поиск с условием (например, найти первый чётный элемент)
    public int searchFirstEven(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                return i;
            }
        }
        return -1;
    }
    
    // Поиск максимального элемента
    public int findMax(int[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        
        int max = arr[0];
        int maxIndex = 0;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                maxIndex = i;
            }
        }
        
        return maxIndex;
    }
}