(ns melee.consensus_test
  (:use [melee.core]
        [melee.consensus]
        [melee.log]
        [midje.sweet]))

(facts "Raft consensus"
  (let [id (uuid)
        node1 (state id 0 nil [] 0 0)
        node2 (state (uuid) 0 nil [] 0 0)
        node3 (state (uuid) 0 nil [] 0 0)]
    (fact "Start state"
      node1 => (contains {:id id
                         :current-term 0
                         :voted-for nil
                         :log []
                         :commit-index 0
                         :last-applied 0}))

    (fact "Start leader state"
      (leader node1 {} {}) => (contains {:state node1
                                       :next-index {}
                                       :match-index {}}))

    (facts "HandleRequestVoteRequest"
      (fact "Vote response has current term"
        (vote
          node2
          (ballot 0 (:id node3) 0 0)) => (contains {:term 0}))

      (fact "Vote not granted if voter term is greater than candidate term"
        (vote
          (state id 1 nil [] 0 0)
          (ballot 0 (:id node3) 0 0)) => {:term 1 :vote-granted false :state (state id 1 nil [] 0 0)})

      (fact "Vote not granted if voted-for not nil and not equal to candidate id"
        (vote
          (state id 0 (:id node2) [] 0 0)
          (ballot 0 (:id node3) 0 0)) => {:term 0 :vote-granted false :state (state id 0 (:id node2) [] 0 0)})

      (fact "Vote granted if voted-for is nil and candidate's log is equivalent to receiver's log"
        (vote
          (state id 1 nil [] 0 0)
          (ballot 1 (:id node3) 0 0)) => {:term 1 :vote-granted true :state (state id 1 (:id node3) [] 0 0)})

      (fact "Vote granted if voted-for is equivalent to candidate and candidate's log is equivalent to receiver's log"
        (vote
          (state id 1 (:id node3) [] 0 0)
          (ballot 1 (:id node3) 0 0)) => {:term 1 :vote-granted true :state (state id 1 (:id node3) [] 0 0)}))

    (facts "HandleAppendEntriesRequest"
      (fact "Append response has current term"
        (append
          node2
          (entry 0 (:id node3) 0 0 [] 0)) => (contains {:term 0}))

      (fact "Append success is false if receiver term is greater than leader's term"
        (append
          (state id 1 nil [] 0 0)
          (entry 0 (:id node2) 0 0 [] 0)) => {:term 1 :success false :state (state id 1 nil [] 0 0)})

      (fact "Append success is true if leader's previous log index is zero"
        (append
          (state id 0 (:id node3) [] 0 0)
          (entry 0 (:id node3) 0 0 ["Log1"] 0)) => {:term 0
                                                    :success true
                                                    :state (state id 0 (:id node3) [(entry 0 (:id node3) 0 0 ["Log1"] 0)] 0 0)})

      (fact "Append success is false if leader's previous log index is greater than zero
        and greater than length of receiver's log"
        (append
          (state id 0 (:id node3) [] 0 0)
          (entry 0 (:id node3) 1 0 ["Log1"] 0)) => {:term 0
                                                    :success false
                                                    :state (state id 0 (:id node3) [] 0 0)})

      (fact "Append success is false if leader's previous log index is greater than zero,
        less than length of receiver's log, and receiver's term of log entry at leader's
        previous log index is not equal to leader's previous log term"
        (append
          (state id 0 (:id node3) (vector (entry 0 (:id node3) 0 0 ["Log0"] 0)
                                          (entry 0 (:id node3) 1 0 ["Log1"] 0)) 1 0)
          (entry 1 (:id node3) 1 0 ["Log2"] 0)) => {:term 1
                                                    :success false
                                                    :state (state id 1 (:id node3) [(entry 0 (:id node3) 0 0 ["Log0"] 0)
                                                                                    (entry 0 (:id node3) 1 0 ["Log1"] 0)] 1 0)})

      (fact "Append success is true if leader's previous log index is greater than zero,
        less than length of receiver's log, and receiver's term of log entry at leader's
        previous log index is equal to leader's previous log term"
          (append
            (:state (append
                      (:state (append
                                (:state (append
                                          (state id 0 (:id node3) [(entry 0 (:id node3) 0 0 [] 0)] 0 0)
                                          (entry 0 (:id node3) 0 0 ["Log1"] 0)))
                                (entry 0 (:id node3) 1 0 ["Log2"] 1)))
                      (entry 0 (:id node3) 2 0 ["Log3"] 2)))
            (entry 0 (:id node3) 3 0 [] 3)) => {:term 0
                                                :success true
                                                :state (state id 0 (:id node3) [(entry 0 (:id node3) 0 0 [] 0)
                                                                                (entry 0 (:id node3) 0 0 ["Log1"] 0)
                                                                                (entry 0 (:id node3) 1 0 ["Log2"] 1)
                                                                                (entry 0 (:id node3) 2 0 ["Log3"] 2)
                                                                                (entry 0 (:id node3) 3 0 [] 3)] 4 0)})

      (fact "If an existing entry conflicts with a new one (same index but different terms),
        delete the existing entry and all that follow it."
          (append
            (state id 0 (:id node3) (vector (entry 0 (:id node3) 0 0 ["Log0"] 0)
                                            (entry 0 (:id node3) 0 0 ["Log1"] 0)
                                            (entry 1 (:id node3) 1 0 ["Log2"] 0)
                                            (entry 1 (:id node3) 2 0 ["Log3"] 0)) 0 0)
            (entry 0 (:id node3) 1 0 ["Log1b"] 0)) => {:term 0
                                                       :success true
                                                       :state (state id 0 (:id node3) [(entry 0 (:id node3) 0 0 ["Log0"] 0)
                                                                                       (entry 0 (:id node3) 0 0 ["Log1"] 0)
                                                                                       (entry 0 (:id node3) 1 0 ["Log1b"] 0)] 1 0)}))))
