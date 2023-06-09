(ns main.pages.library.views
  (:require [clojure.string :as string]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [main.lib :refer [defnc]]
            [main.pages.library.events]
            [main.pages.library.subs]
            [refx.alpha :as refx]))

(defnc page [{:keys [route]}]
  (let [arguments (-> route :parameters :path)
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

     (for [lib-doc lib-docs]
       (let [file-extension (-> lib-doc :filename (string/split ".") last)
             file-type (or (:lang lib-doc) file-extension)]
         (d/section
          {:key (str (:organization arguments)
                     (:library arguments)
                     (:filename lib-doc)
                     file-type)}
          (d/h2 (d/a {:href (str "#/" (:definitions lib-doc))} (:name lib-doc)))
          (when (seq file-type)
            (d/small (str "langs: " (string/join ", " file-type))))
          (d/pre (:doc lib-doc))
          (d/p
           (d/a {:href (:git-source lib-doc)}
                (str (:filename lib-doc) ":" (:row lib-doc))))))))))
