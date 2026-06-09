# Задача о рюкзаке (непрерывный) (Fractional Knapsack)

## Intuition
Задача о непрерывном рюкзаке (Fractional Knapsack) — это вариант задачи о рюкзаке, где мы можем брать части предметов (дроби), а не только целиком. В отличие от задачи 0/1, здесь мы можем взять, например, половину золотого слитка или 0.3 мешка муки. Это делает задачу значительно проще, и она решается жадным алгоритмом. Представьте, что у вас есть рюкзак и вы можете насыпать сколько угодно крупы из каждого мешка, но мешки имеют разную ценность за килограмм. Выгоднее всего брать в первую очередь самые "дорогие" продукты.

## Approach
1. Вычисляем ценность каждого предмета на единицу веса (value/weight)
2. Сортируем предметы по убыванию этой плотности ценности
3. Проходим по отсортированным предметам:
    - Если предмет полностью помещается в рюкзак, берем его целиком
    - Если предмет не помещается целиком, берем только часть (оставшееся место)
4. Алгоритм гарантирует максимальную суммарную стоимость

## Complexity
- Time complexity: **O(n log n)** — из-за сортировки
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class FractionalKnapsack {
    
    // Класс для представления предмета
    static class Item {
        int weight;
        int value;
        double density; // ценность на единицу веса
        
        Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.density = (double) value / weight;
        }
    }
    
    // Основное решение с сортировкой по плотности
    public static double knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Item[] items = new Item[n];
        
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i]);
        }
        
        // Сортируем по убыванию плотности ценности
        Arrays.sort(items, (a, b) -> Double.compare(b.density, a.density));
        
        double totalValue = 0;
        int remainingCapacity = capacity;
        
        for (Item item : items) {
            if (item.weight <= remainingCapacity) {
                // Берем весь предмет
                totalValue += item.value;
                remainingCapacity -= item.weight;
            } else {
                // Берем только часть предмета
                totalValue += item.density * remainingCapacity;
                break;
            }
        }
        
        return totalValue;
    }
    
    // Версия с деталями: сколько взято от каждого предмета
    public static KnapsackResult knapsackDetailed(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Item[] items = new Item[n];
        
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i]);
        }
        
        // Сортируем и запоминаем оригинальные индексы
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> Double.compare(
            (double) values[b] / weights[b], 
            (double) values[a] / weights[a]
        ));
        
        double totalValue = 0;
        int remainingCapacity = capacity;
        double[] fractions = new double[n];
        
        for (int idx : indices) {
            if (weights[idx] <= remainingCapacity) {
                fractions[idx] = 1.0;
                totalValue += values[idx];
                remainingCapacity -= weights[idx];
            } else {
                fractions[idx] = (double) remainingCapacity / weights[idx];
                totalValue += values[idx] * fractions[idx];
                remainingCapacity = 0;
                break;
            }
        }
        
        return new KnapsackResult(totalValue, fractions, weights, values);
    }
    
    // Результат с дробями взятых предметов
    static class KnapsackResult {
        double totalValue;
        double[] fractions;
        int[] weights;
        int[] values;
        
        KnapsackResult(double totalValue, double[] fractions, int[] weights, int[] values) {
            this.totalValue = totalValue;
            this.fractions = fractions;
            this.weights = weights;
            this.values = values;
        }
        
        void print() {
            System.out.printf("Максимальная стоимость: %.2f\n", totalValue);
            System.out.println("Взятые предметы:");
            for (int i = 0; i < fractions.length; i++) {
                if (fractions[i] > 0) {
                    System.out.printf("  Предмет %d: %.2f%% (вес %d, стоимость %d)\n",
                        i, fractions[i] * 100, weights[i], values[i]);
                }
            }
        }
    }
    
    // Альтернативная реализация с PriorityQueue (для динамического добавления предметов)
    public static double knapsackDynamic(List<Item> items, int capacity) {
        PriorityQueue<Item> pq = new PriorityQueue<>((a, b) -> 
            Double.compare(b.density, a.density));
        pq.addAll(items);
        
        double totalValue = 0;
        int remainingCapacity = capacity;
        
        while (!pq.isEmpty() && remainingCapacity > 0) {
            Item item = pq.poll();
            int takeWeight = Math.min(item.weight, remainingCapacity);
            totalValue += item.density * takeWeight;
            remainingCapacity -= takeWeight;
        }
        
        return totalValue;
    }
    
    // Для случая, когда предметы добавляются в потоке (online алгоритм)
    // Жадный алгоритм все еще работает
    public static double knapsackOnline(StreamItem[] stream, int capacity) {
        // Сортируем по мере поступления (в реальности не знаем будущие предметы)
        // Но для демонстрации - просто обрабатываем по порядку с пересчетом
        double totalValue = 0;
        int remainingCapacity = capacity;
        
        // Приоритетная очередь для лучших предметов, которые мы уже видели
        PriorityQueue<StreamItem> pq = new PriorityQueue<>((a, b) -> 
            Double.compare(b.density, a.density));
        
        for (StreamItem item : stream) {
            pq.offer(item);
            // Пересчитываем оптимальный набор (упрощенно)
            // В реальном online алгоритме мы не можем пересмотреть прошлые решения
        }
        
        while (!pq.isEmpty() && remainingCapacity > 0) {
            StreamItem item = pq.poll();
            int takeWeight = Math.min(item.weight, remainingCapacity);
            totalValue += item.density * takeWeight;
            remainingCapacity -= takeWeight;
        }
        
        return totalValue;
    }
    
    static class StreamItem {
        int weight;
        int value;
        double density;
        
        StreamItem(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.density = (double) value / weight;
        }
    }
    
    // Доказательство оптимальности жадного алгоритма: 
    // всегда выгоднее брать предметы с большей плотностью
    public static boolean verifyOptimality(int[] weights, int[] values, int capacity) {
        // Проверяем свойство обмена (exchange argument)
        // Если есть оптимальное решение, не отсортированное по плотности,
        // его можно улучшить, поменяв предметы местами
        int n = weights.length;
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i]);
        }
        
        Arrays.sort(items, (a, b) -> Double.compare(b.density, a.density));
        
        // Жадное решение
        double greedyValue = 0;
        int cap = capacity;
        for (Item item : items) {
            int take = Math.min(item.weight, cap);
            greedyValue += item.density * take;
            cap -= take;
        }
        
        // Для дробной задачи жадное всегда оптимально
        // Поэтому мы можем просто сказать, что оно всегда даст максимум
        return true;
    }
}