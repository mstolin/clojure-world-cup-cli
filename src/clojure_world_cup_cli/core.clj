(ns clojure-world-cup-cli.core
  (:require [clojure-world-cup-cli.handler.group-handler :as group-handler]
            [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
            [clojure-world-cup-cli.handler.team-handler :as team-handler]
            [clojure-world-cup-cli.handler.match-handler :as match-handler]
            [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as client]
            [cheshire.core :refer :all])
  (:gen-class))



(def cli-options
  [["-n" "--name NAME" "The name of a specific group, team or stadium"
    :default false]
   ["-i" "--id ID" "The id of a specific team or stadium"
    :parse-fn #(Integer/parseInt %)]
   ["-h" "--help" "You are using this option right now :)"]])

(defn download-world-cup "" []
  (:body
    (client/get "https://raw.githubusercontent.com/mstolin/fifa-worldcup-2018/master/data.json" {:as :json})))

(defn validate-args
  ""
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      ;; The help summary
      (:help options) {:summary summary}
      ;; The actions
      (and (= 1 (count arguments))
           (#{"group" "team" "stadium" "knockout" "round-of-16" "quarter-finals" "semi-finals" "play-off" "final"} (first arguments)))
      {:action (first arguments) :options options}
      ;; The error message
      errors {:summary summary})))

(defn print-help [message]
  (println 
    (clojure.string/join "\n" 
      ["A simple command line interface for the fifa world cup 2018."
       ""
       "Usage: cup COMMAND [option]"
       ""
       "Options:"
       message
       ""
       "Commands:"
       (format "  %-10s %s" "group" "Shows a specific group (a - h)")
       (format "  %-10s %s" "stadium" "Shows a specific stadium")
       (format "  %-10s %s" "team" "Shows a specific team")
       ""])))
    
(defn show-group [options groups teams stadiums]
  (let [{:keys [name]} options]
    (if-let [group (get groups (keyword name))]
      (let [{:keys [name winner runnerup matches]} group]
        (do
          (group-handler/print-name name)
          (group-handler/print-stats teams matches)))
      (println "No such group"))))
      

(defn show-team [options teams groups stadiums]
  (let [{:keys [name id]} options]
    (if-let [team 
              (cond
                (string? name) (team-handler/get-first-by-name teams name)
                (integer? id) (team-handler/get-first-by-id teams id)
                :else nil)]
      (do 
        (team-handler/print-team team)
        (match-handler/print-matches 
          (get (group-handler/get-by-name groups (get team :group)) :matches) 
          teams 
          stadiums))
      (println "Please provide a valid name or id."))))

(defn show-stadium [options stadiums]
  (let [{:keys [name id]} options]
    (if-let [stadium 
              (cond
                (string? name) (stadium-handler/get-first-by-name stadiums name)
                (integer? id) (stadium-handler/get-first-by-id stadiums id)
                :else nil)]
      (stadium-handler/print-info stadium)
      (println "Please provide a valid name or id."))))

(defn show-knockout [options which knockout teams stadiums]
  (cond
    (= which :all) (print "ALL")
    :else (match-handler/print-matches
            (get (get knockout which) :matches)
            teams
            stadiums)))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (let [{:keys [stadiums groups teams knockout]} (download-world-cup)]
        (case action
          "group" (show-group options groups teams stadiums)
          "knockout" (show-knockout options :all knockout teams stadiums)
          "round-of-16" (show-knockout options :round_16 knockout teams stadiums)
          "quarter-finals" (show-knockout options :round_8 knockout teams stadiums)
          "semifinals" (show-knockout options :round_4 knockout teams stadiums)
          "play-off" (show-knockout options :round_2_loser knockout teams stadiums)
          "final" (show-knockout options :round_2 knockout teams stadiums)
          "team" (show-team options teams groups stadiums)
          "stadium" (show-stadium options stadiums))))))
