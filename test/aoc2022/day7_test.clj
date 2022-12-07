(ns aoc2022.day7-test
  (:require [aoc2022.day7 :refer :all]
            [clojure.test :refer :all]))

(def example "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")
(def puzzle-input (slurp "resources/day7.txt"))

(deftest add-file-test
  (testing "add-file"
    (let [parent (add-file (make-dir "/") "file.exe" 3546333)]
      (is (= (count (parent :children)) 1))
      )
    )
  )

(println (filesystem-to-string (parse-commands example)))

(deftest part1-test
  (testing "part1"
    (is (= (part1 (parse-commands example)) 95437))
    (is (= (part1 (parse-commands puzzle-input)) 1642503))
  ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 (parse-commands example)) 24933642))
    (is (= (part2 (parse-commands puzzle-input)) 6999588))
    ))
