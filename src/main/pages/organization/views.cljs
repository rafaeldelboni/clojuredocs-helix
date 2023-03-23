(ns main.pages.organization.views
  (:require [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.organization.events]
            [main.pages.organization.subs]
            [refx.alpha :as refx]))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        loading? (refx/use-sub [:app.organization/loading])
        [error error-res] (refx/use-sub [:app.organization/error])
        org-docs (refx/use-sub [:app.organization/docs])]

    (hooks/use-effect
      [arguments]
      (refx/dispatch [:app.organization/get (str (:organization arguments) ".json")]))

    (d/div

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (for [org-doc org-docs]
       (d/section
        {:key (str (:organization arguments) (:name org-doc))}
        (d/h2
         (d/a {:href (str "#/" (:var-definitions org-doc))} (:name org-doc)))
        (d/pre (:doc org-doc))
        (d/h6 (str "version: " (:version org-doc))))))))
