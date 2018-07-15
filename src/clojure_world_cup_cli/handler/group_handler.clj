(ns clojure-world-cup-cli.handler.group-handler
    (:gen-class))

(defn print-name [name]
    (println (clojure.string/upper-case name)))

(defn print-winner [winner runnerup]
    (println
        (clojure.string/join "\n" 
        [""
        (format "%s %-10s %s %s" "🥇" "Winner:" 
            (get winner :name) 
            (get winner :emojiString))
        (format "%s %-10s %s %s" "🥈" "Runner-Up:"
            (get runnerup :name) 
            (get runnerup :emojiString))
        ""
        ""])))

(defn get-group-by-team [groups team-id]
    (first 
        (filter #(= (:name %) team-id) groups)))
