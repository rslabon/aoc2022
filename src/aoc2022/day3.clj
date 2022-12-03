(ns aoc2022.day3
  (:require [clojure.string :as str]
            [clojure.set :as s]))

(defn parse-compartment
  [line]
  (let [length (count line)
        mid (Math/ceil (/ length 2))]
    [(subs line 0 mid) (subs line mid)]
    ))

(defn parse-compartments
  [input]
  (map parse-compartment (str/split input #"\n")))

(defn find-intersection
  [& word]
    (mapv str (apply s/intersection (map set word)))
  )

(defn priority
  [letter]
  (let [c (int (.charAt letter 0))]
  (if (Character/isLowerCase c)
    (- c 96)
    (+ 27 (- c 65))
    )))

(defn part1
  [compartments]
  (reduce + 0 (map #(reduce + 0 (map priority (apply find-intersection %))) compartments))
  )

(defn parse-groups
  [input]
  (let [lines (str/split input #"\n")]
    (partition 3 lines)
    )
  )

(defn part2
  [groups]
  (reduce + 0 (map #(reduce + 0 (map priority (apply find-intersection %))) groups))
  )