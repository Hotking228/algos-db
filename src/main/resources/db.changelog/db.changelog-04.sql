INSERT INTO algo_tag (algo_id, tag_id) VALUES
-- Быстрая сортировка (id=1)
(1, 1),  -- сортировка
(1, 4),  -- рекурсия

-- Сортировка пузырьком (id=2)
(2, 1),  -- сортировка

-- Бинарный поиск (id=3)
(3, 2),  -- поиск
(3, 4),  -- рекурсия

-- Хеш-таблица (id=4)
(4, 3),  -- структуры данных
(4, 10), -- хеш-таблицы

-- Двоичное дерево поиска (id=5)
(5, 3),  -- структуры данных
(5, 2),  -- поиск
(5, 9),  -- деревья
(5, 4),  -- рекурсия

-- Сортировка слиянием (id=6)
(6, 1),  -- сортировка
(6, 4),  -- рекурсия

-- Линейный поиск (id=7)
(7, 2),  -- поиск

-- Стек (id=8)
(8, 3),  -- структуры данных
(8, 8),  -- линейные структуры

-- Очередь (id=9)
(9, 3),  -- структуры данных
(9, 8),  -- линейные структуры

-- Граф (BFS) (id=10)
(10, 3), -- структуры данных
(10, 2), -- поиск
(10, 5), -- графы

-- Поиск в глубину (DFS) (id=11)
(11, 3), -- структуры данных
(11, 2), -- поиск
(11, 5), -- графы
(11, 4), -- рекурсия

-- Сортировка выбором (id=12)
(12, 1), -- сортировка

-- Пирамидальная сортировка (id=13)
(13, 1), -- сортировка

-- Алгоритм Дейкстры (id=14)
(14, 5), -- графы
(14, 7), -- жадные алгоритмы

-- Алгоритм Флойда (id=15)
(15, 5), -- графы
(15, 6); -- динамическое программирование

-- DSU (id=16)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (16, 3),   -- структуры данных
                                           (16, 22);  -- системы непересекающихся множеств

-- Префиксное дерево (Trie) (id=17)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (17, 3),   -- структуры данных
                                           (17, 11),  -- строки
                                           (17, 9);   -- деревья

-- Куча (бинарная) (id=18)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (18, 3),   -- структуры данных
                                           (18, 9);   -- деревья

-- Декартово дерево (Treap) (id=19)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (19, 3),   -- структуры данных
                                           (19, 9),   -- деревья
                                           (19, 14);  -- вероятностный

-- Красно-черное дерево (id=20)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (20, 3),   -- структуры данных
                                           (20, 9);   -- деревья

-- АВЛ-дерево (id=21)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (21, 3),   -- структуры данных
                                           (21, 9);   -- деревья

-- Хеш-таблица с открытой адресацией (id=22)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (22, 3),   -- структуры данных
                                           (22, 10);  -- хеш-таблицы

-- Кольцевая очередь (id=23)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (23, 3),   -- структуры данных
                                           (23, 8);   -- линейные структуры

-- Список с пропусками (Skip List) (id=24)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (24, 3),   -- структуры данных
                                           (24, 14);  -- вероятностный

-- Двухсвязный список (id=25)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (25, 3),   -- структуры данных
                                           (25, 8);   -- линейные структуры

-- Система непересекающихся множеств оптимизированная (id=26)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (26, 3),   -- структуры данных
                                           (26, 22);  -- системы непересекающихся множеств

-- Сортировка Шелла (id=27)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (27, 1);   -- сортировка

-- Сортировка подсчетом (id=28)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (28, 1);   -- сортировка

-- Поразрядная сортировка (id=29)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (29, 1);   -- сортировка

-- Блочная сортировка (id=30)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (30, 1);   -- сортировка

-- Гномья сортировка (id=31)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (31, 1);   -- сортировка

-- Сортировка вставками (id=32)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (32, 1);   -- сортировка

