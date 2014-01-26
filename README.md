# Melee

[![Build Status](https://travis-ci.org/wayoutmind/melee.png?branch=master)](https://travis-ci.org/wayoutmind/melee)
[![Dependencies Status](http://www.jarkeeper.com/wayoutmind/melee/status.png)](http://jarkeeper.com/wayoutmind/melee)

Melee provides distributed system primitives. It is not a database or a messaging queue, but these could be built using Melee.

Primary features and goals:

- [Raft consensus](http://raftconsensus.github.io/) for a [CP](http://henryr.github.io/cap-faq/) replicated state machine and log
- Consistent hashing for partitioning
- CRDTs ([Commutative Replicated Data Types](http://pagesperso-systeme.lip6.fr/Marc.Shapiro/papers/RR-6956.pdf)) for eventually consistent AP data structures

For example, Melee could be used to provide replicated [WAL](http://en.wikipedia.org/wiki/Write-ahead_logging) (write-ahead logging) for a distributed system.

## License

Copyright © 2014 Fredrick Galoso

Distributed under the Eclipse Public License, the same as Clojure.
