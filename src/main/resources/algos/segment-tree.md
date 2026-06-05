# Дерево отрезков (Segment Tree)

## Intuition
Дерево отрезков — это структура данных, которая позволяет выполнять операции над диапазонами массива (например, сумма, минимум, максимум) за O(log n) время и обновлять отдельные элементы также за O(log n). Представьте, что у вас есть массив чисел, и вы часто хотите знать сумму на отрезке [l, r] или изменить значение элемента. Дерево отрезков хранит информацию о каждом отрезке в виде дерева, где листья — это отдельные элементы, а внутренние узлы — это результат операции над детьми.

## Approach
1. Дерево строится как полное бинарное дерево, хранящееся в массиве размера 4*n
2. **Построение**: рекурсивно строим дерево, для листьев записываем значение элемента, для внутренних узлов — результат операции над детьми
3. **Запрос на отрезке**: рекурсивно обходим дерево, если текущий отрезок полностью внутри запроса — возвращаем значение узла, если не пересекается — пропускаем, иначе рекурсивно идем в детей
4. **Обновление**: рекурсивно идем к листу, обновляем его значение, затем пересчитываем значения на пути к корню

## Complexity
- Time complexity (построение): **O(n)**
- Time complexity (запрос): **O(log n)**
- Time complexity (обновление): **O(log n)**
- Space complexity: **O(n)**

## Code

