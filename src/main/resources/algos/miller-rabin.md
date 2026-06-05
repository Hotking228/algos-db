# Алгоритм Миллера-Рабина (Miller-Rabin Primality Test)

## Intuition
Алгоритм Миллера-Рабина — это вероятностный тест простоты чисел, который определяет, является ли число простым с высокой точностью. Он основан на свойствах чисел по модулю и малой теореме Ферма. В отличие от детерминированных тестов (которые могут быть очень медленными для больших чисел), Миллер-Рабин очень быстр и дает ответ с пренебрежимо малой вероятностью ошибки. Представьте, что вы проверяете, является ли большое число простым, проводя несколько "тестов", каждый из которых может ошибочно принять составное число за простое с вероятностью не более 1/4. После k тестов вероятность ошибки становится (1/4)^k.

## Approach
1. Если n < 2 — не простое
2. Проверяем четные числа: если n четное и n ≠ 2 — не простое
3. Представляем n-1 как d·2^s, где d нечетное
4. Для каждого выбранного основания a (обычно 2, 3, 5, 7, 11...):
    - Вычисляем x = a^d mod n
    - Если x ≡ 1 или x ≡ n-1, переходим к следующему a
    - Повторяем s-1 раз: x = x^2 mod n
    - Если x ≡ n-1, переходим к следующему a
    - Если ни одно условие не выполнено — n составное
5. Если все тесты пройдены — n вероятно простое

## Complexity
- Time complexity: **O(k × log³ n)** — k итераций теста
- Вероятность ошибки: **4^(-k)** для случайных чисел

## Code

```java
import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {
    
    private static final Random random = new Random();
    
    // Детерминированные основания для 32-битных чисел
    private static final int[] BASES_32BIT = {2, 7, 61};
    
    // Детерминированные основания для 64-битных чисел
    private static final int[] BASES_64BIT = {2, 325, 9375, 28178, 450775, 9780504, 1795265022};
    
    // Базовый вероятностный тест
    public static boolean isPrime(long n, int iterations) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        
        // Представляем n-1 как d * 2^s
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        
        for (int i = 0; i < iterations; i++) {
            long a = 2 + random.nextLong() % (n - 3);
            if (!isWitness(a, n, d, s)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Детерминированная версия для 32-битных чисел
    public static boolean isPrimeDeterministic32(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        
        for (int a : BASES_32BIT) {
            if (a % n == 0) continue;
            if (!isWitness(a, n, d, s)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Детерминированная версия для 64-битных чисел
    public static boolean isPrimeDeterministic64(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        
        for (long a : BASES_64BIT) {
            if (a % n == 0) continue;
            if (!isWitness(a, n, d, s)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Проверка свидетеля простоты
    private static boolean isWitness(long a, long n, long d, int s) {
        long x = powMod(a, d, n);
        
        if (x == 1 || x == n - 1) {
            return true;
        }
        
        for (int i = 0; i < s - 1; i++) {
            x = (x * x) % n;
            if (x == n - 1) {
                return true;
            }
            if (x == 1) {
                return false;
            }
        }
        
        return false;
    }
    
    // Быстрое возведение в степень по модулю
    private static long powMod(long a, long d, long n) {
        long result = 1;
        long base = a % n;
        
        while (d > 0) {
            if ((d & 1) == 1) {
                result = (result * base) % n;
            }
            base = (base * base) % n;
            d >>= 1;
        }
        
        return result;
    }
    
    // Версия для BigInteger (работает с очень большими числами)
    public static boolean isPrimeBig(BigInteger n, int iterations) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0) return false;
        if (n.equals(BigInteger.valueOf(2))) return true;
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) return false;
        
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;
        while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.valueOf(2));
            s++;
        }
        
        for (int i = 0; i < iterations; i++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), random);
            } while (a.compareTo(BigInteger.ONE) < 0 || a.compareTo(n.subtract(BigInteger.ONE)) >= 0);
            
            if (!isWitnessBig(a, n, d, s)) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean isWitnessBig(BigInteger a, BigInteger n, BigInteger d, int s) {
        BigInteger x = a.modPow(d, n);
        
        if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
            return true;
        }
        
        for (int i = 0; i < s - 1; i++) {
            x = x.multiply(x).mod(n);
            if (x.equals(n.subtract(BigInteger.ONE))) {
                return true;
            }
            if (x.equals(BigInteger.ONE)) {
                return false;
            }
        }
        
        return false;
    }
    
    // Поиск следующего простого числа после n
    public static long nextPrime(long n) {
        if (n < 2) return 2;
        if (n == 2) return 3;
        
        long candidate = (n % 2 == 0) ? n + 1 : n;
        
        while (true) {
            if (isPrimeDeterministic64(candidate)) {
                return candidate;
            }
            candidate += 2;
        }
    }
    
    // Генерация случайного простого числа заданной битовой длины
    public static BigInteger randomPrime(int bitLength) {
        BigInteger candidate;
        do {
            candidate = new BigInteger(bitLength, random);
            // Убеждаемся, что число нечетное
            candidate = candidate.setBit(0);
        } while (!isPrimeBig(candidate, 20));
        
        return candidate;
    }
    
    // Проверка с предварительным просеиванием (быстрее)
    public static boolean isPrimeWithSieve(long n) {
        if (n < 2) return false;
        
        // Проверяем малые простые числа
        int[] smallPrimes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
        for (int p : smallPrimes) {
            if (n == p) return true;
            if (n % p == 0) return false;
        }
        
        return isPrimeDeterministic64(n);
    }
    
    // Оценка вероятности ошибки
    public static double errorProbability(int iterations) {
        return Math.pow(0.25, iterations);
    }
}