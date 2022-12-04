(ns aoc2022.day4
  (:require [clojure.string :as str]))

(defn make-pair
  [pair-string]
  (map read-string (str/split pair-string #"-")))
(defn parse-pair
  [line]
  (map make-pair (str/split line #",")))
(defn parse-pairs
  [input]
  (map parse-pair (str/split input #"\n")))

(defn intersection
  [[x1 y1] [x2 y2]]
  (let [[x3 y3] [(max x1 x2) (min y1 y2)]]
    (if (> x3 y3)
      nil
      [x3 y3]
      )))

(defn fully-contains?
  [p1 p2]
  (let [i (intersection p1 p2)]
    (or (= i p1) (= i p2))
    )
  )

(defn part1
  [pairs]
  (count (filter true? (map #(apply fully-contains? %) pairs)))
  )

(defn part2
  [pairs]
  (count (filter some? (map #(apply intersection %) pairs)))
  )