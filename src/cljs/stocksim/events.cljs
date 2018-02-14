(ns stocksim.events
  (:require [re-frame.core :as rf]
            [stocksim.db :as db]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [cljs.spec.alpha :as s]
            [stocksim.spec :as v]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

#_(rf/reg-event-db
   ::search
   (fn [db [_ search]]
     (println (str "foo: " search))
     (assoc db :search-symbol search)))

(rf/reg-event-fx
 ::search
 (fn
   [{db :db} [_ search]]
   {:http-xhrio {:method          :get
                 :uri             (str "https://cors-anywhere.herokuapp.com/http://data.benzinga.com/rest/richquoteDelayed?symbols=" search)
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:process-response]
                 :on-failure      [:bad-response]}
    :db  (assoc db :search-symbol search)}))

(rf/reg-event-db
 :process-response
 (fn
   [db [_ {response (keyword (:search-symbol db))}]]
   (if (s/valid? ::v/quote response)
     (-> db
         (assoc :error? false)
         (assoc :quote response))
     (assoc db :error? true))))


(rf/reg-event-db
 :bad-response
 (fn [db [_ _]]
   (println "Error contacting server.")
   (assoc db :error? true )))
