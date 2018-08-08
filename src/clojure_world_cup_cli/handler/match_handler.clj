(ns clojure-world-cup-cli.handler.match-handler
    (:require [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
              [clojure-world-cup-cli.handler.team-handler :as team-handler]
              [clojure.pprint :as pprint :refer [print-table]])
    (:gen-class))

(defn print-matches [matches teams stadiums]
    (print-table ["Date" "Result" "Stadium"]
        (reverse 
        (for [match matches] 
            (hash-map 
            "Date" (.format (java.text.SimpleDateFormat. "MMM d yyyy, h:mm a") (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX") (get match :date)))
            "Result" (if-let [home-team (team-handler/get-first-by-id teams (get match :home_team))]
                            (if-let [away-team (team-handler/get-first-by-id teams (get match :away_team))]
                                (format 
                                "%-8s %s:%s %-8s"
                                (get home-team :name)
                                (get match :home_result)
                                (get match :away_result)
                                (get away-team :name)))
                            "No results for this match given")
            "Stadium" (if-let [stadium (stadium-handler/get-first-by-id stadiums (get match :stadium))]
                        (format
                            "%s (%s)"
                            (get stadium :name)
                            (get stadium :city))
                        "No such stadium"))))))