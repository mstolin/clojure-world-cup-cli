(ns clojure-world-cup-cli.handler.stadium-handler
    (:gen-class))

(defn get-first-stadium [stadiums id]
    (first 
        (filter #(= (:id %) id) stadiums)))
