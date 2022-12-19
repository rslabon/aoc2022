(ns aoc2022.day19-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day19 :refer :all]))

(def blueprint
  {
   :ore [4 0 0 0]
   :cly [2 0 0 0]
   :obsidian [3 14 0 0]
   :geocode [2 0 7 0]
   }
  )

(deftest can-build-robot?-test
  (testing "can-build-robot?"
  (is (= (can-build-robot? [3 12 0 0] (:obsidian blueprint)) false))
  (is (= (can-build-robot? [3 14 0 0] (:obsidian blueprint)) true))
  (is (= (can-build-robot? [3 12 0 0] (:ore blueprint)) false))
  ))

(deftest build-robot-test
  (testing "build-robot"
    (is (= (build-robot blueprint [5 1 0 0] [1 0 0 0] :ore) [[1 1 0 0] [2 0 0 0]]))
    (is (= (build-robot blueprint [3 1 0 0] [1 0 0 0] :ore) [[1 0 0 0] [3 1 0 0]]))
    ))
