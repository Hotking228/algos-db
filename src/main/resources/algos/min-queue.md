# Очередь с минимумом (Min Queue)

## Intuition
Очередь с минимумом — это структура данных, которая поддерживает все стандартные операции очереди (enqueue, dequeue, front) и позволяет получать минимальный элемент в очереди за O(1) времени. В отличие от стека с минимумом, очередь работает по принципу FIFO, что усложняет задачу, так как элементы удаляются с другого конца. Однако эту проблему можно решить с помощью двух стеков или монотонной очереди. Представьте, что у вас есть очередь людей, и вы хотите всегда знать самого низкого человека в очереди, при этом люди входят с одного конца и выходят с другого.

## Approach
**Метод 1: Два стека (алгоритм очереди с минимумом)**
- Используем два стека: входной (push) и выходной (pop)
- Каждый стек поддерживает свой текущий минимум
- При enqueue: добавляем элемент во входной стек
- При dequeue: если выходной стек пуст, перекладываем все элементы из входного стека в выходной (с сохранением минимумов)
- getMin: минимум = min(мин_входного_стека, мин_выходного_стека)

**Метод 2: Монотонная очередь (deque)**
- Используем deque для хранения элементов в возрастающем порядке
- При enqueue: удаляем с конца все элементы, большие нового, затем добавляем новый
- При dequeue: если удаляемый элемент равен первому в deque, удаляем и из deque

