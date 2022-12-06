(ns aoc2022.day6-test
  (:require [clojure.test :refer :all])
  (:require [aoc2022.day6 :refer :all]))

(def puzzle-input (slurp "resources/day6.txt"))

(deftest find-marker-test
  (testing "find-marker"
    (is (= (find-marker "abcdz") 4))
    (is (= (find-marker "mjqjpqmgbljsphdztnvjfqwrcgsmlb") 7))
    (is (= (find-marker "bvwbjplbgvbhsrlpgdmjqwftvncz") 5))
    (is (= (find-marker "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") 10))
    (is (= (find-marker "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") 11))
    )
  )

(deftest part1-test
  (testing "part1"
    (is (= (find-marker puzzle-input) 1757))
    )
  )

(deftest find-message-test
  (testing "find-message"
    (is (= (find-message "mjqjpqmgbljsphdztnvjfqwrcgsmlb") 19))
    (is (= (find-message "bvwbjplbgvbhsrlpgdmjqwftvncz") 23))
    (is (= (find-message "nppdvjthqldpwncqszvftbrmjlhg") 23))
    (is (= (find-message "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") 29))
    (is (= (find-message "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") 26))
    )
  )

(deftest part2-test
  (testing "part2"
    (is (= (find-message puzzle-input) 0))
    )
  )