# Декартово дерево (Treap)

## Intuition
Treap (декартово дерево) — это структура данных, которая объединяет свойства бинарного дерева поиска (BST) и кучи. Название происходит от комбинации Tree + Heap. Каждый узел содержит ключ (удовлетворяющий свойству BST) и приоритет (удовлетворяющий свойству кучи). Приоритеты обычно генерируются случайно, что делает дерево сбалансированным в среднем. Представьте, что вы вставляете узлы в обычное BST, но каждый узел получает случайный "вес", и вы вращаете дерево, чтобы сохранить heap-свойство по этому весу.

## Approach
1. Каждый узел имеет ключ (уникальный) и приоритет (случайный)
2. Дерево является BST по ключу: левые дети меньше, правые — больше
3. Дерево является max-кучей по приоритету: родитель имеет больший приоритет, чем дети
4. **Вставка**:
    - Вставляем новый узел как в обычное BST
    - Генерируем случайный приоритет
    - Поднимаем узел вверх вращениями, чтобы восстановить heap-свойство
5. **Удаление**:
    - Вращаем удаляемый узел вниз, пока он не станет листом
    - Удаляем его
6. **Разделение (split)**:
    - Разделяем дерево на два по ключу k: левое содержит ключи ≤ k, правое — > k
7. **Слияние (merge)**:
    - Объединяем два дерева, где все ключи первого меньше всех ключей второго

## Complexity
- Time complexity (вставка): **O(log n)** в среднем
- Time complexity (удаление): **O(log n)** в среднем
- Time complexity (поиск): **O(log n)** в среднем
- Time complexity (split/merge): **O(log n)** в среднем
- Space complexity: **O(n)**

## Code

```java
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Treap {
    
    // Узел декартова дерева
    static class Node {
        int key;
        int priority;
        Node left;
        Node right;
        
        Node(int key) {
            this.key = key;
            this.priority = new Random().nextInt();
            this.left = null;
            this.right = null;
        }
        
        Node(int key, int priority) {
            this.key = key;
            this.priority = priority;
            this.left = null;
            this.right = null;
        }
    }
    
    private Node root;
    private Random random;
    
    public Treap() {
        root = null;
        random = new Random();
    }
    
    // Вращение вправо
    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        return q;
    }
    
    // Вращение влево
    private Node rotateLeft(Node p) {
        Node q = p.right;
        p.right = q.left;
        q.left = p;
        return q;
    }
    
    // Вставка узла
    public void insert(int key) {
        root = insertRec(root, key);
    }
    
    private Node insertRec(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }
        
        if (key < node.key) {
            node.left = insertRec(node.left, key);
            if (node.left.priority > node.priority) {
                node = rotateRight(node);
            }
        } else if (key > node.key) {
            node.right = insertRec(node.right, key);
            if (node.right.priority > node.priority) {
                node = rotateLeft(node);
            }
        }
        // Если key == node.key, ничего не делаем (ключи уникальны)
        
        return node;
    }
    
    // Удаление узла
    public void delete(int key) {
        root = deleteRec(root, key);
    }
    
    private Node deleteRec(Node node, int key) {
        if (node == null) {
            return null;
        }
        
        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            // Нашли узел для удаления
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            
            // Вращаем узел вниз
            if (node.left.priority > node.right.priority) {
                node = rotateRight(node);
                node.right = deleteRec(node.right, key);
            } else {
                node = rotateLeft(node);
                node.left = deleteRec(node.left, key);
            }
        }
        
        return node;
    }
    
    // Поиск элемента
    public boolean search(int key) {
        Node current = root;
        while (current != null) {
            if (key == current.key) {
                return true;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }
    
    // Разделение дерева по ключу
    public Node[] split(int key) {
        return splitRec(root, key);
    }
    
    private Node[] splitRec(Node node, int key) {
        if (node == null) {
            return new Node[]{null, null};
        }
        
        if (node.key <= key) {
            Node[] rightSplit = splitRec(node.right, key);
            node.right = rightSplit[0];
            return new Node[]{node, rightSplit[1]};
        } else {
            Node[] leftSplit = splitRec(node.left, key);
            node.left = leftSplit[1];
            return new Node[]{leftSplit[0], node};
        }
    }
    
    // Слияние двух деревьев (все ключи leftTree < все ключи rightTree)
    public Node merge(Node leftTree, Node rightTree) {
        if (leftTree == null) return rightTree;
        if (rightTree == null) return leftTree;
        
        if (leftTree.priority > rightTree.priority) {
            leftTree.right = merge(leftTree.right, rightTree);
            return leftTree;
        } else {
            rightTree.left = merge(leftTree, rightTree.left);
            return rightTree;
        }
    }
    
    // Нахождение минимального ключа
    public int findMin() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }
    
    // Нахождение максимального ключа
    public int findMax() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }
    
    // Получение следующего элемента (больше заданного)
    public Integer successor(int key) {
        Node current = root;
        Integer successor = null;
        
        while (current != null) {
            if (current.key > key) {
                successor = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        
        return successor;
    }
    
    // Получение предыдущего элемента (меньше заданного)
    public Integer predecessor(int key) {
        Node current = root;
        Integer predecessor = null;
        
        while (current != null) {
            if (current.key < key) {
                predecessor = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        
        return predecessor;
    }
    
    // Обход в порядке возрастания (In-order traversal)
    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }
    
    private void inorderRec(Node node, List<Integer> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.key);
            inorderRec(node.right, result);
        }
    }
    
    // Печать дерева (для отладки)
    public void printTree() {
        printTreeRec(root, 0);
    }
    
    private void printTreeRec(Node node, int level) {
        if (node != null) {
            printTreeRec(node.right, level + 1);
            System.out.println("  ".repeat(level) + node.key + "(" + node.priority + ")");
            printTreeRec(node.left, level + 1);
        }
    }
    
    public boolean isEmpty() {
        return root == null;
    }
}