-- =====================================================
-- ДОПОЛНИТЕЛЬНЫЕ АЛГОРИТМЫ И СТРУКТУРЫ ДАННЫХ
-- =====================================================

-- СТРУКТУРЫ ДАННЫХ (продолжение)
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (16, 'Disjoint Set Union (DSU)', 'algos/dsu.md', 4),              -- O(α(n)) амортизированно
    (17, 'Префиксное дерево (Trie)', 'algos/trie.md', 2),            -- O(L) L - длина строки
    (18, 'Куча (бинарная)', 'algos/heap.md', 1),                     -- O(log n) для вставки/удаления
    (19, 'Декартово дерево (Treap)', 'algos/treap.md', 2),           -- O(log n) в среднем
    (20, 'Красно-черное дерево', 'algos/red-black-tree.md', 2),      -- O(log n)
    (21, 'АВЛ-дерево', 'algos/avl-tree.md', 2),                      -- O(log n)
    (22, 'Хеш-таблица с открытой адресацией', 'algos/hash-open-addressing.md', 1), -- O(1) средний
    (23, 'Кольцевая очередь', 'algos/circular-queue.md', 1),         -- O(1)
    (24, 'Список с пропусками (Skip List)', 'algos/skip-list.md', 2), -- O(log n) в среднем
    (25, 'Двухсвязный список', 'algos/doubly-linked-list.md', 1),    -- O(1) вставка/удаление по указателю
(26, 'Система непересекающихся множеств', 'algos/dsu-optimized.md', 4); -- O(α(n))

-- АЛГОРИТМЫ СОРТИРОВКИ (дополнительные)
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (27, 'Сортировка Шелла', 'algos/shell-sort.md', 5),              -- O(n²) в худшем, O(n log² n) в среднем
    (28, 'Сортировка подсчетом', 'algos/counting-sort.md', 3),       -- O(n + k)
    (29, 'Поразрядная сортировка', 'algos/radix-sort.md', 3),        -- O(nk)
    (30, 'Блочная сортировка', 'algos/bucket-sort.md', 3),           -- O(n + k) среднее
    (31, 'Гномья сортировка', 'algos/gnome-sort.md', 5),             -- O(n²)
    (32, 'Сортировка вставками', 'algos/insertion-sort.md', 5),      -- O(n²)
    (33, 'Introsort', 'algos/introsort.md', 4);                      -- O(n log n)

-- АЛГОРИТМЫ НА ГРАФАХ (продолжение)
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (34, 'Алгоритм Прима', 'algos/prims.md', 4),                     -- O((n + m) log n)
    (35, 'Алгоритм Крускала', 'algos/kruskal.md', 4),               -- O(m log n)
    (36, 'Алгоритм Беллмана-Форда', 'algos/bellman-ford.md', 5),    -- O(nm)
    (37, 'Алгоритм Форда-Фалкерсона', 'algos/ford-fulkerson.md', 4), -- O(E * max_flow)
    (38, 'Алгоритм Эдмондса-Карпа', 'algos/edmonds-karp.md', 5),    -- O(VE²)
    (39, 'Топологическая сортировка', 'algos/topological-sort.md', 3), -- O(n + m)
    (40, 'Поиск компонент сильной связности (Косарайю)', 'algos/kosaraju.md', 3), -- O(n + m)
    (41, 'Поиск компонент сильной связности (Тарьян)', 'algos/tarjan-scc.md', 3), -- O(n + m)
    (42, 'Алгоритм Дейкстры для разреженных графов', 'algos/dijkstra-sparse.md', 4), -- O(m log n)
    (43, 'Алгоритм A*', 'algos/astar.md', 4),                       -- O(E) в лучшем, O(b^d) в худшем
    (45, 'Алгоритм Джонсона', 'algos/johnson.md', 4);               -- O(n² log n + n m)

-- ЗАДАЧИ НА СТРОКИ
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (46, 'Алгоритм Кнута-Морриса-Пратта (КМП)', 'algos/kmp.md', 3), -- O(n + m)
    (47, 'Алгоритм Бойера-Мура', 'algos/boyer-moore.md', 3),        -- O(n/m) в лучшем, O(nm) в худшем
    (48, 'Алгоритм Рабина-Карпа', 'algos/rabin-karp.md', 3),        -- O(n + m) среднее
    (49, 'Суффиксный массив', 'algos/suffix-array.md', 3),          -- O(n log n)
    (50, 'Поиск наибольшей общей подстроки', 'algos/longest-common-substring.md', 3), -- O(nm)
    (51, 'Расстояние Левенштейна', 'algos/levenshtein.md', 5),      -- O(nm)
    (52, 'Алгоритм Ахо-Корасик', 'algos/aho-corasick.md', 3);       -- O(n + m + z)

