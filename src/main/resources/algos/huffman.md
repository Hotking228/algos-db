# Алгоритм сжатия Хаффмана (Huffman Coding)

## Intuition
Алгоритм Хаффмана — это алгоритм сжатия данных без потерь, который создает префиксные коды переменной длины для символов. Наиболее часто встречающиеся символы получают короткие коды, а редко встречающиеся — длинные. Представьте, что вы отправляете сообщение, где буква "а" встречается часто, а "я" — редко. Вместо того чтобы использовать одинаковое количество бит для каждой буквы (как в ASCII), вы даете "а" очень короткий код (например, "0"), а "я" — длинный ("11111001"). При распаковке коды однозначно декодируются, потому что ни один код не является префиксом другого.

## Approach
1. Подсчитываем частоту каждого символа
2. Создаем листовые узлы для каждого символа
3. Помещаем все узлы в приоритетную очередь (min-heap) по частоте
4. Пока в очереди больше одного узла:
    - Извлекаем два узла с наименьшей частотой
    - Создаем новый внутренний узел с частотой = сумме их частот
    - Делаем извлеченные узлы левым и правым ребенком
    - Добавляем новый узел в очередь
5. Оставшийся узел — корень дерева Хаффмана
6. Обходим дерево, присваивая коды (0 для левого ребра, 1 для правого)

## Complexity
- Time complexity (построение): **O(n log n)**, где n — количество уникальных символов
- Time complexity (кодирование/декодирование): **O(m)**, где m — длина сообщения
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class HuffmanCoding {
    
    // Узел дерева Хаффмана
    static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left;
        Node right;
        
        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
            this.left = null;
            this.right = null;
        }
        
        Node(int freq, Node left, Node right) {
            this.ch = '\0';
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
        
        boolean isLeaf() {
            return left == null && right == null;
        }
    }
    
    // Кодирование строки
    public static Map<Character, String> buildCodeMap(String text) {
        // Подсчет частот
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        
        // Построение дерева Хаффмана
        Node root = buildTree(freqMap);
        
        // Генерация кодов
        Map<Character, String> codes = new HashMap<>();
        generateCodes(root, "", codes);
        
        return codes;
    }
    
    private static Node buildTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.freq + right.freq, left, right);
            pq.offer(parent);
        }
        
        return pq.poll();
    }
    
    private static void generateCodes(Node node, String code, Map<Character, String> codes) {
        if (node == null) return;
        
        if (node.isLeaf()) {
            codes.put(node.ch, code);
            return;
        }
        
        generateCodes(node.left, code + "0", codes);
        generateCodes(node.right, code + "1", codes);
    }
    
    // Кодирование строки в битовую строку
    public static String encode(String text, Map<Character, String> codes) {
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(codes.get(c));
        }
        return encoded.toString();
    }
    
    // Декодирование битовой строки
    public static String decode(String encoded, Node root) {
        StringBuilder decoded = new StringBuilder();
        Node current = root;
        
        for (char bit : encoded.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            
            if (current.isLeaf()) {
                decoded.append(current.ch);
                current = root;
            }
        }
        
        return decoded.toString();
    }
    
    // Полный цикл сжатия
    public static class HuffmanResult {
        String encoded;
        Node root;
        Map<Character, String> codes;
        double compressionRatio;
        
        HuffmanResult(String encoded, Node root, Map<Character, String> codes, 
                     int originalBits, int compressedBits) {
            this.encoded = encoded;
            this.root = root;
            this.codes = codes;
            this.compressionRatio = (double) compressedBits / originalBits;
        }
    }
    
    public static HuffmanResult compress(String text) {
        Map<Character, String> codes = buildCodeMap(text);
        String encoded = encode(text, codes);
        Node root = buildTree(getFreqMap(text));
        
        int originalBits = text.length() * 8; // ASCII: 8 бит на символ
        int compressedBits = encoded.length(); // бит в закодированной строке
        
        return new HuffmanResult(encoded, root, codes, originalBits, compressedBits);
    }
    
    private static Map<Character, Integer> getFreqMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }
    
    // Сериализация дерева для сохранения
    public static String serializeTree(Node root) {
        StringBuilder sb = new StringBuilder();
        serializeTreeRec(root, sb);
        return sb.toString();
    }
    
    private static void serializeTreeRec(Node node, StringBuilder sb) {
        if (node == null) return;
        
        if (node.isLeaf()) {
            sb.append("1").append(node.ch);
        } else {
            sb.append("0");
            serializeTreeRec(node.left, sb);
            serializeTreeRec(node.right, sb);
        }
    }
    
    // Десериализация дерева
    public static Node deserializeTree(String data) {
        if (data == null || data.isEmpty()) return null;
        
        int[] index = new int[]{0};
        return deserializeTreeRec(data, index);
    }
    
    private static Node deserializeTreeRec(String data, int[] index) {
        if (index[0] >= data.length()) return null;
        
        char flag = data.charAt(index[0]++);
        
        if (flag == '1') {
            char ch = data.charAt(index[0]++);
            return new Node(ch, 0);
        } else {
            Node left = deserializeTreeRec(data, index);
            Node right = deserializeTreeRec(data, index);
            Node node = new Node(0, left, right);
            return node;
        }
    }
    
    // Печать дерева (для отладки)
    public static void printTree(Node root) {
        printTreeRec(root, 0);
    }
    
    private static void printTreeRec(Node node, int level) {
        if (node == null) return;
        
        printTreeRec(node.right, level + 1);
        System.out.println("  ".repeat(level) + 
            (node.isLeaf() ? node.ch + "(" + node.freq + ")" : "*(" + node.freq + ")"));
        printTreeRec(node.left, level + 1);
    }
    
    // Статистика сжатия
    public static void printStatistics(String original, String encoded, Map<Character, String> codes) {
        System.out.println("Статистика сжатия Хаффмана:");
        System.out.printf("Исходная длина: %d байт (%d бит)%n", original.length(), original.length() * 8);
        System.out.printf("Сжатая длина: %d бит%n", encoded.length());
        System.out.printf("Коэффициент сжатия: %.2f%%%n", (1 - (double) encoded.length() / (original.length() * 8)) * 100);
        System.out.println("\nКоды символов:");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("  '%c' (%d раз) → %s (длина %d)%n", 
                entry.getKey(), getCharCount(original, entry.getKey()), 
                entry.getValue(), entry.getValue().length());
        }
    }
    
    private static int getCharCount(String text, char ch) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }
}