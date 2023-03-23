(ns main.pages.organization.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.organization/get-done
 (fn
   [{db :db} [_ response]]
   {:db (-> db
            (assoc :organization-loading? false)
            (assoc :organization-error nil)
            (assoc :organization (->> (js->clj response :keywordize-keys true)
                                      :body
                                      (sort-by :name))))}))

(refx/reg-event-db
 :app.organization/get-error
 (fn
   [db [key-error val-error]]
   (println key-error val-error)
   (-> db
       (assoc :organization-loading? false)
       (assoc :organization-error [key-error (:body val-error)])
       (assoc :organization nil))))

(refx/reg-event-fx
 :app.organization/get
 (fn
   [{db :db} [_ org-name]]
   {:http {:method      :get
           :url         (str "./static/" org-name)
           :accept      :json
           :on-success  [:app.organization/get-done]
           :on-failure  [:app.organization/get-error]}
    :db  (assoc db
                :organization nil
                :organization-error nil
                :organization-loading? true)}))

