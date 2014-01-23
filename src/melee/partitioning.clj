(ns melee.partitioning
  (:use melee.hashing))

(defprotocol Partitioner
  (add [this node] "Adds node.")
  (delete [this node] "Deletes node.")
  (lookup [this object] "Finds node to use for object."))

;;; ## Consistent Hashing

(defn- allocate [node replicas]
  (map #(hash-map (hash->long (hash-object (murmur3-128 %) node)) node)
    (range replicas)))

(defn- ring-push [ring node replicas]
  (apply merge ring (allocate node replicas)))

(defn- ring-pop [ring node replicas]
  (apply dissoc ring (map first (map keys (allocate node replicas)))))

(defn- tail-map [ring hash]
  (filter #(<= 0 (compare (key %) hash)) ring))

(defrecord ConsistentHash [replicas ring]
  Partitioner
  (add [this node]
    (->ConsistentHash replicas (ring-push ring node replicas)))
  (delete [this node]
    (->ConsistentHash replicas (ring-pop ring node replicas)))
  (lookup [this object]
    (if-not (empty? ring)
      (let [hash (hash->long (hash-object (murmur3-128) object))
            tail-map (tail-map ring hash)]
        (if (empty? tail-map)
          (val (first ring))
          (val (first tail-map)))))))

(defn consistent-hash
  "Returns a new consistent hash ring, with zero or many seed nodes."
  ([replicas] (->ConsistentHash replicas (sorted-map)))
  ([nodes replicas] (reduce add (->ConsistentHash replicas (sorted-map)) nodes)))
