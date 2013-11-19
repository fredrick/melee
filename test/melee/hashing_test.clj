(ns melee.hashing-test
  (:use [melee.hashing])
  (:use [midje.sweet]))

(defn- string->bytes [^String string] (.getBytes string "UTF-8"))

(let [pangram "The quick brown fox jumps over the lazy dog"]
  (facts "32-bit MurmurHash 3"
    (murmur3-32) => truthy
    (murmur3-32 1) => truthy
    (hash->string (hash-string (murmur3-32) pangram))
    => "23f74f2e"
    (hash->string (hash-bytes (murmur3-32) (string->bytes pangram)))
    => "23f74f2e")

  (facts "128-bit MurmurHash 3"
    (murmur3-128) => truthy
    (murmur3-128 1) => truthy
    (hash->string (hash-string (murmur3-128) pangram))
    => "6c1b07bc7bbc4be347939ac4a93c437a")

  (facts "SipHash 2-4"
    (sip24) => truthy
    (sip24 1 2) => truthy
    (hash->string (hash-string (sip24) pangram))
    => "e46f1fdc05612752")

  (facts "MD5"
    (md5) => truthy
    (hash->string (hash-string (md5) pangram))
    => "9e107d9d372bb6826bd81d3542a419d6")

  (facts "SHA-1"
    (sha1) => truthy
    (hash->string (hash-string (sha1) pangram))
    => "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12")

  (facts "SHA-256"
    (sha256) => truthy
    (hash->string (hash-string (sha256) pangram))
    => "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592")

  (facts "SHA-512"
    (sha512) => truthy
    (hash->string (hash-string (sha512) pangram))
    => "07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb642e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854fee6")

  (facts "CRC-32"
    (crc32) => truthy
    (hash->string (hash-string (crc32) pangram))
    => "39a34f41")

  (facts "Adler-32"
    (adler32) => truthy
    (hash->string (hash-string (adler32) pangram))
    => "da0fdc5b")

  (let [hashcode (hash-string (murmur3-128) pangram)]
    (fact "Convert between HashCode and byte array"
      (bytes->hash (hash->bytes hashcode)) => hashcode)

    (fact "Convert between HashCode and int"
      (.hashCode (int->hash (hash->int hashcode))) => (.hashCode hashcode))

    (fact "Convert between HashCode and long"
      (.hashCode (long->hash (hash->long hashcode))) => (.hashCode hashcode))

    (fact "Convert between HashCode and string"
      (string->hash (hash->string hashcode)) => hashcode)))
