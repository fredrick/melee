(ns melee.partitioning-test
  (:use [melee.hashing])
  (:use [melee.partitioning])
  (:use [midje.sweet]))

(facts "Consistent hashing partitioner"
  (let [ring (consistent-hash 3)
        node1 "node1"
        node2 "node2"
        node3 "node3"]
    (fact "Add object"
      (:ring (add ring node1)) => (has every? #(= node1 %)))

    (fact "Add many objects"
      (let [updated-ring (:ring (add (add (add ring node1) node2) node3))]
        updated-ring => (has some #(= node1 %))
        updated-ring => (has some #(= node2 %))
        updated-ring => (has some #(= node3 %))))

    (fact "Remove object"
      (:ring (delete (add ring node1) node1)) => {})

    (fact "Remove many objects"
      (let [full-ring (add (add (add ring node1) node2) node3)]
        (:ring (delete (delete full-ring node1) node2)) => (has every? #(= node3 %))))))
