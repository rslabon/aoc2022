(ns aoc2022.greedy_best_first_search-test
  (:require [aoc2022.greedy_best_first_search :refer :all]
            [clojure.test :refer :all]))

(defn neighbours
  [i] (condp = i
        [0 0] [[1 1] [2 1]]
        [1 1] [[2 2]]
        [2 2] [[3 3]]
        [2 1] [[3 3]]
        [3 3] []
        ))

(defn heuristic
  [[ax ay] [bx by]] (int (+ (Math/abs (- ax bx)) (Math/abs (- ay by)))))

(deftest greedy-best-first-search-test
  (testing "greedy-best-first-search"
    (is (= (greedy-best-first-search neighbours heuristic [0 0] [3 3]) [[0 0] [2 1] [3 3]]))
    )
  )