## Complexity
- Time complexity (enqueue): **O(1)** амортизированно
- Time complexity (dequeue): **O(1)** амортизированно
- Time complexity (getMin): **O(1)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class MinQueue {
    
    // Метод 1: Два стека с минимумом
    public static class TwoStackMinQueue {
        private StackWithMin inputStack;
        private StackWithMin outputStack;
        
        public TwoStackMinQueue() {
            inputStack = new StackWithMin();
            outputStack = new StackWithMin();
        }
        
        public void enqueue(int val) {
            inputStack.push(val);
        }
        
        public int dequeue() {
            if (outputStack.isEmpty()) {
                while (!inputStack.isEmpty()) {
                    outputStack.push(inputStack.pop());
                }
            }
            return outputStack.pop();
        }
        
        public int front() {
            if (outputStack.isEmpty()) {
                while (!inputStack.isEmpty()) {
                    outputStack.push(inputStack.pop());
                }
            }
            return outputStack.peek();
        }
        
        public int getMin() {
            int min = Integer.MAX_VALUE;
            if (!inputStack.isEmpty()) {
                min = Math.min(min, inputStack.getMin());
            }
            if (!outputStack.isEmpty()) {
                min = Math.min(min, outputStack.getMin());
            }
            return min;
        }
        
        public boolean isEmpty() {
            return inputStack.isEmpty() && outputStack.isEmpty();
        }
        
        public int size() {
            return inputStack.size() + outputStack.size();
        }
        
        // Вспомогательный стек с минимумом
        private static class StackWithMin {
            private Stack<Integer> stack;
            private Stack<Integer> minStack;
            
            StackWithMin() {
                stack = new Stack<>();
                minStack = new Stack<>();
            }
            
            void push(int val) {
                stack.push(val);
                if (minStack.isEmpty() || val <= minStack.peek()) {
                    minStack.push(val);
                }
            }
            
            int pop() {
                int val = stack.pop();
                if (val == minStack.peek()) {
                    minStack.pop();
                }
                return val;
            }
            
            int peek() {
                return stack.peek();
            }
            
            int getMin() {
                return minStack.peek();
            }
            
            boolean isEmpty() {
                return stack.isEmpty();
            }
            
            int size() {
                return stack.size();
            }
        }
    }
    
    // Метод 2: Монотонная очередь (deque)
    public static class MonotonicMinQueue {
        private Queue<Integer> queue;
        private Deque<Integer> minDeque; // хранит элементы в возрастающем порядке
        
        public MonotonicMinQueue() {
            queue = new LinkedList<>();
            minDeque = new LinkedList<>();
        }
        
        public void enqueue(int val) {
            queue.offer(val);
            // Удаляем с конца все элементы, большие val
            while (!minDeque.isEmpty() && minDeque.peekLast() > val) {
                minDeque.pollLast();
            }
            minDeque.offerLast(val);
        }
        
        public int dequeue() {
            int removed = queue.poll();
            if (removed == minDeque.peekFirst()) {
                minDeque.pollFirst();
            }
            return removed;
        }
        
        public int front() {
            return queue.peek();
        }
        
        public int getMin() {
            return minDeque.peekFirst();
        }
        
        public boolean isEmpty() {
            return queue.isEmpty();
        }
        
        public int size() {
            return queue.size();
        }
    }
    
    // Очередь с максимумом (аналогично минимуму)
    public static class MaxQueue {
        private Queue<Integer> queue;
        private Deque<Integer> maxDeque;
        
        public MaxQueue() {
            queue = new LinkedList<>();
            maxDeque = new LinkedList<>();
        }
        
        public void enqueue(int val) {
            queue.offer(val);
            while (!maxDeque.isEmpty() && maxDeque.peekLast() < val) {
                maxDeque.pollLast();
            }
            maxDeque.offerLast(val);
        }
        
        public int dequeue() {
            int removed = queue.poll();
            if (removed == maxDeque.peekFirst()) {
                maxDeque.pollFirst();
            }
            return removed;
        }
        
        public int front() {
            return queue.peek();
        }
        
        public int getMax() {
            return maxDeque.peekFirst();
        }
        
        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }
    
    // Обобщенная очередь с поддержкой min и max
    public static class MinMaxQueue {
        private Queue<Integer> queue;
        private Deque<Integer> minDeque;
        private Deque<Integer> maxDeque;
        
        public MinMaxQueue() {
            queue = new LinkedList<>();
            minDeque = new LinkedList<>();
            maxDeque = new LinkedList<>();
        }
        
        public void enqueue(int val) {
            queue.offer(val);
            
            // Поддержка minDeque
            while (!minDeque.isEmpty() && minDeque.peekLast() > val) {
                minDeque.pollLast();
            }
            minDeque.offerLast(val);
            
            // Поддержка maxDeque
            while (!maxDeque.isEmpty() && maxDeque.peekLast() < val) {
                maxDeque.pollLast();
            }
            maxDeque.offerLast(val);
        }
        
        public int dequeue() {
            int removed = queue.poll();
            if (removed == minDeque.peekFirst()) {
                minDeque.pollFirst();
            }
            if (removed == maxDeque.peekFirst()) {
                maxDeque.pollFirst();
            }
            return removed;
        }
        
        public int front() {
            return queue.peek();
        }
        
        public int getMin() {
            return minDeque.peekFirst();
        }
        
        public int getMax() {
            return maxDeque.peekFirst();
        }
        
        public boolean isEmpty() {
            return queue.isEmpty();
        }
        
        public int size() {
            return queue.size();
        }
    }
    
    // Очередь с минимумом для скользящего окна
    public static class SlidingWindowMin {
        private Deque<Integer> deque;
        private int[] arr;
        private int left;
        private int right;
        
        public SlidingWindowMin(int[] arr) {
            this.arr = arr;
            this.deque = new LinkedList<>();
            this.left = 0;
            this.right = -1;
        }
        
        // Добавляем элемент в окно
        public void add(int index) {
            while (!deque.isEmpty() && arr[deque.peekLast()] >= arr[index]) {
                deque.pollLast();
            }
            deque.offerLast(index);
            right = index;
        }
        
        // Удаляем элемент из окна
        public void remove(int index) {
            if (!deque.isEmpty() && deque.peekFirst() == index) {
                deque.pollFirst();
            }
            left++;
        }
        
        // Получаем минимум в текущем окне
        public int getMin() {
            return arr[deque.peekFirst()];
        }
        
        // Сдвигаем окно
        public void slide() {
            if (left <= right) {
                remove(left);
                add(right + 1);
            }
        }
    }
}