(ns aoc2022.day17-test
  (:require [aoc2022.day17 :refer :all]
            [clojure.test :refer :all]))


(def example ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>")
(def puzzle-input (slurp "resources/day17.txt"))

(def grid (make-grid))
(def origin (:origin grid))
(def shape (make-shape 1 (:origin grid)))

(deftest make-shape-test
  (testing "make-shape"
    (is (= (make-shape 1 origin) [[0 0] [1 0] [2 0] [3 0]]))
    (is (= (make-shape 2 origin) [[1 0] [1 -1] [0 -1] [2 -1] [1 -2]]))
    (is (= (make-shape 3 origin) [[0 0] [1 0] [2 0] [2 -1] [2 -2]]))
    (is (= (make-shape 4 origin) [[0 0] [0 -1] [0 -2] [0 -3]]))
    (is (= (make-shape 5 origin) [[0 0] [1 0] [0 -1] [1 -1]]))
    )
  )

(deftest shift-shape-test
  (testing "make-shape"
    (is (= (shape-shift-right shape grid) [[1 0] [2 0] [3 0] [4 0]]))
    (is (= (shape-shift-right (shape-shift-right shape grid) grid) [[1 0] [2 0] [3 0] [4 0]]))

    (is (= (shape-shift-left shape grid) [[-1 0] [0 0] [1 0] [2 0]]))

    (is (= (shape-shift-down shape grid) [[0 1] [1 1] [2 1] [3 1]]))
    (is (= (shape-shift-down 3 shape grid) [[0 3] [1 3] [2 3] [3 3]]))
    (is (= (shape-shift-down 4 shape grid) [[0 3] [1 3] [2 3] [3 3]]))
    )
  )

(deftest exec-command-test
  (testing "exec-command"
    (is (= (exec-command :down grid) [grid [[0 1] [1 1] [2 1] [3 1]]]))
    (is (= (exec-command :left grid) [grid [[-1 0] [0 0] [1 0] [2 0]]]))
    (is (= (exec-command :right grid) [grid [[1 0] [2 0] [3 0] [4 0]]]))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 example 2022) 3068))
    (is (= (part1 puzzle-input 2022) 3224))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 example 1000000000000) 1514285714288))
    (is (= (part2 puzzle-input 1000000000000) 1595988538691N))
    ))
