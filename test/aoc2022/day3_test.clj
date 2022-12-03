(ns aoc2022.day3-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day3 :refer :all]))

(def example "vJrwpWtwJgWrhcsFMMfFFhFp\njqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\nPmmdzqPrVvPwwTWBwg\nwMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\nttgJtRGJQctTZtZT\nCrZsJsPPZsGzwwsLwLmpwMDw")
(def puzzle-input (slurp "resources/day3.txt"))

(deftest parse-compartments-test
  (testing "parse-compartments"
    (is (= (parse-compartments example) [
                                         ["vJrwpWtwJgWr" "hcsFMMfFFhFp"]
                                         ["jqHRNqRjqzjGDLGL" "rsFMfFZSrLrFZsSL"]
                                         ["PmmdzqPrV" "vPwwTWBwg"]
                                         ["wMqvLMZHhHMvwLH" "jbvcjnnSBnvTQFn"]
                                         ["ttgJtRGJ" "QctTZtZT"]
                                         ["CrZsJsPPZsGz" "wwsLwLmpwMDw"]]))
    ))

(deftest parse-compartments-test
  (testing "find-intersection"
    (is (= (find-intersection "vJrwpWtwJgWr" "hcsFMMfFFhFp") ["p"]))
    ))

(deftest priority-test
  (testing "priority"
    (is (= (priority "a") 1))
    (is (= (priority "z") 26))
    (is (= (priority "A") 27))
    (is (= (priority "Z") 52))
    ))

(deftest part1-test
  (testing "part1"
    (is (= (part1 (parse-compartments example)) 157))
    (is (= (part1 (parse-compartments puzzle-input)) 7997))
    ))

(deftest parse-groups-test
  (testing "parse-groups"
    (is (= (parse-groups example) [["vJrwpWtwJgWrhcsFMMfFFhFp"
                                     "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                                     "PmmdzqPrVvPwwTWBwg"]
                                   ["wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
                                     "ttgJtRGJQctTZtZT"
                                     "CrZsJsPPZsGzwwsLwLmpwMDw"]]))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 (parse-groups example)) 70))
    (is (= (part2 (parse-groups puzzle-input)) 2545))
    ))
