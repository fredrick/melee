(ns melee.consensus)

(defprotocol Consensus
  (vote [this ballot])
  (append [this entry]))

(defrecord Ballot [term candidate-id last-log-ndex last-log-term])

(defrecord Entry [term leader-id prev-log-index prev-log-term entries leader-commit])

(defrecord State [id current-term voted-for log commit-index last-applied]
  Consensus
  (vote [this ballot])
  (append [this entry]))

(defn state [id current-term voted-for log commit-index last-applied]
  (State. id current-term voted-for log commit-index last-applied))

(defn intialize-state [id]
  (state id 0 nil () 0 0))

(defrecord Leader [state next-index match-index])

(defn leader [state next-index match-index]
  (Leader. state next-index match-index))
