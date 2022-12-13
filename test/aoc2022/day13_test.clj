(ns aoc2022.day13-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day13 :refer :all]))

(def example "[1,1,3,1,1]\n[1,1,5,1,1]\n\n[[1],[2,3,4]]\n[[1],4]\n\n[9]\n[[8,7,6]]\n\n[[4,4],4,4]\n[[4,4],4,4,4]\n\n[7,7,7,7]\n[7,7,7]\n\n[]\n[3]\n\n[[[]]]\n[[]]\n\n[1,[2,[3,[4,[5,6,7]]]],8,9]\n[1,[2,[3,[4,[5,6,0]]]],8,9]")
(def puzzle-input (slurp "resources/day13.txt"))


(deftest cmp-test
  (testing "cmp"
    (is (= (cmp 1 2) -1))
    (is (= (cmp 1 1) 0))
    (is (= (cmp 2 1) 1))

    (is (= (cmp [1] [2]) -1))
    (is (= (cmp [1 1] [2 2]) -1))
    (is (= (cmp [1 3] [2 2]) -1))
    (is (= (cmp [2] [1]) 1))
    (is (= (cmp [1] [2 2]) -1))
    (is (= (cmp [1 1 1] [2 2]) -1))

    (is (= (cmp [1] 2) -1))
    (is (= (cmp 1 [2]) -1))

    (is (= (cmp [[[]]] [[]]) 1))
    (is (= (cmp [[]] []) 1))
    (is (= (cmp [] []) 0))

    (is (= (cmp [2 3 4] 4) -1))
    )
  (testing "example"
    (is (= (cmp [1,1,3,1,1] [1,1,5,1,1]) -1))
    (is (= (cmp [[1],[2,3,4]] [[1],4]) -1))
    (is (= (cmp [9] [[8,7,6]]) 1))
    (is (= (cmp [[4,4],4,4] [[4,4],4,4,4]) -1))
    (is (= (cmp [7,7,7,7] [7,7,7]) 1))
    (is (= (cmp [] [3]) -1))
    (is (= (cmp [[[]]] [[]]) 1))
    (is (= (cmp [1,[2,[3,[4,[5,6,7]]]],8,9] [1,[2,[3,[4,[5,6,0]]]],8,9]) 1))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 13))
    (is (= (part1 puzzle-input) 5882))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) 140))
    (is (= (part2 puzzle-input) 24948))
    ))