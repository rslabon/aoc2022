(ns aoc2022.day10_viz
  (:require
    [aoc2022.day10 :refer :all]
    [clojure.string :as str]
    [quil.core :as q]
    [quil.middleware :as m]))
(def puzzle-input (slurp "resources/day10.txt"))
(defn setup []
  (q/frame-rate 1)
  (q/background 200)
  (part2 puzzle-input)
  )

(def SCALE 20)

(defn draw [state]
  (q/stroke 0 0 0)
  (q/stroke-weight 2)
  (q/fill 0)

  (let [screen (mapv #(str/split % #"") (str/split state #"\n"))
        coords-to-lit (map-indexed
                        (fn [row-idx row] (map-indexed
                                            (fn [column-idx value]
                                              (if (= "#" value) {:x column-idx :y row-idx} nil)
                                              )
                                            row))
                        screen
                        )
        coords-to-lit (filter some? (flatten coords-to-lit))]

    (let [cx (- (/ (q/width) 2) 380)
          cy (- (/ (q/height) 2) 50)]
      (mapv (fn [coord] (q/ellipse (+ cx (* (:x coord) SCALE)) (+ cy (* (:y coord) SCALE)) 12 12)) coords-to-lit)
      )
    )
  )

(q/defsketch day10_viz
             :title "DAY 10"
             :settings #(q/smooth 2)
             :setup setup
             :draw draw
             :fullscreen true
             :size [800 800]
             :middleware [m/fun-mode])
(defn -main [& args])