(ns aoc2022.day13
  (:require [clojure.string :as str]))



(defn convert
  [left right]
  (let [types (map int? [left right])]
    (cond
      (= types [true false]) [[left] right]
      (= types [false true]) [left [right]]
      :else [left right]
      )))

(defn cmp
  [left right]
  (let [[left right] (convert left right)]
    (cond
      (and (int? left) (int? right)) (cond (< left right) -1
                                           (= left right) 0
                                           :else 1)
      (and (coll? left) (coll? right))
      (loop [result 0
             left left
             right right]
        (cond
          (not= 0 result) result
          (and (empty? left) (empty? right)) 0
          (empty? left) -1
          (empty? right) 1
          :else (recur
                  (cmp (first left) (first right))
                  (rest left)
                  (rest right))
          )
        )
      )
    ))

(defn part1
  [input]
  (let [parts (map #(str/split % #"\n") (str/split input #"\n\n"))
        right-orders (map (fn [[left right]] (cmp (read-string left) (read-string right))) parts)
        ids (map-indexed (fn [idx val] (if (= val -1) (inc idx) -1)) right-orders)
        ids (filter #(not= % -1) ids)]
    (reduce + ids)
    ))

(defn part2
  [input]
  (let [parts (map #(str/split % #"\n") (str/split input #"\n\n"))
        packets (map read-string (flatten parts))
        packets (into packets [[[2]] [[6]]])
        sorted-packets (sort cmp packets)
        ids (map-indexed (fn [idx val] (if (or (= val [[2]]) (= val [[6]])) (inc idx) -1)) sorted-packets)
        ids (filter #(> % -1) ids)]
    (apply * ids)
    ))