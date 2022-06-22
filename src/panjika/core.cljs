(ns panjika.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ["astronomy-engine" :as astronomy]
      [panjika.calc :as calc]
      [panjika.chart :as chart]))

;; -------------------------
;; Views


(defonce store
  (let [date-now (js/Date.)]
    (r/atom {:date-now date-now
             :panjika (calc/for-dt date-now)})))


(defn set-time []
  (let [in-val (r/atom nil)]
    (fn []
      [:div {:style {:display "flex" :justify-content "center" :margin "30px"}}
       [:input.set-date {:type "datetime-local"
                         :value @in-val
                         :on-change (fn [e] (reset! in-val (.-value (.-target e))))}]
       [:button.date-btn {:on-click (fn [e] (swap! store assoc
                                                   :date-now (js/Date. @in-val)))
                          :disabled (nil? @in-val)} "Change"]])))


(defn progress [flt]
  [:div#progress-circle
   {:style {:background
            (str
             "radial-gradient(#082b3f 50%, transparent 51%),"
             "conic-gradient(transparent 0deg " (* 360 flt) "deg, #082b3f " (* 360 flt) "deg 360deg),"
             "conic-gradient(#f4f6f0 0deg, #f4f6f0 360deg)"
             )}} [:span (subs (str (.toFixed flt 2)) 2) "%"]])


(defn segment-view [strg keyw]
  [:div {:class "flex"} [:p strg]
   [:p (if (= strg "Maasa: ")
         [:span {:style {:margin-right "42px"}}
          (keyw (:panjika @store))]
         (let [[txt fctr] (keyw (:panjika @store))]
           [:span {:style {:display "flex" :align-items "center"}}
            [:span {:style {:margin-right "12px"}} txt]
            (progress fctr)]))]])


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

     [chart/chart-component (:date-now @store)]

     [:div.wrapper
      [:div {:style {:margin-bottom "8px"}}
       (segment-view "Maasa: " :masa)]
      (segment-view "Tithi: " :tithi)
      (segment-view "Naksh: " :naks)
      (segment-view "Rashi: " :rashi)
      ]]))


;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [main] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
