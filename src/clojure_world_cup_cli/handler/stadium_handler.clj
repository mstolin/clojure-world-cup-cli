(ns clojure-world-cup-cli.handler.stadium-handler
    (:gen-class))

(defn print-info [stadium]
    (println
        (clojure.string/join "\n"
            [
             (format "%-10s: %s" "Name" (get stadium :name))
             (format "%-10s: %s" "City" (get stadium :city))
             (format "%-10s: %s, %s" "Coordinate" (get stadium :lat) (get stadium :lng))
            ])))

(defn get-first-by-id [stadiums id]
    (first 
        (filter #(= (:id %) id) stadiums)))

(defn get-first-by-name [stadiums name]
    (first 
        (filter #(= (:name %) name) stadiums)))
