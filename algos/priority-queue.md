# Очередь с приоритетом (Priority Queue)

## Intuition
Очередь с приоритетом — это абстрактная структура данных, где каждый элемент имеет приоритет, и элементы извлекаются в порядке приоритета (сначала наибольший или наименьший). В отличие от обычной очереди (FIFO), здесь порядок определяется приоритетом, а не временем добавления. Представьте больницу, где пациенты с более тяжелыми состояниями обслуживаются раньше, независимо от времени прибытия. Обычно очередь с приоритетом реализуется через кучу (heap), что обеспечивает логарифмическое время для вставки и извлечения.

## Approach
1. **Реализация на куче**:
    - Вставка: добавляем элемент в конец, проталкиваем вверх (heapify up)
    - Извлечение: заменяем корень последним элементом, проталкиваем вниз (heapify down)
2. **Реализация на отсортированном списке** (для маленьких n):
    - Вставка за O(n), извлечение за O(1)
3. **Биномиальная куча** или **Фибоначчиева куча** для более эффективных операций

## Complexity
- Вставка (куча): **O(log n)**
- Извлечение (куча): **O(log n)**
- Просмотр вершины: **O(1)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class PriorityQueueCustom<E extends Comparable<E>> {
    
    private List<E> heap;
    private boolean isMinHeap;
    
    // Min-heap по умолчанию
    public PriorityQueueCustom() {
        this(true);
    }
    
    public PriorityQueueCustom(boolean isMinHeap) {
        this.heap = new ArrayList<>();
        this.isMinHeap = isMinHeap;
    }
    
    // Создание из коллекции (heapify за O(n))
    public PriorityQueueCustom(Collection<E> collection, boolean isMinHeap) {
        this(isMinHeap);
        heap.addAll(collection);
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }
    
    // Добавление элемента
    public void add(E element) {
        heap.add(element);
        heapifyUp(heap.size() - 1);
    }
    
    // Получение и удаление элемента с наивысшим приоритетом
    public E poll() {
        if (isEmpty()) return null;
        
        E result = heap.get(0);
        E last = heap.remove(heap.size() - 1);
        
        if (!isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return result;
    }
    
    // Просмотр элемента с наивысшим приоритетом без удаления
    public E peek() {
        return isEmpty() ? null : heap.get(0);
    }
    
    // Размер очереди
    public int size() {
        return heap.size();
    }
    
    // Проверка на пустоту
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    // Проталкивание вверх
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(heap.get(index), heap.get(parent)) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }
    
    // Проталкивание вниз
    private void heapifyDown(int index) {
        int size = heap.size();
        
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int target = index;
            
            if (left < size && compare(heap.get(left), heap.get(target)) < 0) {
                target = left;
            }
            if (right < size && compare(heap.get(right), heap.get(target)) < 0) {
                target = right;
            }
            
            if (target != index) {
                swap(index, target);
                index = target;
            } else {
                break;
            }
        }
    }
    
    // Сравнение с учетом типа кучи
    private int compare(E a, E b) {
        return isMinHeap ? a.compareTo(b) : b.compareTo(a);
    }
    
    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    // Удаление произвольного элемента (требует поиска)
    public boolean remove(E element) {
        int index = heap.indexOf(element);
        if (index == -1) return false;
        
        int lastIndex = heap.size() - 1;
        if (index == lastIndex) {
            heap.remove(lastIndex);
            return true;
        }
        
        heap.set(index, heap.get(lastIndex));
        heap.remove(lastIndex);
        
        heapifyDown(index);
        heapifyUp(index);
        
        return true;
    }
    
    // Очистка очереди
    public void clear() {
        heap.clear();
    }
    
    // Приоритетная очередь с компаратором
    public static class PriorityQueueWithComparator<T> {
        private List<T> heap;
        private Comparator<T> comparator;
        
        public PriorityQueueWithComparator(Comparator<T> comparator) {
            this.heap = new ArrayList<>();
            this.comparator = comparator;
        }
        
        public void add(T element) {
            heap.add(element);
            heapifyUp(heap.size() - 1);
        }
        
        public T poll() {
            if (isEmpty()) return null;
            
            T result = heap.get(0);
            T last = heap.remove(heap.size() - 1);
            
            if (!isEmpty()) {
                heap.set(0, last);
                heapifyDown(0);
            }
            
            return result;
        }
        
        public T peek() {
            return isEmpty() ? null : heap.get(0);
        }
        
        public int size() { return heap.size(); }
        public boolean isEmpty() { return heap.isEmpty(); }
        
        private void heapifyUp(int index) {
            while (index > 0) {
                int parent = (index - 1) / 2;
                if (comparator.compare(heap.get(index), heap.get(parent)) < 0) {
                    swap(index, parent);
                    index = parent;
                } else {
                    break;
                }
            }
        }
        
        private void heapifyDown(int index) {
            int size = heap.size();
            
            while (true) {
                int left = 2 * index + 1;
                int right = 2 * index + 2;
                int target = index;
                
                if (left < size && comparator.compare(heap.get(left), heap.get(target)) < 0) {
                    target = left;
                }
                if (right < size && comparator.compare(heap.get(right), heap.get(target)) < 0) {
                    target = right;
                }
                
                if (target != index) {
                    swap(index, target);
                    index = target;
                } else {
                    break;
                }
            }
        }
        
        private void swap(int i, int j) {
            T temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
    
    // Медианная очередь (поддерживает получение медианы за O(1))
    public static class MedianQueue {
        private PriorityQueueCustom<Integer> maxHeap; // левая половина (максимум)
        private PriorityQueueCustom<Integer> minHeap; // правая половина (минимум)
        
        public MedianQueue() {
            maxHeap = new PriorityQueueCustom<>(false); // max-heap
            minHeap = new PriorityQueueCustom<>(true);  // min-heap
        }
        
        public void add(int num) {
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.add(num);
            } else {
                minHeap.add(num);
            }
            
            // Балансировка
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.add(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.add(minHeap.poll());
            }
        }
        
        public double getMedian() {
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                return maxHeap.peek();
            }
        }
        
        public void removeMedian() {
            if (maxHeap.size() >= minHeap.size()) {
                maxHeap.poll();
            } else {
                minHeap.poll();
            }
            
            // Балансировка
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.add(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.add(minHeap.poll());
            }
        }
    }
}