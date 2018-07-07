(ns clojure-world-cup-cli.core
  (:require [clojure.tools.cli :refer [cli]])
  (:gen-class))

(defn -main [& args]
  (let [[opts args banner] (cli args
                                ["-h" "--help" "Print this help"
                                 :default false :flag true])]
    (when (:help opts)
      (println banner))))
