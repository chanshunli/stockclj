(ns stocksim.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::search-symbol
 (fn [db]
   (:search-symbol db)))

(re-frame/reg-sub
 ::quote
 (fn [db]
   (:quote db)))
