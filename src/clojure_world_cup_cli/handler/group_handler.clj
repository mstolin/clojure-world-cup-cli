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

(defn get-goals [matches team-id]
    (reduce + 
        (map 
            #(if (= (:home_team %) team-id) 
                (:home_result %) (:away_result %)) matches)))

(defn print-stats [teams matches]
    (print-table [:country :games :wins :draws :losses :goals :points]
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
                            :points (* (count (get-wins (get-games matches %) %)) 3)) (get-all-teams matches))))))
