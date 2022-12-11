(ns aoc2022.day11
  (:require [clojure.string :as str]))

(defn divided-by?
  [value divisor] (integer? (/ value divisor)))

(defn parse-id
  [line]
  (let [[_ id] (re-matches #"\s*Monkey (\d+):" line)
        id (read-string id)]
    id))

(defn parse-items
  [line]
  (let [[_ items] (re-matches #"\s*Starting items: ((\s*\d+,?)+)" line)
        items (mapv read-string (mapv str/trim (str/split items #",")))]
    items
    )
  )

(defn parse-operation
  [line]
  (let [[_ op val] (re-matches #"\s*Operation: new = old ([\*\+]+) (\w+)" line)
        operation (fn [old] (let [v1 old
                                  v2 (if (= "old" val) old (read-string val))]
                              (condp = op
                                "*" (* v1 v2)
                                "+" (+ v1 v2)
                                )
                              ))]
    operation
    ))

(defn parse-divided-by
  [line]
  (let [[_ divided-by] (re-matches #"\s*Test: divisible by (\d+)" line)
        divided-by (read-string divided-by)]
    divided-by)
  )

(defn parse-on-true
  [line]
  (let [[_ on-true] (re-matches #"\s*If true: throw to monkey (\d+)" line)
        on-true (read-string on-true)]
    on-true)
  )

(defn parse-on-false
  [line]
  (let [[_ on-false] (re-matches #"\s*If false: throw to monkey (\d+)" line)
        on-false (read-string on-false)]
    on-false
    ))

(defn parse-monkey
  [text]
  (let [lines (str/split text #"\n")
        id (parse-id (nth lines 0))
        items (parse-items (nth lines 1))
        operation (parse-operation (nth lines 2))
        divided-by (parse-divided-by (nth lines 3))
        on-true (parse-on-true (nth lines 4))
        on-false (parse-on-false (nth lines 5))]
    {:id id :items items :operation operation :on-true on-true :on-false on-false :divided-by divided-by}
    ))

(defn parse-monkeys
  [input] (mapv parse-monkey (str/split input #"\n\n")))

(defn divide-by-3
  [val] (int (Math/floor (/ val 3))))

(defn turn-result
  [monkeys]
  (reduce #(assoc %1 (:id %2) (:items %2)) {} monkeys)
  )

(defn round
  [monkey-id monkeys-by-id worry-level-fn]
  (loop [items (:items (get monkeys-by-id monkey-id))
         monkeys-by-id monkeys-by-id]
    (if (empty? items)
      monkeys-by-id
      (recur (rest items)
             (let [item (first items)
                   monkey (get monkeys-by-id monkey-id)
                   monkey (update monkey :items rest)
                   new-item ((:operation monkey) item)
                   new-item (worry-level-fn new-item)
                   test-result (divided-by? new-item (:divided-by monkey))
                   throw-to (if test-result (:on-true monkey) (:on-false monkey))
                   monkey-to-throw (get monkeys-by-id throw-to)
                   monkey-to-throw (update monkey-to-throw :items #(conj % new-item))
                   monkeys-by-id (assoc monkeys-by-id
                                   (:id monkey-to-throw) monkey-to-throw
                                   (:id monkey) monkey
                                   )
                   ]
               monkeys-by-id
               )
             )
      )
    )
  )

(defn new-activity
  [monkeys] (vec (repeat (count monkeys) 0)))

(defn take-single-turn
  ([monkeys worry-level-fn]
   (loop [ids (sort (map :id monkeys))
          monkeys-by-id (reduce #(assoc %1 (:id %2) %2) {} monkeys)
          activity (new-activity monkeys)]
     (if (empty? ids)
       {:monkeys (vals monkeys-by-id) :activity activity}
       (let [id (first ids)
             monkey (get monkeys-by-id id)
             activity (update activity id + (count (:items monkey)))]
         (recur (rest ids)
                (round id monkeys-by-id worry-level-fn)
                activity)
         )
       )
     )
   )
  )

(defn take-turn
  ([monkeys number-of-turns worry-level-fn]
   (loop [turn-nr 0
          monkeys monkeys
          activity (new-activity monkeys)]
     (if (= turn-nr number-of-turns)
       {:monkeys monkeys :activity activity}
       (let [next-turn (take-single-turn monkeys worry-level-fn)
             next-turn-activity (:activity next-turn)
             activity (mapv + activity next-turn-activity)]
         (recur (inc turn-nr) (:monkeys next-turn) activity)
         )
       ))
   ))

(defn part1
  [input]
  (let [monkeys (parse-monkeys input)
        activity (:activity (take-turn monkeys 20 divide-by-3))
        monkey-business (apply * (take 2 (reverse (sort activity))))]
    monkey-business
    ))

(defn part2
  [input]
  (let [monkeys (parse-monkeys input)
        divided-by-all (reduce * (mapv :divided-by monkeys))
        activity (:activity (take-turn monkeys 10000 (fn [item] (mod item divided-by-all))))
        monkey-business (apply * (take 2 (reverse (sort activity))))]
    monkey-business
    ))