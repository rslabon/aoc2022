(ns aoc2022.day15-test
  (:require [aoc2022.day15 :refer :all]
            [clojure.test :refer :all]))

(def example "Sensor at x=2, y=18: closest beacon is at x=-2, y=15\nSensor at x=9, y=16: closest beacon is at x=10, y=16\nSensor at x=13, y=2: closest beacon is at x=15, y=3\nSensor at x=12, y=14: closest beacon is at x=10, y=16\nSensor at x=10, y=20: closest beacon is at x=10, y=16\nSensor at x=14, y=17: closest beacon is at x=10, y=16\nSensor at x=8, y=7: closest beacon is at x=2, y=10\nSensor at x=2, y=0: closest beacon is at x=2, y=10\nSensor at x=0, y=11: closest beacon is at x=2, y=10\nSensor at x=20, y=14: closest beacon is at x=25, y=17\nSensor at x=17, y=20: closest beacon is at x=21, y=22\nSensor at x=16, y=7: closest beacon is at x=15, y=3\nSensor at x=14, y=3: closest beacon is at x=15, y=3\nSensor at x=20, y=1: closest beacon is at x=15, y=3")
(def puzzle-input (slurp "resources/day15.txt"))

(deftest parse-zones-test
  (testing "parse-zones"
    (is (= (parse-zones "Sensor at x=2, y=18: closest beacon is at x=-2, y=15") [{:sensor [2 18] :beacon [-2 15]}]))
    (is (= (parse-zones example) [{:beacon [-2 15]
                                   :sensor [2 18]}
                                  {:beacon [10 16]
                                   :sensor [9 16]}
                                  {:beacon [15 3]
                                   :sensor [13 2]}
                                  {:beacon [10 16]
                                   :sensor [12 14]}
                                  {:beacon [10 16]
                                   :sensor [10 20]}
                                  {:beacon [10 16]
                                   :sensor [14 17]}
                                  {:beacon [2 10]
                                   :sensor [8 7]}
                                  {:beacon [2 10]
                                   :sensor [2 0]}
                                  {:beacon [2 10]
                                   :sensor [0 11]}
                                  {:beacon [25 17]
                                   :sensor [20 14]}
                                  {:beacon [21 22]
                                   :sensor [17 20]}
                                  {:beacon [15 3]
                                   :sensor [16 7]}
                                  {:beacon [15 3]
                                   :sensor [14 3]}
                                  {:beacon [15 3]
                                   :sensor [20 1]}]))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 example 10) 26))
    (is (= (part1 puzzle-input 2000000) 5809294))
    ))

(deftest part2-test
  (testing "part1"
    (is (= (part2 example 20) 56000011))
    (is (= (part2 puzzle-input 4000000) 10693731308112))
    ))

