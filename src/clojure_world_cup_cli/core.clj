(ns clojure-world-cup-cli.core
  (:require [clojure.tools.cli :refer [parse-opts]]
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
           (#{"group", "team"} (first arguments)))
      {:action (first arguments) :options options}
      ;; The error message
      errors {:message "IRGENDWIE ERROR"})))

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
       (format "  %-10s %s" "group" "Shows a specific group")
       (format "  %-10s %s" "team" "Shows a specific team")
       ""])))

(defn show-group [options groups]
  (let [{:keys [name all]} options]
    (if all
      (println groups)
      (println 
        (get groups (keyword name) "No such group"))
      )))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (let [{:keys [stadiums groups teams knockout]} (download-world-cup)]
        (case action
          "group" (show-group options groups))))))
