# Алгоритм Евклида (НОД - Greatest Common Divisor)

## Intuition
Алгоритм Евклида — это один из старейших известных алгоритмов для нахождения наибольшего общего делителя (НОД) двух чисел. Он основан на простом наблюдении: НОД(a, b) = НОД(b, a mod b). Другими словами, если разделить большее число на меньшее, то общий делитель будет таким же. Представьте, что у вас есть прямоугольник со сторонами a и b, и вы хотите найти самую большую квадратную плитку, которой можно полностью замостить этот прямоугольник. Вы отрезаете квадраты со стороной b, оставшийся прямоугольник имеет размеры b и (a mod b), и повторяете процесс.

## Approach
**Классический алгоритм Евклида:**
1. Пока b ≠ 0:
    - temp = b
    - b = a % b
    - a = temp
2. НОД = |a|

**Рекурсивная версия:**
- Если b == 0: return |a|
- Иначе: return gcd(b, a % b)

## Complexity
- Time complexity: **O(log min(a, b))** — количество шагов пропорционально логарифму
- Space complexity: **O(1)** — итеративная версия

## Code

```java
public class EuclideanAlgorithm {
    
    // Итеративная версия
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        
        return a;
    }
    
    // Рекурсивная версия
    public static int gcdRecursive(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        if (b == 0) return a;
        return gcdRecursive(b, a % b);
    }
    
    // НОД для трех и более чисел
    public static int gcd(int[] numbers) {
        int result = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            result = gcd(result, numbers[i]);
            if (result == 1) return 1;
        }
        return result;
    }
    
    // НОД для длинных целых
    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        
        return a;
    }
    
    // НОД для BigInteger
    public static java.math.BigInteger gcd(java.math.BigInteger a, java.math.BigInteger b) {
        return a.gcd(b);
    }
    
    // Бинарный алгоритм Евклида (алгоритм Стейна)
    // Эффективнее для больших чисел, использует сдвиги вместо деления
    public static int binaryGcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        if (a == 0) return b;
        if (b == 0) return a;
        
        int shift;
        for (shift = 0; ((a | b) & 1) == 0; shift++) {
            a >>= 1;
            b >>= 1;
        }
        
        while ((a & 1) == 0) a >>= 1;
        
        do {
            while ((b & 1) == 0) b >>= 1;
            
            if (a > b) {
                int temp = a;
                a = b;
                b = temp;
            }
            
            b = b - a;
        } while (b != 0);
        
        return a << shift;
    }
    
    // НОК - наименьшее общее кратное
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a / gcd(a, b) * b); // сначала деление, чтобы избежать переполнения
    }
    
    public static int lcm(int[] numbers) {
        int result = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            result = lcm(result, numbers[i]);
        }
        return result;
    }
    
    // Сокращение дроби
    public static int[] reduceFraction(int numerator, int denominator) {
        int divisor = gcd(numerator, denominator);
        return new int[]{numerator / divisor, denominator / divisor};
    }
    
    // Общий знаменатель для двух дробей
    public static int commonDenominator(int denom1, int denom2) {
        return lcm(denom1, denom2);
    }
    
    // Проверка, являются ли числа взаимно простыми
    public static boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;
    }
    
    // Наибольший общий делитель двух чисел с использованием вычитания (классическая версия)
    public static int gcdSubtraction(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        if (a == 0) return b;
        if (b == 0) return a;
        
        while (a != b) {
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
        
        return a;
    }
}