# Экспоненциальный поиск (Exponential Search)

## Intuition
Экспоненциальный поиск — это алгоритм для поиска элемента в отсортированном массиве, который сначала определяет диапазон, где может находиться элемент, путем экспоненциального увеличения границы, а затем выполняет бинарный поиск в найденном диапазоне. Он особенно эффективен, когда элемент находится в начале массива или когда размер массива неизвестен (например, в бесконечных потоках данных). Представьте, что вы ищете книгу на бесконечной полке: сначала вы проверяете 1-ю книгу, затем 2-ю, 4-ю, 8-ю и так далее, пока не найдете раздел, где искомое заведомо находится. Затем вы сужаете поиск до этого раздела.

## Approach
1. Начинаем с первого элемента
2. Удваиваем интервал, пока не найдем элемент, больший или равный искомому:
    - Проверяем позиции: 1, 2, 4, 8, 16, ... пока arr[bound] < target
3. Найденный интервал: [bound/2, min(bound, n-1)]
4. Выполняем бинарный поиск в этом интервале
5. Если массив "бесконечный", продолжаем увеличивать bound, пока не найдем

## Complexity
- Time complexity (лучшая): **O(1)**
- Time complexity (средняя): **O(log i)**, где i — позиция элемента
- Time complexity (худшая): **O(log n)**
- Space complexity: **O(1)**

## Code

```java
import java.util.Arrays;

public class ExponentialSearch {
    
    // Экспоненциальный поиск в массиве
    public static int search(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        
        // Если первый элемент уже искомый
        if (arr[0] == target) return 0;
        
        int n = arr.length;
        int bound = 1;
        
        // Экспоненциально увеличиваем границу, пока не найдем диапазон
        while (bound < n && arr[bound] < target) {
            bound *= 2;
        }
        
        // Бинарный поиск в найденном диапазоне
        int left = bound / 2;
        int right = Math.min(bound, n - 1);
        
        return binarySearch(arr, target, left, right);
    }
    
    // Экспоненциальный поиск для "бесконечного" массива (через функцию)
    public static int searchInInfiniteArray(ArrayReader reader, int target) {
        if (reader.get(0) == target) return 0;
        
        int bound = 1;
        while (reader.get(bound) != Integer.MAX_VALUE && reader.get(bound) < target) {
            bound *= 2;
        }
        
        return binarySearchInfinite(reader, target, bound / 2, bound);
    }
    
    // Бинарный поиск в диапазоне
    private static int binarySearch(int[] arr, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    // Бинарный поиск для бесконечного массива
    private static int binarySearchInfinite(ArrayReader reader, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = reader.get(mid);
            
            if (midVal == Integer.MAX_VALUE) {
                right = mid - 1;
                continue;
            }
            
            if (midVal == target) {
                return mid;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    // Экспоненциальный поиск для списка
    public static int search(java.util.List<Integer> list, int target) {
        if (list == null || list.isEmpty()) return -1;
        
        if (list.get(0) == target) return 0;
        
        int n = list.size();
        int bound = 1;
        
        while (bound < n && list.get(bound) < target) {
            bound *= 2;
        }
        
        int left = bound / 2;
        int right = Math.min(bound, n - 1);
        
        return binarySearch(list, target, left, right);
    }
    
    private static int binarySearch(java.util.List<Integer> list, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = list.get(mid);
            
            if (midVal == target) {
                return mid;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    // Версия с подсчетом количества сравнений (для демонстрации эффективности)
    public static SearchResult searchWithStats(int[] arr, int target) {
        int comparisons = 0;
        
        if (arr == null || arr.length == 0) {
            return new SearchResult(-1, comparisons);
        }
        
        comparisons++;
        if (arr[0] == target) {
            return new SearchResult(0, comparisons);
        }
        
        int n = arr.length;
        int bound = 1;
        
        while (bound < n) {
            comparisons++;
            if (arr[bound] >= target) {
                break;
            }
            bound *= 2;
        }
        
        int left = bound / 2;
        int right = Math.min(bound, n - 1);
        
        // Бинарный поиск с подсчетом сравнений
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            
            if (arr[mid] == target) {
                return new SearchResult(mid, comparisons);
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return new SearchResult(-1, comparisons);
    }
    
    static class SearchResult {
        int index;
        int comparisons;
        
        SearchResult(int index, int comparisons) {
            this.index = index;
            this.comparisons = comparisons;
        }
    }
    
    // Интерфейс для "бесконечного" читателя массива
    public interface ArrayReader {
        int get(int index);
    }
    
    // Реализация для демонстрации
    static class InfiniteArrayReader implements ArrayReader {
        private int[] arr;
        
        InfiniteArrayReader(int[] arr) {
            this.arr = arr;
        }
        
        @Override
        public int get(int index) {
            if (index >= arr.length) {
                return Integer.MAX_VALUE; // сигнал, что вышли за пределы
            }
            return arr[index];
        }
    }
}