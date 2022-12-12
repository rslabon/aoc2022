(ns aoc2022.pq)

(defn make-pq
  [] [])

(defn pq-empty?
  [pq] (empty? pq))

(defn pq-size
  [pq] (count pq))

(defn pq-poll
  [pq]
  (if (pq-empty? pq)
    nil
    [(second (first pq)) (rest pq)]
    )
  )

(defn pq-put
  [pq item priority]
  (let [pq (filter (fn [[_ i]] (not= i item)) pq)]
    (sort-by first (conj pq [priority item]))
    ))