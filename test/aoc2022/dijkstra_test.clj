(ns aoc2022.dijkstra-test
  (:require [aoc2022.dijkstra :refer :all]
            [clojure.test :refer :all]))


;1->2->3->4->5
;1->2->3->5
;1->2->5
(defn adj
  [n]
  (condp = n
    1 [2]
    2 [3 5]
    3 [4 5]
    4 5
    5 []
    ))

(defn constant-cost
  [_ _] 1)

(defn can-move
  [_ _] true)


(deftest shortest-path-test
  (testing "shortest-path"
    (is (= (shortest-path 1 5 adj constant-cost can-move) [1 2 5]))
    )
  )
