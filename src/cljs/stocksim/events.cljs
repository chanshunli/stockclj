(ns stocksim.events
  (:require [re-frame.core :as rf]
            [stocksim.db :as db]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [cljs.spec.alpha :as s]
            [stocksim.spec :as v]
            [clojure.string :as str]))

(def ^:private cors-anywhere
  "https://cors-anywhere.herokuapp.com/")

(def ^:private quote-service
  "http://data.benzinga.com/rest/richquoteDelayed?symbols=")

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-fx
 ::search
 rf/trim-v
 (fn
   [{db :db} [search]]
   (let [search (str/upper-case search)]
     {:http-xhrio {:method          :get
                   :uri             (str cors-anywhere quote-service search)
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:process-response]
                   :on-failure      [:bad-response]}
      :db (assoc db
                 :search-symbol search
                 :error nil
                 :searching? true)})))

(rf/reg-event-db
 :process-response
 rf/trim-v
 (fn
   [db [{response (keyword (:search-symbol db))}]]
   (if (s/valid? ::v/quote response)
     (assoc db
            :error nil
            :quote response
            :searching? false)
     (assoc db
            :error :not-found
            :quote nil
            :searching? false))))


(rf/reg-event-db
 :bad-response
 rf/trim-v
 (fn [db [response]]
   (assoc db
          :error (:last-error-code response)
          :quote nil
          :searching? false)))
