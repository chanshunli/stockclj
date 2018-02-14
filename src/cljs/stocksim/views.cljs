(ns stocksim.views
  (:require [re-frame.core :as rf]
            [stocksim.subs :as subs]
            [stocksim.events :as events]))

(defn search []
  (let [search (rf/subscribe [::subs/search-symbol])]
    [:input {:type "text"
             :value @search
             :auto-focus true
             :on-change #(rf/dispatch [::events/search (-> % .-target .-value)])}]))

(defn main-panel []
  (search))
