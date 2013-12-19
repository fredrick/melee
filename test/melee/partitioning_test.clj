(ns melee.partitioning-test
  (:use [melee.hashing]
        [melee.partitioning]
        [midje.sweet]))

(facts "Consistent hashing partitioner"
  (let [ring (consistent-hash 10)
        node1 "node1"
        node2 "node2"
        node3 "node3"]
    (fact "Add node"
      (:ring (add ring node1)) => (has every? #(= node1 %)))

    (fact "Add many nodes"
      (let [updated-ring (:ring (add (add (add ring node1) node2) node3))]
        updated-ring => (has some #(= node1 %))
        updated-ring => (has some #(= node2 %))
        updated-ring => (has some #(= node3 %))))

    (fact "Remove node"
      (:ring (delete (add ring node1) node1)) => {})

    (fact "Remove many nodes"
      (let [full-ring (add (add (add ring node1) node2) node3)]
        (:ring (delete (delete full-ring node1) node2)) => (has every? #(= node3 %))))

    (fact "Lookup node for empty hash ring"
      (lookup ring "object") => nil?)

    (fact "Lookup node for hash ring with one node"
      (lookup (add ring node1) "object1") => node1)

    (fact "Lookup another node for hash ring with two nodes"
      (lookup (add (add ring node1) node2) "object5") => node2)

    (fact "Lookup another node for hash ring with three nodes"
      (lookup (add (add (add ring node1) node2) node3) "object7") => node3)

    (fact "Initialize consistent hash with vector of nodes"
      (consistent-hash [node1 node2 node3] 10) => (add (add (add ring node1) node2) node3))))
