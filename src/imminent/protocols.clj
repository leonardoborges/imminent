(ns imminent.protocols
  (:refer-clojure :exclude [map filter await]))

(defprotocol IReturn
  (success?    [this])
  (failure?    [this])
  (map-failure [this f]))

(defprotocol IFuture
  (on-success   [this f])
  (on-failure   [this f])
  (on-complete  [this f])
  (filter       [this f?]))

(defprotocol IAwaitable
  (await
    [this]
    [this ms]))

(defprotocol IPromise
  (complete [this value])
  (->future [this]))
