(ns main.pages.home.views
  (:require [helix.dom :as d]
            [main.lib :refer [defnc]]
            [reitit.frontend.easy :as rfe]))

(defnc page []
  (d/div
   "Home page"
   (d/h1
    (d/a
     {:href (rfe/href :routes/organization
                      {:organization "org.clojure"})}
     "org.clojure"))))
