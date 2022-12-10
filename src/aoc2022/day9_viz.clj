(ns aoc2022.day9_viz
  (:require
    [aoc2022.day9 :refer :all]
    [clojure.string :as str]
    [quil.core :as q]
    [quil.middleware :as m]))

(defn elf-line-paths
  [input number-of-knots]
  (let [head (head-path (str/split input #"\n"))]
    (loop [head-path head
           knots-path (repeat number-of-knots [[0 0]])]
      (if (empty? head-path)
        (into [head] knots-path)
        (let [new-knots-positions
              (loop [current-knots-path (rest knots-path)
                     result [(follow (first head-path) (last (nth knots-path 0)))]]
                (if (empty? current-knots-path)
                  result
                  (recur (rest current-knots-path)
                         (conj result (follow (last result) (last (nth knots-path (count result))))))
                  )
                )]
          (recur
            (rest head-path)
            (map-indexed (fn [idx knot-position] (conj (nth knots-path idx) knot-position)) new-knots-positions))
          ))
      )))


(def SCALE 7)
(def puzzle-input (slurp "resources/day9.txt"))
(defn setup []
  (q/frame-rate 15)
  (q/background 200)
  ;(elf-line-paths "R 5\nU 8\nL 8\nD 3\nR 17\nD 10\nL 25\nU 20" 9)
  (elf-line-paths puzzle-input 9)
  )

(defn draw [state]
  (q/clear)
  (q/stroke 0 0 0)
  (q/stroke-weight 2)
  (q/background 200)

  (if (not (empty? (nth state 0)))
    (let [cx (/ (q/width) 2)
          cy (/ (q/height) 2)]
      (mapv (fn [i]
              (let [p (first (nth state i))]
                (q/fill (+ 20 (* i 25)))
                (q/ellipse (+ cx (* SCALE (first p))) (+ cy (* SCALE (second p))) (- 20 i) (- 20 i))
                )) (range 0 (count state)))
      )
    )
  )

(defn update [state] (mapv rest state))

(q/defsketch day9_viz
             :title "DAY 9"
             :settings #(q/smooth 2)
             :setup setup
             :draw draw
             :update update
             :fullscreen true
             :size [800 800]
             :middleware [m/fun-mode])
(defn -main [& args])