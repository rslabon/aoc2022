(ns aoc2022.dijkstra
  (:require [aoc2022.pq :refer :all]))

(defn make-path
  [prev target]
  (loop [n target
         path []]
    (if (nil? n)
      (reverse path)
      (recur (prev n) (conj path n))
      )
    )
  )

(defn shortest-path
  [start end adj-fn cost-fn can-move-fn]
  (loop [pq (-> (make-pq)
                (pq-put! start 0))
         dist-to {start 0}
         prev {start nil}]
    (if (pq-empty? pq)
      (make-path dist-to end)
      (let [[current pq] (pq-poll! pq)]
        (if (= current end)
          (make-path prev end)
          (let [[dist-to prev pq]
                (loop [neighbours (adj-fn current)
                       dist-to dist-to
                       prev prev
                       pq pq]
                  (if (empty? neighbours)
                    [dist-to prev pq]
                    (let [next (first neighbours)
                          existing-next-distance (get dist-to next (Integer/MAX_VALUE))
                          existing-current-distance (dist-to current)
                          new-next-distance (+ existing-current-distance (cost-fn current next))]
                      (if (and (or (not (contains? dist-to next)) (< new-next-distance existing-next-distance)) (can-move-fn current next))
                        (let [dist-to (assoc dist-to next new-next-distance)
                              pq (pq-put! pq next new-next-distance)
                              prev (assoc prev next current)]
                          (recur (rest neighbours) dist-to prev pq)
                          )
                        (recur (rest neighbours) dist-to prev pq)
                        )
                      ))
                  )]
            (recur pq dist-to prev))
          )
        )
      )
    ))