(ns panjika.chart
  (:require
   [reagent.core :as r]
   ["recharts" :as re]
   ["astronomy-engine" :as astronomy]
   [panjika.calc :as calc]))

(defn get-data [body dt]
  (let [observer (new astronomy/Observer 23.17 75.78 0)
        eq #(let [{:keys [ra dec]}
                  (calc/js-parse (astronomy/Equator % dt observer true false))]
              {:ra ra :dec dec})]
    #js {"z" (if (= body "Moon") 100 1000)
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

(defn format-raasi [label]
  (case label
    24  "0°Ari"
    144 "0°Leo"
    264 "0°Sag"))

(def raasi-vals
  (mapv #(+ calc/ayanaamsa %) [0 120 240]))

(def ecliptic-line
  (conj
   (mapv #(get-data "Sun" (js/Date. 2000 % 21))
         (conj (vec (range 2 12)) 1))
   (get-data "Sun" (js/Date. 2000 2 20))))

(defn chart-component [dt]
  [:div
   {:style {:width "370px" :height "180px" :margin "10px auto"}}
   [:> re/ComposedChart {:width 370 :height 180
                         :margin {:top 0 :right 30 :left -30 :bottom 0}
                         :data (clj->js ecliptic-line)}
    [:> re/XAxis {:type "number" :domain #js [0 360] :dataKey "ra" :xAxisId 0
                  :tick tick-style :tickFormatter #(format-tick %)}]
    [:> re/XAxis {:type "number" :domain #js [0 360] :xAxisId 1 :orientation "top" :tickMargin 6 :axisLine nil
                  :tick (assoc tick-style :dx 15) :ticks raasi-vals :tickFormatter #(format-raasi %)}]
    [:> re/YAxis {:type "number" :domain #js [-35 35] :dataKey "dec"
                  :ticks [0] :tick tick-style :tickFormatter #(format-tick %)}]
    [:> re/ZAxis {:dataKey "z" :range [100 300]}]
    [:> re/Line {:type "monotone" :dataKey "dec" :stroke "darkslategray"}]
    [:> re/ReferenceLine {:x (mod (+ 180 (:ra (calc/js-parse (get-data "Sun" dt)))) 360)
                          :stroke "#f3f3f3" :label {:fill "#fefcd7" :value "Full Moon"
                                                    :fontSize "9px" :position "insideTop"}
                          :strokeDasharray "3 3"}]
    [:> re/Scatter {:name "Surya" :data #js [(get-data "Sun" dt)] :fill "yellow"}]
    [:> re/Scatter {:name "Chandra" :data #js [(get-data "Moon" dt)] :fill "#fefcd7"}]
    ]
   ])

