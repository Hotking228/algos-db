# Поиск компонент сильной связности (Алгоритм Тарьяна - Tarjan's Algorithm)

## Intuition
Алгоритм Тарьяна находит компоненты сильной связности за один проход DFS, используя индексы и low-ссылки. В отличие от алгоритма Косарайю, ему не требуется второй проход и транспонирование графа. Идея в том, что каждая SCC образует поддерево в DFS-дереве, и мы можем определить корень SCC по тому, что его индекс равен его low-ссылке. Представьте, что вы исследуете граф и помечаете вершины номерами. Если из вершины можно вернуться в более раннюю вершину, значит, они находятся в одной SCC.

## Approach
1. Каждой вершине присваивается уникальный индекс (время входа)
2. Поддерживается low-ссылка — наименьший индекс вершины, достижимый из текущей
3. Вершины добавляются в стек
4. При возврате из рекурсии обновляем low-ссылку:
   `low[u] = min(low[u], low[v])` для ребер в дереве
   `low[u] = min(low[u], index[v])` для обратных ребер
5. Если `index[u] == low[u]`, то u — корень SCC:
    - Извлекаем вершины из стека до u включительно
    - Это одна компонента сильной связности

## Complexity
- Time complexity: **O(V + E)**
- Space complexity: **O(V)**

## Code

```java
import java.util.*;

public class TarjanSCC {
    
    static class Graph {
        private int vertices;
        private List<List<Integer>> adj;
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }
        
        public List<List<Integer>> findSCCs() {
            int[] index = new int[vertices];     // время входа
            int[] low = new int[vertices];       // low-ссылка
            boolean[] onStack = new boolean[vertices];
            Stack<Integer> stack = new Stack<>();
            List<List<Integer>> sccs = new ArrayList<>();
            
            Arrays.fill(index, -1);
            
            for (int i = 0; i < vertices; i++) {
                if (index[i] == -1) {
                    dfs(i, index, low, onStack, stack, sccs, new int[]{0});
                }
            }
            
            return sccs;
        }
        
        private void dfs(int u, int[] index, int[] low, boolean[] onStack,
                        Stack<Integer> stack, List<List<Integer>> sccs, int[] counter) {
            index[u] = counter[0];
            low[u] = counter[0];
            counter[0]++;
            stack.push(u);
            onStack[u] = true;
            
            for (int v : adj.get(u)) {
                if (index[v] == -1) {
                    // Ребро дерева DFS
                    dfs(v, index, low, onStack, stack, sccs, counter);
                    low[u] = Math.min(low[u], low[v]);
                } else if (onStack[v]) {
                    // Обратное ребро к вершине в стеке
                    low[u] = Math.min(low[u], index[v]);
                }
            }
            
            // Если u — корень SCC
            if (low[u] == index[u]) {
                List<Integer> scc = new ArrayList<>();
                int v;
                do {
                    v = stack.pop();
                    onStack[v] = false;
                    scc.add(v);
                } while (v != u);
                sccs.add(scc);
            }
        }
        
        // Проверка, является ли граф сильно связным
        public boolean isStronglyConnected() {
            return findSCCs().size() == 1;
        }
        
        // Получение количества SCC
        public int countSCCs() {
            return findSCCs().size();
        }
        
        // Печать SCC
        public void printSCCs() {
            List<List<Integer>> sccs = findSCCs();
            System.out.println("Найдено " + sccs.size() + " компонент сильной связности:");
            for (int i = 0; i < sccs.size(); i++) {
                System.out.println("SCC " + (i + 1) + ": " + sccs.get(i));
            }
        }
        
        // Построение графа конденсации (SCC как вершины)
        public Graph getCondensationGraph() {
            List<List<Integer>> sccs = findSCCs();
            int[] sccId = new int[vertices];
            for (int i = 0; i < sccs.size(); i++) {
                for (int v : sccs.get(i)) {
                    sccId[v] = i;
                }
            }
            
            Graph condensation = new Graph(sccs.size());
            for (int u = 0; u < vertices; u++) {
                for (int v : adj.get(u)) {
                    if (sccId[u] != sccId[v]) {
                        condensation.addEdge(sccId[u], sccId[v]);
                    }
                }
            }
            
            // Удаляем дубликаты ребер
            for (int i = 0; i < condensation.vertices; i++) {
                List<Integer> unique = new ArrayList<>(new HashSet<>(condensation.adj.get(i)));
                condensation.adj.set(i, unique);
            }
            
            return condensation;
        }
        
        // Поиск SCC, содержащей заданную вершину
        public List<Integer> findSCCContaining(int vertex) {
            List<List<Integer>> sccs = findSCCs();
            for (List<Integer> scc : sccs) {
                if (scc.contains(vertex)) {
                    return scc;
                }
            }
            return null;
        }
    }
    
    // Итеративная версия (для больших графов, чтобы избежать переполнения стека)
    static class TarjanIterative {
        
        public static List<List<Integer>> findSCCs(List<List<Integer>> graph, int vertices) {
            int[] index = new int[vertices];
            int[] low = new int[vertices];
            boolean[] onStack = new boolean[vertices];
            Stack<Integer> stack = new Stack<>();
            List<List<Integer>> sccs = new ArrayList<>();
            
            Arrays.fill(index, -1);
            
            for (int i = 0; i < vertices; i++) {
                if (index[i] == -1) {
                    iterativeDFS(i, graph, index, low, onStack, stack, sccs);
                }
            }
            
            return sccs;
        }
        
        private static void iterativeDFS(int start, List<List<Integer>> graph,
                                        int[] index, int[] low, boolean[] onStack,
                                        Stack<Integer> stack, List<List<Integer>> sccs) {
            // Собственный стек для симуляции рекурсии
            Stack<Frame> callStack = new Stack<>();
            callStack.push(new Frame(start, 0));
            int counter = 0;
            
            while (!callStack.isEmpty()) {
                Frame frame = callStack.peek();
                int u = frame.u;
                
                if (index[u] == -1) {
                    index[u] = counter;
                    low[u] = counter;
                    counter++;
                    stack.push(u);
                    onStack[u] = true;
                }
                
                // Обработка следующего соседа
                if (frame.nextIndex < graph.get(u).size()) {
                    int v = graph.get(u).get(frame.nextIndex);
                    frame.nextIndex++;
                    
                    if (index[v] == -1) {
                        callStack.push(new Frame(v, 0));
                    } else if (onStack[v]) {
                        low[u] = Math.min(low[u], index[v]);
                    }
                } else {
                    // Все соседи обработаны
                    callStack.pop();
                    
                    if (!callStack.isEmpty()) {
                        low[callStack.peek().u] = Math.min(low[callStack.peek().u], low[u]);
                    }
                    
                    if (low[u] == index[u]) {
                        List<Integer> scc = new ArrayList<>();
                        int w;
                        do {
                            w = stack.pop();
                            onStack[w] = false;
                            scc.add(w);
                        } while (w != u);
                        sccs.add(scc);
                    }
                }
            }
        }
        
        static class Frame {
            int u;
            int nextIndex;
            Frame(int u, int nextIndex) {
                this.u = u;
                this.nextIndex = nextIndex;
            }
        }
    }
}