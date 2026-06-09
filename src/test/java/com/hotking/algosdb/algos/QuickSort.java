package com.hotking.algosdb.algos;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    @Test
    public void quickSortTest() {
        int[] arr = {5, 1, 4, 2, 3};
        int low = 0;
        int high = arr.length - 1;
        quickSort(arr, low, high);
        assertThat(arr).isSorted();
    }

    private static final Random random = new Random();

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
        // Выбираем случайный опорный элемент и меняем его с правым
        int randomIndex = left + random.nextInt(right - left + 1);
//        swap(array, randomIndex, left);

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
    }
}
