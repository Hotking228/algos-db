# Расширенный алгоритм Евклида (Extended Euclidean Algorithm)

## Intuition
Расширенный алгоритм Евклида не только находит НОД(a, b), но и находит коэффициенты x и y, такие что a·x + b·y = gcd(a, b). Это называется линейным представлением НОД. Представьте, что вы хотите выразить наибольший общий делитель двух чисел как их линейную комбинацию. Это фундаментальное свойство используется в криптографии (RSA), решении диофантовых уравнений и нахождении обратных элементов по модулю.

## Approach
1. Базовый случай: если b = 0, то НОД(a, 0) = a, и решение: x = 1, y = 0
2. Рекурсивно находим (x1, y1) для пары (b, a % b):
    - b·x1 + (a % b)·y1 = gcd(b, a % b)
3. Используем соотношение: a % b = a - (a/b)·b
4. Выражаем: a·y1 + b·(x1 - (a/b)·y1) = gcd(a, b)
5. Поэтому: x = y1, y = x1 - (a/b)·y1

## Complexity
- Time complexity: **O(log min(a, b))** — как у алгоритма Евклида
- Space complexity: **O(1)** для итеративной версии

## Code

```java
public class ExtendedEuclidean {
    
    // Класс для хранения результата
    static class Result {
        int gcd;
        int x;
        int y;
        
        Result(int gcd, int x, int y) {
            this.gcd = gcd;
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return String.format("gcd = %d, x = %d, y = %d", gcd, x, y);
        }
    }
    
    // Рекурсивная версия
    public static Result extendedGcd(int a, int b) {
        if (b == 0) {
            return new Result(Math.abs(a), (a < 0 ? -1 : 1), 0);
        }
        
        Result prev = extendedGcd(b, a % b);
        int x = prev.y;
        int y = prev.x - (a / b) * prev.y;
        
        // Нормализация знаков
        if (a < 0 && b < 0) {
            x = -x;
            y = -y;
        } else if (a < 0) {
            x = -x;
        }
        
        return new Result(prev.gcd, x, y);
    }
    
    // Итеративная версия (без рекурсии)
    public static Result extendedGcdIterative(int a, int b) {
        int x0 = 1, x1 = 0;
        int y0 = 0, y1 = 1;
        int originalA = a, originalB = b;
        
        a = Math.abs(a);
        b = Math.abs(b);
        
        while (b != 0) {
            int quotient = a / b;
            
            int temp = b;
            b = a % b;
            a = temp;
            
            temp = x1;
            x1 = x0 - quotient * x1;
            x0 = temp;
            
            temp = y1;
            y1 = y0 - quotient * y1;
            y0 = temp;
        }
        
        // Нормализация знаков
        if (originalA < 0) x0 = -x0;
        if (originalB < 0) y0 = -y0;
        
        return new Result(a, x0, y0);
    }
    
    // Нахождение обратного элемента по модулю (a^(-1) mod m)
    // Возвращает x, такой что (a * x) % m == 1
    public static int modularInverse(int a, int m) {
        Result result = extendedGcd(a, m);
        if (result.gcd != 1) {
            throw new ArithmeticException("Обратный элемент не существует: НОД(" + a + ", " + m + ") = " + result.gcd);
        }
        
        int inverse = result.x % m;
        if (inverse < 0) inverse += m;
        
        return inverse;
    }
    
    // Решение линейного диофантова уравнения: a·x + b·y = c
    // Возвращает решение, если существует, иначе null
    public static Result solveLinearDiophantine(int a, int b, int c) {
        Result result = extendedGcd(a, b);
        int gcd = result.gcd;
        
        if (c % gcd != 0) {
            return null; // Нет решений
        }
        
        int factor = c / gcd;
        int x = result.x * factor;
        int y = result.y * factor;
        
        return new Result(gcd, x, y);
    }
    
    // Получение всех решений диофантова уравнения
    public static List<Result> getAllSolutions(int a, int b, int c, int limit) {
        List<Result> solutions = new ArrayList<>();
        Result base = solveLinearDiophantine(a, b, c);
        
        if (base == null) return solutions;
        
        int gcd = base.gcd;
        int stepX = b / gcd;
        int stepY = -a / gcd;
        
        for (int t = -limit; t <= limit; t++) {
            int x = base.x + stepX * t;
            int y = base.y + stepY * t;
            solutions.add(new Result(gcd, x, y));
        }
        
        return solutions;
    }
    
    // Китайская теорема об остатках (для двух уравнений)
    // Решает: x ≡ a1 (mod m1), x ≡ a2 (mod m2)
    public static int chineseRemainder(int a1, int m1, int a2, int m2) {
        Result result = extendedGcd(m1, m2);
        
        if (result.gcd != 1) {
            throw new ArithmeticException("Модули не взаимно простые");
        }
        
        int x = a1 + m1 * ((a2 - a1) * result.x % m2);
        int lcm = m1 * m2;
        x %= lcm;
        if (x < 0) x += lcm;
        
        return x;
    }
    
    // Нахождение обратного элемента для больших чисел
    public static long modularInverse(long a, long m) {
        long[] result = extendedGcdLong(a, m);
        if (result[0] != 1) {
            throw new ArithmeticException("Обратный элемент не существует");
        }
        
        long inverse = result[1] % m;
        if (inverse < 0) inverse += m;
        
        return inverse;
    }
    
    private static long[] extendedGcdLong(long a, long b) {
        if (b == 0) {
            return new long[]{Math.abs(a), (a < 0 ? -1 : 1), 0};
        }
        
        long[] prev = extendedGcdLong(b, a % b);
        long x = prev[2];
        long y = prev[1] - (a / b) * prev[2];
        
        return new long[]{prev[0], x, y};
    }
    
    // Проверка, существует ли обратный элемент
    public static boolean hasModularInverse(int a, int m) {
        return gcd(a, m) == 1;
    }
    
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }
    
    // Демонстрация: RSA-like вычисления
    public static class RSAExample {
        public static void demonstrate() {
            // Выбираем два простых числа
            int p = 61, q = 53;
            int n = p * q;
            int phi = (p - 1) * (q - 1);
            
            // Выбираем e (открытая экспонента)
            int e = 17;
            
            // Находим d (секретная экспонента) — обратное к e по модулю phi
            int d = modularInverse(e, phi);
            
            System.out.println("RSA параметры:");
            System.out.println("  n = " + n);
            System.out.println("  e = " + e);
            System.out.println("  d = " + d);
            System.out.println("  e * d mod phi = " + ((long)e * d % phi));
            
            // Шифрование и дешифрование
            int message = 42;
            long encrypted = powMod(message, e, n);
            long decrypted = powMod(encrypted, d, n);
            
            System.out.println("  Сообщение: " + message);
            System.out.println("  Зашифровано: " + encrypted);
            System.out.println("  Расшифровано: " + decrypted);
        }
        
        private static long powMod(long a, long n, long mod) {
            long result = 1;
            a %= mod;
            while (n > 0) {
                if ((n & 1) == 1) {
                    result = (result * a) % mod;
                }
                a = (a * a) % mod;
                n >>= 1;
            }
            return result;
        }
    }
}