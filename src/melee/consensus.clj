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

(defrecord Leader [state next-index match-index])
