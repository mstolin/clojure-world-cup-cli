(ns clojure-world-cup-cli.handler.match-handler
    (:require [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
              [clojure-world-cup-cli.handler.team-handler :as team-handler]
              [clojure.pprint :as pprint :refer [print-table]])
    (:gen-class))

(defn print-matches 
    "This function prints all important data of a match:
    Formatted Date, Result and the Stadium"
    [matches teams stadiums]
    (print-table ["Date" "Result" "Stadium"]
        (for [match matches] 
            (hash-map 
            "Date" (format "%-22s" 
                        (.format 
                            (java.text.SimpleDateFormat. "MMM d yyyy, h:mm a") 
                            (.parse 
                                (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX") 
                                (get match :date))))
            "Result" (format "%-26s" 
                        (if-let [home-team (team-handler/get-first-by-id teams (get match :home_team))]
                            (if-let [away-team (team-handler/get-first-by-id teams (get match :away_team))]
                                (format 
                                "%-8s %s:%s %-8s"
                                (get home-team :name)
                                (get match :home_result)
                                (get match :away_result)
                                (get away-team :name)))
                            "No results for this match given"))
            "Stadium" (format "%-42s" 
                        (if-let [stadium (stadium-handler/get-first-by-id stadiums (get match :stadium))]
                            (format
                                "%s (%s)"
                                (get stadium :name)
                                (get stadium :city))
                            "No such stadium"))))))

(defn get-matches-for-stadium [matches stadium-id]
    (filter #(= (:stadium %) stadium-id) matches))

(defn get-all-for-stadium [groups knockout stadium-id]
    (remove nil?
        (sort-by :date
            (concat
                (get-matches-for-stadium (get (get groups :a) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :b) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :c) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :d) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :e) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :f) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :g) :matches) stadium-id)
                (get-matches-for-stadium (get (get groups :h) :matches) stadium-id)
                (get-matches-for-stadium (get (get knockout :round_16) :matches) stadium-id)
                (get-matches-for-stadium (get (get knockout :round_8) :matches) stadium-id)
                (get-matches-for-stadium (get (get knockout :round_4) :matches) stadium-id)
                (get-matches-for-stadium (get (get knockout :round_2_loser) :matches) stadium-id)
                (get-matches-for-stadium (get (get knockout :round_2) :matches) stadium-id)))))