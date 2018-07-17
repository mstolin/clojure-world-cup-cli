(ns clojure-world-cup-cli.handler.team-handler
    (:gen-class))

(defn get-first-by-id [teams id]
    (first 
        (filter #(= (:id %) id) teams)))

(defn get-first-by-name [teams name]
    (first 
        (filter #(= (:name %) name) teams)))

(defn print-team [team]
    (println
        (clojure.string/join "\n"
            [
             (format "️️️%s %-10s: %s" (get team :emojiString) "Name" (get team :name))
             (format "⚽️ %-10s: %s" "Fifa Code" (get team :fifaCode))
             (format "⚽️ %-10s: %s" "Group" (clojure.string/upper-case (get team :group)))
            ])))

(defn get-group-of-team [groups team-id]
    (println (into [] (map #(into [] (vals %)) groups))))
    ;(conj [group groups]
    ;        (let [matches (get (last group) :matches)]
    ;            (if-let [match (first (filter #(= (:home_team %) team-id) matches))]
    ;               (last group)))))