(ns panjika.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ["astronomy-engine" :as astronomy]
      [panjika.calc :as calc]))

;; -------------------------
;; Views

(defn home-page []
  [:div {:class "wrapper" :style {:padding "0 20px"}}
   [:h2 "Panjika"]
   [:div {:class "flex"} [:p "Tithi: "]
    [:p (calc/get-tithi (js/Date.))]]
   [:div {:class "flex"} [:p "Naksh: "]
    [:p (calc/get-nakshatra (js/Date.))]]
   [:div {:class "flex"} [:p "Rashi: "]
    [:p (calc/get-rashi (js/Date.))]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
