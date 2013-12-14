(ns melee.partitioning
  (:use melee.hashing))

(defprotocol Partitioner
  (add [_ node])
  (delete [_ node])
  (lookup [_ object]))

;;; Consistent hashing

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
  (add [_ node]
    "Adds node to consistent hash ring."
    (->ConsistentHash replicas (ring-push ring node replicas)))
  (delete [_ node]
    "Deletes node from consistent hash ring."
    (->ConsistentHash replicas (ring-pop ring node replicas)))
  (lookup [_ object]
    "Finds node to use for object from consistent hash ring."
    (if-not (empty? ring)
      (let [hash (hash->long (hash-object (murmur3-128) object))
            tail-map (tail-map ring hash)]
        (if (empty? tail-map)
          (val (first ring))
          (val (first tail-map)))))))

(defn consistent-hash
  ([replicas]
    "Creates a new consistent hash ring."
    (->ConsistentHash replicas (sorted-map)))
  ([nodes replicas]
    "Creates a new consistent hash ring with one or many seed nodes."
    (reduce add (->ConsistentHash replicas (sorted-map)) nodes)))
