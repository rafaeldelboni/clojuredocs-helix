(ns main.pages.var.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.var/get-done
 (fn
   [{db :db} [_ var-name index response]]
   (println _ var-name index response)
   {:db (-> db
            (assoc :var-loading? false)
            (assoc :var-error nil)
            (assoc :var (->> (js->clj response :keywordize-keys true)
                             :body
                             (sort-by :name)
                             (filter #(and (= (:name %) var-name)
                                           (= (:index %) index)))
                             first)))}))

(refx/reg-event-db
 :app.var/get-error
 (fn
   [db [key-error var-name index val-error]]
   (println key-error var-name index val-error)
   (-> db
       (assoc :var-loading? false)
       (assoc :var-error [key-error (:body val-error)])
       (assoc :var nil))))

(refx/reg-event-fx
 :app.var/get
 (fn
   [{db :db} [_ lib-name var-name index]]
   {:http {:method      :get
           :url         (str "./static/" lib-name)
           :accept      :json
           :on-success  [:app.var/get-done var-name index]
           :on-failure  [:app.var/get-error var-name index]}
    :db  (assoc db
                :var nil
                :var-error nil
                :var-loading? true)}))

