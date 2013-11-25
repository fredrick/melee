(ns melee.consensus-test
  (:use [melee.core])
  (:use [melee.consensus])
  (:use [midje.sweet]))

(facts "Raft consensus"
  (let [id (uuid)
        start (intialize-state id)]
    (fact "Start state"
      start => (contains {:id id
                         :current-term 0
                         :voted-for nil
                         :log ()
                         :commit-index 0
                         :last-applied 0}))

    (fact "Start leader state"
      (leader start {} {}) => (contains {:state start
                                       :next-index {}
                                       :match-index {}}))))