-- Introsort (id=33)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (33, 1),   -- сортировка
                                           (33, 4);   -- рекурсия

-- Алгоритм Прима (id=34)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (34, 5),   -- графы
                                           (34, 7);   -- жадные алгоритмы

-- Алгоритм Крускала (id=35)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (35, 5),   -- графы
                                           (35, 7),   -- жадные алгоритмы
                                           (35, 22);  -- системы непересекающихся множеств

-- Алгоритм Беллмана-Форда (id=36)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (36, 5),   -- графы
                                           (36, 6);   -- динамическое программирование

-- Алгоритм Форда-Фалкерсона (id=37)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (37, 5),   -- графы
                                           (37, 16);  -- потоки

-- Алгоритм Эдмондса-Карпа (id=38)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (38, 5),   -- графы
                                           (38, 16);  -- потоки

-- Топологическая сортировка (id=39)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (39, 5),   -- графы
                                           (39, 2);   -- поиск

-- Поиск компонент сильной связности (Косарайю) (id=40)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (40, 5),   -- графы
                                           (40, 2);   -- поиск

-- Поиск компонент сильной связности (Тарьян) (id=41)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (41, 5),   -- графы
                                           (41, 2);   -- поиск

-- Алгоритм Дейкстры для разреженных графов (id=42)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (42, 5),   -- графы
                                           (42, 7);   -- жадные алгоритмы

-- Алгоритм A* (id=43)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (43, 5),   -- графы
                                           (43, 2),   -- поиск
                                           (43, 13);  -- оптимизация

-- Алгоритм Флойда-Уоршелла (id=44)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (44, 5),   -- графы
                                           (44, 6);   -- динамическое программирование

-- Алгоритм Джонсона (id=45)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (45, 5),   -- графы
                                           (45, 6);   -- динамическое программирование

-- Алгоритм Кнута-Морриса-Пратта (КМП) (id=46)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (46, 11),  -- строки
                                           (46, 2);   -- поиск

-- Алгоритм Бойера-Мура (id=47)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (47, 11),  -- строки
                                           (47, 2);   -- поиск

-- Алгоритм Рабина-Карпа (id=48)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (48, 11),  -- строки
                                           (48, 2),   -- поиск
                                           (48, 10);  -- хеш-таблицы

-- Суффиксный массив (id=49)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (49, 11),  -- строки
                                           (49, 2);   -- поиск

-- Поиск наибольшей общей подстроки (id=50)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (50, 11),  -- строки
                                           (50, 6);   -- динамическое программирование

-- Расстояние Левенштейна (id=51)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (51, 11),  -- строки
                                           (51, 6);   -- динамическое программирование

-- Алгоритм Ахо-Корасик (id=52)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (52, 11),  -- строки
                                           (52, 2);   -- поиск

-- QuickSelect (id=53)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (53, 2),   -- поиск
                                           (53, 4),   -- рекурсия
                                           (53, 15);  -- комбинаторика

-- Медиана медиан (id=54)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (54, 2),   -- поиск
                                           (54, 4);   -- рекурсия

-- Экспоненциальный поиск (id=55)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
    (55, 2);   -- поиск

-- Интерполяционный поиск (id=56)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (56, 2),   -- поиск
                                           (56, 12);  -- математика

-- Тернарный поиск (id=57)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (57, 2),   -- поиск
                                           (57, 4);   -- рекурсия

-- BFS на матрице (id=58)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (58, 5),   -- графы
                                           (58, 2);   -- поиск

-- DFS на матрице (id=59)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (59, 5),   -- графы
                                           (59, 2),   -- поиск
                                           (59, 4);   -- рекурсия

-- Задача о рюкзаке (0/1) (id=60)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (60, 6),   -- динамическое программирование
                                           (60, 13);  -- оптимизация

-- Задача о рюкзаке (непрерывный) (id=61)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (61, 7),   -- жадные алгоритмы
                                           (61, 13);  -- оптимизация

