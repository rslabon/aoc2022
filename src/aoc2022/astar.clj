(ns aoc2022.astar
  (:import (aoc2022 AStar)
           (java.util.function BiFunction Function)))

(defn ^Function as-function [f]
  (reify Function
    (apply [this arg] (f arg))))
(defn ^BiFunction as-bifunction [f]
  (reify BiFunction
    (apply [this arg1 arg2] (f arg1 arg2))))

(defn a-star
  [neighbours-fn cost-fn heuristic-fn start goal]
  (let [astar (new AStar (as-function neighbours-fn) (as-bifunction cost-fn) (as-bifunction heuristic-fn))]
    (vec (.path astar start goal))
    )
  )
