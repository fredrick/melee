(ns melee.hashing
  (:import (com.google.common.hash HashCode HashFunction Hashing)))

;;; ## Hash Functions

(defn murmur3-32
  ([]     (Hashing/murmur3_32))
  ([seed] (Hashing/murmur3_32 seed)))

(defn murmur3-128
  ([]     (Hashing/murmur3_128))
  ([seed] (Hashing/murmur3_128 seed)))

(defn sip24
  ([]     (Hashing/sipHash24))
  ([k0 k1] (Hashing/sipHash24 k0 k1)))

(defn md5
  ([]     (Hashing/md5)))

(defn sha1
  ([]     (Hashing/sha1)))

(defn sha256
  ([]     (Hashing/sha256)))

(defn sha512
  ([]     (Hashing/sha512)))

(defn crc32
  ([]     (Hashing/crc32)))

(defn adler32
  ([]     (Hashing/adler32)))

;;; ## Data Converters

(defn hash->bytes
  "Converts a HashCode to a byte array."
  [^HashCode hashcode] (.asBytes hashcode))

(defn hash->int
  "Converts a HashCode to an integer."
  [^HashCode hashcode] (.asInt hashcode))

(defn hash->long
  "Converts a HashCode to a long."
  [^HashCode hashcode] (.asLong hashcode))

(defn hash->string
  "Converts a HashCode to a string."
  [^HashCode hashcode] (.toString hashcode))

(defn bytes->hash
  "Converts a byte array to a HashCode."
  [bytes] (HashCode/fromBytes bytes))

(defn int->hash
  "Converts an int to a HashCode."
  [integer] (HashCode/fromInt integer))

(defn long->hash
  "Converts a long to a HashCode."
  [long] (HashCode/fromLong long))

(defn string->hash
  "Converts a string to a HashCode."
  [string] (HashCode/fromString string))

;;; ## Hashed Value Functions

(defn hash-bytes
  "Hash a byte array."
  ([^HashFunction hash-function ^bytes bytes]
    (.hashBytes hash-function bytes)))

(defn hash-int
  "Hash an integer."
  ([^HashFunction hash-function ^Integer integer]
    (.hashInt hash-function integer)))

(defn hash-long
  "Hash a long."
  ([^HashFunction hash-function ^Long long]
    (.hashLong hash-function long)))

(defn hash-string
  "Hash a string."
  ([^HashFunction hash-function ^String string]
    (hash-bytes hash-function (.getBytes string "UTF-8")))
  ([^HashFunction hash-function char-sequence charset]
    (.hashString hash-function char-sequence charset)))

(defn hash-object
  "Hash an object."
  ([^HashFunction hash-function object]
    (hash-string hash-function (str object))))
