(ns main.app
  (:require ["react-dom/client" :as rdom]
            [helix.core :refer [$]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.http]
            [main.lib :refer [defnc]]
            [refx.alpha :as refx]))

(def default-db
  {:current-route nil
   :current-user nil})

;; subs
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

;; effect
(refx/reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db default-db}))

(refx/reg-event-fx
 :app.ns/get-done
 (fn
   [{db :db} [_ response]]
   {:db (-> db
            (assoc :ns-loading? false)
            (assoc :ns-error nil)
            (assoc :ns (->> (js->clj response :keywordize-keys true)
                            :body
                            (sort-by :name))))}))

(refx/reg-event-db
 :app.ns/get-error
 (fn
   [db [key-error val-error]]
   (println key-error val-error)
   (-> db
       (assoc :ns-loading? false)
       (assoc :ns-error [key-error (:body val-error)])
       (assoc :ns nil))))

(refx/reg-event-fx
 :app.ns/get
 (fn
   [{db :db} [_ ns-name]]
   {:http {:method      :get
           :url         (str "/static/" ns-name)
           :accept      :json
           :on-success  [:app.ns/get-done]
           :on-failure  [:app.ns/get-error]}
    :db  (assoc db
                :ns nil
                :ns-error nil
                :ns-loading? true)}))

;; app
(defnc app []
  (let [lib "org.clojure/clojure.json"
        base-git "https://github.com/clojure/clojure/tree/clojure-1.11.1/src/clj/"
        loading? (refx/use-sub [:app.ns/loading])
        [error error-res] (refx/use-sub [:app.ns/error])
        ns-docs (refx/use-sub [:app.ns/docs])]

    (hooks/use-effect
      :once
      (refx/dispatch [:app.ns/get lib]))

    (d/div
     (d/h1 "clojuredocs-helix")

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (for [ns-doc ns-docs]
       (d/section
        {:key (:name ns-doc)}
        (d/h2 (d/a {:href (str "#/" (:var-definitions ns-doc))} (:name ns-doc)))
        (d/pre (:doc ns-doc))
        (d/p (d/a {:href (str base-git (:filename ns-doc))} (:filename ns-doc)))
        (d/h6 (when (:author ns-doc) (str "by: " (:author ns-doc)))))))))

;; start your app with your React renderer
(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn setup! []
  (refx/clear-subscription-cache!)
  (refx/dispatch-sync [::initialize-db]))

(defn ^:export init []
  (setup!)
  (render))
