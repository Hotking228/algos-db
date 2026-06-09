# Куча (бинарная)

## Intuition
Бинарная куча — это полное бинарное дерево, которое удовлетворяет свойству кучи: в max-куче каждый родительский узел больше или равен своим детям (в min-куче — меньше или равен). Представьте себе очередь с приоритетом, где элемент с наивысшим приоритетом всегда находится на вершине. Куча идеально подходит для задач, требующих быстрого доступа к максимальному/минимальному элементу.

## Approach
1. Куча реализуется на массиве, где для индекса i:
    - Левый ребенок: 2*i + 1
    - Правый ребенок: 2*i + 2
    - Родитель: (i-1)/2
2. **Вставка (push)**: добавляем элемент в конец массива, затем "проталкиваем" его вверх (heapify up), меняя с родителем, пока не восстановится свойство кучи
3. **Удаление корня (pop)**: заменяем корень последним элементом, удаляем последний, затем "проталкиваем" новый корень вниз (heapify down), меняя с наибольшим/наименьшим ребенком
4. **Построение кучи из массива**: проходим от последнего нелистового узла до корня и применяем heapify down

## Complexity
- Time complexity (вставка): **O(log n)**
- Time complexity (удаление корня): **O(log n)**
- Time complexity (получение корня): **O(1)**
- Time complexity (построение кучи из массива): **O(n)**
- Space complexity: **O(n)**

## Code

### Max-Куча (приоритетная очередь с максимумом)
```java
public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;
    
    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new int[capacity];
    }
    
    // Получение индекса родителя
    private int parent(int index) {
        return (index - 1) / 2;
    }
    
    // Получение индекса левого ребенка
    private int leftChild(int index) {
        return 2 * index + 1;
    }
    
    // Получение индекса правого ребенка
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    
    // Проталкивание элемента вверх
    private void heapifyUp(int index) {
        while (index > 0 && heap[parent(index)] < heap[index]) {
            // Меняем с родителем, если текущий больше
            swap(index, parent(index));
            index = parent(index);
        }
    }
    
    // Проталкивание элемента вниз
    private void heapifyDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);
        
        // Находим наибольший элемент среди узла и его детей
        if (left < size && heap[left] > heap[largest]) {
            largest = left;
        }
        if (right < size && heap[right] > heap[largest]) {
            largest = right;
        }
        
        // Если наибольший не текущий узел, меняем и продолжаем
        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }
    
    // Вставка элемента
    public void insert(int value) {
        if (size == capacity) {
            throw new IllegalStateException("Heap is full");
        }
        
        heap[size] = value;
        heapifyUp(size);
        size++;
    }
    
    // Удаление и возврат максимального элемента
    public int extractMax() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        
        int max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        
        return max;
    }
    
    // Получение максимального элемента без удаления
    public int getMax() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap[0];
    }
    
    // Построение кучи из массива (heapify)
    public void buildHeap(int[] array) {
        if (array.length > capacity) {
            throw new IllegalArgumentException("Array too large for heap capacity");
        }
        
        System.arraycopy(array, 0, heap, 0, array.length);
        size = array.length;
        
        // Начинаем с последнего нелистового узла
        for (int i = (size - 2) / 2; i >= 0; i--) {
            heapifyDown(i);
        }
    }
    
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getSize() {
        return size;
    }
}