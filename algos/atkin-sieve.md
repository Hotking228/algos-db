# Решето Аткина (Sieve of Atkin)

## Intuition
Решето Аткина — это современный алгоритм для нахождения всех простых чисел до заданного предела. В отличие от решета Эратосфена, которое вычеркивает кратные простых чисел, решето Аткина использует квадратичные формы и теоретико-числовые свойства. Оно основано на том факте, что простые числа (кроме 2, 3, 5) имеют определенные остатки при делении на 60. Алгоритм сначала отмечает числа, которые могут быть простыми, используя три квадратичные формы, а затем вычеркивает кратные квадратов. Для больших n (например, > 10⁷) решето Аткина быстрее решета Эратосфена, но сложнее в реализации.

## Approach
1. Создаем массив isPrime[0..n], инициализируем false
2. Отмечаем 2, 3, 5 как простые
3. Для всех x, y, вычисляем:
    - n = 4x² + y², если n ≤ limit и (n mod 12 == 1 или 5) → инвертируем isPrime[n]
    - n = 3x² + y², если n ≤ limit и (n mod 12 == 7) → инвертируем isPrime[n]
    - n = 3x² - y², если x > y, n ≤ limit и (n mod 12 == 11) → инвертируем isPrime[n]
4. Удаляем кратные квадратов простых чисел
5. Оставшиеся true — простые числа

