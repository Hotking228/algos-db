# Красно-черное дерево (Red-Black Tree)

## Intuition
Красно-черное дерево — это самобалансирующееся бинарное дерево поиска, которое гарантирует выполнение операций за O(log n) в худшем случае. Каждый узел имеет цвет (красный или черный), и дерево поддерживает пять свойств, которые обеспечивают его сбалансированность: корень всегда черный, красные узлы не могут иметь красных детей, все пути от корня до листьев содержат одинаковое количество черных узлов. Представьте, что это BST, которое автоматически перестраивается при вставке и удалении с помощью вращений и перекрашиваний, чтобы дерево оставалось примерно сбалансированным.

## Approach
1. Каждый узел имеет цвет: красный (red) или черный (black)
2. **Свойства красно-черного дерева**:
    - Каждый узел либо красный, либо черный
    - Корень всегда черный
    - Все листья (null) считаются черными
    - Красный узел не может иметь красного родителя (нет двух красных подряд)
    - Каждый путь от корня до листа содержит одинаковое количество черных узлов
3. **Вставка**:
    - Вставляем новый узел как красный (как в обычное BST)
    - Если нарушено свойство (родитель тоже красный), восстанавливаем:
        - Если дядя красный → перекрашиваем
        - Если дядя черный → делаем вращения
4. **Удаление**:
    - Удаляем узел как в BST
    - Если удалили черный узел, восстанавливаем свойства с помощью перекрашиваний и вращений

## Complexity
- Time complexity (вставка): **O(log n)**
- Time complexity (удаление): **O(log n)**
- Time complexity (поиск): **O(log n)**
- Space complexity: **O(n)**

## Code

```java
public class RedBlackTree<K extends Comparable<K>, V> {
    
    // Цвета узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    
    // Узел дерева
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;
        boolean color;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = RED; // Новый узел всегда красный
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }
    
    private Node<K, V> root;
    private int size;
    
    public RedBlackTree() {
        root = null;
        size = 0;
    }
    
    // Вспомогательные методы для работы с цветом
    private boolean isRed(Node<K, V> node) {
        return node != null && node.color == RED;
    }
    
    private boolean isBlack(Node<K, V> node) {
        return node == null || node.color == BLACK;
    }
    
    private void setColor(Node<K, V> node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }
    
    // Левый поворот
    private void rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        x.right = y.left;
        
        if (y.left != null) {
            y.left.parent = x;
        }
        
        y.parent = x.parent;
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        y.left = x;
        x.parent = y;
    }
    
    // Правый поворот
    private void rotateRight(Node<K, V> x) {
        Node<K, V> y = x.left;
        x.left = y.right;
        
        if (y.right != null) {
            y.right.parent = x;
        }
        
        y.parent = x.parent;
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        
        y.right = x;
        x.parent = y;
    }
    
    // Вставка пары ключ-значение
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        insertNode(node);
        fixInsertion(node);
        size++;
    }
    
    // Вставка узла как в обычное BST
    private void insertNode(Node<K, V> node) {
        Node<K, V> current = root;
        Node<K, V> parent = null;
        
        while (current != null) {
            parent = current;
            int cmp = node.key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Ключ уже существует, обновляем значение
                current.value = node.value;
                return;
            }
        }
        
        node.parent = parent;
        
        if (parent == null) {
            root = node;
        } else {
            int cmp = node.key.compareTo(parent.key);
            if (cmp < 0) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        }
    }
    
    // Восстановление свойств после вставки
    private void fixInsertion(Node<K, V> node) {
        while (node != root && isRed(node.parent)) {
            Node<K, V> parent = node.parent;
            Node<K, V> grandparent = parent.parent;
            
            // Случай A: родитель - левый ребенок
            if (parent == grandparent.left) {
                Node<K, V> uncle = grandparent.right;
                
                // Случай 1: дядя красный - перекрашивание
                if (isRed(uncle)) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);
                    node = grandparent;
                } 
                // Случай 2: дядя черный и node - правый ребенок
                else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.parent;
                        grandparent = parent.parent;
                    }
                    // Случай 3: дядя черный и node - левый ребенок
                    setColor(parent, BLACK);
                    setColor(grandparent, RED);
                    rotateRight(grandparent);
                }
            } 
            // Случай B: родитель - правый ребенок (симметрично)
            else {
                Node<K, V> uncle = grandparent.left;
                
                if (isRed(uncle)) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                        parent = node.parent;
                        grandparent = parent.parent;
                    }
                    setColor(parent, BLACK);
                    setColor(grandparent, RED);
                    rotateLeft(grandparent);
                }
            }
        }
        setColor(root, BLACK);
    }
    
    // Получение значения по ключу
    public V get(K key) {
        Node<K, V> node = getNode(key);
        return node != null ? node.value : null;
    }
    
    private Node<K, V> getNode(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }
    
    // Удаление по ключу
    public void delete(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) return;
        
        deleteNode(node);
        size--;
    }
    
    private void deleteNode(Node<K, V> node) {
        Node<K, V> toDelete = node;
        Node<K, V> child;
        boolean originalColor;
        
        // Если нет левого ребенка, заменяем правым
        if (node.left == null) {
            child = node.right;
            transplant(node, node.right);
            originalColor = node.color;
        } 
        // Если нет правого ребенка, заменяем левым
        else if (node.right == null) {
            child = node.left;
            transplant(node, node.left);
            originalColor = node.color;
        } 
        // Есть оба ребенка - находим преемника
        else {
            Node<K, V> successor = findMin(node.right);
            originalColor = successor.color;
            child = successor.right;
            
            if (successor.parent == node) {
                if (child != null) {
                    child.parent = successor;
                }
            } else {
                transplant(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }
            
            transplant(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
            successor.color = node.color;
        }
        
        // Если удалили черный узел, восстанавливаем свойства
        if (originalColor == BLACK) {
            fixDeletion(child);
        }
    }
    
    // Замена одного узла другим
    private void transplant(Node<K, V> u, Node<K, V> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }
    
    // Поиск минимального узла в поддереве
    private Node<K, V> findMin(Node<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    // Восстановление свойств после удаления
    private void fixDeletion(Node<K, V> node) {
        while (node != root && isBlack(node)) {
            if (node == node.parent.left) {
                Node<K, V> sibling = node.parent.right;
                
                // Случай 1: брат красный
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(node.parent, RED);
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                
                // Случай 2: оба ребенка брата черные
                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    setColor(sibling, RED);
                    node = node.parent;
                } 
                // Случай 3: левый ребенок брата красный, правый черный
                else {
                    if (isBlack(sibling.right)) {
                        setColor(sibling.left, BLACK);
                        setColor(sibling, RED);
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    // Случай 4: правый ребенок брата красный
                    setColor(sibling, node.parent.color);
                    setColor(node.parent, BLACK);
                    setColor(sibling.right, BLACK);
                    rotateLeft(node.parent);
                    node = root;
                }
            } 
            // Симметричный случай для правого ребенка
            else {
                Node<K, V> sibling = node.parent.left;
                
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(node.parent, RED);
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                
                if (isBlack(sibling.right) && isBlack(sibling.left)) {
                    setColor(sibling, RED);
                    node = node.parent;
                } else {
                    if (isBlack(sibling.left)) {
                        setColor(sibling.right, BLACK);
                        setColor(sibling, RED);
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    setColor(sibling, node.parent.color);
                    setColor(node.parent, BLACK);
                    setColor(sibling.left, BLACK);
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        if (node != null) {
            setColor(node, BLACK);
        }
    }
    
    // Обход в порядке возрастания (In-order)
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
    
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}