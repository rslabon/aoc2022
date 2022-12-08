(ns aoc2022.grid)

(defn rotate-right-90
  [grid]
  (loop [column 0
         rotated-grid []]
    (if (<= (count (first grid)) column)
      rotated-grid
      (recur (inc column) (conj rotated-grid (reverse (map #(nth % column) grid))))
      )
    )
  )