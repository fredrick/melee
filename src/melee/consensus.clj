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
    "Returns a vote response containing the follower's :term for the candidate,
    to update itself :vote-granted, true meaning candidate received vote,
    and updated :state of the follower.")
  (append
    [this entry]
    "Returns an append entries response containing the :term for the
    leader to update itself, :success, true if the follower contained entry
    matching :prev-log-index and prev-log-term, and updated :state with
    entry appended to log."))

(defrecord Ballot [^Number term candidate-id ^Number last-log-index ^Number last-log-term])

(defrecord State [id ^Number current-term voted-for ^IPersistentVector log ^Number commit-index ^Number last-applied]
  Consensus
  (vote [this ballot]
    (let [log-is-current? (or (> (:last-log-term ballot) (last-term log))
                              (and (= (:last-log-term ballot) (last-term log))
                                   (>= (:last-log-index ballot) (last-index log))))
          term (max (:term ballot) current-term)
          grant? (and (= (:term ballot) current-term)
                      log-is-current?
                      (or (nil? voted-for) (= (:candidate-id ballot) voted-for)))]
      {:term term
       :vote-granted grant?
       :state (if (and grant? (nil? voted-for))
                (->State id term (:candidate-id ballot) log commit-index last-applied)
                (->State id term voted-for log commit-index last-applied))}))
  (append [this entry]
    (let [term (max (:term entry) current-term)
          accept? (and (= (:term entry) current-term)
                      (or (zero? (:prev-log-index entry))
                          (and (> (:prev-log-index entry) 0)
                               (<= (:prev-log-index entry) (count log))
                               (= (:prev-log-term entry) (:term (nth log (:prev-log-index entry)))))))]
      {:term term
        :success accept?
        :state (if accept?
                 (->State id term voted-for
                          (concat (remove #(>= (:prev-log-index %) (:prev-log-index entry)) log) [entry])
                          (min (inc commit-index) (:prev-log-index entry)) last-applied)
                 (->State id term voted-for log commit-index last-applied))})))

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
