(defproject clojure-world-cup-cli "0.1.0-SNAPSHOT"
  :description "A world cup cli written in clojure."
  :url "https://github.com/mstolin/clojure-world-cup-cli"
  :license {:name "MIT"
            :url "https://github.com/mstolin/clojure-world-cup-cli/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot clojure-world-cup-cli.core
  :target-path "target"
  :profiles {:uberjar {:aot :all}}
  :bin { :name "cup" }
  :plugins [[lein-bin "0.3.4"]])
