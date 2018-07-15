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
  [["-n" "--name NAME" "The name of a specific group or team"
    :default false]
   ;["-a" "--all" "List all items"]
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
           (#{"group", "team", "stadium"} (first arguments)))
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
       (format "  %-10s %s" "team" "Shows a specific team")
       ""])))
    
(defn show-group [options groups teams stadiums]
  (let [{:keys [name]} options]
    (if-let [group (get groups (keyword name))]
      (let [{:keys [name winner runnerup matches]} group]
        (do
          (group-handler/print-name name)
          (group-handler/print-winner
            (team-handler/get-first-team-by-id teams winner)
            (team-handler/get-first-team-by-id teams runnerup))
          (match-handler/print-matches matches teams stadiums)))
      (println "No such group"))))
      

(defn show-team [options teams groups]
  (let [{:keys [name all]} options]
    (if-let [team (team-handler/get-first-team-by-name teams name)]
      (do 
        (team-handler/print-team team)
        (team-handler/get-group-of-team groups (get team :id)))
      (println "No such team"))))

(defn show-stadium [options stadiums]
  (let [{:keys [name]} options]
    (if-let [stadium (stadium-handler/get-first-by-name stadiums name)]
      (stadium-handler/print-info stadium)
      (println "No such team"))))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (let [{:keys [stadiums groups teams knockout]} (download-world-cup)]
        (case action
          "group" (show-group options groups teams stadiums)
          "team" (show-team options teams groups)
          "stadium" (show-stadium options stadiums))))))
