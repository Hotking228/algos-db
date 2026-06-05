2. Если arr[pos] == target — нашли
3. Если arr[pos] < target — ищем в правой части (left = pos + 1)
4. Если arr[pos] > target — ищем в левой части (right = pos - 1)
5. Повторяем, пока элемент не найден или интервал не исчерпан

## Complexity
- Time complexity (лучшая): **O(1)**
- Time complexity (средняя): **O(log log n)**
- Time complexity (худшая): **O(n)** — при неравномерном распределении
- Space complexity: **O(1)**

## Code

```java
public class InterpolationSearch {
 
 // Интерполяционный поиск в отсортированном массиве
 public static int search(int[] arr, int target) {
     if (arr == null || arr.length == 0) return -1;
     
     int left = 0;
     int right = arr.length - 1;
     
     while (left <= right && target >= arr[left] && target <= arr[right]) {
         // Если массив содержит только один элемент
         if (left == right) {
             if (arr[left] == target) return left;
             return -1;
         }
         
         // Интерполяционная формула
         int pos = left + (right - left) * (target - arr[left]) / (arr[right] - arr[left]);
         
         // Защита от выхода за границы
         if (pos < left || pos > right) {
             break;
         }
         
         if (arr[pos] == target) {
             return pos;
         } else if (arr[pos] < target) {
             left = pos + 1;
         } else {
             right = pos - 1;
         }
     }
     
     return -1;
 }
 
 // Интерполяционный поиск для массива с двойной точностью
 public static int search(double[] arr, double target) {
     if (arr == null || arr.length == 0) return -1;
     
     int left = 0;
     int right = arr.length - 1;
     
     while (left <= right && target >= arr[left] && target <= arr[right]) {
         if (left == right) {
             if (Math.abs(arr[left] - target) < 1e-9) return left;
             return -1;
         }
         
         int pos = left + (int)((right - left) * (target - arr[left]) / (arr[right] - arr[left]));
         
         if (pos < left || pos > right) {
             break;
         }
         
         if (Math.abs(arr[pos] - target) < 1e-9) {
             return pos;
         } else if (arr[pos] < target) {
             left = pos + 1;
         } else {
             right = pos - 1;
         }
     }
     
     return -1;
 }
 
 // Интерполяционный поиск с рекурсией
 public static int searchRecursive(int[] arr, int target) {
     if (arr == null || arr.length == 0) return -1;
     return searchRecursive(arr, target, 0, arr.length - 1);
 }
 
 private static int searchRecursive(int[] arr, int target, int left, int right) {
     if (left > right || target < arr[left] || target > arr[right]) {
         return -1;
     }
     
     if (left == right) {
         return arr[left] == target ? left : -1;
     }
     
     int pos = left + (right - left) * (target - arr[left]) / (arr[right] - arr[left]);
     
     if (pos < left || pos > right) {
         return -1;
     }
     
     if (arr[pos] == target) {
         return pos;
     } else if (arr[pos] < target) {
         return searchRecursive(arr, target, pos + 1, right);
     } else {
         return searchRecursive(arr, target, left, pos - 1);
     }
 }
 
 // Интерполяционный поиск с подсчетом сравнений
 public static SearchResult searchWithStats(int[] arr, int target) {
     int comparisons = 0;
     
     if (arr == null || arr.length == 0) {
         return new SearchResult(-1, comparisons);
     }
     
     int left = 0;
     int right = arr.length - 1;
     
     while (left <= right && target >= arr[left] && target <= arr[right]) {
         comparisons++;
         
         if (left == right) {
             if (arr[left] == target) {
                 return new SearchResult(left, comparisons);
             }
             return new SearchResult(-1, comparisons);
         }
         
         int pos = left + (right - left) * (target - arr[left]) / (arr[right] - arr[left]);
         
         if (pos < left || pos > right) {
             return new SearchResult(-1, comparisons);
         }
         
         comparisons++;
         if (arr[pos] == target) {
             return new SearchResult(pos, comparisons);
         } else if (arr[pos] < target) {
             left = pos + 1;
         } else {
             right = pos - 1;
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
 
 // Интерполяционный поиск для длинных массивов (с защитой от переполнения)
 public static int searchLong(int[] arr, int target) {
     if (arr == null || arr.length == 0) return -1;
     
     int left = 0;
     int right = arr.length - 1;
     
     while (left <= right && target >= arr[left] && target <= arr[right]) {
         if (left == right) {
             if (arr[left] == target) return left;
             return -1;
         }
         
         // Используем long для избежания переполнения
         long numerator = (long)(right - left) * (target - arr[left]);
         long denominator = arr[right] - arr[left];
         int pos = left + (int)(numerator / denominator);
         
         if (pos < left || pos > right) {
             break;
         }
         
         if (arr[pos] == target) {
             return pos;
         } else if (arr[pos] < target) {
             left = pos + 1;
         } else {
             right = pos - 1;
         }
     }
     
     return -1;
 }
 
 // Поиск в равномерно распределенных данных (оптимальный случай)
 public static int searchUniform(int[] arr, int target) {
     // Этот метод ожидает, что данные равномерно распределены
     // Например, массив типа [0, 10, 20, 30, 40, 50]
     return search(arr, target);
 }
 
 // Адаптивный интерполяционный поиск (переключается на бинарный при плохой интерполяции)
 public static int adaptiveSearch(int[] arr, int target) {
     if (arr == null || arr.length == 0) return -1;
     
     int left = 0;
     int right = arr.length - 1;
     int binaryThreshold = 5; // Порог для переключения на бинарный поиск
     
     while (left <= right && target >= arr[left] && target <= arr[right]) {
         if (right - left < binaryThreshold) {
             // Для маленьких интервалов используем бинарный поиск
             return binarySearch(arr, target, left, right);
         }
         
         if (left == right) {
             if (arr[left] == target) return left;
             return -1;
         }
         
         int pos = left + (right - left) * (target - arr[left]) / (arr[right] - arr[left]);
         
         if (pos < left || pos > right) {
             break;
         }
         
         if (arr[pos] == target) {
             return pos;
         } else if (arr[pos] < target) {
             left = pos + 1;
         } else {
             right = pos - 1;
         }
     }
     
     return -1;
 }
 
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
}