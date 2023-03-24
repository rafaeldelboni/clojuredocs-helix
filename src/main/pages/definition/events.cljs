(ns main.pages.definition.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.definition/get-done
 (fn
   [{db :db} [_ response]]
   {:db (-> db
            (assoc :definition-loading? false)
            (assoc :definition-error nil)
            (assoc :definition (->> (js->clj response :keywordize-keys true)
                                    :body
                                    (sort-by :name))))}))

(refx/reg-event-db
 :app.definition/get-error
 (fn
   [db [key-error val-error]]
   (println key-error val-error)
   (-> db
       (assoc :definition-loading? false)
       (assoc :definition-error [key-error (:body val-error)])
       (assoc :definition nil))))

(refx/reg-event-fx
 :app.definition/get
 (fn
   [{db :db} [_ lib-name]]
   {:http {:method      :get
           :url         (str "./static/" lib-name)
           :accept      :json
           :on-success  [:app.definition/get-done]
           :on-failure  [:app.definition/get-error]}
    :db  (assoc db
                :definition nil
                :definition-error nil
                :definition-loading? true)}))

