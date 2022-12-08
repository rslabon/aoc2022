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

(defn merge-grid
  [grid1 grid2]
  (loop [rows1 grid1
         rows2 grid2
         merged-grid []]
    (if (or (empty? rows1) (empty? rows2))
      merged-grid
      (recur (rest rows1) (rest rows2) (conj merged-grid (into (first rows1) (first rows2))))
      )
    )
  )

(defn grid-2x
  [grid]
  (let [grid (merge-grid grid grid)
        [top bottom] (repeat 2 grid)]
    (into top bottom)
    ))