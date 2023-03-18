(ns main.pages.library.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.ns/docs
 (fn [db]
   (:ns db)))

(refx/reg-sub
 :app.ns/loading
 (fn [db]
   (:ns-loading? db)))

(refx/reg-sub
 :app.ns/error
 (fn [db]
   (:ns-error db)))
