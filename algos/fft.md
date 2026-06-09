# Быстрое преобразование Фурье (FFT - Fast Fourier Transform)

## Intuition
Быстрое преобразование Фурье (FFT) — это алгоритм, который вычисляет дискретное преобразование Фурье (ДПФ) и обратное к нему за O(n log n) операций, тогда как прямое вычисление требует O(n²). FFT используется для умножения многочленов, обработки сигналов, сжатия изображений и многих других задач. Идея основана на разделяй-и-властвуй: разбиваем многочлен на четные и нечетные степени, рекурсивно вычисляем преобразования для половин, а затем комбинируем с использованием комплексных корней из единицы.

## Approach
1. **Прямое FFT (Cooley-Tukey)**:
    - Базовый случай: если n == 1, возвращаем текущее значение
    - Разделяем на четные и нечетные индексы
    - Рекурсивно вычисляем FFT для обеих половин
    - Комбинируем: для k от 0 до n/2:
        - t = ω_k × odd[k]
        - even[k] + t — верхняя половина
        - even[k] - t — нижняя половина
2. **Обратное FFT (IFFT)** аналогично, но с использованием сопряженных корней и делением на n
3. **Бит-реверсирование** используется для итеративной версии

## Complexity
- Time complexity: **O(n log n)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class FFT {
    
    // Класс для комплексных чисел
    static class Complex {
        double re;
        double im;
        
        Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }
        
        Complex add(Complex other) {
            return new Complex(this.re + other.re, this.im + other.im);
        }
        
        Complex sub(Complex other) {
            return new Complex(this.re - other.re, this.im - other.im);
        }
        
        Complex mul(Complex other) {
            return new Complex(
                this.re * other.re - this.im * other.im,
                this.re * other.im + this.im * other.re
            );
        }
        
        Complex div(double d) {
            return new Complex(this.re / d, this.im / d);
        }
        
        Complex conjugate() {
            return new Complex(this.re, -this.im);
        }
        
        @Override
        public String toString() {
            return String.format("%.2f + %.2fi", re, im);
        }
    }
    
    // Рекурсивная версия FFT
    public static Complex[] fftRecursive(Complex[] a, boolean invert) {
        int n = a.length;
        if (n == 1) return a;
        
        // Разделение на четные и нечетные индексы
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        
        for (int i = 0; i < n / 2; i++) {
            even[i] = a[2 * i];
            odd[i] = a[2 * i + 1];
        }
        
        // Рекурсивные вызовы
        even = fftRecursive(even, invert);
        odd = fftRecursive(odd, invert);
        
        // Объединение
        Complex[] result = new Complex[n];
        double angle = 2 * Math.PI / n * (invert ? -1 : 1);
        Complex w = new Complex(1, 0);
        Complex wn = new Complex(Math.cos(angle), Math.sin(angle));
        
        for (int i = 0; i < n / 2; i++) {
            Complex t = w.mul(odd[i]);
            result[i] = even[i].add(t);
            result[i + n / 2] = even[i].sub(t);
            w = w.mul(wn);
        }
        
        if (invert) {
            for (int i = 0; i < n; i++) {
                result[i] = result[i].div(n);
            }
        }
        
        return result;
    }
    
    // Итеративная версия FFT (быстрее и без рекурсии)
    public static Complex[] fftIterative(Complex[] a, boolean invert) {
        int n = a.length;
        
        // Бит-реверсирование
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            while (j >= bit) {
                j -= bit;
                bit >>= 1;
            }
            j += bit;
            if (i < j) {
                Complex temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        
        // Итеративное построение
        for (int length = 2; length <= n; length <<= 1) {
            double angle = 2 * Math.PI / length * (invert ? -1 : 1);
            Complex wn = new Complex(Math.cos(angle), Math.sin(angle));
            
            for (int i = 0; i < n; i += length) {
                Complex w = new Complex(1, 0);
                for (int j = 0; j < length / 2; j++) {
                    Complex u = a[i + j];
                    Complex v = a[i + j + length / 2].mul(w);
                    a[i + j] = u.add(v);
                    a[i + j + length / 2] = u.sub(v);
                    w = w.mul(wn);
                }
            }
        }
        
        if (invert) {
            for (int i = 0; i < n; i++) {
                a[i] = a[i].div(n);
            }
        }
        
        return a;
    }
    
    // Умножение многочленов через FFT
    public static int[] multiplyPolynomials(int[] p, int[] q) {
        int n = 1;
        while (n < p.length + q.length - 1) {
            n <<= 1;
        }
        
        Complex[] fa = new Complex[n];
        Complex[] fb = new Complex[n];
        
        for (int i = 0; i < n; i++) {
            fa[i] = new Complex(i < p.length ? p[i] : 0, 0);
            fb[i] = new Complex(i < q.length ? q[i] : 0, 0);
        }
        
        fa = fftIterative(fa, false);
        fb = fftIterative(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i].mul(fb[i]);
        }
        
        fa = fftIterative(fa, true);
        
        int[] result = new int[p.length + q.length - 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) Math.round(fa[i].re);
        }
        
        return result;
    }
    
    // Умножение полиномов с double коэффициентами
    public static double[] multiplyPolynomials(double[] p, double[] q) {
        int n = 1;
        while (n < p.length + q.length - 1) {
            n <<= 1;
        }
        
        Complex[] fa = new Complex[n];
        Complex[] fb = new Complex[n];
        
        for (int i = 0; i < n; i++) {
            fa[i] = new Complex(i < p.length ? p[i] : 0, 0);
            fb[i] = new Complex(i < q.length ? q[i] : 0, 0);
        }
        
        fa = fftIterative(fa, false);
        fb = fftIterative(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i].mul(fb[i]);
        }
        
        fa = fftIterative(fa, true);
        
        double[] result = new double[p.length + q.length - 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = fa[i].re;
        }
        
        return result;
    }
    
    // Умножение больших чисел через FFT
    public static String multiplyLargeNumbers(String num1, String num2) {
        int[] a = new int[num1.length()];
        int[] b = new int[num2.length()];
        
        for (int i = 0; i < num1.length(); i++) {
            a[i] = num1.charAt(num1.length() - 1 - i) - '0';
        }
        for (int i = 0; i < num2.length(); i++) {
            b[i] = num2.charAt(num2.length() - 1 - i) - '0';
        }
        
        int[] result = multiplyPolynomials(a, b);
        
        // Обработка переносов
        int carry = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            int sum = result[i] + carry;
            sb.append(sum % 10);
            carry = sum / 10;
        }
        while (carry > 0) {
            sb.append(carry % 10);
            carry /= 10;
        }
        
        // Удаление ведущих нулей
        String resultStr = sb.reverse().toString();
        int leadingZeros = 0;
        while (leadingZeros < resultStr.length() - 1 && resultStr.charAt(leadingZeros) == '0') {
            leadingZeros++;
        }
        
        return resultStr.substring(leadingZeros);
    }
    
    // Кросс-корреляция двух сигналов
    public static double[] crossCorrelation(double[] x, double[] y) {
        int n = 1;
        while (n < x.length + y.length - 1) {
            n <<= 1;
        }
        
        Complex[] fx = new Complex[n];
        Complex[] fy = new Complex[n];
        
        for (int i = 0; i < n; i++) {
            fx[i] = new Complex(i < x.length ? x[i] : 0, 0);
            fy[i] = new Complex(i < y.length ? y[i] : 0, 0);
        }
        
        fx = fftIterative(fx, false);
        fy = fftIterative(fy, false);
        
        for (int i = 0; i < n; i++) {
            fx[i] = fx[i].mul(fy[i].conjugate());
        }
        
        fx = fftIterative(fx, true);
        
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = fx[i].re;
        }
        
        return result;
    }
    
    // Вспомогательный метод для преобразования double в Complex array
    public static Complex[] toComplexArray(double[] arr) {
        Complex[] result = new Complex[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = new Complex(arr[i], 0);
        }
        return result;
    }
}