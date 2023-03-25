(ns main.pages.organization.views
  (:require [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.organization.events]
            [main.pages.organization.subs]
            [refx.alpha :as refx]))

(defnc home []
  (let [loading? (refx/use-sub [:app.organization/loading])
        [error error-res] (refx/use-sub [:app.organization/error])
        org-docs (refx/use-sub [:app.organization/docs])]

    (hooks/use-effect
      :once
      (refx/dispatch [:app.organization/get nil]))

    (d/div
     "Home page"

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (for [[group-name group-values] (group-by :organization org-docs)]
       (d/section
        {:key group-name}
        (d/h2
         (d/a {:href (str "#/" group-name)} group-name))
        (d/div
         (d/p (str "libraries: " (count group-values)))))))))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        loading? (refx/use-sub [:app.organization/loading])
        [error error-res] (refx/use-sub [:app.organization/error])
        org-docs (refx/use-sub [:app.organization/docs])]

    (hooks/use-effect
      [arguments]
      (refx/dispatch [:app.organization/get (:organization arguments)]))

    (d/div

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (for [{:keys [project library tag sha paths url]} org-docs]
       (d/section
          {:key library}
          (d/h2
           (d/a {:href (str "#/" project)} library))
          (d/div
           (d/p (str "version: " tag))
           (d/p (str "sha: " sha))
           (d/p (str "paths: " paths)))
          (d/a {:href url} (d/small "source")))))))


