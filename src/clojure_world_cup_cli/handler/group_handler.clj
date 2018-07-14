(ns clojure-world-cup-cli.handler.group-handler
    (:require [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
              [clojure-world-cup-cli.handler.team-handler :as team-handler]
              [clojure.pprint :as pprint :refer [pprint print-table]])
    (:gen-class))

(defn print-group-winner [group teams stadiums]
  (let [{:keys [name winner runnerup matches]} group]
    (println
        (clojure.string/join "\n" 
        [(clojure.string/upper-case name)
        ""
        (format "%s %-10s %s" "🥇" "Winner:" (get (team-handler/get-first-team teams winner) :emojiString))
        (format "%s %-10s %s" "🥈" "Runner-Up:" (get (team-handler/get-first-team teams runnerup) :emojiString))
        ""
        ""]))))

(defn print-group-matches [group teams stadiums]
    (let [{:keys [matches]} group]
        (do 
            (println "MATCHES:")
            (print-table ["Date" "Result" "Stadium"]
                (reverse 
                (for [keyVal matches] 
                    (hash-map 
                    "Date" (.format (java.text.SimpleDateFormat. "MMM d yyyy, h:mm a") (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX") (get keyVal :date)))
                    "Result" (if-let [home-team (team-handler/get-first-team teams (get keyVal :home_team))]
                                    (if-let [away-team (team-handler/get-first-team teams (get keyVal :away_team))]
                                        (format 
                                        "%-8s %s:%s %-8s"
                                        (get home-team :name)
                                        (get keyVal :home_result)
                                        (get keyVal :away_result)
                                        (get away-team :name)))
                                    "No results for this match given")
                    "Stadium" (if-let [stadium (stadium-handler/get-first-stadium stadiums (get keyVal :stadium))]
                                (format
                                    "%s (%s)"
                                    (get stadium :name)
                                    (get stadium :city))
                                "No such stadium"))))))))
