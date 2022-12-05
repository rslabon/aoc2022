(ns aoc2022.day5-test
  (:require [aoc2022.day5 :refer :all]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def stacks {1 ["A" "B" "C"] 2 [] 3 ["D"]})

(def puzzle-input (slurp "resources/day5.txt"))
(def parts (str/split puzzle-input #"\n\n"))
(def puzzle-move-lines (str/split (last parts) #"\n"))
(def puzzle-stacks-lines (first parts))
(def puzzle-stacks (parse-stacks puzzle-stacks-lines))

(deftest get-stack-test
  (testing "get-stack"
    (is (= (get-item-in-stack "[D] [S] [R] [D] [G] [F] [S] [L] [Q]" 1) "D"))
    (is (= (get-item-in-stack "[D] [S] [R] [D] [G] [F] [S] [L] [Q]" 2) "S"))
    (is (= (get-item-in-stack "[D] [S] [R] [D] [G] [F] [S] [L] [Q]" 3) "R"))
    (is (= (get-item-in-stack "[D] [S] [R] [D] [G] [F] [S] [L] [Q]" 4) "D"))
    (is (= (get-item-in-stack "[D] [S] [R] [D] [G] [F] [S] [L] [Q]" 9) "Q"))
    (is (= (get-item-in-stack "[D] [S]                            " 9) nil))
    )
  )

(deftest parse-stacks-test
  (testing "parse-stacks"
    (is (= (parse-stacks "[A]\n1") {1 ["A"]}))
    (is (= (parse-stacks "[A]\n 1 ") {1 ["A"]}))
    (is (= (parse-stacks "[A] [B]\n1   2") {1 ["A"] 2 ["B"]}))
    (is (= (parse-stacks "[A]    \n1   2") {1 ["A"] 2 []}))
    (is (= (parse-stacks "[B]     [M]\n 1   2   3") {1 ["B"] 2 [] 3 ["M"]}))
    )
  )

(deftest move-stacks-test
  (testing "move-stacks"
    (is (= (move-stacks stacks 1 1 2) {1 ["A" "B"] 2 ["C"] 3 ["D"]}))
    (is (= (move-stacks stacks 3 1 2) {1 [] 2 ["C" "B" "A"] 3 ["D"]}))
    (is (= (move-stacks stacks 3 2 1) stacks))
    )
  )

(deftest move-stacks-test
  (testing "move-stacks"
    (is (= (move-stacks stacks 1 1 2) {1 ["A" "B"] 2 ["C"] 3 ["D"]}))
    (is (= (move-stacks stacks 3 1 2) {1 [] 2 ["C" "B" "A"] 3 ["D"]}))
    (is (= (move-stacks stacks 3 2 1) stacks))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 puzzle-stacks puzzle-move-lines) "SHMSDGZVC"))
    )
  )

(deftest move-stacks2-test
  (testing "move-stacks2"
    (is (= (move-stacks2 {1 [] 2 ["A"]} 4 1 2) {1 [] 2 ["A"]}))
    (is (= (move-stacks2 {1 ["B"] 2 ["A"]} 4 1 2) {1 [] 2 ["A" "B"]}))
    (is (= (move-stacks2 {1 ["B" "C"] 2 ["A"]} 4 1 2) {1 [] 2 ["A" "B" "C"]}))
    (is (= (move-stacks2 stacks 3 1 2) {1 [] 2 ["A" "B" "C"] 3 ["D"]}))
    (is (= (move-stacks2 stacks 3 1 3) {1 [] 2 [] 3 ["D" "A" "B" "C"]}))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (part2 {1 ["Z" "N"] 2 ["M" "C" "D"] 3 ["P"]} ["move 1 from 2 to 1"
                                                         "move 3 from 1 to 3"
                                                         "move 2 from 2 to 1"
                                                         "move 1 from 1 to 2"]) "MCD"))
    (is (= (part2 puzzle-stacks puzzle-move-lines) "VRZGHDFBQ"))
    )
  )

