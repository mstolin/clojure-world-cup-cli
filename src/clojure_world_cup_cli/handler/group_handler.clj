(ns clojure-world-cup-cli.handler.group-handler
    (:require [clojure.pprint :as pprint :refer [print-table]]
              [clojure-world-cup-cli.handler.team-handler :as team-handler])
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
        (some-fn
            #(= (:home_team %) team-id)
            #(= (:away_team %) team-id)) matches))

(defn get-wins [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (> home-result away-result))
                (and (= away-team team-id) (> away-result home-result)))) matches))

(defn get-losses [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (< home-result away-result))
                (and (= away-team team-id) (< away-result home-result)))) matches))

(defn get-draws [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (= home-result away-result))
                (and (= away-team team-id) (= away-result home-result)))) matches))

(defn get-all-teams [matches]
    (distinct 
        (map :home_team matches)))

(defn print-stats [teams matches]
    (print-table ["Name" "Games" "Wins" "Draws" "Losses"] ; "Goals" "Points" 
        (map 
            #(hash-map 
                "Name" (get (team-handler/get-first-by-id teams %) :name)
                "Games" (count (get-games matches %))
                "Wins" (count (get-wins (get-games matches %) %))
                "Draws" (count (get-draws (get-games matches %) %))
                "Losses" (count (get-losses (get-games matches %) %))) (get-all-teams matches))))
