(ns melee.journal
  (:import (journal.io.api JournalBuilder Journal$WriteType Journal$ReadType)))

;;; ## Journal
;;;
;;; Durable journal storage.
;;;

(def write-type {:async Journal$WriteType/ASYNC,
                 :sync Journal$WriteType/SYNC})

(def read-type {:async Journal$ReadType/ASYNC,
                :sync Journal$ReadType/SYNC})

(defprotocol Journal
  (open
    [this]
    "Opens journal.")
  (write
    [this record write-type]
    "Writes record to journal.")
  (redo
    [this]
    [this start]
    "Returns forward-replay seq of journal locations.")
  (undo
    [this]
    [this end]
    "Returns backward-replay seq of journal locations.")
  (fetch
    [this location read-type]
    "Returns record from journal.")
  (delete
    [this location]
    "Deletes journal location.")
  (save
    [this]
    "Synchronizes journal.")
  (compact
    [this]
    "Compacts journal.")
  (truncate
    [this]
    "Truncates journal.")
  (close
    [this]
    "Closes journal."))

(defn journal
  "Returns new journal for append-only rotating log storage."
  ^journal.io.api.Journal [directory] (.open (JournalBuilder/of directory)))

(extend-protocol Journal
  journal.io.api.Journal
  (open [this] (.open this))
  (write [this record write-type] (.write this record write-type))
  (redo
    ([this] (seq (.redo this)))
    ([this start] (seq (.redo this start))))
  (undo
    ([this] (seq (.undo this)))
    ([this end] (seq (.undo this end))))
  (fetch [this location read-type] (.read this location read-type))
  (delete [this location] (.delete this location))
  (save [this] (.sync this))
  (compact [this] (.compact this))
  (truncate [this] (.truncate this))
  (close [this] (.close this)))
