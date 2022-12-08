(ns aoc2022.grid-test
  (:require [aoc2022.grid :refer :all]
            [clojure.test :refer :all]))


(def grid [[0 1 2 3 4]
           [5 6 7 8 9]
           [10 11 12 13 14]
           [15 16 17 18 19]])

(deftest rotate-right-90-test
  (testing "rotate-right-90"
    (is (= (rotate-right-90 grid) [[15 10 5 0]
                                   [16 11 6 1]
                                   [17 12 7 2]
                                   [18 13 8 3]
                                   [19 14 9 4]]))
    )
  )

(deftest merge-grid-test
  (testing "merge-grid"
    (is (= (merge-grid grid grid) [[0 1 2 3 4 0 1 2 3 4]
                                   [5 6 7 8 9 5 6 7 8 9]
                                   [10 11 12 13 14 10 11 12 13 14]
                                   [15 16 17 18 19 15 16 17 18 19]]))
    )
  )

(deftest grid-2x-test
  (testing "grid-2x-grid"
    (is (= (grid-2x [[1 2]
                     [3 4]]) [[1 2 1 2]
                              [3 4 3 4]
                              [1 2 1 2]
                              [3 4 3 4]]))
    )
  )