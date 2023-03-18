(ns main.routes.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.routes/current-route
 (fn [db]
   (:current-route db)))
