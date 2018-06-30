(ns stocksim.views
  (:require [re-frame.core :as rf]
            [stocksim.subs :as subs]
            [stocksim.events :as events]))

(defn search []
  (let [search @(rf/subscribe [::subs/search-symbol])]
    [:input {:type "text"
             :value search
             :auto-focus true
             :on-change
             #(rf/dispatch [::events/search (-> % .-target .-value)])}]))

(defn msg []
  (let [searching? @(rf/subscribe [::subs/searching?])
        error      @(rf/subscribe [::subs/error])]
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
