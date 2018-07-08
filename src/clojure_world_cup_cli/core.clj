(ns clojure-world-cup-cli.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as client])
  (:gen-class))

(def cli-options
  [["-n" "--name NAME" "The name of a specific group"
    :default "A"]
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
           (#{"groups", "group"} (first arguments)))
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
       (format "  %-10s %s" "groups" "Shows all groups at once sorted alphabetically")
       ""])))

(defn show-groups []
  (println "ALL"))

(defn show-group [options]
  (let [{:keys [name]} options]
    (println name)))

(defn -main [& args]
  (let [{:keys [summary action options]} (validate-args args)]
    (if summary
      (print-help summary)
      (println (download-world-cup)))))

;(case action
;        "groups" (show-groups)
;        "group" (show-group options))