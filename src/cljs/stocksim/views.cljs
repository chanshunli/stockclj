(ns stocksim.views
  (:require [re-frame.core :as rf]
            [stocksim.subs :as subs]
            [stocksim.events :as events]))

(def <-sub (comp deref re-frame.core/subscribe vector))
(def ->evt (comp re-frame.core/dispatch vector))

(defn search []
  (let [search (<-sub ::subs/search-symbol)]
    [:input {:type "text"
             :value search
             :auto-focus true
             :on-change
             #(->evt ::events/search
                     (-> % .-target .-value))}]))

(defn msg []
  (let [searching? (<-sub ::subs/searching?)
        error      (<-sub ::subs/error)]
    [:h4#msg
     (cond searching?
           "Searching..."

           (= error :not-found)
           "Symbol not found"

           error
           "Internal error")]))

(defn main-panel []
  [:div
   [search]
   [msg]])
