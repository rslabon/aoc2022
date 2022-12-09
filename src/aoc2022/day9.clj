(ns aoc2022.day9
  (:require [clojure.string :as str]))

(defn manhatan-distance
  [[ax ay] [bx by]]
  [(Math/abs (- ax bx)) (Math/abs (- ay by))]
  )

(defn should-tail-move?
  [[tx ty] [hx hy]]
  (let [[dx dy] (manhatan-distance [tx ty] [hx hy])]
    (> (max dx dy) 1)
    ))

(defn follow
  [[hx hy] [tx ty]]
  (let [dx (- hx tx)
        dy (- hy ty)
        dx (cond (> dx 1) (dec dx)
                 (< dx -1) (inc dx)
                 :else dx)
        dy (cond (> dy 1) (dec dy)
                 (< dy -1) (inc dy)
                 :else dy)]
    (if (should-tail-move? [tx ty] [hx hy])
      [(+ tx dx) (+ ty dy)]
      [tx ty]
      )
    ))

(defn move-right
  ([path n]
   (let [[x y] (last path)]
     (into path (map (fn [i] [(+ i x) y]) (range 1 (inc n))))))
  )

(defn move-left
  ([path n]
   (let [[x y] (last path)]
     (into path (map (fn [i] [(- x i) y]) (range 1 (inc n))))))
  )

(defn move-up
  ([path n]
   (let [[x y] (last path)]
     (into path (map (fn [i] [x (- y i)]) (range 1 (inc n))))))
  )

(defn move-down
  ([path n]
   (let [[x y] (last path)]
     (into path (map (fn [i] [x (+ y i)]) (range 1 (inc n))))))
  )

(defn tail-path
  ([path]
   (loop [path path
          t [0 0]
          tail-path []]
     (if (empty? path)
       tail-path
       (let [t (follow (first path) t)]
         (recur (rest path)
                t
                (if (not= (last tail-path) t)
                  (conj tail-path t)
                  tail-path
                  )
                )
         )
       ))))

(defn head-path
  [move-lines]
  (reduce (fn [path line]
            (let [[_ direction n] (re-matches #"([RLUD]) (\d+)" line)
                  n (read-string n)]
              (condp = direction
                "R" (move-right path n)
                "L" (move-left path n)
                "U" (move-up path n)
                "D" (move-down path n)
                )))
          [[0 0]]
          move-lines)
  )

(defn part1
  [input]
  (let [lines (str/split input #"\n")
        head-path (head-path lines)]
    (count (set (tail-path head-path)))
    )
  )

(defn tail-9-path
  [input]
  (loop [head-path (head-path (str/split input #"\n"))
         t1 [0 0]
         t2 [0 0]
         t3 [0 0]
         t4 [0 0]
         t5 [0 0]
         t6 [0 0]
         t7 [0 0]
         t8 [0 0]
         t9 [0 0]
         path []]
    (if (empty? head-path)
      path
      (let [t1 (follow (first head-path) t1)
            t2 (follow t1 t2)
            t3 (follow t2 t3)
            t4 (follow t3 t4)
            t5 (follow t4 t5)
            t6 (follow t5 t6)
            t7 (follow t6 t7)
            t8 (follow t7 t8)
            t9 (follow t8 t9)]
        (recur (rest head-path)
               t1 t2 t3 t4 t5 t6 t7 t8 t9
               (if (not= (last path) t9)
                 (conj path t9)
                 path)
               )
        )
      )))

(defn part2
  [input]
  (count (set (tail-9-path input))))