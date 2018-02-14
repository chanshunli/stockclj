(ns stocksim.spec
  #?(:clj (:require [clojure.spec.alpha :as s]))
  #?(:cljs (:require [cljs.spec.alpha :as s])))

(s/def ::symbol string?)
(s/def ::name string?)
(s/def ::askPrice float?)
(s/def ::bidPrice float?)

(s/def ::quote
  (s/keys :req-un [::symbol ::name ::askPrice ::bidPrice]))
