(ns main.pages.var.views
  (:require [clojure.string :as str]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.var.events]
            [main.pages.var.subs]
            [refx.alpha :as refx]))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        loading? (refx/use-sub [:app.var/loading])
        [error error-res] (refx/use-sub [:app.var/error])
        var-doc (refx/use-sub [:app.var/docs])]

    (hooks/use-effect
      [arguments]
      (refx/dispatch [:app.var/get (str (:organization arguments)
                                        "/"
                                        (:library arguments)
                                        "/"
                                        (:definition arguments)
                                        ".json")

                      (:var arguments)]))

    (d/div
     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (d/section
      {:key ""}
      (d/h2 (:name var-doc))
      (d/p
       (d/small
        (when (:added var-doc) (str "since: " (:added var-doc) " ")))
       (d/small
        (d/a {:href (:git-source var-doc)}
             "(source)")))
      (d/ul
       (for [arglist (:arglist-strs var-doc)]
         (d/li
          {:key (str/join " " arglist)}
          (d/pre
           (str "(" (:name var-doc)  " " arglist ")")))))
      (d/pre (if (:doc var-doc) (:doc var-doc) "no-doc"))))))
