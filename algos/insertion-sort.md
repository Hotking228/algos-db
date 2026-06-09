# Сортировка вставками (Insertion Sort)

## Intuition
Сортировка вставками работает так же, как вы сортируете карты в руке: вы берете следующую карту и вставляете её в правильную позицию среди уже отсортированных. Представьте, что левая часть массива всегда отсортирована. Вы берете очередной элемент из неотсортированной части и "протаскиваете" его влево, пока не найдете подходящее место. Алгоритм прост и эффективен для небольших или почти отсортированных массивов.

## Approach
1. Начинаем со второго элемента (индекс 1) — считаем, что первый элемент уже отсортирован
2. Для каждого элемента `i` от 1 до n-1:
    - Сохраняем текущий элемент в переменную `key`
    - Инициализируем `j = i - 1`
    - Пока `j >= 0` и `arr[j] > key`:
        - Сдвигаем `arr[j]` вправо на позицию `j + 1`
        - Уменьшаем `j`
    - Вставляем `key` на позицию `j + 1`
3. Повторяем, пока весь массив не будет отсортирован

## Complexity
- Time complexity (лучшая): **O(n)** — массив уже отсортирован
- Time complexity (средняя): **O(n²)**
- Time complexity (худшая): **O(n²)** — массив отсортирован в обратном порядке
- Space complexity: **O(1)**

## Code

```java
public class InsertionSort {
    
    // Базовая сортировка вставками
    public static void sort(int[] arr) {
        int n = arr.length;
        
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // Сдвигаем элементы, которые больше key
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            // Вставляем key на правильную позицию
            arr[j + 1] = key;
        }
    }
    
    // Сортировка вставками с бинарным поиском
    public static void sortWithBinarySearch(int[] arr) {
        int n = arr.length;
        
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            
            // Находим позицию для вставки бинарным поиском
            int left = 0;
            int right = i - 1;
            int pos = i;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (arr[mid] > key) {
                    pos = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            // Сдвигаем элементы
            System.arraycopy(arr, pos, arr, pos + 1, i - pos);
            arr[pos] = key;
        }
    }
    
    // Сортировка вставками для части массива (для быстрой сортировки)
    public static void sort(int[] arr, int left, int right) {
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
    
    // Сортировка вставками для сравнимых объектов
    public static <T extends Comparable<T>> void sort(T[] arr) {
        int n = arr.length;
        
        for (int i = 1; i < n; i++) {
            T key = arr[i];
            int j = i - 1;
            
            while (j >= 0 && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            arr[j + 1] = key;
        }
    }
    
    // Сортировка вставками с пользовательским компаратором
    public static <T> void sort(T[] arr, java.util.Comparator<T> comparator) {
        int n = arr.length;
        
        for (int i = 1; i < n; i++) {
            T key = arr[i];
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            arr[j + 1] = key;
        }
    }
    
    // Сортировка вставками для связного списка
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }
    
    public static ListNode sortLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode dummy = new ListNode(0);
        ListNode current = head;
        
        while (current != null) {
            ListNode prev = dummy;
            ListNode next = current.next;
            
            // Находим позицию для вставки
            while (prev.next != null && prev.next.val < current.val) {
                prev = prev.next;
            }
            
            // Вставляем current
            current.next = prev.next;
            prev.next = current;
            
            current = next;
        }
        
        return dummy.next;
    }
}