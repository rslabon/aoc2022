(ns aoc2022.grid-test
  (:require [aoc2022.grid :refer :all]
            [clojure.test :refer :all]))


(def grid [[0 1 2 3 4]
           [5 6 7 8 9]
           [10 11 12 13 14]
           [15 16 17 18 19]])

(deftest grid-rotate-right-90-test
  (testing "grid-rotate-right-90"
    (is (= (grid-rotate-right-90 grid) [[15 10 5 0]
                                        [16 11 6 1]
                                        [17 12 7 2]
                                        [18 13 8 3]
                                        [19 14 9 4]]))

    (is (= (grid-rotate-right-90 (grid-rotate-right-90 (grid-rotate-right-90 (grid-rotate-right-90 grid)))) grid))
    )
  )

(deftest grid-rotate-left-90-test
  (testing "grid-rotate-left-90"
    (is (= (grid-rotate-left-90 grid) [[4 9 14 19]
                                       [3 8 13 18]
                                       [2 7 12 17]
                                       [1 6 11 16]
                                       [0 5 10 15]]))

    (is (= (grid-rotate-left-90 (grid-rotate-left-90 (grid-rotate-left-90 (grid-rotate-left-90 grid)))) grid))
    )
  )

(deftest grid-merge-test
  (testing "grid-merge"
    (is (= (grid-merge grid grid) [[0 1 2 3 4 0 1 2 3 4]
                                   [5 6 7 8 9 5 6 7 8 9]
                                   [10 11 12 13 14 10 11 12 13 14]
                                   [15 16 17 18 19 15 16 17 18 19]]))
    )
  )

(deftest grid-increase-test
  (testing "grid-increase-grid"
    (is (= (grid-increase 2 [[1 2]
                             [3 4]]) [[1 2 1 2]
                                      [3 4 3 4]
                                      [1 2 1 2]
                                      [3 4 3 4]]))

    (is (= (grid-increase 3 [[1 2]
                             [3 4]]) [[1 2 1 2 1 2]
                                      [3 4 3 4 3 4]
                                      [1 2 1 2 1 2]
                                      [3 4 3 4 3 4]
                                      [1 2 1 2 1 2]
                                      [3 4 3 4 3 4]]))
    )
  )

(deftest grid-get-items-up-test
  (testing "grid-get-items-up"
    (is (= (grid-get-items-up grid 0 0) []))
    (is (= (grid-get-items-up grid 2 2) [7 2]))
    (is (= (grid-get-items-up grid 2 2 -1) [7 2 -1]))
    )
  )

(deftest grid-get-items-down-test
  (testing "grid-get-items-down"
    (is (= (grid-get-items-down grid 3 3) []))
    (is (= (grid-get-items-down grid 1 2) [12 17]))
    (is (= (grid-get-items-down grid 1 2 -1) [12 17 -1]))
    )
  )

(deftest grid-get-items-left-test
  (testing "grid-get-items-left"
    (is (= (grid-get-items-left grid 0 0) []))
    (is (= (grid-get-items-left grid 2 3) [12 11 10]))
    (is (= (grid-get-items-left grid 2 3 -1) [12 11 10 -1]))
    )
  )

(deftest grid-get-items-right-test
  (testing "grid-get-items-right"
    (is (= (grid-get-items-right grid 3 4) []))
    (is (= (grid-get-items-right grid 2 1) [12 13 14]))
    (is (= (grid-get-items-right grid 2 1 -1) [12 13 14 -1]))
    )
  )

(deftest grid-get-items-around-test
  (testing "grid-get-items-around"
    (is (= (grid-get-items-around grid 1 1) [[0 [0 0]]
                                             [1 [0 1]]
                                             [2 [0 2]]
                                             [5 [1 0]]
                                             [7 [1 2]]
                                             [10 [2 0]]
                                             [11 [2 1]]
                                             [12 [2 2]]]))

    (is (= (grid-get-items-around grid 0 0) [[1 [0 1]]
                                             [5 [1 0]]
                                             [6 [1 1]]]))

    (is (= (grid-get-items-around grid 0 0 -1) [[-1 [-1 -1]]
                                                [-1 [-1 0]]
                                                [-1 [-1 1]]
                                                [-1 [0 -1]]
                                                [1 [0 1]]
                                                [-1 [1 -1]]
                                                [5 [1 0]]
                                                [6 [1 1]]]))
    )
  )

(deftest grid-map-test
  (testing "grid-map"
    (is (= (grid-map (fn [row column] [row column]) grid) [[[0 0] [0 1] [0 2] [0 3] [0 4]]
                                                           [[1 0] [1 1] [1 2] [1 3] [1 4]]
                                                           [[2 0] [2 1] [2 2] [2 3] [2 4]]
                                                           [[3 0] [3 1] [3 2] [3 3] [3 4]]])
        )
    ))