(ns panjika.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ["astronomy-engine" :as astronomy]
      [panjika.calc :as calc]))

;; -------------------------
;; Views

(def date-now (r/atom (js/Date.)))

(defn main []
  (fn []
    (js/setTimeout #(reset! date-now (js/Date.)) 2000)
    [:div.wrapper
     [:div {:class "flex"} [:p "Tithi: "]
      [:p (calc/get-tithi @date-now)]]]))

(defn main-rest []
  [:div.wrapper
   [:div {:class "flex"} [:p "Naksh: "]
    [:p (calc/get-nakshatra @date-now)]]
   [:div {:class "flex"} [:p "Rashi: "]
    [:p (calc/get-rashi @date-now)]]])

(defn tithi-until []
  (let [dt (js/Date.)
        tthi (calc/get-tithi (js/Date.))]
    [:div
     [:div {:style {:text-align "right"
                    :margin-right "20px"
                    :font-size "12px"}}
      [:span "until "]
      (subs (str (calc/next-tithi dt)) 4 21)]]))


(defn home-page []
  [:div
   [:h2 {:style {:padding-left "20px"}} "Panjika"]
   [:p {:style {:text-align "center"}} (calc/get-masa) " Masa"]
   [main]
   [tithi-until]
   [main-rest]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
