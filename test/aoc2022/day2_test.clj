(ns aoc2022.day2-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day2 :refer :all]))

(def puzzle-input (slurp "resources/day2.txt"))

(deftest parse-strategy-test
  (testing "parse-strategy"
    (is (= (parse-strategy "A Y\nB X\nC Z") [["A" "Y"] ["B" "X"] ["C" "Z"]]))
    )
  )

(deftest score-strategy-test
  (testing "score-strategy"
    (is (= (score-strategy [["A" "Y"] ["B" "X"] ["C" "Z"]]) 15))
    ))

(deftest part1-test
  (testing "part1"
    (is (= (score-strategy (parse-strategy puzzle-input)) 15691))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (score-strategy2 (parse-strategy puzzle-input)) 12989))
    ))
