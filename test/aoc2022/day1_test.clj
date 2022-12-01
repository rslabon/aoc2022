(ns aoc2022.day1-test
  (:require [clojure.test :refer :all]
            [aoc2022.day1 :refer :all]))

(def input-puzzle (slurp "resources/day1.txt"))

;(println input-puzzle)

(deftest parse-test
  (testing "parse-test"
    (is (= (parse-elf-calories "1\n2\n3\n\n4\n") [[1 2 3] [4]]))
    ))

(deftest total-test
  (testing "total-test"
    (is (= (total-calories [[1 2 3] [4]]) [6 4]))
    ))

(deftest max-test
  (testing "max-test"
    (is (= (max-calories [[1 2 3] [4]]) 6))
    ))

(deftest part1-test
  (testing "part1-test"
    (is (= (max-calories (parse-elf-calories input-puzzle)) 70509))
    ))

(deftest top-test
  (testing "top-test"
    (is (= (top-calories 3 [[1] [2] [3] [4]]) [2 3 4]))
    ))

(deftest sum-of-top-test
  (testing "sum-of-top-test"
    (is (= (sum-of-top-calories 3 [[1] [2] [3] [4]]) 9))
    ))

(deftest part2-test
  (testing "part2-test"
    (is (= (sum-of-top-calories 3 (parse-elf-calories input-puzzle)) 208567))
    ))
