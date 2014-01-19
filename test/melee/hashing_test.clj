(ns melee.hashing-test
  (:use [melee.hashing]
        [midje.sweet]))

(defn- string->bytes [^String string] (.getBytes string "UTF-8"))

(let [pangram "The quick brown fox jumps over the lazy dog"]
  (facts "32-bit MurmurHash 3"
    (let [hashed "23f74f2e"]
      (murmur3-32) => truthy
      (murmur3-32 1) => truthy
      (hash->string (hash-string (murmur3-32) pangram))
      => hashed
      (hash->string (hash-bytes (murmur3-32) (string->bytes pangram)))
      => hashed))

  (facts "128-bit MurmurHash 3"
    (let [hashed "6c1b07bc7bbc4be347939ac4a93c437a"]
      (murmur3-128) => truthy
      (murmur3-128 1) => truthy
      (hash->string (hash-string (murmur3-128) pangram))
      => hashed
      (hash->string (hash-bytes (murmur3-128) (string->bytes pangram)))
      => hashed))

  (facts "SipHash 2-4"
    (let [hashed "e46f1fdc05612752"]
      (sip24) => truthy
      (sip24 1 2) => truthy
      (hash->string (hash-string (sip24) pangram))
      => hashed
      (hash->string (hash-bytes (sip24) (string->bytes pangram)))
      => hashed))

  (facts "MD5"
    (let [hashed "9e107d9d372bb6826bd81d3542a419d6"]
      (md5) => truthy
      (hash->string (hash-string (md5) pangram))
      => hashed
      (hash->string (hash-bytes (md5) (string->bytes pangram)))
      => hashed))

  (facts "SHA-1"
    (let [hashed "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"]
      (sha1) => truthy
      (hash->string (hash-string (sha1) pangram))
      => hashed
      (hash->string (hash-bytes (sha1) (string->bytes pangram)))
      => hashed))

  (facts "SHA-256"
    (let [hashed "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"]
      (sha256) => truthy
      (hash->string (hash-string (sha256) pangram))
      => hashed
      (hash->string (hash-bytes (sha256) (string->bytes pangram)))
      => hashed))

  (facts "SHA-512"
    (let [hashed "07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb642e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854fee6"]
      (sha512) => truthy
      (hash->string (hash-string (sha512) pangram))
      => hashed
      (hash->string (hash-bytes (sha512) (string->bytes pangram)))
      => hashed))

  (facts "CRC-32"
    (let [hashed "39a34f41"]
      (crc32) => truthy
      (hash->string (hash-string (crc32) pangram))
      => hashed
      (hash->string (hash-bytes (crc32) (string->bytes pangram)))
      => hashed))

  (facts "Adler-32"
    (let [hashed "da0fdc5b"]
      (adler32) => truthy
      (hash->string (hash-string (adler32) pangram))
      => hashed
      (hash->string (hash-bytes (adler32) (string->bytes pangram)))
      => hashed))

  (let [hashcode (hash-string (murmur3-128) pangram)]
    (fact "Convert between HashCode and byte array"
      (bytes->hash (hash->bytes hashcode)) => hashcode)

    (fact "Convert between HashCode and int"
      (hash (int->hash (hash->int hashcode))) => (hash hashcode))

    (fact "Convert between HashCode and long"
      (hash (long->hash (hash->long hashcode))) => (hash hashcode))

    (fact "Convert between HashCode and string"
      (string->hash (hash->string hashcode)) => hashcode))

  (fact "Hash an integer"
    (hash->string (hash-int (murmur3-32) 1))
    => "2a40f1fb")

  (fact "Hash a long"
    (hash->string (hash-long (murmur3-32) 1))
    => "445d0753")

  (fact "Hash an object"
    (hash->string (hash-object (murmur3-32) {:a 1}))
    => "bb326b0d"))
