(ns melee.log)

(defrecord Entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit])

(defn entry [^Number term leader-id ^Number prev-log-index ^Number prev-log-term entries ^Number leader-commit]
  "Entry for log."
  (Entry. term leader-id prev-log-index prev-log-term entries leader-commit))

(defn start-index [^IPersistentVector log]
  "Returns first log index."
  (let [length (count log)]
    (if (zero? length)
      length
      (:prev-log-index (first log)))))
