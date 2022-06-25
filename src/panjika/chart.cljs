(ns panjika.chart
  (:require
   [reagent.core :as r]
   ["recharts" :as re]
   ["astronomy-engine" :as astronomy]
   [panjika.calc :as calc]))

(defn get-data [body dt]
  (let [observer (new astronomy/Observer 75.78 23.17 0)
        eq #(let [{:keys [ra dec]}
                  (calc/js-parse (astronomy/Equator % dt observer false false))]
              {:ra ra :dec dec})]
    #js {"z" (if (= body "Moon") 50 1000)
         "ra" (int (* (/ 360 24)
                      (:ra (eq body))))
         "dec" (:dec (eq body))}))

(get-data "Moon" (js/Date.))
;; => #js {:body "Moon", :ra 1.3732008359451153, :dec 4.868361638408829}

(defn get-edata [body dt]
  #js {"body" body
       "lon" (:lon (calc/eclipticCoor body dt))
       "lat" (:lat (calc/eclipticCoor body dt))})

(def tick-style
  {:fontSize "12px"
   :color "silver"})

(defn format-tick [label]
  (str label "°"))

(defn chart-component [dt]
  [:div
   {:style {:width "320px" :height "120px" :margin "10px auto"}}
   [:> re/ScatterChart {:width 320 :height 120
                        :margin {:top 0 :right 30 :left -30 :bottom 0}}
    [:> re/XAxis {:type "number" :domain #js [0 360] :dataKey "ra"
                  :name "Right Ascension" :tick tick-style :tickFormatter #(format-tick %)}]
    [:> re/YAxis {:type "number" :domain #js [-30 30] :dataKey "dec"
                  :name "Declination" :ticks [0] :tick tick-style :tickFormatter #(format-tick %)}]
    [:> re/ZAxis {:dataKey "z" :range [50 300]}]
    [:> re/Scatter {:name "Surya" :data #js [(get-data "Sun" dt)] :fill "yellow"}]
    [:> re/Scatter {:name "Chandra" :data #js [(get-data "Moon" dt)] :fill "#fefcd7"}]
    ]
   ])

(get-data "Moon" (js/Date.))
(get-data "Sun" (js/Date.))
