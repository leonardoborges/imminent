## 0.2.1-SNAPSHOT (UNRELEASED)

- 10261b2 - Return 'this' from 'on-complete' for easy threading

## 0.2.0 (03/08/2019)

- f0f04cf - Upgrade to Clojure 1.10.1
- 36e9dff - Add Applicative support to Result
- 3bfc854 - Extend Monad protocol to Result
- be3c830 - Implement amb

## 0.1.1 (15/03/2015)

- c6a03c1 - Implement BiFunctor for the Result type Simplify Future's fmap
- 2273659 - Implement map directly so as to avoid creating unnecessary threads. That is the job of bind
- d4527eb - Add 'blocking-future' construct:   
          - Leverages ForkJoinPool$ManagedBlocker to prevent thread starvation when performing blocking IO   
          - Replace use of CountDownLatch with Phaser for ForkJoinPool compatibility   
          - Update 'concurrent-test' to test the new blocking construct
- 276a2b5 - Add 'alift' applicative function and example


## 0.1.0 (08/12/2014)

- Initial release.
