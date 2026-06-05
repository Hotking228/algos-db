# Решето Эратосфена (Sieve of Eratosthenes)

## Intuition
Решето Эратосфена — это эффективный алгоритм для нахождения всех простых чисел до заданного предела n. Представьте, что у вас есть список чисел от 2 до n. Вы начинаете с 2 (первое простое) и вычеркиваете все его кратные (4, 6, 8...). Затем переходите к следующему невычеркнутому числу — 3 — и вычеркиваете все его кратные (6, 9, 12...). Продолжаете, пока не дойдете до √n. Все оставшиеся невычеркнутыми числа — простые. Название "решето" происходит от того, что алгоритм "просеивает" составные числа, оставляя простые.

## Approach
1. Создаем булевый массив isPrime[0..n], изначально все true
2. 0 и 1 не простые, помечаем их как false
3. Для p от 2 до √n:
    - Если isPrime[p] == true:
        - Помечаем все кратные p, начиная с p², как false (p², p²+p, p²+2p...)
4. Все индексы, где isPrime[i] == true — простые числа

## Complexity
- Time complexity: **O(n log log n)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class SieveOfEratosthenes {
    
    // Классическое решето Эратосфена
    public static boolean[] sieve(int n) {
        boolean[] isPrime = new boolean[n + 1];
        if (n < 2) return isPrime;
        
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int multiple = p * p; multiple <= n; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }
        
        return isPrime;
    }
    
    // Получение списка простых чисел до n
    public static List<Integer> getPrimes(int n) {
        boolean[] isPrime = sieve(n);
        List<Integer> primes = new ArrayList<>();
        
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        
        return primes;
    }
    
    // Оптимизированное решето с использованием битовых операций (экономия памяти)
    public static boolean[] sieveBitOptimized(int n) {
        int size = (n + 1) / 2; // храним только нечетные числа
        boolean[] isPrime = new boolean[size];
        Arrays.fill(isPrime, true);
        
        if (n >= 2) {
            // 2 — простое
        }
        
        for (int p = 3; p * p <= n; p += 2) {
            if (isPrime[p / 2]) {
                for (int multiple = p * p; multiple <= n; multiple += 2 * p) {
                    isPrime[multiple / 2] = false;
                }
            }
        }
        
        return isPrime;
    }
    
    // Решето для нахождения всех простых чисел в диапазоне [left, right]
    public static List<Integer> segmentedSieve(int left, int right) {
        int limit = (int) Math.sqrt(right) + 1;
        List<Integer> basePrimes = getPrimes(limit);
        
        boolean[] isPrime = new boolean[right - left + 1];
        Arrays.fill(isPrime, true);
        
        if (left <= 1) {
            isPrime[1 - left] = false;
        }
        if (left <= 0) {
            if (0 - left >= 0) isPrime[0 - left] = false;
            if (1 - left >= 0) isPrime[1 - left] = false;
        }
        
        for (int prime : basePrimes) {
            long start = Math.max((long) prime * prime, (left + prime - 1) / prime * prime);
            
            for (long j = start; j <= right; j += prime) {
                isPrime[(int) (j - left)] = false;
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = 0; i <= right - left; i++) {
            if (isPrime[i]) {
                primes.add(left + i);
            }
        }
        
        return primes;
    }
    
    // Факторизация всех чисел до n (нахождение наименьшего простого делителя)
    public static int[] smallestPrimeFactor(int n) {
        int[] spf = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            spf[i] = i;
        }
        
        for (int p = 2; p * p <= n; p++) {
            if (spf[p] == p) {
                for (int multiple = p * p; multiple <= n; multiple += p) {
                    if (spf[multiple] == multiple) {
                        spf[multiple] = p;
                    }
                }
            }
        }
        
        return spf;
    }
    
    // Факторизация числа с помощью решета
    public static List<Integer> factorize(int x, int[] spf) {
        List<Integer> factors = new ArrayList<>();
        while (x > 1) {
            int p = spf[x];
            factors.add(p);
            x /= p;
            while (x % p == 0) {
                factors.add(p);
                x /= p;
            }
        }
        return factors;
    }
    
    // Функция Эйлера (фи) для всех чисел до n
    public static int[] eulerTotient(int n) {
        int[] phi = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            phi[i] = i;
        }
        
        for (int p = 2; p <= n; p++) {
            if (phi[p] == p) { // p — простое
                for (int multiple = p; multiple <= n; multiple += p) {
                    phi[multiple] -= phi[multiple] / p;
                }
            }
        }
        
        return phi;
    }
    
    // Подсчет количества простых чисел до n (функция π(n))
    public static int primeCount(int n) {
        if (n < 2) return 0;
        
        boolean[] isPrime = sieve(n);
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }
    
    // Проверка простоты числа (для одиночного числа)
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    // Сумма простых чисел до n
    public static long sumOfPrimes(int n) {
        boolean[] isPrime = sieve(n);
        long sum = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                sum += i;
            }
        }
        return sum;
    }
}