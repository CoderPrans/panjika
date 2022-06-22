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
    #js {"body" body "ra" (:ra (eq body)) "dec" (:dec (eq body))}))

(get-data "Moon" (js/Date.))
;; => #js {:body "Moon", :ra 1.3732008359451153, :dec 4.868361638408829}

(def ScatterChart (r/adapt-react-class re/ScatterChart))
(def XAxis (r/adapt-react-class re/XAxis))
(def YAxis (r/adapt-react-class re/YAxis))
(def Scatter (r/adapt-react-class re/Scatter))
(def Cell (r/adapt-react-class re/Cell))

(defn chart-component [dt]
  [:div
   {:style {:width "400px"
            :height "150px"
            :margin "10px auto"}}
   [ScatterChart {:width 400 :height 150 :margin {:top 0 :right 0 :left 0 :bottom 0}}
    [XAxis {:type "number" :domain #js [0 24] :dataKey "ra" :name "Right Ascension" :unit "hr"}]
    [YAxis {:type "number" :domain #js [-25 25] :dataKey "dec" :name "Declination" :unit "deg"}]
    [Scatter {:name "A school" :data (map #(get-data % dt) ["Moon" "Sun"])}
     (map (fn [en in]
            [Cell {:key (str "cell-" in)
                   :fill (if (= (.-body en) "Moon") "#fefcd7" "yellow")}])
          (map #(get-data % dt) ["Moon" "Sun"]))]]
   ])
(apply vector (map get-data ["Moon" "Sun"]))
;; => [#js {:body "Moon", :ra 1.4170938159308841, :dec 5.166615721476313} #js {:body "Sun", :ra 6.080449216825982, :dec 23.429428743207737}]




