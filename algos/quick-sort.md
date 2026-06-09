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
       private static int partition(int[] array, int left, int right) {
        int randomIndex = left + random.nextInt(right - left + 1);

        int pivot = array[left];
        int i = left - 1;
        int j = right + 1;

        while (true) {
            // Двигаем i вправо, пока элементы меньше опорного
            do {
                i++;
            } while (array[i] < pivot);

            // Двигаем j влево, пока элементы больше опорного
            do {
                j--;
            } while (array[j] > pivot);

            // Если индексы пересеклись, возвращаем j
            if (i >= j) {
                return j;
            }

            // Меняем элементы местами
            swap(array, i, j);
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
    /**
     * Рекурсивный метод сортировки
     * @param array массив для сортировки
     * @param left левая граница подмассива
     * @param right правая граница подмассива
     */
    private static void quickSort(int[] array, int left, int right) {
        if (left < right) {
            // Разделяем массив и получаем индекс опорного элемента
            int pivotIndex = partition(array, left, right);

            // Рекурсивно сортируем левую и правую части
            quickSort(array, left, pivotIndex);
            quickSort(array, pivotIndex + 1, right);
        }
    }

    /**
     * Разделение массива по схеме Хоара со случайным опорным элементом
     * @param array массив для разделения
     * @param left левая граница
     * @param right правая граница
     * @return индекс опорного элемента
     */
    private static int partition(int[] array, int left, int right) {
        int randomIndex = left + random.nextInt(right - left + 1);

        int pivot = array[left];
        int i = left - 1;
        int j = right + 1;

        while (true) {
            // Двигаем i вправо, пока элементы меньше опорного
            do {
                i++;
            } while (array[i] < pivot);

            // Двигаем j влево, пока элементы больше опорного
            do {
                j--;
            } while (array[j] > pivot);

            // Если индексы пересеклись, возвращаем j
            if (i >= j) {
                return j;
            }

            // Меняем элементы местами
            swap(array, i, j);
        }
    }

    /**
     * Вспомогательный метод для обмена элементов
     * @param array массив
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }}
```

