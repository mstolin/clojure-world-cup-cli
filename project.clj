(defproject clojure-world-cup-cli "0.1.0-SNAPSHOT"
  :description "A world cup cli written in clojure."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot clojure-world-cup-cli.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
