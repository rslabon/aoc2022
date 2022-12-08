(ns aoc2022.day8
  (:require [clojure.string :as str]))

(def EDGE -1)

(defn parse-to-grid
  [input] (mapv #(mapv read-string (str/split % #"")) (str/split input #"\n")))

(defn get-tree
  [grid r c] (let [size (count grid)]
               (if (or (or (< r 0) (>= r size)) (or (< c 0) (>= c size)))
                 EDGE
                 (nth (nth grid r) c)
                 )))

(defn shorter?
  [other-trees tree] (< (apply max other-trees) tree))

(defn visible?
  [grid r c]
  (let [size (count grid)
        tree (get-tree grid r c)
        trees-up (mapv #(get-tree grid (- r %) c) (range 1 (inc size)))
        trees-down (mapv #(get-tree grid (+ r %) c) (range 1 (inc size)))
        trees-left (mapv #(get-tree grid r (- c %)) (range 1 (inc size)))
        trees-right (mapv #(get-tree grid r (+ c %)) (range 1 (inc size)))
        visible (or (shorter? trees-up tree) (shorter? trees-down tree) (shorter? trees-left tree) (shorter? trees-right tree))]
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
        tree (get-tree grid r c)
        trees-up (filter #(> % EDGE) (mapv #(get-tree grid (- r %) c) (range 1 (inc size))))
        trees-down (filter #(> % EDGE) (mapv #(get-tree grid (+ r %) c) (range 1 (inc size))))
        trees-left (filter #(> % EDGE) (mapv #(get-tree grid r (- c %)) (range 1 (inc size))))
        trees-right (filter #(> % EDGE) (mapv #(get-tree grid r (+ c %)) (range 1 (inc size))))
        viewing-score (* (viewing-distance trees-up tree)
                         (viewing-distance trees-down tree)
                         (viewing-distance trees-left tree)
                         (viewing-distance trees-right tree))]
    viewing-score)
  )

(defn part2
  [grid]
  (apply max (flatten (map-indexed (fn [ri r] (map-indexed (fn [ci _] (score grid ri ci)) r)) grid)))
  )