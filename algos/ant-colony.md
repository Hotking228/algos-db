# Муравьиный алгоритм (Ant Colony Optimization)

## Intuition
Муравьиный алгоритм — это вероятностный метаэвристический алгоритм для решения задач поиска оптимального пути, вдохновленный поведением муравьев в природе. В реальности муравьи оставляют феромоновые следы, чтобы отмечать пути к пище. Другие муравьи с большей вероятностью следуют по путям с более сильным феромоновым следом. Со временем короткие пути получают больше феромона и становятся еще привлекательнее. Аналогично, в алгоритме искусственные "муравьи" строят решения, оставляя "феромон" на ребрах графа, и со временем популяция муравьев сходится к оптимальному пути.

## Approach
1. Инициализируем феромоны на всех ребрах небольшим положительным значением
2. Для каждой итерации:
    - Каждый муравей строит путь от начальной до конечной точки
    - Выбор следующей вершины основан на феромонах и эвристической информации (например, расстоянии)
    - После завершения пути, муравей обновляет феромоны на пройденных ребрах
3. Испарение феромона: на каждой итерации все феромоны уменьшаются (испаряются)
4. Лучший муравей может оставить дополнительный феромон (элитизм)
5. Повторяем, пока не будет достигнут критерий остановки

## Complexity
- Time complexity: **O(iterations × кол-во муравьев × V²)**
- Space complexity: **O(V²)**

## Code

