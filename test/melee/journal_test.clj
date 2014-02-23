(ns melee.journal-test
  (:import (journal.io.api Journal$WriteType Journal$ReadType Location))
  (:use [clojure.java.io :only [file]]
        [melee.journal]
        [midje.sweet]))

(facts "Durable journal"
  (fact "Async write type"
    (:async write-type) => Journal$WriteType/ASYNC)
  (fact "Sync write type"
    (:sync write-type) => Journal$WriteType/SYNC)
  (fact "Async read type"
    (:async read-type) => Journal$ReadType/ASYNC)
  (fact "Sync read type"
    (:sync read-type) => Journal$ReadType/SYNC)
  (with-open [journal (journal (file "test"))]
    (fact "Create journal"
      (instance? journal.io.api.Journal journal) => true)
    (fact "Write record to journal"
      (instance? Location (write journal
                                 (.getBytes "record" "utf8")
                                 (:sync write-type))))
    (fact "Replay from journal"
      (seq? (redo journal)) => true)))
