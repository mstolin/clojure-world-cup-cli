(ns clojure-world-cup-cli.handler.group-handler
    (:require [clojure.pprint :as pprint :refer [print-table]])
    (:gen-class))

(defn print-name [name]
    (println (clojure.string/upper-case name)))

(defn print-winner [winner runnerup]
    (println
        (clojure.string/join "\n" 
        [""
        (format "%s %-10s %s %s" "🥇" "Winner:" 
            (get winner :name) 
            (get winner :emojiString))
        (format "%s %-10s %s %s" "🥈" "Runner-Up:"
            (get runnerup :name) 
            (get runnerup :emojiString))
        ""
        ""])))

(defn get-by-name [groups name]
    (get groups (keyword name)))

(defn get-games [matches team-id]
    (filter
        (some-fn #(= (:home_team %) team-id)
                 #(= (:away_team %) team-id)) matches))

(defn print-stats [matches]
    (print-table ["Nr." "Name" "Games" "Wins" "Draw" "Losses" "Goals" "Points"]
        [{"Nr." 1 "Name" "Torben" "Games" (count (get-games matches 1)) "Wins" 1 "Draw" 5 "Losses" 45 "Goals" 234 "Points" 234}]))
