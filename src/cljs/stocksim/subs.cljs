(ns stocksim.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::search-symbol
 (fn [db]
   (:search-symbol db)))

(re-frame/reg-sub
 ::searching?
 (fn [db]
   (:searching? db)))

(re-frame/reg-sub
 ::error
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 ::quote
 (fn [db]
   (:quote db)))
