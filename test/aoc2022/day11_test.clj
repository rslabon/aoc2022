(ns aoc2022.day11-test
  (:require [aoc2022.day11 :refer :all]
            [clojure.test :refer :all]))

(def puzzle-input (slurp "resources/day11.txt"))
(def example "Monkey 0:\n  Starting items: 79, 98\n  Operation: new = old * 19\n  Test: divisible by 23\n    If true: throw to monkey 2\n    If false: throw to monkey 3\n\nMonkey 1:\n  Starting items: 54, 65, 75, 74\n  Operation: new = old + 6\n  Test: divisible by 19\n    If true: throw to monkey 2\n    If false: throw to monkey 0\n\nMonkey 2:\n  Starting items: 79, 60, 97\n  Operation: new = old * old\n  Test: divisible by 13\n    If true: throw to monkey 1\n    If false: throw to monkey 3\n\nMonkey 3:\n  Starting items: 74\n  Operation: new = old + 3\n  Test: divisible by 17\n    If true: throw to monkey 0\n    If false: throw to monkey 1")

(deftest parse-monkey-test
  (testing "parse-monkey"
    (let [monkey (parse-monkey "Monkey 0:\n  Starting items: 79, 98\n  Operation: new = old * 19\n  Test: divisible by 23\n    If true: throw to monkey 2\n    If false: throw to monkey 3")]
      (is (= (:id monkey) 0))
      (is (= (:items monkey) [79, 98]))
      (is (= ((:operation monkey) 2) 38))
      (is (= (:divided-by monkey) 23))
      (is (= (:on-true monkey) 2))
      (is (= (:on-false monkey) 3))
      )
    ))

(deftest parse-monkey-test
  (testing "parse-monkey"
    (let [monkey (parse-monkey "Monkey 0:\n  Starting items: 79, 98\n  Operation: new = old * old\n  Test: divisible by 23\n    If true: throw to monkey 2\n    If false: throw to monkey 3")]
      (is (= ((:operation monkey) 2) 4))
      )
    ))
(deftest take-turn-test
  (let [monkeys (parse-monkeys example)]
    (testing "take-turn"
      (is (= (turn-result (:monkeys (take-turn monkeys 1 divide-by-3)))
             {0 [26 27 23 20]
              1 [1046 401 207 167 25 2080]
              2 []
              3 []}))
      (is (= (turn-result (:monkeys (take-turn monkeys 20 divide-by-3)))
             {0 [34 12 10 26 14]
              1 [115 93 53 199 245]
              2 []
              3 []}))
      )))

(deftest part1-test
  (testing "part1"
    (is (= (part1 example) 10605))
    (is (= (part1 puzzle-input) 95472))
    ))

(deftest part2-test
  (testing "part2"
    (is (= (part2 example) 2713310158))
    (is (= (part2 puzzle-input) 17926061332))
    ))
