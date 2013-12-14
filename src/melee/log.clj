(ns melee.log
  (:import (clojure.lang IPersistentVector)))

(defrecord Entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit])

(defn entry
  "Entry for log."
  [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit]
  (->Entry term leader-id prev-log-index prev-log-term entries leader-commit))

(defn start-index
  "Returns first log index."
  [^IPersistentVector log] (if (empty? log) 0 (:prev-log-index (first log))))

(defn last-index
  "Returns last log index."
  [^IPersistentVector log] (if (empty? log) 0 (:prev-log-index (last log))))

(defn last-term
  "Returns last log term."
  [^IPersistentVector log] (if (empty? log) 0 (:term (last log))))
