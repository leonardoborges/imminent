(ns imminent.result
  (:require [imminent.protocols :refer [IReturn]]
            [imminent.util.functor :refer [BiFunctor]]
            [imminent.util.monad   :as m]
            [uncomplicate.fluokitten.protocols :as fkp]
            [uncomplicate.fluokitten.core :as fkc]
            clojure.core.match)
  (:import clojure.lang.IDeref))

(declare success)
(declare failure)

(deftype Success [v]
  IDeref
  (deref [_] v)
  IReturn
  (success?    [this] true)
  (failure?    [this] false)
  (map-failure [this _]
    this)

  fkp/Functor
  (fmap [fv g]
    (Success. (g v)))
  (fmap [fv g fvs]
    (Success. (apply g v (map deref fvs))))

  BiFunctor
  (bimap [fv f g]
    (try
      (success (f v))
      (catch Exception e
        (failure e))))

  fkp/Monad
  (bind[mv g]
    (try
      (g @mv)
      (catch Exception e
        (failure e))))

  (bind [mv g mvs]
    (fkc/mdo [v  mv
              vs (sequence mvs)]
             (fkc/return (apply g v vs))))

  (join [mm]
    (m/mjoin mm))

  Object
  (equals   [this other] (and (instance? Success other)
                              (= v @other)))
  (hashCode [this] (hash v))
  (toString [this] (pr-str v)))

(deftype Failure [e]
  IDeref
  (deref [_] e)
  IReturn
  (success?    [this] false)
  (failure?    [this] true)
  (map-failure [this f]
    (Failure. (f e)))
  fkp/Functor
  (fmap [fv _]
    fv)
  (fmap [fv g _]
    fv)

  BiFunctor
  (bimap [fv f g]
    (try
      (failure (g e))
      (catch Exception ex
        (failure ex))))

  fkp/Monad
  (bind [mv g] mv)

  (bind [mv g mvs] mv)

  (join [mm]
    (m/mjoin mm))

  Object
  (equals   [this other] (and (instance? Failure other)
                              (= e @other)))
  (hashCode [this] (hash e))
  (toString [this] (pr-str e)))

(defn success [v]
  (Success. v))

(defn failure [v]
  (Failure. v))


;;
;; Limited core.match support
;;

(extend-type Success
  clojure.core.match.protocols/IMatchLookup
  (val-at [this k not-found]
    (if (= imminent.result.Success k)
      @this
      not-found)))


(extend-type Failure
  clojure.core.match.protocols/IMatchLookup
  (val-at [this k not-found]
    (if (= imminent.result.Failure k)
      @this
      not-found)))
