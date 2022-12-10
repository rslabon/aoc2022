(ns aoc2022.day10-test
  (:require [aoc2022.day10 :refer :all]
            [clojure.test :refer :all]))

(def example "addx 15\naddx -11\naddx 6\naddx -3\naddx 5\naddx -1\naddx -8\naddx 13\naddx 4\nnoop\naddx -1\naddx 5\naddx -1\naddx 5\naddx -1\naddx 5\naddx -1\naddx 5\naddx -1\naddx -35\naddx 1\naddx 24\naddx -19\naddx 1\naddx 16\naddx -11\nnoop\nnoop\naddx 21\naddx -15\nnoop\nnoop\naddx -3\naddx 9\naddx 1\naddx -3\naddx 8\naddx 1\naddx 5\nnoop\nnoop\nnoop\nnoop\nnoop\naddx -36\nnoop\naddx 1\naddx 7\nnoop\nnoop\nnoop\naddx 2\naddx 6\nnoop\nnoop\nnoop\nnoop\nnoop\naddx 1\nnoop\nnoop\naddx 7\naddx 1\nnoop\naddx -13\naddx 13\naddx 7\nnoop\naddx 1\naddx -33\nnoop\nnoop\nnoop\naddx 2\nnoop\nnoop\nnoop\naddx 8\nnoop\naddx -1\naddx 2\naddx 1\nnoop\naddx 17\naddx -9\naddx 1\naddx 1\naddx -3\naddx 11\nnoop\nnoop\naddx 1\nnoop\naddx 1\nnoop\nnoop\naddx -13\naddx -19\naddx 1\naddx 3\naddx 26\naddx -30\naddx 12\naddx -1\naddx 3\naddx 1\nnoop\nnoop\nnoop\naddx -9\naddx 18\naddx 1\naddx 2\nnoop\nnoop\naddx 9\nnoop\nnoop\nnoop\naddx -1\naddx 2\naddx -37\naddx 1\naddx 3\nnoop\naddx 15\naddx -21\naddx 22\naddx -6\naddx 1\nnoop\naddx 2\naddx 1\nnoop\naddx -10\nnoop\nnoop\naddx 20\naddx 1\naddx 2\naddx 2\naddx -6\naddx -11\nnoop\nnoop\nnoop")
(def puzzle-input (slurp "resources/day10.txt"))
(def example-part2-output "##..##..##..##..##..##..##..##..##..##..\n###...###...###...###...###...###...###.\n####....####....####....####....####....\n#####.....#####.....#####.....#####.....\n######......######......######......####\n#######.......#######.......#######.....")

(deftest cycle-seq-test
  (testing "cycle-seq"
    (is (= (cycle-seq 1 (parse-commands "noop\naddx 3\naddx -5")) [1 1 1 4 4 -1]))
    (is (= (count (cycle-seq 1 (parse-commands example))) 241))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 19) 21))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 59) 19))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 99) 18))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 139) 21))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 179) 16))
    (is (= (nth (cycle-seq 1 (parse-commands example)) 219) 18))
    ))

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 13140))
    (is (= (part1 puzzle-input) 14420))
    ))

(deftest is-sprite-visible?-test
  (testing "is-sprite-visible?"
    (is (= (is-sprite-visible? "###....................................." 0) true))
    (is (= (is-sprite-visible? "###....................................." 1) true))
    (is (= (is-sprite-visible? "###....................................." 2) true))
    (is (= (is-sprite-visible? "###....................................." 3) false))
    (is (= (is-sprite-visible? ".###...................................." 0) false))
    (is (= (is-sprite-visible? ".###...................................." 1) true))
    ))

(deftest move-sprite-test
  (testing "move-sprite"
    (is (= (move-sprite 1) "###....................................."))
    (is (= (move-sprite 2) ".###...................................."))
    (is (= (move-sprite 3) "..###..................................."))
    (is (= (move-sprite 38) ".....................................###"))
    ))

(deftest crt-row-test
  (testing "crt-row"
    (is (= (crt-row "###....................................." "") "#"))
    (is (= (crt-row "...............###......................" "##") "##."))
    (is (= (crt-row ".......###.............................." "##..##..") "##..##..#"))
    ))

(deftest draw-row-test
  (testing "draw-row"
    (is (= (draw-row
             [1 1 16 16 5 5 11 11 8 8 13 13 12 12 4 4 17 17 21 21 21 20]
             "###.....................................")
           ["##..##..##..##..##..#"
            "...................###.................."]))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) example-part2-output))))

(println (part2 puzzle-input))