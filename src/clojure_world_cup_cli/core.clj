(ns clojure-world-cup-cli.core
  (:require [clojure-world-cup-cli.handler.group-handler :as group-handler]
            [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
            [clojure-world-cup-cli.handler.team-handler :as team-handler]
            [clojure.pprint :as pprint :refer [pprint print-table]]
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

(defn print-group [group teams stadiums]
  (let [{:keys [name winner runnerup matches]} group]
    (do
      (println
        (clojure.string/join "\n" 
          [(clojure.string/upper-case name)
          ""
          (format "%s %-10s %s" "🥇" "Winner:" (get (team-handler/get-first-team teams winner) :emojiString))
          (format "%s %-10s %s" "🥈" "Runner-Up:" (get (team-handler/get-first-team teams runnerup) :emojiString))
          ""
          ""]))
      (println "MATCHES:")
      (print-table ["Date" "Result" "Stadium"]
        (reverse 
          (for [keyVal matches] 
            (hash-map 
              "Date" (.format (java.text.SimpleDateFormat. "MMM d yyyy, h:mm a") (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX") (get keyVal :date)))
              "Result" (format 
                        "%s %s:%s %s"
                        (get (team-handler/get-first-team teams (get keyVal :home_team)) :emojiString)
                        (get keyVal :home_result)
                        (get keyVal :away_result)
                        (get (team-handler/get-first-team teams (get keyVal :away_team)) :emojiString))
              "Stadium" (let [stadium (stadium-handler/get-first-stadium stadiums (get keyVal :stadium))]
                          (format
                            "%s (%s)"
                            (get stadium :name)
                            (get stadium :city))))))))))
    
(defn show-group [options groups teams stadiums]
  (let [{:keys [name]} options]
    (if-let [group (get groups (keyword name))]
      (print-group group teams stadiums)
      (println "No such group"))))

(defn show-team [options teams]
  (let [{:keys [name all]} options]
    (if-let [team (filter #(= (:name %) name) teams)]
      (println team)
      (println "No such team"))))

(defn show-stadium [options stadiums]
  (let [{:keys [name]} options]
    (if-let [stadium (filter #(= (:name %) name) stadiums)]
      (println stadium)
      (println "No such team"))))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (let [{:keys [stadiums groups teams knockout]} (download-world-cup)]
        (case action
          "group" (show-group options groups teams stadiums)
          "team" (show-team options teams)
          "stadium" (show-stadium options stadiums))))))
