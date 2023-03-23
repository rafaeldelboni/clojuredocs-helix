(ns main.pages.library.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.library/docs
 (fn [db]
   (:library db)))

(refx/reg-sub
 :app.library/loading
 (fn [db]
   (:library-loading? db)))

(refx/reg-sub
 :app.library/error
 (fn [db]
   (:library-error db)))
