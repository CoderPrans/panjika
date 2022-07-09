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
             :lapse 1
             :panjika (calc/for-dt date-now)})))


(defn set-time []
  (let [in-val (r/atom nil)
        setting (r/atom false)]
    (fn []
      [:div {:style {:display "flex" :justify-content "center" :margin "30px"}}
       (if @setting
         [:input.set-date {:type "datetime-local"
                           :value @in-val :step 1
                           :on-change (fn [e] (reset! in-val (.-value (.-target e))))}]
         [:p.set-date {:style {:margin 0}} (.toLocaleString (:date-now @store))])
       [:button.date-btn {:on-click (fn [e] (if @setting
                                              (do (reset! setting false)
                                                  (swap! store assoc
                                                         :date-now (js/Date. @in-val)))
                                              (reset! setting true)))}
        (if @setting "Submit" "Change")]])))


(defn progress [flt]
  [:div#progress-circle
   {:style {:background
            (str
             "radial-gradient(#082b3f 50%, transparent 51%),"
             "conic-gradient(transparent 0deg " (* 360 flt) "deg, #082b3f " (* 360 flt) "deg 360deg),"
             "conic-gradient(lightgreen 0deg, green 180deg, forestgreen 360deg)"
             )}} [:span (subs (str (.toFixed flt 2)) 2) "%"]])


(defn position [[txt fctr]]
  [:span {:style {:display "flex" :align-items "flex-start"
                  :justify-content "flex-end" :margin-bottom "7px"}}
   [:span {:style {:margin-right "12px"}} txt]
   (progress fctr)]
  )

(defn segment-view [strg keyw]
  [:div {:class "flex"} [:p strg]
   [:p (cond
         (= strg "Maasa: ") [:span {:style {:margin-right "42px"}}
                             (keyw (:panjika @store))]
         (= strg "Tithi: ") (position (keyw (:panjika @store)))
         :else [:p
                (let [{:keys [naks rashi]} (keyw (:panjika @store))]
                  [:p [:p (position naks)] [:p (position rashi)]])])]])



(defonce timer (js/setInterval
          #(let [up-date (js/Date.
                          (+ (* (:lapse @store) 1000) (.getTime (:date-now @store))))]
             (swap! store assoc
                    :date-now up-date
                    :panjika (calc/for-dt up-date)))
          1000))

(def lapse-map {1 "1 sec" 3600 "1 hr" 86400 "1 day" 2592000 "1 mnth"})


(defn time-lapse []
  [:div.time-lapse
   [:p {:on-click #(swap! store assoc :lapse
                          (case (@store :lapse)
                            1 3600
                            3600 86400
                            86400 2592000
                            2592000 1))}
    (lapse-map (:lapse @store))] " /sec"])
(defn main []
  #_(fn [])
  [:div#main-wrapper
   {:style {:background-image (let
                                  [fct (-> (:date-now @store)
                                           astronomy/MoonPhase (/ 12) 
                                           Math/ceil (- 15) Math/abs (/ 15))]
                                (str "linear-gradient(130deg,#000608 " (* fct 100) "%,#0b2f4f"))}}
   [:h2 {:style {:padding-right "128px" :text-align "center"}} "Panjika"]
   
   [time-lapse]

   [set-time]

   [chart/chart-component (:date-now @store)]

   [:div.wrapper
    [:div {:style {:margin-bottom "8px"}}
     (segment-view "Maasa: " :masa)]
    (segment-view "Tithi: " :tithi)
    [:p {:style {:padding-left "18px"}} ".........."]
    (segment-view "Chandra: " :moon)
    [:p {:style {:padding-left "18px"}} ".........."]
    (segment-view "Surya: " :sun)
    ]])


;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [main] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
