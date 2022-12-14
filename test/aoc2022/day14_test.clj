(ns aoc2022.day14-test
  (:require [aoc2022.day14 :refer :all]
            [clojure.test :refer :all]))

(def puzzle-input (slurp "resources/day14.txt"))
(def example "498,4 -> 498,6 -> 496,6\n503,4 -> 502,4 -> 502,9 -> 494,9")

;(def grid (parse-grid [500, 0] puzzle-input))
;(def grid (parse-grid [500, 0] example))
;(println (grid-to-string grid))
;(println (grid-to-string (drop-sand grid 27)))

(deftest parse-line-test
  (testing "parse-line"
    (is (= (parse-line "0,0 -> 3,3") [[0 0] [1 1] [2 2] [3 3]]))
    (is (= (parse-line "0,0 -> 0,3") [[0 0] [0 1] [0 2] [0 3]]))
    (is (= (parse-line "10,5 -> 7,5") [[10 5] [9 5] [8 5] [7 5]]))
    (is (= (parse-line "498,4 -> 498,6") [[498 4] [498 5] [498 6]]))
    (is (= (parse-line "498,6 -> 496,6") [[498 6] [497 6] [496 6]]))
    (is (= (parse-line "503,4 -> 502,4") [[503 4] [502 4]]))
    (is (= (parse-line "502,9 -> 494,9") [[502 9] [501 9] [500 9] [499 9] [498 9] [497 9] [496 9] [495 9] [494 9]]))
    (is (= (parse-line "498,4 -> 498,6 -> 496,6") [[498 4] [498 5] [498 6] [497 6] [496 6]]))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 24))
    (is (= (part1 puzzle-input) 779))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) 93))
    (is (= (part2 puzzle-input) 27426))
    )
  )
