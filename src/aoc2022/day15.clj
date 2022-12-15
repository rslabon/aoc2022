(ns aoc2022.day15
  (:require [aoc2022.range :as r]
            [clojure.string :as str]))

(defn parse-zone
  [line]
  (let [[_ sx sy bx by] (re-matches #"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)" line)]
    {:sensor [(read-string sx) (read-string sy)] :beacon [(read-string bx) (read-string by)]}
    ))

(defn parse-zones
  [input]
  (let [lines (str/split input #"\n")
        zones (mapv parse-zone lines)]
    zones
    )
  )

(defn sensor-horizontal-range
  [{beacon :beacon sensor :sensor} row]
  (let [d (r/range-manhatan-distance sensor beacon)
        row-diff (abs (- (second sensor) row))
        d (- d row-diff)]
    [(- (first sensor) d) (+ (first sensor) d)]
    )
  )

(defn sensor-ranges-for-row
  [zones row]
  (filterv r/range-valid? (mapv (fn [zone] (sensor-horizontal-range zone row)) zones))
  )

(defn part1
  [input row]
  (let [zones (parse-zones input)
        ranges (sensor-ranges-for-row zones row)
        [[start end]] (r/merge-ranges ranges)]
    (- end start)
    ))

(defn part2
  [input max]
  (let [zones (parse-zones input)
        ranges-by-row (mapv #(r/merge-ranges (sensor-ranges-for-row zones %)) (range 0 max))
        ranges-with-space (filterv (fn [ranges] (= (count ranges) 2)) ranges-by-row)
        range-with-space (first (filterv (fn [[r1 r2]] (= 1 (- (first r2) (+ (second r1) 1)))) ranges-with-space))
        row (.indexOf ranges-by-row range-with-space)
        col (+ (second (first range-with-space)) 1)]
    (+ (* col 4000000) row)
    ))