(ns clojure-world-cup-cli.handler.group-handler
    (:require [clojure.pprint :as pprint :refer [print-table]]
              [clojure.set :refer [rename-keys]]
              [clojure-world-cup-cli.handler.team-handler :as team-handler])
    (:gen-class))

(defn print-name [name]
    (println (clojure.string/upper-case name)))

(defn print-winner
    "Prints the winner and the runnerup."
    [winner runnerup]
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

(defn get-by-name 
    "Returns the first group for the given name."
    [groups name]
    (get groups (keyword name)))

(defn get-games
    "Returns a vector of all games for the given team."
    [matches team-id]
    (filter
        (some-fn
            #(= (:home_team %) team-id)
            #(= (:away_team %) team-id)) matches))

(defn get-wins 
    "Returns a vector of all wins for the given team."
    [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (> home-result away-result))
                (and (= away-team team-id) (> away-result home-result)))) matches))

(defn get-losses 
    "Returns a vector of all losses for the given team."
    [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (< home-result away-result))
                (and (= away-team team-id) (< away-result home-result)))) matches))

(defn get-draws
    "Returns a vector of all draws for the given team."
    [matches team-id]
    (filter
        #(let [home-team (% :home_team) away-team (% :away_team) home-result (% :home_result) away-result (% :away_result)]
            (or
                (and (= home-team team-id) (= home-result away-result))
                (and (= away-team team-id) (= away-result home-result)))) matches))

(defn get-all-teams 
    "Returns a vector of all teams in a match."
    [matches]
    (->> matches
        (map :home_team)
        distinct))

(defn get-goals 
    "Returns a vector of all goals for the given team."
    [matches team-id]
    (reduce + 
        (map 
            #(if (= (:home_team %) team-id) 
                (:home_result %) (:away_result %)) matches)))

(defn print-stats 
    "Prints the stats of a match.
    For each team this function prints out the
    Country, number of Games in the match, number of wins, 
    number of losses, number of goals and the calculated points"
    [teams matches]
    (print-table ["Country" "Games" "Wins" "Draws" "Losses" "Goals" "Points"]
        (map #(rename-keys % {:country "Country" :games "Games" :wins "Wins" :draws "Draws" :losses "Losses" :goals "Goals" :points "Points"})
            (reverse 
                (sort-by 
                    (juxt :points :goals) 
                        (map 
                            #(hash-map 
                                :country (get (team-handler/get-first-by-id teams %) :name)
                                :games (count (get-games matches %))
                                :wins (count (get-wins (get-games matches %) %))
                                :draws (count (get-draws (get-games matches %) %))
                                :losses (count (get-losses (get-games matches %) %))
                                :goals (get-goals (get-games matches %) %)
                                :points (* (count (get-wins (get-games matches %) %)) 3)) (get-all-teams matches)))))))
