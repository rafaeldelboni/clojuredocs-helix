(ns main.pages.organization.events
  (:require [refx.alpha :as refx]))

;; todo cache this
(refx/reg-event-fx
 :app.organization/get-done
 (fn
   [{db :db} [_ org-name response]]
   {:db (-> db
            (assoc :organization-loading? false)
            (assoc :organization-error nil)
            (assoc :organization (->> (js->clj response :keywordize-keys true)
                                      :body
                                      (filter #(if org-name
                                                 (= org-name (:organization %))
                                                 true))
                                      (sort-by :name))))}))

(refx/reg-event-db
 :app.organization/get-error
 (fn
   [db [event-name org-name response]]
   (println event-name org-name response)
   (-> db
       (assoc :organization-loading? false)
       (assoc :organization-error [event-name (:body response)])
       (assoc :organization nil))))

(refx/reg-event-fx
 :app.organization/get
 (fn
   [{db :db} [_ org-name]]
   {:http {:method      :get
           :url         "./static/root.json"
           :accept      :json
           :on-success  [:app.organization/get-done org-name]
           :on-failure  [:app.organization/get-error org-name]}
    :db  (assoc db
                :organization nil
                :organization-error nil
                :organization-loading? true)}))

