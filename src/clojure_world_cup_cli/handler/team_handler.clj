(ns clojure-world-cup-cli.handler.team-handler
    (:gen-class))

(defn get-first-by-id 
    "Returns the first team for the given id."
    [teams id]
    (first 
        (filter #(= (:id %) id) teams)))

(defn get-first-by-name 
    "Returns the first team for the given name"
    [teams name]
    (first 
        (filter #(= (:name %) name) teams)))

(defn print-team 
    "Prints all important data of a team.
    (The name, fifa code and the group)"
    [team]
    (println
        (clojure.string/join "\n"
            [
             (format "️️️%s %-10s: %s" (get team :emojiString) "Name" (get team :name))
             (format "⚽️ %-10s: %s" "Fifa Code" (get team :fifaCode))
             (format "⚽️ %-10s: %s" "Group" (clojure.string/upper-case (get team :group)))
            ])))