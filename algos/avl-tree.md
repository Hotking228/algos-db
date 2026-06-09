# АВЛ-дерево (AVL Tree)

## Intuition
АВЛ-дерево — это самобалансирующееся бинарное дерево поиска, в котором для каждого узла высоты левого и правого поддеревьев отличаются не более чем на 1. Название происходит от фамилий изобретателей Адельсона-Вельского и Ландиса. Представьте, что обычное BST может выродиться в линейный список (например, при вставке отсортированных данных). АВЛ-дерево автоматически перестраивается с помощью вращений, чтобы оставаться сбалансированным, гарантируя высоту O(log n) и, следовательно, логарифмическое время операций.

## Approach
1. **Баланс-фактор** для узла = высота(правого поддерева) - высота(левого поддерева)
2. Допустимые значения баланс-фактора: -1, 0, 1
3. **Вставка**:
    - Вставляем узел как в обычное BST
    - Обновляем высоты узлов на пути к корню
    - Проверяем баланс-фактор каждого узла
    - При нарушении баланса выполняем одно из 4 вращений:
        - **LL** (левый-левый): правый поворот
        - **RR** (правый-правый): левый поворот
        - **LR** (левый-правый): левый поворот вокруг левого ребенка + правый поворот
        - **RL** (правый-левый): правый поворот вокруг правого ребенка + левый поворот
4. **Удаление**:
    - Удаляем узел как в BST
    - Обновляем высоты и балансируем все узлы на пути к корню

## Complexity
- Time complexity (вставка): **O(log n)**
- Time complexity (удаление): **O(log n)**
- Time complexity (поиск): **O(log n)**
- Space complexity: **O(n)**

## Code

```java
public class AVLTree<K extends Comparable<K>, V> {
    
    // Узел АВЛ-дерева
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        int height;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }
    
    private Node<K, V> root;
    private int size;
    
    public AVLTree() {
        root = null;
        size = 0;
    }
    
    // Получение высоты узла
    private int height(Node<K, V> node) {
        return node == null ? 0 : node.height;
    }
    
    // Обновление высоты узла
    private void updateHeight(Node<K, V> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }
    
    // Получение баланс-фактора
    private int getBalance(Node<K, V> node) {
        return node == null ? 0 : height(node.right) - height(node.left);
    }
    
    // Правый поворот (LL)
    private Node<K, V> rotateRight(Node<K, V> y) {
        Node<K, V> x = y.left;
        Node<K, V> T2 = x.right;
        
        // Выполняем поворот
        x.right = y;
        y.left = T2;
        
        // Обновляем высоты
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    // Левый поворот (RR)
    private Node<K, V> rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        Node<K, V> T2 = y.left;
        
        // Выполняем поворот
        y.left = x;
        x.right = T2;
        
        // Обновляем высоты
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    // Балансировка узла
    private Node<K, V> balance(Node<K, V> node) {
        if (node == null) return null;
        
        updateHeight(node);
        int balance = getBalance(node);
        
        // LL: левое поддерево выше на 2
        if (balance < -1 && getBalance(node.left) <= 0) {
            return rotateRight(node);
        }
        
        // LR: левое поддерево выше, но его правое поддерево выше
        if (balance < -1 && getBalance(node.left) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // RR: правое поддерево выше на 2
        if (balance > 1 && getBalance(node.right) >= 0) {
            return rotateLeft(node);
        }
        
        // RL: правое поддерево выше, но его левое поддерево выше
        if (balance > 1 && getBalance(node.right) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    // Вставка пары ключ-значение
    public void put(K key, V value) {
        root = putRec(root, key, value);
    }
    
    private Node<K, V> putRec(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRec(node.right, key, value);
        } else {
            // Ключ уже существует, обновляем значение
            node.value = value;
            return node;
        }
        
        return balance(node);
    }
    
    // Получение значения по ключу
    public V get(K key) {
        Node<K, V> node = getNode(root, key);
        return node == null ? null : node.value;
    }
    
    private Node<K, V> getNode(Node<K, V> node, K key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return getNode(node.left, key);
        } else if (cmp > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }
    
    // Поиск минимального ключа
    public K findMin() {
        if (root == null) return null;
        Node<K, V> min = findMinNode(root);
        return min.key;
    }
    
    private Node<K, V> findMinNode(Node<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    // Поиск максимального ключа
    public K findMax() {
        if (root == null) return null;
        Node<K, V> max = findMaxNode(root);
        return max.key;
    }
    
    private Node<K, V> findMaxNode(Node<K, V> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
    
    // Удаление по ключу
    public void delete(K key) {
        root = deleteRec(root, key);
    }
    
    private Node<K, V> deleteRec(Node<K, V> node, K key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRec(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            // Нашли узел для удаления
            if (node.left == null && node.right == null) {
                // Лист
                size--;
                return null;
            } else if (node.left == null) {
                // Только правый ребенок
                size--;
                return node.right;
            } else if (node.right == null) {
                // Только левый ребенок
                size--;
                return node.left;
            } else {
                // Два ребенка: находим преемника (минимальный в правом поддереве)
                Node<K, V> successor = findMinNode(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = deleteRec(node.right, successor.key);
            }
        }
        
        return balance(node);
    }
    
    // Проверка, содержит ли дерево ключ
    public boolean containsKey(K key) {
        return getNode(root, key) != null;
    }
    
    // Обход в порядке возрастания
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(Node<K, V> node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.print(node.key + ":" + node.value + " ");
            inorderRec(node.right);
        }
    }
    
    // Получение высоты дерева
    public int getHeight() {
        return height(root);
    }
    
    // Проверка, сбалансировано ли дерево
    public boolean isBalanced() {
        return isBalancedRec(root);
    }
    
    private boolean isBalancedRec(Node<K, V> node) {
        if (node == null) return true;
        
        int balance = getBalance(node);
        if (balance < -1 || balance > 1) return false;
        
        return isBalancedRec(node.left) && isBalancedRec(node.right);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}