```java
import java.util.*;

public class AntColony {
    
    private int numAnts;
    private int numCities;
    private double[][] distances;
    private double[][] pheromones;
    private double alpha = 1.0;      // влияние феромона
    private double beta = 2.0;        // влияние эвристики
    private double evaporation = 0.5; // скорость испарения
    private double Q = 100;            // количество феромона
    private Random random;
    
    public AntColony(double[][] distances, int numAnts) {
        this.distances = distances;
        this.numCities = distances.length;
        this.numAnts = numAnts;
        this.random = new Random();
        
        // Инициализация феромонов
        pheromones = new double[numCities][numCities];
        double initialPheromone = 1.0 / numCities;
        for (int i = 0; i < numCities; i++) {
            Arrays.fill(pheromones[i], initialPheromone);
        }
    }
    
    // Запуск оптимизации
    public int[] solve(int maxIterations) {
        int[] bestTour = null;
        double bestLength = Double.POSITIVE_INFINITY;
        
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // Каждый муравей строит путь
            List<int[]> tours = new ArrayList<>();
            List<Double> tourLengths = new ArrayList<>();
            
            for (int ant = 0; ant < numAnts; ant++) {
                int[] tour = constructTour();
                double length = calculateTourLength(tour);
                tours.add(tour);
                tourLengths.add(length);
                
                if (length < bestLength) {
                    bestLength = length;
                    bestTour = tour.clone();
                }
            }
            
            // Испарение феромона
            evaporatePheromones();
            
            // Обновление феромонов
            for (int i = 0; i < numAnts; i++) {
                depositPheromones(tours.get(i), Q / tourLengths.get(i));
            }
        }
        
        return bestTour;
    }
    
    // Построение пути одним муравьем
    private int[] constructTour() {
        int[] tour = new int[numCities];
        boolean[] visited = new boolean[numCities];
        
        // Начинаем со случайного города
        int start = random.nextInt(numCities);
        tour[0] = start;
        visited[start] = true;
        
        for (int i = 1; i < numCities; i++) {
            int current = tour[i - 1];
            int next = selectNextCity(current, visited);
            tour[i] = next;
            visited[next] = true;
        }
        
        return tour;
    }
    
    // Выбор следующего города на основе вероятности
    private int selectNextCity(int current, boolean[] visited) {
        double[] probabilities = new double[numCities];
        double sum = 0;
        
        for (int next = 0; next < numCities; next++) {
            if (!visited[next] && distances[current][next] > 0) {
                double pheromone = Math.pow(pheromones[current][next], alpha);
                double heuristic = Math.pow(1.0 / distances[current][next], beta);
                probabilities[next] = pheromone * heuristic;
                sum += probabilities[next];
            }
        }
        
        // Рулетка
        double randomValue = random.nextDouble() * sum;
        double cumulative = 0;
        
        for (int next = 0; next < numCities; next++) {
            if (probabilities[next] > 0) {
                cumulative += probabilities[next];
                if (cumulative >= randomValue) {
                    return next;
                }
            }
        }
        
        // Если ничего не выбрали, берем первый непосещенный
        for (int next = 0; next < numCities; next++) {
            if (!visited[next]) return next;
        }
        
        return -1;
    }
    
    // Испарение феромона
    private void evaporatePheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] *= (1 - evaporation);
            }
        }
    }
    
    // Откладывание феромона на пути
    private void depositPheromones(int[] tour, double amount) {
        for (int i = 0; i < numCities - 1; i++) {
            int from = tour[i];
            int to = tour[i + 1];
            pheromones[from][to] += amount;
            pheromones[to][from] += amount;
        }
    }
    
    // Вычисление длины пути
    private double calculateTourLength(int[] tour) {
        double length = 0;
        for (int i = 0; i < numCities - 1; i++) {
            length += distances[tour[i]][tour[i + 1]];
        }
        length += distances[tour[numCities - 1]][tour[0]];
        return length;
    }
    
    // Настройка параметров
    public void setAlpha(double alpha) { this.alpha = alpha; }
    public void setBeta(double beta) { this.beta = beta; }
    public void setEvaporation(double evaporation) { this.evaporation = evaporation; }
    public void setQ(double Q) { this.Q = Q; }
    
    // Версия для задачи коммивояжера с использованием городов с координатами
    public static class TSPAntColony {
        private double[][] distances;
        private int numCities;
        private double[][] coordinates;
        
        public TSPAntColony(double[][] coordinates) {
            this.coordinates = coordinates;
            this.numCities = coordinates.length;
            this.distances = new double[numCities][numCities];
            
            // Вычисляем евклидовы расстояния
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    double dx = coordinates[i][0] - coordinates[j][0];
                    double dy = coordinates[i][1] - coordinates[j][1];
                    distances[i][j] = Math.sqrt(dx * dx + dy * dy);
                }
            }
        }
        
        public int[] solve(int maxIterations, int numAnts) {
            AntColony antColony = new AntColony(distances, numAnts);
            return antColony.solve(maxIterations);
        }
    }
    
    // Результат оптимизации
    public static class OptimizationResult {
        public int[] bestTour;
        public double bestLength;
        public double[] convergenceHistory;
        
        OptimizationResult(int[] bestTour, double bestLength, double[] convergenceHistory) {
            this.bestTour = bestTour;
            this.bestLength = bestLength;
            this.convergenceHistory = convergenceHistory;
        }
    }
    
    // Версия с отслеживанием сходимости
    public OptimizationResult solveWithHistory(int maxIterations) {
        int[] bestTour = null;
        double bestLength = Double.POSITIVE_INFINITY;
        double[] history = new double[maxIterations];
        
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<int[]> tours = new ArrayList<>();
            List<Double> tourLengths = new ArrayList<>();
            
            for (int ant = 0; ant < numAnts; ant++) {
                int[] tour = constructTour();
                double length = calculateTourLength(tour);
                tours.add(tour);
                tourLengths.add(length);
                
                if (length < bestLength) {
                    bestLength = length;
                    bestTour = tour.clone();
                }
            }
            
            history[iteration] = bestLength;
            evaporatePheromones();
            
            for (int i = 0; i < numAnts; i++) {
                depositPheromones(tours.get(i), Q / tourLengths.get(i));
            }
        }
        
        return new OptimizationResult(bestTour, bestLength, history);
    }
}