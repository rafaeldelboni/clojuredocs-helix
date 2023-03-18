(ns main.routes
  (:require [main.pages.home.views :as home.views]
            [main.pages.library.views :as library.views]
            [main.routes.subs]
            [main.routes.events]
            [refx.alpha :as refx]
            [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(def routes
  ["/"
   [""
    {:view home.views/page
     :name :routes/home-page}]
   [":organization"
    {:view library.views/organization-page
     :name :routes/organization
     :parameters {:path {:organization string?}}}]
   [":organization/:library"
    {:view library.views/library-page
     :name :routes/organization.library
     :parameters {:path {:organization string?
                         :library string?}}}]
   [":organization/:library/:definition"
    {:view library.views/definition-page
     :name :routes/organization.library.definition
     :parameters {:path {:organization string?
                         :library string?
                         :definition string?}}}]])

(defn router []
  (rf/router routes {:data {:coercion rcm/coercion}}))

(defn on-navigate [new-match]
  (when new-match
    (refx/dispatch [:app.routes/navigated new-match])))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start! (router) on-navigate {:use-fragment true}))
