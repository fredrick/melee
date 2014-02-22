(ns melee.log
  (:import (clojure.lang IPersistentVector)))

;;; ## Log
;;;
;;; Replicated command log.
;;;

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

(defn- index-of [log element]
  (if (or (empty? log)
          (and (= (count log) 1)
               (zero? (:prev-log-index (first log)))))
    0
    (inc (:prev-log-index element))))

(extend-protocol Log
  IPersistentVector
  (start-index [this] (index-of this (first this)))
  (last-index [this] (index-of this (last this)))
  (last-term [this]
    (if (empty? this) 0 (:term (last this)))))

(defrecord Entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit])

(defn entry
  "Entry for log."
  [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit]
  (->Entry term leader-id prev-log-index prev-log-term entries leader-commit))
