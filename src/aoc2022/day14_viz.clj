(ns aoc2022.day14_viz
  (:require
    [aoc2022.day14 :refer :all]
    [quil.core :as q]
    [quil.middleware :as m]))



(def puzzle-input (slurp "resources/day14.txt"))
(def example "498,4 -> 498,6 -> 496,6\n503,4 -> 502,4 -> 502,9 -> 494,9")

(defn move-point-by-one-step
  [[x y] grid]
  (let [can-move? (fn [p] (not (or (contains? (:rock-points grid) p) (contains? (:sand-points grid) p))))
        down [x (inc y)]
        left-down [(dec x) (inc y)]
        right-down [(inc x) (inc y)]]
    (cond
      (into-abyss? grid [x y]) nil
      (into-floor? grid [x y]) [x y]
      (can-move? down) down
      (can-move? left-down) left-down
      (can-move? right-down) right-down
      :else [x y]
      )
    ))

(defn drop-sand-by-one-step
  ([grid]
   (let [[sx sy] (:sand-source grid)
         last-sand-point (get grid :last-sand-point [sx sy])
         current-sand-point (move-point-by-one-step last-sand-point grid)]
     (cond
       (= last-sand-point current-sand-point) (-> grid
                                                  (dissoc :last-sand-point)
                                                  (update :sand-points conj last-sand-point))
       (nil? current-sand-point) (dissoc grid :last-sand-point)
       :else (assoc grid :last-sand-point current-sand-point)
       )
     ))
  )

(defn setup []
  (q/frame-rate 20)
  (q/background 200)
  (let [grid (parse-grid [500, 0] example)
        grid (assoc grid :abyss (+ 1 (grid-y-max grid)))]
    grid
    )
  )
(defn draw [grid]
  (let [rocks (count (:rock-points grid))
        size (cond
               (< rocks 100) 40
               (< rocks 300) 20
               :else 10)]
    (q/clear)
    (q/stroke 0 0 0)
    (q/background 200)

    (let [[sx sy] (:sand-source grid)
          rocket-points (:rock-points grid)
          x-margin (* 6 size)
          y-margin (* 2 size)
          x-min (apply min (mapv first rocket-points))
          rocket-points (mapv (fn [[x y]] [(- x x-min) (+ y 0)]) rocket-points)
          rocket-points (mapv (fn [[x y]] [(+ x (* x size) x-margin) (+ y (* y size) y-margin)]) rocket-points)
          sand-points (:sand-points grid)
          sand-points (mapv (fn [[x y]] [(- x x-min) (+ y 0)]) sand-points)
          sand-points (mapv (fn [[x y]] [(+ x (* x size) (/ size 2) x-margin) (+ y (* y size) (/ size 2) y-margin)]) sand-points)
          sx (- sx x-min)
          sx (+ sx (* sx size) x-margin)
          sy (+ sy size y-margin)]
      (do
        (q/fill 0 0 0)
        (q/text-size (* size 2))
        (q/text "+" sx sy)

        (q/fill 0 0 0 200)
        (q/text-size 30)
        (q/text (str "count: " (count (:sand-points grid))) (+ sx (* size 4)) (- sy (/ size 2)))

        (q/stroke-weight (int (Math/sqrt size)))
        (q/fill 90 77 65)
        (mapv (fn [[x y]]
                (q/rect x y size size)
                ) rocket-points)

        (q/stroke-weight (int (Math/sqrt size)))
        (q/fill 90 77 65)
        (if (contains? grid :floor)
          (let [floor (:floor grid)
                floor (+ floor (* floor size) y-margin size)
                x-max (apply max (mapv first rocket-points))
                floor-points (for [x (range 0 (- x-max x-min))] [(+ x (* x size)) floor])]
            (mapv (fn [[x y]]
                    (q/rect x y size size)
                    ) floor-points)
            )
          )

        (q/stroke-weight (int (Math/sqrt (/ size 2))))
        (q/fill 194 178 128)
        (mapv (fn [[x y]]
                (q/ellipse x y size size)
                ) sand-points)

        (if (contains? grid :last-sand-point)
          (let [[lx ly] (:last-sand-point grid)
                lx (- lx x-min)
                lx (+ lx (* lx size) x-margin (/ size 2))
                ly (+ ly (* ly size) (/ size 2) y-margin)]
            (q/stroke-weight (int (Math/sqrt (/ size 2))))
            (q/fill 194 178 128)
            (q/ellipse lx ly size size)
            )
          )
        ))
    ))

(defn update-state
  [grid]
  (drop-sand-by-one-step grid)
  )

(q/defsketch day14_viz
             :title "DAY 14"
             :settings #(q/smooth 2)
             :setup setup
             :draw draw
             :update update-state
             :fullscreen true
             :size [1000 1000]
             :middleware [m/fun-mode])
(defn -main [& args])