# Гномья сортировка (Gnome Sort)

## Intuition
Гномья сортировка — это простой алгоритм сортировки, основанный на идее "садового гнома", который сортирует цветочные горшки. Гном смотрит на текущий и предыдущий горшки: если они в правильном порядке, он идет вперед; если нет — меняет их местами и отступает на шаг назад. Представьте, что вы сортируете колоду карт, сравнивая соседние карты: если порядок правильный, вы двигаетесь дальше, если нет — меняете их местами и возвращаетесь к предыдущей позиции. Это похоже на упрощенную версию сортировки вставками.

## Approach
1. Устанавливаем указатель `pos = 0`
2. Пока `pos < n`:
    - Если `pos == 0` или `arr[pos] >= arr[pos - 1]`, увеличиваем `pos` на 1
    - Иначе меняем местами `arr[pos]` и `arr[pos - 1]`, уменьшаем `pos` на 1
3. Повторяем, пока весь массив не будет отсортирован

## Complexity
- Time complexity (лучшая): **O(n)** — когда массив уже отсортирован
- Time complexity (средняя): **O(n²)**
- Time complexity (худшая): **O(n²)**
- Space complexity: **O(1)**

## Code

```java
public class GnomeSort {
    
    // Базовая гномья сортировка
    public static void sort(int[] arr) {
        int pos = 0;
        
        while (pos < arr.length) {
            if (pos == 0 || arr[pos] >= arr[pos - 1]) {
                pos++;
            } else {
                // Меняем местами и отступаем
                swap(arr, pos, pos - 1);
                pos--;
            }
        }
    }
    
    // Гномья сортировка с оптимизацией (запоминаем позицию)
    public static void sortOptimized(int[] arr) {
        int pos = 0;
        int lastSwapped = 0;
        
        while (pos < arr.length) {
            if (pos == 0 || arr[pos] >= arr[pos - 1]) {
                pos++;
            } else {
                swap(arr, pos, pos - 1);
                if (lastSwapped == 0) {
                    lastSwapped = pos;
                }
                pos--;
                
                // Если дошли до начала, начинаем с lastSwapped
                if (pos == 0 && lastSwapped > 0) {
                    pos = lastSwapped;
                    lastSwapped = 0;
                }
            }
        }
    }
    
    // Гномья сортировка для сравнимых объектов
    public static <T extends Comparable<T>> void sort(T[] arr) {
        int pos = 0;
        
        while (pos < arr.length) {
            if (pos == 0 || arr[pos].compareTo(arr[pos - 1]) >= 0) {
                pos++;
            } else {
                T temp = arr[pos];
                arr[pos] = arr[pos - 1];
                arr[pos - 1] = temp;
                pos--;
            }
        }
    }
    
    // Гномья сортировка с пользовательским компаратором
    public static <T> void sort(T[] arr, java.util.Comparator<T> comparator) {
        int pos = 0;
        
        while (pos < arr.length) {
            if (pos == 0 || comparator.compare(arr[pos], arr[pos - 1]) >= 0) {
                pos++;
            } else {
                T temp = arr[pos];
                arr[pos] = arr[pos - 1];
                arr[pos - 1] = temp;
                pos--;
            }
        }
    }
    
    // Гномья сортировка для части массива
    public static void sort(int[] arr, int left, int right) {
        int pos = left;
        
        while (pos <= right) {
            if (pos == left || arr[pos] >= arr[pos - 1]) {
                pos++;
            } else {
                swap(arr, pos, pos - 1);
                pos--;
            }
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}