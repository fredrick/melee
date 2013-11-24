(ns melee.partitioning-test
  (:use [melee.hashing])
  (:use [melee.partitioning])
  (:use [midje.sweet]))

(facts "Consistent hashing partitioner"
  (let [ring (consistent-hash 10)]
    (fact "Add an object"
      (:ring (add ring 1)) => (has every? #(= 1 %)))))
