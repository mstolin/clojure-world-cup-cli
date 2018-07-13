(ns clojure-world-cup-cli.handler.team-handler
    (:gen-class))

(defn get-first-team [teams id]
    (first 
        (filter #(= (:id %) id) teams)))