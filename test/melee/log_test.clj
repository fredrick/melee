(ns melee.log_test
  (:use [melee.core])
  (:use [melee.log])
  (:use [midje.sweet]))

(facts "Consensus log"
  (let [id (uuid)
        log []]
    (fact "Start index is 0 for empty log"
      (start-index log) => 0)

    (fact "Start index is first log index when adding entry to log
           (indices are strictly monotonically increasing across terms and snapshots)"
      (start-index (conj log
                         (entry 0 id (start-index log) 0 ["Log"] 0))) => 0
      (start-index (conj log
                         (entry 0 id 0 0 ["Log1"] 0)
                         (entry 0 id 1 0 ["Log2"] 0))) => 0
      (start-index (conj log
                         (entry 0 id 1 0 ["Log2"] 0)
                         (entry 0 id 2 0 ["Log3"] 0)
                         (entry 0 id 3 0 ["Log4" "Log5"] 0))) => 1)))
