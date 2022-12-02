(ns aoc2022.day2
  (:require [clojure.string :as str]))

(defn parse-strategy
  [input]
  (map #(str/split % #"\s+") (str/split input #"\n")))

(def score-table {"A" 1, "B" 2, "C" 3, "X" 1, "Y" 2, "Z" 3})

(defn score
  [p1-score p2-score]
    (cond
      (= p1-score p2-score) (+ 3 p1-score)
      (= [p1-score p2-score] [1 2]) (+ 6 p2-score)
      (= [p1-score p2-score] [2 1]) p2-score
      (= [p1-score p2-score] [2 3]) (+ 6 p2-score)
      (= [p1-score p2-score] [3 2]) p2-score
      (= [p1-score p2-score] [3 1]) (+ 6 p2-score)
      (= [p1-score p2-score] [1 3]) p2-score
      )
  )

(defn score-part1
  [player1 player2]
  (let [p1-score (get score-table player1)
        p2-score (get score-table player2)]
    (score p1-score p2-score)
  ))

(defn score-strategy
  [strategy]
  (reduce + 0 (map #(apply score-part1 %) strategy))
  )

(defn lose
  [score]
  (condp = score
    1 3
    2 1
    3 2
    ))

(defn win
  [score]
  (condp = score
    1 2
    2 3
    3 1
    ))

(defn score-part2
  [player1 player2]
  (let [p1-score (get score-table player1)
        p2-score (condp = player2
                   "Y" p1-score
                   "X" (lose p1-score)
                   "Z" (win p1-score)
                   )]
    (score p1-score p2-score)
  ))

(defn score-strategy2
  [strategy]
  (reduce + 0 (map #(apply score-part2 %) strategy))
  )