(ns clojure-world-cup-cli.core-test
  (:require [clojure.test :refer :all]
            [clojure-world-cup-cli.core :refer :all]
            [clojure-world-cup-cli.handler.stadium-handler :as stadium-handler]
            [clojure-world-cup-cli.handler.team-handler :as team-handler]
            [clojure-world-cup-cli.handler.group-handler :as group-handler]))

;;;; Test data

(def stadiums [
  {:id 1 :name "Luzhniki Stadium" :city "Moscow" :lat 55.715765 :lng 37.5515217 :image "https://upload.wikimedia.org/wikipedia/commons/e/e6/Luzhniki_Stadium%2C_Moscow.jpg"}
  {:id 2 :name "Otkrytiye Arena" :city "Moscow" :lat 55.817765 :lng 37.440363 :image "https://upload.wikimedia.org/wikipedia/commons/5/50/Stadium_Spartak_in_Moscow.jpg"}])

(def teams [
  {:id 1 :group "a" :name "Russia" :fifaCode "RUS" :iso2 "ru" :flag "https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/900px-Flag_of_Russia.png" :emoji "flag-ru" :emojiString "🇷🇺"} 
  {:id 2 :group "a" :name "Saudi Arabia" :fifaCode "KSA" :iso2 "sa" :flag "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Flag_of_Saudi_Arabia.svg/750px-Flag_of_Saudi_Arabia.png" :emoji "flag-sa" :emojiString "🇸🇦"} 
  {:id 3 :group "a" :name "Egypt" :fifaCode "EGY" :iso2 "eg" :flag "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/900px-Flag_of_Egypt.png" :emoji "flag-eg" :emojiString "🇪🇬"} 
  {:id 4 :group "a" :name "Uruguay" :fifaCode "URU" :iso2 "uy" :flag "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Uruguay.svg/900px-Flag_of_Uruguay.png" :emoji "flag-uy" :emojiString "🇺🇾"} 
  {:id 5 :group "b" :name "Portugal" :fifaCode "POR" :iso2 "pt" :flag "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Portugal.svg/600px-Flag_of_Portugal.png" :emoji "flag-pt" :emojiString "🇵🇹"} 
  {:id 6 :group "b" :name "Spain" :fifaCode "ESP" :iso2 "es" :flag "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Flag_of_Spain.svg/750px-Flag_of_Spain.png" :emoji "flag-es" :emojiString "🇪🇸"}])

(def groups {
  :a {
    :name "Group A" 
    :winner 4
    :runnerup 1 
    :matches [
      {:date "2018-06-14T18:00:00+03:00" :channels [4 6 13 17 20 22] :name 1 :away_result 0 :matchday 1 :stadium 1 :type "group" :finished true :home_team 1 :away_team 2 :home_result 5} 
      {:date "2018-06-15T17:00:00+05:00" :channels [3 6 14 17 20 22] :name 2 :away_result 1 :matchday 1 :stadium 12 :type "group" :finished true :home_team 3 :away_team 4 :home_result 0} 
      {:date "2018-06-19T21:00:00+03:00" :channels [3 6 13 17 15 21 22] :name 17 :away_result 1 :matchday 2 :stadium 3 :type "group" :finished true :home_team 1 :away_team 3 :home_result 3} 
      {:date "2018-06-20T18:00:00+03:00" :channels [3 6 13 17 21 22] :name 18 :away_result 0 :matchday 2 :stadium 10 :type "group" :finished true :home_team 4 :away_team 2 :home_result 1} 
      {:date "2018-06-25T18:00:00+04:00" :channels [4 6 13 18 15 20 22] :name 33 :away_result 0 :matchday 3 :stadium 7 :type "group" :finished true :home_team 4 :away_team 1 :home_result 3} 
      {:date "2018-06-25T17:00:00+03:00" :channels [5 6 14 18 15 21 22] :name 34 :away_result 1 :matchday 3 :stadium 8 :type "group" :finished true :home_team 2 :away_team 3 :home_result 2}]}})

;;;; Stadium tests

(deftest get-stadium-by-name
  (testing "Get the first stadium by name"
    (is (= 
      (stadium-handler/get-first-by-name stadiums "Otkrytiye Arena")
      {:id 2 :name "Otkrytiye Arena" :city "Moscow" :lat 55.817765 :lng 37.440363 :image "https://upload.wikimedia.org/wikipedia/commons/5/50/Stadium_Spartak_in_Moscow.jpg"}))))

(deftest get-stadium-by-id
  (testing "Get the first stadium by id"
    (is (= 
      (stadium-handler/get-first-by-id stadiums 1)
      {:id 1 :name "Luzhniki Stadium" :city "Moscow" :lat 55.715765 :lng 37.5515217 :image "https://upload.wikimedia.org/wikipedia/commons/e/e6/Luzhniki_Stadium%2C_Moscow.jpg"}))))

;;;; Team tests

(deftest get-team-by-name
  (testing "Get the first team by name"
    (is (= 
      (team-handler/get-first-by-name teams "Egypt")
      {:id 3 :group "a" :name "Egypt" :fifaCode "EGY" :iso2 "eg" :flag "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/900px-Flag_of_Egypt.png" :emoji "flag-eg" :emojiString "🇪🇬"}))))

(deftest get-team-by-id
  (testing "Get the first team by id"
    (is (= 
      (team-handler/get-first-by-id teams 6)
      {:id 6 :group "b" :name "Spain" :fifaCode "ESP" :iso2 "es" :flag "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Flag_of_Spain.svg/750px-Flag_of_Spain.png" :emoji "flag-es" :emojiString "🇪🇸"}))))

;;;; Group tests

(deftest get-group-by-name
  (testing "Get the group by name"
    (let [group-a (group-handler/get-by-name groups "a")]
      (do
        (is (= (get group-a :name) "Group A"))
        (is (= (get group-a :winner) 4))
        (is (= (get group-a :runnerup) 1))
        (is (= (count (get group-a :matches)) 6))))))

(deftest count-number-of-games
  (testing "Count the number of games"
    (is 
      (= 
        (count
          (group-handler/get-games 
            (get (get groups :a) :matches) 1)) 3))))

(deftest count-number-of-wins
  (testing "Count the number of wins"
    (is
      (=
        (count 
          (group-handler/get-wins 
            (get (get groups :a) :matches) 1)) 2))))

(deftest count-number-of-draws
  (testing "Count the number of draws"
    (is
      (=
        (count 
          (group-handler/get-draws 
            (get (get groups :a) :matches) 1)) 0))))

(deftest count-number-of-defeats
  (testing "Count the number of defeats"
    (is
      (=
        (count 
          (group-handler/get-defeats 
            (get (get groups :a) :matches) 1)) 1))))

(deftest count-number-of-teams
  (testing "Count the number of teams in a match"
    (is
      (=
        (count 
          (group-handler/get-all-teams 
            (get (get groups :a) :matches))) 4))))

(deftest count-number-of-goals
  (testing "Count the number of goals"
    (is
      (=
        (group-handler/get-goals 
          (get (get groups :a) :matches) 1)) 8)))
