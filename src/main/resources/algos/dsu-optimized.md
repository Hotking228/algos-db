# Система непересекающихся множеств с эвристиками (DSU with Heuristics)

## Intuition
DSU (Disjoint Set Union) с эвристиками — это оптимизированная версия системы непересекающихся множеств, которая использует две ключевые эвристики: сжатие путей (path compression) и объединение по рангу/размеру (union by rank/size). Сжатие путей делает структуру данных почти константной, сплющивая деревья при каждом поиске. Объединение по рангу гарантирует, что мы всегда присоединяем меньшее дерево к большему, предотвращая вырождение в длинные цепочки. Вместе эти эвристики дают амортизированное время O(α(n)) — обратную функцию Аккермана, которая для любых практических n ≤ 5.

## Approach
1. **Инициализация**: каждый элемент — отдельное множество, parent[i] = i, rank[i] = 0
2. **Find(x) с сжатием путей**:
    - Рекурсивно находим корень
    - Переподвешиваем все пройденные узлы напрямую к корню
3. **Union(x, y) с объединением по рангу**:
    - Находим корни rootX и rootY
    - Если rootX == rootY, ничего не делаем
    - Присоединяем дерево с меньшим рангом к дереву с большим рангом
    - Если ранги равны, один становится корнем, его ранг увеличивается
4. **Альтернатива — объединение по размеру**: присоединяем меньшее множество к большему
5. Эти эвристики работают вместе, обеспечивая почти константное время

## Complexity
- Time complexity (find): **O(α(n))** — обратная функция Аккермана (≤ 5)
- Time complexity (union): **O(α(n))**
- Time complexity (connected): **O(α(n))**
- Space complexity: **O(n)**

## Code

```java
public class DSUWithHeuristics {
    private int[] parent;
    private int[] rank;      // для объединения по рангу
    private int[] size;      // для объединения по размеру (альтернатива)
    private int components;  // количество компонент связности
    
    // Конструктор с выбором эвристики
    public DSUWithHeuristics(int n, boolean useRank) {
        parent = new int[n];
        this.components = n;
        
        if (useRank) {
            rank = new int[n];
            size = null;
        } else {
            size = new int[n];
            rank = null;
        }
        
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            if (useRank) {
                rank[i] = 0;
            } else {
                size[i] = 1;
            }
        }
    }
    
    // Find со сжатием путей (итеративная версия)
    public int find(int x) {
        // Находим корень
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        
        // Сжатие путей: проходим еще раз и переподвешиваем
        while (x != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        
        return root;
    }
    
    // Find со сжатием путей (рекурсивная версия - более элегантная)
    public int findRecursive(int x) {
        if (parent[x] != x) {
            parent[x] = findRecursive(parent[x]);
        }
        return parent[x];
    }
    
    // Объединение по рангу
    public void unionByRank(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) return;
        
        // Присоединяем дерево с меньшим рангом к дереву с большим рангом
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // Если ранги равны, один становится корнем и увеличиваем его ранг
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        components--;
    }
    
    // Объединение по размеру
    public void unionBySize(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) return;
        
        // Присоединяем меньшее множество к большему
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
        
        components--;
    }
    
    // Проверка, находятся ли x и y в одном множестве
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
    
    // Получение размера множества, содержащего x
    public int getSize(int x) {
        int root = find(x);
        if (size != null) {
            return size[root];
        }
        // Для ранговой эвристики нужно хранить отдельный массив размеров
        return calculateSize(root);
    }
    
    // Подсчет размера множества (если не храним отдельно)
    private int calculateSize(int root) {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (find(i) == root) count++;
        }
        return count;
    }
    
    // Получение количества компонент связности
    public int getComponents() {
        return components;
    }
    
    // Проверка, все ли элементы в одном множестве
    public boolean allConnected() {
        return components == 1;
    }
    
    // Сброс структуры данных
    public void reset() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            if (rank != null) {
                rank[i] = 0;
            }
            if (size != null) {
                size[i] = 1;
            }
        }
        components = parent.length;
    }
    
    // Получение представителя множества (корня)
    public int getRepresentative(int x) {
        return find(x);
    }
    
    // Печать структуры (для отладки)
    public void printStructure() {
        System.out.println("Components: " + components);
        for (int i = 0; i < parent.length; i++) {
            System.out.println("Element " + i + " -> parent: " + parent[i] + 
                               ", root: " + find(i));
        }
    }
}