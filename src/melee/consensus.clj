(ns melee.consensus
  (:use [melee.log])
  (:import (clojure.lang IPersistentVector)))

(defprotocol Consensus
  (vote [_ ballot])
  (append [_ entry]))

;;; Raft consensus algorithm
;;; http://raftconsensus.github.io/

(defrecord Ballot [^Number term candidate-id ^Number last-log-index ^Number last-log-term])

(defrecord State [id ^Number current-term voted-for ^IPersistentVector log ^Number commit-index ^Number last-applied]
  Consensus
  (vote [_ ballot]
    (let [log-is-current? (or (> (:last-log-term ballot) (last-term log))
                            (and (= (:last-log-term ballot) (last-term log))
                                 (>= (:last-log-index ballot) (last-index log))))]
      {:term (max (:term ballot) current-term)
       :vote-granted (and
                       (= (:term ballot) current-term)
                       log-is-current?
                       (or (nil? voted-for) (= (:candidate-id ballot) voted-for)))}))
  (append [_ entry]))

(defrecord Leader [^State state next-index match-index])

(defn ballot [^Number term candidate-id ^Number last-log-index ^Number last-log-term]
  "Ballot for leader election."
  (Ballot. term candidate-id last-log-index last-log-term))

(defn state [id ^Number current-term voted-for ^IPersistentVector log ^Number commit-index ^Number last-applied]
  "State for node."
  (State. id current-term voted-for log commit-index last-applied))

(defn leader [^State state next-index match-index]
  "Leader from state."
  (Leader. state next-index match-index))
