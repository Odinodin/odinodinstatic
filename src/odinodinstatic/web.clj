(ns odinodinstatic.web
  (:require [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [schema.core :as s]
            [mapdown.core :as mapdown]
            [odinodinstatic.util :refer [map-vals map-keys]]
            [odinodinstatic.highlight :as highlight]
            [odinodinstatic.validation :as v]
            [odinodinstatic.pages :as p]))

;; Globally enable Schema validation
(schema.core/set-fn-validation! true)

(defn prepare-pages [pages]
  "Highlights code blocks"
  ;; by sending a function and not a string containing HTML, stasis will only generate
  ;; pages on-demand, thus improving the development process
  (map-vals
    #(fn [_] (highlight/highlight-code-blocks %))
    pages))


(s/defn load-content :- {:blog-posts {v/Path v/BlogPost}} []
        {:blog-posts (->>
                       (mapdown/slurp-directory "resources/posts" #"\.md$")
                       (map-keys #(str/replace % #"\.md$" "")))})


(s/defn get-pages :- v/Routes []
        (-> (load-content)
            p/create-pages
            prepare-pages))



(defn get-assets []
  (assets/load-assets "public" [#".*"]))

(def app
  (optimus/wrap (stasis/serve-pages get-pages)
                get-assets
                optimizations/all
                serve-live-assets))

;; Export site to dist/
(def export-dir "dist")

(defn export []
  (let [assets (optimizations/all (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))