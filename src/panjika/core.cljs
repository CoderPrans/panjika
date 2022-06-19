(ns panjika.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ["astronomy-engine" :as astronomy]
      [panjika.calc :as calc]))

;; -------------------------
;; Views


(defonce store
  (let [date-now (js/Date.)]
    (r/atom {:date-now date-now
             :panjika (calc/for-dt date-now)})))


(defn tithi-until []
  (let [dt (js/Date.)]
    [:div
     [:div {:style {:text-align "right"
                    :margin-right "20px"
                    :font-size "13px"}}
      [:span {:style {:font-size "11px"}} "until "]
      (subs (str (calc/next-tthi)) 4 21)]]))


(defn set-time []
  (let [in-val (r/atom nil)]
    (fn []
      [:div {:style {:display "flex" :justify-content "center" :margin "30px"}}
       [:input {:type "datetime-local"
                :value @in-val
                :on-change (fn [e] (reset! in-val (.-value (.-target e))))
                :style {:width "170px"}
                }]
       [:button {:style {:margin-left "10px"
                         :padding "3px 10px"}
                 :on-click (fn [e] (swap! store assoc
                                          :date-now (js/Date. @in-val)))
                 :disabled (nil? @in-val)} "Change"]])))


(defn main []
  (fn []
    (js/setTimeout
     #(let [up-date (js/Date.
                     (+ 2000 (.getTime (:date-now @store))))]
        (swap! store assoc
               :date-now up-date
               :panjika (calc/for-dt up-date)))
     2000)

    [:div
     [:h2 {:style {:padding-left "20px"}} "Panjika"]
     ;;"Draw a chart here.

     [set-time]

     [:p {:style {:text-align "center"}} (calc/get-masa) " Masa"]

     [:div.wrapper
      [:div {:class "flex"} [:p "Tithi: "]
       [:p (:tithi (:panjika @store))]]
      [tithi-until] [:br ]

      [:div {:class "flex"} [:p "Naksh: "]
       [:p (:naks (:panjika @store))]]
      [:div {:class "flex"} [:p "Rashi: "]
       [:p (:rashi (:panjika @store))]]
      ]]))


;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [main] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
