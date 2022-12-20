(ns aoc2022.day19)

(defn collected-inc
  [collected robots]
  (reduce (fn [m type] (update m type + (get robots type))) collected (keys collected))
  )

(defn consume-collected-for-build
  [collected robot-blueprint]
  (apply hash-map (flatten (map (fn [[k v]] [k (- (get collected k) v)]) robot-blueprint)))
  )

(defn can-build-robot?
  [collected blueprint type]
  (let [robot-blueprint (get blueprint type)]
    (every? #(>= % 0) (vals (consume-collected-for-build collected robot-blueprint)))
    ))

(defn build-robot
  [blueprint collected robots type]
  (let [robot-blueprint (get blueprint type)]
    (if (can-build-robot? collected blueprint type)
      [(consume-collected-for-build collected robot-blueprint) (update robots type + 1)]
      [collected robots]
      )
    )
  )

(def turn
  (memoize
    (fn
      [minute collected robots blueprint]
      (let [geode-blueprint (get blueprint :geode)
            obsidian-blueprint (get blueprint :obsidian)
            cly-blueprint (get blueprint :cly)]
        (do
          (println "minute=" minute " collected=" collected " robots=" robots)
          (cond (= minute 24) (:geode collected)
                (and (>= minute 21) (= (:geode robots) 0)) (:geode collected)
                ;(and (>= minute 15) (= (:obsidian robots) 0)) (:geode collected)
                ;(and (>= minute 18) (= (:obsidian robots) 1)) (:geode collected)
                ;(and (>= minute 10) (<= (:cly robots) 1)) (:geode collected)
                (>= (:ore robots) 8) (:geode collected)
                (>= (:cly robots) 8) (:geode collected)
                (>= (:obsidian robots) 8) (:geode collected)

                (and (= (:geode robots) 0) (< 1 (- (get collected :ore) (get geode-blueprint :ore))) (< 1 (- (get collected :obsidian) (get geode-blueprint :obsidian))))
                (:geode collected)

                (and (= (:geode robots) 0) (= (:obsidian robots) 0) (< 1 (- (get collected :ore) (get obsidian-blueprint :ore))) (< 1 (- (get collected :cly) (get obsidian-blueprint :cly))))
                (:geode collected)

                (and (= (:obsidian robots) 0) (= (:cly robots) 0) (< 1 (- (get collected :ore) (get obsidian-blueprint :ore))))
                (:geode collected)

                :else
                (let [chooses [(turn (+ minute 1) (collected-inc collected robots) robots blueprint)
                               (let [[collected new-robots] (build-robot blueprint collected robots :geode)]
                                 (if (= new-robots robots)
                                   -1
                                   (turn (+ minute 1) (collected-inc collected robots) new-robots blueprint)
                                   ))
                               (let [[collected new-robots] (build-robot blueprint collected robots :ore)]
                                 (if (= new-robots robots)
                                   -1
                                   (turn (+ minute 1) (collected-inc collected robots) new-robots blueprint)))
                               (let [[collected new-robots] (build-robot blueprint collected robots :cly)]
                                 (if (= new-robots robots)
                                   -1
                                   (turn (+ minute 1) (collected-inc collected robots) new-robots blueprint)))
                               (let [[collected new-robots] (build-robot blueprint collected robots :obsidian)]
                                 (if (= new-robots robots)
                                   -1
                                   (turn (+ minute 1) (collected-inc collected robots) new-robots blueprint)))]

                      ]
                  (apply max (flatten chooses))
                  )
                )
          )))))

(defn parse-blueprint
  [line]
  (let [[_ id ore-robot cly-robot obsidian-robot-ore obsidian-robot-cly geode-robot-ore geode-robot-obsidian] (re-matches #"Blueprint (\d+): Each ore robot costs (\d+) ore\. Each clay robot costs (\d+) ore\. Each obsidian robot costs (\d+) ore and (\d+) clay\. Each geode robot costs (\d+) ore and (\d+) obsidian\." line)]
    {:id       (read-string id)
     :ore      {:ore (read-string ore-robot) :cly 0 :obsidian 0 :geode 0}
     :cly      {:ore (read-string cly-robot) :cly 0 :obsidian 0 :geode 0}
     :obsidian {:ore (read-string obsidian-robot-ore) :cly (read-string obsidian-robot-cly)}
     :geode    {:ore (read-string geode-robot-ore) :cly 0 :obsidian (read-string geode-robot-obsidian) :geode 0}
     }
    )
  )

(defn part1
  [blueprints]
  (let [result (map (fn [blueprint]
                      [(:id blueprint)
                       (turn
                         0
                         {:ore 0 :cly 0 :obsidian 0 :geode 0}
                         {:ore 1 :cly 0 :obsidian 0 :geode 0}
                         blueprint
                         )]) blueprints)
        ]
    (reduce (fn [acc [id geode]] (+ acc (* id geode))) 0 result)
    )
  )

