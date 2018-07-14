(ns clojure-world-cup-cli.handler.team-handler
    (:gen-class))

(defn get-first-team-by-id [teams id]
    (first 
        (filter #(= (:id %) id) teams)))

(defn get-first-team-by-name [teams name]
    (first 
        (filter #(= (:name %) name) teams)))

(defn print-team [team]
    (do
        (println 
            (format "%s %s"
                (get team :emojiString)
                (get team :name)))))