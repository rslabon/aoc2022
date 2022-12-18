(ns aoc2022.day17
  (:require [clojure.string :as str]))

(defn shape-hit-other?
  [shape grid]
  (some #(contains? (:points grid) %) shape)
  )

(defn shift-horizontal-shape
  [shape diff grid]
  (let [width (:width grid)
        [ox _] (:origin grid)
        left-max (- ox (:left-distance grid))
        right-max (+ left-max (dec width))
        shifted-shape (mapv (fn [[x y]] [(+ x diff) y]) shape)
        above-max? (some (fn [[x _]] (or (< x left-max) (> x right-max))) shifted-shape)
        hit-other-shape? (shape-hit-other? shifted-shape grid)]
    (if (or above-max? hit-other-shape?)
      shape
      shifted-shape
      )
    )
  )

(defn shape-shift-right
  [shape grid] (shift-horizontal-shape shape 1 grid))

(defn shape-shift-left
  [shape grid] (shift-horizontal-shape shape -1 grid))

(defn shape-hit-floor?
  [shape grid]
  (some #(contains? (:floor grid) %) shape)
  )

(defn grid-new-origin
  [grid]
  (let [[ox oy] (:origin grid)
        ys (mapv second (:points grid))]
    (if (empty? ys)
      [ox oy]
      [ox (- (dec (apply min ys)) (:ceiling-distance grid))]
      ))
  )

(defn shape-shift-down
  ([shape grid]
   (let [shifted-shape (mapv (fn [[x y]] [x (+ 1 y)]) shape)
         hit-floor? (shape-hit-floor? shifted-shape grid)
         hit-other-shape? (shape-hit-other? shifted-shape grid)]
     (if (or hit-floor? hit-other-shape?)
       shape
       shifted-shape
       )
     ))
  ([n shape grid]
   (reduce (fn [s _] (shape-shift-down s grid)) shape (range 0 n))
   )
  )

(defn make-shape
  [nr [ox oy]]
  (let [shapes [[[0 0] [1 0] [2 0] [3 0]]                   ; -
                [[1 0] [1 -1] [0 -1] [2 -1] [1 -2]]         ; +
                [[0 0] [1 0] [2 0] [2 -1] [2 -2]]           ; reverse L
                [[0 0] [0 -1] [0 -2] [0 -3]]                ; |
                [[0 0] [1 0] [0 -1] [1 -1]]]]               ; square
    (mapv (fn [[x y]] [(+ ox x) (+ oy y)]) (nth shapes (dec nr)))
    ))

(defn make-grid
  []
  {
   :ceiling-distance 3
   :left-distance    2
   :width            7
   :origin           [0 0]
   :points           #{}
   :shape-counter    0
   :floor            (set (for [x (range -2 6) y [4]] [x y]))
   })

(defn next-shape
  [grid]
  (let [shape-nr (inc (mod (:shape-counter grid) 5))]
    (make-shape shape-nr (:origin grid))
    ))

(defn exec-command
  [command grid]
  (if (not (contains? grid :shape))
    (recur command (assoc grid :shape (next-shape grid)))
    (let [shape (:shape grid)
          old-shape-count (:shape-counter grid)
          shifted-shape (condp = command
                          :left (shape-shift-left shape grid)
                          :right (shape-shift-right shape grid)
                          :down (shape-shift-down shape grid)
                          )
          grid (if (and (= :down command) (= shifted-shape shape))
                 (-> grid
                     (update :points into shape)
                     (update :shape-counter + 1)
                     (dissoc :shape)
                     )
                 (assoc grid :shape shifted-shape)
                 )
          grid (if (= old-shape-count (:shape-counter grid))
                 grid
                 (assoc grid :origin (grid-new-origin grid))
                 )
          grid (if (contains? grid :shape)
                 grid
                 (assoc grid :shape (next-shape grid))
                 )
          grid (if (and (> (:shape-counter grid) 0) (= (mod (:shape-counter grid) 10) 0))
                 (let [pppp (set (filter (fn [[_ y]] (< y (+ 100 (second (:origin grid))))

                                           ) (:points grid)))]
                   (do
                     ;(println "old " (count (:points grid))  (count pppp))
                     (assoc grid :points pppp)
                     ))
                 grid)
          ]
      grid
      )
    )
  )

(defn draw-grid
  [grid]
  (let [shape (:shape grid)
        [ox oy] (:origin grid)
        left-distance (:left-distance grid)
        width (:width grid)
        margin 4
        s (repeat (+ margin 2 (abs oy) (:ceiling-distance grid)) (range 0 (+ 2 width)))
        s (map-indexed (fn [row-idx row]
                         (map-indexed (fn [col-idx _]
                                        (do
                                          ;(println (:points grid) [(- col-idx left-distance) (- row-idx oy)]  (contains? (:points grid) [(- col-idx left-distance) (- row-idx oy)]) )

                                          (cond
                                            (= col-idx 0) "|"
                                            (= col-idx (inc width)) "|"
                                            (contains? (:floor grid) [(- col-idx left-distance 1) (- row-idx (abs oy) margin)]) "-"
                                            (contains? (:points grid) [(- col-idx left-distance 1) (- row-idx (abs oy) margin)]) "#"
                                            (contains? (set shape) [(- col-idx left-distance 1) (- row-idx (abs oy) margin)]) "@"
                                            :else "."
                                            ))

                                        ) row)) s)
        ]
    (str/join "\n" (mapv str/join s))
    )
  )

(defn grid-height
  [grid]
  (if (empty? (:points grid))
    0
    (let [points (mapv second (:points grid))
          y-max (dec (second (first (:floor grid))))
          y-min (apply min points)]
      (+ 1 (- y-max y-min))
      )
    )
  )

(defn part1
  [input shapes]
  (let [commands (mapv #(condp = %
                          ">" [:right :down]
                          "<" [:left :down]
                          ) (filter #(not (.isBlank %)) (str/split input #"")))
        commands (flatten commands)
        commands (cycle commands)
        grid (make-grid)]
    (loop [commands commands
           i 0
           grid grid]
      (do
        (if (= (:shape-counter grid) shapes)
          (grid-height grid)
          (recur (rest commands) (inc i) (exec-command (first commands) grid))
          )
        )
      )
    ))

(defn lazy-diff
  [grid last-shapes last-height commands]
  (lazy-seq
    (cons [last-shapes last-height]
          (let [new-grid (exec-command (first commands) grid)
                new-height (grid-height new-grid)
                new-shapes (:shape-counter new-grid)]
            (lazy-diff new-grid new-shapes new-height (rest commands))
            )
          ))
  )

(defn diff-height-and-shapes
  [input]
  (let [commands (mapv #(condp = %
                          ">" [:right :down]
                          "<" [:left :down]
                          ) (filter #(not (.isBlank %)) (str/split input #"")))
        commands (flatten commands)
        commands (cycle commands)
        grid (make-grid)
        diff (lazy-diff grid 0 0 commands)
        diff (map (fn [[as ah] [bs bh]] [(- as bs) (- ah bh)]) (rest diff) (drop-last diff))
        diff (filter (fn [[last-shapes _]] (> last-shapes 0)) diff)
        ]
    diff
    ))

(defn cycle-detect
  [array min-values-in-cycle]
  (loop [slow-pointer array
         i 0
         fast-pointer (rest (rest array))]
    (if (= (take min-values-in-cycle slow-pointer) (take min-values-in-cycle fast-pointer))
      i
      (recur (rest slow-pointer) (inc i) (rest (rest fast-pointer)))
      )
    )
  )

(defn part2
  [input n]
  (let [diff (diff-height-and-shapes input)
        cycle (+ 1 (cycle-detect diff 25))
        without-cycle (take cycle diff)
        with-cycle (take (+ 1 cycle) (drop cycle diff))
        without-cycle-shapes (reduce + 0 (map first without-cycle))
        without-cycle-height (reduce + 0 (map second without-cycle))
        cycle-shapes (reduce + 0 (map first with-cycle))
        cycle-height (reduce + 0 (map second with-cycle))
        number-of-cycles (bigint (Math/floor (/ (- n without-cycle-shapes) cycle-shapes)))
        offset-after-cycle (- n without-cycle-shapes (* number-of-cycles cycle-shapes))
        total-cycles (* number-of-cycles cycle-shapes)
        after-cycles (take (+ 0 offset-after-cycle) with-cycle)
        after-cycles-shapes (reduce + 0 (map first after-cycles))
        after-cycle-height (reduce + 0 (map second after-cycles))]
    (+ without-cycle-height (* cycle-height number-of-cycles) after-cycle-height)
    )
  )