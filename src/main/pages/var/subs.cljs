(ns main.pages.var.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.var/docs
 (fn [db]
   (:var db)))

(refx/reg-sub
 :app.var/loading
 (fn [db]
   (:var-loading? db)))

(refx/reg-sub
 :app.var/error
 (fn [db]
   (:var-error db)))
