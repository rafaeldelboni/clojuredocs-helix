(ns main.pages.library.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.ns/get-done
 (fn
   [{db :db} [_ response]]
   {:db (-> db
            (assoc :ns-loading? false)
            (assoc :ns-error nil)
            (assoc :ns (->> (js->clj response :keywordize-keys true)
                            :body
                            (sort-by :name))))}))

(refx/reg-event-db
 :app.ns/get-error
 (fn
   [db [key-error val-error]]
   (println key-error val-error)
   (-> db
       (assoc :ns-loading? false)
       (assoc :ns-error [key-error (:body val-error)])
       (assoc :ns nil))))

(refx/reg-event-fx
 :app.ns/get
 (fn
   [{db :db} [_ ns-name]]
   {:http {:method      :get
           :url         (str "/static/" ns-name)
           :accept      :json
           :on-success  [:app.ns/get-done]
           :on-failure  [:app.ns/get-error]}
    :db  (assoc db
                :ns nil
                :ns-error nil
                :ns-loading? true)}))

