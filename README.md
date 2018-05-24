# Melee


Melee provides distributed system primitives. It is not a database or a messaging queue, but these could be built using Melee.
This code is a fork for Fredrick Galoso's work which can be found [on Github](https://github.com/fredrick/melee).
I logging from the original code and updated the runtime dependencies.

Primary features and goals:

- [Raft consensus](http://raftconsensus.github.io/) for a [CP](http://henryr.github.io/cap-faq/) replicated state machine and log
- Consistent hashing for partitioning

For example, Melee could be used to provide replicated [WAL](http://en.wikipedia.org/wiki/Write-ahead_logging) (write-ahead logging) for a distributed system.

## License

Copyright © 2014 Fredrick Galoso

Distributed under the Eclipse Public License, the same as Clojure.