-- ПОИСК И ВЫБОРКА
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (53, 'Алгоритм поиска k-й порядковой статистики (QuickSelect)', 'algos/quickselect.md', 4), -- O(n) в среднем
    (54, 'Алгоритм поиска k-й порядковой статистики (медиана медиан)', 'algos/median-of-medians.md', 3), -- O(n)
    (55, 'Экспоненциальный поиск', 'algos/exponential-search.md', 2), -- O(log n)
    (56, 'Интерполяционный поиск', 'algos/interpolation-search.md', 2), -- O(log log n) среднее
    (57, 'Тернарный поиск', 'algos/ternary-search.md', 2),          -- O(log n)
    (58, 'Поиск в ширину (BFS) на матрице', 'algos/bfs-matrix.md', 4), -- O(n²)
(59, 'Поиск в глубину (DFS) на матрице', 'algos/dfs-matrix.md', 4); -- O(n²)

-- ДИНАМИЧЕСКОЕ ПРОГРАММИРОВАНИЕ
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (60, 'Задача о рюкзаке (0/1)', 'algos/knapsack-01.md', 5),       -- O(nW)
    (61, 'Задача о рюкзаке (непрерывный)', 'algos/knapsack-fractional.md', 4), -- O(n log n)
    (62, 'Наибольшая общая подпоследовательность (LCS)', 'algos/lcs.md', 5), -- O(nm)
    (63, 'Наибольшая возрастающая подпоследовательность (LIS)', 'algos/lis.md', 4), -- O(n log n)
    (64, 'Алгоритм Вагнера-Фишера', 'algos/wagner-fischer.md', 5),   -- O(nm)
    (65, 'Матричное умножение цепочек', 'algos/matrix-chain.md', 5), -- O(n³)
    (66, 'Алгоритм Флойда для поиска цикла (черепаха и заяц)', 'algos/floyd-cycle.md', 3); -- O(n)

-- ПРОЧИЕ АЛГОРИТМЫ
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (67, 'Быстрое возведение в степень', 'algos/binary-exponentiation.md', 2), -- O(log n)
    (68, 'Алгоритм Евклида (НОД)', 'algos/euclidean.md', 2),         -- O(log min(a,b))
    (69, 'Расширенный алгоритм Евклида', 'algos/extended-euclidean.md', 2), -- O(log min(a,b))
    (70, 'Решето Эратосфена', 'algos/sieve-of-eratosthenes.md', 3),  -- O(n log log n)
    (71, 'Решето Аткина', 'algos/atkin-sieve.md', 3),               -- O(n / log log n)
    (72, 'Алгоритм Миллера-Рабина (проверка простоты)', 'algos/miller-rabin.md', 4), -- O(k log³ n)
    (73, 'RSA-алгоритм', 'algos/rsa.md', 4),                        -- O(n³) для ключей
    (74, 'Алгоритм сжатия Хаффмана', 'algos/huffman.md', 3),        -- O(n log n)
    (75, 'Алгоритм LZW', 'algos/lzw.md', 3),                        -- O(n)
    (76, 'Быстрое преобразование Фурье (БПФ)', 'algos/fft.md', 4),   -- O(n log n)
    (77, 'Алгоритм Штрассена (умножение матриц)', 'algos/strassen.md', 4), -- O(n^2.81)
    (78, 'Алгоритм Карпа-Рабина', 'algos/karp-rabin.md', 3),        -- O(n + m)
    (79, 'Муравьиный алгоритм', 'algos/ant-colony.md', 5),          -- O(iterations * n²)
    (80, 'Алгоритм имитации отжига', 'algos/simulated-annealing.md', 5), -- зависит от параметров
    (81, 'Генетический алгоритм', 'algos/genetic.md', 5);           -- зависит от поколений

-- СТРУКТУРЫ ДАННЫХ (ещё)
INSERT INTO algorithm (id, name, file_path, complexity_id) VALUES
    (82, 'Дерево отрезков', 'algos/segment-tree.md', 2),            -- O(log n) на операцию
    (83, 'Дерево Фенвика (BIT)', 'algos/fenwick.md', 2),            -- O(log n) на операцию
    (84, 'Sparse Table', 'algos/sparse-table.md', 1),               -- O(1) на запрос (предподсчёт O(n log n))
    (85, 'Декартово дерево по неявному ключу', 'algos/treap-implicit.md', 2), -- O(log n)
    (86, 'Очередь с приоритетом', 'algos/priority-queue.md', 2),    -- O(log n) вставка/удаление
    (87, 'Стек с минимумом', 'algos/min-stack.md', 1),              -- O(1) все операции
    (88, 'Очередь с минимумом', 'algos/min-queue.md', 1);          -- O(1) амортизированно
