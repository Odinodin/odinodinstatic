(ns odinodinstatic.web
  (:require [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [mapdown.core :as mapdown]
            [odinodinstatic.util :refer [fmap]]
            [odinodinstatic.highlight :as highlight]
            [odinodinstatic.validation :as validation]
            [odinodinstatic.pages :as pages]))

(defn prepare-pages [pages]
  "Highlights code blocks"
  ;; by sending a function and not a string containing HTML, stasis will only generate
  ;; pages on-demand, thus improving the development process
  (fmap
    #(fn [_] (highlight/highlight-code-blocks %))
    pages))

(defn get-assets []
  (assets/load-assets "public" [#".*"]))

(defn load-content []
  {:blog-posts (fmap :body (mapdown/slurp-directory "resources/posts" #"\.md$"))})

(defn get-pages []
  (-> (load-content)
      ;validation/validate-content
      pages/create-pages
      prepare-pages))

(def app
  (optimus/wrap (stasis/serve-pages get-pages)
                get-assets
                optimizations/all
                serve-live-assets))

;; Export site
(def export-dir "dist")

(defn export []
  (let [assets (optimizations/all (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))