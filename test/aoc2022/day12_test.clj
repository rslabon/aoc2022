(ns aoc2022.day12-test
  (:require [aoc2022.day12 :refer :all]
            [clojure.test :refer :all]))

(def example "Sabqponm\nabcryxxl\naccszExk\nacctuvwj\nabdefghi")
(def puzzle-input (slurp "resources/day12.txt"))

(deftest day12-test
  (let [position-map (parse-to-position-map example)
        adj (make-adj-fn position-map)
        cost (make-cost-fn position-map)
        can-move (make-can-move-fn position-map)]
  (testing "adj"
    (is (= (adj [0 0]) [[1 0] [0 1]]))
    (is (= (adj [1 1]) [[2 1] [0 1] [1 2] [1 0]]))
    )
  (testing "cost"
    (is (= (cost [1 1] [1 0]) 1));b->a
    (is (= (cost [1 1] [2 1]) 1));b->c
    (is (= (cost [2 0] [2 1]) (Integer/MAX_VALUE)));a->c
    (is (= (cost [0 0] [0 1]) 1));S->a
    (is (= (cost [0 1] [0 0]) 1));a->S
    (is (= (cost [2 4] [2 5]) (Integer/MIN_VALUE)));z->E
    (is (= (cost [2 6] [2 5]) (Integer/MAX_VALUE)));x->E
    (is (= (cost [1 5] [0 5]) 1));x->o
    )
  (testing "can-move"
    (is (= (can-move [1 1] [1 0]) true));b->a
    (is (= (can-move [1 1] [2 1]) true));b->c
    (is (= (can-move [2 0] [2 1]) false));a->c
    (is (= (can-move [0 0] [0 1]) true));S->a
    (is (= (can-move [0 1] [0 0]) true));a->S
    (is (= (can-move [2 4] [2 5]) true));z->E
    (is (= (can-move [2 6] [2 5]) false));x->E
    (is (= (can-move [1 5] [0 5]) true));x->o
    )
  ))

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 31))
    (is (= (part1 puzzle-input) 437))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) 29))
    (is (= (part2 puzzle-input) 430))
    )
  )
