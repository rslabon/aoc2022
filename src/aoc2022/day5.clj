(ns aoc2022.day5
  (:require [clojure.string :as str]))

(defn get-item-in-stack
  [line n]
  (let [item (str (if (= n 1)
                    (nth line n)
                    (nth line (- (+ (* 3 n) (- n 1)) 2))
                    ))]
    (if (str/blank? item)
      nil
      item)))

(defn parse-stacks
  [input]
  (let [lines (str/split input #"\n")
        size (last (map read-string (str/split (str/trim (last lines)) #"\s+")))
        initial-stacks (reduce #(assoc %1 (inc %2) []) {} (range size))
        lines (drop-last lines)]
    (reduce
      #(reduce
         (fn [m k] (update m (inc k) (fn [v] (filter some? (conj v (get-item-in-stack %2 (inc k)))))))
         %1
         (range size))
      initial-stacks
      lines)))


(defn move-stacks
  [stacks count from to]
  (let [stack-from (vec (stacks from))
        stack-to (vec (stacks to))
        last-item (last stack-from)
        new-stacks (assoc stacks from (drop-last stack-from))
        new-stacks (assoc new-stacks to (conj stack-to last-item))]
    (if (or (= count 0) (nil? last-item))
      stacks
      (move-stacks new-stacks (dec count) from to))))

(defn part1
  [stacks move-lines]
  (let [result-stacks (reduce #(let [[_ count from to] (re-matches #"move (\d+) from (\d+) to (\d+)" %2)
                                     count (read-string count)
                                     from (read-string from)
                                     to (read-string to)]
                                 (move-stacks %1 count from to)
                                 ) stacks move-lines)]
    (reduce str (map #(last (result-stacks %)) (sort (keys result-stacks))))
    )
  )

(defn move-stacks2
  [stacks count from to]
  (let [stack-from (vec (stacks from))
        stack-to (vec (stacks to))
        last-items (vec (take-last count stack-from))
        new-from (vec (drop-last count stack-from))
        new-to (vec (into stack-to last-items))
        new-stacks (assoc stacks from new-from)
        new-stacks (assoc new-stacks to new-to)]
    new-stacks))

(defn part2
  [stacks move-lines]
  (let [result-stacks (reduce #(let [[_ count from to] (re-matches #"move (\d+) from (\d+) to (\d+)" %2)
                                     count (read-string count)
                                     from (read-string from)
                                     to (read-string to)]
                                 (move-stacks2 %1 count from to)
                                 ) stacks move-lines)]
    (reduce str (map #(last (result-stacks %)) (sort (keys result-stacks))))
    )
  )