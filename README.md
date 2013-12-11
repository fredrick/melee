# Melee

Melee provides distributed system building blocks. It is not a database or a messaging queue, but these could be built using Melee.

Primary features and goals:

- Consistent hashing for partitioning
- [Raft consensus](http://raftconsensus.github.io/) for a [CP](http://henryr.github.io/cap-faq/) state machine and replicated log
- CRDTs ([Commutative Replicated Data Types](http://pagesperso-systeme.lip6.fr/Marc.Shapiro/papers/RR-6956.pdf)) for eventually consistent AP data structures

For example, Melee could be used to provide replicated [WAL](http://en.wikipedia.org/wiki/Write-ahead_logging) (write-ahead logging) for a distributed system.

## License

Copyright © 2013 Fredrick Galoso

Distributed under the Eclipse Public License, the same as Clojure.
