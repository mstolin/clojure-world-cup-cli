(ns clojure-world-cup-cli.core
  (:require [clojure.pprint :as pprint :refer [pprint print-table]]
            [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as client]
            [cheshire.core :refer :all])
  (:gen-class))

(def cli-options
  [["-n" "--name NAME" "The name of a specific group or team"
    :default false]
   ["-a" "--all" "List all items"]
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

(defn print-group [group]
  (let [{:keys [name winner runnerup matches]} group]
    (do
      (println
        (clojure.string/join "\n" 
          [(clojure.string/upper-case name)
          ""
          (format "%s %-10s %s" "🥇" "Winner:" winner)
          (format "%s %-10s %s" "🥈" "Runner-Up:" runnerup)
          ""
          ""]))
      (pprint matches)
      (println "MATCHES:")
      (print-table [:date :stadium] (reverse (sort-by :date matches))))))
    
(defn show-group [options groups]
  (let [{:keys [name all]} options]
    (if all
      (print-group (get groups :a))
      (println 
        (get groups (keyword name) "No such group")))))

(defn show-team [options teams]
  (let [{:keys [name all]} options]
    (if all
      (println teams)
      (if-let [team (filter #(= (:name %) name) teams)]
        (println team)
        (println "No such team")))))

(defn show-stadium [options stadiums]
  (let [{:keys [name all]} options]
    (if all
      (println stadiums)
      (if-let [stadium (filter #(= (:name %) name) stadiums)]
        (println stadium)
        (println "No such team")))))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (let [{:keys [stadiums groups teams knockout]} (download-world-cup)]
        (case action
          "group" (show-group options groups)
          "team" (show-team options teams)
          "stadium" (show-stadium options stadiums))))))
