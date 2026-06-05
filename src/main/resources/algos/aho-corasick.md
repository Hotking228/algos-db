# Алгоритм Ахо-Корасик (Aho-Corasick Algorithm)

## Intuition
Алгоритм Ахо-Корасик — это эффективный алгоритм для одновременного поиска множества образцов в тексте. Он строит автомат (конечный автомат с суффиксными ссылками) из всех образцов, а затем проходит по тексту один раз, переходя между состояниями. Представьте, что у вас есть словарь из нескольких слов, и вы хотите найти все их вхождения в книге. Вместо того чтобы искать каждое слово отдельно, вы строите "машину", которая одновременно отслеживает все возможные совпадения. Это похоже на то, как работает Ctrl+F для поиска нескольких слов сразу.

## Approach
1. **Построение бора (Trie)**: вставляем все образцы в префиксное дерево
2. **Построение суффиксных ссылок (fail links)**:
    - Для корня: все ссылки ведут на корень
    - Для каждого узла: идем по fail link родителя, затем по ребру
    - С помощью BFS заполняем fail ссылки для всех узлов
3. **Построение выходных ссылок (output links)**:
    - Каждый узел знает, какие образцы заканчиваются в нем или в его суффиксных ссылках
4. **Поиск**:
    - Идем по тексту символ за символом, переходя по автомату
    - При каждом переходе записываем все найденные образцы

## Complexity
- Time complexity (построение): **O(total length of patterns)**
- Time complexity (поиск): **O(n + m + matches)**, где n — длина текста, m — суммарная длина образцов
- Space complexity: **O(total length of patterns × алфавит)**

## Code

```java
import java.util.*;

public class AhoCorasick {
    
    // Узел автомата
    static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node failLink = null;
        List<String> outputs = new ArrayList<>(); // слова, заканчивающиеся здесь
        int depth = 0;
        
        Node() {}
        
        Node(int depth) {
            this.depth = depth;
        }
    }
    
    private Node root;
    private List<String> patterns;
    
    public AhoCorasick() {
        root = new Node(0);
        patterns = new ArrayList<>();
    }
    
    // Вставка одного образца
    public void addPattern(String pattern) {
        patterns.add(pattern);
        Node current = root;
        
        for (char c : pattern.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new Node(current.depth + 1));
        }
        
        current.outputs.add(pattern);
    }
    
    // Добавление нескольких образцов
    public void addPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            addPattern(pattern);
        }
    }
    
    // Построение fail ссылок (BFS)
    public void build() {
        Queue<Node> queue = new LinkedList<>();
        
        // Корневой уровень: failLink всех детей корня ведет на корень
        for (Map.Entry<Character, Node> entry : root.children.entrySet()) {
            Node child = entry.getValue();
            child.failLink = root;
            queue.offer(child);
        }
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                
                // Находим failLink для child
                Node failCandidate = current.failLink;
                while (failCandidate != null && !failCandidate.children.containsKey(c)) {
                    failCandidate = failCandidate.failLink;
                }
                
                if (failCandidate == null) {
                    child.failLink = root;
                } else {
                    child.failLink = failCandidate.children.get(c);
                    // Копируем outputs из failLink
                    child.outputs.addAll(child.failLink.outputs);
                }
                
                queue.offer(child);
            }
        }
    }
    
    // Поиск всех вхождений образцов в тексте
    public Map<String, List<Integer>> search(String text) {
        Map<String, List<Integer>> result = new HashMap<>();
        for (String pattern : patterns) {
            result.put(pattern, new ArrayList<>());
        }
        
        Node current = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            // Переход по автомату
            while (current != root && !current.children.containsKey(c)) {
                current = current.failLink;
            }
            
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
            }
            
            // Записываем все найденные образцы
            for (String pattern : current.outputs) {
                int startIndex = i - pattern.length() + 1;
                result.get(pattern).add(startIndex);
            }
        }
        
        return result;
    }
    
    // Поиск с подсчетом количества вхождений
    public Map<String, Integer> countOccurrences(String text) {
        Map<String, Integer> counts = new HashMap<>();
        for (String pattern : patterns) {
            counts.put(pattern, 0);
        }
        
        Node current = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            while (current != root && !current.children.containsKey(c)) {
                current = current.failLink;
            }
            
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
            }
            
            for (String pattern : current.outputs) {
                counts.put(pattern, counts.get(pattern) + 1);
            }
        }
        
        return counts;
    }
    
    // Поиск с игнорированием перекрывающихся вхождений (непересекающиеся)
    public Map<String, List<Integer>> searchNonOverlapping(String text) {
        Map<String, List<Integer>> result = new HashMap<>();
        for (String pattern : patterns) {
            result.put(pattern, new ArrayList<>());
        }
        
        Map<String, Integer> lastMatch = new HashMap<>();
        for (String pattern : patterns) {
            lastMatch.put(pattern, -pattern.length());
        }
        
        Node current = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            while (current != root && !current.children.containsKey(c)) {
                current = current.failLink;
            }
            
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
            }
            
            for (String pattern : current.outputs) {
                int startIndex = i - pattern.length() + 1;
                // Добавляем только если не перекрывается с предыдущим
                if (startIndex >= lastMatch.get(pattern) + pattern.length()) {
                    result.get(pattern).add(startIndex);
                    lastMatch.put(pattern, startIndex);
                }
            }
        }
        
        return result;
    }
    
    // Получение следующего состояния для данного символа (для ручного обхода)
    public Node getNextState(Node node, char c) {
        while (node != root && !node.children.containsKey(c)) {
            node = node.failLink;
        }
        
        if (node.children.containsKey(c)) {
            return node.children.get(c);
        }
        
        return root;
    }
    
    // Проверка, содержит ли текст хотя бы один образец
    public boolean containsAny(String text) {
        Node current = root;
        
        for (char c : text.toCharArray()) {
            current = getNextState(current, c);
            if (!current.outputs.isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    // Получение первого найденного образца
    public String findFirst(String text) {
        Node current = root;
        
        for (char c : text.toCharArray()) {
            current = getNextState(current, c);
            if (!current.outputs.isEmpty()) {
                return current.outputs.get(0);
            }
        }
        
        return null;
    }
    
    // Замена всех образцов на указанную строку
    public String replaceAll(String text, String replacement) {
        if (!containsAny(text)) return text;
        
        StringBuilder result = new StringBuilder();
        Node current = root;
        int lastPos = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            current = getNextState(current, c);
            
            if (!current.outputs.isEmpty()) {
                String matched = current.outputs.get(0);
                int start = i - matched.length() + 1;
                if (start >= lastPos) {
                    result.append(text, lastPos, start);
                    result.append(replacement);
                    lastPos = i + 1;
                }
            }
        }
        
        result.append(text, lastPos, text.length());
        return result.toString();
    }
    
    // Очистка автомата
    public void clear() {
        root = new Node(0);
        patterns.clear();
    }
    
    // Получение количества узлов в автомате
    public int getNodeCount() {
        return countNodes(root);
    }
    
    private int countNodes(Node node) {
        int count = 1;
        for (Node child : node.children.values()) {
            count += countNodes(child);
        }
        return count;
    }
    
    // Печать автомата (для отладки)
    public void printAutomaton() {
        printNode(root, 0);
    }
    
    private void printNode(Node node, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "Node (depth=" + node.depth + 
                          ", outputs=" + node.outputs + ")");
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            System.out.print(indent + "  '" + entry.getKey() + "' -> ");
            printNode(entry.getValue(), level + 1);
        }
    }
}