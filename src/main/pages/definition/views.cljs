(ns main.pages.definition.views
  (:require [clojure.string :as string]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.definition.events]
            [main.pages.definition.subs]
            [refx.alpha :as refx]))

;; todo use state to get this info and not this
(defn ->git [arguments]
  (case (:library arguments)
    "clojure" "https://github.com/clojure/clojure/tree/clojure-1.11.1/src/clj/"
    "https://github.com/clojure/clojurescript/tree/r1.11.60/src/main/clojure/"))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        base-git (->git arguments)
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
       (let [file-extension (-> def-doc :filename (string/split ".") last)
             file-type (or (:lang def-doc) file-extension)]
         (d/section
          {:key (str (:organization arguments)
                     (:library arguments)
                     (:definition arguments)
                     (:filename def-doc)
                     (:name def-doc)
                     (:row def-doc)
                     file-type)}
          (d/h2 (:name def-doc))
          (d/pre (:doc def-doc))
          (d/p
           (d/a {:href (str base-git (:filename def-doc) "#L" (:row def-doc))}
                (str (:filename def-doc) ":" (:row def-doc))))
          (d/h6 (when (:author def-doc) (str "by: " (:author def-doc))))))))))