-- LCS (id=62)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (62, 11),  -- строки
                                           (62, 6);   -- динамическое программирование

-- LIS (id=63)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (63, 6),   -- динамическое программирование
                                           (63, 15);  -- комбинаторика

-- Алгоритм Вагнера-Фишера (id=64)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (64, 11),  -- строки
                                           (64, 6);   -- динамическое программирование

-- Матричное умножение цепочек (id=65)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (65, 6),   -- динамическое программирование
                                           (65, 12),  -- математика
                                           (65, 13);  -- оптимизация

-- Алгоритм Флойда для поиска цикла (id=66)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (66, 2),   -- поиск
                                           (66, 3);   -- структуры данных

-- Быстрое возведение в степень (id=67)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (67, 12),  -- математика
                                           (67, 4),   -- рекурсия
                                           (67, 21);  -- битовые операции

-- Алгоритм Евклида (НОД) (id=68)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (68, 12),  -- математика
                                           (68, 4);   -- рекурсия

-- Расширенный алгоритм Евклида (id=69)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (69, 12),  -- математика
                                           (69, 4);   -- рекурсия

-- Решето Эратосфена (id=70)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (70, 12),  -- математика
                                           (70, 2);   -- поиск

-- Решето Аткина (id=71)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (71, 12),  -- математика
                                           (71, 2);   -- поиск

-- Алгоритм Миллера-Рабина (id=72)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (72, 12),  -- математика
                                           (72, 14);  -- вероятностный

-- RSA-алгоритм (id=73)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (73, 12),  -- математика
                                           (73, 3);   -- структуры данных

-- Алгоритм сжатия Хаффмана (id=74)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (74, 17),  -- сжатие
                                           (74, 9),   -- деревья
                                           (74, 7);   -- жадные алгоритмы

-- Алгоритм LZW (id=75)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (75, 17),  -- сжатие
                                           (75, 10);  -- хеш-таблицы

-- Быстрое преобразование Фурье (БПФ) (id=76)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (76, 12),  -- математика
                                           (76, 4),   -- рекурсия
                                           (76, 18);  -- параллельные

-- Алгоритм Штрассена (id=77)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (77, 12),  -- математика
                                           (77, 4);   -- рекурсия

-- Алгоритм Карпа-Рабина (id=78)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (78, 11),  -- строки
                                           (78, 2),   -- поиск
                                           (78, 10);  -- хеш-таблицы

-- Муравьиный алгоритм (id=79)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (79, 5),   -- графы
                                           (79, 14),  -- вероятностный
                                           (79, 13);  -- оптимизация

-- Алгоритм имитации отжига (id=80)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (80, 13),  -- оптимизация
                                           (80, 14);  -- вероятностный

-- Генетический алгоритм (id=81)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (81, 13),  -- оптимизация
                                           (81, 14);  -- вероятностный

-- Дерево отрезков (id=82)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (82, 3),   -- структуры данных
                                           (82, 9),   -- деревья
                                           (82, 2);   -- поиск

-- Дерево Фенвика (BIT) (id=83)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (83, 3),   -- структуры данных
                                           (83, 2);   -- поиск

-- Sparse Table (id=84)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (84, 3),   -- структуры данных
                                           (84, 2);   -- поиск

-- Декартово дерево по неявному ключу (id=85)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (85, 3),   -- структуры данных
                                           (85, 9);   -- деревья

-- Очередь с приоритетом (id=86)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (86, 3),   -- структуры данных
                                           (86, 8);   -- линейные структуры

-- Стек с минимумом (id=87)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (87, 3),   -- структуры данных
                                           (87, 8);   -- линейные структуры

-- Очередь с минимумом (id=88)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (88, 3),   -- структуры данных
                                           (88, 8);   -- линейные структуры

-- Система непересекающихся множеств с эвристиками (id=89)
INSERT INTO algo_tag (algo_id, tag_id) VALUES
                                           (89, 3),   -- структуры данных
                                           (89, 22);  -- системы непересекающихся множеств