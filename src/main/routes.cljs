(ns main.routes
  (:require [main.pages.definition.views :as definition.views]
            [main.pages.library.views :as library.views]
            [main.pages.organization.views :as organization.views]
            [main.pages.var.views :as var.views]
            [main.routes.events]
            [main.routes.subs]
            [refx.alpha :as refx]
            [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(def routes
  ["/"
   [""
    {:view organization.views/home
     :name :routes/home-page}]

   [":organization"
    {:view organization.views/page
     :name :routes/organization
     :parameters {:path {:organization string?}}}]

   [":organization/:library"
    {:view library.views/page
     :name :routes/organization.library
     :parameters {:path {:organization string?
                         :library string?}}}]

   [":organization/:library/:definition"
    {:view definition.views/page
     :name :routes/organization.library.definition
     :parameters {:path {:organization string?
                         :library string?
                         :definition string?}}}]

   [":organization/:library/:definition/:var"
    {:view var.views/page
     :name :routes/organization.library.definition.var
     :parameters {:path {:organization string?
                         :library string?
                         :definition string?
                         :var string?}}}]])

(defn router []
  (rf/router routes {:data {:coercion rcm/coercion}}))

(defn on-navigate [new-match]
  (when new-match
    (refx/dispatch [:app.routes/navigated new-match])))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start! (router) on-navigate {:use-fragment true}))
