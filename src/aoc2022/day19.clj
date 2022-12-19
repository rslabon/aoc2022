(ns aoc2022.day19)

(defn collected-inc
  [collected robots]
  (map + collected robots)
  )

(defn can-build-robot?
  [collected robot-blueprint]
  (every? #(>= % 0) (map - collected robot-blueprint))
  )

(defn start-building
  [collected robot-blueprint]
  (map - collected robot-blueprint)
  )

(defn build-robot
  [blueprint collected robots robot-type]
  (let [robot-blueprint (get blueprint robot-type)]
    (if (can-build-robot? collected robot-blueprint)
      [(start-building collected robot-blueprint)
       (map + robots (condp = robot-type
                       :ore [1 0 0 0]
                       :cly [0 1 0 0]
                       :obsidian [0 0 1 0]
                       :geocode [0 0 0 1]))]
      [collected robots]
      )
    )
  )

(defn turn
  [minute collected robots]
  (if (= minute 24)
    collected
    0
    )
  )