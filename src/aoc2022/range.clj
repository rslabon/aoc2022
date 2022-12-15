(ns aoc2022.range)


(defn range-intersect?
  [[_ first-end] [second-start _]]
  (<= second-start first-end)
  )

(defn range-merge
  [[first-start first-end] [second-start second-end]]
  [(min first-start second-start) (max first-end second-end)]
  )

(defn range-manhatan-distance
  [[first-start first-end] [second-start second-end]]
  (+ (abs (- second-start first-start)) (abs (- second-end first-end))))

(defn range-valid?
  [[start end]] (<= start end))

(defn merge-ranges
  "merges ranges as [0 1] [1 2] into [0 2]"
  [ranges]
  (let [ranges (sort-by first ranges)]
    (loop [ranges ranges
           stack (list (first ranges))]
      (if (empty? ranges)
        (sort-by first stack)
        (let [last (first stack)
              current (first ranges)]
          (if (range-intersect? last current)
            (recur (rest ranges) (conj (rest stack) (range-merge last current)))
            (recur (rest ranges) (conj stack current))
            )
          )
        )
      )
    )
  )