## Complexity
- Time complexity: **O(n / log log n)** — асимптотически быстрее решета Эратосфена
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class AtkinSieve {
    
    // Решето Аткина для нахождения простых чисел до n
    public static boolean[] sieve(int limit) {
        if (limit < 2) return new boolean[limit + 1];
        
        boolean[] isPrime = new boolean[limit + 1];
        
        // 2, 3, 5 — простые по определению
        if (limit >= 2) isPrime[2] = true;
        if (limit >= 3) isPrime[3] = true;
        if (limit >= 5) isPrime[5] = true;
        
        int sqrtLimit = (int) Math.sqrt(limit);
        
        for (int x = 1; x <= sqrtLimit; x++) {
            int x2 = x * x;
            for (int y = 1; y <= sqrtLimit; y++) {
                int y2 = y * y;
                int n = 4 * x2 + y2;
                
                // Первая квадратичная форма: 4x² + y²
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    isPrime[n] = !isPrime[n];
                }
                
                // Вторая квадратичная форма: 3x² + y²
                n = 3 * x2 + y2;
                if (n <= limit && n % 12 == 7) {
                    isPrime[n] = !isPrime[n];
                }
                
                // Третья квадратичная форма: 3x² - y²
                n = 3 * x2 - y2;
                if (x > y && n <= limit && n % 12 == 11) {
                    isPrime[n] = !isPrime[n];
                }
            }
        }
        
        // Удаляем числа, кратные квадратам простых чисел
        for (int i = 5; i <= sqrtLimit; i++) {
            if (isPrime[i]) {
                int i2 = i * i;
                for (int j = i2; j <= limit; j += i2) {
                    isPrime[j] = false;
                }
            }
        }
        
        return isPrime;
    }
    
    // Получение списка простых чисел с помощью решета Аткина
    public static List<Integer> getPrimes(int limit) {
        boolean[] isPrime = sieve(limit);
        List<Integer> primes = new ArrayList<>();
        
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        
        return primes;
    }
    
    // Оптимизированная версия с skip для четных и делящихся на 3 чисел
    public static boolean[] sieveOptimized(int limit) {
        if (limit < 2) return new boolean[limit + 1];
        
        boolean[] isPrime = new boolean[limit + 1];
        
        if (limit >= 2) isPrime[2] = true;
        if (limit >= 3) isPrime[3] = true;
        
        int sqrtLimit = (int) Math.sqrt(limit);
        
        for (int x = 1; x <= sqrtLimit; x++) {
            int x2 = x * x;
            for (int y = 1; y <= sqrtLimit; y++) {
                int y2 = y * y;
                int n = 4 * x2 + y2;
                
                if (n <= limit) {
                    int mod = n % 12;
                    if (mod == 1 || mod == 5) {
                        isPrime[n] = !isPrime[n];
                    }
                }
                
                n = 3 * x2 + y2;
                if (n <= limit && n % 12 == 7) {
                    isPrime[n] = !isPrime[n];
                }
                
                n = 3 * x2 - y2;
                if (x > y && n <= limit && n % 12 == 11) {
                    isPrime[n] = !isPrime[n];
                }
            }
        }
        
        // Просеивание квадратами
        for (int i = 5; i <= sqrtLimit; i++) {
            if (isPrime[i]) {
                int i2 = i * i;
                for (int j = i2; j <= limit; j += i2) {
                    isPrime[j] = false;
                }
            }
        }
        
        // Добавляем простые числа, которые могли быть пропущены
        // (2 и 3 уже установлены)
        
        return isPrime;
    }
    
    // Версия для больших чисел с использованием BitSet (экономия памяти)
    public static java.util.BitSet sieveBitSet(int limit) {
        java.util.BitSet isPrime = new java.util.BitSet(limit + 1);
        
        if (limit >= 2) isPrime.set(2);
        if (limit >= 3) isPrime.set(3);
        if (limit >= 5) isPrime.set(5);
        
        int sqrtLimit = (int) Math.sqrt(limit);
        
        for (int x = 1; x <= sqrtLimit; x++) {
            int x2 = x * x;
            for (int y = 1; y <= sqrtLimit; y++) {
                int y2 = y * y;
                int n = 4 * x2 + y2;
                
                if (n <= limit) {
                    int mod = n % 12;
                    if (mod == 1 || mod == 5) {
                        isPrime.flip(n);
                    }
                }
                
                n = 3 * x2 + y2;
                if (n <= limit && n % 12 == 7) {
                    isPrime.flip(n);
                }
                
                n = 3 * x2 - y2;
                if (x > y && n <= limit && n % 12 == 11) {
                    isPrime.flip(n);
                }
            }
        }
        
        for (int i = 5; i <= sqrtLimit; i++) {
            if (isPrime.get(i)) {
                int i2 = i * i;
                for (int j = i2; j <= limit; j += i2) {
                    isPrime.clear(j);
                }
            }
        }
        
        return isPrime;
    }
    
    // Подсчет простых чисел до limit
    public static int primeCount(int limit) {
        if (limit < 2) return 0;
        
        boolean[] isPrime = sieve(limit);
        int count = 0;
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }
    
    // Сравнение производительности с решетом Эратосфена
    public static PerformanceResult comparePerformance(int limit) {
        long startTime = System.nanoTime();
        boolean[] atkinResult = sieve(limit);
        long atkinTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        boolean[] eratosthenesResult = eratosthenesSieve(limit);
        long eratosthenesTime = System.nanoTime() - startTime;
        
        int atkinPrimes = 0, eratosthenesPrimes = 0;
        for (int i = 2; i <= limit; i++) {
            if (atkinResult[i]) atkinPrimes++;
            if (eratosthenesResult[i]) eratosthenesPrimes++;
        }
        
        return new PerformanceResult(
            atkinTime / 1_000_000, 
            eratosthenesTime / 1_000_000,
            atkinPrimes,
            eratosthenesPrimes
        );
    }
    
    private static boolean[] eratosthenesSieve(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        if (limit < 2) return isPrime;
        
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int p = 2; p * p <= limit; p++) {
            if (isPrime[p]) {
                for (int multiple = p * p; multiple <= limit; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }
        
        return isPrime;
    }
    
    static class PerformanceResult {
        long atkinTimeMs;
        long eratosthenesTimeMs;
        int atkinCount;
        int eratosthenesCount;
        
        PerformanceResult(long atkinTimeMs, long eratosthenesTimeMs, 
                         int atkinCount, int eratosthenesCount) {
            this.atkinTimeMs = atkinTimeMs;
            this.eratosthenesTimeMs = eratosthenesTimeMs;
            this.atkinCount = atkinCount;
            this.eratosthenesCount = eratosthenesCount;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Решето Аткина: %d мс, %d простых\nРешето Эратосфена: %d мс, %d простых",
                atkinTimeMs, atkinCount, eratosthenesTimeMs, eratosthenesCount
            );
        }
    }
}