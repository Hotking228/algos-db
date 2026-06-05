# Префиксное дерево (Trie)

## Intuition
Trie (префиксное дерево) — это структура данных для эффективного хранения и поиска строк, особенно полезная для автодополнения, проверки орфографии и поиска по префиксу. Представьте себе дерево, где каждый путь от корня до листа образует слово, а каждый узел хранит одну букву. Например, слова "cat" и "car" будут иметь общий префикс "ca", а затем разветвляться на "t" и "r".

## Approach
1. Корневой узел не содержит символа (пустая строка)
2. Каждый узел содержит массив/словарь детей (по одному на каждую букву алфавита) и флаг конца слова
3. **Вставка**: идем по символам слова, создавая новые узлы при необходимости, в конце помечаем узел как конец слова
4. **Поиск**: идем по символам слова, если на каком-то шаге нужного ребенка нет — слово отсутствует
5. **Поиск по префиксу**: аналогично поиску, но не проверяем флаг конца слова

## Complexity
- Time complexity (вставка): **O(L)**, где L — длина слова
- Time complexity (поиск): **O(L)**, где L — длина слова
- Time complexity (поиск по префиксу): **O(P)**, где P — длина префикса
- Space complexity: **O(N * L)**, где N — количество слов, L — средняя длина слова

## Code

### Реализация для строчных латинских букв
```java
public class Trie {
    
    // Узел префиксного дерева
    static class TrieNode {
        private TrieNode[] children;
        private boolean isEndOfWord;
        
        public TrieNode() {
            children = new TrieNode[26]; // только a-z
            isEndOfWord = false;
        }
    }
    
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    // Вставка слова
    public void insert(String word) {
        TrieNode current = root;
        
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }
    
    // Поиск точного слова
    public boolean search(String word) {
        TrieNode node = searchNode(word);
        return node != null && node.isEndOfWord;
    }
    
    // Проверка наличия префикса
    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }
    
    // Вспомогательный метод для поиска узла
    private TrieNode searchNode(String str) {
        TrieNode current = root;
        
        for (int i = 0; i < str.length(); i++) {
            int index = str.charAt(i) - 'a';
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current;
    }
    
    // Удаление слова из дерева
    public boolean delete(String word) {
        return deleteHelper(root, word, 0);
    }
    
    private boolean deleteHelper(TrieNode node, String word, int depth) {
        if (depth == word.length()) {
            if (!node.isEndOfWord) return false;
            node.isEndOfWord = false;
            // Возвращаем true, если у узла нет детей (можно удалить)
            return hasNoChildren(node);
        }
        
        int index = word.charAt(depth) - 'a';
        if (node.children[index] == null) return false;
        
        boolean shouldDelete = deleteHelper(node.children[index], word, depth + 1);
        
        if (shouldDelete) {
            node.children[index] = null;
            return hasNoChildren(node) && !node.isEndOfWord;
        }
        return false;
    }
    
    private boolean hasNoChildren(TrieNode node) {
        for (TrieNode child : node.children) {
            if (child != null) return false;
        }
        return true;
    }
}