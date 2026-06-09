# Алгоритм имитации отжига (Simulated Annealing)

## Intuition
Алгоритм имитации отжига — это вероятностный метаэвристический алгоритм для приближенного решения задач глобальной оптимизации, вдохновленный процессом отжига в металлургии. При отжиге металл нагревается до высокой температуры, а затем медленно охлаждается, позволяя атомам принять низкоэнергетическую (более стабильную) конфигурацию. Аналогично, алгоритм начинает с высокой "температуры" и высокой вероятностью принимать решения с ухудшением (чтобы выбраться из локальных минимумов). Постепенно температура уменьшается, и алгоритм становится более жадным, сходясь к хорошему решению.

## Approach
1. Начинаем с начального решения и высокой температуры T
2. Пока T > T_min:
    - Генерируем соседнее решение
    - Вычисляем изменение качества ΔE = новое_решение - текущее_решение
    - Если ΔE < 0 (улучшение), принимаем новое решение
    - Если ΔE > 0 (ухудшение), принимаем с вероятностью exp(-ΔE / T)
    - Уменьшаем температуру: T = T × cooling_rate
3. Возвращаем лучшее найденное решение

## Complexity
- Time complexity: **O(iterations × cost_of_neighbor_generation)**
- Space complexity: **O(1)**

## Code

