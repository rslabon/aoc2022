(ns aoc2022.day4-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day4 :refer :all]))

(def puzzle-input (slurp "resources/day4.txt"))
(def example "2-4,6-8\n2-3,4-5\n5-7,7-9\n2-8,3-7\n6-6,4-6\n2-6,4-8")

(deftest parse-pairs-test
  (testing "parse-pairs"
    (is (= (parse-pairs example) [[[2 4] [6 8]]
                                  [[2 3] [4 5]]
                                  [[5 7] [7 9]]
                                  [[2 8] [3 7]]
                                  [[6 6] [4 6]]
                                  [[2 6] [4 8]]]))
    )
  )

(deftest intersection-test
  (testing "intersection"
    (is (= (intersection [2 8] [3 7]) [3 7]))
    (is (= (intersection [3 7] [2 8]) [3 7]))
    (is (= (intersection [2 5] [6 8]) nil))
    (is (= (intersection [2 5] [5 5]) [5 5]))
    (is (= (intersection [2 7] [7 9]) [7 7]))
    )
  )

(deftest fully-contains?-test
  (testing "fully-contains?"
    (is (= (fully-contains? [2 8] [3 7]) true))
    (is (= (fully-contains? [3 7] [2 8]) true))
    (is (= (fully-contains? [2 5] [6 8]) false))
    (is (= (fully-contains? [2 5] [5 5]) true))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 (parse-pairs example)) 2))
    (is (= (part1 (parse-pairs puzzle-input)) 456))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 (parse-pairs example)) 4))
    (is (= (part2 (parse-pairs puzzle-input)) 808))
    )
  )
