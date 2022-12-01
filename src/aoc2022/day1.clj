(ns aoc2022.day1
  (:require [clojure.string :as str]))

(defn parse-elf-calories
  [input]
  (let [elfs (str/split input #"\n\n")
        elfs (map #(map read-string (str/split-lines %)) elfs)
        elfs (vec elfs)]
    elfs
    ))

(defn total-calories
  [elfs]
  (map #(reduce + 0 %) elfs))

(defn max-calories
  [elfs]
  (apply max (total-calories elfs)))

(defn top-calories
  [n elfs]
  (take-last n (sort (total-calories elfs))))

(defn sum-of-top-calories
  [n elfs]
  (reduce + 0 (top-calories n elfs)))