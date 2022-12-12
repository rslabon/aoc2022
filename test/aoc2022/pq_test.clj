(ns aoc2022.pq-test
  (:require [aoc2022.pq :refer :all]
            [clojure.test :refer :all]))

(deftest pq-put-test
    (testing "pq-put"
      (is (= (pq-empty? (make-pq)) true))
      (is (= (pq-poll (make-pq)) nil))
      (is (= (let [[item pq] (pq-poll (pq-put (make-pq) \c 3))]
               item) \c))
      (is (= (let [pq (-> (make-pq)
                          (pq-put \c 3)
                          (pq-put \a 1)
                          (pq-put \b 2))
                   [item pq] (pq-poll pq)]
               item) \a))
      (is (= (let [pq (-> (make-pq)
                          (pq-put \a 2)
                          (pq-put \a 1))]
               (pq-size pq)) 1))
      ))
