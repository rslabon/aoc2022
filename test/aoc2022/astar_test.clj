(ns aoc2022.astar-test
  (:require [aoc2022.astar :refer :all]
            [clojure.test :refer :all]))

(defn neighbours
  [i] (condp = i
        [0 0] [[1 1] [2 1]]
        [1 1] [[2 2]]
        [2 2] [[3 3]]
        [2 1] [[3 3]]
        [3 3] []
        ))

(defn cost
  [i j] 1)

(defn heuristic
  [[ax ay] [bx by]] (+ (Math/abs (- ax bx)) (Math/abs (- ay by))))

(deftest astar-test
  (testing "astar"
    (is (= (a-star neighbours cost heuristic [0 0] [3 3]) [[0 0] [2 1] [3 3]]))
    )
  )