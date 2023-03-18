(ns main.routes.events
  (:require [refx.alpha :as refx]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))

(refx/reg-fx
 :push-state
 (fn [route]
   (apply rfe/push-state route)))

(refx/reg-event-fx
 :app.routes/push-state
 (fn [_ [_ & route]]
   {:push-state route}))

(refx/reg-event-db
 :app.routes/navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(refx/reg-sub
 :app.routes/current-route
 (fn [db]
   (:current-route db)))
