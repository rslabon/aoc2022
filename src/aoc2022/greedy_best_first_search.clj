(ns aoc2022.greedy_best_first_search
  (:import (aoc2022 GreedyBestFirstSearch)
           (java.util.function BiFunction Function)))

(defn ^Function as-function [f]
  (reify Function
    (apply [this arg] (f arg))))
(defn ^BiFunction as-bifunction [f]
  (reify BiFunction
    (apply [this arg1 arg2] (f arg1 arg2))))

(defn greedy-best-first-search
  [neighbours-fn heuristic-fn start goal]
  (let [search (new GreedyBestFirstSearch (as-function neighbours-fn) (as-bifunction heuristic-fn))]
    (vec (.path search start goal))
    )
  )