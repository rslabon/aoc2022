(ns aoc2022.day8
  (:require [clojure.string :as str]))

(defn parse-to-grid
  [input] (mapv #(mapv read-string (str/split % #"")) (str/split input #"\n")))

(defn get-item
  [grid r c] (let [size (count grid)]
               (if (or (or (< r 0) (>= r size)) (or (< c 0) (>= c size)))
                 -1
                 (nth (nth grid r) c)
                 )))

(defn shorter?
  [values value] (< (apply max values) value))

(defn visible?
  [grid r c]
  (let [size (count grid)
        item (get-item grid r c)
        items-up (mapv #(get-item grid (- r %) c) (range 1 (inc size)))
        items-down (mapv #(get-item grid (+ r %) c) (range 1 (inc size)))
        items-left (mapv #(get-item grid r (- c %)) (range 1 (inc size)))
        items-right (mapv #(get-item grid r (+ c %)) (range 1 (inc size)))
        visible (or (shorter? items-up item) (shorter? items-down item) (shorter? items-left item) (shorter? items-right item))]
    visible)
  )


(defn mark-visible
  [grid]
  (map-indexed (fn [ri r] (map-indexed (fn [ci _] (visible? grid ri ci)) r)) grid)
  )

(defn part1
  [grid]
  (count (filter true? (flatten (mark-visible grid))))
  )

(defn viewing-distance
  ([tress tree count]
   (cond (empty? tress) (dec count)
         (>= (first tress) tree) count
         :else (recur (rest tress) tree (inc count)))
   )
  ([tress tree] (viewing-distance tress tree 1))
  )

(defn score
  [grid r c]
  (let [size (count grid)
        item (get-item grid r c)
        items-up (filter #(> % -1) (mapv #(get-item grid (- r %) c) (range 1 (inc size))))
        items-down (filter #(> % -1) (mapv #(get-item grid (+ r %) c) (range 1 (inc size))))
        items-left (filter #(> % -1) (mapv #(get-item grid r (- c %)) (range 1 (inc size))))
        items-right (filter #(> % -1) (mapv #(get-item grid r (+ c %)) (range 1 (inc size))))
        viewing-score (* (viewing-distance items-up item)
                         (viewing-distance items-down item)
                         (viewing-distance items-left item)
                         (viewing-distance items-right item))]
    viewing-score)
  )

(defn part2
  [grid]
  (apply max (flatten (map-indexed (fn [ri r] (map-indexed (fn [ci _] (score grid ri ci)) r)) grid)))
  )