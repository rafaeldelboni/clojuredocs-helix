(ns main.app
  (:require ["react-dom/client" :as rdom]
            [helix.core :refer [$]]
            [helix.dom :as d]
            [main.http]
            [main.lib :refer [defnc]]
            [main.routes :as routes]
            [refx.alpha :as refx]))

(def default-db
  {:current-route nil
   :current-user nil})

(refx/reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db default-db}))

;; app
(defnc app []
  (let [current-route (refx/use-sub [:app.routes/current-route])
        route-data (:data current-route)]
    (d/div
     (d/h1 "clojuredocs-helix")
     (when-let [view (:view route-data)]
       ($ view {:route current-route})))))

;; start your app with your React renderer
(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn setup! []
  (refx/clear-subscription-cache!)
  (refx/dispatch-sync [::initialize-db])
  (routes/init-routes!))

(defn ^:export init []
  (setup!)
  (render))
