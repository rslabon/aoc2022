(ns aoc2022.day12
  (:require [aoc2022.dijkstra :as d]
            [clojure.string :as str]))

(defn parse-to-position-map
  [input]
  (let [positions (reduce into (map-indexed (fn [row-idx row]
                                              (vec (map-indexed (fn [col-idx cell] [[row-idx col-idx] cell]) row))
                                              ) (str/split input #"\n")))
        position-map (reduce (fn [m [position value]] (assoc m position value)) {} positions)
        ]
    position-map)
  )

(defn make-adj-fn
  [position-map]
  (fn [[x y]]
    (let [neighbours [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]
          neighbours (filter #(contains? position-map %) neighbours)]
      neighbours
      )
    )
  )

(defn make-cost-fn
  [position-map]
  (fn [current-position next-position]
    (let [current-value (position-map current-position)
          next-value (position-map next-position)
          current-value (if (= \S current-value) (int \a) current-value)]
      (if (and (= current-value \z) (= next-value \E))
        (Integer/MIN_VALUE)
        (let [next-value (if (= \E next-value) (inc (int \z)) next-value)
              diff (- (int next-value) (int current-value))]
          (if (<= diff 1)
            1
            (Integer/MAX_VALUE)
            )
          )
        ))))

(defn make-can-move-fn
  [position-map]
  (fn [current-position next-position]
    (let [current-value (position-map current-position)
          next-value (position-map next-position)
          current-value (if (= \S current-value) (int \a) current-value)
          next-value (if (= \E next-value) (inc (int \z)) next-value)
          diff (- (int next-value) (int current-value))]
      (<= diff 1)
      )))

(defn find-in-position-map
  [position-map value]
  (mapv first (filter (fn [[_ val]] (= val value)) (partition 2 (into [] cat position-map))))
  )

(defn part1
  [input]
  (let [position-map (parse-to-position-map input)
        adj (make-adj-fn position-map)
        cost (make-cost-fn position-map)
        can-move (make-can-move-fn position-map)
        start-position (first (find-in-position-map position-map \S))
        end-position (first (find-in-position-map position-map \E))
        path (d/shortest-path start-position end-position adj cost can-move)]
    (dec (count path))
    ))

(defn part2
  [input]
  (let [position-map (parse-to-position-map input)
        adj (make-adj-fn position-map)
        cost (make-cost-fn position-map)
        can-move (make-can-move-fn position-map)
        start-position (first (find-in-position-map position-map \S))
        a-positions (find-in-position-map position-map \a)
        all-starting-points (into [start-position] a-positions)
        end-position (first (find-in-position-map position-map \E))
        paths-sizes (mapv #(dec (count (d/shortest-path % end-position adj cost can-move))) all-starting-points)
        paths-sizes (filterv #(> % 0) paths-sizes)]
    (apply min paths-sizes)
    ))