# Стек с минимумом (Min Stack)

## Intuition
Стек с минимумом — это структура данных, которая поддерживает все стандартные операции стека (push, pop, top), а также позволяет получать минимальный элемент в стеке за O(1) времени. Идея заключается в том, чтобы хранить вместе с каждым элементом текущий минимум на момент его добавления. Можно использовать вспомогательный стек для хранения минимумов или хранить пары (значение, текущий_минимум). Представьте, что вы складываете предметы в стопку и хотите всегда знать, какой самый маленький предмет находится в стопке, не перебирая все предметы.

## Approach
**Метод 1: Два стека**
- Основной стек хранит все элементы
- Вспомогательный стек хранит текущие минимумы
- При push: если вспомогательный стек пуст или элемент ≤ вершины вспомогательного стека, добавляем в него элемент
- При pop: если удаляемый элемент равен вершине вспомогательного стека, удаляем и из него

**Метод 2: Хранение пар**
- В стеке хранятся пары (значение, минимум_на_данный_момент)
- При push: новый минимум = min(значение, предыдущий_минимум)

**Метод 3: Оптимизация по памяти**
- Вместо хранения каждого минимума, храним разницу между значением и текущим минимумом (требует аккуратности с переполнением)

## Complexity
- Time complexity (push): **O(1)**
- Time complexity (pop): **O(1)**
- Time complexity (getMin): **O(1)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class MinStack {
    
    // Метод 1: Два стека
    public static class TwoStackMinStack {
        private Stack<Integer> stack;
        private Stack<Integer> minStack;
        
        public TwoStackMinStack() {
            stack = new Stack<>();
            minStack = new Stack<>();
        }
        
        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }
        
        public void pop() {
            if (stack.isEmpty()) return;
            int val = stack.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }
        
        public int top() {
            return stack.peek();
        }
        
        public int getMin() {
            return minStack.peek();
        }
    }
    
    // Метод 2: Хранение пар
    public static class PairMinStack {
        private Stack<int[]> stack; // [value, currentMin]
        
        public PairMinStack() {
            stack = new Stack<>();
        }
        
        public void push(int val) {
            if (stack.isEmpty()) {
                stack.push(new int[]{val, val});
            } else {
                int currentMin = Math.min(val, stack.peek()[1]);
                stack.push(new int[]{val, currentMin});
            }
        }
        
        public void pop() {
            stack.pop();
        }
        
        public int top() {
            return stack.peek()[0];
        }
        
        public int getMin() {
            return stack.peek()[1];
        }
    }
    
    // Метод 3: Оптимизация памяти (храним разницу)
    public static class OptimizedMinStack {
        private Stack<Long> stack;
        private long min;
        
        public OptimizedMinStack() {
            stack = new Stack<>();
            min = Long.MAX_VALUE;
        }
        
        public void push(int val) {
            if (stack.isEmpty()) {
                min = val;
                stack.push(0L); // разница 0
            } else {
                long diff = (long) val - min;
                stack.push(diff);
                if (diff < 0) {
                    min = val;
                }
            }
        }
        
        public void pop() {
            if (stack.isEmpty()) return;
            long diff = stack.pop();
            if (diff < 0) {
                min = min - diff;
            }
        }
        
        public int top() {
            long diff = stack.peek();
            if (diff < 0) {
                return (int) min;
            } else {
                return (int) (min + diff);
            }
        }
        
        public int getMin() {
            return (int) min;
        }
    }
    
    // Стек с максимумом (аналогично)
    public static class MaxStack {
        private Stack<Integer> stack;
        private Stack<Integer> maxStack;
        
        public MaxStack() {
            stack = new Stack<>();
            maxStack = new Stack<>();
        }
        
        public void push(int val) {
            stack.push(val);
            if (maxStack.isEmpty() || val >= maxStack.peek()) {
                maxStack.push(val);
            }
        }
        
        public void pop() {
            int val = stack.pop();
            if (val == maxStack.peek()) {
                maxStack.pop();
            }
        }
        
        public int top() {
            return stack.peek();
        }
        
        public int getMax() {
            return maxStack.peek();
        }
    }
    
    // Обобщенный стек с поддержкой min и max
    public static class MinMaxStack<T extends Comparable<T>> {
        private Stack<T> stack;
        private Stack<T> minStack;
        private Stack<T> maxStack;
        
        public MinMaxStack() {
            stack = new Stack<>();
            minStack = new Stack<>();
            maxStack = new Stack<>();
        }
        
        public void push(T val) {
            stack.push(val);
            
            if (minStack.isEmpty() || val.compareTo(minStack.peek()) <= 0) {
                minStack.push(val);
            } else {
                minStack.push(minStack.peek());
            }
            
            if (maxStack.isEmpty() || val.compareTo(maxStack.peek()) >= 0) {
                maxStack.push(val);
            } else {
                maxStack.push(maxStack.peek());
            }
        }
        
        public T pop() {
            minStack.pop();
            maxStack.pop();
            return stack.pop();
        }
        
        public T top() {
            return stack.peek();
        }
        
        public T getMin() {
            return minStack.peek();
        }
        
        public T getMax() {
            return maxStack.peek();
        }
        
        public boolean isEmpty() {
            return stack.isEmpty();
        }
    }
    
    // Пример использования
    public static void main(String[] args) {
        TwoStackMinStack minStack = new TwoStackMinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin()); // -3
        minStack.pop();
        System.out.println(minStack.top());    // 0
        System.out.println(minStack.getMin()); // -2
    }
}