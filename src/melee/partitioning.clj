(ns melee.partitioning
  (:use melee.hashing))

(defprotocol Partitioner
  (add [this node])
  (delete [this node])
  (lookup [this node]))

;;; Consistent hashing

(defn- allocate [node replicas]
  (map #(hash-map (hash->int (hash-object (murmur3-32 %) node)) node)
    (range replicas)))

(defn- ring-push [node replicas ring]
  (apply merge ring (consistent-partition node replicas)))

(defn- ring-pop [node replicas ring]
  (apply dissoc ring (map first (map keys (allocate node replicas)))))

(defrecord ConsistentHash [replicas ring]
  Partitioner
  (add [this node]
    (ConsistentHash. replicas (ring-push node replicas ring)))
  (delete [this node]
    (ConsistentHash. replicas (ring-pop node replicas ring)))
  (lookup [this node]))

(defn consistent-hash [replicas]
  (ConsistentHash. replicas (sorted-map)))
