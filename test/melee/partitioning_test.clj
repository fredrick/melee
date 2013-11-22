(ns melee.partitioning-test
  (:use [melee.hashing])
  (:use [melee.partitioning])
  (:use [midje.sweet]))

(facts "Consistent hashing partitioner"
  (let [consistent-hash (ConsistentHash. murmur3-32 10 ())]
    (fact "Create consistent hash"
      consistent-hash => truthy)))
