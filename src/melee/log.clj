(ns melee.log
  (:import (clojure.lang IPersistentVector)))

(defrecord Entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit])

(defn entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit]
  "Entry for log."
  (Entry. term leader-id prev-log-index prev-log-term entries leader-commit))

(defn start-index [^IPersistentVector log]
  "Returns first log index."
  (if (empty? log)
    0
    (:prev-log-index (first log))))

(defn last-index [^IPersistentVector log]
  "Returns last log index."
  (if (empty? log)
    0
    (:prev-log-index (last log))))

(defn last-term [^IPersistentVector log]
  "Returns last log term."
  (if (empty? log)
    0
    (:term (last log))))
