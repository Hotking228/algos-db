# Бинарный поиск

## Intuition
Бинарный поиск основан на принципе "разделяй и властвуй". Если массив отсортирован, мы можем эффективно искать элемент, каждый раз отбрасывая половину массива. Представьте, что вы ищете слово в словаре - вы не будете просматривать каждую страницу, а откроете словарь примерно посередине и поймёте, в какой половине искать дальше.

## Approach
1. Определяем левую (left) и правую (right) границы поиска
2. Находим средний элемент mid = left + (right - left) / 2
3. Сравниваем средний элемент с искомым (target)
4. Если равны - возвращаем индекс mid
5. Если target меньше - ищем в левой половине (right = mid - 1)
6. Если target больше - ищем в правой половине (left = mid + 1)
7. Повторяем шаги 2-6 пока left <= right
8. Если элемент не найден - возвращаем -1

## Complexity
- Time complexity: $$O(\log n)$$ - на каждой итерации массив уменьшается вдвое

- Space complexity: $$O(1)$$ для итеративной реализации, $$O(\log n)$$ для рекурсивной

## Code
```java
public class BinarySearch {
    // Итеративная версия
    public int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // предотвращает переполнение
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // элемент не найден
    }
    
    // Рекурсивная версия
    public int binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return binarySearchRecursive(arr, target, mid + 1, right);
        } else {
            return binarySearchRecursive(arr, target, left, mid - 1);
        }
    }
}
```