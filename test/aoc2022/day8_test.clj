(ns aoc2022.day8-test
  (:require [aoc2022.day8 :refer :all]
            [clojure.test :refer :all]))

(def puzzle-input (slurp "resources/day8.txt"))
(def example "30373\n25512\n65332\n33549\n35390")
(def grid [[3 0 3 7 3]
           [2 5 5 1 2]
           [6 5 3 3 2]
           [3 3 5 4 9]
           [3 5 3 9 0]])


(deftest parse-to-grid-test
  (testing "parse-to-grid"
    (is (= (parse-to-grid example) grid))
    )
  )

(deftest get-tree-test
  (testing "get-tree-grid"
    (is (= (get-tree grid 0 0) 3))
    (is (= (get-tree grid 4 0) 3))
    (is (= (get-tree grid 4 4) 0))
    (is (= (get-tree grid 0 4) 3))
    (is (= (get-tree grid 0 -1) -1))
    (is (= (get-tree grid -1 0) -1))
    (is (= (get-tree grid -1 -1) -1))
    (is (= (get-tree grid 0 5) -1))
    (is (= (get-tree grid 5 5) -1))
    (is (= (get-tree grid 5 0) -1))
    )
  )

(deftest shorter?-test
  (testing "shorter?"
    (is (= (shorter? [1] 0) false))
    (is (= (shorter? [3 2] 1) false))
    (is (= (shorter? [0 2] 1) false))
    (is (= (shorter? [-1] 0) true))
    (is (= (shorter? [1] 2) true))
    (is (= (shorter? [1] 1) false))
    (is (= (shorter? [-1 -1 -1] 3) true))
    )
  )

(deftest visible?-test
  (testing "visible?"
    (is (= (visible? grid 0 0) true))
    (is (= (visible? grid 2 0) true))
    (is (= (visible? grid 4 3) true))
    (is (= (visible? grid 4 3) true))
    (is (= (visible? grid 1 1) true))
    (is (= (visible? grid 2 2) false))
    )
  )

(deftest mark-visible-test
  (testing "mark-visible"
    (is (= (mark-visible grid) [[true true true true true]
                                [true true true false true]
                                [true true false true true]
                                [true false true false true]
                                [true true true true true]]))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 grid) 21))
    (is (= (part1 (parse-to-grid puzzle-input)) 1805))
    )
  )

(deftest viewing-distance-test
  (testing "viewing-distance"
    (is (= (viewing-distance [5] 5) 1))
    (is (= (viewing-distance [9] 5) 1))
    (is (= (viewing-distance [3 5] 5) 2))
    (is (= (viewing-distance [3 3] 5) 2))
    (is (= (viewing-distance [3 3] 5) 2))
    (is (= (viewing-distance [4 9] 5) 2))
    (is (= (viewing-distance [4 9] 5) 2))
    )
  )

(deftest score-test
  (testing "score"
    (is (= (score grid 1 2) 4))
    (is (= (score grid 3 2) 8))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 grid) 8))
    (is (= (part2 (parse-to-grid puzzle-input)) 444528))
    )
  )
