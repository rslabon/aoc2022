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

    (is (= (rotate-right-90 (rotate-right-90 (rotate-right-90 (rotate-right-90 grid)))) grid))
    )
  )

(deftest rotate-left-90-test
  (testing "rotate-left-90"
    (is (= (rotate-left-90 grid) [[4 9 14 19]
                                  [3 8 13 18]
                                  [2 7 12 17]
                                  [1 6 11 16]
                                  [0 5 10 15]]))

    (is (= (rotate-left-90 (rotate-left-90 (rotate-left-90 (rotate-left-90 grid)))) grid))
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