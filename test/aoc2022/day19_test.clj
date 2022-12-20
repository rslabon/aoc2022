(ns aoc2022.day19-test
  (:require [aoc2022.day19 :refer :all]
            [clojure.test :refer :all]))

(def puzzle-input (slurp "resources/day19.txt"))

(def blueprint-1
  {
   :id       1
   :ore      {:ore 4 :cly 0 :obsidian 0 :geode 0}
   :cly      {:ore 2 :cly 0 :obsidian 0 :geode 0}
   :obsidian {:ore 3 :cly 14 :obsidian 0 :geode 0}
   :geode  {:ore 2 :cly 0 :obsidian 7 :geode 0}
   }
  )

(def blueprint-2
  {
   :id       2
   :ore      {:ore 2 :cly 0 :obsidian 0 :geode 0}
   :cly      {:ore 3 :cly 0 :obsidian 0 :geode 0}
   :obsidian {:ore 3 :cly 8 :obsidian 0 :geode 0}
   :geode  {:ore 3 :cly 0 :obsidian 12 :geode 0}
   }
  )

(deftest can-build-robot?-test
  (testing "can-build-robot?"
    (is (= (can-build-robot? {:ore 3 :cly 12 :obsidian 0 :geode 0} blueprint-1 :obsidian) false))
    (is (= (can-build-robot? {:ore 3 :cly 14 :obsidian 0 :geode 0} blueprint-1 :obsidian) true))
    (is (= (can-build-robot? {:ore 3 :cly 12 :obsidian 0 :geode 0} blueprint-1 :ore) false))
    ))

(deftest build-robot-test
  (testing "build-robot"
    (is (= (build-robot blueprint-1
                        {:ore 5 :cly 1 :obsidian 0 :geode 0}
                        {:ore 1 :cly 0 :obsidian 0 :geode 0} :ore)
           [{:ore 1 :cly 1 :obsidian 0 :geode 0}
            {:ore 2 :cly 0 :obsidian 0 :geode 0}]))
    (is (= (build-robot blueprint-1
                        {:ore 3 :cly 1 :obsidian 0 :geode 0}
                        {:ore 1 :cly 0 :obsidian 0 :geode 0} :ore)
           [{:ore 3 :cly 1 :obsidian 0 :geode 0}
            {:ore 1 :cly 0 :obsidian 0 :geode 0}]))
    ))

(deftest collected-inc-test
  (testing "collected-inc"
    (is (= (collected-inc {:ore 0 :cly 0 :obsidian 0 :geode 0} {:ore 1 :cly 0 :obsidian 0 :geode 0}) {:ore 1 :cly 0 :obsidian 0 :geode 0}))
    ))

(deftest turn-test
  (testing "turn"
    (is (= (turn 0
                 {:ore 0 :cly 0 :obsidian 0 :geode 0}
                 {:ore 1 :cly 0 :obsidian 0 :geode 0}
                 blueprint-1)
           9))
    ))

(deftest parse-blueprint-test
  (testing "parse-blueprint"
    (is (= (parse-blueprint "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 14 clay. Each geode robot costs 3 ore and 16 obsidian.")
           {:cly      {:cly      0
                       :geode  0
                       :obsidian 0
                       :ore      4}
            :geode  {:cly      0
                       :geode  0
                       :obsidian 16
                       :ore      3}
            :id       1
            :obsidian {:cly 14
                       :ore 4}
            :ore      {:cly      0
                       :geode  0
                       :obsidian 0
                       :ore      4}}
           ))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (part1 [blueprint-2]) 33))
    ))