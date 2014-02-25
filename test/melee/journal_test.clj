(ns melee.journal-test
  (:import (journal.io.api Journal$WriteType Journal$ReadType Location OpenJournalException)
           (java.io IOException))
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
    (fact "Forward replay from journal"
      (seq? (redo journal)) => true)
    (fact "Forward replay from journal after location"
      (let [start (write journal
                         (.getBytes "record" "utf8")
                         (:sync write-type))
            next (write journal
                        (.getBytes "record" "utf8")
                        (:sync write-type))]
        (seq? (redo journal start)) => true))
    (fact "Backward replay from journal"
      (seq? (undo journal)) => true)
    (fact "Backward replay from journal before location"
      (let [start (write journal
                         (.getBytes "record" "utf8")
                         (:sync write-type))
            next (write journal
                        (.getBytes "record" "utf8")
                        (:sync write-type))]
        (seq? (undo journal next)) => true))
    (fact "Fetch record from location"
      (let [location (write journal
                           (.getBytes "record" "utf8")
                           (:sync write-type))]
        (String. (fetch journal location (:sync read-type))) => "record"))
    (fact "Delete location from journal"
      (let [location (write journal
                            (.getBytes "record" "utf8")
                            (:sync write-type))]
        (delete journal location)
        (fetch journal location (:sync read-type)) => (throws IOException)))
    (fact "Synchronize journal"
      (save journal))
    (fact "Compact journal"
      (compact journal))
    (fact "Truncate journal"
      (truncate journal) => (throws OpenJournalException)
      (close journal)
      (truncate journal))
    (fact "Close journal"
      (close journal))))
