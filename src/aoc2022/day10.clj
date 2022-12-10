(ns aoc2022.day10
  (:require [clojure.string :as str]))

(defn cycle-seq
  [register commands]
  (loop [register-cycles [register]
         commands commands]
    (if (empty? commands)
      register-cycles
      (let [command (first commands)
            register (last register-cycles)
            register (command register)]
        (recur
          (into register-cycles register)
          (rest commands)
          )
        )
      )
    )
  )

(defn parse-commands
  [input] (let [lines (str/split input #"\n")]
            (mapv (fn [line]
                    (cond
                      (= line "noop") (fn [register] [register])
                      :else (let [[_ value] (re-matches #"addx (-?\d+)" line)]
                              (fn [register] [register (+ register (read-string value))])
                              )
                      ))
                  lines)))

(defn part1
  [input] (let [cycles (cycle-seq 1 (parse-commands input))
                signal-strengths (map #(* % (nth cycles (dec %))) [20 60 100 140 180 220])]
            (reduce + signal-strengths)
            ))

(defn is-sprite-visible?
  [sprite pixel] (= \# (nth sprite pixel)))

(defn move-sprite
  [position]
  (let [prefix (repeat (dec position) ".")
        suffix (repeat (- 40 3 (count prefix)) ".")]
    (str (str/join prefix) "###" (str/join suffix))
    ))

(defn crt-row
  [sprite row]
  (let [pixel (count row)]
    (if (is-sprite-visible? sprite pixel)
      (str row "#")
      (str row ".")
      )
    ))

(defn draw-row
  [cycles sprite]
  (loop [cycles (rest cycles)
         sprite sprite
         current-row ""]
    (if (empty? cycles)
      [current-row sprite]
      (recur (rest cycles)
             (move-sprite (first cycles))
             (crt-row sprite current-row)
             )
      )
    ))

(defn part2
  [input]
  (let [cycles (cycle-seq 1 (parse-commands input))
        [row1 sprite] (draw-row (subvec cycles 0 41) "###.....................................")
        [row2 sprite] (draw-row (subvec cycles 40 81) sprite)
        [row3 sprite] (draw-row (subvec cycles 80 121) sprite)
        [row4 sprite] (draw-row (subvec cycles 120 161) sprite)
        [row5 sprite] (draw-row (subvec cycles 160 201) sprite)
        [row6 _] (draw-row (subvec cycles 200 241) sprite)
        ]
    (str/join "\n" [row1 row2 row3 row4 row5 row6])
    ))