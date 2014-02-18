(ns melee.journal)

;;; ## Journal
;;;
;;; Durable journal storage
;;;

(defprotocol Journal
  (journal
    [this directory]
    "Returns durable journal.")
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