```java
import java.util.*;

public class SimulatedAnnealing {
    
    private Random random;
    private double initialTemperature;
    private double coolingRate;
    private double minTemperature;
    private int iterationsPerTemp;
    
    public SimulatedAnnealing() {
        this.random = new Random();
        this.initialTemperature = 10000.0;
        this.coolingRate = 0.995;
        this.minTemperature = 1e-8;
        this.iterationsPerTemp = 100;
    }
    
    // Интерфейс для задачи оптимизации
    public interface OptimizationProblem {
        double getEnergy(Object solution);
        Object getRandomNeighbor(Object current);
        Object getRandomSolution();
    }
    
    // Задача коммивояжера (TSP)
    public static class TSPProblem implements OptimizationProblem {
        private double[][] distances;
        private int numCities;
        
        public TSPProblem(double[][] distances) {
            this.distances = distances;
            this.numCities = distances.length;
        }
        
        @Override
        public double getEnergy(Object solution) {
            int[] tour = (int[]) solution;
            double length = 0;
            for (int i = 0; i < numCities - 1; i++) {
                length += distances[tour[i]][tour[i + 1]];
            }
            length += distances[tour[numCities - 1]][tour[0]];
            return length;
        }
        
        @Override
        public Object getRandomNeighbor(Object current) {
            int[] tour = ((int[]) current).clone();
            // Двухоператорный обмен (2-opt)
            int i = new Random().nextInt(numCities);
            int j = new Random().nextInt(numCities);
            while (i == j) j = new Random().nextInt(numCities);
            
            if (i > j) { int temp = i; i = j; j = temp; }
            
            // Разворот подпоследовательности
            while (i < j) {
                int temp = tour[i];
                tour[i] = tour[j];
                tour[j] = temp;
                i++;
                j--;
            }
            return tour;
        }
        
        @Override
        public Object getRandomSolution() {
            int[] tour = new int[numCities];
            for (int i = 0; i < numCities; i++) tour[i] = i;
            // Перемешиваем
            Random rand = new Random();
            for (int i = numCities - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int temp = tour[i];
                tour[i] = tour[j];
                tour[j] = temp;
            }
            return tour;
        }
    }
    
    // Основной алгоритм
    public OptimizationResult optimize(OptimizationProblem problem) {
        Object current = problem.getRandomSolution();
        double currentEnergy = problem.getEnergy(current);
        Object best = current;
        double bestEnergy = currentEnergy;
        
        double temperature = initialTemperature;
        List<Double> energyHistory = new ArrayList<>();
        
        while (temperature > minTemperature) {
            for (int i = 0; i < iterationsPerTemp; i++) {
                Object neighbor = problem.getRandomNeighbor(current);
                double neighborEnergy = problem.getEnergy(neighbor);
                double delta = neighborEnergy - currentEnergy;
                
                if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble()) {
                    current = neighbor;
                    currentEnergy = neighborEnergy;
                    
                    if (currentEnergy < bestEnergy) {
                        best = current;
                        bestEnergy = currentEnergy;
                        energyHistory.add(bestEnergy);
                    }
                }
            }
            temperature *= coolingRate;
        }
        
        return new OptimizationResult(best, bestEnergy, energyHistory);
    }
    
    // Результат оптимизации
    public static class OptimizationResult {
        public Object bestSolution;
        public double bestEnergy;
        public List<Double> history;
        
        OptimizationResult(Object bestSolution, double bestEnergy, List<Double> history) {
            this.bestSolution = bestSolution;
            this.bestEnergy = bestEnergy;
            this.history = history;
        }
    }
    
    // Для функции одной переменной
    public static class FunctionProblem implements OptimizationProblem {
        private java.util.function.Function<Double, Double> function;
        private double minX;
        private double maxX;
        
        public FunctionProblem(java.util.function.Function<Double, Double> function, double minX, double maxX) {
            this.function = function;
            this.minX = minX;
            this.maxX = maxX;
        }
        
        @Override
        public double getEnergy(Object solution) {
            return -function.apply((Double) solution); // для максимизации
        }
        
        @Override
        public Object getRandomNeighbor(Object current) {
            double x = (Double) current;
            double step = (maxX - minX) * 0.1;
            double newX = x + (new Random().nextDouble() - 0.5) * step;
            return Math.max(minX, Math.min(maxX, newX));
        }
        
        @Override
        public Object getRandomSolution() {
            return minX + new Random().nextDouble() * (maxX - minX);
        }
    }
    
    // Установка параметров
    public void setInitialTemperature(double temp) { this.initialTemperature = temp; }
    public void setCoolingRate(double rate) { this.coolingRate = rate; }
    public void setMinTemperature(double minTemp) { this.minTemperature = minTemp; }
    public void setIterationsPerTemp(int iterations) { this.iterationsPerTemp = iterations; }
    
    // Экспоненциальное охлаждение
    public static class ExponentialCooling {
        public static double schedule(double initialTemp, double coolingRate, int iteration) {
            return initialTemp * Math.pow(coolingRate, iteration);
        }
    }
    
    // Линейное охлаждение
    public static class LinearCooling {
        public static double schedule(double initialTemp, double finalTemp, int iteration, int maxIterations) {
            return initialTemp - (initialTemp - finalTemp) * iteration / maxIterations;
        }
    }
    
    // Логарифмическое охлаждение
    public static class LogarithmicCooling {
        public static double schedule(double initialTemp, int iteration) {
            return initialTemp / Math.log(iteration + 2);
        }
    }
    
    // Поиск с адаптивным охлаждением
    public OptimizationResult optimizeAdaptive(OptimizationProblem problem, int maxStagnation) {
        Object current = problem.getRandomSolution();
        double currentEnergy = problem.getEnergy(current);
        Object best = current;
        double bestEnergy = currentEnergy;
        
        double temperature = initialTemperature;
        int stagnationCount = 0;
        List<Double> energyHistory = new ArrayList<>();
        
        while (temperature > minTemperature && stagnationCount < maxStagnation) {
            boolean improved = false;
            
            for (int i = 0; i < iterationsPerTemp; i++) {
                Object neighbor = problem.getRandomNeighbor(current);
                double neighborEnergy = problem.getEnergy(neighbor);
                double delta = neighborEnergy - currentEnergy;
                
                if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble()) {
                    current = neighbor;
                    currentEnergy = neighborEnergy;
                    
                    if (currentEnergy < bestEnergy) {
                        best = current;
                        bestEnergy = currentEnergy;
                        improved = true;
                        energyHistory.add(bestEnergy);
                    }
                }
            }
            
            if (improved) {
                stagnationCount = 0;
            } else {
                stagnationCount++;
            }
            
            temperature *= coolingRate;
        }
        
        return new OptimizationResult(best, bestEnergy, energyHistory);
    }
}