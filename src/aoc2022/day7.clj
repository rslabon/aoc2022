(ns aoc2022.day7
  (:require [clojure.string :as str]))

(defn make-node
  [name dir?] (transient {:name name :children [] :size 0 :dir dir? :parent nil})
  )

(defn make-file
  [name size] (assoc! (make-node name false) :size size)
  )

(defn make-dir
  [name] (make-node name true)
  )

(defn add-dir
  [parent name]
  (let [dir (make-dir name)
        parent (assoc! parent :children (conj (parent :children) dir))
        dir (assoc! dir :parent parent)]
    parent)
  )

(defn add-file
  [parent name size]
  (let [file (make-file name size)]
    (assoc! parent :children (conj (parent :children) file)))
  )

(defn cd
  [pwd name]
  (if (= ".." name)
    (if (nil? (pwd :parent))
      pwd
      (pwd :parent))
    (first (filter #(= (% :name) name) (pwd :children)))
    ))

(defn root
  [pwd]
  (if (nil? (pwd :parent))
    pwd
    (root (pwd :parent))
    )
  )

(defn compute-dir-size
  ([pwd] (do (compute-dir-size pwd (transient #{}))
             pwd
             ))
  ([pwd visited]
   (if (pwd :dir)
     (do (assoc! pwd :size (+ (pwd :size) (reduce + 0 (map #(compute-dir-size % visited) (pwd :children)))))
         (pwd :size))
     (do (conj! visited pwd)
         (pwd :size))
     )
   )
  )

(defn parse-commands
  ([input] (parse-commands (str/split input #"\n") nil))
  ([lines pwd]
   (let [line (first lines)]
     (if (empty? lines)
       (let [r (root pwd)]
         (do (compute-dir-size r)
             r
             ))
       (if (str/starts-with? line "$")
         (cond
           (= line "$ ls") (parse-commands (rest lines) pwd)
           (= line "$ cd ..") (parse-commands (rest lines) (cd pwd ".."))
           (= line "$ cd /") (parse-commands (rest lines) (make-dir "/"))
           ;cd <name>
           :else (let [[_ dir-name] (re-matches #"\$ cd (\w+)" line)]
                   (parse-commands (rest lines) (cd pwd dir-name))
                   )
           )
         ;ls content
         (if (str/starts-with? line "dir")
           (let [[_ dir-name] (re-matches #"dir ([\w.]+)" line)
                 already-exists (some #(= % dir-name) (map :name (pwd :children)))]
             (if already-exists
               (parse-commands (rest lines) pwd)
               (parse-commands (rest lines) (add-dir pwd dir-name)))
             )
           (let [[_ file-size file-name] (re-matches #"(\d+) ([\w.]+)" line)
                 already-exists (some #(= % file-name) (map :name (pwd :children)))]
             (if already-exists
               (parse-commands (rest lines) pwd)
               (parse-commands (rest lines) (add-file pwd file-name (read-string file-size)))
               )
             )
           )
         )
       )
     )
   )
  )

(defn filesystem-to-string
  ([pwd] (filesystem-to-string pwd 0))
  ([pwd level]
   (if (nil? pwd)
     ""
     (str (reduce str (repeat level "\t"))
          (if (pwd :dir)
            (str " - " (pwd :name) " (dir, size=" (pwd :size) ")\n" (reduce str (map #(filesystem-to-string % (inc level)) (pwd :children))))
            (str " - " (pwd :name) " (file, size=" (pwd :size) ")\n")
            )
          )))
  )

(defn list-all-dirs
  [pwd]
  (if (nil? pwd)
    []
    (if (pwd :dir)
      (into [pwd] (flatten (map list-all-dirs (pwd :children))))
      []
      )
    )
  )

(defn part1
  [pwd]
  (reduce + 0 (map :size (filter (fn [pwd] (and (pwd :dir) (<= (pwd :size) 100000))) (list-all-dirs pwd))))
  )

(defn part2
  [pwd]
  (let [total 70000000
        used (pwd :size)
        unused (- total used)
        needed (- 30000000 unused)]
  (apply min (map :size (filter (fn [pwd] (and (pwd :dir) (>= (pwd :size) needed))) (list-all-dirs pwd))))
  ))