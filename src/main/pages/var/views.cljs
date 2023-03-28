(ns main.pages.var.views
  (:require [helix.dom :as d]
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
                                        ".json")]))

    (d/div

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (println var-doc)

     (d/section
      {:key ""}
      (d/h2 (:name var-doc))
      (d/pre (:doc var-doc))
      (d/p
       (d/a {:href (:git-source var-doc)}
            (str (:filename var-doc) ":" (:row var-doc))))
      (d/h6 (when (:author var-doc) (str "by: " (:author var-doc))))))))
