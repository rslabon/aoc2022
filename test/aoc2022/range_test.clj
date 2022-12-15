(ns aoc2022.range-test
  (:require [aoc2022.range :refer :all]
            [clojure.test :refer :all]))

(deftest merge-ranges-test
  (testing "merge-ranges"
    (is (= (merge-ranges [[0 1] [2 3]]) [[0 1] [2 3]]))
    (is (= (merge-ranges [[0 1] [1 3]]) [[0 3]]))
    (is (= (merge-ranges [[0 1] [1 2] [2 3] [3 4] [4 10]]) [[0 10]]))
    (is (= (merge-ranges [[0 1] [1 2] [3 4] [4 10]]) [[0 2] [3 10]]))
    )
  )

(deftest manhatan-distance-test
  (testing "manhatan"
    (= (range-manhatan-distance [1 1] [3 3]) 4)
    ))
