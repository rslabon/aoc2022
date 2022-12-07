(ns aoc2022.day7_ver2-test
  (:require [aoc2022.day7_ver2 :refer :all]
            [clojure.test :refer :all]))

(def example "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")
(def puzzle-input (slurp "resources/day7.txt"))
(deftest cd-test
  (testing "cd"
    (is (= (cd "/a/b/c") "/a/b"))
    (is (= (cd "/a") "/"))
    (is (= (cd "/") "/"))
    (is (= (cd "/a/b/c.exe") "/a/b"))
    )
  )

(def example-filesystem [{:dir  true
                          :path "/"
                          :size 0}
                         {:dir  true
                          :path "/a"
                          :size 0}
                         {:dir  false
                          :path "/b.txt"
                          :size 14848514}
                         {:dir  false
                          :path "/c.dat"
                          :size 8504156}
                         {:dir  true
                          :path "/d"
                          :size 0}
                         {:dir  true
                          :path "/a/e"
                          :size 0}
                         {:dir  false
                          :path "/a/f"
                          :size 29116}
                         {:dir  false
                          :path "/a/g"
                          :size 2557}
                         {:dir  false
                          :path "/a/h.lst"
                          :size 62596}
                         {:dir  false
                          :path "/a/e/i"
                          :size 584}
                         {:dir  false
                          :path "/d/j"
                          :size 4060174}
                         {:dir  false
                          :path "/d/d.log"
                          :size 8033020}
                         {:dir  false
                          :path "/d/d.ext"
                          :size 5626152}
                         {:dir  false
                          :path "/d/k"
                          :size 7214296}])

(deftest parse-to-filesystem-test
  (testing "parse-to-filesystem"
    (is (= (parse-to-filesystem example) example-filesystem))
    )
  )

(deftest compute-sizes-test
  (testing "compute-sizes"
    (let [filesystem-with-size (compute-sizes example-filesystem)]
      (is (= (:size (first (filter #(= (% :path) "/a/e") filesystem-with-size))) 584))
      (is (= (:size (first (filter #(= (% :path) "/a") filesystem-with-size))) 94853))
      (is (= (:size (first (filter #(= (% :path) "/d") filesystem-with-size))) 24933642))
      (is (= (:size (first (filter #(= (% :path) "/") filesystem-with-size))) 48381165))
      )
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 (parse-to-filesystem example)) 95437))
    (is (= (part1 (parse-to-filesystem puzzle-input)) 1642503))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 (parse-to-filesystem example)) 24933642))
    (is (= (part2 (parse-to-filesystem puzzle-input)) 6999588))
    )
  )

