# Генетический алгоритм (Genetic Algorithm)

## Intuition
Генетический алгоритм — это метаэвристика, вдохновленная естественным отбором и эволюцией. Он имитирует процессы наследования, мутации, скрещивания и отбора для поиска оптимальных решений. Представьте, что у вас есть популяция особей (кандидатов решений), каждая со своим геномом. Те, кто лучше приспособлены к окружающей среде (имеют лучшее значение целевой функции), имеют больше шансов передать свои гены следующему поколению через скрещивание. Также могут происходить случайные мутации, вносящие разнообразие. Со временем популяция эволюционирует и находит все более хорошие решения.

## Approach
1. **Инициализация**: создаем случайную популяцию из N особей
2. **Оценка**: вычисляем приспособленность (fitness) каждой особи
3. **Отбор**: выбираем родителей с вероятностью, пропорциональной их приспособленности (рулетка, турнирный отбор)
4. **Скрещивание (кроссовер)**: с вероятностью crossover_rate создаем потомков, комбинируя гены родителей
5. **Мутация**: с вероятностью mutation_rate случайно изменяем гены потомков
6. **Формирование нового поколения**: заменяем старую популяцию новой
7. Повторяем шаги 2-6 до достижения критерия остановки

## Complexity
- Time complexity: **O(generations × population_size × (eval_time + crossover_time + mutation_time))**
- Space complexity: **O(population_size × chromosome_length)**

## Code

