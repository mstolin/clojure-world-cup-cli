(defproject clojure-world-cup-cli "0.1.0-SNAPSHOT"
  :description "A world cup cli written in clojure."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [org.clojure/tools.cli "0.3.7"]]
  :main ^:skip-aot clojure-world-cup-cli.core
  :target-path "target"
  :profiles {:uberjar {:aot :all}}
  :bin { :name "cup" }
  :plugins [[lein-bin "0.3.4"]])
