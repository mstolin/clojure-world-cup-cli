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

(defn get-group-of-team [groups team-id]
    (println (into [] (map #(into [] (vals %)) groups))))
    ;(map [group groups]
    ;        (let [matches (get (last group) :matches)]
    ;            (if-let [match (first (filter #(= (:home_team %) team-id) matches))]
    ;               (last group)))))