```java
import java.util.*;

public class GeneticAlgorithm {
    
    private Random random;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private int elitismCount;
    private int generations;
    
    public GeneticAlgorithm() {
        this.random = new Random();
        this.populationSize = 100;
        this.crossoverRate = 0.8;
        this.mutationRate = 0.01;
        this.elitismCount = 2;
        this.generations = 100;
    }
    
    // Интерфейс для задачи оптимизации
    public interface GeneticProblem {
        double fitness(Object individual);
        Object crossover(Object parent1, Object parent2);
        Object mutate(Object individual);
        Object randomIndividual();
    }
    
    // Задача о рюкзаке (0/1)
    public static class KnapsackProblem implements GeneticProblem {
        private int[] weights;
        private int[] values;
        private int capacity;
        private int numItems;
        
        public KnapsackProblem(int[] weights, int[] values, int capacity) {
            this.weights = weights;
            this.values = values;
            this.capacity = capacity;
            this.numItems = weights.length;
        }
        
        @Override
        public double fitness(Object individual) {
            boolean[] chromosome = (boolean[]) individual;
            int totalWeight = 0;
            int totalValue = 0;
            
            for (int i = 0; i < numItems; i++) {
                if (chromosome[i]) {
                    totalWeight += weights[i];
                    totalValue += values[i];
                }
            }
            
            if (totalWeight > capacity) {
                return 0; // Неправильное решение
            }
            return totalValue;
        }
        
        @Override
        public Object crossover(Object parent1, Object parent2) {
            boolean[] p1 = (boolean[]) parent1;
            boolean[] p2 = (boolean[]) parent2;
            boolean[] child = new boolean[numItems];
            
            int crossoverPoint = new Random().nextInt(numItems);
            for (int i = 0; i < numItems; i++) {
                child[i] = (i < crossoverPoint) ? p1[i] : p2[i];
            }
            
            return child;
        }
        
        @Override
        public Object mutate(Object individual) {
            boolean[] chromosome = (boolean[]) individual;
            boolean[] mutated = chromosome.clone();
            
            int index = new Random().nextInt(numItems);
            mutated[index] = !mutated[index];
            
            return mutated;
        }
        
        @Override
        public Object randomIndividual() {
            boolean[] chromosome = new boolean[numItems];
            for (int i = 0; i < numItems; i++) {
                chromosome[i] = random.nextBoolean();
            }
            return chromosome;
        }
    }
    
    // Задача о коммивояжере (TSP) — порядковое представление
    public static class TSPProblem implements GeneticProblem {
        private double[][] distances;
        private int numCities;
        
        public TSPProblem(double[][] distances) {
            this.distances = distances;
            this.numCities = distances.length;
        }
        
        @Override
        public double fitness(Object individual) {
            int[] tour = (int[]) individual;
            double length = 0;
            for (int i = 0; i < numCities - 1; i++) {
                length += distances[tour[i]][tour[i + 1]];
            }
            length += distances[tour[numCities - 1]][tour[0]];
            return 1.0 / length; // Обратная длина для максимизации
        }
        
        @Override
        public Object crossover(Object parent1, Object parent2) {
            int[] p1 = (int[]) parent1;
            int[] p2 = (int[]) parent2;
            int[] child = new int[numCities];
            Arrays.fill(child, -1);
            
            // PMX (Partially Mapped Crossover)
            int start = random.nextInt(numCities);
            int end = random.nextInt(numCities);
            if (start > end) { int temp = start; start = end; end = temp; }
            
            // Копируем сегмент
            for (int i = start; i <= end; i++) {
                child[i] = p1[i];
            }
            
            // Заполняем остальное из p2
            for (int i = 0; i < numCities; i++) {
                if (i >= start && i <= end) continue;
                
                int value = p2[i];
                while (contains(child, value, start, end)) {
                    int position = findPosition(p1, value);
                    value = p2[position];
                }
                child[i] = value;
            }
            
            return child;
        }
        
        private boolean contains(int[] arr, int value, int start, int end) {
            for (int i = start; i <= end; i++) {
                if (arr[i] == value) return true;
            }
            return false;
        }
        
        private int findPosition(int[] arr, int value) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == value) return i;
            }
            return -1;
        }
        
        @Override
        public Object mutate(Object individual) {
            int[] tour = (int[]) individual;
            int[] mutated = tour.clone();
            
            // swap mutation
            int i = random.nextInt(numCities);
            int j = random.nextInt(numCities);
            while (i == j) j = random.nextInt(numCities);
            
            int temp = mutated[i];
            mutated[i] = mutated[j];
            mutated[j] = temp;
            
            return mutated;
        }
        
        @Override
        public Object randomIndividual() {
            int[] tour = new int[numCities];
            for (int i = 0; i < numCities; i++) tour[i] = i;
            
            for (int i = numCities - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = tour[i];
                tour[i] = tour[j];
                tour[j] = temp;
            }
            return tour;
        }
    }
    
    // Основной алгоритм
    public OptimizationResult evolve(GeneticProblem problem) {
        List<Object> population = initializePopulation(problem);
        List<Double> bestHistory = new ArrayList<>();
        List<Double> avgHistory = new ArrayList<>();
        
        Object bestIndividual = null;
        double bestFitness = Double.NEGATIVE_INFINITY;
        
        for (int generation = 0; generation < generations; generation++) {
            // Оценка популяции
            double[] fitnesses = evaluatePopulation(population, problem);
            
            double generationBest = Double.NEGATIVE_INFINITY;
            double generationAvg = 0;
            for (int i = 0; i < populationSize; i++) {
                generationBest = Math.max(generationBest, fitnesses[i]);
                generationAvg += fitnesses[i];
            }
            generationAvg /= populationSize;
            
            bestHistory.add(generationBest);
            avgHistory.add(generationAvg);
            
            if (generationBest > bestFitness) {
                bestFitness = generationBest;
                for (int i = 0; i < populationSize; i++) {
                    if (fitnesses[i] == generationBest) {
                        bestIndividual = population.get(i);
                        break;
                    }
                }
            }
            
            // Формирование новой популяции
            List<Object> newPopulation = new ArrayList<>();
            
            // Элитизм
            Integer[] indices = getTopIndices(fitnesses, elitismCount);
            for (int i = 0; i < elitismCount; i++) {
                newPopulation.add(population.get(indices[i]));
            }
            
            // Создание потомков
            while (newPopulation.size() < populationSize) {
                Object parent1 = selection(population, fitnesses);
                Object parent2 = selection(population, fitnesses);
                
                Object child;
                if (random.nextDouble() < crossoverRate) {
                    child = problem.crossover(parent1, parent2);
                } else {
                    child = parent1;
                }
                
                if (random.nextDouble() < mutationRate) {
                    child = problem.mutate(child);
                }
                
                newPopulation.add(child);
            }
            
            population = newPopulation;
        }
        
        return new OptimizationResult(bestIndividual, bestFitness, bestHistory, avgHistory);
    }
    
    private List<Object> initializePopulation(GeneticProblem problem) {
        List<Object> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(problem.randomIndividual());
        }
        return population;
    }
    
    private double[] evaluatePopulation(List<Object> population, GeneticProblem problem) {
        double[] fitnesses = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitnesses[i] = problem.fitness(population.get(i));
        }
        return fitnesses;
    }
    
    private Object selection(List<Object> population, double[] fitnesses) {
        // Турнирный отбор
        int tournamentSize = 3;
        int bestIndex = random.nextInt(populationSize);
        
        for (int i = 1; i < tournamentSize; i++) {
            int candidate = random.nextInt(populationSize);
            if (fitnesses[candidate] > fitnesses[bestIndex]) {
                bestIndex = candidate;
            }
        }
        
        return population.get(bestIndex);
    }
    
    private Integer[] getTopIndices(double[] arr, int n) {
        Integer[] indices = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> Double.compare(arr[b], arr[a]));
        return Arrays.copyOf(indices, Math.min(n, arr.length));
    }
    
    public static class OptimizationResult {
        public Object bestSolution;
        public double bestFitness;
        public List<Double> bestHistory;
        public List<Double> avgHistory;
        
        OptimizationResult(Object bestSolution, double bestFitness, 
                          List<Double> bestHistory, List<Double> avgHistory) {
            this.bestSolution = bestSolution;
            this.bestFitness = bestFitness;
            this.bestHistory = bestHistory;
            this.avgHistory = avgHistory;
        }
    }
    
    // Настройка параметров
    public void setPopulationSize(int size) { this.populationSize = size; }
    public void setCrossoverRate(double rate) { this.crossoverRate = rate; }
    public void setMutationRate(double rate) { this.mutationRate = rate; }
    public void setElitismCount(int count) { this.elitismCount = count; }
    public void setGenerations(int generations) { this.generations = generations; }
}