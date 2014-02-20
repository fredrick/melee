(ns melee.journal-test
  (:use [clojure.java.io :only [file]]
        [melee.journal]
        [midje.sweet]))

(facts "Durable journal"
  (with-open [journal (journal (file "test"))]
    (fact "Create journal"
      journal => truthy)))
