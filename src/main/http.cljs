(ns main.http
  (:require [lambdaisland.fetch :as fetch]
            [refx.alpha :refer [dispatch reg-fx]]))

(defn- send-request!
  [{:keys [url on-success on-failure] :as request} fn-request]
  (-> (fn-request url request)
      (.then (fn [{:keys [status] :as resp}]
               (if (> status 400)
                 (dispatch (conj on-failure resp))
                 (dispatch (conj on-success resp)))))
      (.catch (fn [resp]
                (dispatch (conj on-failure resp))))))

(defn http-effect
  [fn-request]
  (fn [request]
    (if (sequential? request)
      (doseq [req request]
        (send-request! req fn-request))
      (send-request! request fn-request))))

(reg-fx :http (http-effect fetch/request))
