(ns main.pages.organization.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.organization/docs
 (fn [db]
   (:organization db)))

(refx/reg-sub
 :app.organization/loading
 (fn [db]
   (:organization-loading? db)))

(refx/reg-sub
 :app.organization/error
 (fn [db]
   (:organization-error db)))
