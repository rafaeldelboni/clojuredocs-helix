(ns main.pages.definition.views
  (:require [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.definition.events]
            [main.pages.definition.subs]
            [refx.alpha :as refx]))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        loading? (refx/use-sub [:app.definition/loading])
        [error error-res] (refx/use-sub [:app.definition/error])
        def-docs (refx/use-sub [:app.definition/docs])]

    (hooks/use-effect
      [arguments]
      (refx/dispatch [:app.definition/get (str (:organization arguments)
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

     (for [def-doc def-docs]
       (let [id (str (:organization arguments)
                     (:library arguments)
                     (:definition arguments)
                     (:name def-doc)
                     (:row def-doc))]
         (d/section
          {:id id :key id}
          (d/h2
           (d/a {:href (str "#/"
                            (:organization arguments) "/"
                            (:library arguments) "/"
                            (:definition arguments) "/"
                            (:name def-doc)
                            (when-not (zero? (:index def-doc))
                              (str "/" (:index def-doc))))}
                (:name def-doc)))
          (d/pre (:doc def-doc))
          (d/p
           (d/a {:href (:git-source def-doc)}
                (str (:filename def-doc) ":" (:row def-doc))))
          (d/h6 (when (:author def-doc) (str "by: " (:author def-doc))))))))))

