(ns aoc2022.day9-test
  (:require [aoc2022.day9 :refer :all]
            [clojure.test :refer :all]))

(def example "R 4\nU 4\nL 3\nD 1\nR 4\nD 1\nL 5\nR 2")
(def large-example "R 5\nU 8\nL 8\nD 3\nR 17\nD 10\nL 25\nU 20")
(def puzzle-input (slurp "resources/day9.txt"))

(deftest move-right-test
  (testing "move-right"
    (is (= (move-right [[0 0]] 4) [[0 0] [1 0] [2 0] [3 0] [4 0]]))
    )
  )

(deftest move-left-test
  (testing "move-left"
    (is (= (move-left [[0 0]] 1) [[0 0] [-1 0]]))
    )
  )

(deftest move-up-test
  (testing "move-up"
    (is (= (move-up [[0 0]] 1) [[0 0] [0 -1]]))
    )
  )

(deftest move-down-test
  (testing "move-down"
    (is (= (move-down [[0 0]] 1) [[0 0] [0 1]]))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 13))
    (is (= (part1 puzzle-input) 5710))
    )
  )

(deftest follow-test
  (testing "follow"
    (is (= (follow [4 -2] [3 0]) [4 -1]))
    (is (= (follow [4 -1] [2 0]) [3 -1]))
    (is (= (follow [4 -2] [3 -1]) [3 -1]))
    (is (= (follow [1 0] [0 0]) [0 0]))
    (is (= (follow [2 0] [0 0]) [1 0]))
    (is (= (follow [1 -3] [2 -4]) [2 -4]))
    (is (= (follow [1 1] [1 1]) [1 1]))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) 1))
    (is (= (part2 large-example) 36))
    (is (= (part2 puzzle-input) 2259))
    ))