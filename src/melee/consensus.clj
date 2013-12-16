(ns melee.consensus
  (:use [melee.log])
  (:import (clojure.lang IPersistentVector)))

;;; ## Raft Consensus Algorithm
;;;
;;; [Raft](http://raftconsensus.github.io/) is a consensus algorithm that is
;;; designed to be easy to understand. It's equivalent to Paxos in
;;; fault-tolerance and performance. The difference is that it's decomposed
;;; into relatively independent subproblems, and it cleanly addresses all
;;; major pieces needed for practical systems.
;;;

(defprotocol Consensus
  (vote
    [this ballot]
    "Returns a vote response containing the follower's :term for the
    candidate to update itself and :vote-granted, true meaning candidate received vote.")
  (append
    [this entry]
    "Returns an append entries response containing the :term for the
    leader to update itself and :success, true if the follower contained entry
    matching :prev-log-index and prev-log-term."))

(defrecord Ballot [^Number term candidate-id ^Number last-log-index ^Number last-log-term])

(defrecord State [id ^Number current-term voted-for ^IPersistentVector log ^Number commit-index ^Number last-applied]
  Consensus
  (vote [this ballot]
    (let [log-is-current? (or (> (:last-log-term ballot) (last-term log))
                              (and (= (:last-log-term ballot) (last-term log))
                                   (>= (:last-log-index ballot) (last-index log))))]
      {:term (max (:term ballot) current-term)
       :vote-granted (and (= (:term ballot) current-term)
                          log-is-current?
                          (or (nil? voted-for) (= (:candidate-id ballot) voted-for)))}))
  (append [this entry]))

(defrecord Leader [^State state next-index match-index])

(defn ballot
  "Ballot for leader election."
  [^Number term candidate-id ^Number last-log-index ^Number last-log-term]
  (->Ballot term candidate-id last-log-index last-log-term))

(defn state
  "State for node."
  [id ^Number current-term voted-for ^IPersistentVector log ^Number commit-index ^Number last-applied]
  (->State id current-term voted-for log commit-index last-applied))

(defn leader
  "Leader from state."
  [^State state next-index match-index]
  (->Leader state next-index match-index))
