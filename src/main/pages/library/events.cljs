(ns main.pages.library.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.library/get-done
 (fn
   [{db :db} [_ response]]
   {:db (-> db
            (assoc :library-loading? false)
            (assoc :library-error nil)
            (assoc :library (->> (js->clj response :keywordize-keys true)
                                 :body
                                 (sort-by :name))))}))

(refx/reg-event-db
 :app.library/get-error
 (fn
   [db [key-error val-error]]
   (println key-error val-error)
   (-> db
       (assoc :library-loading? false)
       (assoc :library-error [key-error (:body val-error)])
       (assoc :library nil))))

(refx/reg-event-fx
 :app.library/get
 (fn
   [{db :db} [_ lib-name]]
   {:http {:method      :get
           :url         (str "./static/" lib-name)
           :accept      :json
           :on-success  [:app.library/get-done]
           :on-failure  [:app.library/get-error]}
    :db  (assoc db
                :library nil
                :library-error nil
                :library-loading? true)}))

