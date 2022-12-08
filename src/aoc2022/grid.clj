(ns aoc2022.grid)

(defn grid-rotate-right-90
  [grid]
  (loop [column 0
         rotated-grid []]
    (if (<= (count (first grid)) column)
      rotated-grid
      (recur (inc column) (conj rotated-grid (reverse (map #(nth % column) grid))))
      )
    )
  )

(defn grid-rotate-left-90
  [grid]
  (loop [column (dec (count (first grid)))
         rotated-grid []]
    (if (> 0 column)
      rotated-grid
      (recur (dec column) (conj rotated-grid (map #(nth % column) grid)))
      )
    ))

(defn grid-merge
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

(defn grid-increase
  [times grid]
  (let [grid (map #(reduce into [] (repeat times %)) grid)]
    (reduce into [] (repeat times grid))
    ))

(defn grid-get-item
  [grid row column]
  (if (or (> 0 row) (> 0 column) (>= row (count grid)) (>= column (count (first grid))))
    nil
    (nth (nth grid row) column)
    ))

(defn grid-get-items-up
  ([grid row column]
   (let [ids (for [i (range (dec row) -1 -1)] i)]
     (mapv #(grid-get-item grid % column) ids)
     ))
  ([grid row column edge] (conj (grid-get-items-up grid row column) edge))
  )

(defn grid-get-items-down
  ([grid row column]
   (let [ids (for [i (range (inc row) (count grid))] i)]
     (mapv #(grid-get-item grid % column) ids)
     ))
  ([grid row column edge] (conj (grid-get-items-down grid row column) edge))
  )

(defn grid-get-items-left
  ([grid row column]
   (let [ids (for [i (range (dec column) -1 -1)] i)]
     (mapv #(grid-get-item grid row %) ids)
     ))
  ([grid row column edge] (conj (grid-get-items-left grid row column) edge))
  )

(defn grid-get-items-right
  ([grid row column]
   (let [ids (for [i (range (inc column) (count (first grid)))] i)]
     (mapv #(grid-get-item grid row %) ids)
     ))
  ([grid row column edge] (conj (grid-get-items-right grid row column) edge))
  )

(defn grid-get-items-around
  ([grid row column] (filterv (fn [[item]] (some? item)) (grid-get-items-around grid row column nil)))
  ([grid row column edge]
   (let [ids [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]]
     (mapv (fn [[item position]] (if (nil? item) [edge position] [item position]))
           (mapv (fn [[i j]]
                   (let [item-row (+ row i)
                         item-column (+ column j)
                         item (grid-get-item grid item-row item-column)]
                     [item [item-row item-column]]))
                 ids))
     )
   ))

(defn grid-map
  [f grid]
  (map-indexed (fn [row-idx row] (map-indexed (fn [column-idx _] (f row-idx column-idx)) row)) grid)
  )