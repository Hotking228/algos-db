# Алгоритм Флойда для поиска цикла (Floyd's Cycle Detection - Hare and Tortoise)

## Intuition
Алгоритм Флойда (также известный как алгоритм "черепахи и зайца") — это эффективный алгоритм для обнаружения цикла в последовательности или связном списке. Используются два указателя: медленный (черепаха) движется на 1 шаг за раз, быстрый (заяц) — на 2 шага. Если есть цикл, они обязательно встретятся внутри него. Представьте двух бегунов на круговой дорожке: если один бежит в два раза быстрее другого, они обязательно встретятся. Алгоритм также может найти начало цикла.

## Approach
**Обнаружение цикла:**
1. Инициализируем два указателя: slow = head, fast = head
2. Пока fast и fast.next не null:
    - slow = slow.next (на 1 шаг)
    - fast = fast.next.next (на 2 шага)
    - Если slow == fast → цикл обнаружен

**Нахождение начала цикла:**
1. После обнаружения встречи перемещаем slow в начало
2. Двигаем оба указателя по 1 шагу
3. Когда они встретятся снова — это начало цикла

**Нахождение длины цикла:**
1. После встречи фиксируем один указатель
2. Двигаем второй до повторной встречи, считая шаги

## Complexity
- Time complexity: **O(n)** — два прохода в худшем случае
- Space complexity: **O(1)**

## Code

```java
import java.util.*;

public class FloydCycleDetection {
    
    // Класс для узла связного списка
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    
    // Проверка наличия цикла
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        
        return false;
    }
    
    // Нахождение начала цикла
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) return null;
        
        ListNode slow = head;
        ListNode fast = head;
        
        // Обнаружение цикла
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        
        if (fast == null || fast.next == null) return null;
        
        // Нахождение начала цикла
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        return slow;
    }
    
    // Нахождение длины цикла
    public static int cycleLength(ListNode head) {
        if (head == null || head.next == null) return 0;
        
        ListNode slow = head;
        ListNode fast = head;
        
        // Обнаружение цикла
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        
        if (fast == null || fast.next == null) return 0;
        
        // Вычисление длины
        int length = 0;
        ListNode current = slow;
        do {
            current = current.next;
            length++;
        } while (current != slow);
        
        return length;
    }
    
    // Для массива (задача Find Duplicate Number)
    public static int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];
        
        // Обнаружение цикла
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        
        // Нахождение входа в цикл (дубликата)
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        
        return slow;
    }
    
    // Для функции f(x) (общий случай)
    public static interface Function {
        int f(int x);
    }
    
    public static boolean hasCycle(int start, Function f) {
        int slow = start;
        int fast = start;
        
        do {
            slow = f.f(slow);
            fast = f.f(f.f(fast));
            if (slow == fast) return true;
        } while (slow != fast);
        
        return false;
    }
    
    public static int detectCycleStart(int start, Function f) {
        int slow = start;
        int fast = start;
        
        // Обнаружение цикла
        do {
            slow = f.f(slow);
            fast = f.f(f.f(fast));
            if (slow != fast) continue;
        } while (slow != fast);
        
        // Нахождение начала
        slow = start;
        while (slow != fast) {
            slow = f.f(slow);
            fast = f.f(fast);
        }
        
        return slow;
    }
    
    // Алгоритм Брента (альтернатива с меньшим количеством вычислений)
    public static class BrentCycleDetection {
        
        public static CycleInfo detect(ListNode head) {
            if (head == null) return new CycleInfo(false, null, 0, 0);
            
            ListNode slow = head;
            ListNode fast = head.next;
            int power = 1;
            int length = 1;
            
            while (fast != null && fast != slow) {
                if (length == power) {
                    slow = fast;
                    power *= 2;
                    length = 0;
                }
                fast = fast.next;
                length++;
            }
            
            if (fast == null) {
                return new CycleInfo(false, null, 0, 0);
            }
            
            // Нахождение начала
            slow = head;
            ListNode cycleStart = null;
            int steps = 0;
            
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
                steps++;
            }
            cycleStart = slow;
            
            return new CycleInfo(true, cycleStart, length, steps);
        }
        
        static class CycleInfo {
            boolean hasCycle;
            ListNode cycleStart;
            int cycleLength;
            int stepsToStart;
            
            CycleInfo(boolean hasCycle, ListNode cycleStart, int cycleLength, int stepsToStart) {
                this.hasCycle = hasCycle;
                this.cycleStart = cycleStart;
                this.cycleLength = cycleLength;
                this.stepsToStart = stepsToStart;
            }
        }
    }
    
    // Для последовательности с циклом (например, последовательность коллатца)
    public static class SequenceCycleDetector {
        
        public static CycleResult detectCycle(int[] sequence) {
            int n = sequence.length;
            int slow = 0;
            int fast = 0;
            
            // Обнаружение цикла
            do {
                slow = sequence[slow];
                fast = sequence[sequence[fast]];
            } while (slow != fast);
            
            // Нахождение начала цикла
            slow = 0;
            int startIndex = 0;
            while (slow != fast) {
                slow = sequence[slow];
                fast = sequence[fast];
                startIndex++;
            }
            
            // Вычисление длины
            int cycleLength = 1;
            fast = sequence[slow];
            while (slow != fast) {
                fast = sequence[fast];
                cycleLength++;
            }
            
            return new CycleResult(startIndex, cycleLength);
        }
        
        static class CycleResult {
            int startIndex;
            int cycleLength;
            
            CycleResult(int startIndex, int cycleLength) {
                this.startIndex = startIndex;
                this.cycleLength = cycleLength;
            }
        }
    }
    
    // Удаление цикла из связного списка
    public static void removeCycle(ListNode head) {
        ListNode cycleStart = detectCycleStart(head);
        if (cycleStart == null) return;
        
        ListNode current = cycleStart;
        while (current.next != cycleStart) {
            current = current.next;
        }
        current.next = null;
    }
    
    // Создание списка с циклом для тестирования
    public static ListNode createListWithCycle(int[] values, int cycleStartIndex) {
        if (values.length == 0) return null;
        
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        ListNode cycleStartNode = null;
        
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
            if (i == cycleStartIndex) {
                cycleStartNode = current;
            }
        }
        
        if (cycleStartIndex == 0) {
            cycleStartNode = head;
        }
        
        if (cycleStartNode != null) {
            current.next = cycleStartNode;
        }
        
        return head;
    }
}