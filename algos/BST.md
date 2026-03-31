# Двоичное дерево поиска (BST)

## Intuition
Двоичное дерево поиска — это структура данных, которая сочетает гибкость связного списка с эффективностью бинарного поиска в массиве. Основная идея: для каждого узла все значения в левом поддереве меньше значения узла, а все значения в правом поддереве — больше. Это свойство позволяет быстро находить элементы, каждый раз отбрасывая половину дерева.

## Approach
1. Каждый узел содержит значение, ссылки на левого и правого потомка
2. **Поиск**: начинаем с корня, сравниваем с искомым значением:
    - Если равно — нашли
    - Если меньше — идём влево
    - Если больше — идём вправо
3. **Вставка**: аналогично поиску, ищем место и вставляем новый узел
4. **Удаление**: самый сложный случай, нужно рассмотреть 3 варианта:
    - Удаляемый узел не имеет детей
    - Удаляемый узел имеет одного ребёнка
    - Удаляемый узел имеет двух детей (заменяем на наименьший элемент в правом поддереве)

## Complexity
- Time complexity:
    - В среднем: $$O(\log n)$$ для поиска, вставки, удаления
    - В худшем случае: $$O(n)$$ (если дерево вырождается в связный список)

- Space complexity: $$O(n)$$ для хранения всех узлов

## Code
```java
public class BST {
    class Node {
        int val;
        Node left;
        Node right;
        
        Node(int val) {
            this.val = val;
        }
    }
    
    private Node root;
    
    // Вставка
    public void insert(int val) {
        root = insertRec(root, val);
    }
    
    private Node insertRec(Node root, int val) {
        if (root == null) {
            return new Node(val);
        }
        
        if (val < root.val) {
            root.left = insertRec(root.left, val);
        } else if (val > root.val) {
            root.right = insertRec(root.right, val);
        }
        
        return root;
    }
    
    // Поиск
    public boolean search(int val) {
        return searchRec(root, val);
    }
    
    private boolean searchRec(Node root, int val) {
        if (root == null) {
            return false;
        }
        
        if (root.val == val) {
            return true;
        }
        
        if (val < root.val) {
            return searchRec(root.left, val);
        } else {
            return searchRec(root.right, val);
        }
    }
    
    // Поиск (итеративный)
    public boolean searchIterative(int val) {
        Node current = root;
        
        while (current != null) {
            if (current.val == val) {
                return true;
            } else if (val < current.val) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        
        return false;
    }
    
    // Удаление
    public void delete(int val) {
        root = deleteRec(root, val);
    }
    
    private Node deleteRec(Node root, int val) {
        if (root == null) {
            return null;
        }
        
        if (val < root.val) {
            root.left = deleteRec(root.left, val);
        } else if (val > root.val) {
            root.right = deleteRec(root.right, val);
        } else {
            // Нашли узел для удаления
            
            // Случай 1: нет детей
            if (root.left == null && root.right == null) {
                return null;
            }
            
            // Случай 2: один ребёнок
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            
            // Случай 3: два ребёнка
            // Находим наименьший элемент в правом поддереве
            Node successor = findMin(root.right);
            root.val = successor.val;
            // Удаляем этот наименьший элемент
            root.right = deleteRec(root.right, successor.val);
        }
        
        return root;
    }
    
    private Node findMin(Node root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
    
    // Обходы дерева
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.val + " ");
            inorderRec(root.right);
        }
    }
    
    // Нахождение минимального значения
    public int findMin() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.val;
    }
    
    // Нахождение максимального значения
    public int findMax() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.val;
    }
}
```