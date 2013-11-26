(ns melee.consensus-test
  (:use [melee.core])
  (:use [melee.consensus])
  (:use [midje.sweet]))

(facts "Raft consensus"
  (let [id (uuid)
        node1 (state id 0 nil () 0 0)
        node2 (state (uuid) 0 nil () 0 0)
        node3 (state (uuid) 0 nil () 0 0)]
    (fact "Start state"
      node1 => (contains {:id id
                         :current-term 0
                         :voted-for nil
                         :log ()
                         :commit-index 0
                         :last-applied 0}))

    (fact "Start leader state"
      (leader node1 {} {}) => (contains {:state node1
                                       :next-index {}
                                       :match-index {}}))

    (fact "Vote response has current term"
      (vote node2 (ballot 1 (:id node3) 0 0)) => (contains {:term 0}))))
