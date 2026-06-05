# Декартово дерево по неявному ключу (Implicit Treap)

## Intuition
Декартово дерево по неявному ключу (Implicit Treap) — это мощная структура данных, которая работает с массивом как с последовательностью, поддерживая операции вставки, удаления, разворота подотрезка и другие за O(log n). В отличие от обычного декартова дерева, здесь ключом является не значение, а позиция элемента в последовательности (неявно). Структура поддерживает инвариант: при обходе in-order получаем исходную последовательность. Это позволяет выполнять операции над отрезками так же легко, как со связным списком, но за логарифмическое время.

## Approach
1. Каждый узел хранит значение, случайный приоритет, размер поддерева и флаг реверса
2. **Операция split(root, k)** — разделяет дерево на два:
    - Левое содержит первые k элементов
    - Правое — остальные
3. **Операция merge(left, right)** — объединяет два дерева (все ключи left < all keys right)
4. **Вставка** на позицию k: split(root, k) → left, right, затем merge(left, newNode), merge(result, right)
5. **Удаление** позиции k: split(root, k) → left, mid, split(mid, 1) → mid, right, merge(left, right)
6. **Разворот отрезка [l, r]**:
    - split(root, l) → left, mid
    - split(mid, r - l + 1) → mid, right
    - Устанавливаем флаг реверса на mid
    - merge(left, merge(mid, right))
7. Ленивая propag'ация: при push(node) меняем местами левого и правого ребенка и передаем флаг детям

## Complexity
- Time complexity (вставка, удаление, разворот, запрос): **O(log n)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class ImplicitTreap {
    
    private static class Node {
        int value;
        int priority;
        int size;
        Node left;
        Node right;
        boolean rev; // флаг разворота
        
        Node(int value) {
            this.value = value;
            this.priority = new Random().nextInt();
            this.size = 1;
            this.left = null;
            this.right = null;
            this.rev = false;
        }
    }
    
    private Node root;
    private Random random;
    
    public ImplicitTreap() {
        this.root = null;
        this.random = new Random();
    }
    
    // Получение размера поддерева
    private int getSize(Node node) {
        return node == null ? 0 : node.size;
    }
    
    // Обновление размера узла
    private void updateSize(Node node) {
        if (node != null) {
            node.size = getSize(node.left) + 1 + getSize(node.right);
        }
    }
    
    // Проталкивание флага реверса
    private void push(Node node) {
        if (node != null && node.rev) {
            node.rev = false;
            Node temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) node.left.rev ^= true;
            if (node.right != null) node.right.rev ^= true;
        }
    }
    
    // Разделение дерева на два
    private Node[] split(Node node, int k) {
        if (node == null) {
            return new Node[]{null, null};
        }
        
        push(node);
        
        int leftSize = getSize(node.left);
        
        if (k <= leftSize) {
            Node[] leftRight = split(node.left, k);
            node.left = leftRight[1];
            updateSize(node);
            return new Node[]{leftRight[0], node};
        } else {
            Node[] leftRight = split(node.right, k - leftSize - 1);
            node.right = leftRight[0];
            updateSize(node);
            return new Node[]{node, leftRight[1]};
        }
    }
    
    // Слияние двух деревьев
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        push(left);
        push(right);
        
        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            updateSize(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            updateSize(right);
            return right;
        }
    }
    
    // Вставка элемента в позицию pos (0-индексация)
    public void insert(int pos, int value) {
        Node newNode = new Node(value);
        Node[] leftRight = split(root, pos);
        root = merge(merge(leftRight[0], newNode), leftRight[1]);
    }
    
    // Удаление элемента в позиции pos
    public void remove(int pos) {
        Node[] leftRight = split(root, pos);
        Node[] midRight = split(leftRight[1], 1);
        root = merge(leftRight[0], midRight[1]);
    }
    
    // Разворот отрезка [l, r]
    public void reverse(int l, int r) {
        Node[] leftMid = split(root, l);
        Node[] midRight = split(leftMid[1], r - l + 1);
        
        if (midRight[0] != null) {
            midRight[0].rev ^= true;
        }
        
        root = merge(leftMid[0], merge(midRight[0], midRight[1]));
    }
    
    // Получение массива в текущем порядке
    public List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }
    
    private void inorder(Node node, List<Integer> result) {
        if (node == null) return;
        
        push(node);
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }
    
    // Получение элемента по индексу
    public int get(int index) {
        Node[] leftRight = split(root, index);
        Node[] midRight = split(leftRight[1], 1);
        int value = midRight[0].value;
        root = merge(leftRight[0], merge(midRight[0], midRight[1]));
        return value;
    }
    
    // Установка значения по индексу
    public void set(int index, int value) {
        Node[] leftRight = split(root, index);
        Node[] midRight = split(leftRight[1], 1);
        if (midRight[0] != null) {
            midRight[0].value = value;
        }
        root = merge(leftRight[0], merge(midRight[0], midRight[1]));
    }
    
    // Размер дерева
    public int size() {
        return getSize(root);
    }
    
    // Вставка массива в позицию pos
    public void insertArray(int pos, int[] arr) {
        Node newTree = buildFromArray(arr, 0, arr.length - 1);
        Node[] leftRight = split(root, pos);
        root = merge(merge(leftRight[0], newTree), leftRight[1]);
    }
    
    // Построение сбалансированного дерева из массива
    private Node buildFromArray(int[] arr, int l, int r) {
        if (l > r) return null;
        int mid = (l + r) / 2;
        Node node = new Node(arr[mid]);
        node.left = buildFromArray(arr, l, mid - 1);
        node.right = buildFromArray(arr, mid + 1, r);
        updateSize(node);
        return node;
    }
    
    // Применение операции к отрезку (пример: сумма)
    // Можно модифицировать для хранения дополнительной информации в узле
    private static class SumNode extends Node {
        long sum;
        
        SumNode(int value) {
            super(value);
            this.sum = value;
        }
    }
    
    private void updateSum(SumNode node) {
        if (node == null) return;
        node.size = getSize(node.left) + 1 + getSize(node.right);
        node.sum = (node.left != null ? ((SumNode) node.left).sum : 0) +
                   node.value +
                   (node.right != null ? ((SumNode) node.right).sum : 0);
    }
    
    // Вращение отрезка (циклический сдвиг)
    public void rotate(int l, int r, int k) {
        int length = r - l + 1;
        k = ((k % length) + length) % length;
        if (k == 0) return;
        
        Node[] leftMid = split(root, l);
        Node[] midRight = split(leftMid[1], length);
        Node[] leftPart = split(midRight[0], length - k);
        
        root = merge(leftMid[0], 
                     merge(leftPart[1], 
                           merge(leftPart[0], midRight[1])));
    }
    
    // Печать дерева (для отладки)
    public void printTree() {
        printTree(root, 0);
        System.out.println();
    }
    
    private void printTree(Node node, int depth) {
        if (node == null) return;
        printTree(node.right, depth + 1);
        System.out.println("  ".repeat(depth) + node.value + "(" + node.priority + ", sz=" + node.size + ")");
        printTree(node.left, depth + 1);
    }
}