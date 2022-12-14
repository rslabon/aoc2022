(ns aoc2022.day14
  (:require [clojure.string :as str]))

(defn diff
  [a b] (if (< a b) (range a (inc b)) (range a (dec b) -1)))

(defn parse-line
  [line]
  (loop [line-coords (mapv #(mapv read-string (str/split % #",")) (str/split line #" -> "))
         point-coords []]
    (if (< (count line-coords) 2)
      point-coords
      (let [[start-x start-y] (first line-coords)
            [end-x end-y] (second line-coords)
            diff-x (diff start-x end-x)
            diff-y (diff start-y end-y)
            new-points (if (= (count diff-x) (count diff-y))
                         (mapv (fn [i j] [i j]) diff-x diff-y)
                         (for [x diff-x y diff-y] [x y])
                         )
            new-points (if (empty? point-coords)
                         new-points
                         (rest new-points)
                         )]
        (recur (rest line-coords) (into point-coords new-points))
        )
      )
    ))

(defn parse-grid
  [sand-source input]
  (let [rock-lines (mapv parse-line (filterv not-empty (str/split input #"\n")))
        rock-points (reduce (fn [rock-points rock-line] (reduce (fn [m point] (conj m point)) rock-points rock-line)) #{} rock-lines)
        sand-points #{}]
    {:rock-points rock-points, :sand-points sand-points :sand-source sand-source}
    )
  )

(defn into-abyss?
  [grid [_ y]]
  (let [max-y (apply max (mapv second (:rock-points grid)))]
    (> y max-y)
    )
  )

(defn move-point
  [[x y] grid]
  (let [can-move? (fn [p] (not (or (contains? (:rock-points grid) p) (contains? (:sand-points grid) p))))
        down [x (inc y)]
        left-down [(dec x) (inc y)]
        right-down [(inc x) (inc y)]]
    (cond
      (into-abyss? grid [x y]) nil
      (can-move? down) (recur down grid)
      (can-move? left-down) (recur left-down grid)
      (can-move? right-down) (recur right-down grid)
      :else [x y]
      )
    ))

(defn drop-sand
  ([grid]
   (let [[sx sy] (:sand-source grid)
         sand-point (move-point [sx sy] grid)]
     (if (nil? sand-point)
       grid
       (update grid :sand-points conj sand-point)
       )))
  ([grid n] ((apply comp (repeat n drop-sand)) grid))
  )

(defn part1
  [input]
  (let [grid (parse-grid [500, 0] input)]
    (loop [grid grid
           n 0]
      (let [next-grid (drop-sand grid)]
        (if (= grid next-grid)
          n
          (recur next-grid (inc n))
          )
        )
      )
    ))

(defn part2
  [input]
  (let [grid (parse-grid [500, 0] input)
        y (+ 2 (apply max (mapv second (:rock-points grid))))
        x-min (apply min (mapv first (:rock-points grid)))
        x-max (apply max (mapv first (:rock-points grid)))
        floor-points (mapv (fn [i] [i y]) (range (- x-min y) (+ x-max y)))
        grid (update grid :rock-points into floor-points)]
    (loop [grid grid
           n 0]
      (let [next-grid (drop-sand grid)]
        (do
          (if (= 0 (mod n 300)) (println "turn=" n))
          (if (= grid next-grid)
            n
            (recur next-grid (inc n))
            )
          ))
      )
    ))