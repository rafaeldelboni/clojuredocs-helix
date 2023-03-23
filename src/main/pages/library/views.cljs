(ns main.pages.library.views
  (:require [clojure.string :as string]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.library.events]
            [main.pages.library.subs]
            [refx.alpha :as refx]))

;; todo use state to get this info and not this
(defn ->git [arguments]
  (case (:library arguments)
    "clojure" "https://github.com/clojure/clojure/tree/clojure-1.11.1/src/clj/"
    "https://github.com/clojure/clojurescript/tree/r1.11.60/src/main/clojure/"))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
        base-git (->git arguments)
        loading? (refx/use-sub [:app.library/loading])
        [error error-res] (refx/use-sub [:app.library/error])
        lib-docs (refx/use-sub [:app.library/docs])]

    (hooks/use-effect
      [arguments]
      (refx/dispatch [:app.library/get (str (:organization arguments)
                                            "/"
                                            (:library arguments)
                                            ".json")]))

    (d/div

     (when error
       (d/div (str "Error: " error-res)))

     (when loading?
       (d/div "Loading... "))

     (for [ns-doc lib-docs]
       (let [file-extension (-> ns-doc :filename (string/split ".") last)
             file-type (or (:lang ns-doc) file-extension)]
         (d/section
          {:key (str (:organization arguments)
                     (:library arguments)
                     (:filename ns-doc)
                     file-type)}
          (d/h2
           (d/a {:href (str "#/" (:var-definitions ns-doc))}
                (str (:name ns-doc) " (" file-type ")")))
          (d/pre (:doc ns-doc))
          (d/p (d/a {:href (str base-git (:filename ns-doc))} (:filename ns-doc)))
          (d/h6 (when (:author ns-doc) (str "by: " (:author ns-doc))))))))))

