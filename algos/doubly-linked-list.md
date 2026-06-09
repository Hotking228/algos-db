# Двухсвязный список (Doubly Linked List)

## Intuition
Двухсвязный список — это линейная структура данных, где каждый узел содержит ссылки на следующий и предыдущий элементы. В отличие от односвязного списка, здесь можно перемещаться в обоих направлениях. Представьте поезд, где каждый вагон знает не только следующий вагон, но и предыдущий. Это позволяет легко вставлять и удалять элементы в любом месте списка, а также проходить список с конца в начало.

## Approach
1. Каждый узел содержит: данные, указатель на следующий узел (next), указатель на предыдущий узел (prev)
2. Голова (head) — первый узел списка, её prev = null
3. Хвост (tail) — последний узел списка, его next = null
4. **Вставка в начало**:
    - Создаем новый узел
    - Новый узел.next = head
    - Если head не null, head.prev = новый узел
    - head = новый узел
5. **Вставка в конец**:
    - Создаем новый узел
    - Если список пуст, head = tail = новый узел
    - Иначе tail.next = новый узел, новый узел.prev = tail, tail = новый узел
6. **Вставка в середину**:
    - Находим узел после которого вставляем
    - Перенастраиваем ссылки четырех узлов
7. **Удаление**:
    - Перенастраиваем ссылки соседних узлов, чтобы пропустить удаляемый
    - Если удаляем голову/хвост, обновляем head/tail

## Complexity
- Time complexity (вставка/удаление по позиции): **O(n)** (поиск позиции)
- Time complexity (вставка/удаление по указателю): **O(1)**
- Time complexity (доступ по индексу): **O(n)**
- Space complexity: **O(n)**

## Code

```java
public class DoublyLinkedList<T> {
    
    // Узел двусвязного списка
    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }
    
    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    
    // Добавление в начало
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    
    // Добавление в конец
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    
    // Добавление по индексу
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == 0) {
            addFirst(element);
            return;
        }
        
        if (index == size) {
            addLast(element);
            return;
        }
        
        Node<T> current = getNode(index);
        Node<T> newNode = new Node<>(element);
        
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev.next = newNode;
        current.prev = newNode;
        
        size++;
    }
    
    // Удаление первого элемента
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        
        T data = head.data;
        
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        
        size--;
        return data;
    }
    
    // Удаление последнего элемента
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        
        T data = tail.data;
        
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        
        size--;
        return data;
    }
    
    // Удаление по индексу
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == 0) {
            return removeFirst();
        }
        
        if (index == size - 1) {
            return removeLast();
        }
        
        Node<T> current = getNode(index);
        T data = current.data;
        
        current.prev.next = current.next;
        current.next.prev = current.prev;
        
        size--;
        return data;
    }
    
    // Удаление по значению (первое вхождение)
    public boolean removeFirstOccurrence(T element) {
        Node<T> current = head;
        
        while (current != null) {
            if (current.data.equals(element)) {
                if (current == head) {
                    removeFirst();
                } else if (current == tail) {
                    removeLast();
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    size--;
                }
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    // Получение элемента по индексу
    public T get(int index) {
        return getNode(index).data;
    }
    
    // Вспомогательный метод для получения узла по индексу
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node<T> current;
        // Оптимизация: идем с ближайшего конца
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }
    
    // Установка значения по индексу
    public T set(int index, T element) {
        Node<T> node = getNode(index);
        T oldData = node.data;
        node.data = element;
        return oldData;
    }
    
    // Проверка наличия элемента
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    // Индекс первого вхождения элемента
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }
    
    // Индекс последнего вхождения элемента
    public int lastIndexOf(T element) {
        Node<T> current = tail;
        int index = size - 1;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
    }
    
    // Разворот списка
    public void reverse() {
        Node<T> current = head;
        Node<T> temp = null;
        
        // Меняем местами next и prev для всех узлов
        while (current != null) {
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            current = current.prev;
        }
        
        // Меняем местами head и tail
        temp = head;
        head = tail;
        tail = temp;
    }
    
    // Обход от начала к концу
    public void printForward() {
        Node<T> current = head;
        System.out.print("[");
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) System.out.print(" <-> ");
            current = current.next;
        }
        System.out.println("]");
    }
    
    // Обход от конца к началу
    public void printBackward() {
        Node<T> current = tail;
        System.out.print("[");
        while (current != null) {
            System.out.print(current.data);
            if (current.prev != null) System.out.print(" <-> ");
            current = current.prev;
        }
        System.out.println("]");
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public T getFirst() {
        if (isEmpty()) return null;
        return head.data;
    }
    
    public T getLast() {
        if (isEmpty()) return null;
        return tail.data;
    }
    
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}