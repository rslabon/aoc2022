(ns aoc2022.day6)

(defn find-unique-seq
  [data-stream unique-length]
  (loop [ids (range (- (count data-stream) unique-length))]
    (let [i (first ids)
          w (subs data-stream i (+ i unique-length))]
      (if (= unique-length (count (set w)))
        (+ i unique-length)
        (recur (rest ids))
        )
      )
    )
  )

(defn find-marker
  [data-stream] (find-unique-seq data-stream 4)
  )

(defn find-message
  [data-stream] (find-unique-seq data-stream 14)
  )