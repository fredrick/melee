(ns melee.partitioning
  (:use melee.hashing))

(defprotocol Partitioner
  (add [node])
  (delete [node])
  (lookup [node]))

(defrecord ConsistentHash [hash-function replicas nodes]
  Partitioner
  (add [node])
  (delete [node])
  (lookup [node]))
