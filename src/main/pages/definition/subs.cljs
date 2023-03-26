(ns main.pages.definition.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.definition/docs
 (fn [db]
   (:definition db)))

(refx/reg-sub
 :app.definition/loading
 (fn [db]
   (:definition-loading? db)))

(refx/reg-sub
 :app.definition/error
 (fn [db]
   (:definition-error db)))
