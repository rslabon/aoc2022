(ns aoc2022.day7_ver2
  (:require [clojure.string :as str]))

(defn make-file
  [path dir? size] {:path path :dir dir? :size size})

(defn cd
  [path]
  (let [parent (subs path 0 (str/last-index-of path "/"))]
    (if (empty? parent)
      "/"
      parent
      )
    )
  )

(defn new-path
  [pwd name] (if (= pwd "/") (str "/" name) (str pwd "/" name)))

(defn parse-to-filesystem
  ([input] (parse-to-filesystem (str/split input #"\n") nil []))
  ([lines pwd filesystem]
   (let [line (first lines)]
     (if (empty? lines)
       filesystem
       (if (str/starts-with? line "$")
         (cond
           (= line "$ ls") (parse-to-filesystem (rest lines) pwd filesystem)
           (= line "$ cd ..") (parse-to-filesystem (rest lines) (cd pwd) filesystem)
           (= line "$ cd /") (parse-to-filesystem (rest lines) "/" (conj filesystem (make-file "/" true 0)))
           ;cd <name>
           :else (let [[_ dir-name] (re-matches #"\$ cd (\w+)" line)]
                   (parse-to-filesystem (rest lines) (new-path pwd dir-name) filesystem)
                   )
           )
         ;ls content
         (if (str/starts-with? line "dir")
           (let [[_ dir-name] (re-matches #"dir ([\w.]+)" line)]
             (parse-to-filesystem (rest lines) pwd (conj filesystem (make-file (new-path pwd dir-name) true 0))))
           (let [[_ file-size file-name] (re-matches #"(\d+) ([\w.]+)" line)]
             (parse-to-filesystem (rest lines) pwd (conj filesystem (make-file (new-path pwd file-name) false (read-string file-size)))))
           )
         )
       )
     )
   )
  )

(defn compute-sizes
  ([filesystem]
   (let [files (map :path (filter #(not (% :dir)) filesystem))
         filesystem (compute-sizes (group-by :path filesystem) files)
         dirs (map :path (filter #(% :dir) filesystem))
         dirs (filter #(not= % "/") dirs)
         dirs (reverse (sort dirs))
         filesystem (compute-sizes (group-by :path filesystem) dirs)
         ]
     filesystem
     )
   )
  ([files-by-path paths]
   (if (empty? paths)
     (map first (vals files-by-path))
     (let [path (first paths)
           parent-path (cd path)
           parent-dir (first (get files-by-path parent-path))
           parent-dir (update parent-dir :size + ((first (get files-by-path path)) :size))
           files-by-path (assoc files-by-path parent-path [parent-dir])
           ]
       (compute-sizes files-by-path (rest paths))
       )
     )
   )
  )

(defn part1
  [filesystem]
  (reduce + 0 (map :size (filter #(and (<= (% :size) 100000) (% :dir)) (compute-sizes filesystem))))
  )

(defn part2
  [filesystem]
  (let [filesystem (compute-sizes filesystem)
        root (first (filter #(= (% :path) "/") filesystem))
        total 70000000
        used (root :size)
        unused (- total used)
        needed (- 30000000 unused)]
    (apply min (map :size (filter #(and (>= (% :size) needed) (% :dir)) filesystem)))
    )
  )
