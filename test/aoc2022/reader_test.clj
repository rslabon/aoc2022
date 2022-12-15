(ns aoc2022.reader_test
  (:require
    [clojure.string :as str]
    [clojure.test :refer :all]))

(defn read-arrays
  [input]
  (loop [chars (str/split input #"")
         stack (list)
         val nil]
    (let [c (first chars)]
      (cond
        (empty? chars)
        (first stack)

        (str/includes? "1234567890" c)
        (recur (rest chars) stack (str val c))

        (and (contains? #{"[" "]" "," " "} c) (not-empty val))
        (recur chars (conj stack (Integer/parseInt val)) nil)

        (= c " ")
        (recur (rest chars) stack nil)

        (= c "[")
        (recur (rest chars) (conj stack []) nil)

        (and (= c "]") (> (count stack) 1))
        (recur (rest chars) (conj (rest (rest stack))
                                  (conj (first (rest stack)) (first stack))) nil)

        (and (= c "]") (= (count stack) 1))
        (recur (rest chars) stack nil)

        (= c ",")
        (recur (rest chars) (conj
                              (rest (rest stack))
                              (conj (first (rest stack)) (first stack))) nil)

        )
      )
    )
  )


(defn reader
  [input]
  (read-arrays input))

(deftest reader-test
  (testing "reader"
    (is (= (reader "[]") []))
    (is (= (reader "[1]") [1]))
    (is (= (reader "[1,2]") [1, 2]))
    (is (= (reader "[1,[2]]") [1, [2]]))
    (is (= (reader "[[ [[1], 2],3 ],4 ]") [[[[1], 2], 3], 4]))
    ))

