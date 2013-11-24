(ns melee.partitioning
  (:use melee.hashing))

(defprotocol Partitioner
  (add [this node])
  (delete [this node])
  (lookup [this node]))

;;; Consistent hashing

(defn- push [node replicas ring]
  (apply merge ring
    (map #(hash-map (hash->int (hash-object (murmur3-32 %) node)) node)
      (range replicas))))

(defrecord ConsistentHash [replicas ring]
  Partitioner
  (add [this node]
    (ConsistentHash. replicas (push node replicas ring)))
  (delete [this node])
  (lookup [this node]))

(defn consistent-hash [replicas]
  (ConsistentHash. replicas (sorted-map)))
