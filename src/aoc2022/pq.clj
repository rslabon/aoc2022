(ns aoc2022.pq
  (:import (java.util HashMap PriorityQueue)))



(defn make-pq
  []
  (let [priority-map (new HashMap)
        pq {:priority-map priority-map
            :queue        (new PriorityQueue (fn [a b] (- (.get priority-map a) (.get priority-map b))))}]
    pq
    ))

(defn pq-empty?
  [pq] (.isEmpty (pq :queue)))

(defn pq-size
  [pq] (.size (pq :queue)))

(defn pq-poll!
  [pq]
  (if (pq-empty? pq)
    nil
    [(.poll (pq :queue)) pq]
    )
  )

(defn pq-put!
  [pq item priority]
  (let [_ (.remove (pq :queue) item)
        _ (.put (pq :priority-map) item priority)
        _ (.remove (pq :queue) item)
        _ (.add (pq :queue) item)]
      pq
    )
  )