(ns main.pages.definition.subs
  (:require [refx.alpha :as refx]))

; todo move this to logic and unit test
(defn group-clj-cljs [definitions]
  (->> definitions
       (group-by
        (juxt :name :row))
       (map (fn [[_ v]]
              (reduce
               (fn [acc cur]
                 (let [langs (into (or [(:lang acc)] []) [(:lang cur)])]
                   (-> (merge acc cur)
                       (assoc :lang langs))))
               v)))
       flatten))

; todo move this to logic and unit test
(defn inrelevant-definitions [{:keys [defined-by]}]
  (contains? #{"clojure.core/declare"} defined-by))

(refx/reg-sub
 :app.definition/docs
 (fn [db]
   (->> db
        :definition
        (remove inrelevant-definitions)
        group-clj-cljs
        (sort-by :name))))

(refx/reg-sub
 :app.definition/loading
 (fn [db]
   (:definition-loading? db)))

(refx/reg-sub
 :app.definition/error
 (fn [db]
   (:definition-error db)))