```java
public class SegmentTree {
    
    private int[] tree;
    private int[] arr;
    private int n;
    private Operation operation;
    
    // Типы операций
    public enum Operation {
        SUM, MIN, MAX, GCD, PRODUCT
    }
    
    public SegmentTree(int[] arr, Operation operation) {
        this.arr = arr;
        this.n = arr.length;
        this.operation = operation;
        this.tree = new int[4 * n];
        build(1, 0, n - 1);
    }
    
    // Построение дерева
    private void build(int node, int left, int right) {
        if (left == right) {
            tree[node] = arr[left];
            return;
        }
        
        int mid = (left + right) / 2;
        build(node * 2, left, mid);
        build(node * 2 + 1, mid + 1, right);
        tree[node] = combine(tree[node * 2], tree[node * 2 + 1]);
    }
    
    // Комбинирование двух значений согласно операции
    private int combine(int a, int b) {
        switch (operation) {
            case SUM: return a + b;
            case MIN: return Math.min(a, b);
            case MAX: return Math.max(a, b);
            case GCD: return gcd(a, b);
            case PRODUCT: return a * b;
            default: return a + b;
        }
    }
    
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }
    
    // Запрос на отрезке [ql, qr]
    public int query(int ql, int qr) {
        return query(1, 0, n - 1, ql, qr);
    }
    
    private int query(int node, int left, int right, int ql, int qr) {
        if (ql > right || qr < left) {
            // Нет пересечения
            return operation == Operation.MIN ? Integer.MAX_VALUE :
                   operation == Operation.MAX ? Integer.MIN_VALUE : 0;
        }
        
        if (ql <= left && right <= qr) {
            return tree[node];
        }
        
        int mid = (left + right) / 2;
        int leftResult = query(node * 2, left, mid, ql, qr);
        int rightResult = query(node * 2 + 1, mid + 1, right, ql, qr);
        
        if (operation == Operation.MIN) return Math.min(leftResult, rightResult);
        if (operation == Operation.MAX) return Math.max(leftResult, rightResult);
        if (operation == Operation.GCD) return gcd(leftResult, rightResult);
        return leftResult + rightResult; // SUM и PRODUCT
    }
    
    // Обновление одного элемента
    public void update(int index, int value) {
        arr[index] = value;
        update(1, 0, n - 1, index, value);
    }
    
    private void update(int node, int left, int right, int index, int value) {
        if (left == right) {
            tree[node] = value;
            return;
        }
        
        int mid = (left + right) / 2;
        if (index <= mid) {
            update(node * 2, left, mid, index, value);
        } else {
            update(node * 2 + 1, mid + 1, right, index, value);
        }
        tree[node] = combine(tree[node * 2], tree[node * 2 + 1]);
    }
    
    // Дерево отрезков с ленивой propag'ацией (для массовых обновлений)
    public static class LazySegmentTree {
        private int[] tree;
        private int[] lazy;
        private int n;
        private Operation operation;
        
        public LazySegmentTree(int[] arr, Operation operation) {
            this.n = arr.length;
            this.operation = operation;
            this.tree = new int[4 * n];
            this.lazy = new int[4 * n];
            build(1, 0, n - 1, arr);
        }
        
        private void build(int node, int left, int right, int[] arr) {
            if (left == right) {
                tree[node] = arr[left];
                return;
            }
            int mid = (left + right) / 2;
            build(node * 2, left, mid, arr);
            build(node * 2 + 1, mid + 1, right, arr);
            tree[node] = combine(tree[node * 2], tree[node * 2 + 1]);
        }
        
        private int combine(int a, int b) {
            return operation == Operation.SUM ? a + b : Math.min(a, b);
        }
        
        private void push(int node, int left, int right) {
            if (lazy[node] != 0) {
                tree[node] += lazy[node] * (right - left + 1);
                if (left != right) {
                    lazy[node * 2] += lazy[node];
                    lazy[node * 2 + 1] += lazy[node];
                }
                lazy[node] = 0;
            }
        }
        
        public void rangeUpdate(int ql, int qr, int delta) {
            rangeUpdate(1, 0, n - 1, ql, qr, delta);
        }
        
        private void rangeUpdate(int node, int left, int right, int ql, int qr, int delta) {
            push(node, left, right);
            
            if (ql > right || qr < left) return;
            
            if (ql <= left && right <= qr) {
                tree[node] += delta * (right - left + 1);
                if (left != right) {
                    lazy[node * 2] += delta;
                    lazy[node * 2 + 1] += delta;
                }
                return;
            }
            
            int mid = (left + right) / 2;
            rangeUpdate(node * 2, left, mid, ql, qr, delta);
            rangeUpdate(node * 2 + 1, mid + 1, right, ql, qr, delta);
            tree[node] = combine(tree[node * 2], tree[node * 2 + 1]);
        }
        
        public int rangeQuery(int ql, int qr) {
            return rangeQuery(1, 0, n - 1, ql, qr);
        }
        
        private int rangeQuery(int node, int left, int right, int ql, int qr) {
            push(node, left, right);
            
            if (ql > right || qr < left) return operation == Operation.SUM ? 0 : Integer.MAX_VALUE;
            if (ql <= left && right <= qr) return tree[node];
            
            int mid = (left + right) / 2;
            int leftResult = rangeQuery(node * 2, left, mid, ql, qr);
            int rightResult = rangeQuery(node * 2 + 1, mid + 1, right, ql, qr);
            
            return combine(leftResult, rightResult);
        }
    }
    
    // Дерево отрезков с указателями (для больших n или динамических данных)
    public static class DynamicSegmentTree {
        private static class Node {
            int value;
            Node left;
            Node right;
            
            Node(int value) {
                this.value = value;
            }
        }
        
        private Node root;
        private int n;
        private Operation operation;
        
        public DynamicSegmentTree(int size, Operation operation) {
            this.n = size;
            this.operation = operation;
            this.root = new Node(operation == Operation.SUM ? 0 : Integer.MAX_VALUE);
        }
        
        public void update(int index, int value) {
            root = update(root, 0, n - 1, index, value);
        }
        
        private Node update(Node node, int left, int right, int index, int value) {
            if (node == null) {
                node = new Node(operation == Operation.SUM ? 0 : Integer.MAX_VALUE);
            }
            
            if (left == right) {
                node.value = value;
                return node;
            }
            
            int mid = (left + right) / 2;
            if (index <= mid) {
                node.left = update(node.left, left, mid, index, value);
            } else {
                node.right = update(node.right, mid + 1, right, index, value);
            }
            
            int leftVal = node.left != null ? node.left.value : (operation == Operation.SUM ? 0 : Integer.MAX_VALUE);
            int rightVal = node.right != null ? node.right.value : (operation == Operation.SUM ? 0 : Integer.MAX_VALUE);
            node.value = operation == Operation.SUM ? leftVal + rightVal : Math.min(leftVal, rightVal);
            
            return node;
        }
        
        public int query(int ql, int qr) {
            return query(root, 0, n - 1, ql, qr);
        }
        
        private int query(Node node, int left, int right, int ql, int qr) {
            if (node == null) return operation == Operation.SUM ? 0 : Integer.MAX_VALUE;
            if (ql > right || qr < left) return operation == Operation.SUM ? 0 : Integer.MAX_VALUE;
            if (ql <= left && right <= qr) return node.value;
            
            int mid = (left + right) / 2;
            int leftResult = query(node.left, left, mid, ql, qr);
            int rightResult = query(node.right, mid + 1, right, ql, qr);
            
            return operation == Operation.SUM ? leftResult + rightResult : Math.min(leftResult, rightResult);
        }
    }
}