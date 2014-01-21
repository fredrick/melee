(ns melee.log
  (:import (clojure.lang IPersistentVector)))

(defprotocol Log
  (start-index
    [this]
    "Returns first log index.")
  (last-index
    [this]
    "Returns last log index.")
  (last-term
    [this]
    "Returns last log term."))

(extend-protocol Log
  IPersistentVector
  (start-index [this]
    (if (empty? this) 0 (:prev-log-index (first this))))
  (last-index [this]
    (if (empty? this) 0 (:prev-log-index (last this))))
  (last-term [this]
    (if (empty? this) 0 (:term (last this)))))

(defrecord Entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit])

(defn entry
  "Entry for log."
  [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit]
  (->Entry term leader-id prev-log-index prev-log-term entries leader-commit))
