(ns melee.journal
  (:import (journal.io.api JournalBuilder)))

;;; ## Journal
;;;
;;; Durable journal storage
;;;

(defprotocol Journal
  (write
    [this record write-type]
    "Writes record to journal.")
  (redo
    [this]
    "Returns forward-replay seq of journal locations.")
  (undo
    [this]
    "Returns backward-replay seq of journal locations.")
  (read
    [this location read-type]
    "Returns record from journal.")
  (delete
    [this location]
    "Deletes journal location.")
  (sync
    [this]
    "Synchronize journal.")
  (compact
    [this]
    "Compact journal."))

(defn journal [directory] (.open (JournalBuilder/of directory)))
